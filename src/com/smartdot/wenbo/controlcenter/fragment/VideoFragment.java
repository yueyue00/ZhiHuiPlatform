package com.smartdot.wenbo.controlcenter.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.panframe.android.lib.PFAsset;
import com.panframe.android.lib.PFAssetObserver;
import com.panframe.android.lib.PFAssetStatus;
import com.panframe.android.lib.PFNavigationMode;
import com.panframe.android.lib.PFObjectFactory;
import com.panframe.android.lib.PFView;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.activity.FullScreenActivity;
import com.smartdot.wenbo.controlcenter.activity.MainActivity;
import com.smartdot.wenbo.controlcenter.adapter.VideoGridAdapter;
import com.smartdot.wenbo.controlcenter.bean.User;
import com.smartdot.wenbo.controlcenter.bean.VideoBean;
import com.smartdot.wenbo.controlcenter.util.MSharePreferenceUtils;

public class VideoFragment extends Fragment
// implements PFAssetObserver, OnSeekBarChangeListener
{

	private MainActivity mContext;
	private TextView tv_title;

	// ----中医药----显示webView--控件--开始
	private GridView gridView;
	private ImageView ivView;
	private WebView videoFra_wv;
	private TextView tv_video_link;
	private List<VideoBean> data;
	private VideoGridAdapter adapter;
	private String videoUrl = "http://101.201.76.73/ZYY/";// 测试地址
	// private String videoUrl = Constant.DOMAIN
	// + "/comandPlatform.do?method=hotelInfoReportTotalSeven";
	// ----中医药--显示webView---控件--开始

	// rtmp 流----控件----开始
	// private String mVideoPath =
	// "rtmp://114.112.96.184:1938/conf1/speak";//测试地址
	// private String mVideoPath = "rtmp://114.112.96.184:1936/conf1/ppt";//正式地址
//	private String mVideoPath2 = "rtmp://61.178.27.233:19350/live/gsws_sd";// 正式地址
//	private String mVideoPath3 = "rtmp://61.178.27.233:19350/live/400ybs_sd";// 正式地址
//	private String mVideoPath1 = "rtmp:/live.hkstv.hk.lxdns.com/live/hks";// 正式地址
//	private String mVideoPath2 = "rtmp:/live.hkstv.hk.lxdns.com/live/hks";// 正式地址
//	private String mVideoPath3 = "rtmp:/live.hkstv.hk.lxdns.com/live/hks";// 正式地址
//	private String mVideoPath4 = "rtmp:/live.hkstv.hk.lxdns.com/live/hks";// 正式地址
	private String mVideoPath1 = "";// 正式地址
	private String mVideoPath2 = "";// 正式地址
	private String mVideoPath3 = "";// 正式地址
	private String mVideoPath4 = "";// 正式地址
	private static final String TAG = MainActivity.class.getSimpleName();
	// 左上
	private ImageView video_iv1;
	private SurfaceView mSurfaceView1;
	private View mLoadingView1;
	private PLMediaPlayer mMediaPlayer1;
	private AVOptions mAVOptions1;
	private int mSurfaceWidth1 = 0;
	private int mSurfaceHeight1 = 0;
	private boolean mIsStopped1 = false;
	private boolean mIsActivityPaused1 = true;
	// 左下
	private ImageView video_iv2;
	private SurfaceView mSurfaceView2;
	private View mLoadingView2;
	private PLMediaPlayer mMediaPlayer2;
	private AVOptions mAVOptions2;
	private int mSurfaceWidth2 = 0;
	private int mSurfaceHeight2 = 0;
	private boolean mIsStopped2 = false;
	private boolean mIsActivityPaused2 = true;
	// 右上
	private ImageView video_iv3;
	private SurfaceView mSurfaceView3;
	private View mLoadingView3;
	private PLMediaPlayer mMediaPlayer3;
	private AVOptions mAVOptions3;
	private int mSurfaceWidth3 = 0;
	private int mSurfaceHeight3 = 0;
	private boolean mIsStopped3 = false;
	private boolean mIsActivityPaused3 = true;
	// 右下
	private ImageView video_iv4;
	private SurfaceView mSurfaceView4;
	private View mLoadingView4;
	private PLMediaPlayer mMediaPlayer4;
	private AVOptions mAVOptions4;
	private int mSurfaceWidth4 = 0;
	private int mSurfaceHeight4 = 0;
	private boolean mIsStopped4 = false;
	private boolean mIsActivityPaused4 = true;
     
	private TextView video_toast;
	private User user;
	// rtmp----控件---结束--
	@Override
	public void onAttach(Context context) {
		// TODO Auto-generated method stub
		super.onAttach(context);
		mContext = (MainActivity) getActivity();

	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		((Activity) mContext).getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.fragment_video, null);
		MSharePreferenceUtils.getAppConfig(mContext);
//		user = (User) MSharePreferenceUtils
//				.getBean(mContext, Constant.sp_user);
		user=new User();
		user.setImportant("0");
		initView(view);
		// initGridData();
		MSharePreferenceUtils.getAppConfig(mContext);
		System.out.println("==videoF=视频源1=="+Constant.VIDEOPATH_1);
		System.out.println("==videoF=视频源2=="+Constant.VIDEOPATH_2);
		System.out.println("==videoF=视频源3=="+Constant.VIDEOPATH_3);
		System.out.println("==videoF=视频源4=="+Constant.VIDEOPATH_4);
		mVideoPath1 = (String) MSharePreferenceUtils.getParam("video1", "");
		mVideoPath2 = (String) MSharePreferenceUtils.getParam("video2", "");
		mVideoPath3 = (String) MSharePreferenceUtils.getParam("video3", "");
		mVideoPath4 = (String) MSharePreferenceUtils.getParam("video4", "");
		System.out.println("=======videoFragment=====import="+user.getImportant());
		
		//适用于大屏，不区分用户均可以看视频
//				initRtmp1();
//				initRtmp2();
//				initRtmp3();
//				initRtmp4();
//		video_toast.setVisibility(view.GONE);
		
		
		
		//适用于pad 区分用户
//		if (user.getImportant().equals("1")) {//可以看视频
//			
//			setVisibilityForVideo(View.VISIBLE, View.GONE);
//			
//			initRtmp1(); 
//			initRtmp2();
//			initRtmp3();
//			initRtmp4();
//			video_toast.setVisibility(view.GONE);
//		}else {
			setVisibilityForVideo(View.GONE, View.VISIBLE);
			video_toast.setVisibility(View.VISIBLE);
			video_toast.setText(user.getToastMessage());
//		}
		

		return view;
	}
	public void setVisibilityForVideo(int visibility1,int visibility2) {
		_btn_fullScreen1.setVisibility(visibility1);
		_btn_fullScreen2.setVisibility(visibility1);
		_btn_fullScreen3.setVisibility(visibility1);
		_btn_fullScreen4.setVisibility(visibility1);
		mSurfaceView1.setVisibility(visibility1);
		mSurfaceView2.setVisibility(visibility1);
		mSurfaceView3.setVisibility(visibility1);
		mSurfaceView4.setVisibility(visibility1);
		mLoadingView1.setVisibility(visibility1);
		mLoadingView2.setVisibility(visibility1);
		mLoadingView3.setVisibility(visibility1);
		mLoadingView4.setVisibility(visibility1);
		
		video_iv1.setVisibility(visibility2);
		video_iv2.setVisibility(visibility2);
		video_iv3.setVisibility(visibility2);
		video_iv4.setVisibility(visibility2);
	}
