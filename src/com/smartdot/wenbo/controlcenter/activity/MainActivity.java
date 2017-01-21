package com.smartdot.wenbo.controlcenter.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.adapter.AdapterHomeLv;
import com.smartdot.wenbo.controlcenter.fragment.CarSourceFragment;
import com.smartdot.wenbo.controlcenter.fragment.HotelLocationsFragment;
import com.smartdot.wenbo.controlcenter.fragment.ImportantGuestFragment;
import com.smartdot.wenbo.controlcenter.fragment.ManageFragment;
import com.smartdot.wenbo.controlcenter.fragment.MeetingStateFragment;
import com.smartdot.wenbo.controlcenter.fragment.ScheduleFragment;
import com.smartdot.wenbo.controlcenter.fragment.ServiceStateFragment;
import com.smartdot.wenbo.controlcenter.fragment.VideoFragment;
import com.smartdot.wenbo.controlcenter.rong.RongCloudEvent;

public class MainActivity extends FragmentActivity {
    public Window window;
	private Context mContext;
	private String tag;
	private final String TAG_MEETING = "meetingSchedule";
	ListView home_lv;
	TextView home_tv;
	LinearLayout ll_backToHome;
	AdapterHomeLv home_adapter;
	ArrayList<String> list;
	HashMap<String, Fragment> fragmentMap;
	public String MeetingProgress = "MEETINGPROGRESS";
	public String MeetingState = "MEETINGSTATE";
	public String ServiceState = "SERVICESTATE";
	public String ImportantGuest = "IMPORTANTGUEST";
	public String HotelLocations = "HOTELLOCATIONS";
	public String CarResource = "CARRESOURCE";
	public String VideoMonitoring = "VIDEOMONITORING";
	public String ManageCenter = "MANAGECENTER";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		RongCloudEvent.init(this);
		mContext = this;
		window = getWindow();
		Intent intent = getIntent();
		tag = intent.getStringExtra("tag");
//		CustomToast.showToast(mContext, "====>" + tag);
		initView();
		initData();
		procss();
		setAllClick();
		
	}

	@SuppressWarnings("rawtypes")
	private void switchFragment(String showTag) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		Iterator iter = fragmentMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Fragment f = (Fragment) entry.getValue();
			if (f == fragmentMap.get(showTag)) {
				if (!f.isAdded()) {// 未被添加
					transaction
							.add(R.id.frame_layout, fragmentMap.get(showTag));
				} else if (f.isHidden()) {// 添加了但被隐藏
					transaction.show(f);
				}
			} else if (f.isAdded() && !f.isHidden()) {// 被添加但未被隐藏
				transaction.hide(f);
			}
		}
		transaction.commit();
	}

	private void setAllClick() {
		
		home_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				switch (position) {
				case 0:
					switchFragment(MeetingProgress);
					break;
				case 1:
					switchFragment(MeetingState);
					break;
				case 2:
					switchFragment(ServiceState);
					break;
				case 3:
					switchFragment(ImportantGuest);
					break;
				case 4:
					switchFragment(HotelLocations);
					break;
				case 5:
					switchFragment(CarResource);
					break;
				case 6:
					switchFragment(VideoMonitoring);
					break;
				case 7:
					switchFragment(ManageCenter);
					break;

				default:
					break;
				}
			}
		});
		
		ll_backToHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//
		switch (tag) {
		case "meetingSchedule":
			home_lv.setSelection(0);
			home_lv.setItemChecked(0, true);
			switchFragment(MeetingProgress);
			break;
		case "daohuiStatue":
			home_lv.setSelection(2);
			home_lv.setItemChecked(2, true);
			switchFragment(ServiceState);
			break;
		case "hotelDistribution":
			home_lv.setSelection(4);
			home_lv.setItemChecked(4, true);
			switchFragment(HotelLocations);
			break;
		case "video":
			home_lv.setSelection(6);
			home_lv.setItemChecked(6, true);
			switchFragment(VideoMonitoring);
			break;
		case "carSource":
			home_lv.setSelection(5);
			home_lv.setItemChecked(5, true);
			switchFragment(CarResource);
			break;
		default:
			break;
		}
	}

	private void initView() {
		home_tv = (TextView) findViewById(R.id.home_tv);
		home_lv = (ListView) findViewById(R.id.home_lv);
		ll_backToHome = (LinearLayout) findViewById(R.id.ll_backToHome);
	}

	private void initData() {
		list = new ArrayList<>();
		list.add("大会日程进度");
		list.add("到会现状");
		list.add("服务现状");
		list.add("嘉宾信息");
		list.add("酒店分布");
		list.add("车辆资源");
		list.add("视频监控");
		list.add("服务调度");
		fragmentMap = new HashMap<String, Fragment>();
		fragmentMap.put(MeetingProgress, new ScheduleFragment());
		fragmentMap.put(MeetingState, new MeetingStateFragment());
		fragmentMap.put(ServiceState, new ServiceStateFragment());
		fragmentMap.put(ImportantGuest, new ImportantGuestFragment());
		fragmentMap.put(HotelLocations, new HotelLocationsFragment());
		fragmentMap.put(CarResource, new CarSourceFragment());
		fragmentMap.put(VideoMonitoring, new VideoFragment());
		fragmentMap.put(ManageCenter, new ManageFragment());
	}

	private void procss() {
		home_adapter = new AdapterHomeLv(mContext, list);
		home_lv.setAdapter(home_adapter);
	}
}
