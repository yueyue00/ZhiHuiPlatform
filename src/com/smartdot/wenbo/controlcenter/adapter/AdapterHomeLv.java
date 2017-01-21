package com.smartdot.wenbo.controlcenter.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartdot.wenbo.controlcenter.R;

public class AdapterHomeLv extends BaseAdapter {
	Context mcontext;
	List<String> datas;

	public AdapterHomeLv(Context mcontext, List<String> datas) {
		this.mcontext = mcontext;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mcontext).inflate(
					R.layout.adapter_homelv, null);
			holder.home_icon = (ImageView) convertView
					.findViewById(R.id.home_icon);
			holder.home_name = (TextView) convertView
					.findViewById(R.id.home_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.home_name.setText(datas.get(position));
		switch (position) {
		case 0:
			holder.home_icon
					.setBackgroundResource(R.drawable.btn_meeting_schdule_progress);
			break;
		case 1:
			holder.home_icon.setBackgroundResource(R.drawable.btn_daohui_state);
			break;
		case 2:
			holder.home_icon
					.setBackgroundResource(R.drawable.btn_fuwuxianzhuang);
			break;
		case 3:
			holder.home_icon.setBackgroundResource(R.drawable.btn_importvip);
			break;
		case 4:
			holder.home_icon.setBackgroundResource(R.drawable.btn_jiudianfenbu);
			break;
		case 5:
			holder.home_icon.setBackgroundResource(R.drawable.btn_carsource);
			break;
		case 6:
			holder.home_icon.setBackgroundResource(R.drawable.btn_video);
			break;
		case 7:
			holder.home_icon.setBackgroundResource(R.drawable.btn_fuwudiaodu);
			break;

		default:
			break;
		}
		return convertView;
	}

	public class ViewHolder {
		ImageView home_icon;
		TextView home_name;
	}
}
