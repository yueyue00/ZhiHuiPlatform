package com.smartdot.wenbo.controlcenter.activity;

import com.smartdot.wenbo.controlcenter.R.layout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.panframe.android.lib.PFAsset;
import com.panframe.android.lib.PFAssetObserver;
import com.panframe.android.lib.PFAssetStatus;
import com.panframe.android.lib.PFNavigationMode;
import com.panframe.android.lib.PFObjectFactory;
import com.panframe.android.lib.PFView;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.adapter.HomeAdapter;
import com.smartdot.wenbo.controlcenter.adapter.HomeBigAdapter;
import com.smartdot.wenbo.controlcenter.bean.HomeBean;
import com.smartdot.wenbo.controlcenter.bean.HomeImageBean;
import com.smartdot.wenbo.controlcenter.bean.NewApkVersionPojo;
import com.smartdot.wenbo.controlcenter.bean.User;
import com.smartdot.wenbo.controlcenter.bean.VideoSourceBean;
import com.smartdot.wenbo.controlcenter.bean.HomeBean.InfoBean;
import com.smartdot.wenbo.controlcenter.bean.HomeBean.InfoBean.MeettingBean;
import com.smartdot.wenbo.controlcenter.customview.HorizontalListView;
import com.smartdot.wenbo.controlcenter.fragment.HomeCarSourceFragment;
import com.smartdot.wenbo.controlcenter.fragment.HomeHotelLocationsFragment;
import com.smartdot.wenbo.controlcenter.fragment.HomeMeetingStateFragment;
import com.smartdot.wenbo.controlcenter.fragment.HomeScheduleFragment;
import com.smartdot.wenbo.controlcenter.fragment.HomeVideoFragment;
import com.smartdot.wenbo.controlcenter.task.AsyncTaskForCheckApkVersion;
import com.smartdot.wenbo.controlcenter.task.AsyncTaskForDownloadTask;
import com.smartdot.wenbo.controlcenter.task.HomeImageTask;
import com.smartdot.wenbo.controlcenter.task.HomeTask;
import com.smartdot.wenbo.controlcenter.task.ScheduleTask;
import com.smartdot.wenbo.controlcenter.task.VideoTask;
import com.smartdot.wenbo.controlcenter.util.MSharePreferenceUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient.FileChooserParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HomeForBigActivity extends Activity implements OnClickListener
// ,PFAssetObserver, OnSeekBarChangeListener
{

	private WebView hotelInfo;
	private WebView connecter;
	private WebView vipLeave;
	private WebView worker;
	private TextView tv_title_now;
	private TextView tv_host_now;
	private TextView tv_chargeP_now;
	private TextView tv_connectP_now;
	private TextView tv_title_next;
	private TextView tv_host_next;
	private TextView tv_chargeP_next;
	private TextView tv_connectP_next;
	private HorizontalListView listView;
	private TextView tv_home_time1;
	private TextView tv_home_time2;
	private ImageView home_iv_video;
	private TextView home_tv_currentTime;
	private ImageView iv_homeHead;
	private TextView tv_homeTitle1;
	private TextView tv_homeTitle;
	private TextView home_horizonal_tvEmpty;
	private TextView tv_home_time1_day;
	private TextView tv_home_time2_day;
	private LinearLayout tv_home_time1_linear;
	private LinearLayout tv_home_time2_linear;
	private TextView tv_empty_next;
	private TextView tv_empty_now;

	private LinearLayout ll_sche;
	private RelativeLayout re_video;
	private LinearLayout ll_vipLeave;
	private FrameLayout fra_connect;
	private FrameLayout fra_worker;
	private RelativeLayout linaer_hotel;
	private boolean isback = false;// 点击两次退出
	// 遮盖层
	private View worker_view;
	private View connect_view;
	private View hotelInfo_view;
	private View vipLeave_view;
	// 更多视频
	private LinearLayout ll_video_homeMore;
	// 左上
	ViewGroup framecontainer_1;
	private LinearLayout home_video_qp1;
	// private LinearLayout home_video_motion1;
	// private ImageView home_iv_videoMotion1;
	// 左下
	ViewGroup framecontainer_2;
	private LinearLayout home_video_qp2;
	// private LinearLayout home_video_motion2;
	// private ImageView home_iv_videoMotion2;
	// 右上
	ViewGroup framecontainer_3;
	private LinearLayout home_video_qp3;
	// private LinearLayout home_video_motion3;
	// private ImageView home_iv_videoMotion3;
	// 右下
	ViewGroup framecontainer_4;
	private LinearLayout home_video_qp4;
	// private LinearLayout home_video_motion4;
	// private ImageView home_iv_videoMotion4;
	// 小时报
	private Button home_xiaoshibao;
	private PopupWindow popupWindow;
	private RelativeLayout container_relative;
	private TextView xsb_colose;
	private WebView xsb_wv;

	// rtmp 流----控件----开始
	// private String mVideoPath =
	// "rtmp://114.112.96.184:1938/conf1/speak";//测试地址
	// private String mVideoPath = "rtmp://114.112.96.184:1936/conf1/ppt";//正式地址
	// private String mVideoPath2 = "rtmp://61.178.27.233:19350/live/gsws_sd";//
	// 正式地址
	// private String mVideoPath3 =
	// "rtmp://61.178.27.233:19350/live/400ybs_sd";// 正式地址
	// private String mVideoPath1 = "rtmp:/live.hkstv.hk.lxdns.com/live/hks";//
	// 正式地址
	// private String mVideoPath2 = "rtmp:/live.hkstv.hk.lxdns.com/live/hks";//
	// 正式地址
	// private String mVideoPath3 = "rtmp:/live.hkstv.hk.lxdns.com/live/hks";//
	// 正式地址
	// private String mVideoPath4 = "rtmp:/live.hkstv.hk.lxdns.com/live/hks";//
	// 正式地址
	private String mVideoPath1 = "";// 正式地址
	private String mVideoPath2 = "";// 正式地址
	private String mVideoPath3 = "";// 正式地址
	private String mVideoPath4 = "";// 正式地址
	private static final String TAG = HomeNewActivity.class.getSimpleName();
	// 左上
	private SurfaceView mSurfaceView1;
	private View mLoadingView1;
	private PLMediaPlayer mMediaPlayer1;
	private AVOptions mAVOptions1;
	private int mSurfaceWidth1 = 0;
	private int mSurfaceHeight1 = 0;
	private boolean mIsStopped1 = false;
	private boolean mIsActivityPaused1 = true;
	// 左下
	private SurfaceView mSurfaceView2;
	private View mLoadingView2;
	private PLMediaPlayer mMediaPlayer2;
	private AVOptions mAVOptions2;
	private int mSurfaceWidth2 = 0;
	private int mSurfaceHeight2 = 0;
	private boolean mIsStopped2 = false;
	private boolean mIsActivityPaused2 = true;
	// 右上
	private SurfaceView mSurfaceView3;
	private View mLoadingView3;
	private PLMediaPlayer mMediaPlayer3;
	private AVOptions mAVOptions3;
	private int mSurfaceWidth3 = 0;
	private int mSurfaceHeight3 = 0;
	private boolean mIsStopped3 = false;
	private boolean mIsActivityPaused3 = true;
	// 右下
	private SurfaceView mSurfaceView4;
	private View mLoadingView4;
	private PLMediaPlayer mMediaPlayer4;
	private AVOptions mAVOptions4;
	private int mSurfaceWidth4 = 0;
	private int mSurfaceHeight4 = 0;
	private boolean mIsStopped4 = false;
	private boolean mIsActivityPaused4 = true;

	// rtmp----控件---结束--

	/**
	 * 10寸
	 */
//	private String urlHoteInfo1 = Constant.DOMAIN
//			+ "/comandPlatform.do?method=hotelInfoReportTotal";
//	private String urlConnecter1 = Constant.DOMAIN
//			+ "/comandPlatform.do?method=connectPersonInfoReport";
//	private String urlWorker1 = Constant.DOMAIN
//			+ "/comandPlatform.do?method=carInfoReport";
//	private String urlVipLeave1 = Constant.DOMAIN
//			+ "/comandPlatform.do?method=memberInfoReport";
	
	/**
	 * 大屏
	 */
	private String urlHoteInfo = Constant.DOMAIN
			+ "/comandPlatform.do?method=hotelInfoReportTotalScreen";
	private String urlConnecter = Constant.DOMAIN
			+ "/comandPlatform.do?method=connectPersonInfoReportScreen";
	private String urlWorker = Constant.DOMAIN
			+ "/comandPlatform.do?method=carInfoReportScreen";
	private String urlVipLeave = Constant.DOMAIN
			+ "/comandPlatform.do?method=memberInfoReportScreen";

	private Context mContext;
	private HomeScheduleFragment meetingScheduleHomeFragment;
	private HomeMeetingStateFragment daoHuiStatusHomeFragment;
	private HomeHotelLocationsFragment hotelDistributionHomeFragment;
	private HomeVideoFragment videoHomeFragment;
	private HomeCarSourceFragment carSourceHomeFragment;
	int myversioncode = 1;// 当前系统版本号

	private List<InfoBean> dataBeans = new ArrayList<>();
	private List<MeettingBean> data = new ArrayList<>();

	private HomeBigAdapter adapter;
	// private Timer mTimer;//定时器
	private int pingMuSize;
	private Task task;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home_for_big);
		mContext = this;
		
		MSharePreferenceUtils.getAppConfig(this);
		user = (User) MSharePreferenceUtils.getBean(this, Constant.sp_user);
		// mTimer = new Timer();
		initView();
		pingMuSize = getPingMuSize(mContext);
		initWebView(pingMuSize);
		process();
		CheckForUpdate();
		// loadImageView();
		loadData();
