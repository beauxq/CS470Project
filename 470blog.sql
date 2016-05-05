-- MySQL dump 10.13  Distrib 5.7.11, for Win64 (x86_64)
--
-- Host: localhost    Database: 470blog
-- ------------------------------------------------------
-- Server version	5.7.11-log

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
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authors` (
  `aID` int(11) NOT NULL AUTO_INCREMENT,
  `aName` varchar(45) NOT NULL,
  `aPassword` varchar(45) NOT NULL,
  PRIMARY KEY (`aID`),
  UNIQUE KEY `aID_UNIQUE` (`aID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
  `cID` int(11) NOT NULL AUTO_INCREMENT,
  `cText` varchar(255) NOT NULL,
  `pID` int(11) NOT NULL,
  `cDate` datetime(3) DEFAULT NULL,
  `aID` int(11) NOT NULL,
  PRIMARY KEY (`cID`),
  UNIQUE KEY `cid_UNIQUE` (`cID`),
  KEY `aid_idx` (`aID`),
  KEY `pidcomment_idx` (`pID`),
  CONSTRAINT `aidcomment` FOREIGN KEY (`aID`) REFERENCES `authors` (`aID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `pidcomment` FOREIGN KEY (`pID`) REFERENCES `posts` (`pID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posts` (
  `pID` int(11) NOT NULL AUTO_INCREMENT,
  `pTitle` varchar(255) NOT NULL,
  `pText` mediumtext,
  `pDate` datetime(3) DEFAULT NULL,
  `aID` int(11) NOT NULL,
  PRIMARY KEY (`pID`),
  UNIQUE KEY `pid_UNIQUE` (`pID`),
  KEY `aid_idx` (`aID`),
  CONSTRAINT `aidposts` FOREIGN KEY (`aID`) REFERENCES `authors` (`aID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `posttags`
--

DROP TABLE IF EXISTS `posttags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posttags` (
  `pID` int(11) NOT NULL,
  `tID` int(11) NOT NULL,
  PRIMARY KEY (`pID`,`tID`),
  KEY `tidposttags_idx` (`tID`),
  CONSTRAINT `pidposttags` FOREIGN KEY (`pID`) REFERENCES `posts` (`pID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `tidposttags` FOREIGN KEY (`tID`) REFERENCES `tags` (`tID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tags` (
  `tID` int(11) NOT NULL AUTO_INCREMENT,
  `tText` varchar(45) NOT NULL,
  PRIMARY KEY (`tID`),
  UNIQUE KEY `tid_UNIQUE` (`tID`)
) ENGINE=InnoDB AUTO_INCREMENT=690 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-05  7:57:46
