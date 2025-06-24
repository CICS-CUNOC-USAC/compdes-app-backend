DROP TABLE IF EXISTS `module_uni`;

CREATE TABLE `module_uni` (
  `id` varchar(36) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `desactivated_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `classroom`;

CREATE TABLE `classroom` (
  `id` varchar(36) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `desactivated_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `module_uni_id` varchar(36) NOT NULL,
  `classrooms_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbw57cj8ouehr9j4ek4xe1y3we` (`module_uni_id`),
  KEY `FKpaxas92p8ciahdj49pl1ggul0` (`classrooms_id`),
  CONSTRAINT `FKbw57cj8ouehr9j4ek4xe1y3we` FOREIGN KEY (`module_uni_id`) REFERENCES `module_uni` (`id`),
  CONSTRAINT `FKpaxas92p8ciahdj49pl1ggul0` FOREIGN KEY (`classrooms_id`) REFERENCES `module_uni` (`id`)
);


DROP TABLE IF EXISTS `activity`;

CREATE TABLE `activity` (
  `id` varchar(36) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `desactivated_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `description` varchar(500) NOT NULL,
  `end_scheduled_date` datetime(6) NOT NULL,
  `init_scheduled_date` datetime(6) NOT NULL,
  `name` varchar(100) NOT NULL,
  `type` tinyint DEFAULT NULL,
  `classroom_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4c8hjgtep7baq11385e03dicj` (`classroom_id`),
  CONSTRAINT `FK4c8hjgtep7baq11385e03dicj` FOREIGN KEY (`classroom_id`) REFERENCES `classroom` (`id`),
  CONSTRAINT `activity_chk_1` CHECK ((`type` between 0 and 2))
);