package com.smartdot.wenbo.controlcenter.fragment;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.adapter.AdapterContactHor;
import com.smartdot.wenbo.controlcenter.adapter.AdapterContactVer;
import com.smartdot.wenbo.controlcenter.adapter.AdapterContactVer.Callback;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;
import com.smartdot.wenbo.controlcenter.bean.ContactBean;
import com.smartdot.wenbo.controlcenter.bean.Contacts;
import com.smartdot.wenbo.controlcenter.bean.RongConversitionInfo;
import com.smartdot.wenbo.controlcenter.bean.User;
import com.smartdot.wenbo.controlcenter.customview.HorizontalListView;
import com.smartdot.wenbo.controlcenter.task.NetTask;
import com.smartdot.wenbo.controlcenter.task.ResponseBody;
import com.smartdot.wenbo.controlcenter.task.zAsyncTaskForContact;
import com.smartdot.wenbo.controlcenter.util.MSharePreferenceUtils;

import de.greenrobot.event.EventBus;

public class ImportantGuestFragment extends Fragment {

	private Context mcontext;
	HorizontalListView lv_hor;
	ListView lv_ver;
	LinearLayout search_layout;
	EditText search_location;
	TextView search_tv;
	//
	AdapterContactHor adapter_rvHor;
	AdapterContactVer adapter_rvVer;
	static ArrayList<ContactBean> horlist = new ArrayList<>();
	// 搜索结果
	ArrayList<ContactBean> search_result = new ArrayList<ContactBean>();
	ArrayList<ContactBean> verlist = new ArrayList<>();
	// 选中的那些人》》用来提交的时候使用
	int hasdata = 0;// 获取到当前页是否之前保存了数据的标识(有数据：1；没数据：0)
	ArrayList<ContactBean> mchecks = new ArrayList<ContactBean>();
	// 修改的那些集合对应的id的集合
	public ArrayList<String> changeids = new ArrayList<>();
	String addgroupid = "";// 从拉人界面传来的
	String addgroupname = "";// 从拉人界面传来的
	Boolean creategroup = false;// 从rongdemotabs进入创建群组
	// 右边的布局
	TextView conversition_title;
	ImageView conversition_backiv;
	LinearLayout import_guest_right_ll;
	LinearLayout import_guest_conversition;
	ViewPager guest_vp;
	ViewPager tuan_vp;
	RadioGroup important_rg;
	TextView nodatafound;
	ArrayList<Fragment> mfragments = new ArrayList<Fragment>();
	ArrayList<Fragment> tuanfragments = new ArrayList<Fragment>();

