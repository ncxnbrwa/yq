package cn.iimedia.yq.custom.chart;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import cn.iimedia.yq.Base.utils.MyUtils;

/**
 * Created by iiMedia on 2018/1/2.
 * 媒体占比数值转换
 */

public class MediaRatioValueFormatter implements IValueFormatter {
    private float sum;

    public MediaRatioValueFormatter(float sum) {
        this.sum = sum;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        PieEntry pieEntry = (PieEntry) entry;
        String valueFormater = MyUtils.formatted2Percentage(value / sum);
        return pieEntry.getLabel() + ":(" + valueFormater + ")";
    }
}
