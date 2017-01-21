package com.smartdot.wenbo.controlcenter.fragment;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartdot.wenbo.controlcenter.R;

import de.greenrobot.event.EventBus;

//服务调度-聊天界面
public class MyConversitionFragment extends Fragment {

	private Context mContext;
	TextView tv_title;
	ImageView conversition_backiv;
	private Conversation.ConversationType mConversationType;
	private String mTargetId;
	private String talkname;

	public MyConversitionFragment(
			Conversation.ConversationType conversationType, String targetId,
			String talkname) {
		this.mConversationType = conversationType;
		this.mTargetId = targetId;
		this.talkname = talkname;
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
				R.layout.fragment_conversation, null);
		initView(view);
		setAllClick();
		return view;
	}

	private void initView(View view) {
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		conversition_backiv = (ImageView) view
				.findViewById(R.id.conversition_backiv);
		tv_title.setText(talkname);
		enterFragment(mConversationType, mTargetId);
	}

	private void setAllClick() {

		conversition_backiv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// EventBus.getDefault().post(new ImportoConversition("back"));
			}
		});
	}

	public void onEventMainThread() {

	}

	private void enterFragment(Conversation.ConversationType mConversationType,
			String mTargetId) {

		ConversationFragment fragment = (ConversationFragment) ((FragmentActivity) mContext)
				.getSupportFragmentManager().findFragmentById(
						R.id.conversation_fragment);

		Uri uri = Uri
				.parse("rong://" + mContext.getApplicationInfo().packageName)
				.buildUpon().appendPath("conversation")
				.appendPath(mConversationType.getName().toLowerCase())
				.appendQueryParameter("targetId", mTargetId).build();

		fragment.setUri(uri);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
