/*!40101 SET NAMES utf8 */;
/*!40101 SET SQL_MODE=''*/;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/ `member_assistance_db` /*!40100 DEFAULT CHARACTER SET latin1 */;

CREATE USER IF NOT EXISTS 'ma_db_user'@'localhost' IDENTIFIED BY 'm@DBu53r';
GRANT ALL ON member_assistance_db.* TO 'ma_db_user'@'localhost' IDENTIFIED BY 'm@DBu53r';

CREATE TABLE ma_member (
    id INT(20) AUTO_INCREMENT  PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255) DEFAULT NULL,
    email_address VARCHAR(255) NOT NULL,
    contact_number varchar (64) NOT NULL,
    age INT(3) NOT NULL,
    address_1 TEXT NOT NULL,
    address_2 TEXT DEFAULT NULL,
    city VARCHAR (255) NOT NULL,
    province VARCHAR (255),
    is_active tinyint(1) NOT NULL DEFAULT 0,
    is_verified tinyint(1) NOT NULL DEFAULT 0,
    keycloak_user_id varchar(64) DEFAULT NULL,
    created_date datetime DEFAULT NULL,
    updated_date datetime DEFAULT NULL,
    created_by VARCHAR(128) DEFAULT NULL,
    updated_by VARCHAR(128) DEFAULT NULL,
    UNIQUE KEY `UNQ_MA_MEM_EML` (`email_address`),
    UNIQUE KEY `UNQ_MA_KCLOAK_ID` (`keycloak_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
BIT
CREATE TABLE ma_member_account (
   id INT(20) AUTO_INCREMENT  PRIMARY KEY,
   member_id INT(20) NOT NULL,
   email_address VARCHAR(255) NOT NULL,
   account_number VARCHAR(18) NOT NULL,
   account_status VARCHAR(128) NOT NULL,
   application_status VARCHAR(64) DEFAULT NULL,
   last_applied_date DATETIME DEFAULT NULL,
   total_assistance_amount DECIMAL(12, 4) DEFAULT 0.0000,
   profile_photo_uuid VARCHAR(64) DEFAULT NULL,
   id_photo_uuid VARCHAR(64) DEFAULT NULL,
   created_date DATETIME DEFAULT NULL,
   updated_date DATETIME DEFAULT NULL,
   created_by VARCHAR(128) DEFAULT NULL,
   updated_by VARCHAR(128) DEFAULT NULL,
   UNIQUE KEY `UNQ_MA_MEM_ACCT_ACCT_NUM` (`account_number`),
   UNIQUE KEY `UNQ_MA_MEM_ACCT_EMAIL` (`email_address`),
   CONSTRAINT `FK_MEM_ACCT_REF` FOREIGN KEY (`email_address`)
       REFERENCES `ma_member` (`email_address`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE ma_medical_assistance (
      id INT(20) AUTO_INCREMENT  PRIMARY KEY,
      email_address VARCHAR(255) NOT NULL,
      patient_first_name VARCHAR(255) NOT NULL,
      patient_last_name VARCHAR(255) NOT NULL,
      patient_middle_name VARCHAR(255) DEFAULT NULL,
      patient_email_address VARCHAR(255) DEFAULT NULL,
      age INT(3) NOT NULL,
      address_1 TEXT NOT NULL,
      address_2 TEXT DEFAULT NULL,
      city VARCHAR (255) NOT NULL,
      province VARCHAR (255),
      contact_number VARCHAR(64) NOT NULL,
      diagnosis TEXT NOT NULL,
      month_house_hold_income VARCHAR(64) NOT NULL,
      hospital_id INT(11) NOT NULL,
      hospital_name VARCHAR (255) NOT NULL,
      assistance_type VARCHAR (64) NOT NULL,
      amount DECIMAL(12, 4) DEFAULT 0.0000,
      date_applied DATETIME NOT NULL,
      status VARCHAR (64) NOT NULL,
      reason text DEFAULT NULL,
      reviewed_by VARCHAR (255) DEFAULT NULL,
      approved_by VARCHAR (255) DEFAULT NULL,
      created_date datetime DEFAULT NULL,
      updated_date datetime DEFAULT NULL,
      created_by VARCHAR(128) DEFAULT NULL,
      updated_by VARCHAR(128) DEFAULT NULL,
      KEY `mma_by_email_index` (`email_address`),
      KEY `mma_by_hospital_index` (`hospital_name`),
      CONSTRAINT `FK_MMA_EMAIL_REF` FOREIGN KEY (`email_address`)
      REFERENCES `ma_member_account` (`email_address`)  ON DELETE NO ACTION ON UPDATE NO ACTION
)

CREATE TABLE ma_member_transaction (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    email_address VARCHAR(255) NOT NULL,
    type VARCHAR(128) NOT NULL,
    created_date datetime DEFAULT NULL,
    updated_date datetime DEFAULT NULL,
    created_by VARCHAR(128) DEFAULT NULL,
    updated_by VARCHAR(128) DEFAULT NULL,
    CONSTRAINT `FK_MEM_LOGS_REF` FOREIGN KEY (`email_address`)
        REFERENCES `ma_member` (`email_address`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE ma_config_param(
     id INT(20) AUTO_INCREMENT PRIMARY KEY,
     `code` VARCHAR(128) NOT NULL,
     `type` VARCHAR(128) NOT NULL,
     `value` VARCHAR(255) NOT NULL,
     `description` VARCHAR(255) NOT NULL,
     UNIQUE(code, type),
     KEY `MA_CONFIG_IDX1` (`code`),
     KEY `MA_CONFIG_IDX2` (`code`, `type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE ma_voucher (
     id                     INT(20) AUTO_INCREMENT PRIMARY KEY,
     application_id          INT(20) NOT NULL,
     account_number          VARCHAR(64),
     patient_full_name      TEXT NOT NULL,
     voucher_number         VARCHAR(64) NOT NULL,
     hospital_name          VARCHAR(255) NOT NULL,
     amount                 DECIMAL(12, 4),
     status                 VARCHAR(128) DEFAULT NULL,
     created_date           DATETIME DEFAULT NULL,
     updated_date           DATETIME DEFAULT NULL,
     created_by             VARCHAR(128) DEFAULT NULL,
     updated_by             VARCHAR(128) DEFAULT NULL,
     KEY `UNQ_MA_VCHR_IDX1` (`voucher_number`),
     KEY `UNQ_MA_VCHR_IDX2` (`account_number`),
     KEY `UNQ_MA_VCHR_IDX3` (`hospital_name`),
     CONSTRAINT `FK_VOUCHER_REF` FOREIGN KEY (`application_id`)
        REFERENCES `ma_medical_assistance` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE ma_hospital (
     id INT(11) AUTO_INCREMENT PRIMARY KEY,
     hospital_name VARCHAR(250) NOT NULL,
     address_1 TEXT NOT NULL,
     address_2 TEXT NOT NULL,
     city VARCHAR(255) NOT NULL,
     region VARCHAR(255) NOT NULL,
     country VARCHAR(128) NOT NULL,
     created_date datetime DEFAULT NULL,
     updated_date datetime DEFAULT NULL,
     created_by VARCHAR(128) DEFAULT NULL,
     updated_by VARCHAR(128) DEFAULT NULL,
     UNIQUE KEY `UNQ_MA_HOSP_IDX1` (`hospital_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE ma_hospital_contact (
         id INT(20) AUTO_INCREMENT PRIMARY KEY,
         hospital_id INT(20) NOT NULL,
         name VARCHAR(512) NOT NULL,
         position VARCHAR(255) NOT NULL,
         contact_number VARCHAR(64) NOT NULL,
         email TEXT NOT NULL,
         is_primary bit DEFAULT 0 NOT NULL,
         created_date datetime NOT NULL,
         updated_date datetime DEFAULT NULL,
         created_by VARCHAR(128) NOT NULL,
         updated_by VARCHAR(128) DEFAULT NULL,
         KEY `FK_HOSPITAL_CT_REF_IDX` (`hospital_id`),
         CONSTRAINT `FK_HOSPITAL_CT_REF` FOREIGN KEY (`hospital_id`) REFERENCES `ma_hospital` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
         KEY `MA_HOSP_CT_IDX1` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE ma_hospital_account (
         id INT(20) AUTO_INCREMENT PRIMARY KEY,
         hospital_id INT(20) NOT NULL,
         budget DECIMAL(12, 4) DEFAULT 0.0 NOT NULL,
         balance DECIMAL(12, 4) DEFAULT 0.0 NOT NULL,
         created_date datetime NOT NULL,
         updated_date datetime DEFAULT NULL,
         created_by VARCHAR(128) NOT NULL,
         updated_by VARCHAR(128) DEFAULT NULL,
         KEY `FK_HOSPITAL_ACCT_REF_IDX` (`hospital_id`),
         CONSTRAINT `FK_HOSPITAL_ACCT_REF` FOREIGN KEY (`hospital_id`) REFERENCES `ma_hospital` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- historical/replenish ???
CREATE TABLE ma_hospital_transaction (
         id INT(20) AUTO_INCREMENT PRIMARY KEY,
         hospital_id INT(20) NOT NULL,
         member_id INT(20) NOT NULL,
         amount DECIMAL(12, 4) DEFAULT 0.0 NOT NULL,
         created_date datetime NOT NULL,
         updated_date datetime DEFAULT NULL,
         created_by VARCHAR(128) NOT NULL,
         updated_by VARCHAR(128) DEFAULT NULL,
         KEY `FK_HOSPITAL_TX_REF_IDX` (`hospital_id`),
         CONSTRAINT `FK_HOSPITAL_TX_REF` FOREIGN KEY (`hospital_id`) REFERENCES `ma_hospital` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE ma_config_param(
     id INT(11) AUTO_INCREMENT PRIMARY KEY,
     `code` VARCHAR(128) NOT NULL,
     `type` VARCHAR(128) NOT NULL,
     `value` VARCHAR(255) NOT NULL,
     `description` VARCHAR(255) NOT NULL,
     UNIQUE(code, type),
     KEY `MA_CONFIG_IDX1` (`code`),
     KEY `MA_CONFIG_IDX2` (`code`, `type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE ma_re_captcha(
     `id` INT(20) AUTO_INCREMENT PRIMARY KEY,
     `client_response` TEXT NOT NULL,
     `success` TINYINT(1) NOT NULL,
     KEY `MA_CONFIG_IDX1` (`client_response`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


