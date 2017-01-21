package com.smartdot.wenbo.controlcenter.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.adapter.ManageGroupRyAdapter;

//服务调度-会务群组界面<没有用到>
public class ManageHuiWuGroupFragment extends Fragment {

	private Context mContext;
	ListView manage_lv;
	ManageGroupRyAdapter manage_adapter;
	ArrayList<String> list = new ArrayList<>();
	MyConversitionFragment conversitionFragment;

	public ManageHuiWuGroupFragment() {

	}

	@Override
	public void onAttach(Context context) {
		// TODO Auto-generated method stub
		super.onAttach(context);
		mContext = getActivity();
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.fragment_manage_huiwugroup, null);
		// conversitionFragment = new ConversitionFragment();
		initView(view);
		setAllClick();
		return view;
	}

	private void initView(View view) {
		//
		manage_lv = (ListView) view.findViewById(R.id.manage_group_lv);
		// manage_adapter = new ManageGroupRyAdapter(mContext, list, 1);
		// manage_lv.setAdapter(manage_adapter);
		//
		FragmentTransaction transaction = getChildFragmentManager()
				.beginTransaction();
		if (!conversitionFragment.isAdded()) {// 未被添加
			transaction.add(R.id.manage_frame_group, conversitionFragment);
		} else if (conversitionFragment.isHidden()) {// 添加了但被隐藏
			transaction.show(conversitionFragment);
		}
		// transaction.replace(R.id.manage_frame_group,
		// new ManageHuiWuConversitionFragment());
		transaction.commit();
	}

	private void setAllClick() {

	}
}
