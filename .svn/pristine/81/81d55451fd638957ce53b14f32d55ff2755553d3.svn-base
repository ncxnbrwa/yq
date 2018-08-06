package cn.iimedia.yq.http;

import cn.iimedia.yq.http.bean.ResponseBean.CommonResponse;
import cn.iimedia.yq.http.bean.ResponseBean.EmoRatioBean;
import cn.iimedia.yq.http.bean.ResponseBean.EmoTrendBean;
import cn.iimedia.yq.http.bean.ResponseBean.HotHeatBean;
import cn.iimedia.yq.http.bean.ResponseBean.MediaRatioBean;
import cn.iimedia.yq.http.bean.ResponseBean.NewsBean;
import cn.iimedia.yq.http.bean.ResponseBean.ProjectBean;
import cn.iimedia.yq.http.bean.ResponseBean.RelatedWordBean;
import cn.iimedia.yq.http.bean.ResponseBean.User;
import cn.iimedia.yq.http.bean.ResponseBean.UserInfoBean;
import cn.iimedia.yq.http.bean.ResponseBean.UserWarningBean;
import cn.iimedia.yq.http.bean.ResponseBean.WarningNewsBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by iiMedia on 2017/11/9.
 * API接口
 */

public interface APIConstants {

    @POST(RequestEngine.LOGIN)
    Call<User> login(@Query("j_username") String username,
                     @Query("j_password") String password);

    //按时间逆序 order：”desc”,按时间顺序不传,t尽量传时间戳
    @POST(RequestEngine.GET_CURRENT_PROJECT)
    Call<ProjectBean> getCurrentProject(@Query("t") long t,
                                        @Query("order") String order);

    //获取消息
    @POST(RequestEngine.GET_NEWS)
    Call<NewsBean> getNews(@Query("param") String param);

    //添加项目
    @POST(RequestEngine.ADD_PROJECT)
    Call<CommonResponse> addProject(@Query("model") int model,//项目模型,传3即可
                                    @Query("keyword") String keyword, //关键词
                                    @Query("name") String name, //项目名
                                    @Query("related_word") String related_word //关联词,项目模型为3时不需要传
    );

    //根据关键字查询关联词
    @POST(RequestEngine.QUERY_RELATED_WORD)
    Call<RelatedWordBean> queryRelatedWord(@Query("keyword") String keyword);

    //拉取项目历史预警
//    @POST(RequestEngine.GET_HISTOTY_WARNING)
//    Call<UserWarningBean> getHistoryWarining(@Query("proj_id") int projectId, //项目id
//                                             @Query("page_size") int pageSize, //每页大小
//                                             @Query("page_num") int num //页码
//    );

    //拉取用户预警
    @POST(RequestEngine.GET_USER_WARNING)
    Call<UserWarningBean> getUserWarining(@Query("page_size") int pageSize, //每页大小
                                          @Query("page_num") int num //页码
    );

    //预警详情新闻
    @POST(RequestEngine.GET_WARNING_DETAIL_NEWS)
    Call<WarningNewsBean> getWarningDetail(@Query("proj_id") int projectId, //项目id
                                           @Query("dateline") String dateline, //时间,格式yyMMddHH
                                           @Query("page_size") int pageSize, //每页大小
                                           @Query("page_num") int num //页码
    );

    //获取用户信息
    @POST(RequestEngine.GET_USER_INFO)
    Call<UserInfoBean> getUserInfo();

    //24小时:24-hour;
    //昨天:lastday;
    //一周:week;
    //一个月:month
    //媒体占比
    @POST(RequestEngine.MEDIA_DATA_RATIO)
    Call<MediaRatioBean> getMediaRatio(@Query("task_id") int taskId, //项目ID
                                       @Query("dateline") String date, //形如170913;170913-170923;
                                       @Query("category") String timeRange //时间范围
    );

    //热度指数
    @POST(RequestEngine.HOT_HEAT)
    Call<HotHeatBean> getHotHeat(@Query("dateline") String date, //同上
                                 @Query("category") String timeRange, //同上
                                 @Query("task_id") int taskId, //同上
                                 @Query("task_name") String taskName //项目名,项目对比时需要用到
    );

    //情感比例
    @POST(RequestEngine.EMO_RATIO)
    Call<EmoRatioBean> getEmoRatio(@Query("dateline") String date, //171214-177221(写死一周就好)
                                   @Query("category") String timeRange, //week(写死一周就好)
                                   @Query("task_id") int taskId
    );

    //情感走势
    @POST(RequestEngine.EMO_TREND)
    Call<EmoTrendBean> getEmoTrend(@Query("dateline") String date,
                                   @Query("category") String timeRange,
                                   @Query("task_id") int taskId

    );
	
	//退出登录
	@GET(RequestEngine.LOGOUT)
	Call<Void> logout();
}
