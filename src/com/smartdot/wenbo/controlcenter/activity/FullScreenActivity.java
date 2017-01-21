package com.smartdot.wenbo.controlcenter.activity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.panframe.android.lib.PFAsset;
import com.panframe.android.lib.PFAssetObserver;
import com.panframe.android.lib.PFAssetStatus;
import com.panframe.android.lib.PFNavigationMode;
import com.panframe.android.lib.PFObjectFactory;
import com.panframe.android.lib.PFView;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.R.layout;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.util.MSharePreferenceUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class FullScreenActivity extends Activity 
//implements PFAssetObserver, OnSeekBarChangeListener 
{

	private Context mContext;
	private String type = "";
	private String videoPathType = "";
//	private String videoPathForFullScreen1 = "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
//	private String videoPathForFullScreen2 = "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
//	private String videoPathForFullScreen3 = "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
//	private String videoPathForFullScreen4 = "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
//	
//	PFView _pfview;
//	PFAsset _pfasset;
//	PFNavigationMode _currentNavigationMode = PFNavigationMode.MOTION;
//
//	boolean _updateThumb = true;
//	Timer _scrubberMonitorTimer;
//
//	ViewGroup _frameContainer;
//	// Button _stopButton;
//	// Button _playButton;
//	Button _touchButton;
	Button full_exit_fullScreen;
//	SeekBar _scrubber;
    //rtmp 流
	private static final String TAG = FullScreenActivity.class.getSimpleName();

    private SurfaceView mSurfaceView;
    private PLMediaPlayer mMediaPlayer;
    private View mLoadingView;
    private AVOptions mAVOptions;

    private int mSurfaceWidth  = 0;
    private int mSurfaceHeight = 0;
    private String mVideoPath = "";
//	private String mVideoPath2 = "rtmp://61.178.27.233:19350/live/gsws_sd";// 正式地址
//	private String mVideoPath3 = "rtmp://61.178.27.233:19350/live/400ybs_sd";// 正式地址
//    private String mVideoPath1 = "rtmp:/live.hkstv.hk.lxdns.com/live/hks";// 正式地址
//    private String mVideoPath2 = "rtmp:/live.hkstv.hk.lxdns.com/live/hks";// 正式地址
//	private String mVideoPath3 = "rtmp:/live.hkstv.hk.lxdns.com/live/hks";// 正式地址
//	private String mVideoPath4 = "rtmp:/live.hkstv.hk.lxdns.com/live/hks";// 正式地址
    
//    private String mVideoPath1 = "";// 正式地址
//    private String mVideoPath2 = "";// 正式地址
//	private String mVideoPath3 = "";// 正式地址
//	private String mVideoPath4 = "";// 正式地址
    
    private boolean mIsStopped = false;
    private boolean mIsActivityPaused = true;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_full_screen);
		mContext = this;
		MSharePreferenceUtils.getAppConfig(mContext);
		System.out.println("==full=视频源1=="+Constant.VIDEOPATH_1);
		System.out.println("==full=视频源2=="+Constant.VIDEOPATH_2);
		System.out.println("==full=视频源3=="+Constant.VIDEOPATH_3);
		System.out.println("==full=视频源4=="+Constant.VIDEOPATH_4);
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		videoPathType = intent.getStringExtra("videoPathType");
		initView();
		initRtmp();
