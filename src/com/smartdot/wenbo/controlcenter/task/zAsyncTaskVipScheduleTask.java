package com.smartdot.wenbo.controlcenter.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.bean.VipSchduleNewBean;
import com.smartdot.wenbo.controlcenter.bean.VipScheduleInfoBean;
import com.smartdot.wenbo.controlcenter.http.HttpUrlConnectionutil;

public class zAsyncTaskVipScheduleTask extends AsyncTask<Integer, Integer, Integer> {

	Message m;
	Gson g = new Gson();

	String vipid;
	Context context;

	public zAsyncTaskVipScheduleTask(Message m, String vipid, Context context) {
		this.m = m;
		this.vipid = vipid;
		this.context = context;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			
			URL url = new URL(Constant.DOMAIN
					+ "/viptrips.do?method=vipTrip&vipid=" + vipid);
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisGET(
					url, this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out
						.println("=======调用了VipScheduleTask的doInBackground方法-->"
								+ json);
				// 当info的数据为空的时候
				VipSchduleNewBean vipSchduleNewBean = null;
				vipSchduleNewBean = g.fromJson(json, VipSchduleNewBean.class);
				if (vipSchduleNewBean.getCode().equals("300")) {
					m.obj = vipSchduleNewBean.getMessage();
					return 300;
				}
				if (vipSchduleNewBean.getCode().equals("500")) {
					m.obj = vipSchduleNewBean.getMessage();
					return 500;
				}
				if (vipSchduleNewBean.getCode().equals("400")) {
					m.obj = vipSchduleNewBean.getMessage();
					return 400;
				}
				if (vipSchduleNewBean.getCode().equals("200")) {// 请求成功
					List<VipScheduleInfoBean> result = new ArrayList<>();
					result.clear();
					JsonArray info = (JsonArray) vipSchduleNewBean.getInfo();
					if (info != null) {

						for (int i = 0; i < info.size(); i++) {
							JsonObject object = (JsonObject) info.get(i);
							VipScheduleInfoBean bean = g.fromJson(object,
									VipScheduleInfoBean.class);
							result.add(bean);
						}
						m.obj = result;
						return 1;

					} else {
						return -2;// info为空
					}
				} else {
					return -1;
				}

			} else {
				return -1;
			}
		} catch (IOException e) {
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
