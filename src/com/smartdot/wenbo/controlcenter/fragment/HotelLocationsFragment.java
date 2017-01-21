package com.smartdot.wenbo.controlcenter.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.adapter.HotelsAdapter;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;
import com.smartdot.wenbo.controlcenter.bean.HotelInfo;
import com.smartdot.wenbo.controlcenter.bean.User;
import com.smartdot.wenbo.controlcenter.task.NetTask;
import com.smartdot.wenbo.controlcenter.task.ResponseBody;
import com.smartdot.wenbo.controlcenter.util.IsWebCanBeUse;
import com.smartdot.wenbo.controlcenter.util.MSharePreferenceUtils;

public class HotelLocationsFragment extends Fragment {
	private TextView tv_title;
	TextView nodatafound;
	private Context mContext;
	TextureMapView mMapView;
	// MapView mMapView = null; // 地图View
	BaiduMap mBaidumap = null;
	LatLng ml1 = new LatLng(40.148357, 94.668396);
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true; // 是否首次定位
	// 收藏点
	private Marker mMarkerB;
	ArrayList<Marker> markers = new ArrayList<>();
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.call_center_hot_problem_hotel_scene_icon);
	BitmapDescriptor bdB = BitmapDescriptorFactory
			.fromResource(R.drawable.call_center_hot_problem_hotel_icon);
	private InfoWindow mInfoWindow;
	//
	ArrayList<HotelInfo> hotels = new ArrayList<HotelInfo>();
	ListView hotel_lv;
	HotelsAdapter hotel_adapter;

	TextView jiudian;
	User parent;
	Handler hotel_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 获取重要嘉宾的列表返回数据
				ResponseBody<HotelInfo> res = (ResponseBody<HotelInfo>) msg.obj; // 首先创建接收方法
				hotels.clear();
				hotels.addAll(res.list);

				nodatafound.setVisibility(View.GONE);
				hotel_lv.setVisibility(View.VISIBLE);
				hotel_adapter.notifyDataSetChanged();
				for (int i = 0; i < hotels.size(); i++) {
					HotelInfo hotelInfo = hotels.get(i);
					if (hotelInfo.lat != null && hotelInfo.lng != null
							&& !hotelInfo.lat.equals("")
							&& !hotelInfo.lng.equals(""))
						hotelInfo.ll = new LatLng(
								Double.parseDouble(hotelInfo.lat),
								Double.parseDouble(hotelInfo.lng));
				}
				process(-1);
				break;
			case 1:
				Toast.makeText(mContext, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				nodatafound.setVisibility(View.VISIBLE);
				Toast.makeText(mContext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(mContext, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MSharePreferenceUtils.getAppConfig(mContext);
		parent = (User) MSharePreferenceUtils.getBean(mContext,
				Constant.sp_user);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.fragment_hotel_locations, null);

		initView(view);
		initData();
		// process();
		setAllClick();
		showMyLocation();
		return view;
	}

	private void initView(View view) {
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		nodatafound = (TextView) view.findViewById(R.id.nodatafound);
		tv_title.setText("酒店分布");
		// 初始化百度地图
		// 1.不可用
		// mMapView = (MapView) view.findViewById(R.id.baidu_map_hotel2);
		// mBaidumap = mMapView.getMap();
		// 2.不可用
		// SupportMapFragment map1 = (SupportMapFragment) (((FragmentActivity)
		// mContext)
		// .getSupportFragmentManager()
		// .findFragmentById(R.id.baidu_map_hotel2));
		// mMapView = map1.getMapView();
		// mBaidumap = map1.getBaiduMap();
		// 3.可用
		mMapView = (TextureMapView) view.findViewById(R.id.baidu_map_hotel2);
		mBaidumap = mMapView.getMap();

		mMapView.showZoomControls(false);
		mBaidumap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.NORMAL, true, null));
		// 百度地图定位----开启定位图层
		mBaidumap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(mContext);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(10);
		mLocClient.setLocOption(option);

		jiudian = (TextView) view.findViewById(R.id.jiudian);
		hotel_lv = (ListView) view.findViewById(R.id.hotel_lv);
		hotel_adapter = new HotelsAdapter(mContext, hotels);
		hotel_lv.setAdapter(hotel_adapter);
	}

	private void initData() {

		// 下面是网络请求
//		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
//			Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
//			return;
//		}
		try {
			ClientParams client = new ClientParams(); // 创建一个新的Http请求
			client.url = "/hotelFloor.do?"; // Http 请求的地址 前面的域名封装好了
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("method=getHotelList&userid=");
			strbuf.append(Constant.decode(Constant.key, parent.getUserId()
					.trim()));
			strbuf.append("&lg=1");
			String str = strbuf.toString();
			client.params = str;
			Type type = new TypeToken<ArrayList<HotelInfo>>() {
			}.getType();
			NetTask<HotelInfo> net = new NetTask<HotelInfo>(
					hotel_handler.obtainMessage(), client, type, mContext);
			net.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void process(int position) {

		mBaidumap.clear();
		markers.clear();
		for (int i = 0; i < hotels.size(); i++) {
			if (i == position) {
				if (hotels.get(i).hotelName != null && hotels.get(i).ll != null) {
					MarkerOptions oob = new MarkerOptions()
							.title(hotels.get(i).hotelName)
							.position(hotels.get(i).ll).icon(bdB).zIndex(i)
							.draggable(true);
					mMarkerB = (Marker) (mBaidumap.addOverlay(oob));
					markers.add(mMarkerB);
				}
			} else {
				if (hotels.get(i).ll != null && hotels.get(i).hotelName != null) {
					MarkerOptions oob = new MarkerOptions()
							.title(hotels.get(i).hotelName)
							.position(hotels.get(i).ll).icon(bdA).zIndex(i)
							.draggable(true);
					mMarkerB = (Marker) (mBaidumap.addOverlay(oob));
					markers.add(mMarkerB);
				}
			}
		}
		if (hotels.size() > 0) {
			if (position >= 0) {
				if (hotels.get(position).ll != null) {
					MapStatusUpdate u = MapStatusUpdateFactory
							.newMapStatus(new MapStatus.Builder()
									.target(hotels.get(position).ll).zoom(16)
									.build());
					mBaidumap.setMapStatus(u);
				}
			} else {
				if (hotels.get(0).ll != null) {
					MapStatusUpdate u = MapStatusUpdateFactory
							.newMapStatus(new MapStatus.Builder()
									.target(hotels.get(0).ll).zoom(16).build());
					mBaidumap.setMapStatus(u);
				}
			}
		}
	}

	private void setAllClick() {
		mBaidumap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				Button button = new Button(mContext);
				button.setBackgroundResource(R.drawable.popup);
				button.setTextColor(Color.parseColor("#3a3b3b"));
				button.setTextSize(12);
				button.setText(marker.getTitle());
				// TextView nametv = new TextView(mContext);
				// nametv.setBackgroundResource(R.drawable.popup);
				// nametv.setTextColor(Color.parseColor("#3a3b3b"));
				// nametv.setTextSize(12);
				// nametv.setText(marker.getTitle());
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory
						.fromView(button), marker.getPosition(), -47, null);
				mBaidumap.showInfoWindow(mInfoWindow);
				// }
				return true;
			}
		});
		jiudian.setText("定位");
		jiudian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (jiudian.getText().equals("定位")) {
					jiudian.setText("返回");
					if (!mLocClient.isStarted()) {
						mLocClient.start();
					}
					if (ml1 != null) {
						MapStatus.Builder builder = new MapStatus.Builder();
						builder.target(ml1).zoom(16.0f);
						mBaidumap.animateMapStatus(MapStatusUpdateFactory
								.newMapStatus(builder.build()));
					}
				} else {
					jiudian.setText("定位");
					initData();
				}
			}
		});
		hotel_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				process(position);
			}
		});
	}

	private void showMyLocation() {
		MapStatus.Builder builder = new MapStatus.Builder();
		builder.target(ml1).zoom(16.0f);
		mBaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder
				.build()));
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 回收 bitmap 资源
		bdA.recycle();
		bdB.recycle();
		mMapView.onDestroy();
		mLocClient.stop();
		super.onDestroy();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			// 这里一般不会走，走到了ifFrirstLoc里面
			ml1 = new LatLng(location.getLatitude(), location.getLongitude());
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaidumap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ml1).zoom(15.0f);
				mBaidumap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
}
