package cn.iimedia.yq

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import cn.iimedia.yq.Base.BaseActivity
import cn.iimedia.yq.Base.YqApplication
import cn.iimedia.yq.Base.utils.ELS
import cn.iimedia.yq.HomeNewsList.FragmentNews
import cn.iimedia.yq.Warning.FragmentWarning
import cn.iimedia.yq.analyze.FragmentAnalyze
import cn.iimedia.yq.mine.FragmentMine
import com.flyco.tablayout.entity.TabEntity
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_main.*

//主界面
class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    private val mContext: Context = this
    private val mFragments: ArrayList<Fragment> = ArrayList()

    private val mTitles: ArrayList<String> = arrayListOf("最新舆情", "可视数据", "预警通知", "个人中心")
    private val mIconUnselectIds = intArrayOf(R.drawable.bottom_hot, R.drawable.bottom_visibledata,
            R.drawable.bottom_warning, R.drawable.bottom_mine)
    private val mIconSelectIds = intArrayOf(R.drawable.bottom_hot_1, R.drawable.bottom_visibledata_1,
            R.drawable.bottom_warning_1, R.drawable.bottom_mine_1)
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private var els: ELS? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSwipeBackEnable(false)
        els = ELS.getInstance(YqApplication.getAppContext())
        mFragments.add(FragmentNews.getInstance())
        mFragments.add(FragmentAnalyze.getInstance())
        mFragments.add(FragmentWarning.getInstance())
        mFragments.add(FragmentMine.getInstance())

        for (i in mTitles.indices) {
            mTabEntities.add(TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]))
        }
        tabs.setTabData(mTabEntities, this, R.id.content, mFragments)
        tabs.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(current: Int) {
                val last = tabs.getLastTab()
                if (last != current) {
                    mFragments[current].userVisibleHint = true
                    mFragments[last].userVisibleHint = false
                }
            }

            override fun onTabReselect(position: Int) {}
        })

    }

    //按两次退出程序
    var clickTwiceFlag = false

    override fun onBackPressedSupport() {
        if (clickTwiceFlag) {
            super.onBackPressedSupport()
            mElfApp.finishApplication()
        } else {
            clickTwiceFlag = true
            showToast("快速按两次退出程序")
        }
        Handler().postDelayed({ clickTwiceFlag = false }, 2000)
    }
}
