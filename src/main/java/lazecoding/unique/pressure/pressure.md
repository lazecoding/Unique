# 压力数据

比较 UUID 和 Number 主键性能差距。

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

-- 翻页模糊排序
SELECT uid,ctime FROM pressure_uuid WHERE uname LIKE '%三3%'  ORDER BY soft LIMIT 20,300;
SELECT uid,ctime FROM pressure_long WHERE uname LIKE '%三3%'  ORDER BY soft LIMIT 20,300;

-- 翻页范围排序
SELECT uid FROM pressure_uuid WHERE ctime > '2022-03-19 09:21:19'   ORDER BY soft,ctime LIMIT 500;
SELECT uid FROM pressure_long WHERE ctime > '2022-03-19 09:21:19'  ORDER BY soft,ctime LIMIT 500;
```

### LOG

#### 10W

```C
2022-03-19 12:58:42.048 [Unique] [executor-async-uuid start:37504 end:50004]  INFO PressureMain:138 - executor-async-uuid start:37504 end:50004 start-time:1647665922048
2022-03-19 12:58:42.048 [Unique] [executor-async-uuid start:12502 end:25002]  INFO PressureMain:138 - executor-async-uuid start:12502 end:25002 start-time:1647665922048
2022-03-19 12:58:42.048 [Unique] [executor-async-uuid start:50005 end:62505]  INFO PressureMain:138 - executor-async-uuid start:50005 end:62505 start-time:1647665922048
2022-03-19 12:58:42.048 [Unique] [executor-async-uuid start:25003 end:37503]  INFO PressureMain:138 - executor-async-uuid start:25003 end:37503 start-time:1647665922048
2022-03-19 12:58:42.048 [Unique] [executor-async-uuid start:1 end:12501]  INFO PressureMain:138 - executor-async-uuid start:1 end:12501 start-time:1647665922048
2022-03-19 12:58:42.049 [Unique] [executor-async-uuid start:62506 end:75006]  INFO PressureMain:138 - executor-async-uuid start:62506 end:75006 start-time:1647665922048
2022-03-19 12:58:42.049 [Unique] [executor-async-uuid start:75007 end:87507]  INFO PressureMain:138 - executor-async-uuid start:75007 end:87507 start-time:1647665922049
2022-03-19 12:58:42.049 [Unique] [executor-async-uuid start:87508 end:100000]  INFO PressureMain:138 - executor-async-uuid start:87508 end:100000 start-time:1647665922049
2022-03-19 13:05:53.306 [Unique] [executor-async-uuid start:87508 end:100000]  INFO PressureMain:146 - executor-async-uuid start:87508 end:100000 end-time:1647666353306  cost-time:431257 atomicInteger:99909
2022-03-19 13:05:53.528 [Unique] [executor-async-uuid start:75007 end:87507]  INFO PressureMain:146 - executor-async-uuid start:75007 end:87507 end-time:1647666353528  cost-time:431479 atomicInteger:99954
2022-03-19 13:05:53.612 [Unique] [executor-async-uuid start:12502 end:25002]  INFO PressureMain:146 - executor-async-uuid start:12502 end:25002 end-time:1647666353612  cost-time:431564 atomicInteger:99968
2022-03-19 13:05:53.619 [Unique] [executor-async-uuid start:62506 end:75006]  INFO PressureMain:146 - executor-async-uuid start:62506 end:75006 end-time:1647666353619  cost-time:431571 atomicInteger:99969
2022-03-19 13:05:53.714 [Unique] [executor-async-uuid start:50005 end:62505]  INFO PressureMain:146 - executor-async-uuid start:50005 end:62505 end-time:1647666353714  cost-time:431666 atomicInteger:99980
2022-03-19 13:05:53.733 [Unique] [executor-async-uuid start:37504 end:50004]  INFO PressureMain:146 - executor-async-uuid start:37504 end:50004 end-time:1647666353733  cost-time:431685 atomicInteger:99981
2022-03-19 13:05:53.969 [Unique] [executor-async-uuid start:25003 end:37503]  INFO PressureMain:146 - executor-async-uuid start:25003 end:37503 end-time:1647666353969  cost-time:431921 atomicInteger:99994
2022-03-19 13:05:54.175 [Unique] [executor-async-uuid start:1 end:12501]  INFO PressureMain:146 - executor-async-uuid start:1 end:12501 end-time:1647666354175  cost-time:432127 atomicInteger:100000


