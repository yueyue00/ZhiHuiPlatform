package com.smartdot.wenbo.controlcenter.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartdot.wenbo.controlcenter.R;

public class ImportGuestInfoAdapter extends BaseAdapter {

	private Context context;
	private List<String> kvInfoList;
	private List<String> jbxxInfoList;

	public ImportGuestInfoAdapter(Context context, List<String> kvInfoList,
			List<String> jbxxInfoList) {
		this.context = context;
		this.kvInfoList = kvInfoList;
		this.jbxxInfoList = jbxxInfoList;
	}

	@Override
	public int getCount() {
		return kvInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup view) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.adapter_importguestinfo_gv, null);
			holder.vip_info_top = (TextView) convertView
					.findViewById(R.id.vip_info_top);
			holder.vip_info_bottom = (TextView) convertView
					.findViewById(R.id.vip_info_bottom);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.vip_info_top.setText(kvInfoList.get(position));
		holder.vip_info_bottom.setText(jbxxInfoList.get(position));
		return convertView;
	}

	class ViewHolder {
		TextView vip_info_top, vip_info_bottom;
	}
}
