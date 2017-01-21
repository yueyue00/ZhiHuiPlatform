package com.smartdot.wenbo.controlcenter.adapter;

import java.util.List;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.bean.HomeBean;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecycleAdapterForHome extends RecyclerView.Adapter<RecycleAdapterForHome.ViewHolder> {
	 private Context context;
	    private List<HomeBean.InfoBean.MeettingBean> data;
	    TextView tv_title_now;
		TextView tv_host_now;
		TextView tv_charge_now;
		TextView tv_connect_now;
		TextView tv_title_next;
		TextView tv_host_next;
		TextView tv_charge_next;
		TextView tv_connect_next;
		private RecyclerView recyclerView;
		public RecycleAdapterForHome(Context context,
				List<HomeBean.InfoBean.MeettingBean> data,
				TextView tv_title_now,
				TextView tv_host_now, TextView tv_charge_now,
				TextView tv_connect_now, TextView tv_title_next,
				TextView tv_host_next, TextView tv_charge_next,
				TextView tv_connect_next) {
			this.context = context;
			this.data = data;
			this.tv_charge_next = tv_charge_next;
			this.tv_charge_now = tv_charge_now;
			this.tv_connect_next = tv_connect_next;
			this.tv_connect_now = tv_connect_now;
			this.tv_host_next  = tv_host_next;
			this.tv_host_now = tv_host_now;
			this.tv_title_next = tv_title_next;
			this.tv_title_now = tv_title_now;
		}

	@Override
	public int getItemCount() {
		int ret = 0;
        if (data != null) {
            ret = data.size();
        }
        return ret;
	}
	 @Override
	    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
	        super.onAttachedToRecyclerView(recyclerView);
	        //这个recyclerView就是在MainActivity中进行了recyclerView.setAdapter(adapter);
	        //只要进行了setAdapter这个方法，备案：当前adapter依附到哪一个recyclerView上
	        this.recyclerView = recyclerView;
	    }
	@Override
	public void onBindViewHolder(RecycleAdapterForHome.ViewHolder holder, int position) {
		long startTime = data.get(position).getStartTime();
        long endTiime = data.get(position).getEndTiime();
        long current = System.currentTimeMillis();
        holder.tv_title.setText(data.get(position).getMeettingtitle());
        holder.tv_time.setText(Constant.getCurrentTimeToHm(startTime)+"-"+Constant.getCurrentTimeToHm(endTiime));
        if (endTiime < current) {
        	holder.tv_title.setTextColor(Color.parseColor("#2e6069"));
            holder.tv_time.setTextColor(Color.parseColor("#2e6069"));
		}else if (startTime < current && current < endTiime) {
			holder.tv_title.setTextColor(Color.parseColor("#46fcfb"));
            holder.tv_time.setTextColor(Color.parseColor("#ffffff"));
		}else if (current < startTime) {
			holder.tv_title.setTextColor(Color.parseColor("#43f200"));
            holder.tv_time.setTextColor(Color.parseColor("#ffffff"));
		}
        if (data.get(position).getType() == -1){//结束
            holder.iv_progressState.setVisibility(View.VISIBLE);
            holder.iv_arrow.setVisibility(View.VISIBLE);
            holder.iv_progressState.setImageResource(R.drawable.home_over);
            holder.iv_arrow.setImageResource(R.drawable.home_blue_arrow);
            
        }else if (data.get(position).getType() == 0){//正在进行
            holder.iv_progressState.setVisibility(View.INVISIBLE);
            holder.iv_arrow.setVisibility(View.INVISIBLE);
            holder.tv_title.setBackgroundResource(R.drawable.home_now_progress);
            holder.tv_title.setTextColor(Color.parseColor("#46fcfb"));
            holder.tv_time.setTextColor(Color.parseColor("#ffffff"));
            
            
//            tv_title_now.setText(data.get(position).getMeettingtitle());
//            tv_host_now.setText("演讲人："+data.get(position).getSpeakPerson());
//            tv_charge_now.setText("会场负责人："+data.get(position).getVenueContact());
//            tv_connect_now.setText("敦煌联络员："+data.get(position).getDunhuangService());
            
        }else if (data.get(position).getType() == 1){//未进行
            holder.iv_progressState.setVisibility(View.VISIBLE);
            holder.iv_arrow.setVisibility(View.VISIBLE);
            holder.iv_progressState.setImageResource(R.drawable.home_no_progress);
            holder.iv_arrow.setImageResource(R.drawable.home_green_arrow);
            
//            tv_title_next.setText(data.get(position).getMeettingtitle());
//            tv_host_next.setText("提供单位："+data.get(position).getUndertakingUnit());
//            tv_charge_next.setText("负责人："+data.get(position).getUndertakingContact());
//            tv_connect_next.setText("敦煌联络员："+data.get(position).getDunhuangService());
            
        }else{
        	holder.tv_title.setBackground(null);
        	holder.iv_progressState.setVisibility(View.INVISIBLE);
            holder.iv_arrow.setVisibility(View.INVISIBLE);
            
//            tv_title_next.setText(data.get(position).getMeettingtitle());
//            tv_host_next.setText("提供单位："+data.get(position).getUndertakingUnit());
//            tv_charge_next.setText("负责人："+data.get(position).getUndertakingContact());
//            tv_connect_next.setText("敦煌联络员："+data.get(position).getDunhuangService());
        }		
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		
		View view = LayoutInflater.from(context).inflate(R.layout.listview_item,null);
		return new ViewHolder(view);
	}
	
	
	 public class ViewHolder extends RecyclerView.ViewHolder {
		    private ImageView iv_progressState,iv_arrow;
	        private TextView tv_title,tv_time;
	        
	        public ViewHolder(View itemView) {
	            super(itemView);
	            iv_progressState = (ImageView) itemView.findViewById(R.id.li_iv_progressState);
	            iv_arrow = ((ImageView) itemView.findViewById(R.id.li_iv_over_arrow));
	            tv_title = (TextView) itemView.findViewById(R.id.li_tv_titile);
	            tv_time = (TextView) itemView.findViewById(R.id.li_tv_time);
	        }
	    }
}
