-- CONFIGURATION / LOOKUPS / CONSTANTS
-- VIDEO SETTINGS
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(101, 'VIDEO_URL', 'VIDEO_SETTINGS', 'https://www.youtube.com/watch?v=mlSbzvyd5MU', 'Video settings url.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(102, 'VIDEO_MESSAGE', 'VIDEO_SETTINGS', 'Watch the orientation video until the end to proceed with the registration.', 'Video settings message.');

-- MEDICAL ASSISTANCE SETTINGS
-- ASSISTANCE TYPE LOOKUP
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1000, 'CODE_CHECK_UP', 'ASSISTANCE_TYPE', 'Check-up', 'Assistance type.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1001, 'CODE_MEDICAL_EXPENSE', 'ASSISTANCE_TYPE', 'Medical Expense', 'Assistance type.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1002, 'CODE_THERAPY', 'ASSISTANCE_TYPE', 'Therapy', 'Assistance type.');
-- DIAGNOSIS LOOKUP
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1100, 'CHRONIC KIDNEY_DISEASE', 'DIAGNOSIS', 'Chronic Kidney Disease', 'Diagnosis.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1101, 'POST_KIDNEY_TRANSPLANT', 'DIAGNOSIS', 'Post Kidney Transplant', 'Diagnosis.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1102, 'CODE_END_STAGE_RENAL_DISEASE', 'DIAGNOSIS', 'End Stage Renal Disease', 'Diagnosis.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1103, 'CODE_CANCER', 'DIAGNOSIS', 'Cancer', 'Diagnosis.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1104, 'CEREBRAL_PALSY', 'DIAGNOSIS', 'Cerebral Palsy', 'Diagnosis.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1105, 'OTHERS', 'DIAGNOSIS', 'Others', 'Diagnosis.');
-- MEDICAL ASSISTANCE INFO
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1200, 'MESSAGE_1', 'MAA_MESSAGES', '1. HOSPITALS UNDER DOH at Hospital na tumatanggap ng DOH Guaranteed letter lamang po sa ngayon ang tinatanggap naming request. (Hindi kasali ang mga Private Hospitals, Dialysis Centers, Lying-in at mga Clinics)', 'Medical Assistance Application Instructions.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1201, 'MESSAGE_2', 'MAA_MESSAGES', '2. Siguraduhin pangalan ng pasyente ang ilalagay sa form.', 'Medical Assistance Application Instructions.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1202, 'MESSAGE_3', 'MAA_MESSAGES', '3. Iwasang magkamali ng spelling ng pangalan ng pasyente.', 'Medical Assistance Application Instructions.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1203, 'MESSAGE_4', 'MAA_MESSAGES', '4. Kumpletuhin ang pag-fill-up sa form.', 'Medical Assistance Application Instructions.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1204, 'MESSAGE_5', 'MAA_MESSAGES', '5. Huwag mag-fill-up kung hindi gagamitin o sa susunod pang taon gagamitin ang assistance.', 'Medical Assistance Application Instructions.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1205, 'MESSAGE_6', 'MAA_MESSAGES', '6. Marami ang nagpapasa kada araw ng request, pasensya at pag-unawa ang ang hiling namin sa mga hindi agad mabibigyan', 'Medical Assistance Application Instructions.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1206, 'MESSAGE_7', 'MAA_MESSAGES', 'Sana po ay masusunod ito upang maiwasan ang mali sa mga request. At mas mabilis nating maproproseso ang iyong request', 'Medical Assistance Application Instructions.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1250, 'HH_INCOME_1', 'HH_MONTHLY_INCOME_', '10000 Below', 'Household monthly income brackets');
-- HOUSEHOLD MONTHLY INCOME BRACKET
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1250, 'HH_INCOME_1', 'HH_MONTHLY_INCOME', '10000 Below', 'Household monthly income brackets');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1251, 'HH_INCOME_2', 'HH_MONTHLY_INCOME', '10001 - 20000', 'Household monthly income brackets');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1252, 'HH_INCOME_3', 'HH_MONTHLY_INCOME', '20001 - 30000', 'Household monthly income brackets');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1253, 'HH_INCOME_4', 'HH_MONTHLY_INCOME', '30001 - 40000', 'Household monthly income brackets');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1254, 'HH_INCOME_5', 'HH_MONTHLY_INCOME', '40001 - 50000', 'Household monthly income brackets');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(1255, 'HH_INCOME_6', 'HH_MONTHLY_INCOME', '50001 Above', 'Household monthly income brackets');

INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(2000, 'QR_CODE_WIDTH', 'QR_CODE_SETTINGS', '350', 'QR Code Settings Width.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(2001, 'QR_CODE_LENGTH', 'QR_CODE_SETTINGS', '350', 'QR Code Settings Length.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(2002, 'QR_CODE_URL', 'QR_CODE_SETTINGS', 'http://localhost:9000/voucher/validate', 'QR Code Settings Length.');

-- APP SETTINGS
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(200, 'DEFAULT_MEMBER_ACCOUNT_APPLICATION_STATUS', 'MEMBER_ACCOUNT_CONFIGURATION', 'Allowed', 'Member Account configuration settings.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(250, 'MEMBER_REQUIREMENT_1', 'MEMBER_ACCOUNT_REQUIREMENTS', 'Upload your updated profile photo(under my-profile page).', 'Member account requirements.');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(251, 'MEMBER_REQUIREMENT_2', 'MEMBER_ACCOUNT_REQUIREMENTS', 'Upload a valid id.(Government issued id)', 'Member account requirements.');

-- KEYCLOAK SETTINGS
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(300, 'KC_ADMIN_SERVER_URL', 'KEYCLOAK_ADMIN_SETTINGS', 'http://localhost:8081/auth', 'Keycloak Admin Settings, url');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(301, 'KC_ADMIN_REALM', 'KEYCLOAK_ADMIN_SETTINGS', 'MemberAssistanceRealm', 'Keycloak Admin Settings, realm');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(302, 'KC_ADMIN_CLIENT_ID', 'KEYCLOAK_ADMIN_SETTINGS', 'admin-cli', 'Keycloak Admin Settings, client id');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(303, 'KC_ADMIN_CLIENT_SECRET', 'KEYCLOAK_ADMIN_SETTINGS', 'f501f355-7baa-4052-8235-e97f1eaa57df', 'Keycloak Admin Settings, secret');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(304, 'KC_ADMIN_USERNAME', 'KEYCLOAK_ADMIN_SETTINGS', 'admin', 'Keycloak Admin Settings, username');
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(305, 'KC_ADMIN_PASSWORD', 'KEYCLOAK_ADMIN_SETTINGS', 'admin', 'Keycloak Admin Settings, password');

-- KEYCLOAK USER REQUIRED ACTIONS
INSERT INTO ma_config_param (id, `code`, `type`, `value`, description)
VALUES(320, 'KC_CONFIGURE_OTP', 'KEYCLOAK_USER_SETTINGS', 'false', 'Keycloak Admin Settings, username');

-- HOSPITAL DATA ENTRY
INSERT INTO ma_hospital(hospital_name, address_1, address_2, city,
                        region, country, created_date, updated_date,
                        created_by, updated_by)
VALUES('Philippine General Hospital', 'Taft Ave', 'Ermita', 'Manila City', 'NCR', 'Philippines', '2021-01-01 00:00:00', null, 'system', null),
      ('Asian Hospital and Medical Center', '2205 Civic Dr', 'Alabang', 'Muntinlupa City', 'NCR', 'Philippines', '2021-01-02 00:00:00', null, 'system', null),
      ('Paranaque Doctors Hospital', '175 Doña Soledad Ave', 'Better Living Subdivision', 'Paranaque City', 'NCR', 'Philippines', '2021-01-04 00:00:00', null, 'system', null),
      ('Philippine Heart Center', '174', 'Diliman', 'Quezon City', 'NCR', 'Philippines', '2021-01-05 00:00:00', null, 'system', null);

-- HOSPITAL CONTACT DATA ENTRY
INSERT INTO ma_hospital_contact(hospital_id, name, `position`, contact_number,
                                email, is_primary, created_date, updated_date, created_by, updated_by)
VALUES (1, 'Dr. Francisco-test Duque', 'Doctor', '0917-101-1111', 'duque@gmail.com', 1, '2021-01-01 00:00:00', null, 'system', null),
(2, 'Dr. John Del Mundo', 'Doctor', '0916-101-2222', 'jdel.mundo@gmail.com', 1, '2021-01-01 00:00:00', null, 'system', null),
(3, 'Dr. Jennifer San Roque', 'Doctor', '0919-101-3333', 'drjen123456@yahoo.com', 1, '2021-01-01 00:00:00', null, 'system', null),
(4, 'Dr. Facundo Costavio', 'Doctor', '0917-101-44444', 'facundo.custavio.med@yahoo.com', 1, '2021-01-01 00:00:00', null, 'system', null);

-- HOSPITAL ACCOUNT
INSERT INTO ma_hospital_account(hospital_id, budget, balance, created_date, updated_date, created_by, updated_by)
VALUES (1, 100000.0000, 100000.0000, '2021-01-01 00:00:00', null, 'system', null),
(2, 150000.0000, 85000.0000, '2021-01-01 00:00:00', null, 'system', null),
(3, 1000000.0000, 512000.0000, '2021-01-01 00:00:00', null, 'system', null),
(4, 525000.0000, 0.0000, '2021-01-01 00:00:00', null, 'system', null);

-- HOSPITAL TRANSACTION
INSERT INTO ma_hospital_transaction(`id`,`hospital_id`,`member_id`,`amount`,`created_date`,`updated_date`,`created_by`,`updated_by`) 
VALUES (3,3,1,1000000.0000,'2021-02-26 07:31:39',NULL,'makun@123',NULL);
INSERT INTO ma_hospital_transaction(`id`,`hospital_id`,`member_id`,`amount`,`created_date`,`updated_date`,`created_by`,`updated_by`) 
VALUES (4,3,1,24000.0000,'2021-02-26 14:16:22',NULL,'makun@123',NULL);
INSERT INTO ma_hospital_transaction(`id`,`hospital_id`,`member_id`,`amount`,`created_date`,`updated_date`,`created_by`,`updated_by`) 
VALUES (5,3,1,80000.0000,'2021-02-26 14:16:40',NULL,'makun@123',NULL);
INSERT INTO ma_hospital_transaction(`id`,`hospital_id`,`member_id`,`amount`,`created_date`,`updated_date`,`created_by`,`updated_by`) 
VALUES (6,3,1,10000.0000,'2021-02-26 14:17:03',NULL,'makun@123',NULL);
INSERT INTO ma_hospital_transaction(`id`,`hospital_id`,`member_id`,`amount`,`created_date`,`updated_date`,`created_by`,`updated_by`) 
VALUES (7,3,1,50000.0000,'2021-02-26 14:17:18',NULL,'makun@123',NULL);
INSERT INTO ma_hospital_transaction(`id`,`hospital_id`,`member_id`,`amount`,`created_date`,`updated_date`,`created_by`,`updated_by`) 
VALUES (8,3,1,10000.0000,'2021-02-26 14:17:53',NULL,'makun@123',NULL);

-- MEDICAL ASSISTANCE
INSERT INTO ma_medical_assistance(`id`,`email_address`,`patient_first_name`,`patient_last_name`,`patient_middle_name`,
                                  `patient_email_address`,`age`,`address_1`,`address_2`,`city`,`province`,`contact_number`,
                                  `diagnosis`,`month_house_hold_income`,`hospital_id`,`hospital_name`,`assistance_type`,
                                  `amount`,`date_applied`,`date_awarded`,`status`,`reviewed_by`,`approved_by`,
                                  `voucher_number`,`created_date`,`updated_date`,`created_by`,`updated_by`,
                                  `date_of_birth`,`gender`,`reason`) 
VALUES (2,'makun@123','Juan','Dela Cruz',NULL,'juandc@gmail.com',30,'israel',NULL,'paranaque','metro manila','1234567890','Cancer','10000 Below',3,'Paranaque Doctors Hospital','Medical Expense',15000.0000,'2021-02-08 00:00:00','2021-02-18 00:00:00','Processing',NULL,NULL,'100000000000000001',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO ma_medical_assistance(`id`,`email_address`,`patient_first_name`,`patient_last_name`,`patient_middle_name`,
                                  `patient_email_address`,`age`,`address_1`,`address_2`,`city`,`province`,`contact_number`,
                                  `diagnosis`,`month_house_hold_income`,`hospital_id`,`hospital_name`,`assistance_type`,
                                  `amount`,`date_applied`,`date_awarded`,`status`,`reviewed_by`,`approved_by`,
                                  `voucher_number`,`created_date`,`updated_date`,`created_by`,`updated_by`,
                                  `date_of_birth`,`gender`,`reason`) 
VALUES (3,'makun@123','Anna','Santos',NULL,'annasantos@gmail.com',40,'c5 homeland',NULL,'quezon','metro manila','2345678901','Cerebral Palsy','10001 - 20000',4,'Philippine Heart Center','Therapy',3000.0000,'2021-02-09 00:00:00',NULL,'Processing',NULL,NULL,'100000000000000002',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO ma_medical_assistance(`id`,`email_address`,`patient_first_name`,`patient_last_name`,`patient_middle_name`,
                                  `patient_email_address`,`age`,`address_1`,`address_2`,`city`,`province`,`contact_number`,
                                  `diagnosis`,`month_house_hold_income`,`hospital_id`,`hospital_name`,`assistance_type`,
                                  `amount`,`date_applied`,`date_awarded`,`status`,`reviewed_by`,`approved_by`,
                                  `voucher_number`,`created_date`,`updated_date`,`created_by`,`updated_by`,
                                  `date_of_birth`,`gender`,`reason`) 
VALUES (4,'makun@123','Trae','Reyes',NULL,'icetrae@gmail.com',20,'singalong street',NULL,'manila','metro manila','3456789012','Chronic Kidney Disease','30001 - 40000',1,'Philippine General Hospital','Medical Expense',10000.0000,'2021-02-09 00:00:00',NULL,'Processing',NULL,NULL,'100000000000000003',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO ma_medical_assistance(`id`,`email_address`,`patient_first_name`,`patient_last_name`,`patient_middle_name`,
                                  `patient_email_address`,`age`,`address_1`,`address_2`,`city`,`province`,`contact_number`,
                                  `diagnosis`,`month_house_hold_income`,`hospital_id`,`hospital_name`,`assistance_type`,
                                  `amount`,`date_applied`,`date_awarded`,`status`,`reviewed_by`,`approved_by`,
                                  `voucher_number`,`created_date`,`updated_date`,`created_by`,`updated_by`,
                                  `date_of_birth`,`gender`,`reason`) 
VALUES (5,'makun@123','Erlinda','Pangilinan',NULL,'lindap@gmail.com',60,'bf resort',NULL,'muntinlupa','metro manila','4567890123','Post Kidney Transplant','40001 - 50000',2,'Asian Hospital and Medical Center','Check-up',5000.0000,'2021-01-10 00:00:00',NULL,'Processing',NULL,NULL,'100000000000000004',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO ma_medical_assistance(`id`,`email_address`,`patient_first_name`,`patient_last_name`,`patient_middle_name`,
                                  `patient_email_address`,`age`,`address_1`,`address_2`,`city`,`province`,`contact_number`,
                                  `diagnosis`,`month_house_hold_income`,`hospital_id`,`hospital_name`,`assistance_type`,
                                  `amount`,`date_applied`,`date_awarded`,`status`,`reviewed_by`,`approved_by`,
                                  `voucher_number`,`created_date`,`updated_date`,`created_by`,`updated_by`,
                                  `date_of_birth`,`gender`,`reason`) 
VALUES (10,'makun@123','John Dhave','Perez',NULL,'jdhave@gmail.com',16,'southern square',NULL,'paranaque','metro manila','5678901234','Others','10000 Below',3,'Paranaque Doctors Hospital','Therapy',2500.0000,'2020-12-20 00:00:00','2021-02-18 00:00:00','Processing',NULL,NULL,'100000000000000005',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO ma_medical_assistance(`id`,`email_address`,`patient_first_name`,`patient_last_name`,`patient_middle_name`,
                                  `patient_email_address`,`age`,`address_1`,`address_2`,`city`,`province`,`contact_number`,
                                  `diagnosis`,`month_house_hold_income`,`hospital_id`,`hospital_name`,`assistance_type`,
                                  `amount`,`date_applied`,`date_awarded`,`status`,`reviewed_by`,`approved_by`,
                                  `voucher_number`,`created_date`,`updated_date`,`created_by`,`updated_by`,
                                  `date_of_birth`,`gender`,`reason`) 
VALUES (11,'makun@123','Kimmy','Reynante',NULL,'kimreynante@gmail.com',55,'st james',NULL,'quezon','metro manila','6789012345','Others','20001 - 30000',4,'Philippine Heart Center','Check-up',2000.0000,'2021-01-20 00:00:00',NULL,'Processing',NULL,NULL,'100000000000000006',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO ma_medical_assistance(`id`,`email_address`,`patient_first_name`,`patient_last_name`,`patient_middle_name`,
                                  `patient_email_address`,`age`,`address_1`,`address_2`,`city`,`province`,`contact_number`,
                                  `diagnosis`,`month_house_hold_income`,`hospital_id`,`hospital_name`,`assistance_type`,
                                  `amount`,`date_applied`,`date_awarded`,`status`,`reviewed_by`,`approved_by`,
                                  `voucher_number`,`created_date`,`updated_date`,`created_by`,`updated_by`,
                                  `date_of_birth`,`gender`,`reason`) 
VALUES (12,'makun@123','Matthew','Dela Paz',NULL,'mattdp@gmail.com',12,'kapitolyo',NULL,'pasig','metro manila','7890123456','Others','10001 - 20000',1,'Philippine General Hospital','Therapy',1500.0000,'2020-11-20 00:00:00',NULL,'Processing',NULL,NULL,'100000000000000007',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO ma_medical_assistance(`id`,`email_address`,`patient_first_name`,`patient_last_name`,`patient_middle_name`,
                                  `patient_email_address`,`age`,`address_1`,`address_2`,`city`,`province`,`contact_number`,
                                  `diagnosis`,`month_house_hold_income`,`hospital_id`,`hospital_name`,`assistance_type`,
                                  `amount`,`date_applied`,`date_awarded`,`status`,`reviewed_by`,`approved_by`,
                                  `voucher_number`,`created_date`,`updated_date`,`created_by`,`updated_by`,
                                  `date_of_birth`,`gender`,`reason`) 
VALUES (13,'makun@123','Naty','Soriano',NULL,'natysoriano@gmail.com',65,'barangca',NULL,'las pinas','metro manila','8901234567','Others','50001 Above',2,'Asian Hospital and Medical Center','Medical Expense',5000.0000,'2020-01-23 00:00:00',NULL,'Processing',NULL,NULL,'100000000000000008',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO ma_medical_assistance(`id`,`email_address`,`patient_first_name`,`patient_last_name`,`patient_middle_name`,
                                  `patient_email_address`,`age`,`address_1`,`address_2`,`city`,`province`,`contact_number`,
                                  `diagnosis`,`month_house_hold_income`,`hospital_id`,`hospital_name`,`assistance_type`,
                                  `amount`,`date_applied`,`date_awarded`,`status`,`reviewed_by`,`approved_by`,
                                  `voucher_number`,`created_date`,`updated_date`,`created_by`,`updated_by`,
                                  `date_of_birth`,`gender`,`reason`) 
VALUES (14,'makun@123','Uly','Dela Cruz',NULL,'ulydc@gmail.com',44,'israel',NULL,'paranaque','metro manila','9012345678','End Stage Renal Disease','10000 Below',3,'Paranaque Doctors Hospital','Medical Expense',10000.0000,'2020-12-17 00:00:00','2021-02-18 00:00:00','Processing',NULL,NULL,'100000000000000009',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO ma_medical_assistance(`id`,`email_address`,`patient_first_name`,`patient_last_name`,`patient_middle_name`,
                                  `patient_email_address`,`age`,`address_1`,`address_2`,`city`,`province`,`contact_number`,
                                  `diagnosis`,`month_house_hold_income`,`hospital_id`,`hospital_name`,`assistance_type`,
                                  `amount`,`date_applied`,`date_awarded`,`status`,`reviewed_by`,`approved_by`,
                                  `voucher_number`,`created_date`,`updated_date`,`created_by`,`updated_by`,
                                  `date_of_birth`,`gender`,`reason`) 
VALUES (15,'makun@123','Peter','Santos',NULL,'petesantos@gmail.com',39,'c5 homeland',NULL,'quezon','metro manila','2342341231','Chronic Kidney Disease','10001 - 20000',4,'Philippine Heart Center','Medical Expense',9000.0000,'2020-11-11 00:00:00',NULL,'Processing',NULL,NULL,'100000000000000010',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO ma_medical_assistance(`id`,`email_address`,`patient_first_name`,`patient_last_name`,`patient_middle_name`,
                                  `patient_email_address`,`age`,`address_1`,`address_2`,`city`,`province`,`contact_number`,
                                  `diagnosis`,`month_house_hold_income`,`hospital_id`,`hospital_name`,`assistance_type`,
                                  `amount`,`date_applied`,`date_awarded`,`status`,`reviewed_by`,`approved_by`,
                                  `voucher_number`,`created_date`,`updated_date`,`created_by`,`updated_by`,
                                  `date_of_birth`,`gender`,`reason`) 
VALUES (16,'makun@123','Tony','Reyes',NULL,'tonyr@gmail.com',40,'singalong',NULL,'manila','metro manila','1232131230','Others','30001 - 40000',1,'Philippine General Hospital','Check-up',1000.0000,'2021-02-02 00:00:00',NULL,'Processing',NULL,NULL,'100000000000000011',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO ma_medical_assistance(`id`,`email_address`,`patient_first_name`,`patient_last_name`,`patient_middle_name`,
                                  `patient_email_address`,`age`,`address_1`,`address_2`,`city`,`province`,`contact_number`,
                                  `diagnosis`,`month_house_hold_income`,`hospital_id`,`hospital_name`,`assistance_type`,
                                  `amount`,`date_applied`,`date_awarded`,`status`,`reviewed_by`,`approved_by`,
                                  `voucher_number`,`created_date`,`updated_date`,`created_by`,`updated_by`,
                                  `date_of_birth`,`gender`,`reason`) 
VALUES (17,'makun@123','Kiks','Pangilinan',NULL,'kiksp@gmail.com',70,'bf resort',NULL,'muntinlupa','metro manila','3692465009','Cancer','40001 - 50000',2,'Asian Hospital and Medical Center','Therapy',4000.0000,'2020-10-10 00:00:00',NULL,'Processing',NULL,NULL,'100000000000000012',NULL,NULL,NULL,NULL,NULL,NULL,NULL);

-- MEMBER ENTRY
INSERT INTO ma_member(`id`,`first_name`,`last_name`,`middle_name`,`email_address`,`contact_number`,`age`,
                      `date_of_birth`,`address_1`,`address_2`,`city`,`province`,`is_active`,`is_verified`,
                      `created_date`,`updated_date`,`created_by`,`updated_by`,`createdBy`,`updatedBy`,`gender`,
                      `otp`,`status`,`keycloak_user_id`) 
VALUES (1,'Mak','Perez','Paz','makun@123','1234567890',30,'2016-02-08 16:00:00','israel','better living','paranaque','metro manila',0,0,NULL,NULL,NULL,NULL,NULL,NULL,'Male','873452',NULL,NULL);

-- MEMBER ACCOUNT
INSERT INTO ma_member_account(`email_address`,`account_number`,`member_id`,`status`,`totalAssistanceAmount`,`created_date`,
                              `updated_date`,`created_by`,`updated_by`,`id`,`createdBy`,`updatedBy`,`account_status`,
                              `application_status`,`id_photo_url`,`last_applied_date`,`profile_photo_url`,
                              `total_assistance_amount`,`id_photo_uuid`,`profile_photo_uuid`) 
VALUES ('makun@123','MAA100000000001',1,'Verified',0.0000,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
