package com.member.assistance.core.constants;

import java.util.LinkedHashMap;

public class MemberAssistanceConstants {
    public static final String HOME_URL = "http://localhost:9000";
    public static final String FS_HOME = "user.home";
    public static final String FS_PHOTO = "/workspaces/member_assistance_app/photo";
    public static final String FS_PROFILES = "/workspaces/member_assistance_app/photo/profiles";
    public static final String FS_IDS = "/workspaces/member_assistance_app/photo/ids";
    public static final String FS_OLD_PROFILES = "/workspaces/member_assistance_app/photo/old_profiles";
    public static final String FS_OLD_IDS = "/workspaces/member_assistance_app/photo/old_ids";
    public static final String FS_VOUCHERS = "/workspaces/member_assistance_app/vouchers";
    public static final String FS_REPORTS = "/workspaces/member_assistance_app/reports";
    public static final String FILE_EXTENSION_PNG = ".png";
    public static final String FILE_EXTENSION_JPG = ".jpg";
    public static final String KEY_ID_FRONT = "front";
    public static final String KEY_ID_BACK = "back";
    public static final String ID_FRONT_IMAGE_DEFAULT_URL = "http://localhost:9000/images/id_front.jpg";
    public static final String ID_BACK_IMAGE_DEFAULT_URL = "http://localhost:9000/images/id_back.jpg";
    public static final String PROFILE_IMAGE_DEFAULT_URL = "http://localhost:9000/images/profile.png";
    public static final String DATE_FORMAT_APPEND_TO_FILE = "yyyyMMddHHmmss";

    public static final String CONFIG_TYPE_KEYCLOAK_ADMIN_SETTINGS = "KEYCLOAK_ADMIN_SETTINGS";
    public static final String CONFIG_CODE_KC_ADMIN_SERVER_URL = "KC_ADMIN_SERVER_URL";
    public static final String CONFIG_CODE_KC_ADMIN_REALM = "KC_ADMIN_REALM";
    public static final String CONFIG_CODE_KC_ADMIN_CLIENT_ID = "KC_ADMIN_CLIENT_ID";
    public static final String CONFIG_CODE_KC_ADMIN_CLIENT_SECRET = "KC_ADMIN_CLIENT_SECRET";
    public static final String CONFIG_CODE_KC_ADMIN_USERNAME = "KC_ADMIN_USERNAME";
    public static final String CONFIG_CODE_KC_ADMIN_PASSWORD = "KC_ADMIN_PASSWORD";

    public static final String CONFIG_TYPE_KEYCLOAK_USER_SETTINGS = "KEYCLOAK_USER_SETTINGS";
    public static final String CONFIG_CODE_CONFIGURE_OTP = "KC_CONFIGURE_OTP";

    //API RESPONSE
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_ERROR = "error";
    public static final String ERR_API_MESSAGE_FORBIDDEN = "Invalid access.";

    //Config Param Types
    public static final String TYPE_ASSISTANCE_TYPE = "ASSISTANCE_TYPE";
    public static final String TYPE_DIAGNOSIS = "DIAGNOSIS";
    public static final String TYPE_MAA_MESSAGES = "MAA_MESSAGES";
    public static final String TYPE_HH_MONTHLY_INCOME = "HH_MONTHLY_INCOME";
    public static final String TYPE_MEMBER_ACCOUNT_CONFIGURATION = "MEMBER_ACCOUNT_CONFIGURATION";

    //Keys to api map
    public static final String KEY_ASSISTANCE_TYPE_LIST = "assistanceTypeList";
    public static final String KEY_DIAGNOSIS_LIST = "diagnosisList";
    public static final String KEY_MAA_MESSAGES = "maaMessages";
    public static final String KEY_MONTHLY_INCOME_LIST = "monthlyIncomeList";

    public static final String CODE_DEFAULT_MEMBER_ACCOUNT_APPLICATION_STATUS = "DEFAULT_MEMBER_ACCOUNT_APPLICATION_STATUS";

