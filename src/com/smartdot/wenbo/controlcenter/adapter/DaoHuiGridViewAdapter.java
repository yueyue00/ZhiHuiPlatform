package com.smartdot.wenbo.controlcenter.adapter;

import java.util.List;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.R.id;
import com.smartdot.wenbo.controlcenter.bean.DaoHuiGridBean;
import com.smartdot.wenbo.controlcenter.bean.MeetingDataBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DaoHuiGridViewAdapter extends BaseAdapter{
    private Context context;
    private List<MeetingDataBean> data;
	
    
	
	public DaoHuiGridViewAdapter(Context context, List<MeetingDataBean> data) {
		super();
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		
		if (data!=null) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.meetting_statue_gridview_item, null);
			holder.tv_count = (TextView) convertView.findViewById(R.id.grid_item_tv_count);
			holder.tv_hotel = (TextView) convertView.findViewById(R.id.grid_item_tv_hotelName);
			holder.tv_shortname = (TextView) convertView.findViewById(R.id.grid_item_tv_shortname);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_count.setText(data.get(position).getCount()+"");
		holder.tv_hotel.setText(data.get(position).getName());
		holder.tv_shortname.setText(data.get(position).getShortName());
		return convertView;
	}
class ViewHolder{
	TextView tv_count,tv_hotel,tv_shortname;
}
}
