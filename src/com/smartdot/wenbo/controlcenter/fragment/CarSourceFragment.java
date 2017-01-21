package com.smartdot.wenbo.controlcenter.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.smartdot.wenbo.controlcenter.adapter.CarSourceAdapter;
import com.smartdot.wenbo.controlcenter.bean.CarInfo;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;
import com.smartdot.wenbo.controlcenter.bean.User;
import com.smartdot.wenbo.controlcenter.task.CarSourceSearchTask;
import com.smartdot.wenbo.controlcenter.task.NetTask;
import com.smartdot.wenbo.controlcenter.task.ResponseBody;
import com.smartdot.wenbo.controlcenter.util.IsWebCanBeUse;
import com.smartdot.wenbo.controlcenter.util.MSharePreferenceUtils;

public class CarSourceFragment extends Fragment {
	private TextView tv_title;
	TextView nodatafound;
	TextView cars_tv;
	EditText car_et_search;
	TextView car_search_tv;
	TextView car_tv_search_empty;
	ListView car_search_list;
	private User user;
	private String userId = "";

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
			.fromResource(R.drawable.biz_taxi_bearing);
	private InfoWindow mInfoWindow;
	//
	ArrayList<CarInfo> cars = new ArrayList<CarInfo>();
	ArrayList<CarInfo> searchCars = new ArrayList<CarInfo>();
	ListView cars_lv;
	CarSourceAdapter car_adapter;
	CarSourceAdapter car_SearchAdapter;
	User parent;
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
				//
				for (int i = 0; i < cars.size(); i++) {
					CarInfo carInfo = cars.get(i);
					if (carInfo.lat != null && carInfo.lon != null
							&& carInfo.lat != "" && carInfo.lon != "")
						carInfo.ll = new LatLng(
								Double.parseDouble(carInfo.lat),
								Double.parseDouble(carInfo.lon));
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
	private Handler search_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// String result = (String) msg.obj;
				// System.out.println("=====车辆资源查询结果===》"+result);

				ResponseBody<CarInfo> res = (ResponseBody<CarInfo>) msg.obj; // 首先创建接收方法
				searchCars.clear();
				searchCars.addAll(res.list);
				System.out.println("=======车辆查询结果===size=" + searchCars.size());
				car_tv_search_empty.setVisibility(View.GONE);
				car_search_list.setVisibility(View.VISIBLE);
				car_SearchAdapter.notifyDataSetChanged();
				//
				for (int i = 0; i < searchCars.size(); i++) {
					CarInfo carInfo = searchCars.get(i);
					if (carInfo.lat != null && carInfo.lon != null
							&& carInfo.lat != "" && carInfo.lon != "")
						carInfo.ll = new LatLng(
								Double.parseDouble(carInfo.lat),
								Double.parseDouble(carInfo.lon));
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
				car_tv_search_empty.setVisibility(View.VISIBLE);
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
		MSharePreferenceUtils.getAppConfig(mContext);
		parent = (User) MSharePreferenceUtils.getBean(mContext,
				Constant.sp_user);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.fragment_car_source, null);
		initView(view);
		initData();
		// process(-1);
		setAllClick();
		showMyLocation();
		return view;
	}

