package com.smartdot.wenbo.controlcenter.activity;

import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.R.layout;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class XsbActivity extends Activity {
    private TextView xsb_colose;
    private WebView xsb_wv;
    private String xsbUrl = Constant.DOMAIN+"/comandPlatform.do?method=hourReportTotal";//小时报url
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_xsb);
		initView();
	}

	private void initView() {
		xsb_colose = (TextView)findViewById(R.id.xsb_colose_ac);
		xsb_wv = (WebView)findViewById(R.id.xsb_wv_ac);
		xsb_wv.setHorizontalScrollBarEnabled(false);// 水平滚动条不显示
		xsb_wv.setVerticalScrollBarEnabled(false); // 垂直滚动条不显示
		xsb_wv.setOverScrollMode(View.OVER_SCROLL_NEVER);
		xsb_wv.getSettings().setJavaScriptEnabled(true);// 开启webview对JS的支持
		xsb_wv.getSettings().setSupportZoom(true);
		// xsb_wv.setBackgroundColor(0); // 设置背景色
		// xsb_wv.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
		xsb_wv.getSettings().setUseWideViewPort(true);
		xsb_wv.getSettings().setLoadWithOverviewMode(true);

		xsb_wv.getSettings().setBuiltInZoomControls(true);
		xsb_wv.getSettings().setDisplayZoomControls(false);

		xsb_wv.getSettings().setAllowFileAccess(true);
		xsb_wv.getSettings().setAllowFileAccess(true);
		xsb_wv.getSettings().setAllowContentAccess(true);
		xsb_wv.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		xsb_wv.loadUrl(xsbUrl);
		xsb_colose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				xsbUrl = "http://www.baidu.com";
//				closePopupWindow();
//				xsb_wv.reload();
				finish();
			}
		});
	}
}
