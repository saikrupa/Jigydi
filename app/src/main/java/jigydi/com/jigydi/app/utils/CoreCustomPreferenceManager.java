package jigydi.com.jigydi.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by click2clinic on 05-07-2017.
 */

public class CoreCustomPreferenceManager {
    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;
    private static CoreCustomPreferenceManager manager;

    public static CoreCustomPreferenceManager getInstance(Context activity) {
        if (activity != null) {
            sharedPref = activity.getSharedPreferences("C2C_APP", Context.MODE_PRIVATE);
            editor = sharedPref.edit();
        }
        if (manager == null) {
            manager = new CoreCustomPreferenceManager();
        }
        return manager;
    }

    public void setSharedStringValue(String key, String value) {
        if (editor != null && !TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            editor.putString(key, value);
            editor.commit();
        }
    }
    public void setSharedDoubleValue(String key, Double value) {
        if (editor != null && !TextUtils.isEmpty(key)) {
            editor.putLong(key, Double.doubleToRawLongBits(value));
            editor.commit();
        }

    }
    public Double getSharedDoubleValue(String key) {
        return Double.longBitsToDouble(sharedPref.getLong(key, Double.doubleToRawLongBits(-1.0)));
    }

    public void setUserLoginSucess(boolean isLogin) {
        if (!TextUtils.isEmpty("" + isLogin)) {
            editor.putBoolean(PreferenceKeys.IS_USER_LOGIN, isLogin);
            editor.commit();
        }

    }

    public boolean isUserLoginSucess() {
        return sharedPref.getBoolean(PreferenceKeys.IS_USER_LOGIN, false);
    }


    public static void clerData(Context context) {
        sharedPref = context.getSharedPreferences("C2C_APP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();

    }

    public void setSharedIntValue(String key, int value) {
        if (editor != null && !TextUtils.isEmpty(key)) {
            editor.putInt(key, value);
            editor.commit();
        }

    }

    public int getSharedIntValue(String key) {
        return sharedPref.getInt(key, -1);
    }

    public String getSharedStringValue(String key) {
        return sharedPref.getString(key, "");
    }

    public class PreferenceKeys {
        //        public final static String APP_TYPE= "app_type";
        public final static String USER_TYPE = "user_type";
        public final static String USER_ID = "user_id";
        public final static String WORKER_ID = "worker_id";
        public final static String MEMBER_TYPE = "mem_type";
        public final static String WORKER_NAME = "worker_name";
        public final static String SERVICE_NAME = "SERVICE_NAME";
        public final static String IS_USER_LOGIN = "is_user_login";



        public final static String FB_ID = "fb_id";


        public static final String PROFILE_IMAGE = "PROFILE_IMAGE";
        public static final String USER_PROFILE_PHONE_NO = "USER_PROFILE_PHONE_NO";
        public final static String PUSH_REG_STATUS = "PUSH_REG_STATUS";
        public final static String FROM_WHICH_APP = "from_which_app";
        public final static String COUNTRY_CODE = "COUNTRY_CODE";
        public final static String COUNTRY_NAME = "COUNTRY_NAME";
        public final static String PUSH_GCM_STATUS = "PUSH_GCM_STATUS";
        public final static String USER_QUICKBLOX_ID = "USER_QUICKBLOX_ID";
        public final static String VERIFIED_HCP_ID = "VERIFIED_HCP_ID";


        //Main Key
        public final static String IS_FIRST_LOGIN = "IS_FIRST_LOGIN";
        public static final String SELECTED_PATIENT_NAME = "SELECTED_PATIENT_NAME";


        //Doctor KEY
        public final static String IS_DOCTOR = "is_doctor";
        public final static String DOCTOR_ID = "doctor_id";
        public final static String DOCTOR_NAME = "doctor_id";
        public final static String DOCTOR_CITY = "doctor_id";
        public final static String DOCTOR_IMAGE = "doctor_id";

        //For Home Icons ViewPager
        public final static String CLICKED_POSITION = "CLICKED_POSITION";

        // For map activity

        public final static String LATITUDE="lat";
        public final static String LONGITUDE="long";

    }


    public static void remove(Context context, String key) {

        editor.putString(key, "");
        editor.commit();


    }


    public boolean getSharedBooleanValue(String key) {
        return sharedPref.getBoolean(key, false);
    }

    public void setSharedBooleanValue(String key, boolean value) {
        if (editor != null && !TextUtils.isEmpty(key)) {
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public boolean isFirstLogin() {
        return sharedPref.getBoolean(PreferenceKeys.IS_FIRST_LOGIN, false);
    }

    public void setFirstLogin() {

        editor.putBoolean(PreferenceKeys.IS_FIRST_LOGIN, true);
        editor.commit();

    }

}
