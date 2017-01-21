package com.smartdot.wenbo.controlcenter.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.smartdot.wenbo.controlcenter.R;

@SuppressLint("SetJavaScriptEnabled")
public class HomeScheduleFragment extends Fragment {

	private Context mContext;
	WebView schdule_web;
	ProgressBar myProgressBar;

	@Override
	public void onAttach(Context context) {
		// TODO Auto-generated method stub
		super.onAttach(context);
		mContext = getActivity();
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = LayoutInflater.from(mContext).inflate(
				R.layout.fragment_schdule_home, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		schdule_web = (WebView) view.findViewById(R.id.schdule_web);
		// schdule_web.getBackground().setAlpha(150);
		myProgressBar = (ProgressBar) view.findViewById(R.id.myProgressBar);
		schdule_web.getSettings().setJavaScriptEnabled(true);
		schdule_web.getSettings().setSupportZoom(true);

//		schdule_web.getSettings().setUseWideViewPort(true);
//		schdule_web.getSettings().setLoadWithOverviewMode(true);

		schdule_web.getSettings().setBuiltInZoomControls(true);
		schdule_web.getSettings().setDisplayZoomControls(false);
		// 扩大比例的缩放
		schdule_web.getSettings().setUseWideViewPort(true);
		// 自适应屏幕
		schdule_web.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.SINGLE_COLUMN);
		schdule_web.getSettings().setLoadWithOverviewMode(true);
		schdule_web.setWebChromeClient(new WebChromeClient() {// 为webview添加进度条
					@Override
					public void onProgressChanged(WebView view, int newProgress) {
						if (newProgress == 100) {
							myProgressBar.setVisibility(View.INVISIBLE);
						} else {
							if (View.INVISIBLE == myProgressBar.getVisibility()) {
								myProgressBar.setVisibility(View.VISIBLE);
							}
							myProgressBar.setProgress(newProgress);
						}
						super.onProgressChanged(view, newProgress);
					}

				});
		String jiudian = "http://192.168.1.145:8080/wenbo2/comandPlatform.do?method=hotelInfoReport";
		String jiabin = "http://192.168.1.145:8080/wenbo2/comandPlatform.do?method=memberInfoReport";
		String lianluoren = "http://192.168.1.145:8080/wenbo2/comandPlatform.do?method=connectPersonInfoReport";
		String cheliang = "http://192.168.1.145:8080/wenbo2/comandPlatform.do?method=carInfoReport";
		schdule_web.loadUrl(jiabin);

	}
}
