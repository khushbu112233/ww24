package com.westwood24.connect.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.activity.DashBoardActivity;
import com.westwood24.connect.activity.SplashActivity;
import com.westwood24.connect.adapter.TabViewPagerAdapter;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: dharemesh dudhat
 * Date: 6/9/2016
 */
public class HomeFragment extends Fragment implements  WebserviceCall.WebserviceResponse {

    private View parentView;
    //UI Object declaration...
    //private TabLayout mTabLayout;
    //private ViewPager mViewPager;
    String TAG = "HOME FRAGMENT";
    Context context;
    ListView listview;
    public  static int limit = 10;
    public static ArrayList<HashMap<String, String>> list, list1, list2, list3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_home, container, false);
        //setUpViews();
        initComponents();

        prepareView();
        // callLoginApi();
        setActionListener();
        return parentView;
    }

    private void initComponents() {
        listview = (ListView)parentView.findViewById(R.id.listview);
       // mTabLayout = (TabLayout) parentView.findViewById(R.id.activity_dash_board_tabLayout);
        //mViewPager = (ViewPager) parentView.findViewById(R.id.activity_dash_board_pager);
        context = getActivity();
    }

    private void prepareView() {

        Pref.setValue(getActivity(), "offset", "0");

    }
    private void setActionListener() {

    }

    private void callLoginApi() {
        try {
            RequestParams requestParams = new RequestParams();
            // if (loginPhoneEmail == 0) {
            // requestParams.put("filtertype", "latest");
            /*
                requestParams.put("phone", emailPhone);
            } else {
                requestParams.put("loginType", "native");
                requestParams.put("email", emailPhone);
            }*/

            //requestParams.put("loginType", "phone");
            //requestParams.put("phone", emailPhone);
            //  requestParams.put("password", password);

            requestParams.put("filtertype", "topnews");

            requestParams.put("authkey", Pref.getValue(context, Constants.TAG_AUTHKAY, ""));

            requestParams.put("offset",Pref.getValue(context,"offset",""));
            requestParams.put("limit",limit);
            WebserviceCall webserviceCall = new WebserviceCall(getActivity());
            webserviceCall.setWebserviceResponse(this);
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
                list = new ArrayList<HashMap<String, String>>();
                list1 = new ArrayList<HashMap<String, String>>();
                list2 = new ArrayList<HashMap<String, String>>();
                list3 = new ArrayList<HashMap<String, String>>();

                JSONArray jsonArray = mainObj.getJSONArray("data");

                Log.e(TAG, "response is..latest" + jsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.optString("id");
                    String article_type = jsonObject.optString("article_type");
                    String no_viewers = jsonObject.optString("no_viewers");
                    String thumb_image = jsonObject.optString("thumb_image");
                    String title = jsonObject.optString("title");
                    String no_commenters = jsonObject.optString("no_commenters");
                    String description = jsonObject.optString("description");
                    String image = jsonObject.optString("image");
                    //    String posttime = jsonObject.optString("posttime");
                    map.put("id", id);
                    map.put("article_type", article_type);
                    map.put("no_viewers", no_viewers);
                    map.put("thumb_image", thumb_image);
                    map.put("title", title);
                    map.put("no_commenters", no_commenters);
                    map.put("description", description);
                    map.put("image", image);
                    //  map.put("posttime", posttime);
                    list.add(map);
                    //   Log.e(TAG,"list latest articles"+map);
                }

                //  prepareView();
            }  else if(response_code.equalsIgnoreCase("1000"))
            {
                Pref.deleteAll(context);
                DashBoardActivity.mDrawerLayout.closeDrawers();
                startActivity(new Intent(context, SplashActivity.class));
                Utils.showToast(context, message);
                Pref.setValue(context, "user_type", "guest");
            }else {
                Utils.showToast(getActivity(), message);
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
            Utils.showToast(getActivity(), message);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
