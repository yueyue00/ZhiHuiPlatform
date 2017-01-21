package com.smartdot.wenbo.controlcenter.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.bean.VideoSourceBean;
import com.smartdot.wenbo.controlcenter.task.VideoTask;
import com.smartdot.wenbo.controlcenter.util.MSharePreferenceUtils;

public class StartActivity extends Activity {
	private Context mContext;
	
	private Handler videoHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				// String result = (String) msg.obj;
				// System.out.println("=====视频列表=="+result);
				
				System.out.println("==================startActivity=====");
				VideoSourceBean bean = (VideoSourceBean) msg.obj;
				List<com.smartdot.wenbo.controlcenter.bean.VideoSourceBean.InfoBean> info = bean
						.getInfo();
				for (int i = 0; i < info.size(); i++) {
					int vedioSort = info.get(i).getVedioSort();
					if (vedioSort == 1) {// 视频1
//						Constant.VIDEOPATH_1 = info.get(i).getVedioPath();
						MSharePreferenceUtils.setParam("video1", info.get(i).getVedioPath());
					} else if (vedioSort == 2) {// 视频2
//						Constant.VIDEOPATH_2 = info.get(i).getVedioPath();
						MSharePreferenceUtils.setParam("video2", info.get(i).getVedioPath());
					} else if (vedioSort == 3) {// 视频3
//						Constant.VIDEOPATH_3 = info.get(i).getVedioPath();
						MSharePreferenceUtils.setParam("video3", info.get(i).getVedioPath());
					} else if (vedioSort == 4) {// 视频4
//						Constant.VIDEOPATH_4 = info.get(i).getVedioPath();
						MSharePreferenceUtils.setParam("video4", info.get(i).getVedioPath());
					}
					
				}
				process();
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);
		mContext = this;
		MSharePreferenceUtils.getAppConfig(mContext);
		loadVideoData();
		process();
	}
	private void loadVideoData() {
		System.out.println("====调用视频接口");
		try {
			new VideoTask(videoHandler.obtainMessage(), mContext,
					"")
					.execute(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void process() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
					Intent intent = new Intent(StartActivity.this,
							LoginActivity.class);
					startActivity(intent);
					StartActivity.this.finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
