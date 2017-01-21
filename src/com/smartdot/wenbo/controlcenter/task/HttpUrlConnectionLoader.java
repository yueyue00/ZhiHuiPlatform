package com.smartdot.wenbo.controlcenter.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;

//这个是为NetTask里面使用的下载方法
public class HttpUrlConnectionLoader {
	/**
	 * 建立连接的超时值
	 */
	private static final int TIMEOUT = 10 * 1000;
	private String url;
	private String content;

	public void postDataFromSelf(ClientParams params,
			OnEntityLoadCompleteListener<Base> listener, Context context) {
		Base base = new Base();
		HttpURLConnection connection = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			url = params.domain + params.url;
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);// 不使用缓存
			connection.setRequestMethod(params.http_method);
			System.out.println("aaa:HttpUrlConnectionLoader:getCookie:"
					+ Constant.getCookie(context, Constant.domain));
			try {
				connection.setRequestProperty("Cookie",
						Constant.getCookie(context, Constant.domain));
			} catch (Exception e) {
				e.printStackTrace();
			}// 请求头加入cookie信息

			connection.setRequestProperty("charset", "UTF-8");
			connection.setConnectTimeout(TIMEOUT);
			connection.setReadTimeout(TIMEOUT);
			connection.connect();
			os = connection.getOutputStream();
			os.write(params.params.getBytes("UTF-8"));
			os.flush();
			switch (connection.getResponseCode()) {
			case HttpURLConnection.HTTP_OK:
				// 数据传递成功
				System.out.println("aaa:HttpUrlConnectionLoader:HTTP_OK");
				is = connection.getInputStream();
				content = convertStreamToString(is);
				System.out.println("aaa:HttpUrlConnectionLoader:content:"
						+ content);
				if (content.equals("")) {
					listener.onError(base);
				} else {
					base = Constant.gson.fromJson(content, Base.class);
					base.responseCode = connection.getResponseCode();
					base.params = params;// 将请求参数带出
					System.out.println("aaa:HttpUrlConnectionLoader:base"
							+ base.toString());
					listener.onEntityLoadComplete(base);
				}
				break;
			default:
				System.out.println("aaa:HttpUrlConnectionLoader:请求失败！");
				// 网络异常
				if (listener != null) {
					base.responseCode = connection.getResponseCode();
					base.params = params;// 将请求参数带出

				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// // 网络异常
			// if (listener != null) {
			// if (e instanceof JsonSyntaxException) {
			// base.message = "json数据格式错误";
			// } else if (e instanceof MalformedURLException) {
			// base.message = "URL格式错误";
			// } else if (e instanceof UnknownHostException) {
			// base.message = "不能解析服务器地址";
			// } else if (e instanceof SocketTimeoutException) {
			// base.message = "网络不给力";
			// } else if (e instanceof ConnectException) {
			// base.message = "连接失败";
			// } else {
			// base = new Base();
			// base.params = params;
			// base.message = "未知错误";
			// }
			// base.params = params;// 将请求参数带出
			// listener.onError(base);
			// }
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					if (listener != null) {
						base.params = params;// 将请求参数带出
						listener.onError(base);
					}
				}
				os = null;
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					if (listener != null) {
						base.params = params;// 将请求参数带出
						listener.onError(base);
					}
				}
				is = null;
			}
			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}
	}

	private String convertStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
