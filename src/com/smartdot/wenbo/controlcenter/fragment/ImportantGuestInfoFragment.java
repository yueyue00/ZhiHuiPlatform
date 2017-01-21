package com.smartdot.wenbo.controlcenter.fragment;

import io.rong.imlib.model.Conversation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.adapter.ImportGuestInfoAdapter;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;
import com.smartdot.wenbo.controlcenter.bean.ContactBean;
import com.smartdot.wenbo.controlcenter.bean.ImportGuest;
import com.smartdot.wenbo.controlcenter.bean.RongConversitionInfo;
import com.smartdot.wenbo.controlcenter.bean.VipBaseInfoDynamic;
import com.smartdot.wenbo.controlcenter.bean.VipInfo;
import com.smartdot.wenbo.controlcenter.customview.CustomGridView;
import com.smartdot.wenbo.controlcenter.task.NetTask;
import com.smartdot.wenbo.controlcenter.task.zAsyncTaskVipInfoTask;
import com.smartdot.wenbo.controlcenter.util.CircleBitmapDisplayer;
import com.smartdot.wenbo.controlcenter.util.IsWebCanBeUse;

import de.greenrobot.event.EventBus;

//重要嘉宾信息界面
public class ImportantGuestInfoFragment extends Fragment {

	private Context mContext;
	ContactBean contactBean;
	// VipInfo vipinfo;
	ImportGuestInfoAdapter guestinfo_adapter;

	public ImportantGuestInfoFragment() {

	}

