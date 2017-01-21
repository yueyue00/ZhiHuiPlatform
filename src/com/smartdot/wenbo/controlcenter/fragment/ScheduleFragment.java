package com.smartdot.wenbo.controlcenter.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.adapter.ScheduleAdapter;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;
import com.smartdot.wenbo.controlcenter.bean.SchduleNewBean;
import com.smartdot.wenbo.controlcenter.bean.ScheduleAllData;
import com.smartdot.wenbo.controlcenter.bean.ScheduleAllData.InfoBean;
import com.smartdot.wenbo.controlcenter.bean.ScheduleAllData.InfoBean.MeettingBean;
import com.smartdot.wenbo.controlcenter.bean.User;
import com.smartdot.wenbo.controlcenter.task.NetTask;
import com.smartdot.wenbo.controlcenter.task.ResponseBody;
import com.smartdot.wenbo.controlcenter.task.ScheduleTask;
import com.smartdot.wenbo.controlcenter.util.IsWebCanBeUse;
import com.smartdot.wenbo.controlcenter.util.MSharePreferenceUtils;

public class ScheduleFragment extends Fragment  {
	private TextView tv_title;
	private RadioGroup rg;
	private RadioButton rb_1;
	private RadioButton rb_2;
	private RadioButton rb_3;
	private ListView listView;
	private TextView tv_empty;
	private Context mContext;
	private User user;
	private List<MeettingBean> dataBeans = new ArrayList<>();
//	private List<SchduleNewBean> datas = new ArrayList<>();
	private List<InfoBean> datas = new ArrayList<>();
	private List<MeettingBean> listData1  = new ArrayList<>();//listview的数据
	private List<MeettingBean> listData2  = new ArrayList<>();//listview的数据
	private List<MeettingBean> listData3  = new ArrayList<>();//listview的数据
//	protected BarChart mChart;
	private ScheduleAdapter adapter;
	ProgressDialog pd;
	
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
				R.layout.fragment_schdule, null);
		initView(view);
//		Constant.getCurrentTimeToHm(System.currentTimeMillis());
		
       	adapter = new ScheduleAdapter(mContext, dataBeans);
       	listView.setAdapter(adapter);
//		initData();
       
        loadDataNew();
		return view;
	}
	

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
//				String res = (String) msg.obj;
//				System.out.println("======ccghjkl;===="+res);
                    ScheduleAllData resulData = (ScheduleAllData) msg.obj;
                    System.out.println("===ScheduleAllData=====>"+resulData.getMessage());
                    datas.clear();
    				datas.addAll(resulData.getInfo());
    				listData1.clear();
    				listData2.clear();
    				listData3.clear();
    				System.out.println("========****====schedule---->"+datas.size());
    				
    				System.out.println("=========kjhdfghjk=====>"+datas.get(0).toString());
    				if (datas.size() == 1) {
    					System.out.println("===***date===>"+datas.get(0).getDate());
    					rb_1.setText(datas.get(0).getDate());
    					
    					rb_2.setVisibility(View.GONE);
    					rb_3.setVisibility(View.GONE);
    					List<MeettingBean> meetting = datas.get(0).getMeetting();
    					if (meetting!=null) {
    						
    						listData1.addAll(meetting);
    					}
    				}else if (datas.size() == 2) {
    					System.out.println("===***date==2=>"+datas.get(0).getDate());
    					rb_1.setText(datas.get(0).getDate());
    					rb_2.setText(datas.get(1).getDate());
    					rb_3.setVisibility(View.GONE);
    					listData1.addAll(datas.get(0).getMeetting());
    					listData2.addAll(datas.get(1).getMeetting());
    					
    				}else {
    					rb_1.setText(datas.get(0).getDate());
    					rb_2.setText(datas.get(1).getDate());
    					rb_3.setText(datas.get(2).getDate());
    					
    					listData1.addAll(datas.get(0).getMeetting());
    					listData2.addAll(datas.get(1).getMeetting());
    					listData3.addAll(datas.get(2).getMeetting());
    				}
    				rb_1.setChecked(true);
    		        initData1(listData1);
    		        
