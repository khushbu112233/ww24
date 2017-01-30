package com.westwood24.connect.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.adapter.VideoImageAdapter;
import com.westwood24.connect.adapter.VoteOfQuestionAdapter;
import com.westwood24.connect.model.AllArticles;
import com.westwood24.connect.model.VoteOfQuestion;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VoteOftheArticleActivity extends AppCompatActivity implements WebserviceCall.WebserviceResponse, View.OnClickListener {

    //Object Daclaration..
    ListView mVoteOfQuestionLv;
    Intent mIntent;
    ArrayList<VoteOfQuestion> mVoteOfQuestionsArrayList;
    VoteOfQuestionAdapter mVoteOfQuestionAdapter;
    RelativeLayout mHeaderBackRl;
    TextView mHeaderTitleTv;
    TextView mNoRecordTv;
    Button mSubmitBtn;
    //Variable declaration..
    String TAG = "VoteOftheArticleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voteofarticle);
        mIntent = getIntent();
        mVoteOfQuestionsArrayList = new ArrayList<>();
        initComponents();
        prepareView();
        setActionListener();
        callGetVoteofQuestionApi();
    }


    private void initComponents() {
        mVoteOfQuestionLv = (ListView) findViewById(R.id.activity_voteofarticle_listview);
        mSubmitBtn = (Button) findViewById(R.id.activity_voteofarticle_submit_btn);
        mHeaderBackRl = (RelativeLayout) findViewById(R.id.header_layout_back_rl);
        mHeaderTitleTv = (TextView) findViewById(R.id.txt_title);
        mNoRecordTv = (TextView) findViewById(R.id.activity_voteofarticle_norecord_tv);
    }

    private void prepareView() {

    }

    private void setActionListener() {
        mHeaderBackRl.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
    }


    private void callGetVoteofQuestionApi() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("articalId", mIntent.getStringExtra("articleid"));
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.GETVOTE_OF_QUE_URL, "getvote_que", "post", requestParams);
            Log.e(TAG, "request is" + requestParams);
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
                JSONArray jsonArray_voteque = jsonObjectdata.getJSONArray("votequestion");

                VoteOfQuestion[] voteOfQuestions = new VoteOfQuestion[jsonArray_voteque.length()];
                for (int i = 0; i < jsonArray_voteque.length(); i++) {
                    voteOfQuestions[i] = new VoteOfQuestion();
                    voteOfQuestions[i].setQuestId(jsonArray_voteque.getJSONObject(i).optString("questId"));
                    voteOfQuestions[i].setQuestiontitle(jsonArray_voteque.getJSONObject(i).optString("questiontitle"));
                    voteOfQuestions[i].setArticalId(jsonArray_voteque.getJSONObject(i).optString("articalId"));
                    voteOfQuestions[i].setAnswer(jsonArray_voteque.getJSONObject(i).optString("answer"));
                    voteOfQuestions[i].setVotetype(jsonArray_voteque.getJSONObject(i).optString("votetype"));
                    ArrayList<String> arrayListtitle = new ArrayList<>();
                    for (int count = 0; count < jsonArray_voteque.getJSONObject(i).getJSONArray("voteoptions").length(); count++) {
                        arrayListtitle.add(jsonArray_voteque.getJSONObject(i).getJSONArray("voteoptions").get(count).toString());
                    }
                    voteOfQuestions[i].setVoteoptions(arrayListtitle);
                    mVoteOfQuestionsArrayList.add(voteOfQuestions[i]);
                    //String voteOptions = jsonArray_voteque.getJSONObject(i).optString("voteoptions");

                    //String voteOptionArray[] = voteOptions.split(",");


                   /* for (int j = 0; j < voteOptionArray.length; j++) {
                        voteOfQuestions[i].setVoteoptions(voteOptionArray[j]);

                        Log.e(TAG, "vote@@@ " + voteOfQuestions[i].getVoteoptions());
                        mVoteOfQuestionsArrayList.add(voteOfQuestions[i]);
                    }*/
                }
                if (jsonArray_voteque.length() == 0) {
                    mSubmitBtn.setVisibility(View.GONE);
                    mNoRecordTv.setVisibility(View.VISIBLE);
                    mNoRecordTv.setText("No Record Found!");
                } else {
                    mSubmitBtn.setVisibility(View.VISIBLE);
                }
                mVoteOfQuestionAdapter = new VoteOfQuestionAdapter(VoteOftheArticleActivity.this, mVoteOfQuestionsArrayList);
                mVoteOfQuestionLv.setAdapter(mVoteOfQuestionAdapter);


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_layout_back_rl:
                finish();
                break;
            case R.id.activity_voteofarticle_submit_btn:

                break;
        }
    }
}
