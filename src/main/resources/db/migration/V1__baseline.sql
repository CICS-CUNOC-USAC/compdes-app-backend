/*M!999999\- enable the sandbox mode */
-- MariaDB dump 10.19  Distrib 10.11.13-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: compdes
-- ------------------------------------------------------
-- Server version	10.11.13-MariaDB-0ubuntu0.24.04.1
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;

/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;

/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;

/*!40101 SET NAMES utf8mb4 */;

/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;

/*!40103 SET TIME_ZONE='+00:00' */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activity`
--
/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = utf8mb4 */;

CREATE TABLE
    IF NOT EXISTS `activity` (
        `id` varchar(36) NOT NULL,
        `created_at` datetime (6) NOT NULL,
        `deleted_at` datetime (6) DEFAULT NULL,
        `desactivated_at` datetime (6) DEFAULT NULL,
        `updated_at` datetime (6) DEFAULT NULL,
        `description` varchar(500) NOT NULL,
        `name` varchar(100) NOT NULL,
        `scheduled_date` datetime (6) NOT NULL,
        `type` tinyint (4) DEFAULT NULL,
        PRIMARY KEY (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `compdes_user`
--
/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = utf8mb4 */;

CREATE TABLE
    IF NOT EXISTS `compdes_user` (
        `id` varchar(36) NOT NULL,
        `created_at` datetime (6) NOT NULL,
        `deleted_at` datetime (6) DEFAULT NULL,
        `desactivated_at` datetime (6) DEFAULT NULL,
        `updated_at` datetime (6) DEFAULT NULL,
        `password` varchar(100) DEFAULT NULL,
        `role` enum ('ADMIN', 'PARTICIPANT') NOT NULL,
        `username` varchar(50) DEFAULT NULL,
        `participant_id` varchar(36) DEFAULT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `UKt7we5vw6xgi0l6vc22yh8t830` (`username`),
        UNIQUE KEY `UKdn86r5xd6j2j6v1fr8cv4mcll` (`participant_id`),
        CONSTRAINT `FKly7pq372t9xlpeptexy1ot4ec` FOREIGN KEY (`participant_id`) REFERENCES `participant` (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `participant`
--
/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = utf8mb4 */;

CREATE TABLE
    IF NOT EXISTS `participant` (
        `id` varchar(36) NOT NULL,
        `created_at` datetime (6) NOT NULL,
        `deleted_at` datetime (6) DEFAULT NULL,
        `desactivated_at` datetime (6) DEFAULT NULL,
        `updated_at` datetime (6) DEFAULT NULL,
        `email` varchar(100) NOT NULL,
        `first_name` varchar(50) NOT NULL,
        `identification_document` varchar(30) NOT NULL,
        `is_author` bit (1) NOT NULL,
        `is_guest` bit (1) NOT NULL,
        `last_name` varchar(50) NOT NULL,
        `organisation` varchar(100) NOT NULL,
        `phone` varchar(20) NOT NULL,
        `compdes_user_id` varchar(36) DEFAULT NULL,
        `payment_proof_id` varchar(36) DEFAULT NULL,
        `payment_proof_image_id` varchar(36) DEFAULT NULL,
        `qr_code_id` varchar(36) DEFAULT NULL,
        `registration_status_id` varchar(36) DEFAULT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `UKgl6v798uk1ye39evt1spdkchj` (`email`),
        UNIQUE KEY `UKe1u1jf29ax7it50ynsiph2nr2` (`identification_document`),
        UNIQUE KEY `UK3qja8q8quoqbn76sl34x4qa2i` (`compdes_user_id`),
        UNIQUE KEY `UK8tfcl5yv655ujxkamiegrk4ex` (`payment_proof_id`),
        UNIQUE KEY `UKm1cro0mi8ihf8h2in2l3ip2ee` (`payment_proof_image_id`),
        UNIQUE KEY `UK7gq9fo0hasrcoab3kww75vlxm` (`qr_code_id`),
        UNIQUE KEY `UKak8ygyl0l0sy03l21awjhehvo` (`registration_status_id`),
        CONSTRAINT `FK2fon6ye9cfa4xle9osj744p69` FOREIGN KEY (`registration_status_id`) REFERENCES `registration_status` (`id`),
        CONSTRAINT `FK8nmkvfpdautxiibq1p1i3cmi0` FOREIGN KEY (`compdes_user_id`) REFERENCES `compdes_user` (`id`),
        CONSTRAINT `FKbjinh6ecbukw0s16b3q13iakm` FOREIGN KEY (`payment_proof_image_id`) REFERENCES `stored_file` (`id`),
        CONSTRAINT `FKph8kum13geny2c43i4dt4nxri` FOREIGN KEY (`payment_proof_id`) REFERENCES `payment_proof` (`id`),
        CONSTRAINT `FKqq0k05v0euxi5br9hcn19488b` FOREIGN KEY (`qr_code_id`) REFERENCES `qr_code` (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_proof`
--
/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = utf8mb4 */;

CREATE TABLE
    IF NOT EXISTS `payment_proof` (
        `id` varchar(36) NOT NULL,
        `created_at` datetime (6) NOT NULL,
        `deleted_at` datetime (6) DEFAULT NULL,
        `desactivated_at` datetime (6) DEFAULT NULL,
        `updated_at` datetime (6) DEFAULT NULL,
        `link` varchar(100) NOT NULL,
        PRIMARY KEY (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qr_code`
--
/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = utf8mb4 */;

CREATE TABLE
    IF NOT EXISTS `qr_code` (
        `id` varchar(36) NOT NULL,
        `created_at` datetime (6) NOT NULL,
        `deleted_at` datetime (6) DEFAULT NULL,
        `desactivated_at` datetime (6) DEFAULT NULL,
        `updated_at` datetime (6) DEFAULT NULL,
        `number_code` int (11) NOT NULL,
        `participant_id` varchar(36) DEFAULT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `UKalldjel7jm01igrjvhqa2ovq1` (`number_code`),
        UNIQUE KEY `UKluvw9vdgf88uwre7ywqg75asp` (`participant_id`),
        CONSTRAINT `FKfcjo4466qve89k2jhjve4ykqd` FOREIGN KEY (`participant_id`) REFERENCES `participant` (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `registration_status`
--
/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = utf8mb4 */;

CREATE TABLE
    IF NOT EXISTS `registration_status` (
        `id` varchar(36) NOT NULL,
        `created_at` datetime (6) NOT NULL,
        `deleted_at` datetime (6) DEFAULT NULL,
        `desactivated_at` datetime (6) DEFAULT NULL,
        `updated_at` datetime (6) DEFAULT NULL,
        `is_approved` bit (1) NOT NULL,
        `is_cash_payment` bit (1) DEFAULT NULL,
        `voucher_number` varchar(10) DEFAULT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `UK75cxd7rbr2c143l74wivt960x` (`voucher_number`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stored_file`
--
/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = utf8mb4 */;

CREATE TABLE
    IF NOT EXISTS `stored_file` (
        `id` varchar(36) NOT NULL,
        `created_at` datetime (6) NOT NULL,
        `deleted_at` datetime (6) DEFAULT NULL,
        `desactivated_at` datetime (6) DEFAULT NULL,
        `updated_at` datetime (6) DEFAULT NULL,
        `extension` varchar(20) NOT NULL,
        `file_name` varchar(200) NOT NULL,
        `mime_type` varchar(200) NOT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `UKmowq598902akxrv93g23r0yfl` (`file_name`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

/*!40101 SET character_set_client = @saved_cs_client */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;

/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;

/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-24 23:15:57