package com.smartdot.wenbo.controlcenter.fragment;

import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.controlcenter.R;
import com.smartdot.wenbo.controlcenter.aconstant.Constant;
import com.smartdot.wenbo.controlcenter.adapter.DaoHuiGridViewAdapter;
import com.smartdot.wenbo.controlcenter.bean.ClientParams;
import com.smartdot.wenbo.controlcenter.bean.DaoHuiGridBean;
import com.smartdot.wenbo.controlcenter.bean.ImportGuest;
import com.smartdot.wenbo.controlcenter.bean.MeetingDataBean;
import com.smartdot.wenbo.controlcenter.bean.User;
import com.smartdot.wenbo.controlcenter.task.NetTask;
import com.smartdot.wenbo.controlcenter.task.ResponseBody;
import com.smartdot.wenbo.controlcenter.util.IsWebCanBeUse;
import com.smartdot.wenbo.controlcenter.util.MSharePreferenceUtils;

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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class MeetingStateFragment extends Fragment {

	private Context mContext;
	ProgressDialog pd;
	private ColumnChartView columnChartView;
	private GridView gridView;
	private TextView tv_title;
	private LinearLayout meeting_linear;
	private TextView tv_empty;
	private ColumnChartData data;
	private DaoHuiGridViewAdapter adapter;
	private List<DaoHuiGridBean> datas;
	private List<MeetingDataBean> meeting_list = new ArrayList<MeetingDataBean>();
	User parent;
	/**
	 * 代表总共有8列
	 */
	private int numColumns;
	/**
	 * Column can have many subcolumns, here by default I use 1 subcolumn in
	 * each of 8 columns.
	 */
	private int numSubcolumn = 1;
	/**
	 * X轴每组数据对应的别名
	 */
	// private String [] columnName = new
	// String[]{"会场1","会场2","会场3","会场4","会场5","会场6","会场7","会场8"};
	private String[] columnName;
	// private int [] dataInt = new int[]{112,34,156,134,78,178,68,69};
	private int[] dataInt;

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
				R.layout.fragment_meeting_statue, null);

		// 初始化View
		initView(view);
		loadData();
		adapter = new DaoHuiGridViewAdapter(mContext, meeting_list);
		gridView.setAdapter(adapter);

		return view;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (pd != null) {
				pd.dismiss();
			}
			switch (msg.what) {
			case 0:// 获取重要嘉宾的列表返回数据
				ResponseBody<MeetingDataBean> res = (ResponseBody<MeetingDataBean>) msg.obj; // 首先创建接收方法
				meeting_list.clear();
				meeting_list.addAll(res.list);

				numColumns = meeting_list.size();

				columnName = new String[meeting_list.size()];
				dataInt = new int[meeting_list.size()];
				for (int i = 0; i < meeting_list.size(); i++) {
					columnName[i] = meeting_list.get(i).shortName;
					System.out.println("============>"
							+ meeting_list.get(i).name.substring(0, 1));
					dataInt[i] = meeting_list.get(i).count;

				}
				int max = getMax(dataInt) + 10;
				System.out.println("======max=====>" + max);
				initData(numColumns);
				// 重置坐标轴，将Y轴的范围设置为（0,100）
				resetViewPort(numColumns, max);
				adapter.notifyDataSetChanged();
				tv_empty.setVisibility(View.GONE);
				meeting_linear.setVisibility(View.VISIBLE);
				break;
			case 1:
				Toast.makeText(mContext, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				tv_empty.setVisibility(View.VISIBLE);
				meeting_linear.setVisibility(View.GONE);
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
	 * 在数组中去得最大值
	 * 
	 * @param arr
	 * @return
	 */
	public int getMax(int[] arr)

	{

		int max = arr[0];

		for (int i = 1; i < arr.length; i++) {

			if (arr[i] > max)
				max = arr[i];

		}

		return max;

	}

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
			client.url = "/meetings.do?"; // Http 请求的地址 前面的域名封装好了
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("method=atMeeting&userId=");
			strbuf.append(Constant.decode(Constant.key, parent.getUserId()
					.trim()));
			String str = strbuf.toString();
			client.params = str;

			Type type = new TypeToken<ArrayList<MeetingDataBean>>() {
			}.getType();
			NetTask<MeetingDataBean> net = new NetTask<MeetingDataBean>(
					handler.obtainMessage(), client, type, mContext);
			net.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
				if (dataInt[i] > 100) {

					values.add(new SubcolumnValue(dataInt[i], ChartUtils
							.darkenColor(Color.parseColor("#64b6c2"))));
				} else {
					values.add(new SubcolumnValue(dataInt[i], ChartUtils
							.darkenColor(Color.parseColor("#9c9c9e"))));
				}

			}
			Column column = new Column(values);
			column.setHasLabels(true);// 设置是否显示标签
			column.setHasLabelsOnlyForSelected(false);// 设置是否是有选中的时候才显示标签
			columns.add(column);
			axisValues.add(new AxisValue(i).setLabel(columnName[i]));
		}

		data = new ColumnChartData();
		Axis axisX = new Axis(axisValues);
		Axis axisY = new Axis();
		axisY.setName("Axis Y");// 设置Y轴的名字
		axisY.setHasLines(true);// 设置在Y轴按线分割
		data.setAxisXBottom(axisX);// 设置底边为X轴
		data.setColumns(columns);
		columnChartView.setColumnChartData(data);

	}

	private void initView(View view) {
		columnChartView = (ColumnChartView) view
				.findViewById(R.id.columnChartView);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("到会现状");
		gridView = (GridView) view.findViewById(R.id.f_ms_gridview);
		meeting_linear = (LinearLayout) view.findViewById(R.id.f_ms_linear);
		tv_empty = (TextView) view.findViewById(R.id.f_ms_tvEmpty);
		columnChartView.setViewportCalculationEnabled(false);
		// columnChartView.setClickable(false);
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
