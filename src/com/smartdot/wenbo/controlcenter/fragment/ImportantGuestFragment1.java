package com.smartdot.wenbo.controlcenter.fragment;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.adapter.ImportGuestAdapter;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;
import com.smartdot.wenbo.controlcenter.bean.ImportGuest;
import com.smartdot.wenbo.controlcenter.bean.RongConversitionInfo;
import com.smartdot.wenbo.controlcenter.task.NetTask;
import com.smartdot.wenbo.controlcenter.task.ResponseBody;
import com.smartdot.wenbo.controlcenter.util.IsWebCanBeUse;

import de.greenrobot.event.EventBus;

public class ImportantGuestFragment1 extends Fragment {

	private Context mContext;
	ProgressDialog pd;
	private TextView tv_title;
	TextView conversition_title;
	ImageView conversition_backiv;
	ListView guest_lv;
	LinearLayout import_guest_right_ll;
	LinearLayout import_guest_conversition;
	ArrayList<ImportGuest> guest_list = new ArrayList<ImportGuest>();
	ImportGuestAdapter guest_adapter;
	//
	ViewPager guest_vp;
	RadioGroup important_rg;
	TextView nodatafound;
	ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	ImporGuestFmAdapter imporguest_adapter;

	@Override
	public void onAttach(Context context) {
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
				R.layout.fragment_important_guest, null);
		initView(view);
		LoadData();
		setAllClick();
		return view;
	}

	private void initView(View view) {
		conversition_backiv = (ImageView) view
				.findViewById(R.id.conversition_backiv);
		import_guest_right_ll = (LinearLayout) view
				.findViewById(R.id.import_guest_right_ll);
		import_guest_conversition = (LinearLayout) view
				.findViewById(R.id.import_guest_conversition);
		conversition_title = (TextView) view
				.findViewById(R.id.conversition_title);
		important_rg = (RadioGroup) view.findViewById(R.id.important_vg);
		guest_lv = (ListView) view.findViewById(R.id.guest_lv);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		nodatafound = (TextView) view.findViewById(R.id.nodatafound);
		tv_title.setText("重要嘉宾");
		guest_adapter = new ImportGuestAdapter(mContext, guest_list);
		guest_lv.setAdapter(guest_adapter);
		//
		fragments.add(new ImportantGuestInfoFragment());
		fragments.add(new ImportGuestSchduleFragment());
		fragments.add(new ImportantGuestWaitersFragment());
		guest_vp = (ViewPager) view.findViewById(R.id.guest_vp);
		imporguest_adapter = new ImporGuestFmAdapter(
				((FragmentActivity) mContext).getSupportFragmentManager());
		guest_vp.setAdapter(imporguest_adapter);
		guest_vp.setOffscreenPageLimit(3);
	}

	public void LoadData() {
//		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
//			Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
//			return;
//		}
		pd = ProgressDialog.show(mContext, "", "Loading...");
		pd.setCancelable(true);
		pd.setCanceledOnTouchOutside(false);
		// 下面是网络请求
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/vipmembers.do?"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=allVipList&userid=wangsy");
		strbuf.append("&lg=1");

		String str = strbuf.toString();
		client.params = str;

		Type type = new TypeToken<ArrayList<ImportGuest>>() {
		}.getType();
		NetTask<ImportGuest> net = new NetTask<ImportGuest>(
				guest_handler.obtainMessage(), client, type, mContext);
		net.execute();
	}

	public void onEventMainThread(RongConversitionInfo ronginfo) {
		if (ronginfo.mConversationType != null && ronginfo.mTargetId != null
				&& ronginfo.talkname != null) {
			import_guest_right_ll.setVisibility(View.GONE);
			import_guest_conversition.setVisibility(View.VISIBLE);
			conversition_title.setText(ronginfo.talkname);
			enterFragment(ronginfo.mConversationType, ronginfo.mTargetId);
		}
	}

	private void enterFragment(Conversation.ConversationType mConversationType,
			String mTargetId) {
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		ConversationFragment fragment = new ConversationFragment();

		Uri uri = Uri
				.parse("rong://" + mContext.getApplicationInfo().packageName)
				.buildUpon().appendPath("conversation")
				.appendPath(mConversationType.getName().toLowerCase())
				.appendQueryParameter("targetId", mTargetId).build();

		fragment.setUri(uri);

		FragmentTransaction transaction = ((FragmentActivity) mContext)
				.getSupportFragmentManager().beginTransaction();
		// xxx 为你要加载的 id
		transaction.add(R.id.import_guest_rongy_frame, fragment);
		transaction.commit();
	}

	@SuppressWarnings("deprecation")
	private void setAllClick() {
		guest_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				RadioButton rb = (RadioButton) important_rg
						.getChildAt(position);
				rb.setChecked(true);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		important_rg
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.radiobutton_richeng:
							guest_vp.setCurrentItem(0);
							break;
						case R.id.radiobutton_xingcheng:
							guest_vp.setCurrentItem(1);
							break;
						case R.id.radiobutton_fwrenyuan:
							guest_vp.setCurrentItem(2);
							break;
						}
					}
				});
		guest_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				import_guest_conversition.setVisibility(View.GONE);
				import_guest_right_ll.setVisibility(View.VISIBLE);
				EventBus.getDefault().post(guest_list.get(position));
			}
		});
		conversition_backiv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				import_guest_conversition.setVisibility(View.GONE);
				import_guest_right_ll.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	class ImporGuestFmAdapter extends FragmentPagerAdapter {

		public ImporGuestFmAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}

	@SuppressWarnings("unchecked")
	Handler guest_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (pd != null) {
				pd.dismiss();
			}
			switch (msg.what) {
			case 0:// 获取重要嘉宾的列表返回数据
				ResponseBody<ImportGuest> res = (ResponseBody<ImportGuest>) msg.obj; // 首先创建接收方法
				guest_list.clear();
				guest_list.addAll(res.list);

				nodatafound.setVisibility(View.GONE);
				guest_lv.setVisibility(View.VISIBLE);
				guest_adapter.notifyDataSetChanged();
				// 默认进来显示的内容
				guest_lv.setSelection(0);
				guest_lv.setItemChecked(0, true);
				EventBus.getDefault().post(guest_list.get(0));
				break;
			case 1:
				// Toast.makeText(mContext, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				nodatafound.setVisibility(View.VISIBLE);
				// searchresultrv.setVisibility(View.GONE);
				Toast.makeText(mContext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				// Toast.makeText(mContext, "cookie失效，请求超时！",
				// Toast.LENGTH_SHORT)
				// .show();
				break;
			default:
				break;
			}
		};
	};
}