//		initQuanjing();
		System.out.println("=========full----onCreate");
	}
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	System.out.println("=========full----onStart");
    }
    
	private void initRtmp() {
		//rtmp 流
		
        mSurfaceView.getHolder().addCallback(mCallback);
		if (videoPathType.equals("1")) {
			// mVideoPath = "rtmp:/live.hkstv.hk.lxdns.com/live/hks ";//正式地址
//			mVideoPath = Constant.VIDEOPATH_1;
			mVideoPath = (String) MSharePreferenceUtils.getParam("video1", "");
		} else if (videoPathType.equals("2")) {
//			mVideoPath = Constant.VIDEOPATH_2;
			mVideoPath = (String) MSharePreferenceUtils.getParam("video2", "");
		} else if (videoPathType.equals("3")) {
//			mVideoPath = Constant.VIDEOPATH_3;
			mVideoPath = (String) MSharePreferenceUtils.getParam("video3", "");
		} else if (videoPathType.equals("4")) {
//			mVideoPath = Constant.VIDEOPATH_4;
			mVideoPath = (String) MSharePreferenceUtils.getParam("video4", "");
		}
      

      mAVOptions = new AVOptions();

      int isLiveStreaming = 1;
      // the unit of timeout is ms
      mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
      mAVOptions.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
      // Some optimization with buffering mechanism when be set to 1
      mAVOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming);
      if (isLiveStreaming == 1) {
          mAVOptions.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
      }

      // 1 -> hw codec enable, 0 -> disable [recommended]
      int codec = 1;
      mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, codec);

      // whether start play automatically after prepared, default value is 1
      mAVOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

      AudioManager audioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
      audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

	}

//	private void initQuanjing() {
	// TODO Auto-generated method stub
//		if (videoPathType.equals("1")) {
//		loadVideo(videoPathForFullScreen1);
//	}else if (videoPathType.equals("2")) {
//		loadVideo(videoPathForFullScreen2);
//	}else if (videoPathType.equals("3")) {
//		loadVideo(videoPathForFullScreen3);
//	}else if (videoPathType.equals("4")) {
//		loadVideo(videoPathForFullScreen3);
//	}
	
//	loadVideo("http://114.112.96.184:8000/conf1/fullview.m3u8");
//	showControls(true);
//	_pfasset.play();
//}

	private void initView() {
//		_frameContainer = (ViewGroup) findViewById(R.id.full_framecontainer);
//		_frameContainer.setBackgroundColor(0xFF000000);
//
//		_touchButton = (Button) findViewById(R.id.full_touchbutton);
//		_scrubber = (SeekBar) findViewById(R.id.full_scrubber);
//		_touchButton.setOnClickListener(touchListener);
//		_scrubber.setOnSeekBarChangeListener(this);
//
//		_scrubber.setEnabled(false);
		mLoadingView = findViewById(R.id.LoadingView_full);
        mSurfaceView = (SurfaceView) findViewById(R.id.SurfaceView_full);
		full_exit_fullScreen = (Button) findViewById(R.id.full_exit_fullScreen);
		full_exit_fullScreen.setOnClickListener(new OnClickListener() {//退出全屏
			
			@Override
			public void onClick(View v) {
				if (type.equals("videoFragment")) {
					
	            	  backToVideoFragment();
				}else if (type.equals("HomeNewActivity")) {
					backToHomeNewActivity();
				}else if (type.equals("HomeForBigActivity")) {
					backToHomeForBigActivity();
				}
			}
		});
		
     
	}

	/**
	 * Show/Hide the playback controls
	 * 
	 * @param bShow
	 *            Show or hide the controls. Pass either true or false.
	 */
//	public void showControls(boolean bShow) {
//		int visibility = View.GONE;
//
//		if (bShow)
//			visibility = View.VISIBLE;
//
//		_touchButton.setVisibility(visibility);
//		_scrubber.setVisibility(visibility);
//
//		if (_pfview != null) {
//			if (!_pfview.supportsNavigationMode(PFNavigationMode.MOTION))
//				_touchButton.setVisibility(View.VISIBLE);
//		}
//	}

	/**
	 * Start the video with a local file path
	 * 
	 * @param filename
	 *            The file path on device storage
	 */
