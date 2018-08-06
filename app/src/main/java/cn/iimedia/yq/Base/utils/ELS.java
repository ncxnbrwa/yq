package cn.iimedia.yq.Base.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.litepal.crud.DataSupport;

import java.util.HashSet;
import java.util.Set;

public class ELS {
    private static final String ELS = "EL_SharePrefence";
    private static ELS mPref = null;
    private SharedPreferences mSharePrefer = null;
    private Editor mEditor = null;

    public static final String USERNAME = "UserName";
    public static final String PASSWORD = "Password";
    public static final String PHONE = "Phone";
    public static final String USER_IMG = "user_img";
    public static final String SESSION_KEY = "session_key";
    public static final String COOKIES = "cookies";
    public static final String COOKIES_SET = "cookies_set";
    public static final String CURRENT_TASK_ID = "task_id";
    public static final String CURRENT_TASK_NAME = "task_name";
    public static final String CURRENT_TASK_COUNT = "task_count";
    public static final String TIME_RANGE = "time_range";
    public static final String HAS_INIT_DATABASE = "init_database";
    public static final String PASSWORD_INVALID_TIME = "psw_invalid_time";

    //清除所用户相关的信息
    public void clearUserInfo() {
        mEditor.putStringSet(COOKIES_SET, null);
        mEditor.putString(COOKIES, null);
        mEditor.putBoolean(HAS_INIT_DATABASE, false);
        mEditor.putLong(PASSWORD_INVALID_TIME, 0);
        mEditor.putString(PASSWORD, null);
        DataSupport.deleteAll("ProjectDatabase");
        mEditor.apply();
    }

    //清除用户有关数据除了密码
    public void clearUserInfoExceptPsw() {
        mEditor.putStringSet(COOKIES_SET, null);
        mEditor.putString(COOKIES, null);
        mEditor.putBoolean(HAS_INIT_DATABASE, false);
        mEditor.putLong(PASSWORD_INVALID_TIME, 0);
        DataSupport.deleteAll("ProjectDatabase");
        mEditor.apply();
    }

    public void saveLongDate(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.apply();
    }

    public long getLongDate(String key) {
        return mSharePrefer.getLong(key, 0);
    }

    public void saveBoolData(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    public boolean getBoolData(String key) {
        return mSharePrefer.getBoolean(key, false);
    }

    public void saveCookieSet(HashSet<String> set) {
        mEditor.putStringSet(COOKIES_SET, set);
        mEditor.apply();
    }

    public Set<String> getCookieSet() {
        return mSharePrefer.getStringSet(COOKIES_SET, new HashSet<String>());
    }

    public void saveStringData(String key, String value) {
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public String getStringData(String key) {
        return mSharePrefer.getString(key, "");
    }

    public void saveIntData(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public int getIntData(String key, int defaultValue) {
        return mSharePrefer.getInt(key, defaultValue);
    }

    public int getIntData(String key) {
        return mSharePrefer.getInt(key, 0);
    }

    public void saveFloatData(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.apply();
    }

    public float getFloatData(String key) {
        return mSharePrefer.getFloat(key, 0);
    }


    public static synchronized ELS getInstance(Context context) {
        if (mPref == null) {
            mPref = new ELS(context);
        }
        return mPref;
    }

    private ELS(Context context) {
        mSharePrefer = context.getSharedPreferences(ELS, Context.MODE_PRIVATE);
        mEditor = mSharePrefer.edit();
    }

    /**
     * 清空 SharedPreferences
     */
    public void clear() {
        mEditor.clear();
        mEditor.apply();
    }

}
