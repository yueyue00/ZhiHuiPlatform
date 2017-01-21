package com.smartdot.wenbo.controlcenter.bean;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.annotations.Expose;

public class HotelInfo {
	@Expose
	public String workUsered;
	@Expose
	public String roomUsered;// 已经占用的房间数量
	@Expose
	public String roomCount;// 房间数量
	@Expose
	public String hotelName;// 酒店名称
	@Expose
	public String lng;// 经度
	@Expose
	public String lat;// 纬度
	@Expose
	public LatLng ll;
	
	public LatLng getLl() {
		return ll;
	}

	public void setLl(LatLng ll) {
		this.ll = ll;
	}
}
