package cn.iimedia.yq.analyze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.EmptyUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.iimedia.yq.Base.BaseFragment;
import cn.iimedia.yq.Base.Config;
import cn.iimedia.yq.Base.utils.ELS;
import cn.iimedia.yq.Base.utils.MyUtils;
import cn.iimedia.yq.R;
import cn.iimedia.yq.custom.chart.MediaRatioValueFormatter;
import cn.iimedia.yq.custom.chart.PieMarkerView;
import cn.iimedia.yq.http.APIConstants;
import cn.iimedia.yq.http.RequestEngine;
import cn.iimedia.yq.http.bean.ResponseBean.MediaRatioBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by iiMedia on 2017/12/20.
 * 媒体占比
 */

public class FragmentMediaRatio extends BaseFragment {
    @BindView(R.id.media_ratio_chart)
    PieChart pieChart;

    ELS els;
    APIConstants apiConstants;
    List<PieEntry> entries = new ArrayList<>();
    MediaChangeProjectReceiver receiver;

    public static FragmentMediaRatio getInstance() {
        FragmentMediaRatio fragmentMediaRatio = new FragmentMediaRatio();
        return fragmentMediaRatio;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_media_ratio;
    }

    @Override
    protected void init() {
        els = ELS.getInstance(getBaseActivity());
        apiConstants = RequestEngine.createService(APIConstants.class);

        pieChart.setExtraOffsets(50.f, 12.f, 50.f, 8.f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setMarker(new PieMarkerView(getBaseActivity()));
        pieChart.setRotationEnabled(false);
        pieChart.animateXY(1200, 1200);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(getBaseActivity().getResources().getColor(R.color.black));
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setDrawEntryLabels(false);

        Legend l = pieChart.getLegend();
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setTextSize(12f);
        l.setFormSize(12f);
        l.setYOffset(6f);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        receiver = new MediaChangeProjectReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.CHOOSE_TIME_RANGE_ACTION);
        filter.addAction(Config.CHOOSE_PROJECT_ACTION);
        getBaseActivity().registerReceiver(receiver, filter);
    }

    @Override
    protected void lazyLoad() {
        loadData();
    }

    private void loadData() {
        showLoadingDialog();
        if (EmptyUtils.isNotEmpty(entries)) {
            entries.clear();
        }
        String timeRange = els.getStringData(ELS.TIME_RANGE);
        String date = MyUtils.getDateRange(timeRange);
        if (els.getIntData(ELS.CURRENT_TASK_ID) == 0) return;
        //网络请求
        Call<MediaRatioBean> call = apiConstants.getMediaRatio(els.getIntData(ELS.CURRENT_TASK_ID), date, timeRange);
        call.enqueue(new Callback<MediaRatioBean>() {
            @Override
            public void onResponse(Call<MediaRatioBean> call, Response<MediaRatioBean> response) {
                if (getBaseActivity() == null) return;
                if (EmptyUtils.isNotEmpty(response.body())) {
                    MediaRatioBean bean = response.body();
                    final List<MediaRatioBean.RecordsBean> list = bean.getRecords();
                    if (EmptyUtils.isNotEmpty(list)) {
                        setChartData(list);
                    } else {
                        showToast("当前数据为空");
                    }
                }
                dismissLoadingDialog();
            }

            @Override
            public void onFailure(Call<MediaRatioBean> call, Throwable t) {
                if (getBaseActivity() == null) return;
                dismissLoadingDialog();
                showToast("媒体占比数据获取失败");
            }
        });
    }

    //设置表格数据
    private void setChartData(List<MediaRatioBean.RecordsBean> list) {
        List<Integer> colors = new ArrayList<>();
        //设置各模块颜色
        //#a3495e，#ece8e7，#efccd5，#de8a96，#d4656e，#ba3944，#7c1c2a
        colors.add(Color.parseColor("#a3495e"));
        colors.add(Color.parseColor("#ece8e7"));
        colors.add(Color.parseColor("#efccd5"));
        colors.add(Color.parseColor("#de8a96"));
        colors.add(Color.parseColor("#d4656e"));
        colors.add(Color.parseColor("#ba3944"));
        colors.add(Color.parseColor("#7c1c2a"));
        for (MediaRatioBean.RecordsBean list2 : list) {
            entries.add(new PieEntry(list2.getValue(), list2.getName(), list2.getRate()));
        }
        if (EmptyUtils.isNotEmpty(entries)) {
            removeRepeat(entries);
            float sum = 0;
            for (PieEntry entry : entries) {
                sum += entry.getValue();
            }
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setColors(colors);

            //设置指示线颜色
            dataSet.setValueLineColor(getBaseActivity().getResources().getColor(R.color.colorPrimary));
            //设置内线长度,默认0.3f
            dataSet.setValueLinePart1Length(0.5f);
            //设置外线长度,默认0.4f
            dataSet.setValueLinePart2Length(0.8f);
            //设置数据在图外
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            PieData data = new PieData(dataSet);
            data.setValueFormatter(new MediaRatioValueFormatter(sum));
            data.setValueTextSize(14f);
            data.setValueTextColor(getBaseActivity().getResources().getColor(R.color.primary_text_color));
            pieChart.setData(data);
            pieChart.invalidate();
        }
    }

    //去重
    private void removeRepeat(List<PieEntry> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getLabel().equals(list.get(i).getLabel())) {
                    list.remove(list.get(j));
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getBaseActivity().unregisterReceiver(receiver);
    }

    private class MediaChangeProjectReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Config.CHOOSE_PROJECT_ACTION)) {
                loadData();
            } else if (intent.getAction().equals(Config.CHOOSE_TIME_RANGE_ACTION)) {
                loadData();
            }
        }
    }
}
