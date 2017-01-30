package com.westwood24.connect.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.activity.DashBoardActivity;
import com.westwood24.connect.activity.LoginActivity;
import com.westwood24.connect.activity.SignupActivity;
import com.westwood24.connect.activity.SplashActivity;
import com.westwood24.connect.adapter.VideoImageAdapter;
import com.westwood24.connect.model.AnswerStatus;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class TopNewsFirstFragment extends Fragment implements WebserviceCall.WebserviceResponse {

    //Object Daclaration..
    ListView mVideoLv;
    VideoImageAdapter mVideoImageAdapter;
    String TAG = "TopNewsFirstFragment";
    ArrayList<HashMap<String, String>> articlesArrayList;
    public static VideoImageAdapter adapter;
    View mRootView;
    public static Context context;

    public static  ArrayList<HashMap<String, String>> list,list_temp;
    public static  ArrayList<HashMap<String, ArrayList<String>>> list1,list1_temp;
    public static SwipeRefreshLayout swipe_refresh_layout;
    String offset = "0";
    public static WebserviceCall webserviceCall;
    public  static int limit = 10;
    ArrayList<String> arrayListtitle1;
    ArrayList<String> arrayListtitle;
    ArrayList<String> question_options_list,option_list;
    TextView txt_register;
    public static ProgressDialog progressDialog;
    public static  ArrayList<HashMap<Integer,String>> answerflag_list = new ArrayList<HashMap<Integer, String>>();
    public static HashMap<Integer,String> map_answer ;
    LinearLayout ll_register;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        mRootView = inflater.inflate(R.layout.fragment_topnewfirst, container, false);
        context = getActivity();
        return mRootView;
    }
    public void init()
    {
        Pref.setValue(getActivity(), "offset", offset);
        swipe_refresh_layout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        txt_register = (TextView)mRootView.findViewById(R.id.txt_register);
        ll_register= (LinearLayout)mRootView.findViewById(R.id.ll_register);
        mVideoLv = (ListView) mRootView.findViewById(R.id.activity_video_listview);
        if(Pref.getValue(context,"user_login_register","").equalsIgnoreCase("login"))
        {
            ll_register.setVisibility(View.GONE);
        }else
        {
            ll_register.setVisibility(View.VISIBLE);
        }
        articlesArrayList = new ArrayList<>();
        list = new ArrayList<HashMap<String, String>>();
        list_temp = new ArrayList<HashMap<String, String>>();
        list_temp.clear();
        list.clear();
        list1 = new ArrayList<>();
        list1_temp = new ArrayList<>();
        list1_temp.clear();
        list1.clear();

        webserviceCall = new WebserviceCall(context);
        webserviceCall.setWebserviceResponse(this);

        Pref.setValue(context, "swipe", "2");
        callTopNewsDetailApi();



        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Refreshing data on server
                swipe_refresh_layout.setRefreshing(true);

               Pref.setValue(context,"swipe","1");
                callTopNewsDetailApi1();

            }


        });
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginActivity.class));
                Intent i = new Intent(context, SignupActivity.class);
                startActivity(i);
            }
        });
    }

    public static void callTopNewsDetailApi1() {
        swipe_refresh_layout.setRefreshing(false);
        SplashActivity.answer_status_list = new ArrayList<>();
        SplashActivity.answer_status_list.clear();
        // Log.e("offset in call", "" + Pref.getValue(context, "offset", ""));
        try {
            RequestParams requestParams = new RequestParams();
            // if (loginPhoneEmail == 0) {
            requestParams.put("filtertype", "topnews");
            if (Pref.getValue(context, "user_type", "").equalsIgnoreCase("login")) {
                requestParams.put("authkey", Pref.getValue(context, Constants.TAG_AUTHKAY, ""));
            }
            requestParams.put("offset",Pref.getValue(context,"offset_value",""));
            requestParams.put("limit",limit);

            /*
                requestParams.put("phone", emailPhone);
            } else {
                requestParams.put("loginType", "native");
                requestParams.put("email", emailPhone);
            }*/

            //requestParams.put("loginType", "phone");
            //requestParams.put("phone", emailPhone);
            //  requestParams.put("password", password);

            webserviceCall.callWebservice(Constants.GET_ARTICLES_LIST_URL, "all", "post", requestParams);

            //  Log.e(TAG, "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void callTopNewsDetailApi() {
        swipe_refresh_layout.setRefreshing(false);
        SplashActivity.answer_status_list = new ArrayList<>();
        SplashActivity.answer_status_list.clear();
        // Log.e("offset in call", "" + Pref.getValue(context, "offset", ""));
        try {
            RequestParams requestParams = new RequestParams();
            // if (loginPhoneEmail == 0) {
            requestParams.put("filtertype", "topnews");
            if (Pref.getValue(context, "user_type", "").equalsIgnoreCase("login")) {
                requestParams.put("authkey", Pref.getValue(context, Constants.TAG_AUTHKAY, ""));
            }
            requestParams.put("offset",Pref.getValue(context,"offset_value",""));
            requestParams.put("limit",limit);

            /*
                requestParams.put("phone", emailPhone);
            } else {
                requestParams.put("loginType", "native");
                requestParams.put("email", emailPhone);
            }*/

            //requestParams.put("loginType", "phone");
            //requestParams.put("phone", emailPhone);
            //  requestParams.put("password", password);

            webserviceCall.callWebservice(Constants.GET_ARTICLES_LIST_URL, "all", "post", requestParams);

            //  Log.e(TAG, "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void success(String url, String apiName, String response) {

        try {
            JSONObject mainObj = new JSONObject(response);
            //  String status = mainObj.optString("status");
            String message = mainObj.optString("message");
            String response_code = mainObj.optString("code");

            if (response_code.equals("200")) {
                //   JSONObject dataObj = mainObj.getJSONObject("data");

                if(Pref.getValue(context,"swipe","").equals("1"))
                {
                    list_temp.clear();
                    list1_temp.clear();
                     list.clear();
                     list1.clear();
                }
                else {
                    list_temp = new ArrayList<HashMap<String, String>>();
                    list1_temp = new ArrayList<>();
                    list_temp.clear();
                    list1_temp.clear();
                }
                // list.clear();
                //list1.clear();

                JSONArray jsonArray = mainObj.optJSONArray("data");

                Log.e(TAG, "response is..top news" + jsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    HashMap<String,ArrayList<String>> map1 = new HashMap<>();
                    JSONObject jsonObject = jsonArray.optJSONObject(i);


                    String id = jsonObject.optString("id");
                    String article_type = jsonObject.optString("articleType");
                    String no_viewers = jsonObject.optString("noViewer");
                    String thumb_image = jsonObject.optString("thumbFormediaSource");
                    String title = jsonObject.optString("title");
                    String no_commenters = jsonObject.optString("noCommenter");
                    String description = jsonObject.optString("description");
                    String image = jsonObject.optString("mediaSource");
                    String posttime = jsonObject.optString("posttime");
                    String question_title = jsonObject.optString("question_title");
                    String votable = jsonObject.optString("votable");
                    String question_type = jsonObject.optString("question_type");
                    String answerflag = jsonObject.optString("answerflag");
                    String question_id = jsonObject.optString("question_id");
                    //Log.e("desc1","" + description);

                    map.put("id", id);
                    map.put("question_title",question_title);
                    map.put("article_type", article_type);
                    map.put("no_viewers", no_viewers);
                    map.put("thumb_image", thumb_image);
                    map.put("title", title);
                    map.put("no_commenters", no_commenters);
                    //map.put("description", description);
                    map.put("description", Html.fromHtml(description).toString());
                    map.put("image", image);
                    map.put("posttime", posttime);

                    map.put("votable", votable);

                    map.put("question_type", question_type);
                    map.put("answerflag", answerflag);
                    map.put("question_id", question_id);
                    AnswerStatus[] answerStatus=new AnswerStatus[1];
                    answerStatus[0]=new AnswerStatus();
                    answerStatus[0].setArticle_ida(id);
                    answerStatus[0].setQuestion_ida(question_id);
                    answerStatus[0].setAnswer_flaga(answerflag);
                    SplashActivity.answer_status_list.add(answerStatus[0]);

                    list.add(map);
                    list_temp.add(map);

                    // answerflag_list=new ArrayList<>();

                    map_answer = new HashMap<>();
                    if(question_id.equalsIgnoreCase(""))

                    {
                        if(answerflag.equalsIgnoreCase("")) {
                            map_answer.put(i, "0");
                            answerflag_list.add(map_answer);
                        }else
                        {
                            map_answer.put(i, answerflag);
                            answerflag_list.add(map_answer);
                        }
                    }else {
                        if(answerflag.equalsIgnoreCase("")) {
                            map_answer.put(i, "0");
                            answerflag_list.add(map_answer);
                        }else
                        {
                            map_answer.put(i, answerflag);
                            answerflag_list.add(map_answer);
                        }
                    }

                    if(question_type.equalsIgnoreCase("radio")) {
                        arrayListtitle = new ArrayList<>();

                        for (int count = 0; count < jsonArray.optJSONObject(i).optJSONArray("question_options").length(); count++) {
                            if (jsonArray.optJSONObject(i).optJSONArray("question_options").get(count).toString() != null) {
                                if (jsonArray.optJSONObject(i).optJSONArray("question_options").get(count).toString().length() > 0) {
                                    arrayListtitle.add(jsonArray.optJSONObject(i).optJSONArray("question_options").get(count).toString());

                                }
                            }
                        }

                        arrayListtitle1 = new ArrayList<>();

                        for (int j = 0; j < arrayListtitle.size(); j++) {
                            arrayListtitle1.add(jsonArray.optJSONObject(i).optJSONObject("options_count").get(arrayListtitle.get(j)).toString());

                            //   Log.e("option_count", "" + jsonArray_voteque.getJSONObject(i).getJSONObject("options_count").get(arrayListtitle.get(j)));
                        }
                    }

                    if(question_type.equalsIgnoreCase("radio")) {

                        map1.put("question_options", arrayListtitle);
                        map1.put("options_count", arrayListtitle1);
                    }else
                    {
                        arrayListtitle = new ArrayList<>();
                        arrayListtitle1 = new ArrayList<>();
                        map1.put("question_options", arrayListtitle);
                        map1.put("options_count", arrayListtitle1);

                    }
                   /* if(question_type.equalsIgnoreCase("radio")) {


                        list1.add(map1);
                        list1_temp.add(map1);
                    }else
                    {
                        map1 = new HashMap<>();*/
                    list1.add(map1);
                    list1_temp.add(map1);
                    //}

                    Log.e("list1",i+ "" + list);

                }
                // Log.e(TAG, "list latest articles" + list);
                Log.e("answerflag_list", "" + answerflag_list);

                offset = Pref.getValue(context,"offset_value","");
                Pref.setValue(context, "offset", String.valueOf(offset));
                Pref.setValue(context, "last", "0");
                if(offset.equalsIgnoreCase("0"))
                {
                    adapter = new VideoImageAdapter(getActivity(), list_temp,list1_temp,"first");
                    mVideoLv.setAdapter(adapter);

                }else
                {
                    adapter.addalldata(list_temp,list1_temp);
                    adapter.notifyDataSetChanged();
                }

            }
            else if(response_code.equalsIgnoreCase("1000"))
            {
                Pref.deleteAll(context);
                DashBoardActivity.mDrawerLayout.closeDrawers();

                startActivity(new Intent(context, SplashActivity.class));
                Pref.setValue(context, "user_type", "guest");
                Utils.showToast(context, message);
            }else {
                // Log.e("dashboard",""+message);

                Pref.setValue(context, "last", "1");
                // Utils.showToast(getActivity(), message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failure(String url, String apiName, String response) {
        JSONObject mainObj = null;
        try {
            mainObj = new JSONObject(response);
            String message = mainObj.optString("message");
            // Utils.showToast(getActivity(), message);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        offset = "0";
        init();
    }
    private class ImageGetter implements Html.ImageGetter {
        public Drawable getDrawable(String source) {
            int id;
            if (source.equals("hughjackman.jpg")) {
                id = R.mipmap.ic_launcher;
            }
            else {
                return null;
            }
            Drawable d = getResources().getDrawable(id);
            d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
            return d;
        }
    };
}

