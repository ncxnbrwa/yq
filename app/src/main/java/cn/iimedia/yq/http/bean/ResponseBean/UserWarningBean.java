package cn.iimedia.yq.http.bean.ResponseBean;

import java.util.List;

/**
 * Created by iiMedia on 2017/12/14.
 * 拉取项目历史预警返回类
 */

public class UserWarningBean {
    /**
     * code : 1
     * records : [{"proj_name":"\u201d\u201d","key_words":"\u201d\u201d","warning_type":1,"warning_level":1,"conent":"somethingjustlikethis"}]
     * page_total : 1
     */

    private int code;
    private int page_total;
    private List<UserWarningRecordsList> records;
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

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public List<UserWarningRecordsList> getRecords() {
        return records;
    }

    public void setRecords(List<UserWarningRecordsList> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "HistoryWarningBean{" +
                "code=" + code +
                ", page_total=" + page_total +
                ", records=" + records +
                '}';
    }
}
