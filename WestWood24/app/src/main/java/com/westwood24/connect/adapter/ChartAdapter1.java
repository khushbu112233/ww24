package com.westwood24.connect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.westwood24.R;
import com.westwood24.connect.utils.CircularImageView;

import java.util.List;

public class ChartAdapter1 extends BaseAdapter {

    public static List<String> lableArrayList;
    public static List<Integer> color_list;



    private Context context;

    public ChartAdapter1(Context context, List<String> lableArrayList, List<Integer> color_list) {
        this.context = context;
        this.lableArrayList = lableArrayList;
        this.color_list=color_list;


    }

    @Override
    public int getCount() {
        if (lableArrayList != null) {
            return lableArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public String getItem(int position) {
        if (lableArrayList != null) {
            return lableArrayList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        TextView txtlable;
        CircularImageView view_circle;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_chart_list, null);
        view_circle=(CircularImageView)v.findViewById(R.id.view_circle);
        txtlable = (TextView) v.findViewById(R.id.txtlable);
        txtlable.setText(lableArrayList.get(position));
        view_circle.setBackgroundColor(color_list.get(position));
       // view_circle.setBackgroundTintList(ColorStateList.valueOf(color_list.get(position)));
       // LayerDrawable shape= (LayerDrawable)v.getBackground();
        //Drawable drawable = context.getResources().getDrawable(R.drawable.bg_circle_blue);
        //drawable.setColorFilter(color_list.get(position), PorterDuff.Mode.SRC_ATOP);
      //  bgShape.setColor(color_list.get(position));

        return v;
    }




}
