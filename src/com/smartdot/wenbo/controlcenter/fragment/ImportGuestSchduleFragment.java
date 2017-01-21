package com.smartdot.wenbo.controlcenter.fragment;

import io.rong.imlib.model.UserInfo;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.adapter.StickyHeaderListViewForVip;
import com.smartdot.wenbo.controlcenter.bean.ContactBean;
import com.smartdot.wenbo.controlcenter.bean.User;
import com.smartdot.wenbo.controlcenter.bean.VipScheduleInfoBean;
import com.smartdot.wenbo.controlcenter.customview.stickylistview.StickyListHeadersListView;
import com.smartdot.wenbo.controlcenter.task.zAsyncTaskTuanScheduleTask;
import com.smartdot.wenbo.controlcenter.task.zAsyncTaskVipScheduleTask;
import com.smartdot.wenbo.controlcenter.util.MSharePreferenceUtils;

import de.greenrobot.event.EventBus;

public class ImportGuestSchduleFragment extends Fragment {

	private Context mContext;
	private StickyListHeadersListView vipStickyListHeadersListView;
	private TextView emptyView;
	private StickyHeaderListViewForVip adapter;
	ContactBean contactBean;
	User parent;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			emptyView.setVisibility(View.GONE);
			vipStickyListHeadersListView.setVisibility(View.VISIBLE);
			switch (msg.what) {
			case 1:
				List<VipScheduleInfoBean> result = (List<VipScheduleInfoBean>) msg.obj;
				adapter = new StickyHeaderListViewForVip(getActivity(), result);
				vipStickyListHeadersListView.setAdapter(adapter);
				break;
			case -1:
				Toast.makeText(mContext, "请求数据有误", Toast.LENGTH_LONG).show();
				break;
			case -2:
				Toast.makeText(mContext, "请求数据为空", Toast.LENGTH_LONG).show();
				break;
			case 300:
				String resultThree = (String) msg.obj;
				emptyView.setVisibility(View.VISIBLE);
				vipStickyListHeadersListView.setVisibility(View.GONE);
				break;
			case 500:
				String resultFive = (String) msg.obj;
				emptyView.setVisibility(View.VISIBLE);
				vipStickyListHeadersListView.setVisibility(View.GONE);
				break;
			case 400:
				String resultFour = (String) msg.obj;
				emptyView.setVisibility(View.VISIBLE);
				vipStickyListHeadersListView.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		};
	};
	private Handler tuan_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			emptyView.setVisibility(View.GONE);
			vipStickyListHeadersListView.setVisibility(View.VISIBLE);
			switch (msg.what) {
			case 1:
				List<VipScheduleInfoBean> result = (List<VipScheduleInfoBean>) msg.obj;
				adapter = new StickyHeaderListViewForVip(getActivity(), result);
				vipStickyListHeadersListView.setAdapter(adapter);
				break;
			case -1:
				Toast.makeText(mContext, "请求数据有误", Toast.LENGTH_LONG).show();
				break;
			case -2:
				Toast.makeText(mContext, "请求数据为空", Toast.LENGTH_LONG).show();
				break;
			case 300:
				String resultThree = (String) msg.obj;
				emptyView.setVisibility(View.VISIBLE);
				vipStickyListHeadersListView.setVisibility(View.GONE);
				break;
			case 500:
				String resultFive = (String) msg.obj;
				emptyView.setVisibility(View.VISIBLE);
				vipStickyListHeadersListView.setVisibility(View.GONE);
				break;
			case 400:
				String resultFour = (String) msg.obj;
				emptyView.setVisibility(View.VISIBLE);
				vipStickyListHeadersListView.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		MSharePreferenceUtils.getAppConfig(mContext);
		parent = (User) MSharePreferenceUtils.getBean(mContext,
				Constant.sp_user);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.fragment_vip_schedule, container, false);
		initView(view);
		return view;
	}

	public void onEventMainThread(ContactBean contactBean) {
		this.contactBean = contactBean;
		if (contactBean != null) {
			initData();// 从网络请求数据
			if(contactBean.type.equals("1")){//团
				emptyView.setText("该团没有行程");
			}else{
				emptyView.setText("该用户没有行程");
			}
		}
	}

	/**
	 * 从网络请求数据
	 */
	private void initData() {
		if (contactBean.type.equals("1")) {
			try {
				new zAsyncTaskTuanScheduleTask(tuan_handler.obtainMessage(),
						mContext, Constant.decode(Constant.key, parent.getUserId()
								.trim()), contactBean.id).execute(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			new zAsyncTaskVipScheduleTask(handler.obtainMessage(),
					contactBean.id, mContext).execute(1);
		}
	}

	private void initView(View view) {
		vipStickyListHeadersListView = (StickyListHeadersListView) view
				.findViewById(R.id.vip_stickyHeaderview);
		emptyView = (TextView) view.findViewById(R.id.vip_tv_empty);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
