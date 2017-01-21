package com.smartdot.wenbo.controlcenter.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.bean.NewApkVersionPojo;
import com.smartdot.wenbo.controlcenter.fragment.HomeCarSourceFragment;
import com.smartdot.wenbo.controlcenter.fragment.HomeHotelLocationsFragment;
import com.smartdot.wenbo.controlcenter.fragment.HomeMeetingStateFragment;
import com.smartdot.wenbo.controlcenter.fragment.HomeScheduleFragment;
import com.smartdot.wenbo.controlcenter.fragment.HomeVideoFragment;
import com.smartdot.wenbo.controlcenter.task.AsyncTaskForCheckApkVersion;
import com.smartdot.wenbo.controlcenter.task.AsyncTaskForDownloadTask;

public class HomeActivity extends FragmentActivity implements OnClickListener {

	private LinearLayout home_meetingSchedule;
	private LinearLayout home_daohuiStatue;
	private LinearLayout home_hotelDistribution;
	private LinearLayout home_video;
	private LinearLayout home_carSource;

	private Context mContext;
	@SuppressWarnings("unused")
	private HomeScheduleFragment meetingScheduleHomeFragment;
	private HomeMeetingStateFragment daoHuiStatusHomeFragment;
	private HomeHotelLocationsFragment hotelDistributionHomeFragment;
	private HomeVideoFragment videoHomeFragment;
	private HomeCarSourceFragment carSourceHomeFragment;
	int myversioncode = 1;// 当前系统版本号
	// 下载新版本apk 广播接收
	DownloadReceiver downreceiver = new DownloadReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		mContext = this;
		initView();
		process();
		CheckForUpdate();
	}

	private void initView() {

		home_meetingSchedule = (LinearLayout) findViewById(R.id.home_meetingSchedule);
		home_daohuiStatue = (LinearLayout) findViewById(R.id.home_daohuiStatue);
		home_hotelDistribution = (LinearLayout) findViewById(R.id.home_hotelDistribution);
		home_video = (LinearLayout) findViewById(R.id.home_video);
		home_carSource = (LinearLayout) findViewById(R.id.home_carSource);

		home_meetingSchedule.setOnClickListener(this);
		home_daohuiStatue.setOnClickListener(this);
		home_hotelDistribution.setOnClickListener(this);
		home_video.setOnClickListener(this);
		home_carSource.setOnClickListener(this);

	}

	private void process() {
		try {
			// 获取程序版本号
			PackageManager manager = this.getPackageManager();// 程序包管理器
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			myversioncode = info.versionCode;// 当前系统版本号
			//
			// 下载完成广播监听
			IntentFilter downloadcompleteIF = new IntentFilter();
			downloadcompleteIF
					.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
			this.registerReceiver(downreceiver, downloadcompleteIF);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}// 获取PackageInfo对象就可以获取版本名称和版本号
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(mContext, MainActivity.class);
		switch (v.getId()) {
		case R.id.home_meetingSchedule:
			intent.putExtra("tag", "meetingSchedule");
			break;
		case R.id.home_daohuiStatue:
			intent.putExtra("tag", "daohuiStatue");
			break;
		case R.id.home_hotelDistribution:
			intent.putExtra("tag", "hotelDistribution");
			break;
		case R.id.home_video:
			intent.putExtra("tag", "video");
			break;
		case R.id.home_carSource:
			intent.putExtra("tag", "carSource");
			break;
		default:
			break;
		}
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(downreceiver);
		super.onDestroy();
	}

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

	// 把文件的下载完成 监听全部放到 主页上 广播接收系统所有下载完毕的消息，使用query+downloadid进行过滤
	public class DownloadReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 获取本次下载完毕广播的id号
			long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

			String messionsids = HomeActivity.this.getSharedPreferences("WIC",
					Activity.MODE_PRIVATE).getString("downloadmessions", "");
			if (messionsids == null) {
				messionsids = "";
			}
			String[] messionids = messionsids.split(",");

			long[] messions = new long[messionids.length];

			for (int i = 0; i < messionids.length; i++) {
				if (!messionids[i].equals("")) {
					messions[i] = Long.parseLong(messionids[i]);
				}
			}
			for (int i = 0; i < messions.length; i++) {
				if (messions[i] == id) {
					// 创建过滤对象
					Query q = new Query();
					// 设置过滤方式by id
					q.setFilterById(id);
					DownloadManager dm = (DownloadManager) HomeActivity.this
							.getSystemService(Context.DOWNLOAD_SERVICE);
					// 下载事件数据安卓系统全部放在系统sqlite中，使用Query对象做过滤，然后dm.query()查询出相对应的下载事件相关信息；返回cursor
					Cursor c = dm.query(q);
					if (c.moveToNext()) {// 下载任务必定产生一条相关的任务数据，但是下载成功与否，需要做判断
						String status = c.getString(c.getColumnIndex("status"));// 这里的status如果用getString获取，值是200
																				// 字符串类型，如果用getInt获取，值是8
																				// ，int类型，耐人寻味...
						if (status.equals("200")) {// 下载任务必定产生一条相关的任务数据，但是下载成功与否，需要做判断
													// status字段存放下载完成结果，200成功404找不到目标URL
													// 比如：Tomcat目录下的apk文件被勿删，或者文件名称错误，或者文件夹名称错误，都会引起下面代码的空指针异常
							String filepath = c.getString(c
									.getColumnIndex("local_uri"));// 下载数据有很多字段，可以循环打印出来根据需要做处理
							Uri fileuri = Uri.parse(filepath);
							// zyj添加
							String fpath = fileuri.getPath();
							String filename = fpath.substring(fpath
									.lastIndexOf("/") + 1);
							if (filename.contains(".apk")) {
								HomeActivity.this.openAppFile(new File(fileuri
										.getPath()));
								c.close();
							} else {
								Toast.makeText(HomeActivity.this, "下载完成",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(HomeActivity.this, "下载失败",
									Toast.LENGTH_SHORT).show();
							c.close();
							return;
						}
					}// 下载数据存在
					else {
						c.close();
					}// 下载数据不存在
				}// 匹配成功，自动打开下载完成的应用
			}// 循环完毕
		}
	}

	// 在手机上打开文件
	private void openAppFile(File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		// 设置目标文件的打开方式
		String type = "application/vnd.android.package-archive";
		// 设置intent的目标文件的uri与打开方式MimeType
		intent.setDataAndType(Uri.fromFile(f), type);// 核心方法 设置Intent的数据对象和操作类型
		// 启动intent 打开文件
		this.startActivity(intent);
	}
}
