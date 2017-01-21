package com.smartdot.wenbo.controlcenter.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.bean.CarInfo;

public class CarSourceAdapter extends BaseAdapter {
	Context mcontext;
	List<CarInfo> datas;

	public CarSourceAdapter(Context mcontext, List<CarInfo> datas) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mcontext).inflate(
					R.layout.adapter_carsource_lv, null);
			holder.phone_iv = (ImageView) convertView
					.findViewById(R.id.phone_iv);
			holder.car_num = (TextView) convertView.findViewById(R.id.car_num);
			holder.car_state = (TextView) convertView
					.findViewById(R.id.car_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (datas.get(position).carNumber != null)
			holder.car_num.setText(datas.get(position).carNumber);
		if (datas.get(position).state != null)
			switch (datas.get(position).state) {
			case "0":
				holder.car_state.setText("空闲");
				holder.car_state
						.setBackgroundResource(R.drawable.shape_adapter_carstate1);
				break;
			case "1":
				holder.car_state.setText("行驶中");
				holder.car_state
						.setBackgroundResource(R.drawable.shape_adapter_carstate2);
			default:
				break;
			}
		holder.phone_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (datas.get(position).carTel != null) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri
							.parse("tel:" + datas.get(position).carTel));
					mcontext.startActivity(intent);
				}
			}
		});
		return convertView;
	}

	public class ViewHolder {
		ImageView phone_iv;
		TextView car_num, car_state;
	}
}
