package cn.iimedia.yq.http.bean.ResponseBean;

/**
 * Created by iiMedia on 2017/12/21.
 */

public class UserInfoBean {

    /**
     * ccoin : 480
     * code : 1
     * create_time : -
     * last_day : 0
     * nickName : 15913110374
     * type : 9
     * username : 15913110374
     */

    private int ccoin;
    private int code;
    private String create_time;
    private int last_day;
    private String nickName;
    private int type;
    private String username;
    private int login_error;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLogin_error() {
        return login_error;
    }

    public void setLogin_error(int login_error) {
        this.login_error = login_error;
    }

    public int getCcoin() {
        return ccoin;
    }

    public void setCcoin(int ccoin) {
        this.ccoin = ccoin;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getLast_day() {
        return last_day;
    }

    public void setLast_day(int last_day) {
        this.last_day = last_day;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "ccoin=" + ccoin +
                ", code=" + code +
                ", create_time='" + create_time + '\'' +
                ", last_day=" + last_day +
                ", nickName='" + nickName + '\'' +
                ", type=" + type +
                ", username='" + username + '\'' +
                ", login_error=" + login_error +
                ", email='" + email + '\'' +
                '}';
    }
}
