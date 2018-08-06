package cn.iimedia.yq.http.bean.ResponseBean;

import java.util.List;

/**
 * Created by iiMedia on 2017/12/11.
 * 主页新闻列表实体类
 */

public class NewsBean {

    /**
     * code : 0
     * data : {"list":[{"channel_name":"南宁","city":"南宁","comment_num":0,"forward_num":0,"hash_code":"13768922939430368595","hot_index":0,"image":"http://inews.gtimg.com/newsapp_ls/0/2040818768_196130/0","labels":"交管局,酒驾,公益活动,","like_num":0,"media_name":"南宁交警","news_id":"35735902","platform_id":5,"platform_name":"天天快报","province":"","read_num":0,"release_time":"2017-09-13 19:53:25","similar_num":0,"src_url":"","status":"中立","summary":"","title":""}],"num":6,"page":{"pagesize":6,"pagestart":1505231999,"total":9}}
     * msg :
     */

    private int code;
    private DataBean data;
    private String msg;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * list : [{"channel_name":"南宁","city":"南宁","comment_num":0,"forward_num":0,"hash_code":"13768922939430368595","hot_index":0,"image":"http://inews.gtimg.com/newsapp_ls/0/2040818768_196130/0","labels":"交管局,酒驾,公益活动,","like_num":0,"media_name":"南宁交警","news_id":"35735902","platform_id":5,"platform_name":"天天快报","province":"","read_num":0,"release_time":"2017-09-13 19:53:25","similar_num":0,"src_url":"","status":"中立","summary":"","title":""}]
         * num : 6
         * page : {"pagesize":6,"pagestart":1505231999,"total":9}
         */

        private int num;
        private PageBean page;
        private List<NewsListBean> list;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public List<NewsListBean> getList() {
            return list;
        }

        public void setList(List<NewsListBean> list) {
            this.list = list;
        }

        public static class PageBean {
            /**
             * pagesize : 6
             * pagestart : 1505231999
             * total : 9
             */

            private int pagesize;
            private int pagestart;
            private int total;

            public int getPagesize() {
                return pagesize;
            }

            public void setPagesize(int pagesize) {
                this.pagesize = pagesize;
            }

            public int getPagestart() {
                return pagestart;
            }

            public void setPagestart(int pagestart) {
                this.pagestart = pagestart;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }
        }

    }
}
