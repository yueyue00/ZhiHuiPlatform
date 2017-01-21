package com.smartdot.wenbo.controlcenter.bean;

import com.google.gson.annotations.Expose;

public class ManageGroupoHeadWaiter {
	@Expose
	public String groupId;
	@Expose
	public String name;
	@Expose
	public String tel;
	@Expose
	public String ry_userId;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getRy_userId() {
		return ry_userId;
	}
	public void setRy_userId(String ry_userId) {
		this.ry_userId = ry_userId;
	}

}
