package com.smartdot.wenbo.controlcenter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.bean.ContactBean;

public class AdapterContactHor extends BaseAdapter {

	private ArrayList<ContactBean> list;
	private Context context;
	private LayoutInflater li;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options_liebbiao;

	public AdapterContactHor(ArrayList<ContactBean> list, Context context) {
		this.list = list;
		this.context = context;
		li = LayoutInflater.from(context);
		options_liebbiao = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型
				.displayer(new RoundedBitmapDisplayer(200)).build();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyViewHolder mvh = null;
		if (convertView == null) {
			convertView = this.li.inflate(R.layout.zyjitem_dept_horizontal,
					null);
			mvh = new MyViewHolder(convertView);
			convertView.setTag(mvh);
		} else {
			mvh = (MyViewHolder) convertView.getTag();
		}
		mvh.nametv.setText(list.get(position).name);
		// imageLoader.displayImage(list.get(position).guestphotourl,
		// mvh.guest_icon, options_liebbiao);
		return convertView;
	}
	class MyViewHolder {
		TextView right;
		TextView nametv;

		public MyViewHolder(View itemview) {
			right = (TextView) itemview.findViewById(R.id.right);
			nametv = (TextView) itemview.findViewById(R.id.nametv);
		}
	}
}
