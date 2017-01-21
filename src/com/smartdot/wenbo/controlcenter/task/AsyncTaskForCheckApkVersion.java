package com.smartdot.wenbo.controlcenter.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.google.gson.Gson;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.bean.BasePojo;
import com.smartdot.wenbo.controlcenter.bean.NewApkVersionPojo;
import com.smartdot.wenbo.controlcenter.http.HttpUrlConnectionutil;

public class AsyncTaskForCheckApkVersion extends
		AsyncTask<Integer, Integer, Integer> {

	public Message m;
	public Gson g = new Gson();
	public Context context;

	public AsyncTaskForCheckApkVersion(Message m, Context context) {
		this.m = m;
		this.context = context;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		try {
			URL url = new URL(Constant.DOMAIN
					+ "/appApk.do?method=appEdition&htype=1&type=1");
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisGET(
					url, context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println("AsyncTaskForCheckApkVersion:json" + json);// 输出json
				BasePojo bpojo = g.fromJson(json, BasePojo.class);// 解析成basepojo
				if (bpojo.code.equals("500")) {
					return 500;
				}
				if (bpojo.code.equals("200")) {// 请求成功
					// Type type=new
					// TypeToken<ArrayList<XiaoXiPojo>>(){}.getType();//设置集合type
					NewApkVersionPojo pojo = g.fromJson(bpojo.info,
							NewApkVersionPojo.class);// 解析多层jsonelement数据
					m.obj = pojo;
					return 1000;
				} else {
					return -1000;
				}
			} else {
				return -1000;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1000;
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		m.what = result;
		m.sendToTarget();
	}
}