2022-03-19 13:06:21.589 [Unique] [executor-async-long start:12502 end:25002]  INFO PressureMain:196 - executor-async-long start:12502 end:25002 start-time:1647666381589
2022-03-19 13:06:21.589 [Unique] [executor-async-long start:1 end:12501]  INFO PressureMain:196 - executor-async-long start:1 end:12501 start-time:1647666381589
2022-03-19 13:06:21.590 [Unique] [executor-async-long start:25003 end:37503]  INFO PressureMain:196 - executor-async-long start:25003 end:37503 start-time:1647666381589
2022-03-19 13:06:21.590 [Unique] [executor-async-long start:37504 end:50004]  INFO PressureMain:196 - executor-async-long start:37504 end:50004 start-time:1647666381589
2022-03-19 13:06:21.590 [Unique] [executor-async-long start:50005 end:62505]  INFO PressureMain:196 - executor-async-long start:50005 end:62505 start-time:1647666381590
2022-03-19 13:06:21.590 [Unique] [executor-async-long start:62506 end:75006]  INFO PressureMain:196 - executor-async-long start:62506 end:75006 start-time:1647666381590
2022-03-19 13:06:21.591 [Unique] [executor-async-long start:75007 end:87507]  INFO PressureMain:196 - executor-async-long start:75007 end:87507 start-time:1647666381591
2022-03-19 13:06:21.591 [Unique] [executor-async-long start:87508 end:100000]  INFO PressureMain:196 - executor-async-long start:87508 end:100000 start-time:1647666381591
2022-03-19 13:13:30.088 [Unique] [executor-async-long start:75007 end:87507]  INFO PressureMain:204 - executor-async-long start:75007 end:87507 end-time:1647666810088  cost-time:428497 atomicInteger:99947
2022-03-19 13:13:30.211 [Unique] [executor-async-long start:87508 end:100000]  INFO PressureMain:204 - executor-async-long start:87508 end:100000 end-time:1647666810211  cost-time:428620 atomicInteger:99972
2022-03-19 13:13:30.257 [Unique] [executor-async-long start:25003 end:37503]  INFO PressureMain:204 - executor-async-long start:25003 end:37503 end-time:1647666810257  cost-time:428668 atomicInteger:99980
2022-03-19 13:13:30.312 [Unique] [executor-async-long start:50005 end:62505]  INFO PressureMain:204 - executor-async-long start:50005 end:62505 end-time:1647666810312  cost-time:428722 atomicInteger:99988
2022-03-19 13:13:30.378 [Unique] [executor-async-long start:12502 end:25002]  INFO PressureMain:204 - executor-async-long start:12502 end:25002 end-time:1647666810378  cost-time:428789 atomicInteger:99995
2022-03-19 13:13:30.404 [Unique] [executor-async-long start:37504 end:50004]  INFO PressureMain:204 - executor-async-long start:37504 end:50004 end-time:1647666810404  cost-time:428815 atomicInteger:99996
2022-03-19 13:13:30.449 [Unique] [executor-async-long start:62506 end:75006]  INFO PressureMain:204 - executor-async-long start:62506 end:75006 end-time:1647666810449  cost-time:428859 atomicInteger:99999
2022-03-19 13:13:30.511 [Unique] [executor-async-long start:1 end:12501]  INFO PressureMain:204 - executor-async-long start:1 end:12501 end-time:1647666810511  cost-time:428922 atomicInteger:100000


long  1647666810511 - 1647666381589
uuid  1647666354175 - 1647665922048

longCost:428922  uuidCost:432127  相差:3205
```