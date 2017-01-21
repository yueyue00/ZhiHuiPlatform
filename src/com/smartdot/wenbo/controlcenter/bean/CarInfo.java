package com.smartdot.wenbo.controlcenter.bean;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.annotations.Expose;

public class CarInfo {
	@Expose
	public String carName;
	@Expose
	public String carNumber;
	@Expose
	public String state;
	@Expose
	public String carTel;
	@Expose
	public String lon;
	@Expose
	public String lat;
	@Expose
	public LatLng ll;

	public LatLng getLl() {
		return ll;
	}

	public void setLl(LatLng ll) {
		this.ll = ll;
	}
}
