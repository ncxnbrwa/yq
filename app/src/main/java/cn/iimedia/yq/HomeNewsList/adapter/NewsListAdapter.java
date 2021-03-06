package cn.iimedia.yq.HomeNewsList.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iimedia.yq.R;
import cn.iimedia.yq.http.bean.ResponseBean.NewsListBean;

/**
 * Created by iiMedia on 2017/12/12.
 * 消息adapter,key用来设置关键字提醒
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {
    List<NewsListBean> list;
    LayoutInflater inflater;
    Context context;
    String key;

    public NewsListAdapter(List<NewsListBean> list, Context context, String key) {
        this.list = list;
        this.context = context;
        this.key = key;
        inflater = LayoutInflater.from(context);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_list_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsListBean newsListBean = list.get(position);
        int indexColor = context.getResources().getColor(R.color.colorAccent);
        String summary = newsListBean.getSummary();
        //根据关键字修改描述指定文字颜色
        if (EmptyUtils.isNotEmpty(summary)) {
            holder.newsDetail.setVisibility(View.VISIBLE);
            int index = summary.indexOf(key);
            if (index >= 0) {
                SpannableString ss = new SpannableString(summary);
                ss.setSpan(new ForegroundColorSpan(indexColor), index,
                        index + key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.newsDetail.setText(ss);
            } else {
                holder.newsDetail.setText(summary);
            }
        } else {
            holder.newsDetail.setVisibility(View.GONE);
        }
        //标题也把关键字颜色改了
        String title = newsListBean.getTitle();
        int titleIndex = title.indexOf(key);
        if (titleIndex >= 0) {
            SpannableString ssTitle = new SpannableString(title);
            ssTitle.setSpan(new ForegroundColorSpan(indexColor), titleIndex,
                    titleIndex + key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.newsTitle.setText(ssTitle);
        } else {
            holder.newsTitle.setText(title);
        }
        String time = newsListBean.getRelease_time().trim();
        holder.newsTime.setText(time);
        //根据情感换图
        int emoType = newsListBean.getStatusType();
        if (emoType == 1) {
            holder.newsEmoImg.setImageResource(R.drawable.emo_zhengmian);
        } else if (emoType == 0) {
            holder.newsEmoImg.setImageResource(R.drawable.emo_zhongli);
        } else if (emoType == -1) {
            holder.newsEmoImg.setImageResource(R.drawable.emo_fumian);
        }
        //设置媒体
        String mediaName = newsListBean.getMedia_name();
        if (EmptyUtils.isNotEmpty(mediaName)) {
            holder.newsMediaName.setText(mediaName);
        } else {
            holder.newsMediaName.setText("未知来源");
        }
    }

    @Override
    public int getItemCount() {
        return list.isEmpty() ? 0 : list.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_emo_face)
        ImageView newsEmoImg;
        @BindView(R.id.tv_news_time)
        TextView newsTime;
        @BindView(R.id.tv_news_detail)
        TextView newsDetail;
        @BindView(R.id.tv_news_title)
        TextView newsTitle;
        @BindView(R.id.tv_news_media)
        TextView newsMediaName;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
