package com.smartdot.wenbo.controlcenter.adapter;

import java.util.List;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.bean.ServiceBean;
import com.smartdot.wenbo.controlcenter.bean.ServiceStatueBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ServiceStatueListviewAdapter extends BaseAdapter{
   private Context context;
   private List<ServiceBean> data;
   
   
	
	public ServiceStatueListviewAdapter(Context context,
		List<ServiceBean> data) {
	super();
	this.context = context;
	this.data = data;
}

	@Override
	public int getCount() {
       if (data !=null) {
		return data.size();
	}else {
		
		return 0;
	}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.service_statue_list_item,null);
			holder.tv_task_name = (TextView) convertView.findViewById(R.id.sslist_item_tv_task_name);
			holder.tv_task_content = (TextView) convertView.findViewById(R.id.sslist_item_tv_task_content);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		String taskname = data.get(position).getTaskname();
		if (taskname.equalsIgnoreCase("jieji")) {
			holder.tv_task_name.setText("到达");
		}else if (taskname.equalsIgnoreCase("qiandao")) {
			holder.tv_task_name.setText("签到");
		}else if (taskname.equalsIgnoreCase("banruzhu")) {
			holder.tv_task_name.setText("入住");
		}else if (taskname.equalsIgnoreCase("lingziliao")) {
			holder.tv_task_name.setText("领资料");
		}else if (taskname.equalsIgnoreCase("kanzhanlan")) {
			holder.tv_task_name.setText("活动");
		}else if (taskname.equalsIgnoreCase("songji")) {
			holder.tv_task_name.setText("离开");
		}else if (taskname.equalsIgnoreCase("yuzhuce")) {
			holder.tv_task_name.setText("注册");
		}
			
	    holder.tv_task_content.setText("已完成"+data.get(position).getFinish()+"人，未完成"+data.get(position).getUnfinish()+"人");
		return convertView;
	}
  class ViewHolder{
	  TextView tv_task_name,tv_task_content;
	  
  }
}
