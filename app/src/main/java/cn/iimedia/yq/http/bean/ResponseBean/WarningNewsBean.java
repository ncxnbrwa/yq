package cn.iimedia.yq.http.bean.ResponseBean;

import java.util.List;

/**
 * Created by iiMedia on 2017/12/15.
 */

public class WarningNewsBean {

    /**
     * code : 0
     * new_total : 1
     * new_records : []
     * page_total : 10
     */

    private int code;
    private int new_total;
    private int page_total;
    private List<WarningNewsRecord> new_record;
    private int login_error;

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

    public int getNew_total() {
        return new_total;
    }

    public void setNew_total(int new_total) {
        this.new_total = new_total;
    }

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public List<WarningNewsRecord> getNew_records() {
        return new_record;
    }

    public void setNew_records(List<WarningNewsRecord> new_records) {
        this.new_record = new_records;
    }

    @Override
    public String toString() {
        return "WarningNewsBean{" +
                "code=" + code +
                ", new_total=" + new_total +
                ", page_total=" + page_total +
                ", new_records=" + new_record +
                ", login_error=" + login_error +
                '}';
    }
}
