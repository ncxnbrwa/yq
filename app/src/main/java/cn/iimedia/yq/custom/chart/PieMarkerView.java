package cn.iimedia.yq.custom.chart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import cn.iimedia.yq.R;

/**
 * Created by iiMedia on 2017/12/22.
 * 标记视图
 */

public class PieMarkerView extends MarkerView {
    private TextView tvContent;

    public PieMarkerView(Context context) {
        super(context, R.layout.pie_marker_view);
        tvContent = (TextView) findViewById(R.id.pie_marker_view);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        PieEntry pieEntry = (PieEntry) e;
        String value = String.valueOf(pieEntry.getY());
        tvContent.setText(pieEntry.getLabel() + ":" + value.substring(0, value.indexOf("."))
                + "（" + pieEntry.getData() + "）");
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
