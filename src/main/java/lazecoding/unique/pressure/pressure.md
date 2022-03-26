# 压力数据

100W 数据下比较 UUID 和 Long 主键性能差距。

### 初始化

```sql
DROP TABLE IF EXISTS `pressure_uuid`;

CREATE TABLE `pressure_uuid` (
     `uid` varchar(32)  NOT NULL,
     `uname` varchar(255)  DEFAULT NULL,
     `soft` bigint(20) NOT NULL,
     `ctime` timestamp NOT NULL DEFAULT NOW(),
     PRIMARY KEY (`uid`)
) ENGINE=InnoDB;

ALTER TABLE pressure_uuid ADD INDEX i_uuid_uname (uname);
ALTER TABLE pressure_uuid ADD INDEX i_uuid_soft_id (soft,uid);
ALTER TABLE pressure_uuid ADD INDEX i_uuid_long_ctime_id (ctime,uid);

DROP TABLE IF EXISTS `pressure_long`;

CREATE TABLE `pressure_long` (
     `uid` bigint(20) NOT NULL,
     `uname` varchar(255)  DEFAULT NULL,
     `soft` bigint(20) NOT NULL,
     `ctime` timestamp NOT NULL DEFAULT NOW(),
     PRIMARY KEY (`uid`)
) ENGINE=InnoDB;

ALTER TABLE pressure_long ADD INDEX i_long_uname (uname);
ALTER TABLE pressure_long ADD INDEX i_long_soft_id (soft,uid);
ALTER TABLE pressure_long ADD INDEX i_long_ctime_id (ctime,uid);
```

### 占用和碎片

整理碎片前：

<div align="left">
    <img src="https://github.com/lazecoding/Unique/blob/master/src/main/resources/static/pressure/整理碎片前数据库表占用空间大小.png" width="600px">
</div>

整理碎片后：

<div align="left">
    <img src="https://github.com/lazecoding/Unique/blob/master/src/main/resources/static/pressure/整理碎片后数据库表占用空间大小.png" width="600px">
</div>

碎片率：

- 数值主键的是有序的，分布式场景下并发写入是趋势有序的，碎片率应该略高于单线程 Long 写入 `> 8.33%`。
- UUID 是离散的，碎片率 `= 167.69%`。

### 写入

单线程写入-UUID：

总耗时 57290255 ms；占用空间  380928 KB。

初始化             per 1000 cost-time: 27632 ms。
50W 数据时大致      per 1000 cost-time: 55000 ms。
100W 数据时大致     per 1000 cost-time: 80000 ms。
峰值               per 1000 cost-time: 167018 ms。

单线程写入-Long：

总耗时 27817726 ms；占用空间  159744 KB。

初始化              per 1000 cost-time: 24000 ms。
50W 数据时大致       per 1000 cost-time: 27000 ms。
100W 数据时大致      per 1000 cost-time: 29000 ms。
峰值                per 1000 cost-time: 33000 ms。

`long-cost:27817726 ms，uuid-cost:57290255，相差:29472529 ms，倍率：2.0594873。`

并发写入-UUID：

总耗时 362,5164 ms；占用空间 241664 KB。

并发写入-Long：

总耗时 1718,5857 ms；占用空间  446464 KB。

`long-cost:3625164 ms，uuid-cost:17185857 ms，相差:13560693 ms，倍率：4.740711。`

### 读取

```sql
-- = 条件查询
SELECT uid,ctime FROM pressure_uuid WHERE uname = '张三3'
> OK
> 时间: 0.065s

SELECT uid,ctime FROM pressure_long WHERE uname = '张三3'
> OK
> 时间: 0.032s

-- 翻页模糊排序
SELECT uid,ctime FROM pressure_uuid WHERE uname LIKE '张三3%'  ORDER BY soft LIMIT 20
> OK
> 时间: 2.247s

SELECT uid,ctime FROM pressure_long WHERE uname LIKE '张三3%'  ORDER BY soft LIMIT 20
> OK
> 时间: 0.036s

SELECT uid,ctime FROM pressure_uuid WHERE uname LIKE '张三3%'  ORDER BY soft LIMIT 500
> OK
> 时间: 20.303s

SELECT uid,ctime FROM pressure_long WHERE uname LIKE '张三3%'  ORDER BY soft LIMIT 500
> OK
> 时间: 0.132s

-- 翻页范围排序
SELECT uid FROM pressure_uuid WHERE ctime > '2022-03-22 22:21:19'   ORDER BY uid LIMIT 200
> OK
> 时间: 0.044s

SELECT uid FROM pressure_long WHERE ctime > '2022-03-19 09:21:19'  ORDER BY uid LIMIT 200
> OK
> 时间: 0.027s
```

### 修改

```sql
-- 修改
UPDATE pressure_uuid  SET soft = 12312312  WHERE uname = '张三4869'
> Affected rows: 1
> 时间: 0.074s

UPDATE pressure_long  SET soft = 12312312  WHERE  uname ='张三4869'
> Affected rows: 1
> 时间: 0.034s

UPDATE pressure_uuid  SET soft = 435  WHERE uid = '0732203e92ab47e0a39a7a7fb91a60c0'
> Affected rows: 1
> 时间: 0.052s

UPDATE pressure_long  SET soft = 435  WHERE uid = 4865
> Affected rows: 1
> 时间: 0.043s
```

### 总结

- 占用空间上：UUID 比 Long 主键表多 `1 倍上下`。
- 碎片率：Long 主键碎片率在 `10% 左右`， UUID 碎片率在 `150 % +`，比实际数据还多。
- 顺序写入：
  1. Long 主键插入速度平稳，UUID 插入速度劣化严重。UUID 10W 数量级已经超过比 Long  100W 数量级更慢，波动更大。可以预料，之后的效率劣化应该更严重。
  2. 1OOW 级插入速度，Long 大概是 UUID 的 `3 - 4 倍`速度。
- 并发写入：100W 并发写入，Long 的插入速度是 UUID 的 `4.7 倍`。
- 读取：单个条件匹配，建立索引的基础上 = 查询 `2 倍`；模糊查询 `100 倍 +`；范围查询 `2 倍`。
- 修改：Long 的修改写入，单条匹配非主键差距 `2 倍`，主键差距相对小点，批量修改性能差`会扩大`。

### 补充

UUID 的性能劣化，很大程度取决于关系型数据的底层设计。

关系型数据库是索引组织表，索引的底层数据结构是 B+ 树，这是一种多叉平衡树，索引数据的新增 / 修改对应着 B+ 树节点的变更，节点变更后，B+ 树就需要进行再平衡。如果是 UUID 这种离散数据的乱序插入，B+ 树为了达到平衡就需要做更多变形。而如果是趋势递增的数值（不需要严格自增），新增的节点都是在 B+ 树尾部，这样再平衡消耗会很小。这也印证了上面分析数据中，UUID 主键插入速度不断劣化，数值主键插入速度比较平稳，缓慢降速。

而且乱序插入带来的消耗，也会导致产生更多的碎片，导致表占用更多空间。

即使是清理完碎片，UUID 还是比数值主键多占用 80 % 的空间，这也是由于关系型数据的设计是二次索引，分为聚簇和非聚簇索引，每个非聚簇索引都根据主键到聚簇索引上寻找数据，索引主键的值存在多份。