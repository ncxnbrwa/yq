package cn.iimedia.yq.HomeNewsList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.example.zhouwei.library.CustomPopWindow;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.iimedia.yq.Base.BaseFragment;
import cn.iimedia.yq.Base.Config;
import cn.iimedia.yq.Base.YqApplication;
import cn.iimedia.yq.Base.utils.DLog;
import cn.iimedia.yq.Base.utils.ELS;
import cn.iimedia.yq.Base.utils.MyUtils;
import cn.iimedia.yq.Base.utils.TimeTypeUtils;
import cn.iimedia.yq.HomeNewsList.adapter.NewsListAdapter;
import cn.iimedia.yq.R;
import cn.iimedia.yq.custom.LinearLayoutManagerWrapper;
import cn.iimedia.yq.http.APIConstants;
import cn.iimedia.yq.http.RequestEngine;
import cn.iimedia.yq.http.bean.DatabaseBean.ProjectDatabase;
import cn.iimedia.yq.http.bean.ResponseBean.NewsBean;
import cn.iimedia.yq.http.bean.ResponseBean.NewsListBean;
import cn.iimedia.yq.http.bean.ResponseBean.ProjectBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by iiMedia on 2017/12/11.
 * 主页舆情页面
 */

public class FragmentNews extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    public static final String TAG = "FragmentNews";

    @BindView(R.id.root_view)
    CoordinatorLayout mRootView;
    @BindView(R.id.news_toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_head_right)
    TextView tvRightTime;
    @BindView(R.id.tv_plat)
    TextView tvPlat;
    @BindView(R.id.tv_emotion)
    TextView tvEmotion;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.rv_newsList)
    LRecyclerView rvNewsList;

    ELS els;
    CustomPopWindow timeChooseWindow, platChooseWindow, emotionChooseWindow;
    APIConstants apiConstants = null;
    //数据集合
    List<NewsListBean> newsList = null;
    NewsListAdapter newsListAdapter = null;
    LRecyclerViewAdapter lRecyclerViewAdapter = null;

    //platType  平台类型,默认1
    //orderType 排序类型,默认0
    //timeType  时间范围,0为24H,1为昨天,2为一周,3为30天
    //exactMode 精确匹配参数,1
    int platType = -1, orderType = 1, timeType = 3, exactMode = 0;
    int pageSize = 10;
    long pageStartTime = 0;
    String pre_hashcode = "";
    ChooseProjectReceiver chooseProjectReceiver;

    public static FragmentNews getInstance() {
        FragmentNews fragmentNews = new FragmentNews();
        return fragmentNews;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void lazyLoad() {
        loadData();
    }

    @Override
    protected void init() {
        els = ELS.getInstance(getBaseActivity());
        apiConstants = RequestEngine.createService(APIConstants.class);
        if (orderType == 1) tvRightTime.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityUtils.startActivity(getBaseActivity(), ProjectManagerActivity.class);
                getBaseActivity().toActivity(ProjectManagerActivity.class, null);
                getBaseActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.open_exit);
            }
        });
        rvNewsList.setLayoutManager(new LinearLayoutManagerWrapper(getBaseActivity()));
        rvNewsList.setOnRefreshListener(this);
        rvNewsList.setOnLoadMoreListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chooseProjectReceiver = new ChooseProjectReceiver();
        getBaseActivity().registerReceiver(chooseProjectReceiver, new IntentFilter(Config.CHOOSE_PROJECT_ACTION));
    }

    @Override
    public void onRefresh() {
        if (EmptyUtils.isNotEmpty(newsList)) {
            newsList.clear();
            lRecyclerViewAdapter.notifyDataSetChanged();
        }
        pageStartTime = 0;
        pre_hashcode = "";
        getNewsData(0, "", false);
    }

    @Override
    public void onLoadMore() {
        //加载更多需要更新hash和时间
        getNewsData(pageStartTime, pre_hashcode, false);
    }

    private void loadData() {
        showLoadingDialog();
        //先保存拥有项目到数据库
        Call<ProjectBean> projectCall = apiConstants.getCurrentProject(System.currentTimeMillis(), "desc");
        projectCall.enqueue(new Callback<ProjectBean>() {
            @Override
            public void onResponse(Call<ProjectBean> call, Response<ProjectBean> response) {
                if (getBaseActivity() == null) return;
                if (EmptyUtils.isNotEmpty(response.body())) {
                    ProjectBean bean = response.body();
                    DLog.w(TAG, bean.toString());
                    MyUtils.reLogin(bean.getCode(), bean.getLogin_error(), getBaseActivity());
                    if (bean.getRecords() != null) {
                        List<ProjectBean.RecordsBean> recordsList = bean.getRecords();
                        if (!recordsList.isEmpty()) {
                            btnConfirm.setEnabled(true);
                            els.saveIntData(ELS.CURRENT_TASK_COUNT, recordsList.size());
                            boolean hasInitDatabse = els.getBoolData(ELS.HAS_INIT_DATABASE);
                            if (!hasInitDatabse) {
                                initDatabase(recordsList);
                                els.saveBoolData(ELS.HAS_INIT_DATABASE, true);
                            } else {
                                updateDatabase(recordsList);
                            }
                        } else {
                            els.saveIntData(ELS.CURRENT_TASK_ID, 0);
                            els.saveStringData(ELS.CURRENT_TASK_NAME, "");
                            btnConfirm.setEnabled(false);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ProjectBean> call, Throwable t) {
                if (getBaseActivity() == null) return;
                dismissLoadingDialog();
                els.saveIntData(ELS.CURRENT_TASK_ID, 0);
                els.saveStringData(ELS.CURRENT_TASK_NAME, "");
                btnConfirm.setEnabled(false);
                showToast("项目获取失败");
                DLog.w(TAG, "项目获取失败信息:" + t.getMessage());
            }
        });

    }

    //更新数据库
    private void updateDatabase(List<ProjectBean.RecordsBean> recordsList) {
        for (int i = 0; i < recordsList.size(); i++) {
            ProjectBean.RecordsBean recordsBean = recordsList.get(i);
            List<ProjectDatabase> databaseList = DataSupport
                    .where("project_id = ?", String.valueOf(recordsBean.getProject_id()))
                    .find(ProjectDatabase.class);
            if (databaseList.size() == 0) {
                //数据库中不存在该数据,则新建一个
                ProjectDatabase projectDatabase = new ProjectDatabase();
                projectDatabase.setProject_id(recordsBean.getProject_id());
                projectDatabase.setProjectname(recordsBean.getProjectname());
                projectDatabase.save();
            } else {
                //数据库中存在该数据,则更新
//                ProjectDatabase database = databaseList.get(0);
//                database.setProject_id(recordsBean.getProject_id());
//                database.setProjectname(recordsBean.getProjectname());
//                database.save();
            }
        }
        DataSupport.findAllAsync(ProjectDatabase.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                afterFindAllDatabase((List<ProjectDatabase>) t);
                DLog.w(TAG, "更新数据库保存数据:" + t.toString());
            }
        });
    }

    //第一次登陆初始数据库
    private void initDatabase(List<ProjectBean.RecordsBean> recordsList) {
        List<ProjectDatabase> databaseList = new ArrayList<>();
        for (int i = 0; i < recordsList.size(); i++) {
            ProjectBean.RecordsBean recordsBean = recordsList.get(i);
            ProjectDatabase projectDatabase = new ProjectDatabase();
            projectDatabase.setProject_id(recordsBean.getProject_id());
            projectDatabase.setProjectname(recordsBean.getProjectname());
            databaseList.add(projectDatabase);
            //用sp保存点击次数
        }
        //项目存入数据库
        DataSupport.saveAllAsync(databaseList).listen(new SaveCallback() {
            @Override
            public void onFinish(boolean success) {
                DataSupport.findAllAsync(ProjectDatabase.class).listen(new FindMultiCallback() {
                    @Override
                    public <T> void onFinish(List<T> t) {
                        afterFindAllDatabase((List<ProjectDatabase>) t);
                        DLog.w(TAG, "初始化数据库保存数据:" + t.toString());
                    }
                });
            }
        });

    }

    private void afterFindAllDatabase(List<ProjectDatabase> t) {
        if (EmptyUtils.isNotEmpty(t)) {
            Collections.sort(t);
            els.saveIntData(ELS.CURRENT_TASK_ID, t.get(0).getProject_id());
            els.saveStringData(ELS.CURRENT_TASK_NAME, t.get(0).getProjectname());
            //数据库操作完毕后发送广播来拉取新闻,更新图表页标题
            getBaseActivity().sendBroadcast(new Intent(Config.CHOOSE_PROJECT_ACTION));
            if (EmptyUtils.isNotEmpty(toolbar)) {
                toolbar.setTitle(t.get(0).getProjectname());
            }
        }
    }

    /**
     * 获取新闻数据
     *
     * @param pageStart 用于分页使用,第一次传空,以后更新为返回的新时间戳
     * @param hashcode  用于分页使用,第一次传空,以后更新为最后一项的hashcode
     * @param loading   是否显示加载框
     */
    private void getNewsData(long pageStart, String hashcode, final boolean loading) {
        if (els.getIntData(ELS.CURRENT_TASK_ID) == 0) return;
        if (loading) {
            showLoadingDialog();
        }
        Map<String, Object> newsMap = new HashMap<>();
        if (pageStart == 0) {
            newsMap.put("pagestart", "");  //开始时间,用于分页,第一次传空
        } else {
            newsMap.put("pagestart", pageStart);
        }
        newsMap.put("pagesize", pageSize);  //每页显示新闻条数,默认6
        newsMap.put("task_id", els.getIntData(ELS.CURRENT_TASK_ID, 0)); //项目ID
        if (timeType == 0) {
            newsMap.put("start", TimeTypeUtils.get24HourBeforeTimestamp() / 1000);  //分页开始时间,开始时间戳
            newsMap.put("end", System.currentTimeMillis() / 1000);  //分页结束时间,结束时间戳
        } else if (timeType == 1) {
            newsMap.put("start", TimeTypeUtils.getDayBeforeTimestamp() / 1000);
            newsMap.put("end", TimeTypeUtils.getDayBeforeEndTimestamp() / 1000);
        } else if (timeType == 2) {
            newsMap.put("start", TimeTypeUtils.getWeekBeforeTimestamp() / 1000);
            newsMap.put("end", System.currentTimeMillis() / 1000);
        } else if (timeType == 3) {
            newsMap.put("start", TimeTypeUtils.getMonthBeforeTimestamp() / 1000);
            newsMap.put("end", System.currentTimeMillis() / 1000);
        }
        if (platType == -1) {
            newsMap.put("type", "");
        } else {
            newsMap.put("type", platType);  //媒体类型,网页-1微信-2 微博-3 APP-4 论坛-5 报刊-6 视频-7
        }
        if (orderType == -1) {
            newsMap.put("order_type", "");
        } else {
            newsMap.put("order_type", orderType); //排序方式,1为相关度排序
            if (exactMode == 1) {
                newsMap.put("mode", exactMode);
                newsMap.put("start", 0);
                newsMap.put("end", 0);
            }
//            else {
//                newsMap.put("mode", "");
//                newsMap.put("start", TimeTypeUtils.getMonthBeforeTimestamp() / 1000);
//                newsMap.put("end", System.currentTimeMillis() / 1000);
//            }
        }
        newsMap.put("emo_type", "");
        newsMap.put("sign", "");  //标识号,用来标识有效用户
//        newsMap.put("search_type", "");  //搜索类型,标题-0 全文-1 来源-2 作者-3
        newsMap.put("skey", "");  //搜索词
        newsMap.put("pre_hashcode", hashcode); //hashcode,用于分页,第一次传空
        //参数拼成字符串传入
        String param = "";
        for (Map.Entry<String, Object> entry : newsMap.entrySet()) {
            param += entry.getKey() + "=" + entry.getValue() + "&";
        }

        Call<NewsBean> newsCall = apiConstants.getNews(param);
        newsCall.enqueue(new Callback<NewsBean>() {
            @Override
            public void onResponse(Call<NewsBean> call, Response<NewsBean> response) {
                if (getBaseActivity() == null) return;
                if (EmptyUtils.isNotEmpty(response.body())) {
                    btnConfirm.setEnabled(true);
                    NewsBean newsBean = response.body();
                    MyUtils.reLogin(newsBean.getCode(), newsBean.getLogin_error(), getBaseActivity());
                    if (newsBean.getCode() == 0) {
                        if (newsList == null) {
                            newsList = new ArrayList<>();
                        }
                        if (newsBean.getData() != null) {
                            if (EmptyUtils.isNotEmpty(newsBean.getData().getList())) {
                                List<NewsListBean> list = newsBean.getData().getList();
                                pageStartTime = newsBean.getData().getPage().getPagestart();
                                DLog.w(TAG, "pageStart:" + pageStartTime);
                                newsList.addAll(list);
                                removeRepetition(newsList);
                                pre_hashcode = newsList.get(newsList.size() - 1).getHash_code();
                                initAdapter();

                                if (list.size() < pageSize) {
                                    rvNewsList.setNoMore(true);
                                }
                            } else {
                                showToast("暂时没更多数据哦~~");
                            }
                        }
                    }
                }
                if (loading) {
                    dismissLoadingDialog();
                }
//                initAdapter();
                rvNewsList.refreshComplete(pageSize);
            }

            @Override
            public void onFailure(Call<NewsBean> call, Throwable t) {
                if (getBaseActivity() == null) return;
                if (loading) {
                    dismissLoadingDialog();
                }
                rvNewsList.refreshComplete(pageSize);
                showToast("数据获取失败");
                DLog.w(TAG, "新闻拉取失败信息:" + t.getMessage());
            }
        });
    }

    //去重
    private void removeRepetition(List<NewsListBean> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getNews_id().equals(list.get(i).getNews_id())) {
                    list.remove(j);
                }
            }
        }
    }

    //适配Adapter
    private void initAdapter() {
        if (newsListAdapter == null) {
            String key = "";
            if (!TextUtils.isEmpty(toolbar.getTitle().toString())) {
                key = toolbar.getTitle().toString();
            }
            newsListAdapter = new NewsListAdapter(newsList, getBaseActivity(), key);
        } else {
            newsListAdapter.notifyDataSetChanged();
        }
        if (lRecyclerViewAdapter == null) {
            lRecyclerViewAdapter = new LRecyclerViewAdapter(newsListAdapter);
            rvNewsList.setAdapter(lRecyclerViewAdapter);
        } else {
            lRecyclerViewAdapter.notifyDataSetChanged();
        }
        dismissLoadingDialog();
        //设置item点击
        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String url = newsList.get(position).getSrc_url();
                Bundle bundle = new Bundle();
                bundle.putString(Config.LOAD_URL, url);
                getBaseActivity().toActivity(WebDetailsActivity.class, bundle);
            }
        });
    }

    //存项目到数据库,有卡顿的风险
    private void saveProject2Database(List<ProjectBean.RecordsBean> recordsList, int i) {
        ProjectBean.RecordsBean recordsBean = recordsList.get(i);
        ProjectDatabase projectDatabase = new ProjectDatabase();
        projectDatabase.setProject_id(recordsBean.getProject_id());
        projectDatabase.setProjectname(recordsBean.getProjectname());
        projectDatabase.setKeywords(recordsBean.getKeywords());
        projectDatabase.setModel(recordsBean.getModel());
        projectDatabase.setRelated_keywords(recordsBean.getRelated_keywords());
        projectDatabase.setProject_keyword(recordsBean.getProject_keyword());
        projectDatabase.save();
    }


    @OnClick({R.id.tv_head_right, R.id.tv_plat, R.id.tv_emotion, R.id.btn_confirm})
    public void otherClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right:
                View headRightView = LayoutInflater.from(getBaseActivity())
                        .inflate(R.layout.popwindow_home_head_time, mRootView, false);
                handleHeadRightViewClick(headRightView);
                if (timeChooseWindow == null) {
                    timeChooseWindow = new CustomPopWindow.PopupWindowBuilder(YqApplication.getAppContext())
                            .setView(headRightView)
                            .setOutsideTouchable(true)
                            .setFocusable(true)
                            .create()
                            .showAsDropDown(toolbar, toolbar.getWidth() - tvRightTime.getWidth(), 0);
                } else {
                    timeChooseWindow.showAsDropDown(toolbar, toolbar.getWidth() - tvRightTime.getWidth(), 0);
                }
                break;
            case R.id.tv_plat:
                View platChooseView = LayoutInflater.from(getBaseActivity())
                        .inflate(R.layout.popwindow_home_plat, mRootView, false);
                handlePlatChooseViewClick(platChooseView);
                if (platChooseWindow == null) {
                    platChooseWindow = new CustomPopWindow.PopupWindowBuilder(YqApplication.getAppContext())
                            .setView(platChooseView)
                            .setFocusable(true)
                            .setOutsideTouchable(true)
                            .create()
                            .showAsDropDown(tvPlat);
                } else {
                    platChooseWindow.showAsDropDown(tvPlat);
                }
                break;
            case R.id.tv_emotion:
                View emotionChoosView = LayoutInflater.from(getBaseActivity())
                        .inflate(R.layout.popwindow_home_emotion, mRootView, false);
                handleSortChooseViewClick(emotionChoosView);
                if (emotionChooseWindow == null) {
                    emotionChooseWindow = new CustomPopWindow.PopupWindowBuilder(YqApplication.getAppContext())
                            .setView(emotionChoosView)
                            .setFocusable(true)
                            .setOutsideTouchable(true)
                            .create()
                            .showAsDropDown(tvEmotion);
                } else {
                    emotionChooseWindow.showAsDropDown(tvEmotion);
                }
                break;
            case R.id.btn_confirm:
                //选好平台和情感后单击刷新数据
                if (EmptyUtils.isNotEmpty(newsList)) {
                    newsList.clear();
                    lRecyclerViewAdapter.notifyDataSetChanged();
                }
                pageStartTime = 0;
                pre_hashcode = "";
                getNewsData(0, "", true);
                break;
        }
    }

    //时间选择弹窗的点击事件
    private void handleHeadRightViewClick(View contentView) {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EmptyUtils.isNotEmpty(timeChooseWindow)) {
                    timeChooseWindow.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.tv_24H:
                        tvRightTime.setText("24H");
                        timeType = 0;
                        break;
                    case R.id.tv_yesterday:
                        tvRightTime.setText("昨天");
                        timeType = 1;
                        break;
                    case R.id.tv_aweek:
                        tvRightTime.setText("一周");
                        timeType = 2;
                        break;
                    case R.id.tv_amonth:
                        tvRightTime.setText("30天");
                        timeType = 3;
                        break;
                }
                if (EmptyUtils.isNotEmpty(newsList)) {
                    newsList.clear();
                    lRecyclerViewAdapter.notifyDataSetChanged();
                }
                pageStartTime = 0;
                pre_hashcode = "";
                getNewsData(0, "", true);
            }
        };
        contentView.findViewById(R.id.tv_24H).setOnClickListener(clickListener);
        contentView.findViewById(R.id.tv_yesterday).setOnClickListener(clickListener);
        contentView.findViewById(R.id.tv_aweek).setOnClickListener(clickListener);
        contentView.findViewById(R.id.tv_amonth).setOnClickListener(clickListener);
    }

    //平台选择弹窗的点击处理
    private void handlePlatChooseViewClick(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EmptyUtils.isNotEmpty(platChooseWindow)) {
                    platChooseWindow.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.tv_plat_all:
                        platType = -1;
                        tvPlat.setText("全部平台");
                        break;
                    case R.id.tv_plat_web:
                        tvPlat.setText("网页");
                        platType = 1;
                        break;
                    case R.id.tv_plat_wechat:
                        tvPlat.setText("微信");
                        platType = 2;
                        break;
                    case R.id.tv_plat_weibo:
                        tvPlat.setText("微博");
                        platType = 3;
                        break;
                    case R.id.tv_plat_app:
                        tvPlat.setText("APP");
                        platType = 4;
                        break;
                    case R.id.tv_plat_forum:
                        tvPlat.setText("论坛");
                        platType = 5;
                        break;
                    case R.id.tv_plat_newspaper:
                        tvPlat.setText("报刊");
                        platType = 6;
                        break;
                    case R.id.tv_plat_video:
                        tvPlat.setText("视频");
                        platType = 7;
                        break;
                }
            }
        };
        contentView.findViewById(R.id.tv_plat_all).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_plat_web).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_plat_wechat).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_plat_weibo).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_plat_app).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_plat_forum).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_plat_newspaper).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_plat_video).setOnClickListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getBaseActivity().unregisterReceiver(chooseProjectReceiver);
    }

    private void handleSortChooseViewClick(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EmptyUtils.isNotEmpty(emotionChooseWindow)) {
                    emotionChooseWindow.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.tv_sort_relevancy:
                        tvEmotion.setText("相关度排序");
                        orderType = 1;
                        exactMode = -1;
                        timeType = 3;
                        tvRightTime.setText("30天");
                        tvRightTime.setVisibility(View.GONE);
                        break;
                    case R.id.tv_sort_exact:
                        tvEmotion.setText("精确匹配");
                        orderType = 1;
                        exactMode = 1;
                        tvRightTime.setVisibility(View.GONE);
                        break;
                    case R.id.tv_sort_time:
                        tvEmotion.setText("时间降序");
                        orderType = -1;
                        exactMode = -1;
                        tvRightTime.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
        contentView.findViewById(R.id.tv_sort_relevancy).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_sort_exact).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_sort_time).setOnClickListener(listener);
    }

    class ChooseProjectReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Config.CHOOSE_PROJECT_ACTION)) {
                if (EmptyUtils.isNotEmpty(newsList)) {
                    newsList.clear();
                    lRecyclerViewAdapter.notifyDataSetChanged();
                }
                getNewsData(0, "", true);
                if (EmptyUtils.isNotEmpty(newsListAdapter)) {
                    newsListAdapter.setKey(els.getStringData(ELS.CURRENT_TASK_NAME));
                }
                toolbar.setTitle(els.getStringData(ELS.CURRENT_TASK_NAME));
            }
        }
    }
}
