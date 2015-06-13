-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Mar 07, 2015 at 12:00 PM
-- Server version: 5.5.27
-- PHP Version: 5.4.7

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `pura`
--

-- --------------------------------------------------------

--
-- Table structure for table `conversation`
--

CREATE TABLE IF NOT EXISTS `conversation` (
  `from` bigint(10) NOT NULL,
  `to` bigint(10) NOT NULL,
  `message` varchar(5000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `conversation`
--

INSERT INTO `conversation` (`from`, `to`, `message`) VALUES
(9597333280, 9600092296, 'fucker'),
(9597333280, 9600092296, 'dei'),
(9600092296, 9597333280, 'dei'),
(9597333280, 9600092296, 'dei'),
(9597333280, 9600092296, 'dei'),
(9597333280, 9600092296, 'Vaghul da'),
(9600092296, 9597333280, 'engaged paapom'),
(9597333280, 9600092296, 'dei'),
(9600092296, 9597333280, 'solra');

-- --------------------------------------------------------

--
-- Table structure for table `otp`
--

CREATE TABLE IF NOT EXISTS `otp` (
  `phone` bigint(10) NOT NULL,
  `otp` int(5) NOT NULL,
  `valid` varchar(75) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `otp`
--

INSERT INTO `otp` (`phone`, `otp`, `valid`) VALUES
(9597333280, 8385, '28'),
(9600092296, 7204, '28');

-- --------------------------------------------------------

--
-- Table structure for table `user_details`
--

CREATE TABLE IF NOT EXISTS `user_details` (
  `name` varchar(15) NOT NULL,
  `phone` bigint(10) NOT NULL,
  `reg_id` varchar(300) NOT NULL,
  `status` varchar(150) NOT NULL,
  `prof_pic` blob,
  PRIMARY KEY (`phone`),
  UNIQUE KEY `reg_id` (`reg_id`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `phone_2` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_details`
--

INSERT INTO `user_details` (`name`, `phone`, `reg_id`, `status`, `prof_pic`) VALUES
('vaghul', 9597333280, 'APA91bEzscHpsqNkEyFtygSTpMm2ILUtx9B4Kba_CjCVgIgLWKkU9a0JHSl9UxnJtaFsJz76NMU0K2RMcA1B0PtLr7cudO6KlfxiRRcQ0sO7FW7VH138RBxhqvI5EjLmo6_-Y4dRRUxqQ0byS0izbMEOE8lzCD0xrw', 'I am using Pura !', NULL),
('harish', 9600092296, 'APA91bFJjSbYRfAOM6bDYTZWaPeo8qUt62x1kcDbfzNRuVBOdH9Xi6mzcnYOGt3qCeBO2V-Pa3JyuF3lT-0_-4hpyLdnM7B4ppM0A8CNg5dEJz1yv9YAYGSKX_07SQSD-cj5x8CoKJqUvYbCuBX0EpJmaAT60_H-rQ', 'I am using Pura !', NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
