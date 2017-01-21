package com.smartdot.wenbo.controlcenter.activity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.application.BaseApplication;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;
import com.smartdot.wenbo.controlcenter.bean.DengLuYanZhi;
import com.smartdot.wenbo.controlcenter.bean.User;
import com.smartdot.wenbo.controlcenter.bean.YongHuMingDengLu;
import com.smartdot.wenbo.controlcenter.rong.RongCloudEvent;
import com.smartdot.wenbo.controlcenter.task.NetLoginTask;
import com.smartdot.wenbo.controlcenter.task.zAsyncTaskForRongGlobalGroup;
import com.smartdot.wenbo.controlcenter.task.zAsyncTaskForRongGlobalUser;
import com.smartdot.wenbo.controlcenter.util.Base64;
import com.smartdot.wenbo.controlcenter.util.EncodEncryUtil;
import com.smartdot.wenbo.controlcenter.util.MSharePreferenceUtils;

public class LoginActivity extends Activity implements OnClickListener {

	Context context;
	//
	TextView login_tv;
	EditText login_username;
	EditText edit_shurumima;
	//
	// 登陆等待对话框
	ProgressDialog pro;
	User parent;
	String musername;
	DbUtils db;
	// 获取盐值访问接口返回的handler
	Handler handyz = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				DengLuYanZhi yanzhi = (DengLuYanZhi) msg.obj;
				String sal = new String(Base64.decode(yanzhi.salt));

