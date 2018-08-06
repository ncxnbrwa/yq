package cn.iimedia.yq.http.bean.ResponseBean;

import java.util.List;

/**
 * Created by iiMedia on 2017/12/22.
 */

public class EmoTrendBean {

    /**
     * category : ["2017-12-16","2017-12-17","2017-12-18","2017-12-19","2017-12-20","2017-12-21","2017-12-22"]
     * code : 1
     * legend : []
     * series : [{"barGap":null,"barWidth":null,"data":[324,301,371,303,277,424,144],"name":"正面","stack":null,"type":"bar"},{"barGap":null,"barWidth":null,"data":[52,27,54,57,68,48,45],"name":"负面","stack":null,"type":"bar"},{"barGap":null,"barWidth":null,"data":[345,418,433,276,414,449,497],"name":"中立","stack":null,"type":"bar"}]
     */

    private int code;
    private List<String> category;
    private List<String> legend;
    private List<SeriesBean> series;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List<String> getLegend() {
        return legend;
    }

    public void setLegend(List<String> legend) {
        this.legend = legend;
    }

    public List<SeriesBean> getSeries() {
        return series;
    }

    public void setSeries(List<SeriesBean> series) {
        this.series = series;
    }

    public static class SeriesBean {
        /**
         * barGap : null
         * barWidth : null
         * data : [324,301,371,303,277,424,144]
         * name : 正面
         * stack : null
         * type : bar
         */

        private Object barGap;
        private Object barWidth;
        private String name;
        private Object stack;
        private String type;
        private List<Integer> data;

        public Object getBarGap() {
            return barGap;
        }

        public void setBarGap(Object barGap) {
            this.barGap = barGap;
        }

        public Object getBarWidth() {
            return barWidth;
        }

        public void setBarWidth(Object barWidth) {
            this.barWidth = barWidth;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getStack() {
            return stack;
        }

        public void setStack(Object stack) {
            this.stack = stack;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Integer> getData() {
            return data;
        }

        public void setData(List<Integer> data) {
            this.data = data;
        }
    }
}
