package com.smartdot.wenbo.controlcenter.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.adapter.ServiceStatueListviewAdapter;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;
import com.smartdot.wenbo.controlcenter.bean.MeetingDataBean;
import com.smartdot.wenbo.controlcenter.bean.ServiceBean;
import com.smartdot.wenbo.controlcenter.bean.ServiceStatueBean;
import com.smartdot.wenbo.controlcenter.bean.User;
import com.smartdot.wenbo.controlcenter.task.NetTask;
import com.smartdot.wenbo.controlcenter.task.ResponseBody;
import com.smartdot.wenbo.controlcenter.util.IsWebCanBeUse;
import com.smartdot.wenbo.controlcenter.util.MSharePreferenceUtils;

public class ServiceStateFragment extends Fragment {
	ProgressDialog pd;
	private Context mContext;
	private ColumnChartView columnChartView;
	private TextView tv_title;
	private ListView listView;
	private LinearLayout service_ll;
	private TextView tv_empty;
	private ColumnChartData data;
	private ServiceStatueListviewAdapter adapter;
	private List<ServiceStatueBean> datas;
	private List<ServiceBean> dataBeans = new ArrayList<>();
	User parent;
	/**
	 * 代表总共有8列
	 */
	private int numColumns;
	/**
	 * Column can have many subcolumns, here by default I use 1 subcolumn in
	 * each of 8 columns.
	 */
	private int numSubcolumn = 2;
	/**
	 * X轴每组数据对应的别名
	 */
	// private String [] columnName = new
	// String[]{"接机","报道","入住","展览","资料","送机"};
	private String[] columnName;
	/**
	 * 二维数组 组名个数为行数 每组的条形图为列数
	 */
	// private int [][] dataArray =
	// {{25,11},{18,18},{34,2},{28,8},{35,1},{30,6}};
	private int[][] dataArray;

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
		MSharePreferenceUtils.getAppConfig(mContext);
		parent = (User) MSharePreferenceUtils.getBean(mContext,
				Constant.sp_user);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.fragment_service_statue, null);

		// 初始化View
		initView(view);
		// 加载网络数据
		loadData();
		adapter = new ServiceStatueListviewAdapter(mContext, dataBeans);
		listView.setAdapter(adapter);
		// initListData();

		return view;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (pd != null) {
				pd.dismiss();
			}
			switch (msg.what) {
			case 0:// 获取重要嘉宾的列表返回数据
				ResponseBody<ServiceBean> res = (ResponseBody<ServiceBean>) msg.obj; // 首先创建接收方法
				dataBeans.clear();
				dataBeans.addAll(res.list);
				numColumns = dataBeans.size();
				System.out.println("======numColums==>" + numColumns + ",---"
						+ dataBeans.size());
				columnName = new String[dataBeans.size()];
				dataArray = new int[dataBeans.size()][2];
				int max = 0;
				for (int i = 0; i < dataBeans.size(); i++) {
					String taskname = dataBeans.get(i).getTaskname();
					if (taskname.equalsIgnoreCase("jieji")) {
						columnName[i] = "到达";
					} else if (taskname.equalsIgnoreCase("qiandao")) {
						columnName[i] = "签到";
					} else if (taskname.equalsIgnoreCase("banruzhu")) {
						columnName[i] = "入住";
					} else if (taskname.equalsIgnoreCase("kanzhanlan")) {
						columnName[i] = "活动";
					} else if (taskname.equalsIgnoreCase("songji")) {
						columnName[i] = "离开";
					}else if (taskname.equalsIgnoreCase("yuzhuce")) {
						columnName[i] = "注册";
					}

					dataArray[i][0] = dataBeans.get(i).getFinish();
					dataArray[i][1] = dataBeans.get(i).getUnfinish();
					max = dataBeans.get(i).getFinish()
							+ dataBeans.get(i).getUnfinish() + 30;
					// System.out.println("=====service=max=>"+max);
				}

				initData(numColumns);
				// 重置坐标轴，将Y轴的范围设置为（0,100）
				resetViewPort(numColumns, max);
				adapter.notifyDataSetChanged();
				// initListData(dataBeans);
				tv_empty.setVisibility(View.GONE);
				service_ll.setVisibility(View.VISIBLE);
				// guest_adapter.notifyDataSetChanged();
				break;
			case 1:
				Toast.makeText(mContext, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				tv_empty.setVisibility(View.VISIBLE);
				service_ll.setVisibility(View.GONE);
				// searchresultrv.setVisibility(View.GONE);
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

	/**
	 * 请求网路数据
	 */
	private void loadData() {
//		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
//			Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
//			return;
//		}
		try {
//			pd = ProgressDialog.show(mContext, "", "Loading...");
//			pd.setCancelable(true);
//			pd.setCanceledOnTouchOutside(false);
			// 下面是网络请求
			ClientParams client = new ClientParams(); // 创建一个新的Http请求
			client.url = "/taskSign.do?"; // Http 请求的地址 前面的域名封装好了
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("method=getServiceState&userId=");
			strbuf.append(Constant.decode(Constant.key, parent.getUserId()
					.trim()));
			String str = strbuf.toString();
			client.params = str;
			Type type = new TypeToken<ArrayList<ServiceBean>>() {
			}.getType();
			NetTask<ServiceBean> net = new NetTask<ServiceBean>(
					handler.obtainMessage(), client, type, mContext);
			net.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化listview的数据
	 */
	// private void initListData(List<ServiceBean> data) {
	// datas = new ArrayList<>();
	// datas.clear();
	// for (int i = 0; i < columnName.length; i++) {
	// ServiceStatueBean bean = new ServiceStatueBean();
	// bean.setTaskName(columnName[i]);
	// bean.setTaskContent("已完成"+dataArray[i][0]+"人，未完成"+dataArray[i][1]+"人");
	// datas.add(bean);
	// }
	// adapter = new ServiceStatueListviewAdapter(mContext, datas);
	// listView.setAdapter(adapter);
	// }

	private void resetViewPort(int numColumns, int max) {
		Viewport viewport = new Viewport(columnChartView.getMaximumViewport());
		viewport.bottom = 0;// 相当于设置Y轴的最小值
		viewport.top = max;// 相当于设置Y轴的最大值
		viewport.right = numColumns;
		columnChartView.setMaximumViewport(viewport);
		columnChartView.setCurrentViewport(viewport);

	}

	private void initData(int numColumns) {

		List<AxisValue> axisValues = new ArrayList<>();
		List<Column> columns = new ArrayList<>();
		List<SubcolumnValue> values;
		for (int i = 0; i < numColumns; i++) {
			values = new ArrayList<>();
			for (int j = 0; j < numSubcolumn; j++) {
				// values.add(new SubcolumnValue((float)Math.random()*50f,
				// ChartUtils.pickColor()));
				// values.add(new SubcolumnValue(dataArray[i][j],
				// ChartUtils.pickColor()));
				if (j == 0) {// 已完成
					values.add(new SubcolumnValue(dataArray[i][j], ChartUtils
							.darkenColor(Color.parseColor("#64b6c2"))));

				} else if (j == 1) {// 未完成

					values.add(new SubcolumnValue(dataArray[i][j], ChartUtils
							.darkenColor(Color.parseColor("#9c9c9e"))));
				}

				// values.add(new SubcolumnValue(dataArray[i][j],
				// ChartUtils.COLOR_RED));

			}
			Column column = new Column(values);
			column.setHasLabels(true);// 设置是否显示标签
			column.setHasLabelsOnlyForSelected(false);// 设置是否是有选中的时候才显示标签
			columns.add(column);
			axisValues.add(new AxisValue(i).setLabel(columnName[i]));
		}

		data = new ColumnChartData();
		Axis axisX = new Axis(axisValues);
		// axisX.setName("Axis X");//设置X轴的名字
		Axis axisY = new Axis();
		// axisY.setName("Axis Y");//设置Y轴的名字
		axisY.setHasLines(true);// 设置在Y轴按线分割
		data.setAxisXBottom(axisX);// 设置底边为X轴
		data.setAxisYLeft(axisY);// 设置左边为Y轴
		// data.setBaseValue(Float.NEGATIVE_INFINITY);
		data.setColumns(columns);
		columnChartView.setColumnChartData(data);

	}

	private void initView(View view) {
		columnChartView = (ColumnChartView) view
				.findViewById(R.id.columnChartView_double);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("服务现状");
		service_ll = (LinearLayout) view.findViewById(R.id.service_ll);
		tv_empty = (TextView) view.findViewById(R.id.service_tv_empty);
		listView = (ListView) view.findViewById(R.id.service_statue_listview);
		columnChartView.setViewportCalculationEnabled(false);
		columnChartView
				.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
					@Override
					public void onValueSelected(int i, int i1,
							SubcolumnValue subcolumnValue) {
						// Toast.makeText(mContext,"subcolumnValue:"+subcolumnValue.getValue(),Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onValueDeselected() {

					}
				});
	}
}
