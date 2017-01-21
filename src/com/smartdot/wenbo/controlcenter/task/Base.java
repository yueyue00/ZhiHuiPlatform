package com.smartdot.wenbo.controlcenter.task;

import java.io.Serializable;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;

@SuppressWarnings("serial")
public class Base implements Serializable {
	public int responseCode = 0;
	@Expose
	public String code = "999";
	@Expose
	public JsonElement info = null;
	@Expose
	public int pagecount = 0000;
	@Expose
	public String message = "服务器繁忙,请稍后再试";
	@Expose
	public ClientParams params;// 请求参数

}
