package com.westwood24.connect.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.westwood24.R;
import com.westwood24.connect.adapter.ChartAdapter;
import com.westwood24.connect.adapter.ChartAdapter1;
import com.westwood24.connect.custom.DayAxisValueFormatter;
import com.westwood24.connect.custom.IAxisValueFormatter;
import com.westwood24.connect.custom.MyAxisValueFormatter;
import com.westwood24.connect.font.TextViewMyriad_bld;
import com.westwood24.connect.model.Chart_Option;
import com.westwood24.connect.utils.Pref;

import java.util.ArrayList;

public class BarchartActivity extends AppCompatActivity {

    BarChart mChart;
    ChartAdapter adapter;
    ChartAdapter1 adapter1;
    TextViewMyriad_bld question;
    RelativeLayout mHeaderBackRl;
    ImageView img_left;
    TextView mHeaderTitleTv;
    TextView txt_title_id;

    ListView lstlableodd;
    ListView lstlableeven;
    ArrayList<String> data_lable_odd=new ArrayList<String>();
    ArrayList<String> data_lable_even=new ArrayList<String>();
    ArrayList<Integer> colors_odd = new ArrayList<Integer>();
    ArrayList<Integer> colors_even = new ArrayList<Integer>();
    ArrayList<Chart_Option> chart_optionArrayList=new ArrayList<>();

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart);
        mChart=(BarChart)findViewById(R.id.mChart);
        txt_title_id =(TextView)findViewById(R.id.txt_title_id);
        lstlableodd=(ListView)findViewById(R.id.lstlableodd);
        lstlableeven=(ListView)findViewById(R.id.lstlableeven);
        img_left = (ImageView)findViewById(R.id.img_left);
        question=(TextViewMyriad_bld)findViewById(R.id.question);
        mHeaderBackRl = (RelativeLayout) findViewById(R.id.header_layout_back_rl);

        mHeaderTitleTv = (TextView) findViewById(R.id.txt_title);

        intent=getIntent();
   //     chart_optionArrayList=intent.getParcelableExtra("chart_optionArrayList");
        chart_optionArrayList=(ArrayList<Chart_Option>) getIntent().getSerializableExtra("chart_optionArrayList");
        Log.e("option",chart_optionArrayList.size()+"--");
        question.setText("Que: "+Pref.getValue(BarchartActivity.this, "question", ""));

        txt_title_id.setText(Pref.getValue(BarchartActivity.this, "Htitle", ""));
        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
       // xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.parseColor("#ffffff"));
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
      //  xAxis.setValueFormatter((AxisValueFormatter) xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

       /* YAxis leftAxis = mChart.getAxisLeft();
       // leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
       // leftAxis.setValueFormatter((AxisValueFormatter) custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);*/
        //leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        YAxis leftAxis = mChart.getAxisLeft();
        // leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        // leftAxis.setValueFormatter((AxisValueFormatter) custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        leftAxis.setTextColor(getResources().getColor(R.color.white));
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
       // rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        //rightAxis.setValueFormatter((AxisValueFormatter) custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setTextColor(getResources().getColor(android.R.color.transparent));

        //rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setTextColor(getResources().getColor(R.color.white));
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4);

        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

      //  XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
       // mv.setChartView(mChart); // For bounds control
       // mChart.setMarker(mv); // Set the marker to the chart

        setData(3, 50);
        mChart.setDescription("");
        mChart.animateXY(1500, 1500);
        mHeaderBackRl.setVisibility(View.GONE);

        img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/BarChartActivity.java
    }


    private void setData(int count, float range) {

        float start = 0f;

        mChart.getXAxis().setAxisMinValue(start);
        mChart.getXAxis().setAxisMaxValue(start + count + 2);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            yVals1.add(new BarEntry(i + 1f, val));
        }

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for(int i=0;i<chart_optionArrayList.size();i++) {
            entries.add(new BarEntry(i+1, Integer.parseInt(chart_optionArrayList.get(i).getOption_value())));
        }
      //  entries.add(new BarEntry(2, 31));
      //  entries.add(new BarEntry(3, 72));
     //   entries.add(new BarEntry(4, 33));



        BarDataSet set1=new BarDataSet(entries, "The year 2017");

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.setDescription("");
        } else {
            set1 = new BarDataSet(entries, "The year 2017");

            //Log.v("set1",set1.getEntryCount()+"--");

            int[] rainbow = getResources().getIntArray(R.array.androidcolors);
            ArrayList<Integer> colors = new ArrayList<Integer>();
            for (int i = 0; i < set1.getEntryCount(); i++) {
                colors.add(rainbow[i]);
                // Do something with the paint.
            }
            set1.setColors(colors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mChart.setData(data);


            for(int i=0;i<chart_optionArrayList.size();i++)
            {
                if ((i) % 2 != 0)
                {
                    data_lable_even.add(chart_optionArrayList.get(i).getOption_title());
                    colors_even.add(colors.get(i));
                    Log.v("value", "even" + i);

                } else
                {
                    data_lable_odd.add(chart_optionArrayList.get(i).getOption_title());
                    colors_odd.add(colors.get(i));
                    Log.v("value", "odd" + i);

                }
            }
            adapter=new ChartAdapter(BarchartActivity.this,data_lable_odd,colors_odd);
            lstlableodd.setAdapter(adapter);
            adapter1=new ChartAdapter1(BarchartActivity.this,data_lable_even,colors_even);
            lstlableeven.setAdapter(adapter1);

        }
    }


}
