package cn.iimedia.yq.analyze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.EmptyUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.iimedia.yq.Base.BaseFragment;
import cn.iimedia.yq.Base.Config;
import cn.iimedia.yq.Base.utils.ELS;
import cn.iimedia.yq.Base.utils.MyUtils;
import cn.iimedia.yq.R;
import cn.iimedia.yq.custom.chart.EmoTrendMarkerView;
import cn.iimedia.yq.custom.chart.EmoTrendXFormatter;
import cn.iimedia.yq.http.APIConstants;
import cn.iimedia.yq.http.RequestEngine;
import cn.iimedia.yq.http.bean.ResponseBean.EmoTrendBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by iiMedia on 2017/12/21.
 * 情感走势
 */

public class FragmentEmotionTrend extends BaseFragment {
    @BindView(R.id.emo_trend_chart)
    BarChart mChart;

    APIConstants apiConstants;
    ELS els;
    EmoTrendReceiver receiver;
    List<BarEntry> entries = new ArrayList<>();
    List<String> xValues = new ArrayList<>();

    public static FragmentEmotionTrend getInstance() {
        FragmentEmotionTrend fragment = new FragmentEmotionTrend();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragemnt_emo_trend;
    }

    @Override
    protected void init() {
        els = ELS.getInstance(getBaseActivity());
        apiConstants = RequestEngine.createService(APIConstants.class);
        mChart.getDescription().setEnabled(false);

        EmoTrendMarkerView markerView = new EmoTrendMarkerView(getBaseActivity());
        markerView.setChartView(mChart);
        mChart.setMarker(markerView);

        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setMaxVisibleValueCount(40);
        mChart.setPinchZoom(true);
        mChart.animateXY(1200, 1200);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(true);
        mChart.setExtraOffsets(10.f, 20.f, 20.f, 8.f);

        mChart.getAxisRight().setEnabled(false);

        //设置横坐标
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new EmoTrendXFormatter());
        //设置图例
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(12f);
        l.setTextSize(12f);
        l.setYOffset(6f);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        receiver = new EmoTrendReceiver();
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
        String timeRange = els.getStringData(ELS.TIME_RANGE);
        String date = MyUtils.getDateRange(timeRange);
        if (els.getIntData(ELS.CURRENT_TASK_ID) == 0) return;
        //网络请求
        Call<EmoTrendBean> call = apiConstants.getEmoTrend(date, timeRange, els.getIntData(ELS.CURRENT_TASK_ID));
        call.enqueue(new Callback<EmoTrendBean>() {
            @Override
            public void onResponse(Call<EmoTrendBean> call, Response<EmoTrendBean> response) {
                if (getBaseActivity() == null) return;
                if (EmptyUtils.isNotEmpty(response.body())) {
                    EmoTrendBean bean = response.body();
                    setChartData(bean);
                } else {
                    showToast("当前数据为空");
                }
                dismissLoadingDialog();
            }

            @Override
            public void onFailure(Call<EmoTrendBean> call, Throwable t) {
                if (getBaseActivity() == null) return;
                dismissLoadingDialog();
                showToast("情感走势数据获取失败");
            }
        });
    }

    private void setChartData(final EmoTrendBean bean) {
        if (EmptyUtils.isNotEmpty(entries)) {
            entries.clear();
        }
        if (EmptyUtils.isNotEmpty(xValues)) {
            xValues.clear();
        }
        if (EmptyUtils.isNotEmpty(bean.getCategory())) {
            xValues.addAll(bean.getCategory());
            removeXValuesRepeat(xValues);
            List<EmoTrendBean.SeriesBean> series = bean.getSeries();
            List<Integer> positiveValues = series.get(0).getData();
            List<Integer> negativeValues = series.get(1).getData();
            List<Integer> neutralValues = series.get(2).getData();
            //中立：#2E4454，正面：#5FA1AB，负面：#C33430
            List<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#C33430"));
            colors.add(Color.parseColor("#5FA1AB"));
            colors.add(Color.parseColor("#2E4454"));

            for (int i = 0; i < xValues.size(); i++) {
                entries.add(new BarEntry(i, new float[]{(float) negativeValues.get(i) / -1,
                        (float) positiveValues.get(i),
                        (float) neutralValues.get(i)},
                        bean.getCategory().get(i)));
            }
            removeRepeat(entries);

            BarDataSet dataSet = new BarDataSet(entries, "");
            dataSet.setDrawValues(false);
            dataSet.setDrawIcons(false);
            dataSet.setColors(colors);
            dataSet.setStackLabels(new String[]{"负面", "正面", "中立"});

            List<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);

            BarData data = new BarData(dataSets);
            data.setValueTextColor(Color.BLACK);
            data.setHighlightEnabled(true);

            mChart.setData(data);

            mChart.setFitBars(true);
            mChart.invalidate();
        }
    }

    private void removeXValuesRepeat(List<String> stringList) {
        for (int i = 0; i < stringList.size() - 1; i++) {
            for (int j = stringList.size() - 1; j > i; j--) {
                if (stringList.get(j).equals(stringList.get(i))) {
                    stringList.remove(stringList.get(j));
                }
            }
        }
    }

    //去重
    private void removeRepeat(List<BarEntry> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getX() == (list.get(i).getX())) {
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

    private class EmoTrendReceiver extends BroadcastReceiver {
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
