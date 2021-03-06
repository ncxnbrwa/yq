package cn.iimedia.yq.analyze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.EmptyUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.iimedia.yq.Base.BaseFragment;
import cn.iimedia.yq.Base.Config;
import cn.iimedia.yq.Base.utils.ELS;
import cn.iimedia.yq.Base.utils.MyUtils;
import cn.iimedia.yq.R;
import cn.iimedia.yq.custom.chart.HeatLineMarkerView;
import cn.iimedia.yq.custom.chart.HeatValueFormatter;
import cn.iimedia.yq.custom.chart.HeatXFormatter;
import cn.iimedia.yq.http.APIConstants;
import cn.iimedia.yq.http.RequestEngine;
import cn.iimedia.yq.http.bean.ResponseBean.HotHeatBean;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PointValue;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by iiMedia on 2017/12/20.
 * 热度指数
 */

public class FragmentHeat extends BaseFragment {
    @BindView(R.id.heat_chart)
    LineChart mChart;

    ELS els;
    APIConstants apiConstants;
    HeatChangeProjectReceiver receiver;
    List<PointValue> pointValues = new ArrayList<>();
//    QMUITipDialog loadingDialog;

    public static FragmentHeat getInstance() {
        FragmentHeat fragmentHeat = new FragmentHeat();
        return fragmentHeat;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_heat;
    }

    @Override
    protected void init() {
        els = ELS.getInstance(getBaseActivity());
        apiConstants = RequestEngine.createService(APIConstants.class);

        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(true);
        mChart.setDoubleTapToZoomEnabled(false);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setExtraOffsets(15.f, 20.f, 30.f, 8.f);
        //设置标记
        HeatLineMarkerView mv = new HeatLineMarkerView(getBaseActivity());
        mv.setChartView(mChart);
        mChart.setMarker(mv);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new HeatXFormatter());

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawZeroLine(false);

        mChart.getAxisRight().setEnabled(false);
        mChart.animateXY(1200, 1200);

        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setEnabled(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        receiver = new HeatChangeProjectReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.CHOOSE_PROJECT_ACTION);
        filter.addAction(Config.CHOOSE_TIME_RANGE_ACTION);
        getBaseActivity().registerReceiver(receiver, filter);
    }

    @Override
    protected void lazyLoad() {
        loadData();
    }

    private void loadData() {
        showLoadingDialog();
        String timeRange = els.getStringData(ELS.TIME_RANGE);
        final String date = MyUtils.getDateRange(timeRange);
        if (els.getIntData(ELS.CURRENT_TASK_ID) == 0) return;
        Call<HotHeatBean> call = apiConstants.getHotHeat(date, timeRange,
                els.getIntData(ELS.CURRENT_TASK_ID), els.getStringData(ELS.CURRENT_TASK_NAME));
        call.enqueue(new Callback<HotHeatBean>() {
            @Override
            public void onResponse(Call<HotHeatBean> call, Response<HotHeatBean> response) {
                if (getBaseActivity() == null) return;
                try {
                    if (EmptyUtils.isNotEmpty(response.body())) {
                        HotHeatBean bean = response.body();
                        List<String> xStrings = bean.getCategory();
                        List<Integer> yValues = bean.getSeries().get(0).getData();
                        if (EmptyUtils.isNotEmpty(xStrings) && EmptyUtils.isNotEmpty(yValues)) {
                            setChartData(xStrings, yValues);
                        }else{
                            showToast("当前数据为空");
                        }
                    }
                    dismissLoadingDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<HotHeatBean> call, Throwable t) {
                if (getBaseActivity() == null) return;
                dismissLoadingDialog();
                showToast("热度指数数据失败");
            }
        });
    }

    private void setChartData(List<String> xStrings, List<Integer> yValues) {
        List<Entry> values = new ArrayList<>();

        for (int i = 0; i < xStrings.size(); i++) {
            //把X轴名称放入额外数据
            values.add(new Entry(i, yValues.get(i), xStrings.get(i)));
        }
        removeRepeat(pointValues);

        LineDataSet dataSet = new LineDataSet(values, "");
        dataSet.setDrawIcons(false);

        //设置线的属性和包围区域填充色
        dataSet.setColor(getResources().getColor(R.color.colorAccent));
        dataSet.setCircleColor(getResources().getColor(R.color.colorAccent));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(12f);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(getResources().getColor(R.color.heat_line_wrap_color));

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData data = new LineData(dataSets);
        data.setValueFormatter(new HeatValueFormatter());
        mChart.setData(data);
        mChart.invalidate();
    }

    //去重
    private void removeRepeat(List<PointValue> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getX() == (list.get(i).getX())) {
                    list.remove(list.get(j));
                }
            }
        }
    }

    private void removeRepeat(List<PointValue> list, List<AxisValue> axisValues) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getX() == (list.get(i).getX())) {
                    list.remove(list.get(j));
                }
                if (axisValues.get(j).getValue() == axisValues.get(i).getValue()) {
                    axisValues.remove(axisValues.get(j));
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getBaseActivity().unregisterReceiver(receiver);
    }


    private class HeatChangeProjectReceiver extends BroadcastReceiver {
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