				String username = login_username.getText().toString().trim();
				String password = edit_shurumima.getText().toString().trim();
				// System.out.println(sal+"=-=="+password+"==="+EncodEncryUtil.Salt(password,
				// sal));
				ClientParams client = new ClientParams();
				client.url = "/hylogin.do";
				StringBuffer strbuf = new StringBuffer();
				// des加密后
				strbuf.append("method=passwordLogin&userid=");
				try {
					strbuf.append(URLEncoder.encode(
							Constant.encode(Constant.key, username), "UTF-8"));
					strbuf.append("&password=");
					// System.out.println();
					strbuf.append(URLEncoder.encode(
							Constant.encode(Constant.key,
									EncodEncryUtil.Salt(password, sal)),
							"UTF-8"));
					strbuf.append("&lg=");
					strbuf.append(URLEncoder.encode(
							Constant.encode(Constant.key, "1"), "UTF-8"));
					strbuf.append("&phonetype=");
					strbuf.append(URLEncoder.encode(
							Constant.encode(Constant.key, "android"), "UTF-8"));
					strbuf.append("&type=");
					strbuf.append(URLEncoder.encode(
							Constant.encode(Constant.key, "1"), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				String str = strbuf.toString();
				client.params = str;
				NetLoginTask<YongHuMingDengLu> net = new NetLoginTask<YongHuMingDengLu>(
						hand.obtainMessage(), client, YongHuMingDengLu.class,
						new YongHuMingDengLu(), context);
				net.execute();
				// 指挥平台的新加的
				// getToken(login_username.getText().toString(), "2");
			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				Toast.makeText(context, "网络不给力", Toast.LENGTH_SHORT).show();
				if (pro != null)
					pro.dismiss();
			} else if (msg.what == 2) { // 表示服务器出现问题，返回的CODE不正确
				// Toast.makeText(context, (String) msg.obj, Toast.LENGTH_SHORT)
				// .show();
				if (pro != null)
					pro.dismiss();
			} else {
				if (pro != null)
					pro.dismiss();
			}
		}
	};
	YongHuMingDengLu denglu;
	// 输入用户名密码登陆访问接口返回的handler
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				denglu = (YongHuMingDengLu) msg.obj;
				User user = new User(); // 这里需要注意的是User对象必须有id属性，或者有通过@ID注解的属性
				user.setUserjuese(denglu.userjuese);// 不需要加密 数值
				user.setPassword(denglu.password);// 不需要加密，已经是MD5加密过的
				user.setName(Constant.encode(Constant.key, denglu.name));
				user.setImportant(denglu.important);
				user.setToastMessage(denglu.toastMessage);
				System.out.println("====登录===name：" + denglu.name
						+ ",===important:" + denglu.important);
				if (denglu.zhiwei != null)
					user.setZhiWei(Constant.encode(Constant.key, denglu.zhiwei));
				if (denglu.smallphotourl != null)
					user.setSmallPhotoUrl(Constant.encode(Constant.key,
							denglu.smallphotourl));
				if ("".equals(denglu.vipid)) {// 防止当前用户不是vip，造成加密异常，手动将vip字段就是0
					denglu.vipid = "0";
				}
				if (denglu.userjuese.equals("1")) {// 登陆角色为VIP是手动将vipid内容填写为userid
					user.setVipid(Constant.encode(Constant.key, denglu.userid));
				} else {
					// user.setVipid(Constant.encode(Constant.key,
					// denglu.vipid));
				}
				user.setWorkplace(Constant.encode(Constant.key,
						denglu.workplace));
				user.setRy_userId(denglu.ry_userId);
				user.setRy_token(denglu.ry_token);
				user.setUserId(Constant.encode(Constant.key, denglu.userid));
				// zyj 保存到本地共享参数里面
				MSharePreferenceUtils.putBean(context, Constant.sp_user, user);
				// 使用saveBindingId保存实体时会为实体的id赋值
				connect(denglu.ry_token);
				// 获取融云信息
				zAsyncTaskForRongGlobalUser user_at = new zAsyncTaskForRongGlobalUser(
						ronguser_handler.obtainMessage(), context,
						denglu.userid, db);
				// at.execute(1);
				user_at.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
				zAsyncTaskForRongGlobalGroup group_at = new zAsyncTaskForRongGlobalGroup(
						ronggroup_handler.obtainMessage(), context,
						denglu.userid, db);
				group_at.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
				//
				// zyj 用户名-密码验证成功之后进入主页//lixiaoming pad-大屏
				Intent intent = new Intent(LoginActivity.this,
						HomeNewActivity.class);
				// Intent intent = new Intent(LoginActivity.this,
				// HomeForBigActivity.class);
				startActivity(intent);
				finish();
			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				if (pro != null)
					pro.dismiss();
				Toast.makeText(context, "网络不给力", Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) { // 表示服务器出现问题，返回的CODE不正确
				if (pro != null)
					pro.dismiss();
				Toast.makeText(context, "用户名或密码错误", Toast.LENGTH_SHORT).show();
			}
			// pro.dismiss();
		}
	};

	// zyj融云用户信息的handler
	Handler ronguser_handler = new Handler() {
		public void handleMessage(Message msg) {
			if (pro != null)
				pro.dismiss();
			switch (msg.what) {
			case 0:// 获取融云user信息
				String message = (String) msg.obj;
				// at.execute(1);
				break;
			case 1:
				// Toast.makeText(mContext, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				// Toast.makeText(mContext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				break;

			default:
				break;
			}
		};
	};
	// zyj融云群组信息的handler
	Handler ronggroup_handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 获取融云user信息
				String message = (String) msg.obj;

				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
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
		setContentView(R.layout.activity_login);
		context = this;
		MSharePreferenceUtils.getAppConfig(this);
		parent = (User) MSharePreferenceUtils
				.getBean(context, Constant.sp_user);
		db = DbUtils.create(context);
		if (parent != null) {
			try {
				// 获取融云信息
				zAsyncTaskForRongGlobalUser user_at = new zAsyncTaskForRongGlobalUser(
						ronguser_handler.obtainMessage(),
						context,
						Constant.decode(Constant.key, parent.getUserId().trim()),
						db);
				// at.execute(1);
				user_at.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
				zAsyncTaskForRongGlobalGroup group_at = new zAsyncTaskForRongGlobalGroup(
						ronggroup_handler.obtainMessage(),
						context,
						Constant.decode(Constant.key, parent.getUserId().trim()),
						db);
				group_at.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
				// lixiaoming pad-大屏
				Intent intent = new Intent(LoginActivity.this,
						HomeNewActivity.class);
				// Intent intent = new Intent(LoginActivity.this,
				// HomeForBigActivity.class);
				startActivity(intent);
				connect(parent.getRy_token());
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		initView();
	}

	private void initView() {
		login_tv = (TextView) findViewById(R.id.login_tv);
		login_username = (EditText) findViewById(R.id.login_username);
		edit_shurumima = (EditText) findViewById(R.id.edit_shurumima);
		login_tv.setOnClickListener(this);
	}

	private void getSalt(String username) {
		ClientParams client = new ClientParams();
		client.url = "/saltaction.do";
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=getSalt&userid=");
		try {
			strbuf.append(URLEncoder.encode(
					new String(Base64.encode(username.getBytes())), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String str = strbuf.toString();
		client.params = str;
		NetLoginTask<DengLuYanZhi> net = new NetLoginTask<DengLuYanZhi>(
				handyz.obtainMessage(), client, DengLuYanZhi.class,
				new DengLuYanZhi(), context);
		net.execute();
	}

	// 融云 - zyj 获取token
	// private void getToken(String userid, String type) {
	// ClientParams client = new ClientParams(); // 创建一个新的Http请求
	// client.url = "/vipmembers.do?"; // Http 请求的地址 前面的域名封装好了
	// StringBuffer strbuf = new StringBuffer();
	// strbuf.append("method=getUserToken&userid=");
	// strbuf.append(userid);
	// strbuf.append("&type=");
	// strbuf.append(type);
	//
	// String str = strbuf.toString();
	// client.params = str;
	// System.out.println("aaa:DengLuActivity:getToken:" + client.toString());
	//
	// NetTask<RongInfo> net = new NetTask<RongInfo>(ronghand.obtainMessage(),
	// client, RongInfo.class, new RongInfo(), context);
	// net.execute();
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_tv:
			String username = login_username.getText().toString().trim();
			String password = edit_shurumima.getText().toString().trim();
			//2017加开始
			// zyj 用户名-密码验证成功之后进入主页//lixiaoming pad-大屏
			Intent intent = new Intent(LoginActivity.this,
					HomeNewActivity.class);
			// Intent intent = new Intent(LoginActivity.this,
			// HomeForBigActivity.class);
			startActivity(intent);
			finish();
			//2017加结束
			if (!username.equals("") && !password.equals("")) {
				pro = ProgressDialog.show(this, "", "加载中...", true, true);
				// pro.setCancelable(true);// 点击dialog外空白位置是否消失
				// pro.setCanceledOnTouchOutside(true);// 点击返回键对话框是否消失
				getSalt(login_username.getText().toString().trim());
			} else if (username.equals("")) {
				Toast.makeText(context, "请输入用户名", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			break;
		}
	}

	// 融云 - 建立与融云服务器的连接
	private void connect(String token) {

		if (getApplicationInfo().packageName.equals(BaseApplication
				.getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第二步,建立与服务器的连接
			 */
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				/**
				 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的
				 */
				@Override
				public void onTokenIncorrect() {
					// Toast.makeText(context, "token错误,连接融云失败,请重新登录",
					// Toast.LENGTH_SHORT).show();
				}

				// 连接融云成功
				@Override
				public void onSuccess(String userid) {
					// Toast.makeText(context, "融云连接成功!", Toast.LENGTH_SHORT)
					// .show();

					RongCloudEvent.getInstance().setOtherListener();
					RongIM.getInstance().enableNewComingMessageIcon(true);// 显示新消息提醒
					RongIM.getInstance().enableUnreadMessageIcon(true);// 显示未读消息数目
					// 设置当前用户信息
					if (RongIM.getInstance() != null) {
						RongIM.getInstance().setCurrentUserInfo(
								new UserInfo(userid, musername, null));
						// 设置消息体内是否携带用户信息
						RongIM.getInstance().setMessageAttachedUserInfo(true);
					}
				}

				// 连接融云失败

				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {
					// Toast.makeText(context, "连接融云失败,请重新登录",
					// Toast.LENGTH_SHORT)
					// .show();
				}
			});
		}
	}

}
