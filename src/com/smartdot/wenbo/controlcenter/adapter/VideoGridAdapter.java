package com.smartdot.wenbo.controlcenter.adapter;

import java.util.List;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.bean.VideoBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VideoGridAdapter extends BaseAdapter{
   
	private Context context;
	private List<VideoBean> data;
	
	public VideoGridAdapter(Context context, List<VideoBean> data) {
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
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.video_gridview_item, null);
			holder.tv_source = (TextView) convertView.findViewById(R.id.video_grid_item_tvSource);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_source.setText(data.get(position).getVideoSource());
		return convertView;
	}
  class ViewHolder{
	  TextView tv_source;
  }
}
