-- MySQL dump 10.13  Distrib 5.5.61, for linux-glibc2.12 (x86_64)
--
-- Host: 172.17.0.3    Database: guacamole
-- ------------------------------------------------------
-- Server version	5.5.61

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `guacamole_connection`
--

DROP TABLE IF EXISTS `guacamole_connection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_connection` (
  `connection_id` int(11) NOT NULL AUTO_INCREMENT,
  `connection_name` varchar(128) NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `protocol` varchar(32) NOT NULL,
  `proxy_port` int(11) DEFAULT NULL,
  `proxy_hostname` varchar(512) DEFAULT NULL,
  `proxy_encryption_method` enum('NONE','SSL') DEFAULT NULL,
  `max_connections` int(11) DEFAULT NULL,
  `max_connections_per_user` int(11) DEFAULT NULL,
  `connection_weight` int(11) DEFAULT NULL,
  `failover_only` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`connection_id`),
  UNIQUE KEY `connection_name_parent` (`connection_name`,`parent_id`),
  KEY `guacamole_connection_ibfk_1` (`parent_id`),
  CONSTRAINT `guacamole_connection_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `guacamole_connection_group` (`connection_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_connection`
--

LOCK TABLES `guacamole_connection` WRITE;
/*!40000 ALTER TABLE `guacamole_connection` DISABLE KEYS */;
INSERT INTO `guacamole_connection` VALUES (2,'centos',NULL,'rdp',NULL,NULL,NULL,2,2,NULL,0);
/*!40000 ALTER TABLE `guacamole_connection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guacamole_connection_group`
--

DROP TABLE IF EXISTS `guacamole_connection_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_connection_group` (
  `connection_group_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL,
  `connection_group_name` varchar(128) NOT NULL,
  `type` enum('ORGANIZATIONAL','BALANCING') NOT NULL DEFAULT 'ORGANIZATIONAL',
  `max_connections` int(11) DEFAULT NULL,
  `max_connections_per_user` int(11) DEFAULT NULL,
  `enable_session_affinity` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`connection_group_id`),
  UNIQUE KEY `connection_group_name_parent` (`connection_group_name`,`parent_id`),
  KEY `guacamole_connection_group_ibfk_1` (`parent_id`),
  CONSTRAINT `guacamole_connection_group_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `guacamole_connection_group` (`connection_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_connection_group`
--

LOCK TABLES `guacamole_connection_group` WRITE;
/*!40000 ALTER TABLE `guacamole_connection_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `guacamole_connection_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guacamole_connection_group_permission`
--

