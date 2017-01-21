package com.smartdot.wenbo.controlcenter.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.adapter.CarSourceAdapter;
import com.smartdot.wenbo.controlcenter.bean.BaiduLocation;
import com.smartdot.wenbo.controlcenter.bean.CarInfo;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;
import com.smartdot.wenbo.controlcenter.task.NetTask;
import com.smartdot.wenbo.controlcenter.task.ResponseBody;
import com.smartdot.wenbo.controlcenter.util.IsWebCanBeUse;

public class HomeCarSourceFragment extends Fragment {

	private Context mContext;
	TextView cars_tv;
	TextView nodatafound;
	MapView mMapView = null; // 地图View
	BaiduMap mBaidumap = null;
	LatLng ml1 = null;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true; // 是否首次定位
	// 收藏点
	private Marker mMarkerB;
	ArrayList<Marker> markers = new ArrayList<>();
	ArrayList<BaiduLocation> hotels = new ArrayList<BaiduLocation>();
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.biz_taxi_bearing);
	private InfoWindow mInfoWindow;
	//
	ArrayList<CarInfo> cars = new ArrayList<CarInfo>();
	ListView cars_lv;
	CarSourceAdapter car_adapter;

	Handler carsource_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 获取重要嘉宾的列表返回数据
				ResponseBody<CarInfo> res = (ResponseBody<CarInfo>) msg.obj; // 首先创建接收方法
				cars.clear();
				cars.addAll(res.list);

				nodatafound.setVisibility(View.GONE);
				cars_lv.setVisibility(View.VISIBLE);
				car_adapter.notifyDataSetChanged();
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
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = LayoutInflater.from(mContext).inflate(
				R.layout.fragment_car_source_home, null);
		initView(view);
		initData();
		process();
		setAllClick();
		return view;
	}

	private void initView(View view) {
		// 初始化百度地图
		// mMapView = (MapView) view.findViewById(R.id.baidu_map);
		// mBaidumap = mMapView.getMap();
		nodatafound = (TextView) view.findViewById(R.id.nodatafound);
		SupportMapFragment map1 = (SupportMapFragment) (((FragmentActivity) mContext)
				.getSupportFragmentManager()
				.findFragmentById(R.id.baidu_map_car));
		mMapView = map1.getMapView();
		mBaidumap = map1.getBaiduMap();
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
		//
		cars_tv = (TextView) view.findViewById(R.id.cars_tv);
		cars_lv = (ListView) view.findViewById(R.id.home_cars_lv);
	}

	private void initData() {

		BaiduLocation l1 = new BaiduLocation();
		l1.address = "阿尔丁大街7号";
		l1.ll = new LatLng(40.146835, 94.657235);
		BaiduLocation l2 = new BaiduLocation();
		l2.address = "敦煌大街2号";
		l2.ll = new LatLng(40.144261, 94.66849);
		BaiduLocation l3 = new BaiduLocation();
		l3.address = "名族路28号";
		l3.ll = new LatLng(40.148337, 94.67141);
		BaiduLocation l4 = new BaiduLocation();
		l4.address = "敦煌敦和大酒店";
		l4.ll = new LatLng(40.14223, 94.672026);
		hotels.add(l1);
		hotels.add(l2);
		hotels.add(l3);
		hotels.add(l4);

		mBaidumap.clear();
		markers.clear();
		for (int i = 0; i < hotels.size(); i++) {
			MarkerOptions oob = new MarkerOptions()
					.title(hotels.get(i).address).position(hotels.get(i).ll)
					.icon(bdA).zIndex(i).draggable(true);
			mMarkerB = (Marker) (mBaidumap.addOverlay(oob));
			markers.add(mMarkerB);
		}
		if (hotels.size() > 0) {
			MapStatusUpdate u = MapStatusUpdateFactory
					.newMapStatus(new MapStatus.Builder()
							.target(hotels.get(0).ll).zoom(15).build());
			mBaidumap.setMapStatus(u);
		}
		// 下面是网络请求
//		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
//			Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
//			return;
//		}
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/taskSign.do?"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=getCarList&userId=wangsy");
		String str = strbuf.toString();
		client.params = str;

		Type type = new TypeToken<ArrayList<CarInfo>>() {
		}.getType();
		NetTask<CarInfo> net = new NetTask<CarInfo>(
				carsource_handler.obtainMessage(), client, type, mContext);
		net.execute();
	}

	private void process() {
		car_adapter = new CarSourceAdapter(mContext, cars);
		cars_lv.setAdapter(car_adapter);
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
		cars_tv.setText("定位");
		cars_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cars_tv.getText().equals("定位")) {
					cars_tv.setText("返回");
					if (!mLocClient.isStarted()) {
						mLocClient.start();
					}
					if (ml1 != null) {
						MapStatus.Builder builder = new MapStatus.Builder();
						builder.target(ml1).zoom(15.0f);
						mBaidumap.animateMapStatus(MapStatusUpdateFactory
								.newMapStatus(builder.build()));
					}
				} else {
					cars_tv.setText("定位");
					initData();
				}
			}
		});
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
