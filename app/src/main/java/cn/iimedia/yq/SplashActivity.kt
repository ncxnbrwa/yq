package cn.iimedia.yq

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import cn.iimedia.yq.Base.BaseActivity
import cn.iimedia.yq.Base.YqApplication
import cn.iimedia.yq.Base.utils.ELS

/**
 * 启动图页面
 */
class SplashActivity : BaseActivity() {
    var els: ELS? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSwipeBackEnable(false)
        els = ELS.getInstance(YqApplication.getAppContext())


        Handler(Looper.getMainLooper()).postDelayed({
            //判断密码是否失效
            val time = els?.getLongDate(ELS.PASSWORD_INVALID_TIME)
            if (time != 0L && System.currentTimeMillis() > time as Long) {
                showToast("密码过期,请重新登录")
                els?.clearUserInfo()
                toActivity(LoginActivity::class.java, null)
                finish()
                return@postDelayed
            }
            //根据本地是否有session_key判断跳转
            if ((els?.cookieSet)!!.isNotEmpty()) {
                toActivity(MainActivity::class.java, null)
                finish()
            } else {
                toActivity(LoginActivity::class.java, null)
                finish()
            }
        }, 2000)

    }

    override fun onBackPressedSupport() {
        mElfApp.finishApplication()
        super.onBackPressedSupport()
    }

}
