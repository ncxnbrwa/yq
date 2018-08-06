package cn.iimedia.yq.http.bean.ResponseBean;

import java.util.List;

/**
 * Created by iiMedia on 2017/12/21.
 * 情感比例实体类
 */

public class EmoRatioBean {

    /**
     * code : 0
     * records : [{"color":"#58DB8F","name":"正面","rate":"28.18%","value":23345},{"color":"#FB617F","name":"负面","rate":"10.21%","value":8457},{"color":"#4FBCEF","name":"中性","rate":"61.62%","value":51050}]
     * total : 3
     */

    private int code;
    private int total;
    private int login_error;
    private List<RecordsBean> records;

    public int getLogin_error() {
        return login_error;
    }

    public void setLogin_error(int login_error) {
        this.login_error = login_error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean {
        /**
         * color : #58DB8F
         * name : 正面
         * rate : 28.18%
         * value : 23345
         */

        private String color;
        private String name;
        private String rate;
        private int value;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

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

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "RecordsBean{" +
                    "color='" + color + '\'' +
                    ", name='" + name + '\'' +
                    ", rate='" + rate + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "EmoRatioBean{" +
                "code=" + code +
                ", total=" + total +
                ", records=" + records +
                '}';
    }
}
