package com.westwood24.connect.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.westwood24.R;
import com.westwood24.connect.activity.BarchartActivity;
import com.westwood24.connect.activity.ChartActivity;
import com.westwood24.connect.activity.CommentListOfArticleActivity;
import com.westwood24.connect.activity.DashBoardActivity;
import com.westwood24.connect.activity.DetailOfVideoImageActivity;
import com.westwood24.connect.activity.SplashActivity;
import com.westwood24.connect.fragment.TopNewsFirstFragment;
import com.westwood24.connect.model.AnswerStatus;
import com.westwood24.connect.model.Chart_Option;
import com.westwood24.connect.utils.Config;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.FontCustom;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import eu.janmuller.android.simplecropimage.Util;

public class VideoImageAdapter extends BaseAdapter implements WebserviceCall.WebserviceResponse {
    public static  ArrayList<HashMap<String, String>> listData;
    public static ArrayList<HashMap<String,ArrayList<String>>> listdata1;
    LayoutInflater layoutInflater;
    Context context;
    int offset;
    String value;
    String days;
    TextView txt[]=new TextView[100],txt1[] = new TextView[100];
    RadioButton rb[]=new RadioButton[100];
    TextView text_radio[][]=new TextView[100][100];
    TextView txt_rbCount[] = new TextView[100];
    int radio_flag =0;
    RadioGroup rg_que;
    String type;
    int RECOVERY_DIALOG_REQUEST = 1;
    String typeoption;
    String questionid;
    String authkey;
    String diff_month = "";
    String articleId;
    String str_new="";
    boolean youtube_webview_flage = true;
     View convertView;
    public  int  position;

    ArrayList<Chart_Option> chart_optionArrayList = new ArrayList<>();

    public VideoImageAdapter(Context context, ArrayList<HashMap<String, String>> listData, ArrayList<HashMap<String, ArrayList<String>>> listdata1, String value) {
        this.context = context;
        this.listData = listData;
        this.listdata1 = listdata1;
        this.value = value;

        Log.e("Adapter data",listdata1+"");

        position = 0;
    }