//		mVideoPath1 = Constant.VIDEOPATH_1;
//		mVideoPath2 = Constant.VIDEOPATH_2;
//		mVideoPath3 = Constant.VIDEOPATH_3;
//		mVideoPath4 = Constant.VIDEOPATH_4;
		mVideoPath1 = (String) MSharePreferenceUtils.getParam("video1", "");
		mVideoPath2 = (String) MSharePreferenceUtils.getParam("video2", "");
		mVideoPath3 = (String) MSharePreferenceUtils.getParam("video3", "");
		mVideoPath4 = (String) MSharePreferenceUtils.getParam("video4", "");
		adapter = new HomeBigAdapter(mContext, data, tv_title_now, tv_host_now,
				tv_chargeP_now, tv_connectP_now, tv_title_next, tv_host_next,
				tv_chargeP_next, tv_connectP_next);
		listView.setAdapter(adapter);

		task = new Task();
		handler.postDelayed(task, 60 * 1000);
//		 initRtmp1();
//		 initRtmp2();
//		 initRtmp3();
//		 initRtmp4();
		System.out.println("=====mVideoPath1=="+mVideoPath1);
		System.out.println("=====mVideoPath2=="+mVideoPath2);
		System.out.println("=====mVideoPath3=="+mVideoPath3);
		System.out.println("=====mVideoPath4=="+mVideoPath4);
		
		if (mVideoPath1 != null && !mVideoPath1.equals("")
				&& mVideoPath2 != null && !mVideoPath2.equals("")
				&& mVideoPath3 != null && !mVideoPath3.equals("")
				&& mVideoPath4 != null && !mVideoPath4.equals("")) {

		
		initRtmp1();
		initRtmp2();
		initRtmp3();
		initRtmp4();
		}else {
			Toast.makeText(mContext, "视频源为空", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 用于刷新webView
	 * 
	 * @author lixiaoming
	 * 
	 */
	class Task implements Runnable {

		@Override
		public void run() {
			// webView reload
			hotelInfo.reload();
			connecter.reload();
			worker.reload();
			vipLeave.reload();
			loadData();
			// loadImageView();
			home_tv_currentTime.setText("当前时间:"
					+ Constant.getCurrentTimeToHm(System.currentTimeMillis()));
			handler.postDelayed(task, 60 * 1000);
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case 1:

				HomeBean result = (HomeBean) msg.obj;
				dataBeans.clear();
				dataBeans.addAll(result.getInfo());
				System.out.println("=============首页大会日程======>"
						+ dataBeans.size());
				String currentTimeToDate = Constant.getCurrentTimeToDate(System
						.currentTimeMillis());
				// 当前天
				String subSequence = currentTimeToDate.substring(
						currentTimeToDate.length() - 2,
						currentTimeToDate.length());
				System.out.println("=====currentTimeToDate====>"
						+ currentTimeToDate);
				System.out.println("=====currentTimeToDate====>"
						+ Integer.parseInt(subSequence));

				for (int i = 0; i < dataBeans.size(); i++) {
					if (dataBeans.get(i).getFlag()
							.equals(StringToInt(subSequence) + "")) {// 当天
						List<MeettingBean> meetting = dataBeans.get(i)
								.getMeetting();
						if (meetting != null && meetting.size() != 0) {// 当天数据不为空
							if (meetting.size() == 1) {// 如果只有一个日程的话
								int type = meetting.get(0).getType();
								if (type == 0) {// 正在进行
									setVisibilityForTvNow(View.VISIBLE);
									tv_title_now.setText(meetting.get(0)
											.getMeettingtitle());
									tv_host_now.setText("演讲人："
											+ meetting.get(0).getSpeakPerson());
									tv_chargeP_now
											.setText("会场负责人："
													+ meetting.get(0)
															.getVenueContact());
									tv_connectP_now.setText("敦煌联络员："
											+ meetting.get(0)
													.getDunhuangService());
									tv_empty_now.setVisibility(View.GONE);
									setVisibilityForTvNext(View.GONE);
									tv_empty_next.setVisibility(View.VISIBLE);
								} else if (type == 1) {// 未进行
									setVisibilityForTvNext(View.VISIBLE);
									tv_title_next.setText(meetting.get(0)
											.getMeettingtitle());
									tv_host_next.setText("主办单位："
											+ meetting.get(0)
													.getResponsibilityUnit());
									tv_chargeP_next
											.setText("负责人："
													+ meetting
															.get(0)
															.getResponsibilityProvincePerson());
									tv_connectP_next.setText("敦煌联络员："
											+ meetting.get(0)
													.getDunhuangService());
									tv_empty_next.setVisibility(View.GONE);
									setVisibilityForTvNow(View.GONE);
									tv_empty_now.setVisibility(View.VISIBLE);
								} else {// 已结束
									setVisibilityForTvNow(View.GONE);
									tv_empty_now.setVisibility(View.VISIBLE);
									setVisibilityForTvNext(View.GONE);
									tv_empty_next.setVisibility(View.VISIBLE);
								}

							} else if (meetting.size() >= 2) {// 有>=2个日称

								for (int j = 0; j < meetting.size(); j++) {
									if (meetting.get(j).getType() == 0) {// 正在进行
										setVisibilityForTvNow(View.VISIBLE);
										tv_title_now.setText(meetting.get(j)
												.getMeettingtitle());
										tv_host_now.setText("演讲人："
												+ meetting.get(j)
														.getSpeakPerson());
										tv_chargeP_now.setText("会场负责人："
												+ meetting.get(j)
														.getVenueContact());
										tv_connectP_now.setText("敦煌联络员："
												+ meetting.get(j)
														.getDunhuangService());
										tv_empty_now.setVisibility(View.GONE);

									} else if (meetting.get(j).getType() == 1) {// 未进行
										setVisibilityForTvNext(View.VISIBLE);
										tv_title_next.setText(meetting.get(j)
												.getMeettingtitle());
										tv_host_next
												.setText("主办单位："
														+ meetting
																.get(j)
																.getResponsibilityUnit());
										tv_chargeP_next
												.setText("负责人："
														+ meetting
																.get(j)
																.getResponsibilityProvincePerson());
										tv_connectP_next.setText("敦煌联络员："
												+ meetting.get(j)
														.getDunhuangService());
										tv_empty_next.setVisibility(View.GONE);

									} 
									if (meetting.get(j).getType() == 2) {
										setVisibilityForTvNow(View.GONE);
										tv_empty_now
												.setVisibility(View.VISIBLE);
										setVisibilityForTvNext(View.GONE);
										tv_empty_next
												.setVisibility(View.VISIBLE);
									}
									 if (j == meetting.size()-1 && meetting.get(j).getType() == 0) {//返回数据的最后一条如果是整在进行的，则下一节数据清除
										 setVisibilityForTvNext(View.GONE);
											tv_empty_next
													.setVisibility(View.VISIBLE);
									}
									
//									else if (meetting.get(j).getType() != 0) {// 当天的数据中没有正在进行的数据就显示“暂无数据”
//										if (tv_title_now.getVisibility() == View.VISIBLE) {// 已经设置过为显示
//
//										} else {
//											setVisibilityForTvNow(View.GONE);
//											tv_empty_now
//													.setVisibility(View.VISIBLE);
//										}
//
//									} else if (meetting.get(j).getType() != 1) {// 当天的数据中没有下一节的数据就显示“暂无数据”
//										if (tv_title_next.getVisibility() == View.VISIBLE) {
//
//										} else {
//											setVisibilityForTvNext(View.GONE);
//											tv_empty_next
//													.setVisibility(View.VISIBLE);
//										}
//
//									}
								}

							}
						} else {// 当天数据为空
							setVisibilityForTvNow(View.GONE);
							setVisibilityForTvNext(View.GONE);
							tv_empty_now.setVisibility(View.VISIBLE);
							tv_empty_next.setVisibility(View.VISIBLE);
						}

					}

				}

				/**
				 * 设置当天数据 以及左右text文本
				 */
				for (int i = 0; i < dataBeans.size(); i++) {
					// System.out.println("=======flag===>"+dataBeans.get(i).getFlag()+"=====subSequence=>"+subSequence);
					if (dataBeans.get(i).getFlag()
							.equals(StringToInt(subSequence) - 1 + "")) {
						tv_home_time1
								.setText(StringToInt(subSequence) - 1 + "");
						tv_home_time1_day.setText("日");
					}
					if (dataBeans.get(i).getFlag()
							.equals(StringToInt(subSequence) + 1 + "")) {
						tv_home_time2
								.setText(StringToInt(subSequence) + 1 + "");
						tv_home_time2_day.setText("日");
					}
					if (dataBeans.get(i).getFlag()
							.equals(StringToInt(subSequence) + "")) {
						List<MeettingBean> meetting = dataBeans.get(i)
								.getMeetting();
						if (meetting != null && meetting.size() != 0) {
							data.clear();
							data.addAll(meetting);
							listView.setVisibility(View.VISIBLE);
							home_horizonal_tvEmpty.setVisibility(View.GONE);
							adapter.notifyDataSetChanged();
						} else {
							listView.setVisibility(View.GONE);
							home_horizonal_tvEmpty.setVisibility(View.VISIBLE);
						}
					}

				}

				tv_home_time1_linear.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String leftString = tv_home_time1.getText().toString();
						String left = tv_home_time1.getText().toString();
						if (left != null && !left.equals("")) {
							for (int i = 0; i < dataBeans.size(); i++) {
								if (dataBeans.get(i).getFlag()
										.equals(StringToInt(left) - 1 + "")) {
									tv_home_time1.setText(StringToInt(left) - 1
											+ "");
									tv_home_time1_day.setText("日");
									break;
								} else {
									tv_home_time1.setText("");
									tv_home_time1_day.setText("");
								}
							}
							for (int i = 0; i < dataBeans.size(); i++) {
								if (dataBeans.get(i).getFlag()
										.equals(StringToInt(left) + 1 + "")) {
									tv_home_time2.setText(StringToInt(left) + 1
											+ "");
									tv_home_time2_day.setText("日");
									break;
								} else {
									tv_home_time2.setText("");
									tv_home_time2_day.setText("");
								}
							}
							for (int i = 0; i < dataBeans.size(); i++) {
								if (dataBeans.get(i).getFlag().equals(left)) {
									data.clear();
									data.addAll(dataBeans.get(i).getMeetting());
									adapter.notifyDataSetChanged();
								}
							}

						}
					}
				});

				tv_home_time2_linear.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String rightString = tv_home_time2.getText().toString();
						String right = tv_home_time2.getText().toString();

						if (right != null && !right.equals("")) {
							for (int i = 0; i < dataBeans.size(); i++) {
								if (dataBeans.get(i).getFlag()
										.equals(StringToInt(right) - 1 + "")) {
									tv_home_time1.setText(StringToInt(right)
											- 1 + "");
									tv_home_time1_day.setText("日");
									break;
								} else {
									tv_home_time1.setText("");
									tv_home_time1_day.setText("");
								}
							}
							for (int i = 0; i < dataBeans.size(); i++) {
								if (dataBeans.get(i).getFlag()
										.equals(StringToInt(right) + 1 + "")) {
									tv_home_time2.setText(StringToInt(right)
											+ 1 + "");
									tv_home_time2_day.setText("日");
									break;
								} else {
									tv_home_time2.setText("");
									tv_home_time2_day.setText("");
								}
							}
							for (int i = 0; i < dataBeans.size(); i++) {
								if (dataBeans.get(i).getFlag().equals(right)) {
									data.clear();
									data.addAll(dataBeans.get(i).getMeetting());
									adapter.notifyDataSetChanged();
								}
							}
						}
					}
				});

				break;
			case -1:

				break;
			case 300:

				break;
			case 400:

				break;
			case 500:

				break;

			default:
				break;

			}
		};
	};

	// 给大会日程中的正在进行的textView设置是否可见
	public void setVisibilityForTvNow(int visibility) {
		tv_title_now.setVisibility(visibility);
		tv_host_now.setVisibility(visibility);
		tv_chargeP_now.setVisibility(visibility);
		tv_connectP_now.setVisibility(visibility);
	}

	// 给大会日程中的下一节的textView设置是否可见
	public void setVisibilityForTvNext(int visibility) {
		tv_title_next.setVisibility(visibility);
		tv_host_next.setVisibility(visibility);
		tv_chargeP_next.setVisibility(visibility);
		tv_connectP_next.setVisibility(visibility);
	}

	/**
	 * 从网络请求数据
	 */
	private User user;
	private String userId = "";

	private void loadData() {

		try {
			// userId = Constant.decode(user.getUserId(), Constant.key);
			new HomeTask(handler.obtainMessage(), mContext, Constant.decode(
					Constant.key, user.getUserId().trim())).execute(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	// private Handler imgHandler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// switch (msg.what) {
	// case 1:
	// HomeImageBean bean = (HomeImageBean) msg.obj;
	// String url = bean.getInfo().get(0).getImgUrl();
	// System.out.println("===imgUrl===" + url);
	// // String imgurl =
	// // Constant.DOMAIN+"/upload/zhptImage/20160816010637862829.jpg";
	// ImageLoaderToView(url, home_iv_video);
	// break;
	// default:
	// break;
	// }
	// };
	// };

	/**
	 * 使用imageloder加载图片
	 * 
	 * @param imgUrl
	 * @param view
	 */
	public void ImageLoaderToView(String imgUrl, ImageView view) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		imageLoader.displayImage(imgUrl, view, options);
	}

	/**
	 * 从网络请求视频图片
	 */
	// public void loadImageView() {
	// try {
	// new HomeImageTask(imgHandler.obtainMessage(), mContext,Constant.decode(
	// Constant.key, user.getUserId().trim())).execute(1);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	/**
	 * 初始化webView的数据
	 * 
	 * @param pingMuSize
	 */
	public void initWebView(int pingMuSize) {
		// if (pingMuSize == 1) {
		// initHotelInfoData(urlHoteInfo1);
		// initConnector(urlConnecter1);
		// initWork(urlWorker1);
		// initVipLeave(urlVipLeave1);
		// } else if (pingMuSize == 0) {
		initHotelInfoData(urlHoteInfo);
		initConnector(urlConnecter);
		initWork(urlWorker);
		initVipLeave(urlVipLeave);
		// }
	}

	/**
	 * 嘉宾抵离 初始化数据
	 */
	private void initVipLeave(String url) {
		vipLeave.setHorizontalScrollBarEnabled(false);// 水平滚动条不显示
		vipLeave.setVerticalScrollBarEnabled(false); // 垂直滚动条不显示

		vipLeave.getSettings().setJavaScriptEnabled(true);// 开启webview对JS的支持
		vipLeave.getSettings().setSupportZoom(true);
		vipLeave.setBackgroundColor(0); // 设置背景色
		vipLeave.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
		vipLeave.getSettings().setUseWideViewPort(true);
		vipLeave.getSettings().setLoadWithOverviewMode(true);

		vipLeave.getSettings().setBuiltInZoomControls(true);
		vipLeave.getSettings().setDisplayZoomControls(false);

		vipLeave.getSettings().setAllowFileAccess(true);
		vipLeave.getSettings().setAllowFileAccess(true);
		vipLeave.getSettings().setAllowContentAccess(true);
		vipLeave.loadUrl(url);
	}

	/**
	 * work 初始化数据
	 */
	private void initWork(String url) {
		worker.setHorizontalScrollBarEnabled(false);// 水平滚动条不显示
		worker.setVerticalScrollBarEnabled(false); // 垂直滚动条不显示
		worker.setOverScrollMode(View.OVER_SCROLL_NEVER);
		worker.getSettings().setJavaScriptEnabled(true);// 开启webview对JS的支持
		worker.getSettings().setSupportZoom(true);
		worker.setBackgroundColor(0); // 设置背景色
		worker.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
		worker.getSettings().setUseWideViewPort(true);
		worker.getSettings().setLoadWithOverviewMode(true);

		worker.getSettings().setBuiltInZoomControls(true);
		worker.getSettings().setDisplayZoomControls(false);

		worker.getSettings().setAllowFileAccess(true);
		worker.getSettings().setAllowFileAccess(true);
		worker.getSettings().setAllowContentAccess(true);
		worker.loadUrl(url);
	}

	/**
	 * connecter 初始化数据
	 */
	private void initConnector(String url) {
		connecter.setHorizontalScrollBarEnabled(false);// 水平滚动条不显示
		connecter.setVerticalScrollBarEnabled(false); // 垂直滚动条不显示
		connecter.setOverScrollMode(View.OVER_SCROLL_NEVER);

		connecter.getSettings().setJavaScriptEnabled(true);// 开启webview对JS的支持
		connecter.getSettings().setSupportZoom(true);
		connecter.setBackgroundColor(0); // 设置背景色
		connecter.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
		connecter.getSettings().setUseWideViewPort(true);
		connecter.getSettings().setLoadWithOverviewMode(true);

		connecter.getSettings().setBuiltInZoomControls(true);
		connecter.getSettings().setDisplayZoomControls(false);

		connecter.getSettings().setAllowFileAccess(true);
		connecter.getSettings().setAllowFileAccess(true);
		connecter.getSettings().setAllowContentAccess(true);
		connecter.loadUrl(url);
	}

	/**
	 * hotelInfo 初始化数据
	 */
	private void initHotelInfoData(String url) {
		hotelInfo.setHorizontalScrollBarEnabled(false);// 水平滚动条不显示
		hotelInfo.setVerticalScrollBarEnabled(false); // 垂直滚动条不显示

		hotelInfo.getSettings().setJavaScriptEnabled(true);// 开启webview对JS的支持
		hotelInfo.getSettings().setSupportZoom(true);
		hotelInfo.setBackgroundColor(0); // 设置背景色
		hotelInfo.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
		hotelInfo.getSettings().setUseWideViewPort(true);
		hotelInfo.getSettings().setLoadWithOverviewMode(true);

		hotelInfo.getSettings().setBuiltInZoomControls(true);
		hotelInfo.getSettings().setDisplayZoomControls(false);

		hotelInfo.getSettings().setAllowFileAccess(true);
		hotelInfo.getSettings().setAllowFileAccess(true);
		hotelInfo.getSettings().setAllowContentAccess(true);
		hotelInfo.loadUrl(url);

	}

	/**
	 * 初始化view
	 */
	private void initView() {
		hotelInfo = (WebView) findViewById(R.id.wv_home_hotelInfo);
		connecter = (WebView) findViewById(R.id.wv_home_connecter);
		vipLeave = (WebView) findViewById(R.id.wv_home_vipLeave);
		worker = (WebView) findViewById(R.id.wv_home_worker);

		tv_title_now = (TextView) findViewById(R.id.tv_title_now);
		tv_host_now = (TextView) findViewById(R.id.tv_host_now);
		tv_chargeP_now = (TextView) findViewById(R.id.tv_chargePerson_now);
		tv_connectP_now = (TextView) findViewById(R.id.tv_connectPerson_now);

		tv_title_next = (TextView) findViewById(R.id.tv_title_next);
		tv_host_next = (TextView) findViewById(R.id.tv_host_next);
		tv_chargeP_next = (TextView) findViewById(R.id.tv_chargePerson_next);
		tv_connectP_next = (TextView) findViewById(R.id.tv_connectPerson_next);
		tv_home_time1 = (TextView) findViewById(R.id.tv_home_time1);
		tv_home_time2 = (TextView) findViewById(R.id.tv_home_time2);
		tv_home_time1_day = (TextView) findViewById(R.id.tv_home_time1_day);
		tv_home_time2_day = (TextView) findViewById(R.id.tv_home_time2_day);
		tv_home_time1_linear = (LinearLayout) findViewById(R.id.tv_home_time1_linear);
		tv_home_time2_linear = (LinearLayout) findViewById(R.id.tv_home_time2_linear);
		tv_empty_now = (TextView) findViewById(R.id.tv_empty_now);
		tv_empty_next = (TextView) findViewById(R.id.tv_empty_next);
		listView = (HorizontalListView) findViewById(R.id.horizontalListView);
		home_iv_video = (ImageView) findViewById(R.id.home_iv_video);

		ll_sche = (LinearLayout) findViewById(R.id.ll_homeLeftFrame);
		ll_vipLeave = (LinearLayout) findViewById(R.id.ll_homeRightFrame);
		re_video = (RelativeLayout) findViewById(R.id.re_stateForLeave);
		fra_connect = (FrameLayout) findViewById(R.id.frame_connect);
		fra_worker = (FrameLayout) findViewById(R.id.frame_worker);
		linaer_hotel = (RelativeLayout) findViewById(R.id.linaer_hotel);
		home_tv_currentTime = (TextView) findViewById(R.id.home_tv_currentTime);
		home_tv_currentTime.setText("当前时间:"
				+ Constant.getCurrentTimeToHm(System.currentTimeMillis()));
		home_horizonal_tvEmpty = (TextView) findViewById(R.id.home_horizonal_tvEmpty);

		iv_homeHead = (ImageView) findViewById(R.id.iv_homeHead);
		tv_homeTitle1 = (TextView) findViewById(R.id.tv_homeTitle1);
		tv_homeTitle = (TextView) findViewById(R.id.tv_homeTitle);
		// 遮挡层
		hotelInfo_view = findViewById(R.id.hotelInfo_view);
		connect_view = findViewById(R.id.connect_view);
		worker_view = findViewById(R.id.worker_view);
		vipLeave_view = findViewById(R.id.vipLeave_view);
		// 更多视频
		ll_video_homeMore = (LinearLayout) findViewById(R.id.ll_video_homeMore);
		// 左上
		// home_iv_videoMotion1 = (ImageView)
		// findViewById(R.id.home_iv_videoMotion1);// 切换手势和方向
		// home_video_motion1 = (LinearLayout)
		// findViewById(R.id.home_video_motion1);
		// home_video_motion1.setOnClickListener(touchListener1);
		framecontainer_1 = (ViewGroup) findViewById(R.id.framecontainer_1_big);
		framecontainer_1.setBackgroundColor(0xFF000000);
		home_video_qp1 = (LinearLayout) findViewById(R.id.home_video_qp1);
		home_video_qp1.setOnClickListener(this);
		// 左下
		// home_iv_videoMotion2 = (ImageView)
		// findViewById(R.id.home_iv_videoMotion2);
		// home_video_motion2 = (LinearLayout)
		// findViewById(R.id.home_video_motion2);
		// home_video_motion2.setOnClickListener(touchListener2);
		framecontainer_2 = (ViewGroup) findViewById(R.id.framecontainer_2_big);
		framecontainer_2.setBackgroundColor(0xFF000000);
		home_video_qp2 = (LinearLayout) findViewById(R.id.home_video_qp2);
		home_video_qp2.setOnClickListener(this);
		// 右上
		// home_iv_videoMotion3 = (ImageView)
		// findViewById(R.id.home_iv_videoMotion3);
		// home_video_motion3 = (LinearLayout)
		// findViewById(R.id.home_video_motion3);
		// home_video_motion3.setOnClickListener(touchListener3);
		framecontainer_3 = (ViewGroup) findViewById(R.id.framecontainer_3_big);
		framecontainer_3.setBackgroundColor(0xFF000000);
		home_video_qp3 = (LinearLayout) findViewById(R.id.home_video_qp3);
		home_video_qp3.setOnClickListener(this);
		// 右下
		// home_iv_videoMotion4 = (ImageView)
		// findViewById(R.id.home_iv_videoMotion4);
		// home_video_motion4 = (LinearLayout)
		// findViewById(R.id.home_video_motion4);
		// home_video_motion4.setOnClickListener(touchListener4);
		framecontainer_4 = (ViewGroup) findViewById(R.id.framecontainer_4_big);
		framecontainer_4.setBackgroundColor(0xFF000000);
		home_video_qp4 = (LinearLayout) findViewById(R.id.home_video_qp4);
		home_video_qp4.setOnClickListener(this);
		// 小时报
		home_xiaoshibao = (Button) findViewById(R.id.home_xiaoshibao);

		// rtmp流布局 开始
		// 左上
		mLoadingView1 = findViewById(R.id.LoadingView1_big);
		mSurfaceView1 = (SurfaceView) findViewById(R.id.SurfaceView1_big);
		// 左下
		mLoadingView2 = findViewById(R.id.LoadingView2_big);
		mSurfaceView2 = (SurfaceView) findViewById(R.id.SurfaceView2_big);
		// 右上
		mLoadingView3 = findViewById(R.id.LoadingView3_big);
		mSurfaceView3 = (SurfaceView) findViewById(R.id.SurfaceView3_big);
		// 右下
		mLoadingView4 = findViewById(R.id.LoadingView4_big);
		mSurfaceView4 = (SurfaceView) findViewById(R.id.SurfaceView4_big);
		// rtmp 流布局 结束

		ll_sche.setOnClickListener(this);
		// ll_vipLeave.setOnClickListener(this);
		// re_video.setOnClickListener(this);
		// fra_connect.setOnClickListener(this);
		// fra_worker.setOnClickListener(this);
		// linaer_hotel.setOnClickListener(this);
		hotelInfo_view.setOnClickListener(this);
		connect_view.setOnClickListener(this);
		worker_view.setOnClickListener(this);
		vipLeave_view.setOnClickListener(this);
		ll_video_homeMore.setOnClickListener(this);

		iv_homeHead.setOnClickListener(this);
		tv_homeTitle.setOnClickListener(this);
		tv_homeTitle1.setOnClickListener(this);
		home_xiaoshibao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, XsbBigActivity.class);
				startActivity(intent);
				// 以popupwindow的形式弹出列表
				// getPopWindow();
				// popupWindow.showAtLocation(iv_homeHead, Gravity.CENTER, 0,
				// 10);
				// popupWindow.showAsDropDown(iv_homeHead, 0, 50);
			}
		});

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.ll_homeLeftFrame:// 大会日程
			intent.setClass(mContext, MainActivity.class);
			intent.putExtra("tag", "meetingSchedule");
			break;
		case R.id.vipLeave_view:// 嘉宾抵离
			intent.setClass(mContext, MainActivity.class);
			intent.putExtra("tag", "daohuiStatue");
			break;
		case R.id.ll_video_homeMore:// 视频监控
			intent.setClass(mContext, MainActivity.class);
			intent.putExtra("tag", "video");
			break;
		case R.id.connect_view:// 联络员状态
			intent.setClass(mContext, MainActivity.class);
			intent.putExtra("tag", "meetingSchedule");
			break;
		case R.id.worker_view:// 车辆人员状态
			intent.setClass(mContext, MainActivity.class);
			intent.putExtra("tag", "carSource");
			break;
		case R.id.hotelInfo_view:// 酒店信息
			intent.setClass(mContext, MainActivity.class);
			intent.putExtra("tag", "hotelDistribution");
			break;
		case R.id.iv_homeHead:// 默认跳转到二级界面选中第一个大会日程
			intent.setClass(mContext, MainActivity.class);
			intent.putExtra("tag", "meetingSchedule");
			break;
		case R.id.tv_homeTitle1:// 默认跳转到二级界面选中第一个大会日程
			intent.setClass(mContext, MainActivity.class);
			intent.putExtra("tag", "meetingSchedule");
			break;
		case R.id.tv_homeTitle:// 默认跳转到二级界面选中第一个大会日程
			intent.setClass(mContext, MainActivity.class);
			intent.putExtra("tag", "meetingSchedule");
			break;
		case R.id.home_video_qp1:// 左上
			intent.setClass(mContext, FullScreenActivity.class);
			intent.putExtra("type", "HomeForBigActivity");
			intent.putExtra("videoPathType", "1");
			break;
		case R.id.home_video_qp2:// 左下
			intent.setClass(mContext, FullScreenActivity.class);
			intent.putExtra("type", "HomeForBigActivity");
			intent.putExtra("videoPathType", "2");
			break;
		case R.id.home_video_qp3:// 右上
			intent.setClass(mContext, FullScreenActivity.class);
			intent.putExtra("type", "HomeForBigActivity");
			intent.putExtra("videoPathType", "3");
			break;
		case R.id.home_video_qp4:// 右下
			intent.setClass(mContext, FullScreenActivity.class);
			intent.putExtra("type", "HomeForBigActivity");
			intent.putExtra("videoPathType", "4");
			break;
		default:
			break;
		}
		startActivity(intent);
		// finish();
	}

	private void getPopWindow() {
		if (null != popupWindow) {
			closePopupWindow();
			return;
		} else {
			initPopupWindow();
		}
	}

	/**
	 * 弹出popupWindow
	 */
	private void initPopupWindow() {
		View popView = LayoutInflater.from(mContext).inflate(
				R.layout.pop_xiaoshibao, null, false);

		popupWindow = new PopupWindow(popView, 900, 950, true);

		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		container_relative = (RelativeLayout) popView
				.findViewById(R.id.container_relative);
		xsb_colose = (TextView) popView.findViewById(R.id.xsb_colose);
		xsb_wv = (WebView) popView.findViewById(R.id.xsb_wv);
		xsb_wv.setHorizontalScrollBarEnabled(false);// 水平滚动条不显示
		xsb_wv.setVerticalScrollBarEnabled(false); // 垂直滚动条不显示
		xsb_wv.setOverScrollMode(View.OVER_SCROLL_NEVER);
		xsb_wv.getSettings().setJavaScriptEnabled(true);// 开启webview对JS的支持
		xsb_wv.getSettings().setSupportZoom(true);
		// xsb_wv.setBackgroundColor(0); // 设置背景色
		// xsb_wv.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
		xsb_wv.getSettings().setUseWideViewPort(true);
		xsb_wv.getSettings().setLoadWithOverviewMode(true);

		xsb_wv.getSettings().setBuiltInZoomControls(true);
		xsb_wv.getSettings().setDisplayZoomControls(false);

		xsb_wv.getSettings().setAllowFileAccess(true);
		xsb_wv.getSettings().setAllowFileAccess(true);
		xsb_wv.getSettings().setAllowContentAccess(true);
		xsb_wv.loadUrl("http://huaban.com/boards/15855162/");
		xsb_colose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePopupWindow();
			}
		});
	}

	/**
	 * 关闭popupWindow
	 */
	// 关闭popupWindow时，要在动画结束后再关闭popupWindow，在动画结束的监听事件里关闭popupWindow并将其置空
	public void closePopupWindow() {
		// 涉及到动画就要注意屏幕的坐标原点是在左上角
		// container_linear 为popupwindow的根布局
		popupWindow.dismiss();
	}

	/**
	 * String 转int
	 * 
	 * @param a
	 * @return
	 */
	public int StringToInt(String a) {
		int aint = 0;
		if (a != null && !a.equals("")) {
			aint = Integer.parseInt(a);
		}
		return aint;
	}

	// zyj添加
	public static int px2dip1(Context context, int pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	// 获取手机屏幕大小《0是普通屏，1是大屏》
	public static int getPingMuSize(Context con) {
		// 测量手机尺寸
		DisplayMetrics dm = new DisplayMetrics();
		dm = con.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int widpx = px2dip1(con, screenWidth);

		if (widpx > 950) {
			return 1;
		}
		return 0;
	}

	private void process() {
		try {
			// 获取程序版本号
			PackageManager manager = this.getPackageManager();// 程序包管理器
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			myversioncode = info.versionCode;// 当前系统版本号
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}// 获取PackageInfo对象就可以获取版本名称和版本号
	}

	/**
	 * 版本更新
	 */
	public Handler handler_version = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1000:
				NewApkVersionPojo pojo = (NewApkVersionPojo) msg.obj;
				if (pojo.versioncode > myversioncode) {// 有新版本
					showDialogDownloadNewApk(pojo.downloadurl);
				}
				break;

			default:
				break;
			}
		};
	};

	public void CheckForUpdate() {
		AsyncTaskForCheckApkVersion atapk = new AsyncTaskForCheckApkVersion(
				handler_version.obtainMessage(), this.getApplicationContext());
		atapk.execute(1);
	}

	// 如果发现新版本，调用本方法询问用户是否下载
	public void showDialogDownloadNewApk(final String downloadurl) {
		new AlertDialog.Builder(mContext)
				.setTitle("发现新版本!")
				.setMessage("现在下载吗?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 启动线程下载apk
						// 判断下载服务是否可用，如果未启用，启用
						int state = getPackageManager()
								.getApplicationEnabledSetting(
										"com.android.providers.downloads");

						if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
								|| state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
								|| state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
							System.out.println("下载服务未启用");
							String packageName = "com.android.providers.downloads";
							try {
								// Open the specific App Info page:
								Intent intent = new Intent(
										android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								intent.setData(Uri.parse("package:"
										+ packageName));
								startActivity(intent);
							} catch (ActivityNotFoundException e) {
								e.printStackTrace();
								// Open the generic Apps page:
								Intent intent = new Intent(
										android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
								startActivity(intent);
								Toast.makeText(getApplicationContext(), "下载失败",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							System.out.println("下载服务已经启用");
							AsyncTaskForDownloadTask at = new AsyncTaskForDownloadTask(
									getApplicationContext(), downloadurl);
							at.execute(1);
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}

	// rtmp 流视频播放
	public void initRtmp1() {
		mSurfaceView1.getHolder().addCallback(mCallback11);
		mAVOptions1 = new AVOptions();

		int isLiveStreaming = 1;
		// the unit of timeout is ms
		mAVOptions1.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
		mAVOptions1.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
		// Some optimization with buffering mechanism when be set to 1
		mAVOptions1.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming);
		if (isLiveStreaming == 1) {
			mAVOptions1.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
		}

		// 1 -> hw codec enable, 0 -> disable [recommended]
		int codec = 1;
		mAVOptions1.setInteger(AVOptions.KEY_MEDIACODEC, codec);

		// whether start play automatically after prepared, default value is 1
		mAVOptions1.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

		AudioManager audioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN);

	}

	public void initRtmp2() {
		mSurfaceView2.getHolder().addCallback(mCallback22);
		mAVOptions2 = new AVOptions();

		int isLiveStreaming = 1;
		// the unit of timeout is ms
		mAVOptions2.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
		mAVOptions2.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
		// Some optimization with buffering mechanism when be set to 1
		mAVOptions2.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming);
		if (isLiveStreaming == 1) {
			mAVOptions2.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
		}

		// 1 -> hw codec enable, 0 -> disable [recommended]
		int codec = 1;
		mAVOptions2.setInteger(AVOptions.KEY_MEDIACODEC, codec);

		// whether start play automatically after prepared, default value is 1
		mAVOptions2.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

		AudioManager audioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN);

	}

	public void initRtmp3() {
		mSurfaceView3.getHolder().addCallback(mCallback33);
		mAVOptions3 = new AVOptions();

		int isLiveStreaming = 1;
		// the unit of timeout is ms
		mAVOptions3.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
		mAVOptions3.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
		// Some optimization with buffering mechanism when be set to 1
		mAVOptions3.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming);
		if (isLiveStreaming == 1) {
			mAVOptions3.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
		}

		// 1 -> hw codec enable, 0 -> disable [recommended]
		int codec = 1;
		mAVOptions3.setInteger(AVOptions.KEY_MEDIACODEC, codec);

		// whether start play automatically after prepared, default value is 1
		mAVOptions3.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

		AudioManager audioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN);

	}

	public void initRtmp4() {
		mSurfaceView4.getHolder().addCallback(mCallback44);
		mAVOptions4 = new AVOptions();

		int isLiveStreaming = 1;
		// the unit of timeout is ms
		mAVOptions4.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
		mAVOptions4.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
		// Some optimization with buffering mechanism when be set to 1
		mAVOptions4.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming);
		if (isLiveStreaming == 1) {
			mAVOptions4.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
		}

		// 1 -> hw codec enable, 0 -> disable [recommended]
		int codec = 1;
		mAVOptions4.setInteger(AVOptions.KEY_MEDIACODEC, codec);

		// whether start play automatically after prepared, default value is 1
		mAVOptions4.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

		AudioManager audioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN);

	}

	private SurfaceHolder.Callback mCallback11 = new SurfaceHolder.Callback() {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			prepare1();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			mSurfaceWidth1 = width;
			mSurfaceHeight1 = height;
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// release();
			releaseWithoutStop1();
		}
	};
	private SurfaceHolder.Callback mCallback22 = new SurfaceHolder.Callback() {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			prepare2();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			mSurfaceWidth2 = width;
			mSurfaceHeight2 = height;
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// release();
			releaseWithoutStop2();
		}
	};
	private SurfaceHolder.Callback mCallback33 = new SurfaceHolder.Callback() {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			prepare3();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			mSurfaceWidth3 = width;
			mSurfaceHeight3 = height;
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// release();
			releaseWithoutStop3();
		}
	};
	private SurfaceHolder.Callback mCallback44 = new SurfaceHolder.Callback() {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			prepare4();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			mSurfaceWidth4 = width;
			mSurfaceHeight4 = height;
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// release();
			releaseWithoutStop4();
		}
	};

	private void prepare1() {

		if (mMediaPlayer1 != null) {
			mMediaPlayer1.setDisplay(mSurfaceView1.getHolder());
			return;
		}

		try {
			mMediaPlayer1 = new PLMediaPlayer(mAVOptions1);
			mMediaPlayer1.setVolume(0.0f, 0.0f);
			mMediaPlayer1.setOnPreparedListener(mOnPreparedListener1);
			mMediaPlayer1
					.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener1);
			mMediaPlayer1.setOnCompletionListener(mOnCompletionListener);
			mMediaPlayer1.setOnErrorListener(mOnErrorListener);
			mMediaPlayer1.setOnInfoListener(mOnInfoListener1);
			mMediaPlayer1
					.setOnBufferingUpdateListener(mOnBufferingUpdateListener);

			// set replay if completed
			// mMediaPlayer.setLooping(true);

			mMediaPlayer1.setWakeMode(mContext.getApplicationContext(),
					PowerManager.PARTIAL_WAKE_LOCK);

			mMediaPlayer1.setDataSource(mVideoPath1);
			mMediaPlayer1.setDisplay(mSurfaceView1.getHolder());
			mMediaPlayer1.prepareAsync();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void prepare2() {

		if (mMediaPlayer2 != null) {
			mMediaPlayer2.setDisplay(mSurfaceView2.getHolder());
			return;
		}

		try {
			mMediaPlayer2 = new PLMediaPlayer(mAVOptions2);
			mMediaPlayer2.setVolume(0.0f, 0.0f);
			mMediaPlayer2.setOnPreparedListener(mOnPreparedListener2);
			mMediaPlayer2
					.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener2);
			mMediaPlayer2.setOnCompletionListener(mOnCompletionListener);
			mMediaPlayer2.setOnErrorListener(mOnErrorListener);
			mMediaPlayer2.setOnInfoListener(mOnInfoListener2);
			mMediaPlayer2
					.setOnBufferingUpdateListener(mOnBufferingUpdateListener);

			// set replay if completed
			// mMediaPlayer.setLooping(true);

			mMediaPlayer2.setWakeMode(mContext.getApplicationContext(),
					PowerManager.PARTIAL_WAKE_LOCK);

			mMediaPlayer2.setDataSource(mVideoPath2);
			mMediaPlayer2.setDisplay(mSurfaceView2.getHolder());
			mMediaPlayer2.prepareAsync();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void prepare3() {

		if (mMediaPlayer3 != null) {
			mMediaPlayer3.setDisplay(mSurfaceView3.getHolder());
			return;
		}

		try {
			mMediaPlayer3 = new PLMediaPlayer(mAVOptions3);
			mMediaPlayer3.setVolume(0.0f, 0.0f);
			mMediaPlayer3.setOnPreparedListener(mOnPreparedListener3);
			mMediaPlayer3
					.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener3);
			mMediaPlayer3.setOnCompletionListener(mOnCompletionListener);
			mMediaPlayer3.setOnErrorListener(mOnErrorListener);
			mMediaPlayer3.setOnInfoListener(mOnInfoListener3);
			mMediaPlayer3
					.setOnBufferingUpdateListener(mOnBufferingUpdateListener);

			// set replay if completed
			// mMediaPlayer.setLooping(true);

			mMediaPlayer3.setWakeMode(mContext.getApplicationContext(),
					PowerManager.PARTIAL_WAKE_LOCK);

			mMediaPlayer3.setDataSource(mVideoPath3);
			mMediaPlayer3.setDisplay(mSurfaceView3.getHolder());
			mMediaPlayer3.prepareAsync();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void prepare4() {

		if (mMediaPlayer4 != null) {
			mMediaPlayer4.setDisplay(mSurfaceView4.getHolder());
			return;
		}

		try {
			mMediaPlayer4 = new PLMediaPlayer(mAVOptions4);
			mMediaPlayer4.setVolume(0.0f, 0.0f);
			mMediaPlayer4.setOnPreparedListener(mOnPreparedListener4);
			mMediaPlayer4
					.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener4);
			mMediaPlayer4.setOnCompletionListener(mOnCompletionListener);
			mMediaPlayer4.setOnErrorListener(mOnErrorListener);
			mMediaPlayer4.setOnInfoListener(mOnInfoListener4);
			mMediaPlayer4
					.setOnBufferingUpdateListener(mOnBufferingUpdateListener);

			// set replay if completed
			// mMediaPlayer.setLooping(true);

			mMediaPlayer4.setWakeMode(mContext.getApplicationContext(),
					PowerManager.PARTIAL_WAKE_LOCK);

			mMediaPlayer4.setDataSource(mVideoPath4);
			mMediaPlayer4.setDisplay(mSurfaceView4.getHolder());
			mMediaPlayer4.prepareAsync();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void releaseWithoutStop1() {
		if (mMediaPlayer1 != null) {
			mMediaPlayer1.setDisplay(null);
		}
	}

	public void releaseWithoutStop2() {
		if (mMediaPlayer2 != null) {
			mMediaPlayer2.setDisplay(null);
		}
	}

	public void releaseWithoutStop3() {
		if (mMediaPlayer3 != null) {
			mMediaPlayer3.setDisplay(null);
		}
	}

	public void releaseWithoutStop4() {
		if (mMediaPlayer4 != null) {
			mMediaPlayer4.setDisplay(null);
		}
	}

	private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener1 = new PLMediaPlayer.OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(PLMediaPlayer mp, int width, int height) {
			Log.i("tag", "onVideoSizeChanged, width = " + width + ",height = "
					+ height);
			// resize the display window to fit the screen
			if (width != 0 && height != 0) {
				float ratioW = (float) width / (float) mSurfaceWidth1;
				float ratioH = (float) height / (float) mSurfaceHeight1;
				float ratio = Math.max(ratioW, ratioH);
				width = (int) Math.ceil((float) width / ratio);
				height = (int) Math.ceil((float) height / ratio);
				FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
						width, height);
				layout.gravity = Gravity.CENTER;
				mSurfaceView1.setLayoutParams(layout);
			}
		}
	};
	private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener2 = new PLMediaPlayer.OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(PLMediaPlayer mp, int width, int height) {
			Log.i("tag", "onVideoSizeChanged, width = " + width + ",height = "
					+ height);
			// resize the display window to fit the screen
			if (width != 0 && height != 0) {
				float ratioW = (float) width / (float) mSurfaceWidth2;
				float ratioH = (float) height / (float) mSurfaceHeight2;
				float ratio = Math.max(ratioW, ratioH);
				width = (int) Math.ceil((float) width / ratio);
				height = (int) Math.ceil((float) height / ratio);
				FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
						width, height);
				layout.gravity = Gravity.CENTER;
				mSurfaceView2.setLayoutParams(layout);
			}
		}
	};
	private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener3 = new PLMediaPlayer.OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(PLMediaPlayer mp, int width, int height) {
			Log.i("tag", "onVideoSizeChanged, width = " + width + ",height = "
					+ height);
			// resize the display window to fit the screen
			if (width != 0 && height != 0) {
				float ratioW = (float) width / (float) mSurfaceWidth3;
				float ratioH = (float) height / (float) mSurfaceHeight3;
				float ratio = Math.max(ratioW, ratioH);
				width = (int) Math.ceil((float) width / ratio);
				height = (int) Math.ceil((float) height / ratio);
				FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
						width, height);
				layout.gravity = Gravity.CENTER;
				mSurfaceView3.setLayoutParams(layout);
			}
		}
	};
	private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener4 = new PLMediaPlayer.OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(PLMediaPlayer mp, int width, int height) {
			Log.i("tag", "onVideoSizeChanged, width = " + width + ",height = "
					+ height);
			// resize the display window to fit the screen
			if (width != 0 && height != 0) {
				float ratioW = (float) width / (float) mSurfaceWidth4;
				float ratioH = (float) height / (float) mSurfaceHeight4;
				float ratio = Math.max(ratioW, ratioH);
				width = (int) Math.ceil((float) width / ratio);
				height = (int) Math.ceil((float) height / ratio);
				FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
						width, height);
				layout.gravity = Gravity.CENTER;
				mSurfaceView4.setLayoutParams(layout);
			}
		}
	};
	private PLMediaPlayer.OnPreparedListener mOnPreparedListener1 = new PLMediaPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(PLMediaPlayer mp) {
			Log.i("tag", "On Prepared !");
			mMediaPlayer1.start();
			mIsStopped1 = false;
		}
	};
	private PLMediaPlayer.OnPreparedListener mOnPreparedListener2 = new PLMediaPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(PLMediaPlayer mp) {
			Log.i("tag", "On Prepared !");
			mMediaPlayer2.start();
			mIsStopped2 = false;
		}
	};
	private PLMediaPlayer.OnPreparedListener mOnPreparedListener3 = new PLMediaPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(PLMediaPlayer mp) {
			Log.i("tag", "On Prepared !");
			mMediaPlayer3.start();
			mIsStopped3 = false;
		}
	};
	private PLMediaPlayer.OnPreparedListener mOnPreparedListener4 = new PLMediaPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(PLMediaPlayer mp) {
			Log.i("tag", "On Prepared !");
			mMediaPlayer4.start();
			mIsStopped4 = false;
		}
	};

	private PLMediaPlayer.OnInfoListener mOnInfoListener1 = new PLMediaPlayer.OnInfoListener() {
		@Override
		public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
			Log.i("tag", "OnInfo, what = " + what + ", extra = " + extra);
			switch (what) {
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
				mLoadingView1.setVisibility(View.VISIBLE);
				break;
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
			case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
				mLoadingView1.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			return true;
		}
	};
	private PLMediaPlayer.OnInfoListener mOnInfoListener2 = new PLMediaPlayer.OnInfoListener() {
		@Override
		public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
			Log.i("tag", "OnInfo, what = " + what + ", extra = " + extra);
			switch (what) {
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
				mLoadingView2.setVisibility(View.VISIBLE);
				break;
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
			case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
				mLoadingView2.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			return true;
		}
	};
	private PLMediaPlayer.OnInfoListener mOnInfoListener3 = new PLMediaPlayer.OnInfoListener() {
		@Override
		public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
			Log.i("tag", "OnInfo, what = " + what + ", extra = " + extra);
			switch (what) {
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
				mLoadingView3.setVisibility(View.VISIBLE);
				break;
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
			case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
				mLoadingView3.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			return true;
		}
	};
	private PLMediaPlayer.OnInfoListener mOnInfoListener4 = new PLMediaPlayer.OnInfoListener() {
		@Override
		public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
			Log.i("tag", "OnInfo, what = " + what + ", extra = " + extra);
			switch (what) {
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
				mLoadingView4.setVisibility(View.VISIBLE);
				break;
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
			case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
				mLoadingView4.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			return true;
		}
	};
	private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
		@Override
		public void onBufferingUpdate(PLMediaPlayer mp, int percent) {
			Log.d("tag", "onBufferingUpdate: " + percent + "%");
		}
	};

	/**
	 * Listen the event of playing complete For playing local file, it's called
	 * when reading the file EOF For playing network stream, it's called when
	 * the buffered bytes played over
	 * 
	 * If setLooping(true) is called, the player will restart automatically And
	 * ｀onCompletion｀ will not be called
	 * 
	 */
	private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
		@Override
		public void onCompletion(PLMediaPlayer mp) {
			Log.d("tag", "Play Completed !");
			// showToastTips("Play Completed !");
			// ((Activity) mContext).finish();
		}
	};

	private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
		@Override
		public boolean onError(PLMediaPlayer mp, int errorCode) {
			Log.e("tag", "Error happened, errorCode = " + errorCode);
			switch (errorCode) {
			case PLMediaPlayer.ERROR_CODE_INVALID_URI:
				// showToastTips("Invalid URL !");
				break;
			case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
				// showToastTips("404 resource not found !");
				break;
			case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
				// showToastTips("Connection refused !");
				break;
			case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
				// showToastTips("Connection timeout !");
				break;
			case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
				// showToastTips("Empty playlist !");
				break;
			case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
				// showToastTips("Stream disconnected !");
				break;
			case PLMediaPlayer.ERROR_CODE_IO_ERROR:
				// showToastTips("Network IO Error !");
				break;
			case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
				// showToastTips("Unauthorized Error !");
				break;
			case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
				// showToastTips("Prepare timeout !");
				break;
			case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
				// showToastTips("Read frame timeout !");
				break;
			case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
			default:
				// showToastTips("unknown error !");
				break;
			}
			// Todo pls handle the error status here, retry or call finish()
			// The PLMediaPlayer has moved to the Error state, if you want to
			// retry, must reset first !
			// try {
			// mMediaPlayer.reset();
			// mMediaPlayer.setDisplay(mSurfaceView.getHolder());
			// mMediaPlayer.setDataSource(mVideoPath);
			// mMediaPlayer.prepareAsync();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// Return true means the error has been handled
			// If return false, then `onCompletion` will be called
			return true;
		}
	};

	private void showToastTips(final String tips, boolean mIsActivityPaused) {
		if (mIsActivityPaused) {
			return;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mContext, tips, Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mIsActivityPaused1 = false;
		mIsActivityPaused2 = false;
		mIsActivityPaused3 = false;
		mIsActivityPaused4 = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		mIsActivityPaused1 = true;
		mIsActivityPaused2 = true;
		mIsActivityPaused3 = true;
		mIsActivityPaused4 = true;
	}

	public void release(PLMediaPlayer mMediaPlayer) {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	// 视频---》多屏播放
	// PFView _pfview1;
	// PFView _pfview2;
	// PFView _pfview3;
	// PFView _pfview4;
	// PFAsset _pfasset1;
	// PFAsset _pfasset2;
	// PFAsset _pfasset3;
	// PFAsset _pfasset4;
	// PFNavigationMode _currentNavigationMode1 = PFNavigationMode.MOTION;
	// PFNavigationMode _currentNavigationMode2 = PFNavigationMode.MOTION;
	// PFNavigationMode _currentNavigationMode3 = PFNavigationMode.MOTION;
	// PFNavigationMode _currentNavigationMode4 = PFNavigationMode.MOTION;
	// String videoPath1 =
	// "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
	// String videoPath2 =
	// "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
	// String videoPath3 =
	// "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
	// String videoPath4 =
	// "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
	// boolean _updateThumb = true;;
	// Timer _scrubberMonitorTimer;

	// @Override
	// protected void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// initPF1(videoPath1);
	// initPF2(videoPath2);
	// initPF3(videoPath3);
	// initPF4(videoPath4);
	// // loadVideo("http://114.112.96.184:8000/conf1/fullview.m3u8");
	// loadVideo(framecontainer_1, _pfview1, _pfasset1,
	// _currentNavigationMode1, videoPath1);
	// loadVideo(framecontainer_2, _pfview2, _pfasset2,
	// _currentNavigationMode2, videoPath2);
	// loadVideo(framecontainer_3, _pfview3, _pfasset3,
	// _currentNavigationMode3, videoPath3);
	// loadVideo(framecontainer_4, _pfview4, _pfasset4,
	// _currentNavigationMode4, videoPath4);
	// showControls(home_iv_videoMotion1, _pfview1, true);
	// showControls(home_iv_videoMotion1, _pfview2, true);
	// showControls(home_iv_videoMotion2, _pfview3, true);
	// showControls(home_iv_videoMotion3, _pfview4, true);
	// _pfasset1.play();
	// _pfasset2.play();
	// _pfasset3.play();
	// _pfasset4.play();
	// }

	// public void initPF1(String filename) {
	// _pfview1 = PFObjectFactory.view(this);
	// _pfasset1 = PFObjectFactory.assetFromUri(this, Uri.parse(filename),
	// this);
	// }
	//
	// public void initPF2(String filename) {
	// _pfview2 = PFObjectFactory.view(this);
	// _pfasset2 = PFObjectFactory.assetFromUri(this, Uri.parse(filename),
	// this);
	// }
	//
	// public void initPF3(String filename) {
	// _pfview3 = PFObjectFactory.view(this);
	// _pfasset3 = PFObjectFactory.assetFromUri(this, Uri.parse(filename),
	// this);
	// }
	//
	// public void initPF4(String filename) {
	// _pfview4 = PFObjectFactory.view(this);
	// _pfasset4 = PFObjectFactory.assetFromUri(this, Uri.parse(filename),
	// this);
	// }

	/**
	 * Show/Hide the playback controls
	 * 
	 * @param bShow
	 *            Show or hide the controls. Pass either true or false.
	 */
	// public void showControls(ImageView _touchButton, PFView pfView,
	// boolean bShow) {
	// int visibility = View.GONE;
	//
	// if (bShow)
	// visibility = View.VISIBLE;
	//
	// // _stopButton.setVisibility(visibility);
	// _touchButton.setVisibility(visibility);
	// // _scrubber.setVisibility(visibility);
	//
	// if (pfView != null) {
	// if (!pfView.supportsNavigationMode(PFNavigationMode.MOTION))
	// _touchButton.setVisibility(View.VISIBLE);
	// }
	// }

	/**
	 * Start the video with a local file path
	 * 
	 * @param filename
	 *            The file path on device storage
	 */
	// public void loadVideo(ViewGroup viewGroup, PFView _pfview,
	// PFAsset _pfasset, PFNavigationMode _currentNavigationMode,
	// String filename) {
	//
	// // _pfview = PFObjectFactory.view(this);
	// // _pfasset = PFObjectFactory.assetFromUri(this, Uri.parse(filename),
	// // this);
	//
	// _pfview.displayAsset(_pfasset);
	// _pfview.setNavigationMode(_currentNavigationMode);
	//
	// viewGroup.addView(_pfview.getView(), 0);
	//
	// }

	/**
	 * Status callback from the PFAsset instance. Based on the status this
	 * function selects the appropriate action.
	 * 
	 * @param asset
	 *            The asset who is calling the function
	 * @param status
	 *            The current status of the asset.
	 */
	// public void onStatusMessage(final PFAsset asset, PFAssetStatus status) {
	// switch (status) {
	// case LOADED:
	// Log.d("SimplePlayer", "Loaded");
	// break;
	// case DOWNLOADING:
	// // Log.d("SimplePlayer",
	// //
	// "Downloading 360� movie: "+_pfasset.getDownloadProgress()+" percent complete");
	// break;
	// case DOWNLOADED:
	// Log.d("SimplePlayer", "Downloaded to " + asset.getUrl());
	// break;
	// case DOWNLOADCANCELLED:
	// Log.d("SimplePlayer", "Download cancelled");
	// break;
	// case PLAYING:
	// Log.d("SimplePlayer", "Playing");
	// getWindow()
	// .addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	// // _scrubber.setEnabled(true);
	// _scrubberMonitorTimer = new Timer();
	// final TimerTask task = new TimerTask() {
	// public void run() {
	// if (_updateThumb) {
	// // _scrubber.setMax((int) asset.getDuration());
	// // _scrubber.setProgress((int) asset.getPlaybackTime());
	// }
	// }
	// };
	// _scrubberMonitorTimer.schedule(task, 0, 33);
	// break;
	// case PAUSED:
	// Log.d("SimplePlayer", "Paused");
	// break;
	// /*
	// * case STOPPED: Log.d("SimplePlayer", "Stopped");
	// * _playButton.setText("play"); _scrubberMonitorTimer.cancel();
	// * _scrubberMonitorTimer = null; _scrubber.setProgress(0);
	// * _scrubber.setEnabled(false);
	// * getWindow().clearFlags(WindowManager.LayoutParams
	// * .FLAG_KEEP_SCREEN_ON); break;
	// */
	// case COMPLETE:
	// Log.d("SimplePlayer", "Complete");
	// _scrubberMonitorTimer.cancel();
	// _scrubberMonitorTimer = null;
	// getWindow().clearFlags(
	// WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	// break;
	// case ERROR:
	// Log.d("SimplePlayer", "Error");
	// break;
	// }
	// }

	/**
	 * Click listener for the play/pause button
	 * 
	 */
	// private OnClickListener playListener = new OnClickListener() {
	// public void onClick(View v) {
	// if (_pfasset.getStatus() == PFAssetStatus.PLAYING)
	// {
	// _pfasset.pause();
	// }
	// else
	// _pfasset.play();
	// }
	// };

	/**
	 * Click listener for the stop/back button
	 * 
	 */
	// private OnClickListener stopListener = new OnClickListener() {
	//
	// public void onClick(View v) {
	// _pfasset.stop();
	// }
	// };

	/**
	 * Click listener for the navigation mode (touch/motion (if available))
	 * 
	 */
	// private OnClickListener touchListener1 = new OnClickListener() {
	// public void onClick(View v) {
	// if (_pfview1 != null) {
	// ImageView home_iv_videoMotion_1 = (ImageView)
	// findViewById(R.id.home_iv_videoMotion1);
	// if (_currentNavigationMode1 == PFNavigationMode.TOUCH) {
	// _currentNavigationMode1 = PFNavigationMode.MOTION;
	// // touchButton.setText("motion");
	// home_iv_videoMotion_1
	// .setImageResource(R.drawable.big_home_btn_fxgy_nor);
	// } else {
	// _currentNavigationMode1 = PFNavigationMode.TOUCH;
	// // touchButton.setText("touch");
	// home_iv_videoMotion_1
	// .setImageResource(R.drawable.big_home_btn_ssck_nor);
	// }
	// _pfview1.setNavigationMode(_currentNavigationMode1);
	// }
	// }
	// };
	// private OnClickListener touchListener2 = new OnClickListener() {
	// public void onClick(View v) {
	// if (_pfview2 != null) {
	// ImageView home_iv_videoMotion_2 = (ImageView)
	// findViewById(R.id.home_iv_videoMotion2);
	// if (_currentNavigationMode2 == PFNavigationMode.TOUCH) {
	// _currentNavigationMode2 = PFNavigationMode.MOTION;
	// // touchButton.setText("motion");
	// home_iv_videoMotion_2
	// .setImageResource(R.drawable.big_home_btn_fxgy_nor);
	// } else {
	// _currentNavigationMode2 = PFNavigationMode.TOUCH;
	// // touchButton.setText("touch");
	// home_iv_videoMotion_2
	// .setImageResource(R.drawable.big_home_btn_ssck_nor);
	// }
	// _pfview2.setNavigationMode(_currentNavigationMode2);
	// }
	// }
	// };
	//
	// private OnClickListener touchListener3 = new OnClickListener() {
	// public void onClick(View v) {
	// if (_pfview3 != null) {
	// ImageView home_iv_videoMotion_3 = (ImageView)
	// findViewById(R.id.home_iv_videoMotion3);
	// if (_currentNavigationMode3 == PFNavigationMode.TOUCH) {
	// _currentNavigationMode3 = PFNavigationMode.MOTION;
	// // touchButton.setText("motion");
	// home_iv_videoMotion_3
	// .setImageResource(R.drawable.big_home_btn_fxgy_nor);
	// } else {
	// _currentNavigationMode3 = PFNavigationMode.TOUCH;
	// // touchButton.setText("touch");
	// home_iv_videoMotion_3
	// .setImageResource(R.drawable.big_home_btn_ssck_nor);
	// }
	// _pfview3.setNavigationMode(_currentNavigationMode3);
	// }
	// }
	// };
	//
	// private OnClickListener touchListener4 = new OnClickListener() {
	// public void onClick(View v) {
	// if (_pfview4 != null) {
	// ImageView home_iv_videoMotion_4 = (ImageView)
	// findViewById(R.id.home_iv_videoMotion4);
	// if (_currentNavigationMode4 == PFNavigationMode.TOUCH) {
	// _currentNavigationMode4 = PFNavigationMode.MOTION;
	// // touchButton.setText("motion");
	// home_iv_videoMotion_4
	// .setImageResource(R.drawable.big_home_btn_fxgy_nor);
	// } else {
	// _currentNavigationMode4 = PFNavigationMode.TOUCH;
	// // touchButton.setText("touch");
	// home_iv_videoMotion_4
	// .setImageResource(R.drawable.big_home_btn_ssck_nor);
	// }
	// _pfview4.setNavigationMode(_currentNavigationMode4);
	// }
	// }
	// };

	// /**
	// * Setup the options menu
	// *
	// * @param menu The options menu
	// */
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.activity_main, menu);
	// return true;
	// }

	/**
	 * Called when pausing the app. This function pauses the playback of the
	 * asset when it is playing.
	 * 
	 */
	// public void onPause() {
	// super.onPause();
	// if (_pfasset != null)
	// {
	// if (_pfasset.getStatus() == PFAssetStatus.PLAYING)
	// _pfasset.pause();
	// }
	// }

	/**
	 * Called when a previously created loader is being reset, and thus making
	 * its data unavailable.
	 * 
	 * @param seekbar
	 *            The SeekBar whose progress has changed
	 * @param progress
	 *            The current progress level.
	 * @param fromUser
	 *            True if the progress change was initiated by the user.
	 * 
	 */
	// public void onProgressChanged(SeekBar seekbar, int progress,
	// boolean fromUser) {
	// }

	/**
	 * Notification that the user has started a touch gesture. In this function
	 * we signal the timer not to update the playback thumb while we are
	 * adjusting it.
	 * 
	 * @param seekbar
	 *            The SeekBar in which the touch gesture began
	 * 
	 */
	// public void onStartTrackingTouch(SeekBar seekbar) {
	// _updateThumb = false;
	// }

	/**
	 * Notification that the user has finished a touch gesture. In this function
	 * we request the asset to seek until a specific time and signal the timer
	 * to resume the update of the playback thumb based on playback.
	 * 
	 * @param seekbar
	 *            The SeekBar in which the touch gesture began
	 * 
	 */
	// public void onStopTrackingTouch(SeekBar seekbar) {
	// _updateThumb = true;
	// }
	// protected void onStop() {
	// // TODO Auto-generated method stub
	// super.onStop();
	// _pfasset1.stop();
	// _pfasset2.stop();
	// _pfasset3.stop();
	// _pfasset4.stop();
	// }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeCallbacks(task);
		release(mMediaPlayer1);
		release(mMediaPlayer2);
		release(mMediaPlayer3);
		release(mMediaPlayer4);
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		audioManager.abandonAudioFocus(null);
	}

	@Override
	public void onBackPressed() {
		if (!isback) {
			isback = true;
			Toast.makeText(this, "再一次点击退出", 0).show();
			// 添加计时器，2秒后点击对出按钮，再次提示
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					isback = false;
				}
			}, 2000);
		} else {
			// 此处一定要把父类的点击回退键回调的方法写到else中
			super.onBackPressed();
		}
	}
}
