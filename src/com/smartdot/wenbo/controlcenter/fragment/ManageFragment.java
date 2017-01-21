package com.smartdot.wenbo.controlcenter.fragment;

import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.ConversationListFragment;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.adapter.ManageGroupRyAdapter;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;
import com.smartdot.wenbo.controlcenter.bean.ManageGroupoHeadWaiter;
import com.smartdot.wenbo.controlcenter.rong.ConversationListAdapterEx;
import com.smartdot.wenbo.controlcenter.task.NetTask;
import com.smartdot.wenbo.controlcenter.task.ResponseBody;
import com.smartdot.wenbo.controlcenter.util.IsWebCanBeUse;

import de.greenrobot.event.EventBus;

//服务调度界面
public class ManageFragment extends Fragment {

	private TextView tv_title;
	TextView nodatafound;
	private Context mContext;
	RadioGroup manage_rg;
	ProgressDialog pd;
	// 群组列表数据
	ListView group_lv;
	ManageGroupRyAdapter group_adapter;
	ArrayList<ManageGroupoHeadWaiter> group_list = new ArrayList<ManageGroupoHeadWaiter>();
	// 人员列表数据
	ListView renyuan_lv;
	ManageGroupRyAdapter renyuan_adapter;
	ArrayList<ManageGroupoHeadWaiter> renyuan_list = new ArrayList<ManageGroupoHeadWaiter>();
	//
	ViewPager manage_history_vp;
	ArrayList<Fragment> mfragments = new ArrayList<Fragment>();
	ManageHistoryAdapter historyAdapter;

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
				R.layout.fragment_manage, null);
		initView(view);
		LoadData();
		LoadHuiWuData();
		setAllClick();
		return view;
	}

	private void initView(View view) {
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("服务调度");
		manage_rg = (RadioGroup) view.findViewById(R.id.manage_rg);
		nodatafound = (TextView) view.findViewById(R.id.nodatafound);
		// 群组列表
		group_lv = (ListView) view.findViewById(R.id.manage_group_lv);
		group_adapter = new ManageGroupRyAdapter(mContext, group_list);
		group_lv.setAdapter(group_adapter);
		// 人员列表
		renyuan_lv = (ListView) view.findViewById(R.id.manage_renyuan_lv);
		renyuan_adapter = new ManageGroupRyAdapter(mContext, renyuan_list);
		renyuan_lv.setAdapter(renyuan_adapter);
		// zyj 融云最近聊天记录列表
		manage_history_vp = (ViewPager) view
				.findViewById(R.id.manage_history_vp);
		mfragments.add(initHistoryFragment());
		historyAdapter = new ManageHistoryAdapter(
				((FragmentActivity) mContext).getSupportFragmentManager(),
				mfragments);
		manage_history_vp.setAdapter(historyAdapter);
	}

	public Fragment initHistoryFragment() {
		ConversationListFragment listFragment = ConversationListFragment
				.getInstance();
		listFragment.setAdapter(new ConversationListAdapterEx(RongContext
				.getInstance()));
		Uri uri = Uri
				.parse("rong://" + mContext.getApplicationInfo().packageName)
				.buildUpon()
				.appendPath("conversationlist")
				.appendQueryParameter(
						Conversation.ConversationType.PRIVATE.getName(),
						"false") // 设置私聊会话是否聚合显示
				.appendQueryParameter(
						Conversation.ConversationType.GROUP.getName(), "false")// 群组
				.appendQueryParameter(
						Conversation.ConversationType.DISCUSSION.getName(),
						"false")
				// 讨论组
				.appendQueryParameter(
						Conversation.ConversationType.PUBLIC_SERVICE.getName(),
						"false")
				// 公共服务号
				.appendQueryParameter(
						Conversation.ConversationType.APP_PUBLIC_SERVICE
								.getName(),
						"false")// 订阅号
				.appendQueryParameter(
						Conversation.ConversationType.SYSTEM.getName(), "false")// 系统
				.build();
		listFragment.setUri(uri);
		return listFragment;
	}
	public void LoadData() {
//		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
//			Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		pd = ProgressDialog.show(mContext, "", "Loading...");
//		pd.setCancelable(true);
//		pd.setCanceledOnTouchOutside(false);
		// 下面是网络请求
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/vipmembers.do?"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=getRongYunGroup");

		String str = strbuf.toString();
		client.params = str;

		Type type = new TypeToken<ArrayList<ManageGroupoHeadWaiter>>() {
		}.getType();
		NetTask<ManageGroupoHeadWaiter> net = new NetTask<ManageGroupoHeadWaiter>(
				group_handler.obtainMessage(), client, type, mContext);
		net.execute();
	}

	public void LoadHuiWuData() {
//		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
//			Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
//			return;
//		}
		// 下面是网络请求
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/vipmembers.do?"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=getImportService");

		String str = strbuf.toString();
		client.params = str;

		Type type = new TypeToken<ArrayList<ManageGroupoHeadWaiter>>() {
		}.getType();
		NetTask<ManageGroupoHeadWaiter> net = new NetTask<ManageGroupoHeadWaiter>(
				headwaiters_handler.obtainMessage(), client, type, mContext);
		net.execute();
	}

	class ManageHistoryAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> fragments;
		private FragmentManager fm;

		public ManageHistoryAdapter(FragmentManager fm,
				ArrayList<Fragment> fragments) {
			super(fm);
			this.fm = fm;
			this.fragments = fragments;
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

	public void onEventMainThread(ManageGroupoHeadWaiter groupohwaiter) {
		if (groupohwaiter.groupId != null) {
			enterFragment(Conversation.ConversationType.GROUP,
					groupohwaiter.groupId);
			if (groupohwaiter.name != null) {
				Constant.mTargetId = groupohwaiter.groupId;
				Constant.mTargetName = groupohwaiter.name;
				Constant.mConversationType = Conversation.ConversationType.GROUP;
			}
		}
		if (groupohwaiter.ry_userId != null) {
			enterFragment(Conversation.ConversationType.PRIVATE,
					groupohwaiter.ry_userId);
			if (groupohwaiter.name != null) {
				Constant.mTargetId = groupohwaiter.ry_userId;
				Constant.mTargetName = groupohwaiter.name;
				Constant.mConversationType = Conversation.ConversationType.PRIVATE;
			}
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
		transaction.add(R.id.manage_frame_group, fragment);
		transaction.commit();
	}

	@SuppressWarnings("deprecation")
	private void setAllClick() {
		manage_rg
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.radiobutton_manage_group:
							renyuan_lv.setVisibility(View.GONE);
							manage_history_vp.setVisibility(View.GONE);
							group_lv.setVisibility(View.VISIBLE);
							if (group_lv.getCheckedItemPosition() >= 0) {
								EventBus.getDefault().post(
										group_list.get(group_lv
												.getCheckedItemPosition()));
							} else {
								if (group_list.size() > 0) {
									group_lv.setSelection(0);
									group_lv.setItemChecked(0, true);
									EventBus.getDefault().post(
											group_list.get(0));
								} else {
									nodatafound.setVisibility(View.VISIBLE);
									group_lv.setVisibility(View.GONE);
								}
							}
							break;
						case R.id.radiobutton_manage_renyuan:
							group_lv.setVisibility(View.GONE);
							manage_history_vp.setVisibility(View.GONE);
							renyuan_lv.setVisibility(View.VISIBLE);
							if (renyuan_lv.getCheckedItemPosition() >= 0) {
								EventBus.getDefault().post(
										renyuan_list.get(renyuan_lv
												.getCheckedItemPosition()));
							} else {
								if (renyuan_list.size() > 0) {
									renyuan_lv.setSelection(0);
									renyuan_lv.setItemChecked(0, true);
									EventBus.getDefault().post(
											renyuan_list.get(0));
								} else {
									nodatafound.setVisibility(View.VISIBLE);
									renyuan_lv.setVisibility(View.GONE);
								}
							}
							break;
						case R.id.radiobutton_manage_history:
							group_lv.setVisibility(View.GONE);
							renyuan_lv.setVisibility(View.GONE);
							manage_history_vp.setVisibility(View.VISIBLE);
							
							break;
						}
					}
				});
		group_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EventBus.getDefault().post(group_list.get(position));
			}
		});
		renyuan_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EventBus.getDefault().post(renyuan_list.get(position));
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@SuppressWarnings("unchecked")
	Handler group_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (pd != null) {
				pd.dismiss();
			}
			switch (msg.what) {
			case 0:// 获取重要嘉宾的列表返回数据
				nodatafound.setVisibility(View.VISIBLE);
				ResponseBody<ManageGroupoHeadWaiter> res = (ResponseBody<ManageGroupoHeadWaiter>) msg.obj; // 首先创建接收方法
				group_list.clear();
				group_list.addAll(res.list);
				group_adapter.notifyDataSetChanged();
				//
				group_lv.setSelection(0);
				group_lv.setItemChecked(0, true);
				EventBus.getDefault().post(group_list.get(0));
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
	@SuppressWarnings("unchecked")
	Handler headwaiters_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (pd != null) {
				pd.dismiss();
			}
			switch (msg.what) {
			case 0:// 获取重要嘉宾的列表返回数据
				ResponseBody<ManageGroupoHeadWaiter> res = (ResponseBody<ManageGroupoHeadWaiter>) msg.obj; // 首先创建接收方法
				renyuan_list.clear();
				renyuan_list.addAll(res.list);
				renyuan_adapter.notifyDataSetChanged();
				//
				renyuan_lv.setSelection(0);
				renyuan_lv.setItemChecked(0, true);
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