    @Override
    public int getCount() {
        return listdata1.size();

    }
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position,View convertView, ViewGroup parent) {
       // View convertView = super.getView(position, convertView1, parent);


        layoutInflater = ((FragmentActivity) context).getLayoutInflater();
        final ViewHolder holder;



        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.thumbImage = (ImageView) convertView.findViewById(R.id.thumbImage);
            holder.txtname = (TextView) convertView.findViewById(R.id.txtname);
            holder.videoimage = (WebView) convertView.findViewById(R.id.videoimage);
            holder.play = (ImageView) convertView.findViewById(R.id.play);
            holder.txtvideotitle = (TextView) convertView.findViewById(R.id.txtvideotitle);
            holder.mCommentTv = (TextView) convertView.findViewById(R.id.list_row_comment_tv);
            holder.mViewersNumberTv = (TextView) convertView.findViewById(R.id.list_row_viewuser_tv);
            holder.mCommentLikeTv = (TextView) convertView.findViewById(R.id.list_row_likeofuser_tv_tv);
            holder.list_row_watching_time_tv = (TextView)convertView.findViewById(R.id.list_row_watching_time_tv);
            holder.list_row_top_rl = (RelativeLayout)convertView.findViewById(R.id.list_row_top_rl);
            holder.list_row_header_ln2=(LinearLayout)convertView.findViewById(R.id.list_row_header_ln2);
            holder.list_row_header_ln1=(RelativeLayout)convertView.findViewById(R.id.list_row_header_ln1);
            holder.list_row_header_ln3 = (RelativeLayout)convertView.findViewById(R.id.list_row_header_ln3);
            holder.img_pie = (ImageView)convertView.findViewById(R.id.img_pie);
            holder.img_bar = (ImageView)convertView.findViewById(R.id.img_bar);
            holder.que_save = (TextView)convertView.findViewById(R.id.que_save);
            holder.ll_graph = (LinearLayout)convertView.findViewById(R.id.ll_graph);
            holder.rl_see_more = (RelativeLayout)convertView.findViewById(R.id.rl_see_more);
            holder.que_title = (TextView)convertView.findViewById(R.id.que_title);
            holder.txt_view_more = (TextView)convertView.findViewById(R.id.txt_view_more);
            holder.txtvideotitle.setTypeface(FontCustom.setFontBold(context));
            holder.title.setTypeface(FontCustom.setFont(context));
            holder.title.setTextColor(Color.parseColor("#000000"));
            holder.txt_view_more.setTypeface(FontCustom.setFont(context));
            holder.que_save.setTypeface(FontCustom.setFont(context));
            holder.que_title.setTypeface(FontCustom.setFontBold(context));

            convertView.setTag(holder);

        } else {

          holder = (ViewHolder) convertView.getTag();
       }


        Log.v("pos",position+"");

        //rb=new RadioButton[100];


            Pref.setValue(context, "Htitle", listData.get(position).get("title"));


            //   Log.e("timenew","" +time);
            if (listData.get(position).get("question_type").equalsIgnoreCase("radio")) {

                if (listData.get(position).get("votable").equalsIgnoreCase("0")) {
                    Log.e("votai0", "" + listData.get(position).get("votable"));
                    LinearLayout ll2 = new LinearLayout(context);
                    ll2.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams layoutForInner1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ll2.setLayoutParams(layoutForInner1);

                    for (int i = 0; i < listdata1.get(position).get("options_count").size(); i++) {


                        // holder.list_row_header_ln2.removeAllViews();
                        LinearLayout ll1 = new LinearLayout(context);
                        ll1.setOrientation(LinearLayout.HORIZONTAL);
                        ll1.setWeightSum(1f);

                        LinearLayout.LayoutParams layoutForInner = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutForInner.weight = 1.0f;
                        ll1.setLayoutParams(layoutForInner);

                        txt[i] = new TextView(context);
                        txt[i].setTypeface(FontCustom.setFontBold(context));
                        txt[i].setTextColor(Color.BLACK);
                        txt[i].setTextSize(14);
                        txt[i].setTypeface(FontCustom.setFontBold(context));
                        txt[i].setText(listdata1.get(position).get("question_options").get(i));
                        txt[i].setPadding(35, 5, 0, 5);
                        LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                        childParam1.weight = 0.5f;
                        txt[i].setLayoutParams(childParam1);
                        ll1.addView(txt[i]);

                        txt1[i] = new TextView(context);
                        txt1[i].setTypeface(FontCustom.setFontBold(context));
                        txt1[i].setTextColor(Color.BLACK);
                        txt1[i].setTypeface(FontCustom.setFontBold(context));
                        txt1[i].setTextSize(14);
                        txt1[i].setText(listdata1.get(position).get("options_count").get(i));
                        txt1[i].setPadding(0, 5, 15, 5);
                        LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                        childParam2.weight = 0.5f;
                        txt1[i].setLayoutParams(childParam2);
                        ll1.addView(txt1[i]);
                        ll2.addView(ll1);
                    }

                    holder.list_row_header_ln1.setVisibility(View.VISIBLE);
                    holder.list_row_header_ln3.setVisibility(View.VISIBLE);
                    holder.ll_graph.setVisibility(View.VISIBLE);
                    holder.que_save.setVisibility(View.GONE);

                    holder.list_row_header_ln2.removeAllViews();
                    holder.list_row_header_ln2.addView(ll2);

                    holder.que_title.setText(listData.get(position).get("question_title"));
                } else if (listData.get(position).get("votable").equalsIgnoreCase("1")) {


                    LinearLayout ll2 = new LinearLayout(context);
                    ll2.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams layoutForInner1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ll2.setLayoutParams(layoutForInner1);

                    //rg_que = new RadioGroup(context);
                    LinearLayout ll3 = new LinearLayout(context);
                    ll3.setOrientation(LinearLayout.VERTICAL);
                    ll3.setWeightSum(1.0f);
                    int count_radio = 0;
                    LinearLayout ll_new = null;

                    for (int i = 0; i < listdata1.get(position).get("options_count").size(); i++) {

                        //    rb[i].setText(listdata1.get(position).get("question_options").get(i).toString()+"  ("+listdata1.get(position).get("options_count").get(i).toString()+")");


                        LinearLayout ll = new LinearLayout(context);
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.setPadding(30, 0, 30, 0);
                        if (listdata1.get(position).get("options_count").size() % 2 == 0) {
                            for (int temp = 0; temp < 2; temp++) {

                                text_radio[position][i] = new TextView(context);


                                text_radio[position][i].setText(listdata1.get(position).get("question_options").get(i).toString() + "  (" + listdata1.get(position).get("options_count").get(i).toString() + ")");
                                SpannableStringBuilder ssb = new SpannableStringBuilder(listdata1.get(position).get("question_options").get(i).toString() + "  (" + listdata1.get(position).get("options_count").get(i).toString() + ")");
                                //ssb.append("By registering you are agreeing to our privacy policy and terms of use.");

                                final int finalI = i;
                                txt_rbCount[i] = new TextView(context);

                                ssb.setSpan(new ClickableSpan() {

                                    @Override
                                    public void onClick(View v) {
                                        txt_rbCount[finalI].callOnClick();
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(context.getResources().getColor(R.color.Black));
                                        ds.setUnderlineText(false);
                                        ds.setTextSize(context.getResources().getDimension(R.dimen.description_size));
                                    }

                                }, 0, listdata1.get(position).get("question_options").get(i).toString().length(), 0);

                                ssb.setSpan(new ClickableSpan() {

                                    @Override
                                    public void onClick(View v) {
                                        txt_rbCount[finalI].callOnClick();
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(context.getResources().getColor(R.color.Black));
                                        ds.setUnderlineText(false);
                                        ds.setTextSize(context.getResources().getDimension(R.dimen.lablesize1));
                                    }

                                }, listdata1.get(position).get("question_options").get(i).toString().length() + 2, text_radio[position][i].getText().toString().length(), 0);
                                // text_radio[position][i].setMovementMethod(LinkMovementMethod.getInstance());
                                text_radio[position][i].setText(ssb);


                                text_radio[position][i].setTextColor(Color.BLACK);
                                text_radio[position][i].setBackgroundColor(context.getResources().getColor(R.color.white));
                                text_radio[position][i].setId(i);
                                text_radio[position][i].setPadding(5, -2, 0, 5);

                                text_radio[position][i].setGravity(Gravity.CENTER_VERTICAL);
                                text_radio[position][i].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.uncheck_black, 0, 0, 0);
                                text_radio[position][i].setCompoundDrawablePadding(25);

                                LinearLayout.LayoutParams childParamvote1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                                childParamvote1.weight = 0.5f;
                                text_radio[position][i].setLayoutParams(childParamvote1);
                                text_radio[position][i].setTypeface(FontCustom.setFont(context));

                                ll.addView(text_radio[position][i]);
                                final int finalI1 = i;
                                text_radio[position][i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        radio_flag++;
                                        typeoption = text_radio[position][finalI1].getText().toString().split("\\(")[0].trim();
                                        Log.e("typeoption", "" + typeoption);

                                        for (int k = 0; k < listdata1.get(position).get("options_count").size(); k++) {
                                            Log.e("textradio value", "" + text_radio[position][finalI1].getId());
                                            if (finalI1 == text_radio[position][k].getId()) {
                                                //   text_radio[finalI][k].setTextColor(Color.RED);
                                                text_radio[position][k].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_black, 0, 0, 0);

                                                text_radio[position][k].setBackgroundColor(context.getResources().getColor(R.color.white));


                                            } else {
                                                //  text_radio[finalI][k].setTextColor(Color.WHITE);
                                                text_radio[position][k].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.uncheck_black, 0, 0, 0);

                                                text_radio[position][k].setBackgroundColor(context.getResources().getColor(R.color.white));
                                            }
                                        }
                                    }
                                });

                                i++;

                            }
                            i--;
                        } else {
                            for (int temp = 0; temp < 2; temp++) {
                                text_radio[position][i] = new TextView(context);
                                text_radio[position][i].setText(listdata1.get(position).get("question_options").get(i).toString() + "  (" + listdata1.get(position).get("options_count").get(i).toString() + ")");

                                SpannableStringBuilder ssb = new SpannableStringBuilder(listdata1.get(position).get("question_options").get(i).toString() + "  (" + listdata1.get(position).get("options_count").get(i).toString() + ")");
                                //ssb.append("By registering you are agreeing to our privacy policy and terms of use.");
                                txt_rbCount[i] = new TextView(context);
                                final int finalI = i;
                                ssb.setSpan(new ClickableSpan() {

                                    @Override
                                    public void onClick(View v) {
                                        txt_rbCount[finalI].callOnClick();
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(context.getResources().getColor(R.color.Black));
                                        ds.setUnderlineText(false);
                                        ds.setTextSize(context.getResources().getDimension(R.dimen.description_size));
                                    }

                                }, 0, listdata1.get(position).get("question_options").get(i).toString().length(), 0);

                                ssb.setSpan(new ClickableSpan() {

                                    @Override
                                    public void onClick(View v) {
                                        txt_rbCount[finalI].callOnClick();
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(context.getResources().getColor(R.color.Black));
                                        ds.setUnderlineText(false);
                                        ds.setTextSize(context.getResources().getDimension(R.dimen.lablesize1));
                                    }

                                }, listdata1.get(position).get("question_options").get(i).toString().length() + 2, text_radio[position][i].getText().toString().length(), 0);
                                //  text_radio[position][i].setMovementMethod(LinkMovementMethod.getInstance());
                                text_radio[position][i].setText(ssb);


                                text_radio[position][i].setTextColor(Color.BLACK);

                                text_radio[position][i].setGravity(Gravity.CENTER_VERTICAL);
                                text_radio[position][i].setBackgroundColor(context.getResources().getColor(R.color.white));
                                text_radio[position][i].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.uncheck_black, 0, 0, 0);
                                text_radio[position][i].setCompoundDrawablePadding(25);
                                text_radio[position][i].setId(i);
                                text_radio[position][i].setPadding(5, -2, 0, 5);
                                text_radio[position][i].setTypeface(FontCustom.setFont(context));
                                LinearLayout.LayoutParams childParamvote1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                                childParamvote1.weight = 0.5f;
                                text_radio[position][i].setLayoutParams(childParamvote1);

                                ll.addView(text_radio[position][i]);
                                final int finalI1 = i;

                                text_radio[position][i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Log.e("textradio value", "" + text_radio[finalI][finalJ].getId());
                                        typeoption = text_radio[position][finalI1].getText().toString().split("\\(")[0].trim();
                                        Log.e("typeoption", "" + typeoption);

                                        radio_flag++;
                                        for (int k = 0; k < listdata1.get(position).get("options_count").size(); k++) {
                                            Log.e("textradio value", "" + text_radio[position][finalI1].getId());

                                            if (finalI1 == text_radio[position][k].getId()) {
                                                //   text_radio[finalI][k].setTextColor(Color.RED);
                                                text_radio[position][k].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_black, 0, 0, 0);

                                                text_radio[position][k].setBackgroundColor(context.getResources().getColor(R.color.white));
                                            } else {
                                                //     text_radio[finalI][k].setTextColor(Color.WHITE);
                                                text_radio[position][k].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.uncheck_black, 0, 0, 0);

                                                text_radio[position][k].setBackgroundColor(context.getResources().getColor(R.color.white));
                                            }
                                        }

                                        // text_radio[finalI][finalJ].setId(finalJ);
                                    }
                                });

                                i++;
                                count_radio++;
                            }


                        }

                        ll3.addView(ll);
                        if (listdata1.get(position).get("options_count").size() % 2 != 0) {

                            if (count_radio == listdata1.get(position).get("options_count").size() - 1) {
                                ll_new = new LinearLayout(context);
                                ll_new.setOrientation(LinearLayout.HORIZONTAL);
                                ll_new.setPadding(30, 0, 30, 0);
                                for (int temp = 0; temp < 1; temp++) {

                                    text_radio[position][count_radio] = new TextView(context);
                                    text_radio[position][count_radio].setText(listdata1.get(position).get("question_options").get(count_radio).toString() + "  (" + listdata1.get(position).get("options_count").get(count_radio).toString() + ")");
                                    SpannableStringBuilder ssb = new SpannableStringBuilder(listdata1.get(position).get("question_options").get(i).toString() + "  (" + listdata1.get(position).get("options_count").get(i).toString() + ")");
                                    //ssb.append("By registering you are agreeing to our privacy policy and terms of use.");
                                    txt_rbCount[count_radio] = new TextView(context);
                                    final int finalI = count_radio;
                                    ssb.setSpan(new ClickableSpan() {

                                        @Override
                                        public void onClick(View v) {
                                            txt_rbCount[finalI].callOnClick();
                                        }

                                        @Override
                                        public void updateDrawState(TextPaint ds) {
                                            super.updateDrawState(ds);
                                            ds.setColor(context.getResources().getColor(R.color.Black));
                                            ds.setUnderlineText(false);
                                            ds.setTextSize(context.getResources().getDimension(R.dimen.description_size));
                                        }

                                    }, 0, listdata1.get(position).get("question_options").get(count_radio).toString().length(), 0);

                                    ssb.setSpan(new ClickableSpan() {

                                        @Override
                                        public void onClick(View v) {
                                            txt_rbCount[finalI].callOnClick();
                                        }

                                        @Override
                                        public void updateDrawState(TextPaint ds) {
                                            super.updateDrawState(ds);
                                            ds.setColor(context.getResources().getColor(R.color.Black));
                                            ds.setUnderlineText(false);
                                            ds.setTextSize(context.getResources().getDimension(R.dimen.lablesize1));
                                        }

                                    }, listdata1.get(position).get("question_options").get(count_radio).toString().length() + 2, text_radio[position][count_radio].getText().toString().length(), 0);
                                    //      text_radio[position][count_radio].setMovementMethod(LinkMovementMethod.getInstance());
                                    text_radio[position][count_radio].setText(ssb);


                                    text_radio[position][count_radio].setTextColor(Color.BLACK);

                                    ll_new.addView(text_radio[position][count_radio]);

                                    text_radio[position][count_radio].setGravity(Gravity.CENTER_VERTICAL);
                                    text_radio[position][count_radio].setBackgroundColor(context.getResources().getColor(R.color.white));
                                    text_radio[position][count_radio].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.uncheck_black, 0, 0, 0);
                                    text_radio[position][count_radio].setCompoundDrawablePadding(25);
                                    text_radio[position][count_radio].setId(i);
                                    text_radio[position][count_radio].setPadding(5, 5, 0, 5);
                                    LinearLayout.LayoutParams childParamvote1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                                    childParamvote1.weight = 0.5f;
                                    text_radio[position][count_radio].setLayoutParams(childParamvote1);
                                    text_radio[position][i].setTypeface(FontCustom.setFont(context));

//                                ll.addView(text_radio[position][count_radio]);
                                    final int finalI1 = count_radio;
                                    text_radio[position][count_radio].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Log.e("textradio value", "" + text_radio[finalI][finalJ].getId());
                                            typeoption = text_radio[position][finalI1].getText().toString().split("\\(")[0].trim();
                                            Log.e("typeoption", "" + typeoption);

                                            radio_flag++;
                                            for (int k = 0; k < listdata1.get(position).get("options_count").size(); k++) {
                                                Log.e("textradio value", "" + text_radio[position][finalI1].getId());

                                                if (finalI1 == text_radio[position][k].getId()) {
                                                    //   text_radio[finalI][k].setTextColor(Color.RED);
                                                    text_radio[position][k].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_black, 0, 0, 0);

                                                    text_radio[position][k].setBackgroundColor(context.getResources().getColor(R.color.white));
                                                } else {
                                                    //     text_radio[finalI][k].setTextColor(Color.WHITE);
                                                    text_radio[position][k].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.uncheck_black, 0, 0, 0);

                                                    text_radio[position][k].setBackgroundColor(context.getResources().getColor(R.color.white));
                                                }
                                            }

                                            // text_radio[finalI][finalJ].setId(finalJ);
                                        }
                                    });
                                    count_radio++;
                                }
                                ll3.addView(ll_new);
                            }
                        }


                    }
                    //  ll3.addView(ll);

                    ll2.addView(ll3);
                    holder.list_row_header_ln2.removeAllViews();
                    holder.list_row_header_ln2.addView(ll2);
                    //add_chart(position, holder);

                    holder.list_row_header_ln1.setVisibility(View.VISIBLE);
                    holder.list_row_header_ln3.setVisibility(View.VISIBLE);
                    holder.ll_graph.setVisibility(View.VISIBLE);
                    holder.que_save.setVisibility(View.VISIBLE);

                    holder.que_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Log.e("answerflag", "" + listData.get(position).get("answerflag"));
                            int flag_que = 0;
                            if (Pref.getValue(context, Constants.TAG_USER_ID, "").equals("")) {
                                Utils.loginalert(context);
                                //  Utils.showAlert(context, context.getResources().getString(R.string.dialog_title), context.getResources().getString(R.string.dialog_msg));
                            } else {

                                int count1 = 0;
                                for (int j = 0; j < listdata1.get(position).get("options_count").size(); j++) {
                                    // rb[j] = new RadioButton(context);
                                    if (radio_flag > 0) {
                                        count1++;
                                        Log.e("count1adapter", "" + count1);
                                    }

                                }

                                for (int temp_answer = 0; temp_answer < SplashActivity.answer_status_list.size(); temp_answer++) {
                                    if (SplashActivity.answer_status_list.get(temp_answer).getArticle_ida().equalsIgnoreCase(listData.get(position).get("id"))) {
                                        if (SplashActivity.answer_status_list.get(temp_answer).getQuestion_ida().equalsIgnoreCase(listData.get(position).get("question_id"))) {
                                            if (SplashActivity.answer_status_list.get(temp_answer).getAnswer_flaga().equalsIgnoreCase("1")) {
                                                Log.e("question_id_adapter", "" + listData.get(position).get("question_id") + "-------------------stop");
                                                Utils.showAlert(context, "You already selected an answer for this question.");
                                                break;

                                            } else {
                                                Log.e("question_id_adapter", "execute--------------------------------");

                                                if (count1 == 0) {
                                                    Utils.showAlert(context, "Please mention your vote");
                                                    break;
                                                } else {
                                                    JSONArray jsonArray = new JSONArray();
                                                    JSONObject jsonObject = new JSONObject();
                                                    try {
                                                        jsonObject.put("questionid", listData.get(position).get("question_id"));
                                                        jsonObject.put("type", listData.get(position).get("question_type"));
                                                        jsonObject.put("articleId", listData.get(position).get("id"));
                                                        jsonObject.put("authkey", Pref.getValue(context, Constants.TAG_AUTHKAY, ""));
                                                        authkey = Pref.getValue(context, Constants.TAG_AUTHKAY, "");
                                                        articleId = listData.get(position).get("id");
                                                        type = listData.get(position).get("question_type");
                                                        questionid = listData.get(position).get("question_id");

                                                        // Log.e("radiobutton1",""+rb[0].getText());

                                                        for (int j = 0; j < listdata1.get(position).get("question_options").size(); j++) {
                                                            //   rb[j] = new RadioButton(context);
                                                            if (radio_flag > 0) {
                                                                // count_new ++;
                                                               /*JSONArray jsonArrayans = new JSONArray();
                                                                JSONObject jsonObject1 = new JSONObject();
                                                                jsonObject1.put("typeoption", rb[i][j].getText().toString());
                                                                jsonArrayans.put(jsonObject1);
                                                                jsonObject.put("answer", jsonArrayans);*/
                                                                // Log.e("radiobutton", "" + rb[j].getText());

                                                                // typeoption = rb[j].getText().toString().split("\\(")[0].toString().trim();
                                                                jsonObject.put("typeoption", typeoption);
                                                                jsonArray.put(jsonObject);

                                                            }

                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    callsetVoteApi();


                                                    AnswerStatus[] answerStatus = new AnswerStatus[1];
                                                    answerStatus[0] = new AnswerStatus();
                                                    answerStatus[0].setArticle_ida(listData.get(position).get("id"));
                                                    answerStatus[0].setQuestion_ida(listData.get(position).get("question_id"));
                                                    answerStatus[0].setAnswer_flaga("1");
                                                    SplashActivity.answer_status_list.add(answerStatus[0]);
                                                    break;
                                                }


                                            }
                                        }
                                    }
                                }

                            }
                        }

                    });

                    holder.list_row_header_ln2.setVisibility(View.VISIBLE);
                    holder.list_row_header_ln1.setVisibility(View.VISIBLE);
                    holder.que_save.setVisibility(View.VISIBLE);
                    holder.list_row_header_ln3.setVisibility(View.VISIBLE);
                    holder.ll_graph.setVisibility(View.VISIBLE);
                    holder.que_title.setText(listData.get(position).get("question_title"));
                }


            } else {
                holder.list_row_header_ln2.setVisibility(View.GONE);
                holder.list_row_header_ln1.setVisibility(View.GONE);
                holder.que_save.setVisibility(View.GONE);
                holder.list_row_header_ln3.setVisibility(View.GONE);
                holder.ll_graph.setVisibility(View.GONE);
            }


        //on click


        holder.img_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pref.setValue(context, "Htitle", listData.get(position).get("title"));
                Log.v("position_value",position+"-----"+listData.get(position).get("question_title"));
                Pref.setValue(context, "question", listData.get(position).get("question_title"));

                chart_optionArrayList = new ArrayList<>();
                Chart_Option[] chart_options = new Chart_Option[listdata1.get(position).get("options_count").size()];
                for (int count = 0; count < listdata1.get(position).get("question_options").size(); count++) {
                    if (listdata1.get(position).get("question_options").get(count).toString() != null) {
                        if (listdata1.get(position).get("question_options").get(count).toString().length() > 0) {
                            chart_options[count] = new Chart_Option();
                            chart_options[count].setOption_title(listdata1.get(position).get("question_options").get(count).toString());
                            chart_options[count].setOption_value(listdata1.get(position).get("options_count").get(count).toString());
                            chart_optionArrayList.add(chart_options[count]);
                        }
                    }
                }


                Intent intent = new Intent(context, BarchartActivity.class);
                intent.putExtra("chart_optionArrayList", chart_optionArrayList);
                context.startActivity(intent);
            }
        });
        holder.img_pie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chart_optionArrayList = new ArrayList<>();
                Pref.setValue(context, "Htitle", listData.get(position).get("title"));
                Pref.setValue(context, "question", listData.get(position).get("question_title"));
                Chart_Option[] chart_options = new Chart_Option[listdata1.get(position).get("question_options").size()];
                for (int count = 0; count < listdata1.get(position).get("question_options").size(); count++) {
                    if (listdata1.get(position).get("question_options").get(count).toString() != null) {
                        if (listdata1.get(position).get("question_options").get(count).toString().length() > 0) {
                            chart_options[count] = new Chart_Option();
                            chart_options[count].setOption_title(listdata1.get(position).get("question_options").get(count).toString());
                            chart_options[count].setOption_value(listdata1.get(position).get("options_count").get(count).toString());
                            chart_optionArrayList.add(chart_options[count]);
                        }
                    }
                }
                Pref.setValue(context, "question", listData.get(position).get("question_title"));
                Intent intent = new Intent(context, ChartActivity.class);
