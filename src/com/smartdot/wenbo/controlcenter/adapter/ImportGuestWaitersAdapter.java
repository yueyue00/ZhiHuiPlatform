package com.smartdot.wenbo.controlcenter.adapter;

import io.rong.imlib.model.Conversation;

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
import com.smartdot.wenbo.controlcenter.bean.RongConversitionInfo;
import com.smartdot.wenbo.controlcenter.bean.ZhuanShuFuWu;

import de.greenrobot.event.EventBus;

public class ImportGuestWaitersAdapter extends BaseAdapter {
	Context mcontext;
	List<ZhuanShuFuWu> datas;

	public ImportGuestWaitersAdapter(Context mcontext, List<ZhuanShuFuWu> datas) {
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
					R.layout.adapter_importguest_waiterlv, null);
			holder.user_icon = (ImageView) convertView
					.findViewById(R.id.user_icon);
			holder.mail_image = (ImageView) convertView
					.findViewById(R.id.mail_image);
			holder.user_name = (TextView) convertView
					.findViewById(R.id.user_name);
			holder.user_type = (TextView) convertView
					.findViewById(R.id.user_type);
			holder.phone_number = (TextView) convertView
					.findViewById(R.id.phone_number);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.user_name.setText(datas.get(position).name);
		holder.user_type.setText(datas.get(position).juese);
		holder.phone_number.setText(datas.get(position).mobile);

		holder.mail_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(
						new RongConversitionInfo(
								Conversation.ConversationType.PRIVATE, datas
										.get(position).ry_userId, datas
										.get(position).name));
			}
		});
		holder.phone_number.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ datas.get(position).mobile));
				mcontext.startActivity(intent);
			}
		});
		return convertView;
	}

	public class ViewHolder {
		ImageView user_icon, mail_image;
		TextView user_name, user_type, phone_number;
	}
}
