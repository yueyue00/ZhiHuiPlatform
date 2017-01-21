package com.smartdot.wenbo.controlcenter.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.google.gson.Gson;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.bean.EmptyInfo;
import com.smartdot.wenbo.controlcenter.bean.ScheduleAllData;
import com.smartdot.wenbo.controlcenter.http.HttpUrlConnectionutil;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class CarSourceSearchTask extends AsyncTask<Integer, Integer, Integer>{
	Message m;
	Gson g = new Gson();

	
	Context context;
	String userId;
	String carNumber;

	public CarSourceSearchTask(Message m,Context context,String userId,String carNumber) {
		this.m = m;
		this.context = context;
		this.userId = userId;
		this.carNumber = carNumber;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			String sss = Constant.DOMAIN + "/taskSign.do";
			URL url = new URL(sss);
			System.out.println("method=getCarList&userId="+userId+"&carNumber="+carNumber);
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisPOST(
					url,
					"method=getCarList&userId="+userId+"&carNumber="+carNumber,
					        this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println("===carSourceSearch==:" + json);// 输出json
				
				String jiabinString =  "\"info\":\"\"";
				System.out.println("------->"+jiabinString);
				m.obj = json;
				return 1;
//				if (json.contains("\"info\":\"\"")) {
//					EmptyInfo emptyInfo = g.fromJson(json, EmptyInfo.class);
//					if (emptyInfo.getCode().equals("300")) {
//						return 300;
//					}
//					if (emptyInfo.getCode().equals("400")) {
//						return 400;
//					}
//					if (emptyInfo.getCode().equals("500")) {
//						return 500;
//					}else {
//						return -2;
//					}
//                    
//					
//				} else {
//
//					ScheduleAllData bean = g.fromJson(json, ScheduleAllData.class);
//					if (bean.getCode().equals("300")) {
//						return 300;
//					}
//	                if (bean.getCode().equals("500")) {
//						return 500;
//					}
//	                if (bean.getCode().equals("400")) {
//						return 400;
//					}
//	                if (bean.getCode().equals("200")) {//请求成功
//						m.obj = bean;
////	                	m.obj = json;
//						return 1;
//					}else {
//						return -1;
//					}
//				}
			} else {
				Log.d("tag", "======is为空");
				return -1;
			}
				
		} catch (IOException e) {
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