//                Log.e("chart_optionArrayList", "" + chart_optionArrayList.get(position).getOption_value());
                intent.putExtra("chart_optionArrayList", chart_optionArrayList);
                context.startActivity(intent);
            }
        });

        String dtStart = listData.get(position).get("posttime");
        //     String dtStart = "2016-09-14 11:18:47";
        Date date = null,date_curr = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long date_cur = System.currentTimeMillis();
        String dateString = format.format(date_cur);
        //String dateString = "2017-12-28 11:18:47";
        Log.e("currentdate",""+dateString);
        Log.e("listdate",""+listData.get(position).get("posttime"));
        try {
            date = format.parse(dtStart);
            date_curr = format.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long time_diff=  printDifference_new(date,date_curr);


        holder.title.setText(listData.get(position).get("description"));

        holder.list_row_watching_time_tv.setText(time_diff+diff_month);
        if (listData.get(position).get("article_type").equals("image")) {
            holder.txtvideotitle.setText(listData.get(position).get("title"));
            //   holder.list_row_watching_time_tv.setText(time_diff+diff_month);

            Picasso.with(context).load(listData.get(position).get("thumb_image")).fit().into(holder.thumbImage);
            holder.videoimage.setVisibility(View.GONE);
            holder.play.setVisibility(View.GONE);
            holder.txtvideotitle.setVisibility(View.VISIBLE);
            holder.title.setVisibility(View.VISIBLE);
            holder.thumbImage.setVisibility(View.VISIBLE);


            holder.list_row_top_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callvieweApi(position);
                    context.startActivity(new Intent(context, DetailOfVideoImageActivity.class)
                            .putExtra("vidImgTitle", holder.title.getText().toString())
                            .putExtra("image", listData.get(position).get("thumb_image"))
                            .putExtra("articleid", listData.get(position).get("id"))
                            .putExtra("articleType", listData.get(position).get("article_type"))
                            .putExtra("check", "Image")
                            .putExtra("votable", listData.get(position).get("votable")));
                }
            });
            holder.rl_see_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callvieweApi(position);
                    context.startActivity(new Intent(context, DetailOfVideoImageActivity.class).putExtra("vidImgTitle", holder.title.getText().toString()).putExtra("image", listData.get(position).get("thumb_image")).putExtra("articleid", listData.get(position).get("id")).putExtra("articleType", listData.get(position).get("article_type")).putExtra("check", "Image").putExtra("votable",listData.get(position).get("votable")));
                }
            });

        }else if(listData.get(position).get("article_type").equals("text"))
        {
            // holder.title.setText(listData.get(position).get("description"));
            holder.txtvideotitle.setText(listData.get(position).get("title"));

            //holder.list_row_watching_time_tv.setText(time_diff+diff_month);

            //  Picasso.with(context).load(listData.get(position).get("thumb_image")).fit().into(holder.thumbImage);
            holder.videoimage.setVisibility(View.GONE);
            holder.play.setVisibility(View.GONE);
            holder.txtvideotitle.setVisibility(View.VISIBLE);
            holder.title.setVisibility(View.VISIBLE);
            holder.thumbImage.setVisibility(View.GONE);

            holder.list_row_top_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callvieweApi(position);
                    context.startActivity(new Intent(context, DetailOfVideoImageActivity.class).putExtra("vidImgTitle", holder.title.getText().toString()).putExtra("image", listData.get(position).get("image")).putExtra("articleid", listData.get(position).get("id")).putExtra("articleType", listData.get(position).get("article_type")).putExtra("check", "Text").putExtra("votable", listData.get(position).get("votable")));
                }
            });
            holder.rl_see_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callvieweApi(position);
                    context.startActivity(new Intent(context, DetailOfVideoImageActivity.class).putExtra("vidImgTitle", holder.title.getText().toString()).putExtra("image", listData.get(position).get("image")).putExtra("articleid", listData.get(position).get("id")).putExtra("articleType", listData.get(position).get("article_type")).putExtra("check", "Text").putExtra("votable",listData.get(position).get("votable")));
                }
            });

        }else if(listData.get(position).get("article_type").equals("video")) {

            // holder.title.setVisibility(View.GONE);
            holder.thumbImage.setVisibility(View.GONE);
            //  holder.title.setText(listData.get(position).get("description"));
            holder.txtvideotitle.setText(listData.get(position).get("title"));
            holder.videoimage.setVisibility(View.VISIBLE);
            holder.play.setVisibility(View.VISIBLE);
            holder.txtvideotitle.setVisibility(View.VISIBLE);
            //   holder.list_row_watching_time_tv.setText(time+days);

            // Picasso.with(context).load(listData.get(position).get("thumb_image")).fit().into(holder.videoimage);

            holder.list_row_top_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callvieweApi(position);
                    context.startActivity(new Intent(context, DetailOfVideoImageActivity.class).putExtra("vidImgTitle", holder.txtvideotitle.getText().toString()).putExtra("image", listData.get(position).get("thumb_image")).putExtra("articleid", listData.get(position).get("id")).putExtra("articleType", listData.get(position).get("articleType")).putExtra("check", "Video").putExtra("votable",listData.get(position).get("votable")));
                }
            });

            holder.rl_see_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callvieweApi(position);
                    context.startActivity(new Intent(context, DetailOfVideoImageActivity.class).putExtra("vidImgTitle", holder.txtvideotitle.getText().toString()).putExtra("image", listData.get(position).get("thumb_image")).putExtra("articleid", listData.get(position).get("id")).putExtra("articleType", listData.get(position).get("articleType")).putExtra("check", "Video").putExtra("votable",listData.get(position).get("votable")));
                }
            });

            // int color =  getDominantColor(getBitmapFromURL(listData.get(position).get("thumb_image")));
            //  Log.e("color",""+color);
        }else if(listData.get(position).get("article_type").equals("youtubeUrl")) {

            // holder.title.setVisibility(View.GONE);
            holder.thumbImage.setVisibility(View.GONE);
            //   holder.title.setText(listData.get(position).get("description"));
            holder.txtvideotitle.setText(listData.get(position).get("title"));
            holder.videoimage.setVisibility(View.VISIBLE);
            holder.play.setVisibility(View.GONE);
            holder.txtvideotitle.setVisibility(View.VISIBLE);
            String str = listData.get(position).get("image");


            if (!str.equals("") && !str.isEmpty()) {
                if(str.contains("?"))
                {
                    String str1 = str.split("=")[1];

                    Log.e("str11", "" + str1);
                    if(str1.contains("&"))
                    {
                        str_new = str1.split("&")[0];
                        Log.e("str1", "" + str1);

                    }else {
                        str_new = str1;
                        Log.e("str1", "" + str1);

                    }
                }else {
                    String str1 = str.substring(str.lastIndexOf("."));
                    Log.e("str12", "" + str1);
                    str_new = str1.split("/")[1];
                }

            } else {
                Utils.showAlert(context, "Video is not available!");
            }
            String url = "http://img.youtube.com/vi/"+str_new+"/0.jpg";

            DisplayMetrics metrics = new DisplayMetrics();
            ((FragmentActivity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int height = metrics.heightPixels;
            int width = (metrics.widthPixels/2)+50;
            int width1=(metrics.widthPixels/2);
            final float inPixels = context.getResources().getDimension(R.dimen.list_size);
            String  HtmlString1 = "<html><head><title>Description</title></head><body text="+"white"+">";
            String YoutubeString="";
            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                YoutubeString="<iframe width=100%"+" height="+230+" class =\"youtube-player\" src=\"https://www.youtube.com/embed/"+str_new +"\" frameborder=\"0\" allowfullscreen style=\"+\"width:150px;height:150px; margin-right: 10px\" align=\"left\" ></iframe>";

            }else
            {
                YoutubeString="<iframe width=100%"+" height="+width1+" class =\"youtube-player\" src=\"https://www.youtube.com/embed/"+str_new +"\" frameborder=\"0\" allowfullscreen style=\"+\"width:150px;height:150px; margin-right: 10px\" align=\"left\" ></iframe>";

            }

            // YoutubeString = "<iframe width="+200+"height="+200+"src="+"http://www.youtube.com/embed/"+str_new+" class="+"youtube-player"+" frameborder="+1+"allowfullscreen style="+"background:white; position:relative; float:left; margin-right: 10px; left:0; "+"></iframe>";
            HtmlString1 = HtmlString1.concat(YoutubeString);

            String   EndingStatment= "</body></html>";
            HtmlString1 = HtmlString1.concat(EndingStatment);
            Log.e("HtmlString", "" + HtmlString1);

            //  webview.loadUrl(HtmlString);
            holder.videoimage.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    youtube_webview_flage = false;


                }
            });

                      /*  mWebViewClient = new myWebViewClient();
                        webview.setWebViewClient(mWebViewClient);

                        mWebChromeClient = new myWebChromeClient();
                        webview.setWebChromeClient(mWebChromeClient);
                   */
            holder.videoimage.setBackgroundColor(Color.TRANSPARENT);
            holder.videoimage.getSettings().setJavaScriptEnabled(true);
            holder.videoimage.loadData(HtmlString1, "text/html", "UTF-8");


            // holder.videoimage.initialize(Config.DEVELOPER_KEY, this);
            //  holder.list_row_watching_time_tv.setText(time + days);
            //   Picasso.with(context).load(url).fit().into(holder.videoimage);

            holder.list_row_top_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callvieweApi(position);

                    Log.v("position",position+"");
                    context.startActivity(new Intent(context, DetailOfVideoImageActivity.class)
                            .putExtra("vidImgTitle", holder.txtvideotitle.getText().toString())
                            .putExtra("image", listData.get(position).get("image"))
                            .putExtra("articleid", listData.get(position).get("id"))
                            .putExtra("articleType", listData.get(position).get("articleType"))
                            .putExtra("check", "YouTube")
                            .putExtra("votable", listData.get(position).get("votable")));
                }
            });

            holder.rl_see_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callvieweApi(position);
                    context.startActivity(new Intent(context, DetailOfVideoImageActivity.class)
                            .putExtra("vidImgTitle", holder.txtvideotitle.getText().toString())
                            .putExtra("image", listData.get(position).get("image"))
                            .putExtra("articleid", listData.get(position).get("id"))
                            .putExtra("articleType",listData.get(position).get("articleType"))
                            .putExtra("check", "YouTube")
                            .putExtra("votable", listData.get(position).get("votable")));
                }
            });

        }
        holder.mCommentLikeTv.setText(listData.get(position).get("no_commenters"));
        holder.mViewersNumberTv.setText(listData.get(position).get("no_viewers"));
        //holder.mCommentTv.setTag(listData.get(position).getArticleId());
        holder.mCommentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String position = String.valueOf(v.getTag());
                context.startActivity(new Intent(context, CommentListOfArticleActivity.class).putExtra("articleid", listData.get(position).get("articleId")));
            }
        });

        Log.v("position",position+" "+listData.size());

        if(listData.size()==position+1 && TopNewsFirstFragment.list.size()>=10)
        {
            Log.v("hello"," "+listData.size());
            Pref.setValue(context, "offset_value", listData.size() + "");
            Log.v("hello", "offset; " + Pref.getValue(context, "offset_value", ""));
            Pref.setValue(context, "swipe", "2");
            TopNewsFirstFragment.callTopNewsDetailApi();
        }

