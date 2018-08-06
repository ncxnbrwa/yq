package cn.iimedia.yq.custom.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import cn.iimedia.yq.Base.Config;
import cn.iimedia.yq.Base.YqApplication;
import cn.iimedia.yq.Base.utils.ELS;
import cn.iimedia.yq.Base.utils.TimeTypeUtils;

/**
 * Created by iiMedia on 2017/12/25.
 * 横坐标值转换
 */

public class EmoTrendXFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String valueStr = String.valueOf(value);
        String str = valueStr.substring(valueStr.indexOf(".") + 1, valueStr.indexOf(".") + 2);
        if (!str.equals("0")) {
            return "";
        }
        int valueInt = (int) value;
        ELS els = ELS.getInstance(YqApplication.getAppContext());
        String timeRange = els.getStringData(ELS.TIME_RANGE);
        switch (timeRange) {
            case Config.TIME_24H:
                return TimeTypeUtils.timeRange2Date(6, valueInt);
            case Config.TIME_YESTERDAY:
                return TimeTypeUtils.timeRange2Date(6, valueInt);
            case Config.TIME_WEEK:
                return TimeTypeUtils.timeRange2Date(7, valueInt);
            case Config.TIME_MONTH:
                return TimeTypeUtils.timeRange2Date(30, valueInt);
            default:
                return String.valueOf(valueInt);
        }

    }
}
