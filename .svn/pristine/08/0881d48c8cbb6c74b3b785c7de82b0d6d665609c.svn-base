package cn.iimedia.yq.http.bean.ResponseBean;

import java.util.List;

/**
 * Created by iiMedia on 2017/12/22.
 */

public class MediaRatioBean {

    /**
     * code : 1
     * label : ["网页","微信","微博","APP","论坛","视频"]
     * msg : 返回成功
     * records : [{"name":"网页","rate":"43.38%","total":334,"type":1,"value":334},{"name":"微信","rate":"28.44%","total":219,"type":2,"value":219},{"name":"微博","rate":"2.47%","total":19,"type":3,"value":19},{"name":"APP","rate":"21.56%","total":166,"type":4,"value":166},{"name":"论坛","rate":"2.86%","total":22,"type":5,"value":22},{"name":"视频","rate":"1.3%","total":10,"type":7,"value":10}]
     * total : 6
     */

    private int code;
    private String msg;
    private int total;
    private List<String> label;
    private List<RecordsBean> records;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean {
        /**
         * name : 网页
         * rate : 43.38%
         * total : 334
         * type : 1
         * value : 334
         */

        private String name;
        private String rate;
        private int total;
        private int type;
        private int value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "RecordsBean{" +
                    "name='" + name + '\'' +
                    ", rate='" + rate + '\'' +
                    ", total=" + total +
                    ", type=" + type +
                    ", value=" + value +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MediaRatioBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", total=" + total +
                ", label=" + label +
                ", records=" + records +
                '}';
    }
}
