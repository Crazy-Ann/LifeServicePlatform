package com.service.customer.constant;

import android.Manifest;

import com.amap.api.maps.model.LatLng;

public final class Constant {

    private Constant() {
    }

    public static final int RETRY_TIME = 3;
    public static final String FILE_NAME = "LifeServicePlatform";

    public static final String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE
//            , Manifest.permission.WRITE_SETTINGS
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.RECORD_AUDIO
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_COARSE_LOCATION
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
        public static final int DIALOG_PROGRESS_LOCATION = 0x5028;
        public static final int DIALOG_PROMPT_LOCATION_ERROR = 0x5029;
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
        public static final String PAGE_DATA_CACHE_PATH = CACHE_ROOT + "/page";
        public static final String PAGE_IMAGE_CACHE_PATH = PAGE_DATA_CACHE_PATH + "/image";
        public static final String SERVICE_DATA_CACHE_PATH = CACHE_ROOT + "/service";
        public static final String SERVICE_IMAGE_CACHE_PATH = SERVICE_DATA_CACHE_PATH + "/icon";
        public static final String TAB_DATA_CACHE_PATH = CACHE_ROOT + "/tab";
        public static final String TAB_IMAGE_CACHE_PATH = TAB_DATA_CACHE_PATH + "/icon";
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
        public static final int GET_EVENT_INFOS_SUCCESS = 0x7005;
        public static final int GET_EVNET_INFOS_FAILED = 0x7006;
        public static final int GET_IMAGE_SUCCESS = 0x7007;
        public static final int GET_IMAGE_FAILED = 0x7008;
    }

    public static class Map {
        public static final LatLng MIYUN_DISTRICT = new LatLng(40.376834, 116.843177);
        public static final float ZOOM = 15;
        public static final float TILT = 0;
        public static final float BEARING = 0;
        public static final long LOCATION_INTERVAL = 2000;
        public static final long LOCATION_TIME_OUT = 30000;
    }
}
