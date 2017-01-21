package com.smartdot.wenbo.controlcenter.bean;

import android.view.ViewDebug.ExportedProperty;

import com.google.gson.annotations.Expose;

public class ScheduleBean {
	@Expose 
	public String startTime;
	@Expose
	public String endTime;
	@Expose
	public String title;
	@Expose
	public String hall;
	@Expose 
	public String sponsor;
	@Expose 
    public String hostParty;
	@Expose
	 public String fuzeren;
	@Expose 
	public long startTimeL;
	@Expose
	public long endTimeL;
	
	
	public long getStartTimeL() {
		return startTimeL;
	}
	public void setStartTimeL(long startTimeL) {
		this.startTimeL = startTimeL;
	}
	public long getEndTimeL() {
		return endTimeL;
	}
	public void setEndTimeL(long endTimeL) {
		this.endTimeL = endTimeL;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHall() {
		return hall;
	}
	public void setHall(String hall) {
		this.hall = hall;
	}
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	public String getHostParty() {
		return hostParty;
	}
	public void setHostParty(String hostParty) {
		this.hostParty = hostParty;
	}
	public String getFuzeren() {
		return fuzeren;
	}
	public void setFuzeren(String fuzeren) {
		this.fuzeren = fuzeren;
	}
	
	
}