DROP TABLE IF EXISTS `guacamole_connection_group_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_connection_group_permission` (
  `user_id` int(11) NOT NULL,
  `connection_group_id` int(11) NOT NULL,
  `permission` enum('READ','UPDATE','DELETE','ADMINISTER') NOT NULL,
  PRIMARY KEY (`user_id`,`connection_group_id`,`permission`),
  KEY `guacamole_connection_group_permission_ibfk_1` (`connection_group_id`),
  CONSTRAINT `guacamole_connection_group_permission_ibfk_1` FOREIGN KEY (`connection_group_id`) REFERENCES `guacamole_connection_group` (`connection_group_id`) ON DELETE CASCADE,
  CONSTRAINT `guacamole_connection_group_permission_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `guacamole_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_connection_group_permission`
--

LOCK TABLES `guacamole_connection_group_permission` WRITE;
/*!40000 ALTER TABLE `guacamole_connection_group_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `guacamole_connection_group_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guacamole_connection_history`
--

DROP TABLE IF EXISTS `guacamole_connection_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_connection_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `username` varchar(128) NOT NULL,
  `remote_host` varchar(256) DEFAULT NULL,
  `connection_id` int(11) DEFAULT NULL,
  `connection_name` varchar(128) NOT NULL,
  `sharing_profile_id` int(11) DEFAULT NULL,
  `sharing_profile_name` varchar(128) DEFAULT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`history_id`),
  KEY `user_id` (`user_id`),
  KEY `connection_id` (`connection_id`),
  KEY `sharing_profile_id` (`sharing_profile_id`),
  KEY `start_date` (`start_date`),
  KEY `end_date` (`end_date`),
  KEY `connection_start_date` (`connection_id`,`start_date`),
  CONSTRAINT `guacamole_connection_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `guacamole_user` (`user_id`) ON DELETE SET NULL,
  CONSTRAINT `guacamole_connection_history_ibfk_2` FOREIGN KEY (`connection_id`) REFERENCES `guacamole_connection` (`connection_id`) ON DELETE SET NULL,
  CONSTRAINT `guacamole_connection_history_ibfk_3` FOREIGN KEY (`sharing_profile_id`) REFERENCES `guacamole_sharing_profile` (`sharing_profile_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_connection_history`
--

LOCK TABLES `guacamole_connection_history` WRITE;
/*!40000 ALTER TABLE `guacamole_connection_history` DISABLE KEYS */;
INSERT INTO `guacamole_connection_history` VALUES (6,1,'zenika','88.182.194.100',NULL,'172.31.47.230',NULL,NULL,'2018-10-18 20:45:37','2018-10-18 20:45:37');
INSERT INTO `guacamole_connection_history` VALUES (7,1,'zenika','88.182.194.100',NULL,'172.31.47.230',NULL,NULL,'2018-10-18 20:47:52','2018-10-18 20:47:53');
INSERT INTO `guacamole_connection_history` VALUES (8,1,'zenika','88.182.194.100',NULL,'172.31.47.230',NULL,NULL,'2018-10-18 20:48:48','2018-10-18 20:48:48');
INSERT INTO `guacamole_connection_history` VALUES (9,1,'zenika','88.182.194.100',NULL,'172.31.47.230',NULL,NULL,'2018-10-18 20:51:05','2018-10-18 20:51:05');
/*!40000 ALTER TABLE `guacamole_connection_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guacamole_connection_parameter`
--

DROP TABLE IF EXISTS `guacamole_connection_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_connection_parameter` (
  `connection_id` int(11) NOT NULL,
  `parameter_name` varchar(128) NOT NULL,
  `parameter_value` varchar(4096) NOT NULL,
  PRIMARY KEY (`connection_id`,`parameter_name`),
  CONSTRAINT `guacamole_connection_parameter_ibfk_1` FOREIGN KEY (`connection_id`) REFERENCES `guacamole_connection` (`connection_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_connection_parameter`
--

LOCK TABLES `guacamole_connection_parameter` WRITE;
/*!40000 ALTER TABLE `guacamole_connection_parameter` DISABLE KEYS */;
INSERT INTO `guacamole_connection_parameter` VALUES (2,'hostname','172.31.47.230');
INSERT INTO `guacamole_connection_parameter` VALUES (2,'password','zenika');
INSERT INTO `guacamole_connection_parameter` VALUES (2,'username','centos');
/*!40000 ALTER TABLE `guacamole_connection_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guacamole_connection_permission`
--

DROP TABLE IF EXISTS `guacamole_connection_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_connection_permission` (
  `user_id` int(11) NOT NULL,
  `connection_id` int(11) NOT NULL,
  `permission` enum('READ','UPDATE','DELETE','ADMINISTER') NOT NULL,
  PRIMARY KEY (`user_id`,`connection_id`,`permission`),
  KEY `guacamole_connection_permission_ibfk_1` (`connection_id`),
  CONSTRAINT `guacamole_connection_permission_ibfk_1` FOREIGN KEY (`connection_id`) REFERENCES `guacamole_connection` (`connection_id`) ON DELETE CASCADE,
  CONSTRAINT `guacamole_connection_permission_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `guacamole_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_connection_permission`
--

LOCK TABLES `guacamole_connection_permission` WRITE;
/*!40000 ALTER TABLE `guacamole_connection_permission` DISABLE KEYS */;
INSERT INTO `guacamole_connection_permission` VALUES (1,2,'READ');
INSERT INTO `guacamole_connection_permission` VALUES (1,2,'UPDATE');
INSERT INTO `guacamole_connection_permission` VALUES (1,2,'DELETE');
INSERT INTO `guacamole_connection_permission` VALUES (1,2,'ADMINISTER');
/*!40000 ALTER TABLE `guacamole_connection_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guacamole_sharing_profile`
--

DROP TABLE IF EXISTS `guacamole_sharing_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_sharing_profile` (
  `sharing_profile_id` int(11) NOT NULL AUTO_INCREMENT,
  `sharing_profile_name` varchar(128) NOT NULL,
  `primary_connection_id` int(11) NOT NULL,
  PRIMARY KEY (`sharing_profile_id`),
  UNIQUE KEY `sharing_profile_name_primary` (`sharing_profile_name`,`primary_connection_id`),
  KEY `guacamole_sharing_profile_ibfk_1` (`primary_connection_id`),
  CONSTRAINT `guacamole_sharing_profile_ibfk_1` FOREIGN KEY (`primary_connection_id`) REFERENCES `guacamole_connection` (`connection_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_sharing_profile`
--

LOCK TABLES `guacamole_sharing_profile` WRITE;
/*!40000 ALTER TABLE `guacamole_sharing_profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `guacamole_sharing_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guacamole_sharing_profile_parameter`
--

DROP TABLE IF EXISTS `guacamole_sharing_profile_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_sharing_profile_parameter` (
  `sharing_profile_id` int(11) NOT NULL,
  `parameter_name` varchar(128) NOT NULL,
  `parameter_value` varchar(4096) NOT NULL,
  PRIMARY KEY (`sharing_profile_id`,`parameter_name`),
  CONSTRAINT `guacamole_sharing_profile_parameter_ibfk_1` FOREIGN KEY (`sharing_profile_id`) REFERENCES `guacamole_sharing_profile` (`sharing_profile_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_sharing_profile_parameter`
--

LOCK TABLES `guacamole_sharing_profile_parameter` WRITE;
/*!40000 ALTER TABLE `guacamole_sharing_profile_parameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `guacamole_sharing_profile_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guacamole_sharing_profile_permission`
--

DROP TABLE IF EXISTS `guacamole_sharing_profile_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_sharing_profile_permission` (
  `user_id` int(11) NOT NULL,
  `sharing_profile_id` int(11) NOT NULL,
  `permission` enum('READ','UPDATE','DELETE','ADMINISTER') NOT NULL,
  PRIMARY KEY (`user_id`,`sharing_profile_id`,`permission`),
  KEY `guacamole_sharing_profile_permission_ibfk_1` (`sharing_profile_id`),
  CONSTRAINT `guacamole_sharing_profile_permission_ibfk_1` FOREIGN KEY (`sharing_profile_id`) REFERENCES `guacamole_sharing_profile` (`sharing_profile_id`) ON DELETE CASCADE,
  CONSTRAINT `guacamole_sharing_profile_permission_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `guacamole_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_sharing_profile_permission`
--

LOCK TABLES `guacamole_sharing_profile_permission` WRITE;
/*!40000 ALTER TABLE `guacamole_sharing_profile_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `guacamole_sharing_profile_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guacamole_system_permission`
--

DROP TABLE IF EXISTS `guacamole_system_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_system_permission` (
  `user_id` int(11) NOT NULL,
  `permission` enum('CREATE_CONNECTION','CREATE_CONNECTION_GROUP','CREATE_SHARING_PROFILE','CREATE_USER','ADMINISTER') NOT NULL,
  PRIMARY KEY (`user_id`,`permission`),
  CONSTRAINT `guacamole_system_permission_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `guacamole_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_system_permission`
--

LOCK TABLES `guacamole_system_permission` WRITE;
/*!40000 ALTER TABLE `guacamole_system_permission` DISABLE KEYS */;
INSERT INTO `guacamole_system_permission` VALUES (1,'CREATE_CONNECTION');
INSERT INTO `guacamole_system_permission` VALUES (1,'CREATE_CONNECTION_GROUP');
INSERT INTO `guacamole_system_permission` VALUES (1,'CREATE_SHARING_PROFILE');
INSERT INTO `guacamole_system_permission` VALUES (1,'CREATE_USER');
INSERT INTO `guacamole_system_permission` VALUES (1,'ADMINISTER');
/*!40000 ALTER TABLE `guacamole_system_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guacamole_user`
--

DROP TABLE IF EXISTS `guacamole_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(128) NOT NULL,
  `password_hash` binary(32) NOT NULL,
  `password_salt` binary(32) DEFAULT NULL,
  `password_date` datetime NOT NULL,
  `disabled` tinyint(1) NOT NULL DEFAULT '0',
  `expired` tinyint(1) NOT NULL DEFAULT '0',
  `access_window_start` time DEFAULT NULL,
  `access_window_end` time DEFAULT NULL,
  `valid_from` date DEFAULT NULL,
  `valid_until` date DEFAULT NULL,
  `timezone` varchar(64) DEFAULT NULL,
  `full_name` varchar(256) DEFAULT NULL,
  `email_address` varchar(256) DEFAULT NULL,
  `organization` varchar(256) DEFAULT NULL,
  `organizational_role` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_user`
--

LOCK TABLES `guacamole_user` WRITE;
/*!40000 ALTER TABLE `guacamole_user` DISABLE KEYS */;
INSERT INTO `guacamole_user` VALUES (1,'zenika','NXÑ>X–≤C◊tÒ€±\rû◊éí8ÜªŒ?leíyú–','Çùlál|©ì¢ô	ó»%úä∂v´ê—ºJòãúl1†+','2018-09-27 20:17:26',0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `guacamole_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guacamole_user_history`
--

DROP TABLE IF EXISTS `guacamole_user_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_user_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `username` varchar(128) NOT NULL,
  `remote_host` varchar(256) DEFAULT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`history_id`),
  KEY `user_id` (`user_id`),
  KEY `start_date` (`start_date`),
  KEY `end_date` (`end_date`),
  KEY `user_start_date` (`user_id`,`start_date`),
  CONSTRAINT `guacamole_user_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `guacamole_user` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_user_history`
--

LOCK TABLES `guacamole_user_history` WRITE;
/*!40000 ALTER TABLE `guacamole_user_history` DISABLE KEYS */;
INSERT INTO `guacamole_user_history` VALUES (4,1,'zenika','88.182.194.100','2018-10-18 20:45:36',NULL);
/*!40000 ALTER TABLE `guacamole_user_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guacamole_user_password_history`
--

DROP TABLE IF EXISTS `guacamole_user_password_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_user_password_history` (
  `password_history_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `password_hash` binary(32) NOT NULL,
  `password_salt` binary(32) DEFAULT NULL,
  `password_date` datetime NOT NULL,
  PRIMARY KEY (`password_history_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `guacamole_user_password_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `guacamole_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_user_password_history`
--

LOCK TABLES `guacamole_user_password_history` WRITE;
/*!40000 ALTER TABLE `guacamole_user_password_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `guacamole_user_password_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guacamole_user_permission`
--

DROP TABLE IF EXISTS `guacamole_user_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guacamole_user_permission` (
  `user_id` int(11) NOT NULL,
  `affected_user_id` int(11) NOT NULL,
  `permission` enum('READ','UPDATE','DELETE','ADMINISTER') NOT NULL,
  PRIMARY KEY (`user_id`,`affected_user_id`,`permission`),
  KEY `guacamole_user_permission_ibfk_1` (`affected_user_id`),
  CONSTRAINT `guacamole_user_permission_ibfk_1` FOREIGN KEY (`affected_user_id`) REFERENCES `guacamole_user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `guacamole_user_permission_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `guacamole_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guacamole_user_permission`
--

LOCK TABLES `guacamole_user_permission` WRITE;
/*!40000 ALTER TABLE `guacamole_user_permission` DISABLE KEYS */;
INSERT INTO `guacamole_user_permission` VALUES (1,1,'READ');
INSERT INTO `guacamole_user_permission` VALUES (1,1,'UPDATE');
INSERT INTO `guacamole_user_permission` VALUES (1,1,'ADMINISTER');
/*!40000 ALTER TABLE `guacamole_user_permission` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-10-18 20:57:41
