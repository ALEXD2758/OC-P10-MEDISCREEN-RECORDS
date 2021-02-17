-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mediscreen_test
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mediscreen_test` ;

-- -----------------------------------------------------
-- Schema mediscreen
-- -----------------------------------------------------
CREATE SCHEMA `mediscreen_test` DEFAULT CHARACTER SET utf8 ;
USE `mediscreen_test` ;

-- -----------------------------------------------------
-- Table `mediscreen_test`.`patients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mediscreen_test`.`patients` (
  `id` int NOT NULL AUTO_INCREMENT,
  `given_name` VARCHAR(125) NOT NULL,
  `family_name` VARCHAR(125) NOT NULL,
  `birth_date` TIMESTAMP NOT NULL,
  `gender` VARCHAR(25) NOT NULL,
  `email_address` VARCHAR(125),
  `phone_number` VARCHAR(125),
  PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `mediscreen_test`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mediscreen_test`.`address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `street` VARCHAR(125),
  `city` VARCHAR(125),
  `postcode` VARCHAR(125) ,
  `district` VARCHAR(125),
  `state` VARCHAR(125),
  `country` VARCHAR(125),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id`) REFERENCES patients(`id`)
);

