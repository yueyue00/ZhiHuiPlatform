package com.smartdot.wenbo.controlcenter.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.bean.ImportGuest;

public class ImportGuestAdapter extends BaseAdapter {
	Context mcontext;
	List<ImportGuest> datas;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options_guesticon;

	public ImportGuestAdapter(Context mcontext, List<ImportGuest> datas) {
		this.mcontext = mcontext;
		this.datas = datas;
		options_guesticon = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.a_huiyiricheng_guesticon)
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型
				.displayer(new RoundedBitmapDisplayer(200)).build();
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
					R.layout.adapter_importguest_lv, null);
			holder.guest_icon = (ImageView) convertView
					.findViewById(R.id.guest_icon);
			holder.guest_name = (TextView) convertView
					.findViewById(R.id.guest_name);
			holder.guset_zhiwei = (TextView) convertView
					.findViewById(R.id.guset_zhiwei);
			holder.guest_workplace = (TextView) convertView
					.findViewById(R.id.guest_workplace);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.guest_name.setText(datas.get(position).username);
		holder.guset_zhiwei.setText(datas.get(position).job);
		holder.guest_workplace.setText(datas.get(position).workplace);
		imageLoader.displayImage(datas.get(position).photourlbig,
				holder.guest_icon, options_guesticon);
		return convertView;
	}

	public class ViewHolder {
		ImageView guest_icon;
		TextView guest_name, guset_zhiwei, guest_workplace;
	}
}
