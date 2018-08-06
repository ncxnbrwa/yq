package cn.iimedia.yq.http.bean.ResponseBean;

/**
 * Created by iiMedia on 2017/12/14.
 */

public class UserWarningRecordsList {


    /**
     * content : 【艾媒舆情监测】您的监测项目"仓央嘉措"于2017-12-04 05:00:00触发黄色报警,热度值为137.
     * dateline : 1512334800
     * key_words : 仓央嘉措
     * proj_id : 4559
     * proj_name : 仓央嘉措
     * warning_level : 1
     * warning_type : 2
     */

    private String content;
    private long dateline;
    private String key_words;
    private int proj_id;
    private String proj_name;
    private int warning_level; //1-黄色 2-橙色 3-红色
    private int warning_type; //1-热度预警 2-文章预警 3-评论预警

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }

    public String getKey_words() {
        return key_words;
    }

    public void setKey_words(String key_words) {
        this.key_words = key_words;
    }

    public int getProj_id() {
        return proj_id;
    }

    public void setProj_id(int proj_id) {
        this.proj_id = proj_id;
    }

    public String getProj_name() {
        return proj_name;
    }

    public void setProj_name(String proj_name) {
        this.proj_name = proj_name;
    }

    public int getWarning_level() {
        return warning_level;
    }

    public void setWarning_level(int warning_level) {
        this.warning_level = warning_level;
    }

    public int getWarning_type() {
        return warning_type;
    }

    public void setWarning_type(int warning_type) {
        this.warning_type = warning_type;
    }

    @Override
    public String toString() {
        return "UserWarningRecordsList{" +
                "content='" + content + '\'' +
                ", dateline=" + dateline +
                ", key_words='" + key_words + '\'' +
                ", proj_id=" + proj_id +
                ", proj_name='" + proj_name + '\'' +
                ", warning_level=" + warning_level +
                ", warning_type=" + warning_type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserWarningRecordsList that = (UserWarningRecordsList) o;

        if (dateline != that.dateline) return false;
        if (proj_id != that.proj_id) return false;
        if (warning_level != that.warning_level) return false;
        if (warning_type != that.warning_type) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (key_words != null ? !key_words.equals(that.key_words) : that.key_words != null)
            return false;
        return proj_name != null ? proj_name.equals(that.proj_name) : that.proj_name == null;
    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + (int) (dateline ^ (dateline >>> 32));
        result = 31 * result + (key_words != null ? key_words.hashCode() : 0);
        result = 31 * result + proj_id;
        result = 31 * result + (proj_name != null ? proj_name.hashCode() : 0);
        result = 31 * result + warning_level;
        result = 31 * result + warning_type;
        return result;
    }
}