//                    initListData(dataBeans);
    				tv_empty.setVisibility(View.GONE);
    				listView.setVisibility(View.VISIBLE);
    				
    				rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    					
    					@Override
    					public void onCheckedChanged(RadioGroup group, int checkedId) {
    						switch (checkedId) {
    						case R.id.f_srb_one:
    		                    rb_1.setChecked(true);
    		                    initData1(listData1);
//    		                    rb_1.setBackgroundResource(R.drawable.rb)
    							break;
    						case R.id.f_srb_two:
    		                    rb_2.setChecked(true);
    		                    initData1(listData2);
    							break;
    						case R.id.f_srb_three:
    		                   rb_3.setChecked(true);
    		                   initData1(listData3);
    							break;
    						default:
    							break;
    						}		
    					}
    				});
				break;
			case -1:

				break;
			case 300:

				break;
			case 400:

				break;
			case 500:

				break;

			default:
				break;
			}
		};
	};
	/**
	 * 用task请求数据
	 */
	public void loadDataNew(){
		MSharePreferenceUtils.getAppConfig(getActivity());
		user = (User) MSharePreferenceUtils
				.getBean(getActivity(), Constant.sp_user);
		try {
			new ScheduleTask(handler.obtainMessage(), mContext,Constant.decode(
					Constant.key, user.getUserId().trim()))
					.execute(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 初始化数据
	 */
	private void initData1(List<MeettingBean> dataList) {
        dataBeans.clear();
		dataBeans.addAll(dataList);
       	adapter.notifyDataSetChanged();
       	
       	
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView(View view) {
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("大会日程");
		tv_empty = (TextView) view.findViewById(R.id.f_srb_tv_empty);
		rg = (RadioGroup) view.findViewById(R.id.f_sRg);
		rb_1 = (RadioButton) view.findViewById(R.id.f_srb_one);
		rb_2 = (RadioButton) view.findViewById(R.id.f_srb_two);
		rb_3 = (RadioButton) view.findViewById(R.id.f_srb_three);
		listView = (ListView) view.findViewById(R.id.f_sListView);
		
//		mChart = (BarChart) view.findViewById(R.id.chart1);
//		mChart.setOnChartValueSelectedListener(this);
//		mChart.setDrawBarShadow(false);
//		mChart.setDrawValueAboveBar(true);

//		mChart.setDescription("");

		// if more than 60 entries are displayed in the chart, no values will be
		// drawn
//		mChart.setMaxVisibleValueCount(60);

		// scaling can now only be done on x- and y-axis separately
//		mChart.setPinchZoom(false);

//		mChart.setDrawGridBackground(false);
		// mChart.setDrawYLabels(false);

//		IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

//		XAxis xAxis = mChart.getXAxis();
//		xAxis.setPosition(XAxisPosition.BOTTOM);
		// xAxis.setTypeface(mTfLight);
//		xAxis.setDrawGridLines(false);
//		xAxis.setGranularity(1f); // only intervals of 1 day
//		xAxis.setLabelCount(7);
//		xAxis.setValueFormatter(xAxisFormatter);

//		IAxisValueFormatter custom = new MyAxisValueFormatter();

//		YAxis leftAxis = mChart.getAxisLeft();
		// leftAxis.setTypeface(mTfLight);
//		leftAxis.setLabelCount(8, false);
//		leftAxis.setValueFormatter(custom);
//		leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
//		leftAxis.setSpaceTop(15f);
//		leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

//		YAxis rightAxis = mChart.getAxisRight();
//		rightAxis.setDrawGridLines(false);
		// rightAxis.setTypeface(mTfLight);
//		rightAxis.setLabelCount(8, false);
//		rightAxis.setValueFormatter(custom);
//		rightAxis.setSpaceTop(15f);
//		rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

//		Legend l = mChart.getLegend();
//		l.setPosition(LegendPosition.BELOW_CHART_LEFT);
//		l.setForm(LegendForm.SQUARE);
//		l.setFormSize(9f);
//		l.setTextSize(11f);
//		l.setXEntrySpace(4f);
		// l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
		// "def", "ghj", "ikl", "mno" });
		// l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
		// "def", "ghj", "ikl", "mno" });

//		XYMarkerView mv = new XYMarkerView(mContext, xAxisFormatter);
//		mv.setChartView(mChart); // For bounds control
//		mChart.setMarker(mv); // Set the marker to the chart
//		mChart.animateY(2500);
//		setData(12, 50);

		// mChart.setDrawLegend(false);
	}

//	private void setData(int count, float range) {
//
//		float start = 0f;
//
//		mChart.getXAxis().setAxisMinValue(start);
//		mChart.getXAxis().setAxisMaxValue(start + count + 2);
//
//		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//
//		for (int i = (int) start; i < start + count + 1; i++) {
//			float mult = (range + 1);
//			float val = (float) (Math.random() * mult);
//			yVals1.add(new BarEntry(i + 1f, val));
//		}
//		BarDataSet set1;
//
//		if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
//			set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
//			set1.setValues(yVals1);
//			mChart.getData().notifyDataChanged();
//			mChart.notifyDataSetChanged();
//		} else {
//			set1 = new BarDataSet(yVals1, "The year 2017");
//			set1.setColors(ColorTemplate.MATERIAL_COLORS);
//
//			ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//			dataSets.add(set1);
//
//			BarData data = new BarData(dataSets);
//			data.setValueTextSize(10f);
//			// data.setValueTypeface(mTfLight);
//			data.setBarWidth(0.9f);
//
//			mChart.setData(data);
//		}
//	}
//
//	protected RectF mOnValueSelectedRectF = new RectF();
//
//	@Override
//	public void onValueSelected(Entry e, Highlight h) {
//		if (e == null)
//			return;
//		RectF bounds = mOnValueSelectedRectF;
//		mChart.getBarBounds((BarEntry) e, bounds);
//		MPPointF position = mChart.getPosition(e, AxisDependency.LEFT);
//
//		Log.i("bounds", bounds.toString());
//		Log.i("position", position.toString());
//
//		Log.i("x-index", "low: " + mChart.getLowestVisibleX() + ", high: "
//				+ mChart.getHighestVisibleX());
//
//		MPPointF.recycleInstance(position);
//	}
//
//	@Override
//	public void onNothingSelected() {
//
//	}

	
}
