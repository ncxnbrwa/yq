package cn.iimedia.yq.http.bean.ResponseBean;

import java.util.List;

/**
 * Created by iiMedia on 2017/12/22.
 * 热度指数实体类
 */

public class HotHeatBean {

    /**
     * category : ["21:00","22:00","23:00","00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00"]
     * code : 1
     * legend : ["库克"]
     * series : [{"data":[403,410,401,386,381,377,378,379,382,417,415,415,417,430,414,414,428,400,390,403,408,410,430,0],"name":"库克","type":"line"}]
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
         * data : [403,410,401,386,381,377,378,379,382,417,415,415,417,430,414,414,428,400,390,403,408,410,430,0]
         * name : 库克
         * type : line
         */

        private String name;
        private String type;
        private List<Integer> data;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
