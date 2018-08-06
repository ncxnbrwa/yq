package cn.iimedia.yq.HomeNewsList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.iimedia.yq.AddProjectActivity;
import cn.iimedia.yq.Base.BaseActivity;
import cn.iimedia.yq.Base.Config;
import cn.iimedia.yq.Base.utils.DLog;
import cn.iimedia.yq.Base.utils.ELS;
import cn.iimedia.yq.HomeNewsList.adapter.ProjectAdapter;
import cn.iimedia.yq.R;
import cn.iimedia.yq.http.APIConstants;
import cn.iimedia.yq.http.RequestEngine;
import cn.iimedia.yq.http.bean.DatabaseBean.ProjectDatabase;

public class ProjectManagerActivity extends BaseActivity implements OnRefreshListener {
    public static final String TAG = "ProjectManagerActivity";

    @BindView(R.id.project_manager_toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_project_list)
    LRecyclerView rvProjectList;
    @BindView(R.id.fab_add_project)
    FloatingActionButton btnAddProject;

    APIConstants apiConstants = null;
    ProjectAdapter projectAdapter = null;
    LRecyclerViewAdapter lRecyclerViewAdapter = null;
    List<ProjectDatabase> databasesList = null;
    ELS els;
    AddProjectReceiver addProjectReceiver;
    //设置加载数据,刷新完使用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiConstants = RequestEngine.createService(APIConstants.class);
        els = ELS.getInstance(this);
        addProjectReceiver = new AddProjectReceiver();
        IntentFilter filter = new IntentFilter(Config.ADD_PROJECT_ACTION);
        registerReceiver(addProjectReceiver, filter);
        if (!TextUtils.isEmpty(els.getStringData(ELS.CURRENT_TASK_NAME))) {
            toolbar.setTitle(els.getStringData(ELS.CURRENT_TASK_NAME));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.open_enter, R.anim.slide_out_left);
            }
        });
        rvProjectList.setOnRefreshListener(this);
        rvProjectList.setLayoutManager(new LinearLayoutManager(this));
        loadData();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_project_manager;
    }

    private void loadData() {
        if (databasesList == null) {
            databasesList = new ArrayList<>();
        }
        //从数据库拿到数据
        DataSupport.findAllAsync(ProjectDatabase.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                showLoadingDialog();
                List<ProjectDatabase> list = (List<ProjectDatabase>) t;
                if (EmptyUtils.isNotEmpty(list)) {
                    //数据排序
                    Collections.sort(list);
                    databasesList.addAll(list);
                    initAdapter();
                    DLog.w(TAG, "项目列表:" + databasesList.toString());
                } else {
                    showToast("当前无项目");
                }
                dismissLoadingDialog();
            }
        });
    }

    private void initAdapter() {
        if (projectAdapter == null) {
            projectAdapter = new ProjectAdapter(databasesList, this);
        } else {
            projectAdapter.notifyDataSetChanged();
        }
        if (lRecyclerViewAdapter == null) {
            lRecyclerViewAdapter = new LRecyclerViewAdapter(projectAdapter);
        } else {
            lRecyclerViewAdapter.notifyDataSetChanged();
        }
        rvProjectList.setAdapter(lRecyclerViewAdapter);
        //禁止上拉加载
        rvProjectList.setLoadMoreEnabled(false);
        rvProjectList.refreshComplete(databasesList.size());
        //设置单击事件
        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ProjectDatabase projectDatabase = databasesList.get(position);
                els.saveIntData(ELS.CURRENT_TASK_ID, projectDatabase.getProject_id());
                els.saveStringData(ELS.CURRENT_TASK_NAME, projectDatabase.getProjectname());
                //点击过的项目在SP中做+1记录
                els.saveIntData(String.valueOf(projectDatabase.getProject_id()),
                        els.getIntData(String.valueOf(projectDatabase.getProject_id())) + 1);
                //点击过的项目在数据库中做+1记录
//                List<ProjectDatabase> databaseList = DataSupport
//                        .where("project_id = ?", String.valueOf(projectDatabase.getProject_id()))
//                        .find(ProjectDatabase.class);
//                databaseList.get(0).setUseCount(projectDatabase.getUseCount() + 1);
//                databaseList.get(0).save();
//                DLog.w(TAG, "更改点击的项目:" + databaseList.get(0).toString());
                Intent intent = new Intent();
                intent.setAction(Config.CHOOSE_PROJECT_ACTION);
                sendBroadcast(intent);
                finish();
                overridePendingTransition(R.anim.open_enter, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onRefresh() {
        databasesList.clear();
        lRecyclerViewAdapter.notifyDataSetChanged();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(addProjectReceiver);
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        overridePendingTransition(R.anim.open_enter, R.anim.slide_out_left);
    }

    @OnClick(R.id.fab_add_project)
    public void addClick(View view) {
//        ActivityUtils.startActivity(this, AddProjectActivity.class);
        getBaseActivity().toActivity(AddProjectActivity.class, null);
    }

    class AddProjectReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Config.ADD_PROJECT_ACTION)) {
                databasesList.clear();
                lRecyclerViewAdapter.notifyDataSetChanged();
                loadData();
            }
        }
    }

}
