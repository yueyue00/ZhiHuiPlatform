package com.smartdot.wenbo.controlcenter.adapter;

import io.rong.imkit.RongIM;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.activity.BaseActivity;
import com.smartdot.wenbo.controlcenter.activity.MainActivity;
import com.smartdot.wenbo.controlcenter.bean.User;
import com.smartdot.wenbo.controlcenter.bean.XiaoXiPojo;

public class AdapterForXiaoXiZhongXin extends RecyclerView.Adapter<ViewHolder> {

	public ArrayList<XiaoXiPojo> list;// 消息提醒数据集合
	public Context cont;
	public LayoutInflater lf;

	public AdapterForXiaoXiZhongXin(Context cont, ArrayList<XiaoXiPojo> list) {
		this.cont = cont;
		this.list = list;
		this.lf = LayoutInflater.from(cont);
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	// 这个方法返回item的view 不必考虑复用问题
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		// TODO Auto-generated method stub
		View v = this.lf.inflate(R.layout.item_xiaoxizhongxinlistrecyclerview,
				vg, false);
		return new MyViewHolder(v);
	}

	@Override
	// 这个方法用来实现数据和item的捆绑
	public void onBindViewHolder(ViewHolder vh, int position) {
		// TODO Auto-generated method stub
		MyViewHolder mvh = (MyViewHolder) vh;// 强转为自定义viewHolder

		// 重要说明：为了保证布局美观，所以受到的数据，要进行截取
		String sx = "";
		if (list.get(position).messagetype.equals("0")) {
			mvh.xiaoxitypelogo.setImageDrawable(this.cont.getResources()
					.getDrawable(R.drawable.wuzhen_infocenter));
		}
		if (list.get(position).messagetype.equals("1")) {
			mvh.xiaoxitypelogo.setImageDrawable(this.cont.getResources()
					.getDrawable(R.drawable.wuzhen_infocenter_personalinfo));
			sx = list.get(position).servicename + ":";
		}
		if (list.get(position).messagetype.equals("2")) {
			mvh.xiaoxitypelogo.setImageDrawable(this.cont.getResources()
					.getDrawable(R.drawable.wuzhen_infocenter_remind));
		}
		if (list.get(position).messagetype.equals("3")) {// 其他消息
			mvh.xiaoxitypelogo.setImageDrawable(this.cont.getResources()
					.getDrawable(R.drawable.wuzhen_infocenter));
		}

		// if (list.get(position).messagetype.equals("1")) {// 私信不显示时间
		// mvh.time.setVisibility(View.INVISIBLE);
		// } else {
		// mvh.time.setVisibility(View.VISIBLE);
		// }
		// if (cont instanceof MainActivity) {// 属于从MainActivity设置的适配器
		// if (position == 0)
		// mvh.line.setVisibility(View.GONE);
		// }
		mvh.title.setText(sx + list.get(position).title);
		mvh.time.setText(list.get(position).time);
		if (list.get(position).state.equals("0")) {
			mvh.title.setTextColor(cont.getResources().getColor(
					R.color.zuijinsousuo));
		}
		if (list.get(position).state.equals("1")) {
			mvh.title.setTextColor(cont.getResources().getColor(
					R.color.searchhuiyirvfengexian));
		}

	}

	@Override
	// 重写这个方法，可以控制每一行item返回的view，可以实现header和footer
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

	/**
	 * 开发者需要自己创建ViewHolder类
	 * 并定义item中控件对象，并完成初始化，不同的item（比如header或footer，需要开发者另行自定义ViewHolder）
	 */
	class MyViewHolder extends RecyclerView.ViewHolder {

		ImageView xiaoxitypelogo;// 消息类型图标
		TextView time; // 消息时间
		TextView title;// 消息标题

		public MyViewHolder(View itemView) {
			super(itemView);
			xiaoxitypelogo = (ImageView) itemView
					.findViewById(R.id.xiaoxitypelogo);
			title = (TextView) itemView.findViewById(R.id.title);
			time = (TextView) itemView.findViewById(R.id.time);
		}
	}

}