	@Override
	public void onAttach(Context context) {
		// TODO Auto-generated method stub
		super.onAttach(context);
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
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.fragment_important_guest_info, null);
		ViewUtils.inject(this, view);
		initView(view);
		setAllClick();
		return view;
	}

	private void initView(View view) {
		guest_info_top_layout.getBackground().setAlpha(150);
	}

	private void LoadData() {
//		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
//			Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
//			return;
//		}
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/vipmembers.do?"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=vipInfo&vipid=");
		strbuf.append(contactBean.id);
		// strbuf.append("&lg=1");
		String str = strbuf.toString();
		client.params = str;

		NetTask<VipInfo> net = new NetTask<VipInfo>(
				vipinfo_handler.obtainMessage(), client, VipInfo.class,
				new VipInfo(), mContext);
		net.execute();
	}

	private void LoadNewData() {
//		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
//			Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
//			return;
//		}
		zAsyncTaskVipInfoTask task = new zAsyncTaskVipInfoTask(
				new_handler.obtainMessage(), contactBean.id, mContext);
		task.execute();
	}

	private void setAllClick() {

	}

	public void onEventMainThread(ContactBean contactBean) {
		this.contactBean = contactBean;
		if (contactBean != null) {
			// LoadData();
			LoadNewData();
		}
	}

	private List<String> kvInfoNewList;// 左侧数据
	private List<String> jbxxInfoNewList;// 右侧数据

	private void getDataNew(VipBaseInfoDynamic vipBaseInfoDynamic) {
		kvInfoNewList = new ArrayList<String>();
		jbxxInfoNewList = new ArrayList<String>();
		kvInfoNewList.clear();
		jbxxInfoNewList.clear();

		Map<String, Object> kvInfoMap = vipBaseInfoDynamic.getKvInfoMap();
		Map<String, Object> jbxxInfoMap = vipBaseInfoDynamic.getJbxxInfoMap();
		for (Map.Entry<String, Object> entry : jbxxInfoMap.entrySet()) {
			System.out.println("key= " + entry.getKey() + " and value= "
					+ entry.getValue());
			if (entry.getValue() != null && !entry.getValue().equals("")) {// 右侧的值不为空
				jbxxInfoNewList.add((String) entry.getValue());
				kvInfoNewList.add((String) kvInfoMap.get(entry.getKey()));

			} else {
				System.out
						.println("=======VipBaseInfoFragment==entry.getKey()="
								+ entry.getKey() + ",entry.getValue()="
								+ entry.getValue());
			}
		}
	}

	private void setDataIntoListviewNew(
			final VipBaseInfoDynamic vipBaseInfoDynamic) {

		if (!TextUtils.isEmpty(vipBaseInfoDynamic.getJbjbInfoBean().getName())) {
			guset_name.setText(vipBaseInfoDynamic.getJbjbInfoBean().getName());
		} else {
			guset_name.setText("- -");
		}
		if (!TextUtils
				.isEmpty(vipBaseInfoDynamic.getJbjbInfoBean().getMobile())) {
			guset_mobile.setText(vipBaseInfoDynamic.getJbjbInfoBean()
					.getMobile());
		} else {
			guset_mobile.setText("- -");
		}
		guestinfo_adapter = new ImportGuestInfoAdapter(mContext, kvInfoNewList,
				jbxxInfoNewList);
		vip_info_gv.setAdapter(guestinfo_adapter);
		vip_info_gv.setNumColumns(2);
		//
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.a_huiyiricheng_guesticon)
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new CircleBitmapDisplayer()).build();
		imageLoader.displayImage(vipBaseInfoDynamic.getJbjbInfoBean()
				.getPhotourl(), guest_icon, options);
		linear_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (vipBaseInfoDynamic.getJbjbInfoBean().getMobile() != null) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri
							.parse("tel:"
									+ vipBaseInfoDynamic.getJbjbInfoBean()
											.getMobile()));
					mContext.startActivity(intent);
				} else {
					Toast.makeText(mContext, "所拨电话号码为空", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		linear_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (vipBaseInfoDynamic.getJbjbInfoBean().getRy_userId() != null
						&& vipBaseInfoDynamic.getJbjbInfoBean().getName() != null)
					EventBus.getDefault().post(
							new RongConversitionInfo(
									Conversation.ConversationType.PRIVATE,
									vipBaseInfoDynamic.getJbjbInfoBean()
											.getRy_userId(), vipBaseInfoDynamic
											.getJbjbInfoBean().getName()));
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@ViewInject(R.id.vip_info_gv)
	CustomGridView vip_info_gv;
	//
	@ViewInject(R.id.guset_name)
	TextView guset_name;
	@ViewInject(R.id.guset_place)
	TextView guset_place;// 籍贯
	@ViewInject(R.id.guset_mobile)
	TextView guset_mobile;
	@ViewInject(R.id.guest_icon)
	ImageView guest_icon;
	// @ViewInject(R.id.vip_workplace)
	// TextView vip_workplace;// 单位
	// @ViewInject(R.id.vip_job)
	// TextView vip_job;// 职务
	// @ViewInject(R.id.vip_group)
	// TextView vip_group;// 随行人员
	// @ViewInject(R.id.vip_carinfo)
	// TextView vip_carinfo;
	// @ViewInject(R.id.vip_mingzu)
	// TextView vip_mingzu;// 名族
	// @ViewInject(R.id.vip_country)
	// TextView vip_country;// 国籍
	// @ViewInject(R.id.vip_hotel)
	// TextView vip_hotel;
	// @ViewInject(R.id.vip_schdule)
	// TextView vip_schdule;
	@ViewInject(R.id.linear_phone)
	LinearLayout linear_phone;
	@ViewInject(R.id.linear_message)
	LinearLayout linear_message;
	//
	@ViewInject(R.id.guest_info_top_layout)
	RelativeLayout guest_info_top_layout;

	private Handler new_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				VipBaseInfoDynamic vipBaseInfoDynamic = (VipBaseInfoDynamic) msg.obj;

				if (vipBaseInfoDynamic != null) {
					getDataNew(vipBaseInfoDynamic);
					setDataIntoListviewNew(vipBaseInfoDynamic);
				}

				break;
			case -1:
				Toast.makeText(mContext, "请求数据有误", Toast.LENGTH_LONG).show();
				break;
			case -2:
				Toast.makeText(mContext, "请求数据为空", Toast.LENGTH_LONG).show();
				break;
			case 300:
				// Toast.makeText(mContext, "数据为空 code = 300",
				// Toast.LENGTH_LONG).show();
				break;
			case 500:
				// Toast.makeText(mContext, "请求超时误 code = 500",
				// Toast.LENGTH_LONG).show();
				break;
			case 400:
				// Toast.makeText(mContext, "请求失败 code = 400",
				// Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}
		}
	};
	Handler vipinfo_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
					// vipinfo = (VipInfo) msg.obj;
					// if (vipinfo.username != null)
					// guset_name.setText(vipinfo.username);
					// if (vipinfo.guset_place != null)
					// guset_place.setText(vipinfo.guset_place);
					// if (vipinfo.mobile != null)
					// guset_mobile.setText(vipinfo.mobile);

				// if (vipinfo.workplace != null)
				// vip_workplace.setText(vipinfo.workplace);
				// if (vipinfo.job != null)
				// vip_job.setText(vipinfo.job);
				// // if (vipinfo.vip_group != null)
				// // vip_group.setText(vipinfo.job);
				// if (vipinfo.carInfo != null)
				// vip_carinfo.setText(vipinfo.carInfo);
				// if (vipinfo.ethnic != null)
				// vip_mingzu.setText(vipinfo.ethnic);
				// if (vipinfo.country != null)
				// vip_country.setText(vipinfo.country);
				// if (vipinfo.hotelName != null)
				// vip_hotel.setText(vipinfo.hotelName);
				// if (vipinfo.vip_schdule != null)
				// vip_schdule.setText(vipinfo.vip_schdule);

				break;
			case 1:
				String message = (String) msg.obj;
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(mContext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(mContext, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		};
	};
}