//	public void loadVideo(String filename) {
//
//		_pfview = PFObjectFactory.view(this);
//		_pfasset = PFObjectFactory
//				.assetFromUri(this, Uri.parse(filename), this);
//
//		_pfview.displayAsset(_pfasset);
//		_pfview.setNavigationMode(_currentNavigationMode);
//
//		_frameContainer.addView(_pfview.getView(), 0);
//
//	}

	/**
	 * Status callback from the PFAsset instance. Based on the status this
	 * function selects the appropriate action.
	 * 
	 * @param asset
	 *            The asset who is calling the function
	 * @param status
	 *            The current status of the asset.
	 */
//	public void onStatusMessage(final PFAsset asset, PFAssetStatus status) {
//		switch (status) {
//		case LOADED:
//			Log.d("SimplePlayer", "Loaded");
//			break;
//		case DOWNLOADING:
//			Log.d("SimplePlayer",
//					"Downloading 360� movie: " + _pfasset.getDownloadProgress()
//							+ " percent complete");
//			break;
//		case DOWNLOADED:
//			Log.d("SimplePlayer", "Downloaded to " + asset.getUrl());
//			break;
//		case DOWNLOADCANCELLED:
//			Log.d("SimplePlayer", "Download cancelled");
//			break;
//		case PLAYING:
//			Log.d("SimplePlayer", "Playing");
//			getWindow()
//					.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//			_scrubber.setEnabled(true);
//			_scrubberMonitorTimer = new Timer();
//			final TimerTask task = new TimerTask() {
//				public void run() {
//					if (_updateThumb) {
//						_scrubber.setMax((int) asset.getDuration());
//						_scrubber.setProgress((int) asset.getPlaybackTime());
//					}
//				}
//			};
//			_scrubberMonitorTimer.schedule(task, 0, 33);
//			break;
//		case PAUSED:
//			Log.d("SimplePlayer", "Paused");
//			break;
//		/*
//		 * case STOPPED: Log.d("SimplePlayer", "Stopped");
//		 * _playButton.setText("play"); _scrubberMonitorTimer.cancel();
//		 * _scrubberMonitorTimer = null; _scrubber.setProgress(0);
//		 * _scrubber.setEnabled(false);
//		 * getWindow().clearFlags(WindowManager.LayoutParams
//		 * .FLAG_KEEP_SCREEN_ON); break;
//		 */
//		case COMPLETE:
//			Log.d("SimplePlayer", "Complete");
//			_scrubberMonitorTimer.cancel();
//			_scrubberMonitorTimer = null;
//			getWindow().clearFlags(
//					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//			break;
//		case ERROR:
//			Log.d("SimplePlayer", "Error");
//			break;
//		}
//	}

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
//	private OnClickListener touchListener = new OnClickListener() {
//		public void onClick(View v) {
//			if (_pfview != null) {
//				Button touchButton = (Button) findViewById(R.id.full_touchbutton);
//				if (_currentNavigationMode == PFNavigationMode.TOUCH) {
//					_currentNavigationMode = PFNavigationMode.MOTION;
//					touchButton.setText("方向感应");
//				} else {
//					_currentNavigationMode = PFNavigationMode.TOUCH;
//					touchButton.setText("手势触控");
//				}
//				_pfview.setNavigationMode(_currentNavigationMode);
//			}
//		}
//	};

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
//	public void onPause() {
//		super.onPause();
//		if (_pfasset != null) {
//			if (_pfasset.getStatus() == PFAssetStatus.PLAYING)
//				_pfasset.pause();
//		}
//	}

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
//	public void onProgressChanged(SeekBar seekbar, int progress,
//			boolean fromUser) {
//	}

	/**
	 * Notification that the user has started a touch gesture. In this function
	 * we signal the timer not to update the playback thumb while we are
	 * adjusting it.
	 * 
	 * @param seekbar
	 *            The SeekBar in which the touch gesture began
	 * 
	 */
//	public void onStartTrackingTouch(SeekBar seekbar) {
//		_updateThumb = false;
//	}

	/**
	 * Notification that the user has finished a touch gesture. In this function
	 * we request the asset to seek until a specific time and signal the timer
	 * to resume the update of the playback thumb based on playback.
	 * 
	 * @param seekbar
	 *            The SeekBar in which the touch gesture began
	 * 
	 */
