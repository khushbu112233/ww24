package com.westwood24.connect.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.adapter.CommentArticleAdapter;
import com.westwood24.connect.adapter.VideoImageAdapter;
import com.westwood24.connect.model.AllArticles;
import com.westwood24.connect.model.CommentDataList;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import eu.janmuller.android.simplecropimage.Util;

public class CommentListOfArticleActivity extends AppCompatActivity implements WebserviceCall.WebserviceResponse, View.OnClickListener {

    ListView mCommentListView;
    Intent mIntent;
    String articlaPosition;
    ArrayList<CommentDataList> mCommentArrayList;
    CommentArticleAdapter mCommentArticleAdapter;
    RelativeLayout mHeaderBackRl;
    TextView mHeaderTitleTv;
    TextView mHeaderRightTv;
    Dialog myCustomeDialog;
    ImageView img_left;
    String userComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentofarticle);
        mIntent = getIntent();
        initComponents();
        prepareView();
        setActionListener();

        mCommentArrayList = new ArrayList<CommentDataList>();
        callCommentListAPI();
    }


    private void initComponents() {
        mCommentListView = (ListView) findViewById(R.id.activity_comment_list_lv);
        mHeaderBackRl = (RelativeLayout) findViewById(R.id.header_layout_back_rl);
        mHeaderTitleTv = (TextView) findViewById(R.id.txt_title);
        mHeaderRightTv = (TextView) findViewById(R.id.txt_title_right);
        img_left = (ImageView)findViewById(R.id.img_left);
        mHeaderBackRl.setVisibility(View.GONE);
    }

    private void prepareView() {
        articlaPosition = mIntent.getStringExtra("articleid");
        mHeaderTitleTv.setText("Comments");
        mHeaderRightTv.setVisibility(View.GONE);
        mHeaderRightTv.setText("Add");
    }

    private void setActionListener() {
        mHeaderBackRl.setOnClickListener(this);
        mHeaderRightTv.setOnClickListener(this);
        img_left.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_left:
                finish();
                break;
            case R.id.txt_title_right:
                dialogWithUserComment(CommentListOfArticleActivity.this);
                break;
        }
    }


    //For user comment dialog.........
    private void dialogWithUserComment(Context mContext) {
        myCustomeDialog = new Dialog(mContext, R.style.Animatedialog1);
        myCustomeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myCustomeDialog.setContentView(R.layout.comment_add_dialog);
        myCustomeDialog.setCanceledOnTouchOutside(false);
        myCustomeDialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = myCustomeDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        myCustomeDialog.getWindow().setGravity(Gravity.CENTER);
        //myCustomeDialog.setCancelable(false);
        //myCustomeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        final EditText mEnterCommentEdt = (EditText) myCustomeDialog.findViewById(R.id.comment_add_userenter_edt);
        Button mSendBtn = (Button) myCustomeDialog.findViewById(R.id.comment_add_send_btn);
        ImageView mCancelImg = (ImageView) myCustomeDialog.findViewById(R.id.comment_add_title_cancel_img);


        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userComment = mEnterCommentEdt.getText().toString().trim();
                if (userComment.equals("")) {
                    Utils.showToast(CommentListOfArticleActivity.this, "Please enter comment.");
                } else {
                    callCommentSendAPI();
                }
            }
        });

        mCancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCustomeDialog.dismiss();
            }
        });


        myCustomeDialog.show();

    }

    private void callCommentSendAPI() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("postid", articlaPosition);
            requestParams.put("userid", Pref.getValue(CommentListOfArticleActivity.this, Constants.TAG_USER_ID, ""));
            requestParams.put("comment", userComment);
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.SENDUSER_COMMENT_URL, "send_usercomment", "post", requestParams);
            Log.e("Registration", "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void callCommentListAPI() {
        try {

            RequestParams requestParams = new RequestParams();

            requestParams.put("articleId", mIntent.getStringExtra("articleid"));
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.GET_ARTICLE_COMMENT_URL, "article_comment", "post", requestParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void success(String url, String apiName, String response) {
        JSONObject mainObj = null;
        try {
            mainObj = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String response_code = mainObj.optString("code");

        if (apiName.equals("send_usercomment")) {
            if (response_code.equals("200")) {
                finish();
                startActivity(mIntent);
                myCustomeDialog.dismiss();
            }

        } else {

            try {


                if (response_code.equals("200")) {

                    JSONArray commentArray = mainObj.getJSONArray("data");

                    CommentDataList[] commentArticles = new CommentDataList[commentArray.length()];
                    Log.e("view all comment", "request is" + commentArticles.length);

                    for (int i = 0; i < commentArticles.length; i++) {
                        commentArticles[i] = new CommentDataList();
                        commentArticles[i].setCommentId(commentArray.getJSONObject(i).optString("id"));
                        commentArticles[i].setCommentUserName(commentArray.getJSONObject(i).optString("username"));
                        commentArticles[i].setCommentText(commentArray.getJSONObject(i).optString("comment"));
                        commentArticles[i].setCommentId(commentArray.getJSONObject(i).optString("user_id"));
                        commentArticles[i].setCommenttime(commentArray.getJSONObject(i).optString("comment_time"));
                        mCommentArrayList.add(commentArticles[i]);
                        mCommentArticleAdapter = new CommentArticleAdapter(CommentListOfArticleActivity.this, mCommentArrayList);
                        mCommentListView.setAdapter(mCommentArticleAdapter);
                        mCommentArticleAdapter.notifyDataSetChanged();


                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