	ImportantGuestInfoFragment guestinfofragment;
	ImportGuestSchduleFragment guestschdulefragment;
	ImportantGuestWaitersFragment guestwaitersfragment;
	ImportGuestSchduleFragment tuanschdulefragment;
	// private ArrayList<String> ipadap_datas = new ArrayList<String>();
	ImporGuestFmAdapter imporguest_adapter;
	ImporGuestFmAdapter tuan_adapter;
	//
	RadioButton radiobutton_richeng;
	RadioButton radiobutton_fwrenyuan;
	RadioButton radiobutton_xingcheng;
	TextView tv_title;
	// zyj 新增verlistview选中的位置
	int verlv_position = 0;
	ProgressDialog pd;
	User parent;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mcontext = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = LayoutInflater.from(mcontext).inflate(
				R.layout.zyjfragment_contact, null);
		MSharePreferenceUtils.getAppConfig(mcontext);
		parent = (User) MSharePreferenceUtils.getBean(mcontext,
				Constant.sp_user);
		initView(view);
		LoadData("0", "0", "2");
		process();
		setAllClick();
		return view;
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (hidden) {//隐藏
			System.out.println("======嘉宾信息的界面隐藏掉了");
			search_location.setText("");
		}else {//显示
			System.out.println("======嘉宾信息的界面显示了");
			LoadData("0", "0", "2");
			
		}
	}
	private void initView(View view) {
		guestinfofragment = new ImportantGuestInfoFragment();
		guestschdulefragment = new ImportGuestSchduleFragment();
		guestwaitersfragment = new ImportantGuestWaitersFragment();
		tuanschdulefragment = new ImportGuestSchduleFragment();
		//
		lv_hor = (HorizontalListView) view.findViewById(R.id.lv_horzontal);
		lv_ver = (ListView) view.findViewById(R.id.lv_vertical);
		search_layout = (LinearLayout) view.findViewById(R.id.search_layout);
		// 右边的布局
		conversition_backiv = (ImageView) view
				.findViewById(R.id.conversition_backiv);
		import_guest_right_ll = (LinearLayout) view
				.findViewById(R.id.import_guest_right_ll);
		import_guest_conversition = (LinearLayout) view
				.findViewById(R.id.import_guest_conversition);
		conversition_title = (TextView) view
				.findViewById(R.id.conversition_title);
		important_rg = (RadioGroup) view.findViewById(R.id.important_vg);
		nodatafound = (TextView) view.findViewById(R.id.nodatafound);
		// 人的viewpager
		guest_vp = (ViewPager) view.findViewById(R.id.guest_vp);
		mfragments.add(guestinfofragment);
		mfragments.add(guestschdulefragment);
		mfragments.add(guestwaitersfragment);
		imporguest_adapter = new ImporGuestFmAdapter(
				((FragmentActivity) mcontext).getSupportFragmentManager(),
				mfragments);
		guest_vp.setAdapter(imporguest_adapter);
		guest_vp.setOffscreenPageLimit(3);
		// 团的viewpager
		tuan_vp = (ViewPager) view.findViewById(R.id.tuan_vp);
		tuanfragments.add(tuanschdulefragment);
		tuan_adapter = new ImporGuestFmAdapter(
				((FragmentActivity) mcontext).getSupportFragmentManager(),
				tuanfragments);
		tuan_vp.setAdapter(tuan_adapter);
		//
		radiobutton_richeng = (RadioButton) view
				.findViewById(R.id.radiobutton_richeng);
		radiobutton_fwrenyuan = (RadioButton) view
				.findViewById(R.id.radiobutton_fwrenyuan);
		radiobutton_xingcheng = (RadioButton) view
				.findViewById(R.id.radiobutton_xingcheng);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("嘉宾信息");
		//
		search_location = (EditText) view.findViewById(R.id.search_location);
		search_tv = (TextView) view.findViewById(R.id.search_tv);
	}

	// 加载通讯录列表及层级关系的数据
	@SuppressWarnings("unchecked")
	public void LoadData(String pid, String id, String sign) {
		ArrayList<ContactBean> liebiao = null;
		String ppid = "-1";
		if (horlist.size() >= 2) {
			ppid = horlist.get(horlist.size() - 1).pid;
		}
		for (int i = 0; i < changeids.size(); i++) {
			String changeid = changeids.get(i);
			if (sign.equals("1")) {
				if (changeid.equals(ppid)) {
					liebiao = (ArrayList<ContactBean>) MSharePreferenceUtils
							.getBean(mcontext, ppid);
					hasdata = 1;
				}
			} else if (sign.equals("2")) {
				if (changeid.equals(id)) {
					liebiao = (ArrayList<ContactBean>) MSharePreferenceUtils
							.getBean(mcontext, id);
					hasdata = 1;
				}
			}
		}
		if (hasdata == 1 && liebiao != null) {
			if (liebiao.size() <= 0) {
				hasdata = 0;
			} else {
				Message msg = contact_handler.obtainMessage(5);
				msg.obj = liebiao;
				msg.sendToTarget();
			}
		} else {
			hasdata = 0;
		}
		// ----------------------------------------------
		try {
//			pd = ProgressDialog.show(mcontext, "", "加载中...");
//			pd.setCancelable(true);
//			pd.setCanceledOnTouchOutside(false);
			zAsyncTaskForContact at = new zAsyncTaskForContact(
					contact_handler.obtainMessage(), mcontext, Constant.decode(
							Constant.key, parent.getUserId().trim()), pid, id,
					sign);
			at.execute(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goSearch(String content) throws Exception {
//		pd = ProgressDialog.show(mcontext, "", "加载中...");
//		pd.setCancelable(true);
//		pd.setCanceledOnTouchOutside(false);
		// 下面是网络请求
		NetTask<ContactBean> aa;
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/deptAction.do?"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=findMemberListByName&userId=");
		strbuf.append(Constant.decode(Constant.key, parent.getUserId().trim()));
		strbuf.append("&name=");
		strbuf.append(content);

		String str = strbuf.toString();
		client.params = str;

		Type type = new TypeToken<ArrayList<ContactBean>>() {
		}.getType();
		NetTask<ContactBean> net = new NetTask<ContactBean>(
				search_handler.obtainMessage(), client, type, mcontext);
		net.execute();
	}

	private void process() {
		adapter_rvHor = new AdapterContactHor(horlist, mcontext);
		lv_hor.setAdapter(adapter_rvHor);
		// 在垂直的列表适配器里面可以回调到button点击的回调
		adapter_rvVer = new AdapterContactVer(mcontext, new Callback() {

			@Override
			public void mclick(ContactBean contactBean, int result) {
				lv_ver.setSelection(result);
				lv_ver.setItemChecked(result, true);
				initLvORightLayout(0, -1, contactBean);
			}
		});
		lv_ver.setAdapter(adapter_rvVer);
		// zyj修改-去掉创建群组通道
		if (!addgroupid.equals("") || creategroup) {
			if (creategroup)
				mchecks.clear();
			adapter_rvVer.isEdit = true;
			adapter_rvVer.showCheckBox();
		} else {
			// zyj修改-去掉创建群组通道
			// add_group_img.setVisibility(View.VISIBLE);
		}
	}

	// 设置listview的单选效果及右边的布局更换》0-代表团;1-代表人》如果position的值小于0就是从适配器里面回调的结果
	private void initLvORightLayout(int type, int position,
			ContactBean contactBean) {
		switch (type) {
		case 0:
			radiobutton_richeng.setVisibility(View.GONE);
			radiobutton_fwrenyuan.setVisibility(View.GONE);
			radiobutton_xingcheng.setText("团行程");
			radiobutton_xingcheng.setChecked(true);
			//
			import_guest_conversition.setVisibility(View.GONE);
			import_guest_right_ll.setVisibility(View.VISIBLE);
			guest_vp.setVisibility(View.GONE);
			tuan_vp.setVisibility(View.VISIBLE);
			if (position >= 0) {
				EventBus.getDefault().post(verlist.get(position));
			} else {
				EventBus.getDefault().post(contactBean);
			}
			break;
		case 1:
			radiobutton_xingcheng.setText("个人行程");
			radiobutton_richeng.setVisibility(View.VISIBLE);
			radiobutton_fwrenyuan.setVisibility(View.VISIBLE);
			radiobutton_xingcheng.setVisibility(View.VISIBLE);
			//
			import_guest_conversition.setVisibility(View.GONE);
			import_guest_right_ll.setVisibility(View.VISIBLE);
			tuan_vp.setVisibility(View.GONE);
			guest_vp.setVisibility(View.VISIBLE);
			EventBus.getDefault().post(verlist.get(position));
			break;
		default:
			break;
		}
	}

	// public void initSchduleFragment() {
	// ImportGuestSchduleFragment fragment2 = new ImportGuestSchduleFragment();
	// FragmentManager fragmrentManager = getFragmentManager();
	// FragmentTransaction beginTransaction = fragmrentManager
	// .beginTransaction();
	// beginTransaction.add(R.id.schdule_frame, fragment2);
	// beginTransaction.addToBackStack(null);
	// beginTransaction.commit();
	// }

	public void onEventMainThread(RongConversitionInfo ronginfo) {
		if (ronginfo.mConversationType != null && ronginfo.mTargetId != null
				&& ronginfo.talkname != null) {
			import_guest_right_ll.setVisibility(View.GONE);
			import_guest_conversition.setVisibility(View.VISIBLE);
			conversition_title.setText(ronginfo.talkname);
			enterFragment(ronginfo.mConversationType, ronginfo.mTargetId);
			// zyj 新加的设置全局接收者信息
			Constant.mTargetId = ronginfo.mTargetId;
			Constant.mTargetName = ronginfo.talkname;
			Constant.mConversationType = ronginfo.mConversationType;
		}
	}

	private void enterFragment(Conversation.ConversationType mConversationType,
			String mTargetId) {
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		ConversationFragment fragment = new ConversationFragment();

		Uri uri = Uri
				.parse("rong://" + mcontext.getApplicationInfo().packageName)
				.buildUpon().appendPath("conversation")
				.appendPath(mConversationType.getName().toLowerCase())
				.appendQueryParameter("targetId", mTargetId).build();

		fragment.setUri(uri);

		FragmentTransaction transaction = ((FragmentActivity) mcontext)
				.getSupportFragmentManager().beginTransaction();
		// xxx 为你要加载的 id
		transaction.add(R.id.import_guest_rongy_frame, fragment);
		transaction.commit();
	}

	@SuppressWarnings("deprecation")
	private void setAllClick() {

		lv_ver.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				verlv_position = position;
				if (verlist.get(position).type.equals("1")) {
					String pid = verlist.get(position).pid;
					String mid = verlist.get(position).id;
					String sign = "2";
					// Toast.makeText(context, id,
					// Toast.LENGTH_SHORT).show();
					LoadData(pid, mid, sign);
				} else {
					// try {
					// RongIM.getInstance().startPrivateChat(mcontext,
					// verlist.get(position).map.ry_userId,
					// verlist.get(position).truename);
					// } catch (Exception e) {
					// }
					initLvORightLayout(1, position, null);
				}
			}
		});
		lv_hor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				search_location.setText("");
				String pid = horlist.get(position).pid;
				String mid = horlist.get(position).id;
				String sign = "2";
				LoadData(pid, mid, sign);
			}
		});
		guest_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				if (important_rg.getChildAt(position) != null) {
					RadioButton rb = (RadioButton) important_rg
							.getChildAt(position);
					rb.setChecked(true);
				} else {
					Toast.makeText(mcontext, "没有这个child", Toast.LENGTH_SHORT)
							.show();
				}
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
		conversition_backiv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				import_guest_conversition.setVisibility(View.GONE);
				import_guest_right_ll.setVisibility(View.VISIBLE);
			}
		});
		search_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = search_location.getEditableText().toString();
				try {
					goSearch(content);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	class ImporGuestFmAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> fragments;
		private FragmentManager fm;

		public ImporGuestFmAdapter(FragmentManager fm,
				ArrayList<Fragment> fragments) {
			super(fm);
			this.fm = fm;
			this.fragments = fragments;
		}

		// public void setFragments(ArrayList<Fragment> fragments,
		// ArrayList<String> datas) {
		// if (this.fragments != null) {
		// FragmentTransaction ft = fm.beginTransaction();
		// for (Fragment f : this.fragments) {
		// ft.remove(f);
		// }
		// ft.commit();
		// ft = null;
		// fm.executePendingTransactions();
		// }
		// this.fragments = fragments;
		// this.datas = datas;
		// notifyDataSetChanged();
		// }

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}

	}

	String editgroupName = "默认群组名称";
	// 获取通讯录列表返回的数据
	Handler contact_handler = new Handler() {
		public void handleMessage(Message msg) {
			if (pd != null) {
				pd.dismiss();
			}
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				Contacts contacts = (Contacts) msg.obj;

				ArrayList<ContactBean> cengji = (ArrayList<ContactBean>) contacts.group;
				ContactBean contactBean = new ContactBean();
				contactBean.name = "嘉宾信息";
				contactBean.pid = "0";
				contactBean.id = "0";
				contactBean.type = "1";
				cengji.add(0, contactBean);
				ArrayList<ContactBean> liebiao = (ArrayList<ContactBean>) contacts.groupuser;
				if (hasdata == 0) {
					verlist.clear();
					verlist.addAll(liebiao);
					adapter_rvVer.setData(verlist);
					// zyj 0907新加>如果有数据默认设置第一条被选中
					lv_ver.setSelection(0);
					lv_ver.setItemChecked(0, true);
					if (liebiao.get(0).type.equals("1")) {// 列表的第一项是部门
						initLvORightLayout(0, 0, null);
					} else {
						initLvORightLayout(1, 0, null);
					}
				}
				horlist.clear();
				horlist.addAll(cengji);
				lv_ver.setVisibility(View.VISIBLE);
				nodatafound.setVisibility(View.GONE);
				adapter_rvHor.notifyDataSetChanged();
				break;
			case 1:
				// Toast.makeText(mcontext, "请求错误！", Toast.LENGTH_SHORT).show();
				lv_ver.setVisibility(View.GONE);
				nodatafound.setVisibility(View.VISIBLE);
				break;
			case 2:
				// zyj 0907新加>如果没有数据就设置当前项被选中
				initLvORightLayout(0, verlv_position, null);
				// Toast.makeText(mcontext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				// Toast.makeText(mcontext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				// Toast.makeText(mcontext, "cookie失效，请求超时！",
				// Toast.LENGTH_SHORT)
				// .show();
				break;
			case 5:// 这是保存信息的时候才会
				List<ContactBean> liebiao2 = (List<ContactBean>) msg.obj;
				verlist.clear();
				verlist.addAll(liebiao2);
				adapter_rvVer.setData(verlist);
				break;
			case 6:

				break;
			default:
				break;
			}
		};
	};
	Handler search_handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			try {
				if (pd != null) {
					pd.dismiss();
				}
				switch (msg.what) {
				case 0:// 融云获取token时返回内容
					ResponseBody<ContactBean> res = (ResponseBody<ContactBean>) msg.obj; // 首先创建接收方法
					verlist.clear();
					verlist.addAll(res.list);
					System.out.println("search_handler" + res.list.toString());
					adapter_rvVer.setData(verlist);
					nodatafound.setVisibility(View.GONE);
					break;
				case 1:
					Toast.makeText(mcontext, "请求错误！", Toast.LENGTH_SHORT)
							.show();
					break;
				case 2:
					Toast.makeText(mcontext, "请求失败！", Toast.LENGTH_SHORT)
							.show();
					break;
				case 3:
					lv_ver.setVisibility(View.GONE);
					nodatafound.setVisibility(View.VISIBLE);
					Toast.makeText(mcontext, "数据为空！", Toast.LENGTH_SHORT)
							.show();
					break;
				case 4:
					Toast.makeText(mcontext, "cookie失效，请求超时！",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}
