package com.smartdot.wenbo.controlcenter.task;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.bean.BasePojo;
import com.smartdot.wenbo.controlcenter.bean.XiaoXiPojo;
import com.smartdot.wenbo.controlcenter.http.HttpUrlConnectionutil;

public class AsyncTaskForGetXiaoXin extends
		AsyncTask<Integer, Integer, Integer> {

	Message m;
	Gson g = new Gson();
	int pagenum;
	String userid;
	Context context;
	public int pagesize;

	public AsyncTaskForGetXiaoXin(Message m, int pagenum, String userid,
			Context context, int pagesize) {
		this.m = m;
		this.pagenum = pagenum;
		this.userid = userid;
		this.context = context;
		this.pagesize = pagesize;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			URL url = new URL(Constant.DOMAIN
					+ "/hxLastregistercheckaction.do?pagesize=" + pagesize
					+ "&method=xiaoXiCenterAction&userid=" + userid
					+ "&pagenum=" + pagenum + "&language=1");

			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisGET(
					url, this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println(json);// 输出json
				BasePojo bpojo = g.fromJson(json, BasePojo.class);// 解析成basepojo
				if (bpojo.code.equals("300")) {
					m.arg1 = bpojo.pagecount;
					return 0;
				}
				if (bpojo.code.equals("500")) {
					return 500;
				}
				if (bpojo.code.equals("200")) {// 请求成功 300是没数据
					Type type = new TypeToken<ArrayList<XiaoXiPojo>>() {
					}.getType();// 设置集合type
					ArrayList<XiaoXiPojo> infos = g.fromJson(bpojo.info, type);// 解析多层jsonelement数据
					m.obj = infos;
					m.arg1 = bpojo.pagecount;

					return 1;
				} else {
					return -1;
				}
			} else {
				return -1;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		m.what = result;
		m.sendToTarget();
	}
}
