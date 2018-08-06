package cn.iimedia.yq.custom.chart;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by iiMedia on 2018/1/2.
 * 热度指数数值格式化
 */

public class HeatValueFormatter implements IValueFormatter {
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        int xInt = (int) entry.getX();
        //坐标是奇数则不显示数值
        if (xInt % 2 != 0) {
            return "";
        } else {
            return String.valueOf((int) value);
        }
    }
}
