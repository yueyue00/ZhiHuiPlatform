package com.smartdot.wenbo.controlcenter.adapter;

import java.util.List;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.R.color;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.bean.ScheduleAllData.InfoBean.MeettingBean;
import com.smartdot.wenbo.controlcenter.bean.ScheduleBean;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScheduleAdapter extends BaseAdapter{
	
	private Context context;
	private List<MeettingBean> datas;
	
	

	public ScheduleAdapter(Context context, List<MeettingBean> datas) {
		super();
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		if (datas !=null) {
			return datas.size();
		}else {
			
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.schdule_list_item, null);
			holder.ll_container = (LinearLayout) convertView.findViewById(R.id.sch_ll);
			holder.sch_content_ll = (LinearLayout) convertView.findViewById(R.id.sch_content_ll);
			holder.sch_item_view1 = (View) convertView.findViewById(R.id.sch_item_view1);
			holder.sch_item_view2 = (View) convertView.findViewById(R.id.sch_item_view2);
			holder.tv_startTime = (TextView) convertView.findViewById(R.id.sch_end_tv_starTime);
			holder.tv_endTime = (TextView) convertView.findViewById(R.id.sch_end_tv_endTime);
			holder.tv_fuzeren = (TextView) convertView.findViewById(R.id.sch_item_tv_cbfPerson);
			holder.tv_hall = (TextView) convertView.findViewById(R.id.sch_item_tv_location);
			holder.tv_hostParty = (TextView) convertView.findViewById(R.id.sch_item_tv_cbf);
			holder.tv_sponsor =  (TextView) convertView.findViewById(R.id.sch_item_tv_zbf);
			holder.tv_title = (TextView) convertView.findViewById(R.id.sch_item_tv_title);
			holder.sch_end_dotL = (ImageView) convertView.findViewById(R.id.sch_end_dotL);
			holder.sch_end_dotR = (ImageView) convertView.findViewById(R.id.sch_end_dotR);
//			holder.iv_icon = (ImageView) convertView.findViewById(R.id.schedule_iv_head);
			
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_endTime.setText("结束时间："+Constant.getCurrentTimeToHm(datas.get(position).getEndTiime()));
		holder.tv_startTime.setText("开始时间："+Constant.getCurrentTimeToHm(datas.get(position).getStartTime()));
		holder.tv_fuzeren.setText("承办方负责人："+datas.get(position).getUndertakingContact());
		holder.tv_hall.setText("日程地点："+datas.get(position).getAddress());
		holder.tv_hostParty.setText("承办方："+datas.get(position).getUndertakingUnit());//承办单位
		holder.tv_sponsor.setText("主办方："+datas.get(position).getResponsibilityUnit());//主办单位
		holder.tv_title.setText("日程名称："+datas.get(position).getMeettingtitle());
		
		long currentTime = System.currentTimeMillis();
		long startTimeL = datas.get(position).getStartTime();
		long endTimeL  =datas.get(position).getEndTiime();
		
		if (endTimeL < currentTime) {//开过的
			holder.ll_container.setBackgroundResource(R.color.bar_gray);
			setTextColorForView(holder, Color.parseColor("#9c9c9e"),Color.parseColor("#9c9c9e"));
			holder.sch_content_ll.setBackgroundResource(R.color.white);
			holder.sch_item_view1.setBackgroundResource(R.color.sch_line_color);
			holder.sch_item_view2.setBackgroundResource(R.color.sch_line_color);
			holder.sch_end_dotL.setImageResource(R.drawable.bg_graydot_dis);
			holder.sch_end_dotR.setImageResource(R.drawable.bg_graydot_dis);
		}else if (startTimeL < currentTime && endTimeL > currentTime) {//正在进行
			holder.ll_container.setBackgroundResource(R.color.bar_green);
			setTextColorForView(holder, Color.parseColor("#ffffff"),Color.parseColor("#3e4750"));
			holder.sch_content_ll.setBackgroundResource(R.color.content_green);
			holder.sch_item_view1.setBackgroundResource(R.color.white);
			holder.sch_item_view2.setBackgroundResource(R.color.white);
			holder.sch_end_dotL.setImageResource(R.drawable.bg_whitedot_sel);
			holder.sch_end_dotR.setImageResource(R.drawable.bg_whitedot_sel);
		}else if (startTimeL > currentTime) {//未开始的
			holder.ll_container.setBackgroundResource(R.color.bar_lightGreen);
			setTextColorForView(holder, Color.parseColor("#64b6c2"),Color.parseColor("#3e4750"));
			holder.sch_content_ll.setBackgroundResource(R.color.white);
			holder.sch_item_view1.setBackgroundResource(R.color.sch_line_color);
			holder.sch_item_view2.setBackgroundResource(R.color.sch_line_color);
			holder.sch_end_dotL.setImageResource(R.drawable.bg_greendot_nor);
			holder.sch_end_dotR.setImageResource(R.drawable.bg_greendot_nor);
		}
		System.out.println("========当前时间---long---》"+currentTime);
		Constant.getCurrentTimeToHm(currentTime);
		return convertView;
	}
	
	public void setTextColorForView(ViewHolder holder,int timecolor,int color) {
		holder.tv_endTime.setTextColor(timecolor);
		holder.tv_fuzeren.setTextColor(color);
		holder.tv_hall.setTextColor(color);
		holder.tv_hostParty.setTextColor(color);
		holder.tv_sponsor.setTextColor(color);
		holder.tv_startTime.setTextColor(timecolor);
		holder.tv_title.setTextColor(color);
	}
   class ViewHolder{
	   LinearLayout ll_container,sch_content_ll;
	   ImageView iv_icon,sch_end_dotL,sch_end_dotR;
	   View sch_item_view1,sch_item_view2;
	   TextView tv_startTime,tv_endTime,tv_title,tv_hall,tv_sponsor,tv_hostParty,tv_fuzeren;
   }
}
