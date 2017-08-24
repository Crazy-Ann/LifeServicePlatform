package com.service.customer.constant;

import android.Manifest;

public final class Constant {

    private Constant() {
    }

    public static final String FILE_NAME = "LifeServicePlatform";

    public static final String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE
//            , Manifest.permission.WRITE_SETTINGS
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.RECORD_AUDIO
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
            , Manifest.permission.CAMERA
            , Manifest.permission.READ_SMS
            , Manifest.permission.CALL_PHONE
            , Manifest.permission.READ_CONTACTS};

    public final static class RequestCode {
        public static final int DIALOG_PROMPT_SET_PERMISSION = 0x4999;
        public static final int PREMISSION_SETTING = 0x5000;
        public static final int NET_WORK_SETTING = 0x5001;
        public static final int DIALOG_PROMPT_SET_NET_WORK = 0x5002;
        public static final int DIALOG_PROGRESS_GET_SERVICE_INFOS = 0x5003;
        public static final int DIALOG_PROGRESS_GET_NOTIFICATION_ANNOUNCEMENT_INFOS = 0x5004;
        public static final int DIALOG_PROMPT_QUIT = 0x5005;
        public static final int DIALOG_PROMPT_GET_CONFIG = 0x5006;
        public static final int DIALOG_PROMPT_GET_CONFIG_ERROR = 0x5007;
        public static final int DIALOG_PROMPT_VERSION_UPDATE = 0x5008;
        public static final int DIALOG_PROMPT_DOWNLOAD = 0x5009;
        public static final int DIALOG_PROMPT_DOWNLOAD_ERROR = 0x5010;
        public static final int DIALOG_PROMPT_INSTALL = 0x5011;
        public static final int DIALOG_PROMPT_INSTALL_FAILED = 0x5012;
        public static final int DIALOG_PROGRESS_LOGIN = 0x5013;
        public static final int DIALOG_PROMPT_LOGIN_ERROR = 0x5014;
        public static final int DIALOG_PROMPT_CHECK_VERSION_ERROR = 0x5015;
        public static final int DIALOG_PROGRESS_MODIFY_PASSWORD = 0x5016;
        public static final int DIALOG_PROMPT_MODIFY_PASSWORD_ERROR = 0x5017;
        public static final int DIALOG_PROGRESS_MODIFY_REAL_NAME = 0x5018;
        public static final int DIALOG_PROMPT_MODIFY_REAL_NAME_ERROR = 0x5019;
        public static final int DIALOG_PROGRESS_GET_IMAGE = 0x5020;
        public static final int DIALOG_PROMPT_GET_IMAGE_ERROR = 0x5021;
        public static final int DIALOG_PROGRESS_SAVE_HEAD_IMAGE = 0x5022;
        public static final int DIALOG_PROMPT_SAVE_HEAD_IMAGE_ERROR = 0x5023;
        public static final int DIALOG_PROMPT_SELECT_IMAGE = 0x5024;
        public static final int DIALOG_PROMPT_GET_EVENT_INFOS = 0x5025;
        public static final int DIALOG_PROGRESS_SAVE_TASK_INFO = 0x5026;
        public static final int DIALOG_PROMPT_SAVE_TASK_INFO_ERROR = 0x5027;
        public static final int DIALOG_PROMPT_SAVE_TASK_INFO_SUCCESS = 0x5028;
        public static final int DIALOG_PROGRESS_LOCATION = 0x5029;
        public static final int DIALOG_PROMPT_LOCATION_ERROR = 0x5030;
        public static final int DIALOG_PROGRESS_GET_BOUNDARY = 0x5031;
        public static final int DIALOG_PROMPT_GET_BOUNDARY_ERROR = 0x5032;
        public static final int DIALOG_PROMPT_TOKEN_ERROR = 0x5033;
        public static final int DIALOG_PROMPT_MODIFY_PASSWORD_SUCCESS = 0x5034;
        public static final int DIALOG_PROGRESS_SCORE_TASK_INFO = 0x5035;
        public static final int DIALOG_PROMPT_SCORE_TASK_INFO_ERROR = 0x5036;
        public static final int DIALOG_PROMPT_SCORE_TASK_INFO_SUCCESS = 0x5037;
        public static final int DIALOG_PROGRESS_TASK_INFOS = 0x5038;
        public static final int DIALOG_PROMPT_TASK_INFOS_ERROR = 0x5039;
        public static final int DIALOG_PROGRESS_SAVE_WORK_INFO = 0x5040;
        public static final int DIALOG_PROMPT_SAVE_WORK_INFO_ERROR = 0x5041;
        public static final int DIALOG_PROMPT_SAVE_WORK_INFO_SUCCESS = 0x5042;
        public static final int DIALOG_PROMPT_SUBMIT_PICTURE_ERROR = 0x5043;
        public static final int DIALOG_PROGRESS_DEAL_TASK_INFO = 0x5044;
        public static final int DIALOG_PROMPT_DEAL_TASK_INFO_ERROR = 0x5045;
        public static final int DIALOG_PROMPT_DEAL_TASK_INFO_SUCCESS = 0x5046;
        public static final int DIALOG_PROMPT_TTS_INTIALIZED_ERROR = 0x5047;
        public static final int DIALOG_PROGRESS_SCORE_HELPER_INFO = 0x5048;
        public static final int DIALOG_PROMPT_SCORE_HELPER_INFO_ERROR = 0x5049;
        public static final int DIALOG_PROMPT_SCORE_HELPER_INFO_SUCCESS = 0x5050;
        public static final int INSTALL_APK = 0x5097;
        public static final int REQUEST_CODE_PHOTOGRAPH = 0x5098;
        public static final int REQUEST_CODE_ALBUM = 0x5099;
    }

    public final static class Profile {
        public static final String LOGIN_PROFILE = "login_profile";
        public static final String ACCOUNT = "account";
        public static final String PASSWORD = "password";
        public static final String INDEX_URL = "index_url";
        public static final String TASK_URL = "task_url";
    }

    public static class Tab {
        public static final int HOME_PAGE = 0;
        public final static int TASK_MANAGEMENT = 1;
        public final static int MINE = 2;
    }

    public class Cache {
        public static final String ROOT = "/LifeServicePlatform";
        public static final String CACHE_ROOT = ROOT + "/cache";
        public static final String LOGIN_INFO_CACHE_PATH = CACHE_ROOT + "/login_info";
    }

    public class ServiceAction {
        public static final String EMERGENCY_CALL_FOR_HELP = "emergency_call_for_help";
        public static final String APPLIANCE_MAINTENANCE = "appliance_maintenance";
        public static final String LIVING_FACILITIES_MAINTENANCE = "living_facilities_maintenance";
        public static final String OTHER_LIFE_EVENTS = "other_life_events";
        public static final String PSYCHOLOGICAL_COUNSELING = "psychological_counseling";
        public static final String DOCTOR_MEDICINE = "doctor_medicine";
        public static final String OTHER = "other";

        public static final String POLICIES_REGULATIONS = "policies_regulations";
        public static final String QUERY_ANALYSIS = "query_analysis";
        public static final String INFORMATION_MANAGEMENT = "information_management";
        public static final String EVENT_QUERY = "event_query";
        public static final String MAP_QUERY = "map_query";
    }


    public static class Message {
        public static final int GET_SERVICE_INFOS_SUCCESS = 0x7001;
        public static final int GET_SERVICE_INFOS_FAILED = 0x7002;
        public static final int GET_NOTIFICATION_ANNOUNCEMENT_INFOS_SUCCESS = 0x7003;
        public static final int GET_NOTIFICATION_ANNOUNCEMENT_INFOS_FAILED = 0x7004;
        public static final int GET_IMAGE_SUCCESS = 0x7005;
        public static final int GET_IMAGE_FAILED = 0x7006;
    }

    public static class Map {
        public static final float ZOOM = 10;
        public static final float TILT = 0;
        public static final float BEARING = 0;
        public static final long LOCATION_INTERVAL = 2000;
        public static final long LOCATION_TIME_OUT = 30000;
    }

    public static class JavaScript {
        public static final String INJECTED_NAME = "LifeServicePlatform";
        public static final String TAG = "tag";
        public static final String PARAMETER = "parameter";
        public static final String TITLE = "title";
        public static final String URL = "url";
        public static final String PHONE = "phone";
        public static final String TASK_TYPE = "tasktype";
        public static final String BILL_NO = "billno";

        public static final String TASK_EVALUATION = "task_evaluation";
        public static final String HELPER_EVALUATION = "helper_evaluation";
        public static final String MAP_QUERY = "map_query";
        public static final String WORK_LOG = "work_log";

        public static final String NEW_WAP_PAGE = "new_wap_page";
        public static final String TASK_PROCESSING = "task_processing";
        public static final String TASK_SUBMIT = "task_submit";
        public static final String PHONE_CALL = "phone_call";
    }

    public static class DEAL_STATUS {
        public static final int PROCESSING_COMPLETED = 2;
        public static final int CAN_NOT_HANDLE = 4;
    }

    public static class ASSET_URL {
        public static final String VOLUNTEER_INDEX = "file:///android_asset/h5/zView/Index.html";
        public static final String DEMANDER_INDEX = "file:///android_asset/h5/mView/Index.html";
        public static final String GREETING_CARD_LIST = "file:///android_asset/h5/mView/GreetingCardList.html";
        public static final String TASK_LIST = "file:///android_asset/h5/comView/TaskList.html";
    }
}