/*        if(value.equalsIgnoreCase("first"))
        {
            if(SplashActivity.list.size()==position+1) {
                if(Pref.getValue(context, "last", "").equalsIgnoreCase("0")) {

                    offset = Integer.parseInt(Pref.getValue(context,"offset",""))+1;
                    Pref.setValue(context,"offset",String.valueOf(offset));
                    new SplashActivity().callTopNewsDetailApi();
                }
                Log.e("offset adapter", "" + Pref.getValue(context, "offset", ""));
                Log.e("groupPosition",""+position);


            }
        }*/

        return convertView;
    }
    public void callvieweApi(int position){
        try {
            RequestParams requestParams = new RequestParams();

            requestParams.put("articleId", listData.get(position).get("id"));
            WebserviceCall webserviceCall = new WebserviceCall(context);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.ADDVIEWER_OF_QUE_URL, "addviewer", "post", requestParams);
            Log.e("test ad", "request is add viewer-------" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callsetVoteApi() {
        try {
            RequestParams requestParams = new RequestParams();
            if (Pref.getValue(context, "user_type", "").equalsIgnoreCase("login")) {
                requestParams.put("authkey", Pref.getValue(context, Constants.TAG_AUTHKAY, ""));
            }
            requestParams.put("articleId", articleId);
            requestParams.put("type", type);
            requestParams.put("typeoption",typeoption);
            requestParams.put("questionid",questionid);
            // requestParams.put("answer", answer);
            WebserviceCall webserviceCall = new WebserviceCall(context);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.SETVOTE_OF_QUE_URL, "addvote", "post", requestParams);
            Log.e("test ad", "request is set vote-------" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void success(String url, String apiName, String response) {
        Log.e("response", "response" + response);

        Log.e("vieweradded", "vieweradded");
        JSONObject mainObj = null;
        try {
            mainObj = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String response_code = mainObj.optString("code");

        if (response_code.equals("200")) {
            if (apiName.equals("addvote")) {

                // myCustomeDialog.dismiss();
                //  Log.e("addvote","success");
                TopNewsFirstFragment.callTopNewsDetailApi();
               /* Intent i = new Intent(context,DashBoardActivity.class);
                context.startActivity(i);*/
                String message = mainObj.optString("message");
                Utils.showToast(context, message);


            }else if(apiName.equals("addviewer")){

                Log.e("vieweradded", "vieweradded");
              /*  String message = mainObj.optString("message");
                Utils.showToast(context, message);
*/
            }else {


                String message = mainObj.optString("message");
                Utils.showToast(context, message);
            }
        }else if(response_code.equals("1000")){
            String message = mainObj.optString("message");

            Pref.deleteAll(context);

            DashBoardActivity.mDrawerLayout.closeDrawers();
            context.startActivity(new Intent(context, SplashActivity.class));
            Pref.setValue(context, "user_type", "guest");
            Utils.showToast(context, message);
        } else {

            String message = mainObj.optString("message");
            Utils.showToast(context, message);

        }
    }

    @Override
    public void failure(String url, String apiName, String response) {
        JSONObject mainObj = null;

        try {
            mainObj = new JSONObject(response);

            String message = mainObj.optString("message");
            Utils.showToast(context, message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
  /*  @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog((FragmentActivity)context, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format("There was an error initializing the YouTubePlayer ", errorReason.toString());
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            player.loadVideo(str_new);

            // Hiding player controls
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView)findViewById(R.id.videoimage);
    }*/

    public static class ViewHolder extends Fragment{
        RelativeLayout list_row_top_rl,list_row_header_ln1,list_row_header_ln3;
        TextView title;
        ImageView thumbImage;
        TextView txtname;
        WebView videoimage;
        ImageView play;
        TextView txtvideotitle;
        TextView mCommentTv;
        TextView mViewersNumberTv;
        TextView que_title,que_save;
        TextView mCommentLikeTv,list_row_watching_time_tv;
        LinearLayout list_row_header_ln2,ll_graph;
        ImageView img_bar,img_pie;
        RelativeLayout rl_see_more;
        TextView txt_view_more;
    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }
    public Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("Width", "src " + src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Width", "myBitmap " + myBitmap);
            return myBitmap;
        } catch (IOException e) {
// Log exception
            return null;
        }
    }
    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
    public  void addalldata(ArrayList<HashMap<String,String>> list_new,ArrayList<HashMap<String, ArrayList<String>>> listdata1)
    {
        this.listData.addAll(list_new);
        this.listdata1.addAll(listdata1);
        //notifyDataSetChanged();
    }
    public  void addalldata1(ArrayList<HashMap<String,ArrayList<String>>> list_new)
    {
        this.listdata1.addAll(list_new);
        notifyDataSetChanged();
    }

    public long printDifference_new(Date startDate, Date endDate){

//milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long weekInMilli = daysInMilli * 7;
        long monthinMilli=daysInMilli*30;

        Log.e("weekInMilli",""+weekInMilli);
        long elapsedMonth=different/monthinMilli;
        different=different%monthinMilli;

        long different1 = endDate.getTime() - startDate.getTime();
        long elapsedWeek = different1 / weekInMilli;
        different1 = different1 % weekInMilli;



        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;



        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;



        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;



        long elapsedSeconds = different / secondsInMilli;
        long diff = 0;

        Log.e("diff",elapsedDays+" "+ elapsedHours+" "+elapsedMinutes+" "+elapsedSeconds+" "+elapsedWeek+" "+elapsedMonth);

        if(elapsedMonth>0)
        {
            Log.v("diffvalue",elapsedMonth+"Month");
            diff = elapsedMonth;
            diff_month="M";
        }else if(elapsedWeek>0)
        {
            diff = elapsedWeek;
            diff_month="w";
        }
        else if(elapsedDays>0)
        {
            Log.v("diffvalue",elapsedDays+"Day");
            diff = elapsedDays;
            diff_month="d";

        }
        else if(elapsedHours>0)
        {
            Log.v("diffvalue",elapsedHours+"Hour");
            diff = elapsedHours;
            diff_month="h";
        }
        else if(elapsedMinutes>0)
        {
            diff = elapsedMinutes;
            Log.v("diffvalue",elapsedMinutes+"Minutes");
            diff_month="m";
        }
        else if(elapsedSeconds>0)
        {
            diff = elapsedSeconds;
            diff_month="s";
            Log.v("diffvalue",elapsedSeconds+"Seconds");
        }
        return diff;
    }

    public long printDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime()/1000 - startDate.getTime()/1000;

        System.out.println("startDate : " + startDate.getTime());
        System.out.println("endDate : "+ endDate.getTime());
        System.out.println("different : " + different/3600);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        long elipse = 0;
        //  Log.e("days",""+elapsedDays+"d");
        // Log.e("hour",""+elapsedHours+"h");
        //Log.e("minute",""+elapsedMinutes+"m");
        //Log.e("second",""+elapsedSeconds+"s");
        if(different/3600<=12)
        {
            elipse = different/3600;
            days="h";
        }else {
            if(different/3600>12) {
                elipse = different / 3600 / 12;
                days = "d";
            }else {
                if (different / 3600 / 12 <= 30) {
                    elipse = different / 3600 / 12 / 30;
                    days = "m";
                }else {
                    elipse = different / 3600 / 12 / 30 / 12;
                    days = "y";
                }
            }
        }
       /* if(different<60)
        {
            elipse=different;
            Log.e("s",""+elipse);
            days="s";
        }else if(different<3600){
            elipse=different/60;

            Log.e("m",""+elipse);
            days="m";
        }else if(different/3600<24)
        {
            elipse = different/3600/24;

            Log.e("h",""+elipse);
            days="h";
        }else if(different/3600/24<30)
        {
            elipse = different/3600/24/30;

            Log.e("d",""+elipse);
            days="d";
        }*/
        return elipse;
    }


}
