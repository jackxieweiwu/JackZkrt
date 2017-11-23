package utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jack.jackzkrt.bean.GasTime;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xcong on 2017/10/1.
 *
 * @des ${TODO}
 */

public class ChartUtil {

    public static void initChart(LineChart mChart) {
        mChart.setDrawGridBackground(false);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        //leftAxis.setTypeface(mTf);
        leftAxis.setTextSize(0);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setValueFormatter(new PercentFormatter());

        XAxis xAxis = mChart.getXAxis();
        //xAxis.setTypeface(mTf);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(0);
        xAxis.setTextColor(Color.WHITE);

        mChart.getAxisRight().setEnabled(false);
    }





    public static void setData(LineChart mChart, List<GasTime> gastimelist) {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < gastimelist.size(); i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < gastimelist.size(); i++) {
            float val = (float) gastimelist.get(i).getGasValue();
            yVals1.add(new Entry(val, i));
        }



        LineDataSet set1;


        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setYVals(yVals1);

            mChart.getData().setXVals(xVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);


            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(xVals, dataSets);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            // set data
            mChart.setData(data);
        }
        }

}
