# unique

分布式 ID 生成服务

### 介绍

基于数据库号段生成分布式 ID，每个业务应用设置一个业务标识，在 unique_record 插入一条记录。bus_tag 为业务标识，max_id 为最大值，step 为标准步长，description 为描述，update_time 为上一次ID生成时间。

### 环境准备

```sql
DROP TABLE IF EXISTS `unique_record`;

CREATE TABLE `unique_record` (
  `bus_tag` varchar(128)  NOT NULL DEFAULT '',
  `max_id` bigint(20) NOT NULL DEFAULT '1',
  `step` int(11) NOT NULL,
  `description` varchar(256)  DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`bus_tag`)
) ENGINE=InnoDB;

INSERT INTO unique_record(bus_tag, max_id, step, description) VALUES ('unique-record-segment-test', 1, 2000, 'Test Unique Record Segment Mode Get Id');
```

### 接口

Request URL: http://localhost:8090/api/get/unique-record-segment-test <br>
Request Method: GET

### Druid 监控
Request URL: http://localhost:8090/druid/index.html