    //VOUCHER
    public static final String CONFIG_TYPE_QR_CODE_SETTINGS = "QR_CODE_SETTINGS";
    public static final String CONFIG_CODE_QR_CODE_WIDTH = "QR_CODE_WIDTH";
    public static final String CONFIG_CODE_QR_CODE_HEIGHT = "QR_CODE_HEIGHT";
    public static final String QR_CODE_WIDTH = "350";
    public static final String QR_CODE_HEIGHT = "350";
    public static final String CONFIG_CODE_QR_CODE_URL = "QR_CODE_URL";
    public static final String CODE_PNG = "PNG";

    //REPORTS
    public static final String MEMBER_REPORTS_SHEET = "Member List";
    public static final String MEMBER_REPORT_FILENAME = "Member-Report";
    public static final String KEY_REPORTS_FULL_NAME = "fullName";
    public static final String KEY_REPORTS_GENDER = "gender";
    public static final String KEY_REPORTS_DATE_OF_BIRTH = "dateOfBirth";
    public static final String KEY_REPORTS_LOCATION = "location";

    public static final String LBL_REPORTS_FULL_NAME = "Member's Name";
    public static final String LBL_REPORTS_GENDER = "Gender";
    public static final String LBL_REPORTS_DATE_OF_BIRTH = "Birth Date";
    public static final String LBL_REPORTS_LOCATION = "Location";

    //HOSPITAL REPORTS
    public static final String HOSPITAL_REPORTS_SHEET = "Hospital List";
    public static final String HOSPITAL_REPORT_FILENAME = "Hospital-Report";
    public static final String KEY_REPORTS_HOSPITAL_NAME = "hospitalName";
    public static final String KEY_REPORTS_ITEM = "item";
    public static final String KEY_REPORTS_DATE_CREATED = "dateAdded";
    
    public static final String LBL_REPORTS_HOSPITAL_NAME = "Hospital Name";
    public static final String LBL_REPORTS_ITEM_NAME_BALANCE = "Balance";
    public static final String LBL_REPORTS_ITEM_NAME_BUDGET = "Budget";
    public static final String LBL_REPORTS_DATE_CREATED = "Date";
    
    //MEMBER REPORT HEADERS (K, V)
    public static final LinkedHashMap<String, String> MEMBER_REPORTS_COLUMN_HEADERS = new LinkedHashMap<String, String>() {{
        put(KEY_REPORTS_FULL_NAME, LBL_REPORTS_FULL_NAME);
        put(KEY_REPORTS_LOCATION, LBL_REPORTS_LOCATION);
        put(KEY_REPORTS_GENDER, LBL_REPORTS_GENDER);
        put(KEY_REPORTS_DATE_OF_BIRTH, LBL_REPORTS_DATE_OF_BIRTH);
    }};

    //HOSPITAL REPORT HEADERS DYNAMIC ITEM [BALANCE (K, V)]
    public static final LinkedHashMap<String, String> HOSPITAL_REPORTS_COLUMN_DYNAMIC_HEADERS_BALANCE = new LinkedHashMap<String, String>() {{
        put(KEY_REPORTS_HOSPITAL_NAME, LBL_REPORTS_HOSPITAL_NAME);
        put(KEY_REPORTS_LOCATION, LBL_REPORTS_LOCATION);
        put(KEY_REPORTS_ITEM, LBL_REPORTS_ITEM_NAME_BALANCE);
        put(KEY_REPORTS_DATE_CREATED, LBL_REPORTS_DATE_CREATED);
    }};

    //HOSPITAL REPORT HEADERS DYNAMIC ITEM [BUDGET (K, V)]
    public static final LinkedHashMap<String, String> HOSPITAL_REPORTS_COLUMN_DYNAMIC_HEADERS_BUDGET = new LinkedHashMap<String, String>() {{
        put(KEY_REPORTS_HOSPITAL_NAME, LBL_REPORTS_HOSPITAL_NAME);
        put(KEY_REPORTS_LOCATION, LBL_REPORTS_LOCATION);
        put(KEY_REPORTS_ITEM, LBL_REPORTS_ITEM_NAME_BUDGET);
        put(KEY_REPORTS_DATE_CREATED, LBL_REPORTS_DATE_CREATED);
    }};

    public static final String CONFIG_TYPE_MEMBER_ACCOUNT_REQUIREMENTS = "MEMBER_ACCOUNT_REQUIREMENTS";
}
