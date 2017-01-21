package com.smartdot.wenbo.controlcenter.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.google.gson.Gson;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.bean.EmptyInfo;
import com.smartdot.wenbo.controlcenter.bean.HomeBean;
import com.smartdot.wenbo.controlcenter.bean.HomeBean.InfoBean;
import com.smartdot.wenbo.controlcenter.bean.ScheduleAllData;
import com.smartdot.wenbo.controlcenter.http.HttpUrlConnectionutil;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class HomeTask extends AsyncTask<Integer, Integer, Integer>{

	Message m;
	Gson g = new Gson();
    String userId;
	
	Context context;

	public HomeTask(Message m,  Context context,String userId) {
		this.m = m;
		this.context = context;
		this.userId = userId;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
//			String sss = "http://192.168.253.16:8080/wenbo2" + "/atAgenda.do";
			String sss = Constant.DOMAIN + "/atAgenda.do";
			URL url = new URL(sss);
			System.out.println("method=getAgendaList&userId="+userId);
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisPOST(
					url,
					"method=getAgendaList&userId="+userId,
					        this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println("===schdule==:" + json);// 输出json
				
				String jiabinString =  "\"info\":\"\"";
				System.out.println("------->"+jiabinString);
				
				if (json.contains("\"info\":\"\"")) {
					EmptyInfo emptyInfo = g.fromJson(json, EmptyInfo.class);
					if (emptyInfo.getCode().equals("300")) {
						return 300;
					}
					if (emptyInfo.getCode().equals("400")) {
						return 400;
					}
					if (emptyInfo.getCode().equals("500")) {
						return 500;
					}else {
						return -2;
					}
                    
					
				} else {

					HomeBean bean = g.fromJson(json, HomeBean.class);
					if (bean.getCode().equals("300")) {
						return 300;
					}
	                if (bean.getCode().equals("500")) {
						return 500;
					}
	                if (bean.getCode().equals("400")) {
						return 400;
					}
	                if (bean.getCode().equals("200")) {//请求成功
	                	for (int i = 0; i < bean.getInfo().size(); i++) {
	                		InfoBean infoBean = bean.getInfo().get(i);
	                		int length = infoBean.getDate().length();
	                		String flag = infoBean.getDate().substring(length-2);
							infoBean.setFlag(Integer.parseInt(flag)+"");
						}
						m.obj = bean;
//	                	m.obj = json;
						return 1;
					}else {
						return -1;
					}
				}
                    
					
					
				
				
            	
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
