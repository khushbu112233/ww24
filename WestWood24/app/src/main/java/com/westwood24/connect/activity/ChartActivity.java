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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.westwood24.R;
import com.westwood24.connect.adapter.ChartAdapter;
import com.westwood24.connect.adapter.ChartAdapter1;
import com.westwood24.connect.font.TextViewMyriad_bld;
import com.westwood24.connect.model.Chart_Option;
import com.westwood24.connect.utils.Pref;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    ListView lstlableodd;
    ListView lstlableeven;
    TextViewMyriad_bld question;

    ArrayList<String> data_lable_odd=new ArrayList<String>();
    ArrayList<String> data_lable_even=new ArrayList<String>();
    ArrayList<Integer> colors_odd = new ArrayList<Integer>();
    ArrayList<Integer> colors_even = new ArrayList<Integer>();
    ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();
    ArrayList<Chart_Option> chart_optionArrayList=new ArrayList<>();
    RelativeLayout mHeaderBackRl;
    ChartAdapter adapter;
    ChartAdapter1 adapter1;
    ImageView img_left;
    Intent intent;
    TextView mHeaderTitleTv;
    TextView txt_title_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        lstlableodd=(ListView)findViewById(R.id.lstlableodd);
        lstlableeven=(ListView)findViewById(R.id.lstlableeven);
        question=(TextViewMyriad_bld)findViewById(R.id.question);
        mHeaderBackRl = (RelativeLayout) findViewById(R.id.header_layout_back_rl);
        img_left = (ImageView)findViewById(R.id.img_left);
       txt_title_id = (TextView)findViewById(R.id.txt_title_id);
        mHeaderTitleTv = (TextView) findViewById(R.id.txt_title);
        question.setText("Que: "+Pref.getValue(ChartActivity.this,"question",""));

        intent=getIntent();
        //     chart_optionArrayList=intent.getParcelableExtra("chart_optionArrayList");
        chart_optionArrayList=(ArrayList<Chart_Option>) getIntent().getSerializableExtra("chart_optionArrayList");
        Log.e("option", chart_optionArrayList.size() + "--");

        PieChart chart = (PieChart) findViewById(R.id.chart1);
        configureChart(chart);
        setchartdata(chart);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setHoleRadius(0f);
        //chart.setTransparentCircleRadius(61f);
        chart.animateXY(1500, 1500);
        txt_title_id.setText(Pref.getValue(ChartActivity.this, "Htitle", ""));
        mHeaderBackRl.setVisibility(View.GONE);
        img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public PieChart configureChart(PieChart chart) {
        chart.setHoleColor(getResources().getColor(android.R.color.background_dark));
        chart.setHoleRadius(60f);
        chart.setDescription("");
        chart.setTransparentCircleRadius(5f);
       // chart.setDrawYValues(true);
        chart.setDrawCenterText(true);
        chart.setDrawHoleEnabled(false);
        chart.setRotationAngle(0);
       // chart.setDrawXValues(false);
        chart.setRotationEnabled(true);
        chart.setUsePercentValues(true);
        chart.setCenterText("Audience Poll");
        return chart;
    }

    public void setchartdata(PieChart chart)
    {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for(int i=0;i<chart_optionArrayList.size();i++) {
            entries.add(new PieEntry(Integer.parseInt(chart_optionArrayList.get(i).getOption_value())));
        }
        /*entries.add(new PieEntry(25.0f, ""));
        entries.add(new PieEntry(25.0f, ""));
        entries.add(new PieEntry(20.0f, ""));
        entries.add(new PieEntry(30.0f, ""));
*/
        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        int[] rainbow = getResources().getIntArray(R.array.androidcolors);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i = 0; i < chart_optionArrayList.size(); i++) {
            colors.add(rainbow[i]);
            // Do something with the paint.
        }
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        chart.setDrawEntryLabels(true);
        chart.setData(data);
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
        adapter=new ChartAdapter(ChartActivity.this,data_lable_odd,colors_odd);
        lstlableodd.setAdapter(adapter);
        adapter1=new ChartAdapter1(ChartActivity.this,data_lable_even,colors_even);
        lstlableeven.setAdapter(adapter1);

    }





}