//--------------------------rtmp-----------------------
	public void initRtmp1() {
		mSurfaceView1.getHolder().addCallback(mCallback11);
      mAVOptions1 = new AVOptions();

      int isLiveStreaming = 1;
      // the unit of timeout is ms
      mAVOptions1.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
      mAVOptions1.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
      // Some optimization with buffering mechanism when be set to 1
      mAVOptions1.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming);
      if (isLiveStreaming == 1) {
          mAVOptions1.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
      }

      // 1 -> hw codec enable, 0 -> disable [recommended]
      int codec = 1;
      mAVOptions1.setInteger(AVOptions.KEY_MEDIACODEC, codec);

      // whether start play automatically after prepared, default value is 1
      mAVOptions1.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

      AudioManager audioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
      audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		
	}
	public void initRtmp2() {
		mSurfaceView2.getHolder().addCallback(mCallback22);
      mAVOptions2 = new AVOptions();

      int isLiveStreaming = 1;
      // the unit of timeout is ms
      mAVOptions2.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
      mAVOptions2.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
      // Some optimization with buffering mechanism when be set to 1
      mAVOptions2.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming);
      if (isLiveStreaming == 1) {
          mAVOptions2.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
      }

      // 1 -> hw codec enable, 0 -> disable [recommended]
      int codec = 1;
      mAVOptions2.setInteger(AVOptions.KEY_MEDIACODEC, codec);

      // whether start play automatically after prepared, default value is 1
      mAVOptions2.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

      AudioManager audioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
      audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		
	}
	public void initRtmp3() {
		mSurfaceView3.getHolder().addCallback(mCallback33);
      mAVOptions3 = new AVOptions();

      int isLiveStreaming = 1;
      // the unit of timeout is ms
      mAVOptions3.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
      mAVOptions3.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
      // Some optimization with buffering mechanism when be set to 1
      mAVOptions3.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming);
      if (isLiveStreaming == 1) {
          mAVOptions3.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
      }

      // 1 -> hw codec enable, 0 -> disable [recommended]
      int codec = 1;
      mAVOptions3.setInteger(AVOptions.KEY_MEDIACODEC, codec);

      // whether start play automatically after prepared, default value is 1
      mAVOptions3.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

      AudioManager audioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
      audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		
	}
	public void initRtmp4() {
		mSurfaceView4.getHolder().addCallback(mCallback44);
      mAVOptions4 = new AVOptions();

      int isLiveStreaming = 1;
      // the unit of timeout is ms
      mAVOptions4.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
      mAVOptions4.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
      // Some optimization with buffering mechanism when be set to 1
      mAVOptions4.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming);
      if (isLiveStreaming == 1) {
          mAVOptions4.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
      }

      // 1 -> hw codec enable, 0 -> disable [recommended]
      int codec = 1;
      mAVOptions4.setInteger(AVOptions.KEY_MEDIACODEC, codec);

      // whether start play automatically after prepared, default value is 1
      mAVOptions4.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

      AudioManager audioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
      audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		
	}
	private SurfaceHolder.Callback mCallback11 = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            prepare1();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mSurfaceWidth1 = width;
            mSurfaceHeight1 = height;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // release();
            releaseWithoutStop1();
        }
    };
    private SurfaceHolder.Callback mCallback22 = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            prepare2();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mSurfaceWidth2 = width;
            mSurfaceHeight2 = height;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // release();
            releaseWithoutStop2();
        }
    };
    private SurfaceHolder.Callback mCallback33 = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            prepare3();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mSurfaceWidth3 = width;
            mSurfaceHeight3 = height;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // release();
            releaseWithoutStop3();
        }
    };
    private SurfaceHolder.Callback mCallback44 = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            prepare4();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mSurfaceWidth4 = width;
            mSurfaceHeight4 = height;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // release();
            releaseWithoutStop4();
        }
    };
    private void prepare1() {

        if (mMediaPlayer1 != null) {
            mMediaPlayer1.setDisplay(mSurfaceView1.getHolder());
            return;
        }

        try {
            mMediaPlayer1 = new PLMediaPlayer(mAVOptions1);
            mMediaPlayer1.setVolume(0.0f, 0.0f);
            mMediaPlayer1.setOnPreparedListener(mOnPreparedListener1);
            mMediaPlayer1.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener1);
            mMediaPlayer1.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer1.setOnErrorListener(mOnErrorListener);
            mMediaPlayer1.setOnInfoListener(mOnInfoListener1);
            mMediaPlayer1.setOnBufferingUpdateListener(mOnBufferingUpdateListener);

            // set replay if completed
            // mMediaPlayer.setLooping(true);

            mMediaPlayer1.setWakeMode(mContext.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            mMediaPlayer1.setDataSource(mVideoPath1);
            mMediaPlayer1.setDisplay(mSurfaceView1.getHolder());
            mMediaPlayer1.prepareAsync();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void prepare2() {

        if (mMediaPlayer2 != null) {
            mMediaPlayer2.setDisplay(mSurfaceView2.getHolder());
            return;
        }

        try {
            mMediaPlayer2 = new PLMediaPlayer(mAVOptions2);
            mMediaPlayer2.setVolume(0.0f, 0.0f);
            mMediaPlayer2.setOnPreparedListener(mOnPreparedListener2);
            mMediaPlayer2.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener2);
            mMediaPlayer2.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer2.setOnErrorListener(mOnErrorListener);
            mMediaPlayer2.setOnInfoListener(mOnInfoListener2);
            mMediaPlayer2.setOnBufferingUpdateListener(mOnBufferingUpdateListener);

            // set replay if completed
            // mMediaPlayer.setLooping(true);

            mMediaPlayer2.setWakeMode(mContext.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            mMediaPlayer2.setDataSource(mVideoPath2);
            mMediaPlayer2.setDisplay(mSurfaceView2.getHolder());
            mMediaPlayer2.prepareAsync();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void prepare3() {

        if (mMediaPlayer3 != null) {
            mMediaPlayer3.setDisplay(mSurfaceView3.getHolder());
            return;
        }

        try {
            mMediaPlayer3 = new PLMediaPlayer(mAVOptions3);
            mMediaPlayer3.setVolume(0.0f, 0.0f);
            mMediaPlayer3.setOnPreparedListener(mOnPreparedListener3);
            mMediaPlayer3.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener3);
            mMediaPlayer3.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer3.setOnErrorListener(mOnErrorListener);
            mMediaPlayer3.setOnInfoListener(mOnInfoListener3);
            mMediaPlayer3.setOnBufferingUpdateListener(mOnBufferingUpdateListener);

            // set replay if completed
            // mMediaPlayer.setLooping(true);

            mMediaPlayer3.setWakeMode(mContext.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            mMediaPlayer3.setDataSource(mVideoPath3);
            mMediaPlayer3.setDisplay(mSurfaceView3.getHolder());
            mMediaPlayer3.prepareAsync();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void prepare4() {

        if (mMediaPlayer4 != null) {
            mMediaPlayer4.setDisplay(mSurfaceView4.getHolder());
            return;
        }

        try {
            mMediaPlayer4 = new PLMediaPlayer(mAVOptions4);
            mMediaPlayer4.setVolume(0.0f, 0.0f);
            mMediaPlayer4.setOnPreparedListener(mOnPreparedListener4);
            mMediaPlayer4.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener4);
            mMediaPlayer4.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer4.setOnErrorListener(mOnErrorListener);
            mMediaPlayer4.setOnInfoListener(mOnInfoListener4);
            mMediaPlayer4.setOnBufferingUpdateListener(mOnBufferingUpdateListener);

            // set replay if completed
            // mMediaPlayer.setLooping(true);

            mMediaPlayer4.setWakeMode(mContext.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            mMediaPlayer4.setDataSource(mVideoPath4);
            mMediaPlayer4.setDisplay(mSurfaceView4.getHolder());
            mMediaPlayer4.prepareAsync();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void releaseWithoutStop1() {
        if (mMediaPlayer1 != null) {
            mMediaPlayer1.setDisplay(null);
        }
    }
    public void releaseWithoutStop2() {
        if (mMediaPlayer2 != null) {
            mMediaPlayer2.setDisplay(null);
        }
    }
    public void releaseWithoutStop3() {
        if (mMediaPlayer3 != null) {
            mMediaPlayer3.setDisplay(null);
        }
    }
    public void releaseWithoutStop4() {
        if (mMediaPlayer4 != null) {
            mMediaPlayer4.setDisplay(null);
        }
    }


//---------------------rtmp----------------------------
	


	/**
	 * 初始化gridview的数据
	 */
//	private void initGridData() {
//		data = new ArrayList<>();
//		data.clear();
//		for (int i = 0; i < 8; i++) {
//			VideoBean bean = new VideoBean();
//			bean.setVideoSource("监控位置" + (i + 1));
//			if (i % 2 == 0) {
//				bean.setVideoPath("http://pic35.nipic.com/20131112/12234728_102742085385_2.jpg");
//			} else {
//				bean.setVideoPath("http://pic34.nipic.com/20131021/11808404_173101103000_2.jpg");
//			}
//			data.add(bean);
//		}
//		adapter = new VideoGridAdapter(mContext, data);
//		gridView.setAdapter(adapter);
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				// Toast.makeText(mContext, "----->"+position,
//				// Toast.LENGTH_SHORT).show();
//				setImg(data.get(position).getVideoPath(), ivView);
//			}
//		});
//
//	}

	/**
	 * 给ImageView设置数据
	 * 
	 * @param path
	 * @param imageView
	 */
//	public void setImg(String path, ImageView imageView) {
//		ImageLoader imageLoader = ImageLoader.getInstance();
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showStubImage(R.drawable.bg_video)
//				.showImageForEmptyUri(R.drawable.bg_video)
//				.showImageOnFail(R.drawable.bg_video).cacheInMemory(true)
//				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
//		imageLoader.displayImage(path, imageView, options);
//	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("视频监控");
		video_toast = (TextView) view.findViewById(R.id.video_toast);
		/**
		 * 中医药使用的视频播放的布局 -----开始 gridView = (GridView)
		 * view.findViewById(R.id.videoFra_gridview); ivView = (ImageView)
		 * view.findViewById(R.id.videoFra_iv); videoFra_wv = (WebView)
		 * view.findViewById(R.id.videoFra_wv);
		 * videoFra_wv.setHorizontalScrollBarEnabled(false);// 水平滚动条不显示
		 * videoFra_wv.setVerticalScrollBarEnabled(false); // 垂直滚动条不显示
		 * videoFra_wv.getSettings().setJavaScriptEnabled(true);//
		 * 开启webview对JS的支持 videoFra_wv.getSettings().setSupportZoom(true);
		 * videoFra_wv.getSettings().setUseWideViewPort(true);
		 * videoFra_wv.getSettings().setLoadWithOverviewMode(true);
		 * videoFra_wv.getSettings().setBuiltInZoomControls(true);
		 * videoFra_wv.getSettings().setDisplayZoomControls(false);
		 * videoFra_wv.getSettings().setAllowFileAccess(true);
		 * videoFra_wv.getSettings().setAllowFileAccess(true);
		 * videoFra_wv.getSettings().setAllowContentAccess(true);
		 * videoFra_wv.loadUrl(videoUrl); tv_video_link = (TextView)
		 * view.findViewById(R.id.tv_video_link); 结束
		 */
		// rtmp流布局 开始
		// 左上
		video_iv1 = (ImageView) view.findViewById(R.id.video_iv1);
		mLoadingView1 = view.findViewById(R.id.LoadingView1_video);
		mSurfaceView1 = (SurfaceView) view.findViewById(R.id.SurfaceView1_video);
		// 左下
		video_iv2 = (ImageView) view.findViewById(R.id.video_iv2);
		mLoadingView2 = view.findViewById(R.id.LoadingView2_video);
		mSurfaceView2 = (SurfaceView) view.findViewById(R.id.SurfaceView2_video);
		// 右上
		video_iv3 = (ImageView) view.findViewById(R.id.video_iv3);
		mLoadingView3 = view.findViewById(R.id.LoadingView3_video);
		mSurfaceView3 = (SurfaceView) view.findViewById(R.id.SurfaceView3_video);
		// 右下
		video_iv4 = (ImageView) view.findViewById(R.id.video_iv4);
		mLoadingView4 = view.findViewById(R.id.LoadingView4_video);
		mSurfaceView4 = (SurfaceView) view.findViewById(R.id.SurfaceView4_video);
		// rtmp 流布局 结束

		// 全景视频控件
		// 左上
		 _frameContainer1 = (ViewGroup)
		 view.findViewById(R.id.framecontainer1);
		 _frameContainer1.setBackgroundColor(0xFF000000);
		// _touchButton1 = (Button)view.findViewById(R.id.touchbutton1);
		// touchButton1 = (Button)view.findViewById(R.id.touchbutton1);
		// _scrubber1 = (SeekBar)view.findViewById(R.id.scrubber1);
		_btn_fullScreen1 = (Button) view.findViewById(R.id.btn_fullScreen1);
		_btn_fullScreen1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, FullScreenActivity.class);
				intent.putExtra("videoPathType", "1");
				intent.putExtra("type", "videoFragment");
				startActivity(intent);
				getActivity().finish();
			}
		});
		// _touchButton1.setOnClickListener(touchListener1);
		// _scrubber1.setOnSeekBarChangeListener(this);
		//
		// _scrubber1.setEnabled(false);
		// //左下
		 _frameContainer2 = (ViewGroup)
		 view.findViewById(R.id.framecontainer2);
		 _frameContainer2.setBackgroundColor(0xFF000000);
		// _touchButton2 = (Button)view.findViewById(R.id.touchbutton2);
		// touchButton2 = (Button)view.findViewById(R.id.touchbutton2);
		// _scrubber2 = (SeekBar)view.findViewById(R.id.scrubber2);
		_btn_fullScreen2 = (Button) view.findViewById(R.id.btn_fullScreen2);
		_btn_fullScreen2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, FullScreenActivity.class);
				intent.putExtra("videoPathType", "2");
				intent.putExtra("type", "videoFragment");
				startActivity(intent);
				getActivity().finish();
			}
		});
		// _touchButton2.setOnClickListener(touchListener2);
		// _scrubber2.setOnSeekBarChangeListener(this);
		//
		// _scrubber2.setEnabled(false);
		// //右上
		 _frameContainer3 = (ViewGroup)
		 view.findViewById(R.id.framecontainer3);
		 _frameContainer3.setBackgroundColor(0xFF000000);
		// _touchButton3 = (Button)view.findViewById(R.id.touchbutton3);
		// touchButton3 = (Button)view.findViewById(R.id.touchbutton3);
		// _scrubber3 = (SeekBar)view.findViewById(R.id.scrubber3);
		_btn_fullScreen3 = (Button) view.findViewById(R.id.btn_fullScreen3);
		_btn_fullScreen3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, FullScreenActivity.class);
				intent.putExtra("videoPathType", "3");
				intent.putExtra("type", "videoFragment");
				startActivity(intent);
				getActivity().finish();
			}
		});
		// _touchButton3.setOnClickListener(touchListener3);
		// _scrubber3.setOnSeekBarChangeListener(this);
		//
		// _scrubber3.setEnabled(false);
		// //右下
		 _frameContainer4 = (ViewGroup)
		 view.findViewById(R.id.framecontainer4);
		 _frameContainer4.setBackgroundColor(0xFF000000);
		// _touchButton4 = (Button)view.findViewById(R.id.touchbutton4);
		// touchButton4 = (Button)view.findViewById(R.id.touchbutton4);
		// _scrubber4 = (SeekBar)view.findViewById(R.id.scrubber4);
		_btn_fullScreen4 = (Button) view.findViewById(R.id.btn_fullScreen4);
		_btn_fullScreen4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, FullScreenActivity.class);
				intent.putExtra("videoPathType", "4");
				intent.putExtra("type", "videoFragment");
				startActivity(intent);
				getActivity().finish();
			}
		});
		// _touchButton4.setOnClickListener(touchListener4);
		// _scrubber4.setOnSeekBarChangeListener(this);
		//
		// _scrubber4.setEnabled(false);

	}
	


	private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener1 = new PLMediaPlayer.OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(PLMediaPlayer mp, int width, int height) {
			Log.i("tag", "onVideoSizeChanged, width = " + width + ",height = "
					+ height);
			// resize the display window to fit the screen
			if (width != 0 && height != 0) {
				float ratioW = (float) width / (float) mSurfaceWidth1;
				float ratioH = (float) height / (float) mSurfaceHeight1;
				float ratio = Math.max(ratioW, ratioH);
				width = (int) Math.ceil((float) width / ratio);
				height = (int) Math.ceil((float) height / ratio);
				FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
						width, height);
				layout.gravity = Gravity.CENTER;
				mSurfaceView1.setLayoutParams(layout);
			}
		}
	};
	private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener2 = new PLMediaPlayer.OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(PLMediaPlayer mp, int width, int height) {
			Log.i("tag", "onVideoSizeChanged, width = " + width + ",height = "
					+ height);
			// resize the display window to fit the screen
			if (width != 0 && height != 0) {
				float ratioW = (float) width / (float) mSurfaceWidth2;
				float ratioH = (float) height / (float) mSurfaceHeight2;
				float ratio = Math.max(ratioW, ratioH);
				width = (int) Math.ceil((float) width / ratio);
				height = (int) Math.ceil((float) height / ratio);
				FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
						width, height);
				layout.gravity = Gravity.CENTER;
				mSurfaceView2.setLayoutParams(layout);
			}
		}
	};
	private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener3 = new PLMediaPlayer.OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(PLMediaPlayer mp, int width, int height) {
			Log.i("tag", "onVideoSizeChanged, width = " + width + ",height = "
					+ height);
			// resize the display window to fit the screen
			if (width != 0 && height != 0) {
				float ratioW = (float) width / (float) mSurfaceWidth3;
				float ratioH = (float) height / (float) mSurfaceHeight3;
				float ratio = Math.max(ratioW, ratioH);
				width = (int) Math.ceil((float) width / ratio);
				height = (int) Math.ceil((float) height / ratio);
				FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
						width, height);
				layout.gravity = Gravity.CENTER;
				mSurfaceView3.setLayoutParams(layout);
			}
		}
	};
	private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener4 = new PLMediaPlayer.OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(PLMediaPlayer mp, int width, int height) {
			Log.i("tag", "onVideoSizeChanged, width = " + width + ",height = "
					+ height);
			// resize the display window to fit the screen
			if (width != 0 && height != 0) {
				float ratioW = (float) width / (float) mSurfaceWidth4;
				float ratioH = (float) height / (float) mSurfaceHeight4;
				float ratio = Math.max(ratioW, ratioH);
				width = (int) Math.ceil((float) width / ratio);
				height = (int) Math.ceil((float) height / ratio);
				FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
						width, height);
				layout.gravity = Gravity.CENTER;
				mSurfaceView4.setLayoutParams(layout);
			}
		}
	};
	private PLMediaPlayer.OnPreparedListener mOnPreparedListener1 = new PLMediaPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(PLMediaPlayer mp) {
			Log.i("tag", "On Prepared !");
			mMediaPlayer1.start();
			mIsStopped1 = false;
		}
	};
	private PLMediaPlayer.OnPreparedListener mOnPreparedListener2 = new PLMediaPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(PLMediaPlayer mp) {
			Log.i("tag", "On Prepared !");
			mMediaPlayer2.start();
			mIsStopped2 = false;
		}
	};
	private PLMediaPlayer.OnPreparedListener mOnPreparedListener3 = new PLMediaPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(PLMediaPlayer mp) {
			Log.i("tag", "On Prepared !");
			mMediaPlayer3.start();
			mIsStopped3 = false;
		}
	};
	private PLMediaPlayer.OnPreparedListener mOnPreparedListener4 = new PLMediaPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(PLMediaPlayer mp) {
			Log.i("tag", "On Prepared !");
			mMediaPlayer4.start();
			mIsStopped4 = false;
		}
	};
	
	private PLMediaPlayer.OnInfoListener mOnInfoListener1 = new PLMediaPlayer.OnInfoListener() {
		@Override
		public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
			Log.i("tag", "OnInfo, what = " + what + ", extra = " + extra);
			switch (what) {
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
				mLoadingView1.setVisibility(View.VISIBLE);
				break;
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
			case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
				mLoadingView1.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			return true;
		}
	};
	private PLMediaPlayer.OnInfoListener mOnInfoListener2 = new PLMediaPlayer.OnInfoListener() {
		@Override
		public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
			Log.i("tag", "OnInfo, what = " + what + ", extra = " + extra);
			switch (what) {
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
				mLoadingView2.setVisibility(View.VISIBLE);
				break;
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
			case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
				mLoadingView2.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			return true;
		}
	};
	private PLMediaPlayer.OnInfoListener mOnInfoListener3 = new PLMediaPlayer.OnInfoListener() {
		@Override
		public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
			Log.i("tag", "OnInfo, what = " + what + ", extra = " + extra);
			switch (what) {
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
				mLoadingView3.setVisibility(View.VISIBLE);
				break;
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
			case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
				mLoadingView3.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			return true;
		}
	};
	private PLMediaPlayer.OnInfoListener mOnInfoListener4 = new PLMediaPlayer.OnInfoListener() {
		@Override
		public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
			Log.i("tag", "OnInfo, what = " + what + ", extra = " + extra);
			switch (what) {
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
				mLoadingView4.setVisibility(View.VISIBLE);
				break;
			case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
			case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
				mLoadingView4.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			return true;
		}
	};
	private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
		@Override
		public void onBufferingUpdate(PLMediaPlayer mp, int percent) {
			Log.d("tag", "onBufferingUpdate: " + percent + "%");
		}
	};

	/**
	 * Listen the event of playing complete For playing local file, it's called
	 * when reading the file EOF For playing network stream, it's called when
	 * the buffered bytes played over
	 * 
	 * If setLooping(true) is called, the player will restart automatically And
	 * ｀onCompletion｀ will not be called
	 * 
	 */
	private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
		@Override
		public void onCompletion(PLMediaPlayer mp) {
			Log.d("tag", "Play Completed !");
			// showToastTips("Play Completed !");
			// ((Activity) mContext).finish();
		}
	};

	private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
		@Override
		public boolean onError(PLMediaPlayer mp, int errorCode) {
			Log.e("tag", "Error happened, errorCode = " + errorCode);
			switch (errorCode) {
			case PLMediaPlayer.ERROR_CODE_INVALID_URI:
				// showToastTips("Invalid URL !");
				break;
			case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
				// showToastTips("404 resource not found !");
				break;
			case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
				// showToastTips("Connection refused !");
				break;
			case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
				// showToastTips("Connection timeout !");
				break;
			case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
				// showToastTips("Empty playlist !");
				break;
			case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
				// showToastTips("Stream disconnected !");
				break;
			case PLMediaPlayer.ERROR_CODE_IO_ERROR:
				// showToastTips("Network IO Error !");
				break;
			case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
				// showToastTips("Unauthorized Error !");
				break;
			case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
				// showToastTips("Prepare timeout !");
				break;
			case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
				// showToastTips("Read frame timeout !");
				break;
			case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
			default:
				// showToastTips("unknown error !");
				break;
			}
			// Todo pls handle the error status here, retry or call finish()
			// The PLMediaPlayer has moved to the Error state, if you want to
			// retry, must reset first !
			// try {
			// mMediaPlayer.reset();
			// mMediaPlayer.setDisplay(mSurfaceView.getHolder());
			// mMediaPlayer.setDataSource(mVideoPath);
			// mMediaPlayer.prepareAsync();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// Return true means the error has been handled
			// If return false, then `onCompletion` will be called
			return true;
		}
	};