//	public void onStopTrackingTouch(SeekBar seekbar) {
//		_updateThumb = true;
//	}
	//rtmp 流
	

	    @Override
	    protected void onResume() {
	        super.onResume();
	        mIsActivityPaused = false;
	        System.out.println("=========full----onResume");
	    }
	    
	    @Override
	    protected void onPause() {
	        super.onPause();
	        mIsActivityPaused = true;
	        System.out.println("=========full----onPause");
	    }
	    @Override
	    protected void onStop() {
	    	// TODO Auto-generated method stub
	    	super.onStop();
	    	System.out.println("=========full----onStop");
	    }
	    @Override
	    protected void onRestart() {
	    	// TODO Auto-generated method stub
	    	super.onRestart();
	    	System.out.println("=========full----onRestart");
	    }
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        release();
	        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	        audioManager.abandonAudioFocus(null);
	        System.out.println("=========full----onDestory");
	    }
	    public void releaseWithoutStop() {
	        if (mMediaPlayer != null) {
	            mMediaPlayer.setDisplay(null);
	        }
	    }

	    public void release() {
	        if (mMediaPlayer != null) {
	            mMediaPlayer.stop();
	            mMediaPlayer.release();
	            mMediaPlayer = null;
	        }
	    }

	    private void prepare() {

	        if (mMediaPlayer != null) {
	            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
	            return;
	        }

	        try {
	            mMediaPlayer = new PLMediaPlayer(mAVOptions);
	            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
	            mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
	            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
	            mMediaPlayer.setOnErrorListener(mOnErrorListener);
	            mMediaPlayer.setOnInfoListener(mOnInfoListener);
	            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);

	            // set replay if completed
	            // mMediaPlayer.setLooping(true);

	            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

	            mMediaPlayer.setDataSource(mVideoPath);
	            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
	            mMediaPlayer.prepareAsync();

	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (IllegalStateException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {

	        @Override
	        public void surfaceCreated(SurfaceHolder holder) {
	            prepare();
	        }

	        @Override
	        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	            mSurfaceWidth = width;
	            mSurfaceHeight = height;
	        }

	        @Override
	        public void surfaceDestroyed(SurfaceHolder holder) {
	            // release();
	            releaseWithoutStop();
	        }
	    };

	    private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLMediaPlayer.OnVideoSizeChangedListener() {
	        public void onVideoSizeChanged(PLMediaPlayer mp, int width, int height) {
	            Log.i(TAG, "onVideoSizeChanged, width = "+ width + ",height = " + height);
	            // resize the display window to fit the screen
	            if (width != 0 && height != 0) {
	                float ratioW = (float) width/(float) mSurfaceWidth;
	                float ratioH = (float) height/(float) mSurfaceHeight;
	                float ratio = Math.max(ratioW, ratioH);
	                width  = (int) Math.ceil((float)width/ratio);
	                height = (int) Math.ceil((float)height/ratio);
	                FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(width, height);
	                layout.gravity = Gravity.CENTER;
	                mSurfaceView.setLayoutParams(layout);
	            }
	        }
	    };

	    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
	        @Override
	        public void onPrepared(PLMediaPlayer mp) {
	            Log.i(TAG, "On Prepared !");
	            mMediaPlayer.start();
	            mIsStopped = false;
	        }
	    };

	    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
	        @Override
	        public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
	            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
	            switch (what) {
	                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
	                    mLoadingView.setVisibility(View.VISIBLE);
	                    break;
	                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
	                case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
	                    mLoadingView.setVisibility(View.GONE);
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
	            Log.d(TAG, "onBufferingUpdate: " + percent + "%");
	        }
	    };

	    /**
	     *  Listen the event of playing complete
	     *  For playing local file, it's called when reading the file EOF
	     *  For playing network stream, it's called when the buffered bytes played over
	     *
	     *  If setLooping(true) is called, the player will restart automatically
	     *  And ｀onCompletion｀ will not be called
	     *
	     */
	    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
	        @Override
	        public void onCompletion(PLMediaPlayer mp) {
	            Log.d(TAG, "Play Completed !");
//	            showToastTips("Play Completed !");
//	            finish();
	        }
	    };

	    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
	        @Override
	        public boolean onError(PLMediaPlayer mp, int errorCode) {
	            Log.e(TAG, "Error happened, errorCode = " + errorCode);
	            switch (errorCode) {
	                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
//	                    showToastTips("Invalid URL !");
	                    break;
	                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
//	                    showToastTips("404 resource not found !");
	                    break;
	                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
//	                    showToastTips("Connection refused !");
	                    break;
	                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
//	                    showToastTips("Connection timeout !");
	                    break;
	                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
//	                    showToastTips("Empty playlist !");
	                    break;
	                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
//	                    showToastTips("Stream disconnected !");
	                    break;
	                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
//	                    showToastTips("Network IO Error !");
	                    break;
	                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
//	                    showToastTips("Unauthorized Error !");
	                    break;
	                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
//	                    showToastTips("Prepare timeout !");
	                    break;
	                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
//	                    showToastTips("Read frame timeout !");
	                    break;
	                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
	                default:
//	                    showToastTips("unknown error !");
	                    break;
	            }
	            // Todo pls handle the error status here, retry or call finish()
//	            finish();
	            // The PLMediaPlayer has moved to the Error state, if you want to retry, must reset first !
	            // try {
	            //     mMediaPlayer.reset();
	            //     mMediaPlayer.setDisplay(mSurfaceView.getHolder());
	            //     mMediaPlayer.setDataSource(mVideoPath);
	            //     mMediaPlayer.prepareAsync();
	            // } catch (IOException e) {
	            //     e.printStackTrace();
	            // }
	            // Return true means the error has been handled
	            // If return false, then `onCompletion` will be called
	            return true;
	        }
	    };

	    private void showToastTips(final String tips) {
	        if (mIsActivityPaused) {
	            return;
	        }
	        runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	                Toast.makeText(FullScreenActivity.this, tips, Toast.LENGTH_LONG).show();
	            }
	        });
	    }
	//rtmp；流--结束
	/**
	 * 从全屏播放返回到videoFragment
	 */
	public void backToVideoFragment() {
		Intent intent = new Intent(mContext, MainActivity.class);
		intent.putExtra("tag", "video");
		startActivity(intent);
		finish();
	}
	/**
	 * 从全屏播放返回到HomeNewActivity
	 */
	public void backToHomeNewActivity() {
		Intent intent = new Intent(mContext, HomeNewActivity.class);
		startActivity(intent);
//		finish();
	}
	/**
	 * 从全屏播放返回到HomeNewActivity
	 */
	public void backToHomeForBigActivity() {
		Intent intent = new Intent(mContext, HomeForBigActivity.class);
		startActivity(intent);
//		finish();
	}
	/** 
     * 监听Back键按下事件,方法2: 
     * 注意: 
     * 返回值表示:是否能完全处理该事件 
     * 在此处返回false,所以会继续传播该事件. 
     * 在具体项目中此处的返回值视情况而定. 
     */  
     @Override  
     public boolean onKeyDown(int keyCode, KeyEvent event) {  
         if ((keyCode == KeyEvent.KEYCODE_BACK)) {  
              System.out.println("按下了back键   onKeyDown()");   
              if (type.equals("videoFragment")) {
				
            	  backToVideoFragment();
			}else if (type.equals("HomeNewActivity")) {//
				backToHomeNewActivity();
			}else if (type.equals("HomeForBigActivity")) {
				backToHomeForBigActivity();
			}
              return false;  
         }else {  
             return super.onKeyDown(keyCode, event);  
         }  
           
     }  
}
