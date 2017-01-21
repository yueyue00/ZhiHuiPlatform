package com.smartdot.wenbo.controlcenter.aconstant;

import io.rong.imlib.model.Conversation;

import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartdot.wenbo.controlcenter.util.Base64;

/**
 * 定义全局常量的工具类
 * 
 * @author lixiaoming
 * 
 */
public class Constant {
	
	public static long ENDTIME_NOW = -1L;
	public static long STARTTIME_NEXT = -1L;
	public static String IMPORTANT = "";
	public static String VIDEOPATH_1 = "";
	public static String VIDEOPATH_2 = "";
	public static String VIDEOPATH_3 = "";
	public static String VIDEOPATH_4 = "";
	public static String SERVER_URL = "http://app.reed-sinopharm.com:8080";
	/** 正式服务器域名 */
	// public final static String DOMAIN
	// ="http://wuzhen.smartdot.com:8088/wenbo";
	/** 测试服务器域名 */
	public final static String DOMAIN = "https://mobile.silkroaddunhuang.com:8088";
//	 public final static String DOMAIN =
//	 "http://wuzhen.smartdot.com:8088/wenbo2";
	// public final static String DOMAIN = "http://172.20.15.14:8080/wenbo2";
//	 public final static String DOMAIN = "http://172.22.59.2:8080/wenbo2";
//	 public final static String DOMAIN = "http://192.168.43.41:8080/wenbo2";
	// zyj个人配置在这里
	public final static String apkTarget = "Download"; // apk下载路径
	public final static String sp_user = "user"; // apk下载路径
	public final static String databaseTarget = "/sdcard/wenbohui/databases/";
	// zyj 新加的设置全局接收者信息
	public static String mTargetId = "";
	public static String mTargetName = "";
	public static Conversation.ConversationType mConversationType = Conversation.ConversationType.PRIVATE;

	public static Gson gson = new GsonBuilder()
			.excludeFieldsWithoutExposeAnnotation().create();
	/**
	 * =======慧点正式服务地址 保存cookie
	 */
	public final static String domain = "mobile.silkroaddunhuang";
	// public final static String domain = "wuzhen.smartdot.com";
	// public final static String domain = "192.168.253.21";

	/** MD5加密串 */
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	/** DES加密 */
	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	public static String cookie;

	/** DES加密需要和服务器统一的加密私钥，长度不能够小于8位 */
	public static final String key = "95880288";

	/**
	 * 删除cookie
	 * 
	 * @param context
	 */
	@SuppressWarnings("deprecation")
	public static void removeCookie(Context context) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
		cookie = null;
	}

	/**
	 * 获取cookie
	 * 
	 * @param context
	 * @param domain
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getCookie(Context context, String domain) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeSessionCookie();
		cookieManager.removeExpiredCookie();
		String cookie = cookieManager.getCookie(domain);
		if (cookie == null)
			cookie = "";
		// Log.v("cookie", "---" + cookie);
		CookieSyncManager.getInstance().sync();
		return cookie;
	}

	/**
	 * 保存cookie
	 * 
	 * @param context
	 * @param domain
	 * @param con
	 */
	@SuppressWarnings("deprecation")
	public static void setCookie(Context context, String domain,
			HttpURLConnection con) {

		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeSessionCookie();
		cookieManager.setAcceptCookie(true);
		String cookieVal = "";
		String key = "";
		for (int i = 1; (key = con.getHeaderFieldKey(i)) != null; i++) {
			if (key.equalsIgnoreCase("set-cookie")) {
				cookieVal = con.getHeaderField(i);
				cookieManager.setCookie(domain, cookieVal);// 保存加密后的cookie
				System.out.println("setcookie = " + cookieVal);
			}
		}
		CookieSyncManager.getInstance().sync();
	}

	// MD5加密
	public static String getMD5(String val) {
		byte[] m = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(val.getBytes());
			m = md5.digest();// 加密
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toHexString(m);
	}

	public static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	private static final byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	/**
	 * 加密
	 * 
	 * @param encryptKey
	 * @param encryptString
	 * @return
	 */
	public static String encode(String encryptKey, String encryptString) {
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
		Cipher cipher;
		byte[] encryptedData = null;
		try {
			cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			encryptedData = cipher.doFinal(encryptString.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Base64.encode(encryptedData);
	}

	/**
	 * 解密
	 */
	public static String decode(String decryptKey, String decryptString)
			throws Exception {
		byte[] byteMi = Base64.decode(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);

		return new String(decryptedData);
	}

	/**
	 * 二行制转字符串
	 * 
	 * @param b
	 * 
	 * @return
	 */

	private static String byte2String(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toUpperCase(Locale.CHINA);
	}

	/**
	 * 根据long类型数据获取String类型时间 日期时间 年月日 时分秒
	 * 
	 * @return
	 */
	public static String getCurrentTimeToAll(long currentTime) {
		String str = "";
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日  HH:mm:ss     ");
		Date curDate = new Date(currentTime);// 获取当前时间
		System.out.println("===========long==当前时间为："
				+ System.currentTimeMillis());
		str = formatter.format(curDate);
		return str;
	}

	/**
	 * 根据long类型数据获取String类型时间 日期 年月日
	 * 
	 * @return
	 */
	public static String getCurrentTimeToDate(long currentTime) {
		String str = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(currentTime);// 获取当前时间
		System.out.println("===========long==当前时间为："
				+ System.currentTimeMillis());
		str = formatter.format(curDate);
		return str;
	}

	/**
	 * 根据long类型数据获取String类型时间 时间 时分秒
	 * 
	 * @return
	 */
	public static String getCurrentTimeToHms(long currentTime) {
		String str = "";
		SimpleDateFormat formatter = new SimpleDateFormat(" HH:mm:ss ");
		Date curDate = new Date(currentTime);// 获取当前时间
		System.out.println("===========long==当前时间为："
				+ System.currentTimeMillis());
		str = formatter.format(curDate);
		return str;
	}

	/**
	 * 根据long类型数据获取String类型时间 时间 时分
	 * 
	 * @return
	 */
	public static String getCurrentTimeToHm(long currentTime) {
		String str = "";
		SimpleDateFormat formatter = new SimpleDateFormat(" HH:mm ");
		Date curDate = new Date(currentTime);// 获取当前时间
		System.out.println("===========long==当前时间为："
				+ System.currentTimeMillis());
		str = formatter.format(curDate);
		System.out.println("===========long==当前时间为：" + str);
		return str;
	}
}
