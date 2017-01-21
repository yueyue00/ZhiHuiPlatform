package com.smartdot.wenbo.controlcenter.bean;

import com.google.gson.annotations.Expose;

public class ServiceBean {
	@Expose
	public String taskname;
	@Expose
	public int finish;
	@Expose
	public int unfinish;
	@Expose 
	public int max;
	
	

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public int getFinish() {
		return finish;
	}

	public void setFinish(int finish) {
		this.finish = finish;
	}

	public int getUnfinish() {
		return unfinish;
	}

	public void setUnfinish(int unfinish) {
		this.unfinish = unfinish;
	}

}
