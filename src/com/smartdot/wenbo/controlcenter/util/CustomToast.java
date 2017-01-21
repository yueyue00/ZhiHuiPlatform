package com.smartdot.wenbo.controlcenter.util;

import com.smartdot.wenbo.controlcenter.R;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



/**
 * 此类是Toast的工具类
 */
public class CustomToast {

	private static Toast mToast;
	private static Toast jibuToast;
	private static Handler mHandler = new Handler();
	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};

	private static Runnable jr = new Runnable() {
		public void run() {
			jibuToast.cancel();
		}
	};

	/**
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param text
	 *            显示的文本
	 * @param duration
	 *            持续时间 单位:毫秒
	 */
	public static void showToast(Context mContext, String text, int duration) {

		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(text);
		else
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		mHandler.postDelayed(r, duration);

		mToast.show();
	}

	/**
	 * 图表专用的toast
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param text
	 *            显示的文本
	 * @param duration
	 *            持续时间 单位:毫秒
	 * @param x
	 *            方向x轴
	 * @param y
	 *            方向y轴
	 */
	public static void showToast(Context mContext, String text, int duration, int x, int y) {
		mHandler.removeCallbacks(jr);
		if (jibuToast == null)
			jibuToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		mHandler.postDelayed(jr, duration);
		View view = LayoutInflater.from(mContext).inflate(R.layout.toast, null);
		TextView textView = (TextView) view.findViewById(R.id.toasttext);
		textView.setText(text);
		jibuToast.setView(view);
		jibuToast.setGravity(Gravity.LEFT | Gravity.TOP, x - (textView.getWidth() / 2), y);
		jibuToast.show();
	}

	/**
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param text
	 *            显示的文本
	 * @param duration
	 *            持续时间 单位:毫秒
	 */
	public static void showToast(Context mContext, String text) {

		mHandler.removeCallbacks(r);
		if (mToast != null) {
			try {
				mToast.setText(text);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		} else {
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		}
		mHandler.postDelayed(r, 3000);

		mToast.show();
	}

	/** 如果未来把提示信息放在strings.xml 可以调用此方法 */
	public static void showToast(Context mContext, int resId, int duration) {
		showToast(mContext, mContext.getResources().getString(resId), duration);
	}

	/**
	 * 在非UI线程中显示Toast public void showToast2(Context Context,String text) {
	 * Looper.prepare(); Toast.makeText(Context, text,
	 * Toast.LENGTH_SHORT).show(); Looper.loop(); }
	 * 
	 */

}
