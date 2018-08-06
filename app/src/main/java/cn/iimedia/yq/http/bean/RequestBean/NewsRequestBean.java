package cn.iimedia.yq.http.bean.RequestBean;

/**
 * Created by iiMedia on 2017/12/13.
 * 新闻请求参数
 */

public class NewsRequestBean {
    private long pagestart; //开始时间,传当前时间戳
    private int pagesize; //每页显示新闻条数,默认6
    private int task_id; //项目ID
    private long start; //分页开始时间,开始时间戳
    private long end; //分页结束时间,结束时间戳
    private int type; //媒体类型,网页-1微信-2 微博-3 APP-4 论坛-5 报刊-6 视频-7
    private int emo_type; //情绪,正面-1中立-0 负面-2
    private String sign; //标识号,用来标识有效用户
    private int search_type; //搜索类型,标题-0 全文-1 来源-2 作者-3
    private String skey; //搜索词

    public long getPagestart() {
        return pagestart;
    }

    public void setPagestart(long pagestart) {
        this.pagestart = pagestart;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getEmo_type() {
        return emo_type;
    }

    public void setEmo_type(int emo_type) {
        this.emo_type = emo_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getSearch_type() {
        return search_type;
    }

    public void setSearch_type(int search_type) {
        this.search_type = search_type;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }
}
