CREATE TABLE `user` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255)  NULL  DEFAULT NULL,
    `password` VARCHAR(255)  NULL  DEFAULT NULL,
    `verified` BOOLEAN      NOT NULL DEFAULT 0,
    `birthdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_at`  TIMESTAMP(6)     NULL     DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`  TIMESTAMP(6)     NULL     DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    `is_deleted`     BOOLEAN      NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `order` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255)  NULL  DEFAULT NULL,
    `user_id` int(11) unsigned NULL  DEFAULT NULL,
    `expected_delivery_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_at`  TIMESTAMP(6)     NULL     DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`  TIMESTAMP(6)     NULL     DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    `is_deleted`     BOOLEAN      NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
