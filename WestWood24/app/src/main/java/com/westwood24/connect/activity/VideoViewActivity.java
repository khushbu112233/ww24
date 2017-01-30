package com.westwood24.connect.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.adapter.VideoImageAdapter;
import com.westwood24.connect.model.AllArticles;
import com.westwood24.connect.model.ListItem;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoViewActivity extends Activity implements WebserviceCall.WebserviceResponse {

    //Object Daclaration..
    ListView mVideoLv;
    VideoImageAdapter mVideoImageAdapter;
    String TAG = "VideoViewActivity";
    ArrayList<AllArticles> articlesArrayList;
    VideoImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        initComponents();
        prepareView();
        setActionListener();

        mVideoLv = (ListView) findViewById(R.id.activity_video_listview);
        articlesArrayList = new ArrayList<>();
        callRegisterApi();
        //  mVideoLv.setAdapter(new VideoImageAdapter(this, listData));
    }

    private void initComponents() {


    }

    private void prepareView() {

    }

    private void setActionListener() {

    }


    private void callRegisterApi() {
        try {
            RequestParams requestParams = new RequestParams();

            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.ALL_ARTICLES_URL, "all_articles", "get", requestParams);
            Log.e("Registration", "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void success(String url, String apiName, String response) {
        Log.e(TAG, "response is" + response);

        try {
            JSONObject mainObj = new JSONObject(response);
            String status = mainObj.optString("status");
            String message = mainObj.optString("message");
            String response_code = mainObj.optString("response_code");

            if (response_code.equals("200")) {

                JSONObject jsonObjectdata = mainObj.getJSONObject("data");
                JSONArray jsonArray_articles = jsonObjectdata.getJSONArray("articles");

                Log.v("jsonArray_articles", jsonArray_articles.length() + "");

                AllArticles[] allArticles = new AllArticles[jsonArray_articles.length()];
                for (int i = 0; i < jsonArray_articles.length(); i++) {
                    allArticles[i] = new AllArticles();
                    allArticles[i].setArticleId(jsonArray_articles.getJSONObject(i).getString("articleId"));
                    allArticles[i].setArticleType(jsonArray_articles.getJSONObject(i).getString("articleType"));
                    allArticles[i].setArticleVoteType(jsonArray_articles.getJSONObject(i).getString("articleVoteType"));
                    allArticles[i].setArticleTitle(jsonArray_articles.getJSONObject(i).getString("articleTitle"));
                    allArticles[i].setArticleImage(jsonArray_articles.getJSONObject(i).getString("articleImage"));
                    articlesArrayList.add(allArticles[i]);

                }

                //adapter = new VideoImageAdapter(VideoViewActivity.this, articlesArrayList);
                //mVideoLv.setAdapter(adapter);


            } else {
                Utils.showToast(this, message);
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
            Utils.showToast(this, message);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
