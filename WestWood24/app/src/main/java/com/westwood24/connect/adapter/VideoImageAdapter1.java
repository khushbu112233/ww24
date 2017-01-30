package com.westwood24.connect.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.westwood24.R;
import com.westwood24.connect.activity.BarchartActivity;
import com.westwood24.connect.activity.ChartActivity;
import com.westwood24.connect.activity.CommentListOfArticleActivity;
import com.westwood24.connect.activity.DashBoardActivity;
import com.westwood24.connect.activity.DetailOfVideoImageActivity;
import com.westwood24.connect.activity.SplashActivity;
import com.westwood24.connect.model.Chart_Option;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class VideoImageAdapter1 extends BaseAdapter implements WebserviceCall.WebserviceResponse {
    public ArrayList<HashMap<String, String>> listData;
    public ArrayList<HashMap<String,ArrayList<String>>> listdata1;
    private LayoutInflater layoutInflater;
    Context context;
    int offset;
    String value;
    String days;
    TextView txt[],txt1[];
    RadioButton rb[];
    RadioGroup rg_que;

    String type;
    String typeoption;
    String questionid;
    String authkey;
    String articleId;
    ArrayList<Chart_Option> chart_optionArrayList = new ArrayList<>();
    public VideoImageAdapter1(Context context, ArrayList<HashMap<String, String>> listData, ArrayList<HashMap<String, ArrayList<String>>> listdata1, String value) {
        this.context = context;
        this.listData = listData;
        this.listdata1 = listdata1;
        this.value = value;

        Log.e("Adapter data",listdata1+"");
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public View getView(final int position, View convertView, ViewGroup parent) {

        layoutInflater = LayoutInflater.from(context);
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

            holder.que_title = (TextView)convertView.findViewById(R.id.que_title);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        txt=new TextView[100];
        txt1=new TextView[100];
        rb=new RadioButton[100];
        //rb=new RadioButton[100];
        long date_cur = System.currentTimeMillis();
        String dtStart = listData.get(position).get("posttime");
        Date date = null,date_curr = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dateString = format.format(date_cur);
        try {
            date = format.parse(dtStart);
            date_curr = format.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long time =  printDifference(date,date_curr);
        Pref.setValue(context, "Htitle", listData.get(position).get("title"));
        // Log.e("votai1","" + listData.get(position).get("description"));
        if(listData.get(position).get("question_type").equalsIgnoreCase("radio")) {

            if (listData.get(position).get("votable").equalsIgnoreCase("0")) {
                Log.e("votai0",""+listData.get(position).get("votable"));
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
                    txt[i].setTextColor(Color.WHITE);
                    txt[i].setTextSize(16);
                    txt[i].setText(listdata1.get(position).get("question_options").get(i));
                    txt[i].setPadding(35, 5, 0, 5);
                    LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                    childParam1.weight = 0.5f;
                    txt[i].setLayoutParams(childParam1);
                    ll1.addView(txt[i]);

                    txt1[i] = new TextView(context);
                    txt1[i].setTypeface(FontCustom.setFontBold(context));
                    txt1[i].setTextColor(Color.WHITE);
                    txt1[i].setTextSize(16);
                    txt1[i].setText(listdata1.get(position).get("options_count").get(i));
                    txt1[i].setPadding(0, 5, 15, 5);
                    LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                    childParam2.weight = 0.5f;
                    txt1[i].setLayoutParams(childParam2);
                    ll1.addView(txt1[i]);
                    ll2.addView(ll1);
                }

                holder.list_row_header_ln1.setVisibility(View.GONE);
                holder.list_row_header_ln3.setVisibility(View.GONE);
                holder.list_row_header_ln2.removeAllViews();
                holder.list_row_header_ln2.addView(ll2);

                holder.que_title.setText(listData.get(position).get("question_title"));
            } else if (listData.get(position).get("votable").equalsIgnoreCase("1")) {


                LinearLayout ll2 = new LinearLayout(context);
                ll2.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutForInner1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll2.setLayoutParams(layoutForInner1);

                rg_que = new RadioGroup(context);
                LinearLayout ll3 = new LinearLayout(context);
                ll3.setOrientation(LinearLayout.HORIZONTAL);
                ll3.setWeightSum(1.0f);

                for (int i = 0; i <  listdata1.get(position).get("options_count").size(); i++) {

                    rb[i] = new RadioButton(context);
                    rb[i].setTypeface(FontCustom.setFontBold(context));
                    rb[i].setTextColor(Color.WHITE);
                    rb[i].setTextSize(16);
                    // rb[i].setButtonTintList(ColorStateList.valueOf(Color.WHITE));
                    rb[i].setText(listdata1.get(position).get("question_options").get(i).toString());
                    rg_que.addView(rb[i]);


                  /*  LinearLayout.LayoutParams childParam3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                    childParam3.weight = 1.0f;
                    rb[i].setLayoutParams(childParam3);*/



                }
                ll3.addView(rg_que);
                ll2.addView(ll3);
                holder.list_row_header_ln2.removeAllViews();
                holder.list_row_header_ln2.addView(ll2);
                //add_chart(position, holder);

                holder.list_row_header_ln1.setVisibility(View.VISIBLE);
                holder.list_row_header_ln3.setVisibility(View.VISIBLE);
                holder.ll_graph.setVisibility(View.VISIBLE);
                holder.que_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e("answerflag", "" + listData.get(position).get("answerflag"));
                        if (listData.get(position).get("answerflag").equalsIgnoreCase("1")) {
                            Utils.showAlert(context, "You already saved this answer.");
                        } else {
                            if (Pref.getValue(context, Constants.TAG_USER_ID, "").equals("")) {
                                Utils.showAlert(context, context.getResources().getString(R.string.dialog_title), context.getResources().getString(R.string.dialog_msg));
                            } else {
                                int count1 = 0;
                                for (int j = 0; j < listdata1.get(position).get("options_count").size(); j++) {
                                    rb[j] = new RadioButton(context);
                                    if (rb[j].isChecked()) {
                                        count1++;

                                    }

                                }
                                if (count1 == 0) {
                                    Utils.showAlert(context, "Please mention your vote");
                                }else
                                {
                                    JSONArray jsonArray = new JSONArray();

                                    // for (int i = 0; i < mVoteOfQuestionsArrayList.size(); i++) {
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


                                        int count = 0;
                                        for (int j = 0; j < listdata1.get(position).get("options_count").size(); j++) {
                                            rb[j] = new RadioButton(context);
                                            if (rb[j].isChecked()) {
                                                count++;
                                    /*JSONArray jsonArrayans = new JSONArray();
                                    JSONObject jsonObject1 = new JSONObject();
                                    jsonObject1.put("typeoption", rb[i][j].getText().toString());
                                    jsonArrayans.put(jsonObject1);
                                    jsonObject.put("answer", jsonArrayans);*/
                                                jsonObject.put("typeoption", rb[j].getText().toString());
                                                jsonArray.put(jsonObject);
                                                typeoption = rb[j].getText().toString();

                                            }

                                        }
                                        callsetVoteApi();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }






                            }
                        }
                    }
                });

                holder.que_title.setText(listData.get(position).get("question_title"));
            }



        }else
        {
            holder.list_row_header_ln2.setVisibility(View.GONE);
            holder.list_row_header_ln1.setVisibility(View.GONE);

            holder.list_row_header_ln3.setVisibility(View.GONE);
            holder.ll_graph.setVisibility(View.GONE);
        }

        //on click


        holder.img_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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



        holder.title.setText(listData.get(position).get("description"));
        if (listData.get(position).get("article_type").equals("image")) {
            holder.txtvideotitle.setText(listData.get(position).get("title"));
            holder.list_row_watching_time_tv.setText(time+days);

            Picasso.with(context).load(listData.get(position).get("thumb_image")).fit().into(holder.thumbImage);
            holder.videoimage.setVisibility(View.GONE);
            holder.play.setVisibility(View.GONE);
            holder.txtvideotitle.setVisibility(View.VISIBLE);
            holder.title.setVisibility(View.VISIBLE);
            holder.thumbImage.setVisibility(View.VISIBLE);


            holder.list_row_top_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, DetailOfVideoImageActivity.class).putExtra("vidImgTitle", holder.title.getText().toString()).putExtra("image", listData.get(position).get("thumb_image")).putExtra("articleid", listData.get(position).get("id")).putExtra("articleType", listData.get(position).get("article_type")).putExtra("check", "Image").putExtra("votable",listData.get(position).get("votable")));
                }
            });

        }else if(listData.get(position).get("article_type").equals("text"))
        {
            // holder.title.setText(listData.get(position).get("description"));
            holder.txtvideotitle.setText(listData.get(position).get("title"));

            holder.list_row_watching_time_tv.setText(time+days);

            //  Picasso.with(context).load(listData.get(position).get("thumb_image")).fit().into(holder.thumbImage);
            holder.videoimage.setVisibility(View.GONE);
            holder.play.setVisibility(View.GONE);
            holder.txtvideotitle.setVisibility(View.VISIBLE);
            holder.title.setVisibility(View.VISIBLE);
            holder.thumbImage.setVisibility(View.GONE);

            holder.list_row_top_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            holder.list_row_watching_time_tv.setText(time+days);

          //  Picasso.with(context).load(listData.get(position).get("thumb_image")).fit().into(holder.videoimage);
            holder.list_row_top_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            holder.play.setVisibility(View.VISIBLE);
            holder.txtvideotitle.setVisibility(View.VISIBLE);

            holder.list_row_watching_time_tv.setText(time + days);
          //  Picasso.with(context).load(listData.get(position).get("thumb_image")).fit().into(holder.videoimage);
            holder.list_row_top_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, DetailOfVideoImageActivity.class).putExtra("vidImgTitle", holder.txtvideotitle.getText().toString()).putExtra("image", listData.get(position).get("image")).putExtra("articleid", listData.get(position).get("id")).putExtra("articleType", listData.get(position).get("articleType")).putExtra("check", "YouTube").putExtra("votable",listData.get(position).get("votable")));
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
/*        if(value.equalsIgnoreCase("first"))
        {
            if(TopNewsFirstFragment.list.size()==position+1) {
                if(Pref.getValue(context, "last", "").equalsIgnoreCase("0")) {

                    offset = Integer.parseInt(Pref.getValue(context,"offset",""))+1;
                    Pref.setValue(context,"offset",String.valueOf(offset));
                    new TopNewsFirstFragment().callTopNewsDetailApi();
                }
                Log.e("offset adapter", "" + Pref.getValue(context, "offset", ""));
                Log.e("groupPosition",""+position);


            }
        }*/

        return convertView;
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
                Log.e("addvote","success");
                String message = mainObj.optString("message");
                Utils.showToast(context, message);


            }else {


                String message = mainObj.optString("message");
                Utils.showToast(context, message);
            }
        }else if(response_code.equals("1000")){
            String message = mainObj.optString("message");

            Pref.deleteAll(context);

            DashBoardActivity.mDrawerLayout.closeDrawers();
            context.startActivity(new Intent(context, SplashActivity.class));
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

    public static class ViewHolder {
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
    public  void addalldata(ArrayList<HashMap<String,String>> list_new)
    {
        this.listData.addAll(list_new);
        notifyDataSetChanged();
    }
    public  void addalldata1(ArrayList<HashMap<String,ArrayList<String>>> list_new)
    {
        this.listdata1.addAll(list_new);
        notifyDataSetChanged();
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
        Log.e("days",""+elapsedDays+"d");
        Log.e("hour",""+elapsedHours+"h");
        Log.e("minute",""+elapsedMinutes+"m");
        Log.e("second",""+elapsedSeconds+"s");
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
