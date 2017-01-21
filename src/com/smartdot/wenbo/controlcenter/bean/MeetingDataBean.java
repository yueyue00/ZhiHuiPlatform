package com.smartdot.wenbo.controlcenter.bean;

import com.google.gson.annotations.Expose;

public class MeetingDataBean {
	@Expose
	public int count;
	@Expose
	public String name;
	@Expose 
	public int max;
	@Expose 
	public String shortName;
	
	

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