//	 private void showToastTips(final String tips) {
//	 if (mIsActivityPaused) {
//	 return;
//	 }
//	 runOnUiThread(new Runnable() {
//	 @Override
//	 public void run() {
//	 Toast.makeText(mContext, tips, Toast.LENGTH_LONG).show();
//	 }
//	 });
//	 }

	// ===================全景视频===开始==
	// PFView _pfview1;
	// PFView _pfview2;
	// PFView _pfview3;
	// PFView _pfview4;
	// PFAsset _pfasset1;
	// PFAsset _pfasset2;
	// PFAsset _pfasset3;
	// PFAsset _pfasset4;
	// PFNavigationMode _currentNavigationMode1 = PFNavigationMode.MOTION;
	// PFNavigationMode _currentNavigationMode2 = PFNavigationMode.MOTION;
	// PFNavigationMode _currentNavigationMode3 = PFNavigationMode.MOTION;
	// PFNavigationMode _currentNavigationMode4 = PFNavigationMode.MOTION;
	// String videoPath1 =
	// "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
	// String videoPath2 =
	// "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
	// String videoPath3 =
	// "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
	// String videoPath4 =
	// "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
	//
	// boolean _updateThumb = true;
	// Timer _scrubberMonitorTimer;
	// //左上
	 ViewGroup _frameContainer1;
	// Button _touchButton1;
	// SeekBar _scrubber1;
	// Button touchButton1;
	Button _btn_fullScreen1;
	// //左下
	 ViewGroup _frameContainer2;
	// Button _touchButton2;
	// SeekBar _scrubber2;
	// Button touchButton2;
	Button _btn_fullScreen2;
	// //右上
	 ViewGroup _frameContainer3;
	// Button _touchButton3;
	// SeekBar _scrubber3;
	// Button touchButton3;
	Button _btn_fullScreen3;
	// //右下
	 ViewGroup _frameContainer4;
	// Button _touchButton4;
	// SeekBar _scrubber4;
	// Button touchButton4;
	Button _btn_fullScreen4;

	//
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// initPF1(videoPath1);
		// initPF2(videoPath2);
		// initPF3(videoPath3);
		// initPF4(videoPath4);
		// // loadVideo("http://114.112.96.184:8000/conf1/fullview.m3u8");
		// loadVideo(_frameContainer1, _pfview1, _pfasset1,
		// _currentNavigationMode1, videoPath1);
		// loadVideo(_frameContainer2, _pfview2, _pfasset2,
		// _currentNavigationMode2, videoPath2);
		// loadVideo(_frameContainer3, _pfview3, _pfasset3,
		// _currentNavigationMode3, videoPath3);
		// loadVideo(_frameContainer4, _pfview4, _pfasset4,
		// _currentNavigationMode4, videoPath4);
		// showControls(touchButton1, _pfview1,_scrubber1,true);
		// showControls(touchButton2, _pfview2,_scrubber2,true);
		// showControls(touchButton3, _pfview3,_scrubber3,true);
		// showControls(touchButton4, _pfview4,_scrubber4,true);
		// _pfasset1.play();
		// _pfasset2.play();
		// _pfasset3.play();
		// _pfasset4.play();

	}

	// public void initPF1(String filename) {
	// _pfview1 = PFObjectFactory.view(getActivity());
	// _pfasset1 = PFObjectFactory.assetFromUri(getActivity(),
	// Uri.parse(filename),
	// this);
	// }
	//
	// public void initPF2(String filename) {
	// _pfview2 = PFObjectFactory.view(getActivity());
	// _pfasset2 = PFObjectFactory.assetFromUri(getActivity(),
	// Uri.parse(filename),
	// this);
	// }
	//
	// public void initPF3(String filename) {
	// _pfview3 = PFObjectFactory.view(getActivity());
	// _pfasset3 = PFObjectFactory.assetFromUri(getActivity(),
	// Uri.parse(filename),
	// this);
	// }
	//
	// public void initPF4(String filename) {
	// _pfview4 = PFObjectFactory.view(getActivity());
	// _pfasset4 = PFObjectFactory.assetFromUri(getActivity(),
	// Uri.parse(filename),
	// this);
	// }

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// _pfasset1.stop();
		// _pfasset2.stop();
		// _pfasset3.stop();
		// _pfasset4.stop();
	}

	/**
	 * Show/Hide the playback controls
	 * 
	 * @param bShow
	 *            Show or hide the controls. Pass either true or false.
	 */
	// public void showControls(Button touchButton,PFView pfView,SeekBar
	// seekBar,boolean bShow)
	// {
	// int visibility = View.GONE;
	//
	// if (bShow)
	// visibility = View.VISIBLE;
	//
	// // _playButton.setVisibility(visibility);
	// // _stopButton.setVisibility(visibility);
	// touchButton.setVisibility(visibility);
	// seekBar.setVisibility(visibility);
	//
	// if (pfView != null)
	// {
	// if (!pfView.supportsNavigationMode(PFNavigationMode.MOTION))
	// touchButton.setVisibility(View.GONE);
	// }
	// }

	/**
	 * Start the video with a local file path
	 * 
	 * @param filename
	 *            The file path on device storage
	 */
	// public void loadVideo(ViewGroup _frameContainer,PFView pfView,PFAsset
	// pfAsset,PFNavigationMode _currentNavigationMode,String filename)
	// {
	//
	// // _pfview1 = PFObjectFactory.view(getActivity());
	// // _pfasset1 = PFObjectFactory.assetFromUri(getActivity(),
	// Uri.parse(filename), this);
	//
	// pfView.displayAsset(pfAsset);
	// pfView.setNavigationMode(_currentNavigationMode);
	//
	// _frameContainer.addView(pfView.getView(), 0);
	//
	// }

	/**
	 * Status callback from the PFAsset instance. Based on the status this
	 * function selects the appropriate action.
	 * 
	 * @param asset
	 *            The asset who is calling the function
	 * @param status
	 *            The current status of the asset.
	 */
	// public void onStatusMessage(final PFAsset asset, PFAssetStatus status) {
	// switch (status)
	// {
	// case LOADED:
	// Log.d("SimplePlayer", "Loaded");
	// break;
	// case DOWNLOADING:
	// // Log.d("SimplePlayer",
	// "Downloading 360� movie: "+_pfasset.getDownloadProgress()+" percent complete");
	// break;
	// case DOWNLOADED:
	// Log.d("SimplePlayer", "Downloaded to "+asset.getUrl());
	// break;
	// case DOWNLOADCANCELLED:
	// Log.d("SimplePlayer", "Download cancelled");
	// break;
	// case PLAYING:
	// Log.d("SimplePlayer", "Playing");
	// mContext.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	// _scrubber1.setEnabled(true);
	// _scrubber2.setEnabled(true);
	// _scrubber3.setEnabled(true);
	// _scrubber4.setEnabled(true);
	// // _playButton.setText("pause");
	// _scrubberMonitorTimer = new Timer();
	// final TimerTask task = new TimerTask() {
	// public void run() {
	// if (_updateThumb)
	// {
	// _scrubber1.setMax((int) asset.getDuration());
	// _scrubber1.setProgress((int) asset.getPlaybackTime());
	//
	// _scrubber2.setMax((int) asset.getDuration());
	// _scrubber2.setProgress((int) asset.getPlaybackTime());
	//
	// _scrubber3.setMax((int) asset.getDuration());
	// _scrubber3.setProgress((int) asset.getPlaybackTime());
	//
	// _scrubber4.setMax((int) asset.getDuration());
	// _scrubber4.setProgress((int) asset.getPlaybackTime());
	// }
	// }
	// };
	// _scrubberMonitorTimer.schedule(task, 0, 33);
	// break;
	// case PAUSED:
	// Log.d("SimplePlayer", "Paused");
	// // _playButton.setText("play");
	// break;
	// /* case STOPPED:
	// Log.d("SimplePlayer", "Stopped");
	// _playButton.setText("play");
	// _scrubberMonitorTimer.cancel();
	// _scrubberMonitorTimer = null;
	// _scrubber.setProgress(0);
	// _scrubber.setEnabled(false);
	// getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	// break;*/
	// case COMPLETE:
	// Log.d("SimplePlayer", "Complete");
	// // _playButton.setText("play");
	// _scrubberMonitorTimer.cancel();
	// _scrubberMonitorTimer = null;
	// getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	// break;
	// case ERROR:
	// Log.d("SimplePlayer", "Error");
	// break;
	// }
	// }

	/**
	 * Click listener for the play/pause button
	 * 
	 */
	/*
	 * private OnClickListener playListener = new OnClickListener() { public
	 * void onClick(View v) { if (_pfasset.getStatus() == PFAssetStatus.PLAYING)
	 * { _pfasset.pause(); } else _pfasset.play(); } };
	 */

	/**
	 * Click listener for the stop/back button
	 * 
	 */
	/*
	 * private OnClickListener stopListener = new OnClickListener() {
	 * 
	 * public void onClick(View v) { _pfasset.stop(); } };
	 */

	/**
	 * Click listener for the navigation mode (touch/motion (if available))
	 * 
	 */
	// private OnClickListener touchListener1 = new OnClickListener() {
	// public void onClick(View v) {
	// if (_pfview1 != null)
	// {
	//
	// if (_currentNavigationMode1 == PFNavigationMode.TOUCH)
	// {
	// _currentNavigationMode1 = PFNavigationMode.MOTION;
	// touchButton1.setText("方向感应");
	// }
	// else
	// {
	// _currentNavigationMode1 = PFNavigationMode.TOUCH;
	// touchButton1.setText("手势触控");
	// }
	// _pfview1.setNavigationMode(_currentNavigationMode1);
	// }
	// }
	// };
	// private OnClickListener touchListener2 = new OnClickListener() {
	// public void onClick(View v) {
	// if (_pfview2 != null)
	// {
	//
	// if (_currentNavigationMode2 == PFNavigationMode.TOUCH)
	// {
	// _currentNavigationMode2 = PFNavigationMode.MOTION;
	// touchButton2.setText("方向感应");
	// }
	// else
	// {
	// _currentNavigationMode2 = PFNavigationMode.TOUCH;
	// touchButton2.setText("手势触控");
	// }
	// _pfview2.setNavigationMode(_currentNavigationMode2);
	// }
	// }
	// };
	// private OnClickListener touchListener3 = new OnClickListener() {
	// public void onClick(View v) {
	// if (_pfview3 != null)
	// {
	//
	// if (_currentNavigationMode3 == PFNavigationMode.TOUCH)
	// {
	// _currentNavigationMode3 = PFNavigationMode.MOTION;
	// touchButton3.setText("方向感应");
	// }
	// else
	// {
	// _currentNavigationMode3 = PFNavigationMode.TOUCH;
	// touchButton3.setText("手势触控");
	// }
	// _pfview3.setNavigationMode(_currentNavigationMode3);
	// }
	// }
	// };
	// private OnClickListener touchListener4 = new OnClickListener() {
	// public void onClick(View v) {
	// if (_pfview4 != null)
	// {
	//
	// if (_currentNavigationMode4 == PFNavigationMode.TOUCH)
	// {
	// _currentNavigationMode4 = PFNavigationMode.MOTION;
	// touchButton4.setText("方向感应");
	// }
	// else
	// {
	// _currentNavigationMode4 = PFNavigationMode.TOUCH;
	// touchButton4.setText("手势触控");
	// }
	// _pfview4.setNavigationMode(_currentNavigationMode4);
	// }
	// }
	// };
	/**
	 * Setup the options menu
	 * 
	 * @param menu
	 *            The options menu
	 */
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.activity_main, menu);
	// return true;
	// }

	/**
	 * Called when pausing the app. This function pauses the playback of the
	 * asset when it is playing.
	 * 
	 */
	// public void onPause() {
	// super.onPause();
	// if (_pfasset1 != null)
	// {
	// if (_pfasset1.getStatus() == PFAssetStatus.PLAYING)
	// _pfasset1.pause();
	// }
	// }

	/**
	 * Called when a previously created loader is being reset, and thus making
	 * its data unavailable.
	 * 
	 * @param seekbar
	 *            The SeekBar whose progress has changed
	 * @param progress
	 *            The current progress level.
	 * @param fromUser
	 *            True if the progress change was initiated by the user.
	 * 
	 */
	public void onProgressChanged(SeekBar seekbar, int progress,
			boolean fromUser) {
	}

	/**
	 * Notification that the user has started a touch gesture. In this function
	 * we signal the timer not to update the playback thumb while we are
	 * adjusting it.
	 * 
	 * @param seekbar
	 *            The SeekBar in which the touch gesture began
	 * 
	 */
	public void onStartTrackingTouch(SeekBar seekbar) {
		// _updateThumb = false;
	}

	/**
	 * Notification that the user has finished a touch gesture. In this function
	 * we request the asset to seek until a specific time and signal the timer
	 * to resume the update of the playback thumb based on playback.
	 * 
	 * @param seekbar
	 *            The SeekBar in which the touch gesture began
	 * 
	 */
	public void onStopTrackingTouch(SeekBar seekbar) {
		// _updateThumb = true;
	}
	// ============全景视频----结束

}
