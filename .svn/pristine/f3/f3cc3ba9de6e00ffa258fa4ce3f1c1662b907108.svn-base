package cn.iimedia.yq.Warning;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.iimedia.yq.Base.BaseActivity;
import cn.iimedia.yq.Base.Config;
import cn.iimedia.yq.Base.utils.MyUtils;
import cn.iimedia.yq.Base.utils.TimeTypeUtils;
import cn.iimedia.yq.HomeNewsList.WebDetailsActivity;
import cn.iimedia.yq.R;
import cn.iimedia.yq.Warning.adapter.WarningDetailAdapter;
import cn.iimedia.yq.custom.LinearLayoutManagerWrapper;
import cn.iimedia.yq.http.APIConstants;
import cn.iimedia.yq.http.RequestEngine;
import cn.iimedia.yq.http.bean.ResponseBean.WarningNewsBean;
import cn.iimedia.yq.http.bean.ResponseBean.WarningNewsRecord;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarningDetailActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.warning_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_warning_detail_list)
    LRecyclerView rvDetailList;

    APIConstants apiConstants;
    List<WarningNewsRecord> newsList;
    LRecyclerViewAdapter lRecyclerViewAdapter;
    WarningDetailAdapter warningNewsAdapter;
    int projectId = 0, pageSize = 10, num = 1;
    String dateline;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_warning_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取传值
        Bundle bundle = getIntent().getExtras();
        if (EmptyUtils.isNotEmpty(bundle.getInt(Config.WARNING_PROJECT_ID))) {
            projectId = bundle.getInt(Config.WARNING_PROJECT_ID);
        }
        dateline = TimeTypeUtils.timestamp2String8(bundle.getString(Config.WARNING_DATE), true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        apiConstants = RequestEngine.createService(APIConstants.class);
        rvDetailList.setLayoutManager(new LinearLayoutManagerWrapper(this));
        rvDetailList.setOnRefreshListener(this);
        rvDetailList.setOnLoadMoreListener(this);
        getData(true);
    }

    @Override
    public void onLoadMore() {
        num++;
        getData(false);
    }

    @Override
    public void onRefresh() {
        num = 1;
        if (EmptyUtils.isNotEmpty(newsList)) {
            newsList.clear();
            lRecyclerViewAdapter.notifyDataSetChanged();
        }
        getData(false);
    }

    //拿数据
    private void getData(final boolean loading) {
        if (loading) {
            showLoadingDialog();
        }
        Call<WarningNewsBean> getWarningNewsCall = apiConstants.getWarningDetail(projectId, dateline,
                pageSize, num);
        getWarningNewsCall.enqueue(new Callback<WarningNewsBean>() {
            @Override
            public void onResponse(Call<WarningNewsBean> call, Response<WarningNewsBean> response) {
                if (EmptyUtils.isNotEmpty(response.body())) {
                    WarningNewsBean warningNewsBean = response.body();
                    MyUtils.reLogin(warningNewsBean.getCode(), warningNewsBean.getLogin_error(), getBaseActivity());
                    if (EmptyUtils.isNotEmpty(warningNewsBean.getNew_records())) {
                        List<WarningNewsRecord> list = warningNewsBean.getNew_records();
                        if (newsList == null) {
                            newsList = new ArrayList<>();
                        }
                        newsList.addAll(list);
                        removeRepetition(newsList);
                        initAdapter();
                        if (list.size() < pageSize) {
                            rvDetailList.setNoMore(true);
                        }
                    } else {
                        showToast("当前无数据");
                    }
                }
                if (loading) {
                    dismissLoadingDialog();
                }
//                initAdapter();
                rvDetailList.refreshComplete(pageSize);
            }

            @Override
            public void onFailure(Call<WarningNewsBean> call, Throwable t) {
                showToast("获取失败");
                if (loading) {
                    dismissLoadingDialog();
                }
                rvDetailList.refreshComplete(pageSize);
            }
        });
    }

    private void initAdapter() {
        if (warningNewsAdapter == null) {
            warningNewsAdapter = new WarningDetailAdapter(newsList, this);
        } else {
            warningNewsAdapter.notifyDataSetChanged();
        }
        if (lRecyclerViewAdapter == null) {
            lRecyclerViewAdapter = new LRecyclerViewAdapter(warningNewsAdapter);
            rvDetailList.setAdapter(lRecyclerViewAdapter);
        } else {
            lRecyclerViewAdapter.notifyDataSetChanged();
        }
        //添加点击
        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Config.LOAD_URL, newsList.get(position).getSrc_url());
                toActivity(WebDetailsActivity.class, bundle);
            }
        });
    }

    //去重
    private void removeRepetition(List<WarningNewsRecord> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getHash_code().equals(list.get(i).getHash_code())) {
                    list.remove(j);
                }
            }
        }
    }

}