	private void initView(View view) {
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		nodatafound = (TextView) view.findViewById(R.id.nodatafound);
		tv_title.setText("车辆资源");
		car_et_search = (EditText) view.findViewById(R.id.car_et_search);
		car_search_tv = (TextView) view.findViewById(R.id.car_search_tv);
		car_tv_search_empty = (TextView) view
				.findViewById(R.id.car_tv_search_empty);
		car_search_list = (ListView) view.findViewById(R.id.car_search_list);
		// 初始化百度地图
		// 1.不可用
		// mMapView = (MapView) view.findViewById(R.id.baidu_map_car2);
		// mBaidumap = mMapView.getMap();
		// 2.不可用
		// SupportMapFragment map1 = (SupportMapFragment) (((FragmentActivity)
		// mContext)
		// .getSupportFragmentManager()
		// .findFragmentById(R.id.baidu_map_car2));
		// mMapView = map1.getMapView();
		// mBaidumap = map1.getBaiduMap();
		// 3.可用
		mMapView = (TextureMapView) view.findViewById(R.id.baidu_map_car2);
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
		//
		cars_tv = (TextView) view.findViewById(R.id.cars_tv);
		cars_lv = (ListView) view.findViewById(R.id.cars_lv);
		//
		car_adapter = new CarSourceAdapter(mContext, cars);
		cars_lv.setAdapter(car_adapter);
		car_SearchAdapter = new CarSourceAdapter(mContext, searchCars);
		car_search_list.setAdapter(car_SearchAdapter);
		car_search_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String searchString = car_et_search.getText().toString();
				if (searchString != null && !searchString.equals("")) {
					getCarSourceForSearch(searchString);
					cars_lv.setVisibility(View.GONE);
				} else {
					cars_lv.setVisibility(View.VISIBLE);
					car_search_list.setVisibility(View.GONE);
				}

			}
		});
		car_et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (car_et_search.getText().toString() == null
						|| car_et_search.getText().toString().equals("")) {
					cars_lv.setVisibility(View.VISIBLE);
					car_search_list.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// if (s == null || s.equals("")) {
				// cars_lv.setVisibility(View.VISIBLE);
				// car_search_list.setVisibility(View.GONE);
				// }
			}
		});

	}

	private void initData() {
		// 下面是网络请求
//		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
//			Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
//			return;
//		}
		try {
			ClientParams client = new ClientParams(); // 创建一个新的Http请求
			client.url = "/taskSign.do?"; // Http 请求的地址 前面的域名封装好了
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("method=getCarList&userId=");
			strbuf.append(Constant.decode(Constant.key, parent.getUserId()
					.trim()));
			String str = strbuf.toString();
			client.params = str;

			Type type = new TypeToken<ArrayList<CarInfo>>() {
			}.getType();
			NetTask<CarInfo> net = new NetTask<CarInfo>(
					carsource_handler.obtainMessage(), client, type, mContext);
			net.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getCarSourceForSearch(String carnumber) {
		MSharePreferenceUtils.getAppConfig(getActivity());
		user = (User) MSharePreferenceUtils.getBean(getActivity(),
				Constant.sp_user);
		userId = user.getUserId();
		// new CarSourceSearchTask(search_handler.obtainMessage(),
		// getActivity(), userId, carnumber).execute(1);
		// 下面是网络请求
//		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
//			Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
//			return;
//		}
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/taskSign.do?"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=getCarList&userId=" + userId + "&carNumber="
				+ carnumber);
		String str = strbuf.toString();
		client.params = str;

		Type type = new TypeToken<ArrayList<CarInfo>>() {
		}.getType();
		NetTask<CarInfo> net = new NetTask<CarInfo>(
				search_handler.obtainMessage(), client, type, mContext);
		net.execute();
	}

	private void process(int position) {
		//
		mBaidumap.clear();
		markers.clear();
		for (int i = 0; i < cars.size(); i++) {
			if (i == position) {
				if (cars.get(i).ll != null) {
					MarkerOptions oob = new MarkerOptions()
							.title(cars.get(i).carNumber)
							.position(cars.get(i).ll).icon(bdA).zIndex(i)
							.draggable(true);
					mMarkerB = (Marker) (mBaidumap.addOverlay(oob));
					markers.add(mMarkerB);
				}
			} else {
				if (cars.get(i).ll != null) {
					MarkerOptions oob = new MarkerOptions()
							.title(cars.get(i).carNumber)
							.position(cars.get(i).ll).icon(bdA).zIndex(i)
							.draggable(true);
					mMarkerB = (Marker) (mBaidumap.addOverlay(oob));
					markers.add(mMarkerB);
				}
			}
		}
		if (cars.size() > 0) {
			if (position >= 0) {
				if (cars.get(position).ll != null) {
					MapStatusUpdate u = MapStatusUpdateFactory
							.newMapStatus(new MapStatus.Builder()
									.target(cars.get(position).ll).zoom(16)
									.build());
					mBaidumap.setMapStatus(u);
				}
			} else {
				if (cars.get(0).ll != null) {
					MapStatusUpdate u = MapStatusUpdateFactory
							.newMapStatus(new MapStatus.Builder()
									.target(cars.get(0).ll).zoom(16).build());
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
		cars_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				process(position);
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
						builder.target(ml1).zoom(16.0f);
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
