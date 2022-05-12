# Unique release-2.X.X

分布式 ID 生成服务 `release-2.X.X`，分离出 Client 和 Server。

### 介绍

基于数据库号段生成分布式 ID，namespace 之间互相隔离，Client 可以管理所属 namespace 下 tag 和 segment buffer，Server 用于下发 namespace、tag、segment。

如此设计，极大减少了网络开销，由 Client 自行维护 segment buffer，`每个 Client QPS >> 500W` ！

### 使用

maven 依赖：

```xml
<dependency>
    <groupId>lazecoding</groupId>
    <artifactId>unique-client</artifactId>
    <version>release-2.0.0</version>
</dependency>
```

> 未推送 repository。

Client - application.yml：

```yaml
unique:
  client:
    # server domain
    url: http://localhost:8090
    # 所属 namespace
    namespace: b9fefb0d-6ff4-47c3-a5bc-f5f9c172fe59
```

Server - application.yml：

```yaml
project:
  server-config:
    # 授权码
    authorization: admin
```

> authorization 用于管理 namespace；Server 默认 namespace 为 b9fefb0d-6ff4-47c3-a5bc-f5f9c172fe59。

### 初始化

```sql
DROP TABLE IF EXISTS `unique_record`;

CREATE TABLE `unique_record` (
 `uid` int(11) AUTO_INCREMENT,
 `bus_tag` varchar(128)  NOT NULL,
 `max_id` bigint(20) NOT NULL,
 `step` int(11) NOT NULL,
 `description` varchar(256),
 `namespace_id` varchar(36) NOT NULL,
 `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 PRIMARY KEY (`uid`),
 UNIQUE INDEX  idx_namespace_tag(`namespace_id`,`bus_tag`),
 INDEX idx_tag (`bus_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `namespace`;

CREATE TABLE `namespace` (
 `namespace_id` varchar(36)  NOT NULL,
 `description` varchar(256),
 PRIMARY KEY (`namespace_id`)
) ENGINE=InnoDB CHARSET=utf8mb4;
```

### API

- NameSpace

申请 namespace：`/api/namespace/apply/{authorization}/{description}`  
获取 namespace: `/api/namespace/find/{authorization}/{namespace}`  
删除 namespace: `/api/namespace/remove/{authorization}/{namespace}`  

- Tag

获取 tag: `/api/tag/get/{namespace}`  
tag 存在判断: `/api/tag/exist/{namespace}/{tag}`  
创建 tag: `/api/tag/add/{namespace}/{tag}/{maxId}/{step}/{description}`  
删除 tag: `/api/tag/remove/{namespace}/{tag}`  

- Segment

默认 step 申请 segment: `/api/segment/apply/{namespace}/{tag}`  
自定义 step 申请 segment: `/api/segment/apply/{namespace}/{tag}/{step}`  

## License

Unique software is licenced under the Apache License Version 2.0. See the [LICENSE](https://github.com/lazecoding/Unique/blob/master/LICENSE) file for details.