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