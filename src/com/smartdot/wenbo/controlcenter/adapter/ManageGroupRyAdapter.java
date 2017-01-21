package com.smartdot.wenbo.controlcenter.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.bean.ManageGroupoHeadWaiter;

public class ManageGroupRyAdapter extends BaseAdapter {
	Context mcontext;
	List<ManageGroupoHeadWaiter> datas;

	public ManageGroupRyAdapter(Context mcontext,
			List<ManageGroupoHeadWaiter> datas) {
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
					R.layout.adapter_manage_grouporenyuan, null);
			holder.manage_group_icon = (ImageView) convertView
					.findViewById(R.id.manage_group_icon);
			holder.manage_group_name = (TextView) convertView
					.findViewById(R.id.manage_group_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// holder.manage_group_nameS.setText(datas.get(position));
		if (datas.get(position).name != null)
			holder.manage_group_name.setText(datas.get(position).name);
		return convertView;
	}

	public class ViewHolder {
		ImageView manage_group_icon;
		TextView manage_group_name;
	}
}
