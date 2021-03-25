package constants;

/**
 * Created by sureshkumar on 12/31/2016.
 */
public interface AppConstants {

    String APP_ID = "CARC_and_";
    String APP_NAME = "CARC";
    // SENDER ID GOT FROM DEVELOPER CONSOLE
    String SENDER_ID = "83483254989";
    String TRANSLATION_API_KEY = "AIzaSyAsUilsmtM6pSTz557Pj8oxq0_xH" +
            "AABEq0";
    String PATH_FOR_DATA = "/storage/emulated/0/CARC/Schedules.json";
    String PATH_FOR_IMAGE = "/storage/emulated/0/CARC/Images/";
    String PATH_FOR_VIDEO = "/storage/emulated/0/CARC/Videos/";
    String PATH_FOR_AUDIO = "/storage/emulated/0/CARC/Audios/";
    String SERVER_PATH = "https://demo.iallways.com/CARC/";
    String GEOFENCES_URL = "https://app.contexthub.com/api/geofences";
    String POST_ACTIVITY_EVENT_URL = "https://demo.iallways.com/carc/postEventToQ.jsp?";
    long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60 * 1000;
    // public String API_KEY = "AIzaSyAsUilsmtM6pSTz557Pj8oxq0_xHAABEq0";
    String APP_PREF = "app_pref.pref";
    String APP_PREF_GCM_ID = "gcm_id";
    //public String BEACON_UUID = "f7826da6-4fa2-4e98-8024-bc5b71e0893e";
    String BEACON_UUID = "46FB9109-57C6-42F4-9149-9B58ACFAD3C0";
    boolean NO_STORE_TO_STACK = false;
    //public String ABS_PING_URL = "http://developer.iallways.com/use/html5/index.jsp?&deviceName=MA&deviceOS=and";
    String ABS_PING_URL = "https://demo.iallways.com/carc/html5_CARC/index.jsp?&deviceName=MA&deviceOS=and";
    //public String ABS_PING_URL = "http://developer.iallways.com/use/html5_WW/indexBeacon.html";
    enum ReqTypes {
        ping, openPWS, openMainMenuItem, submitUserInput, execRltdSrv, call, error, webview, empty
    }

    String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    String SERVER_CLIENT_ID = "752707659644-em7i5j4dfaaqjt1i7jk8ert4hdaicsdg.apps.googleusercontent.com";// google sign in
    String CLIENT_SECRET = "TkDP5jqmOwlCNjXSQeZGgECL";
    String OAUTH_URL = "https://www.googleapis.com/oauth2/v4/token";
}

