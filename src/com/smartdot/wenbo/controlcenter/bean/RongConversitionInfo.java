package com.smartdot.wenbo.controlcenter.bean;

import io.rong.imlib.model.Conversation;

public class RongConversitionInfo {

	public String mTargetId;
	public Conversation.ConversationType mConversationType;
	public String talkname;

	public RongConversitionInfo(
			Conversation.ConversationType mConversationType, String mTargetId,
			String talkname) {
		this.mTargetId = mTargetId;
		this.mConversationType = mConversationType;
		this.talkname = talkname;
	}
}
