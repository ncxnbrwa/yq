package cn.iimedia.yq.Base;

/**
 * Created by iiMedia on 2017/12/14.
 * 一些不变的参数
 */

public class Config {
    public static int VERSION_SDK_INT = 15;
    public static String CLIENT_VERSION = "1.0";
    public static String API_VERSION = "1.0";
    public static final String MOBILE_TYPE = "ANDROID";
    public static final String LOG_TAG = "yq_test";

    //微信链接必含参数
    public static final String WEXIN_URL = "mp.weixin.qq.com/s";
    //微信包名
    public static final String WEXIN_PACKAGE = "com.tencent.mm";
    //微信首页class
    public static final String WEXIN_HOME_CLASS = "com.tencent.mm.ui.LauncherUI";
    public static final String WARNING_PROJECT_ID = "project_id";
    public static final String WARNING_DATE = "dateline";
    public static final String LOAD_URL = "src_url";

    //广播action
    public static final String CHOOSE_PROJECT_ACTION = "choose_project_receiver_action";
    public static final String ADD_PROJECT_ACTION = "add_project_receiver_action";
    public static final String CHOOSE_TIME_RANGE_ACTION = "time_range_receiver_action";

    //时间范围
    public static final String TIME_24H = "24-hour";
    public static final String TIME_YESTERDAY = "lastday";
    public static final String TIME_WEEK = "week";
    public static final String TIME_MONTH = "month";

    //Bugly注册ID
    public static final String BUGLY_ID = "fb27ffd2e2";
}
