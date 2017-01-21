package com.smartdot.wenbo.controlcenter.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.adapter.ImportGuestWaitersAdapter;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;
import com.smartdot.wenbo.controlcenter.bean.ContactBean;
import com.smartdot.wenbo.controlcenter.bean.ImportGuest;
import com.smartdot.wenbo.controlcenter.bean.ZhuanShuFuWu;
import com.smartdot.wenbo.controlcenter.task.NetTask;
import com.smartdot.wenbo.controlcenter.task.ResponseBody;
import com.smartdot.wenbo.controlcenter.util.IsWebCanBeUse;

import de.greenrobot.event.EventBus;

//重要嘉宾的服务人员界面
public class ImportantGuestWaitersFragment extends Fragment {

	private Context mContext;
	TextView waiters_nodata;
	ContactBean contactBean;
	ListView impor_guest_waiters_lv;
	ImportGuestWaitersAdapter guest_waiters_adapter;
	ArrayList<ZhuanShuFuWu> waiters_data = new ArrayList<ZhuanShuFuWu>();

	public ImportantGuestWaitersFragment() {

	}

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
				R.layout.fragment_important_guest_waiters, null);
		initView(view);
		setAllClick();
		return view;
	}

	private void initView(View view) {
		waiters_nodata = (TextView) view.findViewById(R.id.waiters_nodata);
		impor_guest_waiters_lv = (ListView) view
				.findViewById(R.id.impor_guest_waiters_lv);
		guest_waiters_adapter = new ImportGuestWaitersAdapter(mContext,
				waiters_data);
		impor_guest_waiters_lv.setAdapter(guest_waiters_adapter);
	}

	private void LoadData(String vipid) {
//		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
//			Toast.makeText(mContext, "网络不给力", Toast.LENGTH_SHORT).show();
//			return;
//		}
		try {
			ClientParams client = new ClientParams();
			client.url = "/vipmembers.do?";
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("method=vipFwInfo&vipid=");
			strbuf.append(vipid);
			String str = strbuf.toString();
			client.params = str;
			Type type = new TypeToken<ArrayList<ZhuanShuFuWu>>() {
			}.getType();
			NetTask<ZhuanShuFuWu> net = new NetTask<ZhuanShuFuWu>(
					waiters_handler.obtainMessage(), client, type, mContext);
			net.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setAllClick() {

	}

	public void onEventMainThread(ContactBean contactBean) {
		this.contactBean = contactBean;
		if (contactBean != null) {
			LoadData(contactBean.id);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	Handler waiters_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				impor_guest_waiters_lv.setVisibility(View.VISIBLE);
				waiters_nodata.setVisibility(View.GONE);
				ResponseBody<ZhuanShuFuWu> zhuanshufuwu = (ResponseBody<ZhuanShuFuWu>) msg.obj;
				waiters_data.clear();
				waiters_data.addAll(zhuanshufuwu.list);
				guest_waiters_adapter.notifyDataSetChanged();
				break;
			case 1:
				impor_guest_waiters_lv.setVisibility(View.GONE);
				waiters_nodata.setVisibility(View.VISIBLE);
				String message = (String) msg.obj;
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
				break;
			case 2:
				impor_guest_waiters_lv.setVisibility(View.GONE);
				waiters_nodata.setVisibility(View.VISIBLE);
				// Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				impor_guest_waiters_lv.setVisibility(View.GONE);
				waiters_nodata.setVisibility(View.VISIBLE);
				// Toast.makeText(mContext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				impor_guest_waiters_lv.setVisibility(View.GONE);
				waiters_nodata.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		};
	};
}
