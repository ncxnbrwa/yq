package cn.iimedia.yq.HomeNewsList.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iimedia.yq.R;
import cn.iimedia.yq.http.bean.DatabaseBean.ProjectDatabase;

/**
 * Created by iiMedia on 2017/12/13.
 * 所有项目adapter
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    List<ProjectDatabase> databaseList;
    Context mContext;
    LayoutInflater inflater;

    public ProjectAdapter(List<ProjectDatabase> databaseList, Context mContext) {
        this.databaseList = databaseList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.project_list_item, parent, false);
        return new ProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        ProjectDatabase projectDatabase = databaseList.get(position);
        holder.projectName.setText(projectDatabase.getProjectname());
    }

    @Override
    public int getItemCount() {
        return databaseList.size();
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_project_name)
        TextView projectName;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
