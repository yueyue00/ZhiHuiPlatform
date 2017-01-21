package com.smartdot.wenbo.controlcenter.fragment;

import com.smartdot.wenbo.controlcenter.R;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

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

public class HomeMeetingStateFragment extends Fragment {

	private Context mContext;
	 private ColumnChartView columnChartView;
	    private ColumnChartData data;
	    /**
	     * 代表总共有8列
	     */
	    private int numColumns = 8;
	    /**
	     *  Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
	     */
	    private int numSubcolumn = 1;
	    /**
	     * X轴每组数据对应的别名
	     */
	    private String [] columnName = new String[]{"会场1","会场2","会场3","会场4","会场5","会场6","会场7","会场8"};
	    private int [] dataInt = new int[]{112,34,156,134,78,178,68,69};

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
				R.layout.fragment_meeting_statue_home, null);
		//初始化View
        initView(view);
        //初始化数据
        initData();

        //重置坐标轴，将Y轴的范围设置为（0,100）
        resetViewPort();
		return view;
	}
	 private void resetViewPort() {
	        Viewport viewport = new Viewport(columnChartView.getMaximumViewport());
	        viewport.bottom = 0;//相当于设置Y轴的最小值
	        viewport.top = 200;//相当于设置Y轴的最大值
	        viewport.right = numColumns;
	        columnChartView.setMaximumViewport(viewport);
	        columnChartView.setCurrentViewport(viewport);

	    }
	   
	    private void initData() {
	        List<AxisValue> axisValues = new ArrayList<>();
	        List<Column> columns = new ArrayList<>();
	        List<SubcolumnValue> values ;
	        for (int i = 0; i < numColumns; i++) {
	            values = new ArrayList<>();
	            for (int j = 0; j < numSubcolumn; j++) {
	            	if (dataInt[i]>100) {
						
	            		values.add(new SubcolumnValue(dataInt[i], ChartUtils.darkenColor(Color.parseColor("#64b6c2"))));
					}else {
						values.add(new SubcolumnValue(dataInt[i], ChartUtils.darkenColor(Color.parseColor("#9c9c9e"))));
					}
	            	
	            }
	            Column column =new Column(values);
	            column.setHasLabels(true);//设置是否显示标签
	            column.setHasLabelsOnlyForSelected(false);//设置是否是有选中的时候才显示标签
	            columns.add(column);
	            axisValues.add(new AxisValue(i).setLabel(columnName[i]));
	        }

	        data = new ColumnChartData();
	        Axis axisX = new Axis(axisValues);
	        Axis axisY = new Axis();
	        axisY.setName("Axis Y");//设置Y轴的名字
	        axisY.setHasLines(true);//设置在Y轴按线分割
	        data.setAxisXBottom(axisX);//设置底边为X轴
	        data.setColumns(columns);
	        columnChartView.setColumnChartData(data);

	    }

	    private void initView(View view) {
	        columnChartView = (ColumnChartView) view.findViewById(R.id.columnChartView_home);
	        columnChartView.setViewportCalculationEnabled(false);
	        columnChartView.setClickable(false);
//	        columnChartView.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
//	            @Override
//	            public void onValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {
//	                Toast.makeText(mContext,"subcolumnValue:"+subcolumnValue.getValue(),Toast.LENGTH_SHORT).show();
//	            }
//
//	            @Override
//	            public void onValueDeselected() {
//
//	            }
//	        });
	    }
}
