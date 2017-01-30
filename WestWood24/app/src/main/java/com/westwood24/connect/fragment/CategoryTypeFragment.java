package com.westwood24.connect.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.activity.DashBoardActivity;
import com.westwood24.connect.activity.SplashActivity;
import com.westwood24.connect.adapter.VideoImageAdapter;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryTypeFragment extends Fragment implements WebserviceCall.WebserviceResponse {

    //Object Daclaration..
    ListView mVideoLv;
    VideoImageAdapter mVideoImageAdapter;
    String TAG = "CategoryFragment";
    ArrayList<HashMap<String, String>> articlesArrayList;
    VideoImageAdapter adapter;
    View mRootView;
    public static Context context;
    public static  ArrayList<HashMap<String, String>> list,list_temp;
    public static SwipeRefreshLayout swipe_refresh_layout;
    String offset = "0";
    public static WebserviceCall webserviceCall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        mRootView = inflater.inflate(R.layout.fragment_topnewfirst, container, false);
        swipe_refresh_layout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);

        context = getActivity();
        list = new ArrayList<HashMap<String, String>>();
        list_temp = new ArrayList<HashMap<String, String>>();
        webserviceCall = new WebserviceCall(context);
        webserviceCall.setWebserviceResponse(this);
        Pref.setValue(getActivity(), "offset", offset);
        initComponents();
        callCategoryApi();
        prepareView();
        setActionListener();

        mVideoLv = (ListView) mRootView.findViewById(R.id.activity_video_listview);
        articlesArrayList = new ArrayList<>();

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//Refreshing data on server
                swipe_refresh_layout.setRefreshing(true);


                callCategoryApi();

            }


        });

        return mRootView;
    }

    private void initComponents() {


    }

    private void prepareView() {

    }

    private void setActionListener() {

    }


    public static void callCategoryApi() {
        swipe_refresh_layout.setRefreshing(false);
        try {
            RequestParams requestParams = new RequestParams();
            // if (loginPhoneEmail == 0) {
            Log.e("id in adapter", "" + Pref.getValue(context, "id", ""));
            requestParams.put("categoryId", Pref.getValue(context, "id", ""));

            requestParams.put("offset",Pref.getValue(context,"offset",""));
            /*
                requestParams.put("phone", emailPhone);
            } else {
                requestParams.put("loginType", "native");
                requestParams.put("email", emailPhone);
            }*/

            //requestParams.put("loginType", "phone");
            //requestParams.put("phone", emailPhone);
            //  requestParams.put("password", password);
            webserviceCall.callWebservice(Constants.GET_ARTICLES_BY_CATEGORY_URL, "all", "post", requestParams);
            //  Log.e(TAG, "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void success(String url, String apiName, String response) {
        Log.e(TAG, "category !!! " + response);
        try {
            JSONObject mainObj = new JSONObject(response);
            //  String status = mainObj.optString("status");
            String message = mainObj.optString("message");
            String response_code = mainObj.optString("code");

            list_temp.clear();
            if (response_code.equals("200")) {
                // JSONObject dataObj = mainObj.getJSONObject("data");
                list = new ArrayList<HashMap<String, String>>();
                JSONArray jsonArray = mainObj.getJSONArray("data");

                Log.e(TAG, "response is.." + mainObj);

                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.optString("id");
                    String article_type = jsonObject.optString("articleType");
                    //String articleVoteType = jsonObject.optString("articleVoteType");
                    String description = jsonObject.optString("description");
                    String thumb_image = jsonObject.optString("thumbFormediaSource");
                    String no_viewers = jsonObject.optString("noViewer");
                    String no_commenters = jsonObject.optString("noCommenter");
                    String title = jsonObject.optString("title");
                    String image = jsonObject.optString("mediaSource");
                    String posttime = jsonObject.optString("posttime");
                    map.put("id", id);
                    map.put("article_type", article_type);
                    // map.put("articleVoteType", articleVoteType);
                    map.put("description", description);
                    map.put("thumb_image", thumb_image);
                    map.put("no_viewers", no_viewers);
                    map.put("no_commenters", no_commenters);
                    map.put("title", title);
                    map.put("image", image);
                    map.put("posttime", posttime);
                    list.add(map);
                    list_temp.add(map);
                    //   Log.e(TAG,"list latest articles"+map);
                }


                offset = Pref.getValue(context,"offset","");
                Pref.setValue(context, "offset", String.valueOf(offset));
                Pref.setValue(context, "last", "0");
                if(offset.equalsIgnoreCase("0"))
                {
                 /*   adapter = new VideoImageAdapter(getActivity(), list,"category");
                    mVideoLv.setAdapter(adapter);*/

                }else
                {
                    /*adapter.addalldata(list_temp);
                    adapter.notifyDataSetChanged();
*/
                }

            } else if(response_code.equalsIgnoreCase("1000"))
            {
                Pref.deleteAll(context);
                DashBoardActivity.mDrawerLayout.closeDrawers();
                startActivity(new Intent(context, SplashActivity.class));
                Utils.showToast(context, message);
            } else {

                Pref.setValue(context, "last", "1");
                //    Utils.showToast(getActivity(), message);
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
            //  Utils.showToast(getActivity(), message);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        offset = "0";
    }
}
