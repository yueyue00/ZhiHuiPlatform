package com.smartdot.wenbo.controlcenter.bean;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Contacts {
	@Expose
	public List<ContactBean> group = new ArrayList<ContactBean>();
	@Expose
	public List<ContactBean> groupuser = new ArrayList<ContactBean>();
	@Override
	public String toString() {
		return "Contacts [group=" + group + ", groupuser=" + groupuser + "]";
	}
	
}
