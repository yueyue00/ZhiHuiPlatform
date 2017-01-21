package com.smartdot.wenbo.controlcenter.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.bean.HotelInfo;

public class HotelsAdapter extends BaseAdapter {
	Context mcontext;
	List<HotelInfo> datas;

	public HotelsAdapter(Context mcontext, List<HotelInfo> datas) {
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
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mcontext).inflate(
					R.layout.adapter_hotel_lv, null);
			holder.hotel_state_pb = (ProgressBar) convertView
					.findViewById(R.id.hotel_state_pb);
			holder.hotel_name = (TextView) convertView
					.findViewById(R.id.hotel_name);
			holder.hotel_used = (TextView) convertView
					.findViewById(R.id.hotel_used);
			holder.hotel_count = (TextView) convertView
					.findViewById(R.id.hotel_count);
			holder.hotel_workused = (TextView) convertView
					.findViewById(R.id.hotel_workused);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.hotel_name.setText(datas.get(position).hotelName);
		if (datas.get(position).workUsered != null)
			holder.hotel_workused.setText(datas.get(position).workUsered + "/");
		if (datas.get(position).roomUsered != null)
			holder.hotel_used.setText(datas.get(position).roomUsered + "/");
		if (datas.get(position).roomCount != null)
			holder.hotel_count.setText(datas.get(position).roomCount);
		try {
			Double workUsered = Double
					.parseDouble(datas.get(position).workUsered);
			Double roomused = Double
					.parseDouble(datas.get(position).roomUsered);
			Double roomcount = Double
					.parseDouble(datas.get(position).roomCount);
			holder.hotel_state_pb
					.setProgress((int) ((workUsered / roomcount) * 100));
			holder.hotel_state_pb
					.setSecondaryProgress((int) ((roomused / roomcount) * 100));
		} catch (Exception e) {

		}
		return convertView;
	}

	public class ViewHolder {
		ProgressBar hotel_state_pb;

		TextView hotel_name, hotel_workused, hotel_used, hotel_count;
	}
}
