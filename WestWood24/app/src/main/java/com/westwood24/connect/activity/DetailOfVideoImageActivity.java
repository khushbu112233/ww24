package com.westwood24.connect.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.westwood24.R;
import com.westwood24.connect.adapter.CommentArticleAdapter;
import com.westwood24.connect.adapter.VideoImageAdapter;
import com.westwood24.connect.adapter.VoteOfQuestionAdapter;
import com.westwood24.connect.fragment.TopNewsFirstFragment;
import com.westwood24.connect.model.AnswerStatus;
import com.westwood24.connect.model.Chart_Option;
import com.westwood24.connect.model.CommentDataList;
import com.westwood24.connect.model.VoteOfQuestion;
import com.westwood24.connect.utils.Config;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.FontCustom;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.janmuller.android.simplecropimage.Util;
import uk.co.deanwild.flowtextview.FlowTextView;

public class DetailOfVideoImageActivity extends YouTubeBaseActivity implements View.OnClickListener, YouTubePlayer.OnInitializedListener, WebserviceCall.WebserviceResponse {

    //UI Declaration..
    LinearLayout mCommentLn;
    String articlaPosition;
    ArrayList<CommentDataList> mCommentArrayList;
    CommentArticleAdapter mCommentArticleAdapter;
    TextView mVedImgTitleTv;
    ImageView mUserImageImg;
    ImageView mHeaderCommentImg;
    ImageView mHeaderVoteImg;
    RelativeLayout mHeaderBackRl;
    VideoView myVideo;
    ImageView img_left;
    TextView txt_comments;
    //Class Obj declaration..

    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mCustomView;
    private myWebChromeClient mWebChromeClient;
    private myWebViewClient mWebViewClient;
    Intent mIntent;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    TextView txt_vote_questions;
    Button mAddCommentBtn;
    TextView mViewAllBtn;
    Dialog myCustomeDialog;
    TextView mHeaderTitleTv, activity_detail_desc_tv;
    TextView mNoRecordTv,add_comment;
    Button mSubmitBtn;
    ArrayList<VoteOfQuestion> mVoteOfQuestionsArrayList;
    VoteOfQuestionAdapter mVoteOfQuestionAdapter;
    // YouTube player view
    private YouTubePlayerView youTubeView;
    String str_new;
    String userComment;
    TextView txt;
    //Variable declaration..
    String TAG = "VoteOftheArticleActivity";
    LinearLayout rl, ll_image;
    TextView btn_submit;
    RadioGroup[] rg;
    String strnew="";
    TextView txt_vote[],txt_vote1[];
    EditText[] txtview;
    TextView []txt_submit;
    public static CheckBox[][][] ch =new CheckBox[100][100][100];
    public static String[][][] ch_value =new String[100][100][100];
    TextView text_radio[][]=new TextView[100][100];
    //  TextView text_checkbox[][]=new TextView[100][100];
    int radio_flag=0;
    int count_ch = 0;
    RadioButton[][] rb;
    String answer;
    ArrayList<Chart_Option> chart_optionArrayList = new ArrayList<>();
    ImageView[] img_pie = new ImageView[100];
    ImageView[] img_bar = new ImageView[100];
    TextView[] txt1,txt3;
    String type;
    String typeoption;
    String questionid;
    String authkey;
    EditText activity_add_comment_edt;
    String articleId;
    FlowTextView flowTextView;
    TextView txt_text_des;
    LinearLayout ll_text;
    LinearLayout ll_comment_all;
    int flag_view_create=0;
    TextView title_img;
    ImageView back,img_main;
    TextView txt_title;
    LinearLayout ll_webview;
    WebView webview;
    String HtmlString,HtmlString1,EndingStatment,imageviewString,YoutubeString;
    Spanned html;
    boolean youtube_webview_flage =true;
    boolean flag=false;
    ImageView img_click;


    private FrameLayout customViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_videoimage);

        mIntent = getIntent();
        initComponents();

        mVoteOfQuestionsArrayList.clear();
        callGetDetailArticleAPI();
        callGetVoteofQuestionApi();
        prepareView();
        setActionListener();
        mCommentArrayList = new ArrayList<CommentDataList>();
        callCommentListAPI();
        // webview.reload();
        chart_optionArrayList.clear();


    }

    private void initComponents() {
        flowTextView = (FlowTextView) findViewById(R.id.ftv);
        flowTextView.setTextColor(Color.WHITE);
        flowTextView.setTextSize(getResources().getDimension(R.dimen.edittextsize));
        mVedImgTitleTv = (TextView) findViewById(R.id.activity_detail_title_tv);
        mUserImageImg = (ImageView) findViewById(R.id.activity_detail_image_img);
        ll_webview = (LinearLayout)findViewById(R.id.ll_webview);
        webview = (WebView)findViewById(R.id.webview);

        ll_text = (LinearLayout)findViewById(R.id.ll_text);
        txt_text_des = (TextView)findViewById(R.id.txt_text_des);
        img_click= (ImageView)findViewById(R.id.img_click);
        txt_title = (TextView)findViewById(R.id.txt_title_id);
        img_left = (ImageView)findViewById(R.id.img_left);
        ll_comment_all = (LinearLayout)findViewById(R.id.ll_comment_all);
        mHeaderCommentImg = (ImageView) findViewById(R.id.header_layout_comment_add_see_img);
        mHeaderVoteImg = (ImageView) findViewById(R.id.header_layout_vote_see_img);
        mHeaderBackRl = (RelativeLayout) findViewById(R.id.header_layout_back_rl);
        mHeaderBackRl.setVisibility(View.GONE);
        activity_detail_desc_tv = (TextView) findViewById(R.id.activity_detail_desc_tv);
        txt_vote_questions=(TextView)findViewById(R.id.txt_vote_questions);

        add_comment = (TextView)findViewById(R.id.add_comment);
        activity_add_comment_edt = (EditText)findViewById(R.id.activity_add_comment_edt);
        myVideo = (VideoView) findViewById(R.id.myVideo);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        mCommentLn = (LinearLayout) findViewById(R.id.activity_comment_list_ln);
        mViewAllBtn = (TextView) findViewById(R.id.activity_detail_viewall_comment_btn);
        mAddCommentBtn = (Button) findViewById(R.id.activity_detail_add_comment_btn);
        rl = (LinearLayout) findViewById(R.id.rl);
        ll_image = (LinearLayout) findViewById(R.id.ll_image);
        txt_comments=(TextView)findViewById(R.id.txt_comments);


        mVoteOfQuestionsArrayList = new ArrayList<>();
        youtube_webview_flage =true;

        txt_title.setTypeface(FontCustom.setFontBold(DetailOfVideoImageActivity.this));
        txt_vote_questions.setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));
        txt_comments.setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));
        activity_add_comment_edt.setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));
        add_comment.setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));

        //  mHeaderTitleTv.setTypeface(FontCustom.setFontBold(DetailOfVideoImageActivity.this));
    }

    private void prepareView() {
        //mVedImgTitleTv.setTypeface(FontCustom.setFont(this));
        mViewAllBtn.setTypeface(FontCustom.setFont(this));
        mAddCommentBtn.setTypeface(FontCustom.setFontBold(this));
        articlaPosition = mIntent.getStringExtra("articleid");
        mHeaderCommentImg.setVisibility(View.GONE);
        mHeaderVoteImg.setVisibility(View.GONE);

        if (mIntent.getStringExtra("check").equals("Image")) {
            mUserImageImg.setVisibility(View.VISIBLE);
            myVideo.setVisibility(View.GONE);
            youTubeView.setVisibility(View.GONE);
            ll_text.setVisibility(View.GONE);
            if(mIntent.getStringExtra("vidImgTitle").length()>0)
            mVedImgTitleTv.setText(mIntent.getStringExtra("vidImgTitle").substring(0, 1).toUpperCase() + mIntent.getStringExtra("vidImgTitle").substring(1));

            Picasso.with(DetailOfVideoImageActivity.this).load(mIntent.getStringExtra("image")).fit().into(mUserImageImg);
            mUserImageImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog listDialog = new Dialog(DetailOfVideoImageActivity.this);
                    LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    listDialog.setContentView(R.layout.doc_file_image_layout);
                    listDialog.setCanceledOnTouchOutside(true);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    Window window = listDialog.getWindow();
                    lp.copyFrom(window.getAttributes());
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    int height = displaymetrics.heightPixels;
                    int width = displaymetrics.widthPixels;
                    lp.width =width-50 ;
                    lp.height = height-200;
                    window.setAttributes(lp);

                    listDialog.getWindow().setGravity(Gravity.CENTER);
                    listDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                    title_img = (TextView)listDialog.findViewById(R.id.title);
                    back = (ImageView)listDialog.findViewById(R.id.back);
                    img_main = (ImageView)listDialog.findViewById(R.id.img_main);
                    Picasso.with(DetailOfVideoImageActivity.this).load(mIntent.getStringExtra("image")).fit().into(img_main);
                    title_img.setText(txt_title.getText().toString());



                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listDialog.dismiss();
                        }
                    });
                    listDialog.show();
                }
            });

        } else if (mIntent.getStringExtra("check").equals("Text")) {
            mUserImageImg.setVisibility(View.GONE);
            myVideo.setVisibility(View.GONE);
            youTubeView.setVisibility(View.GONE);
            ll_image.setVisibility(View.GONE);

            ll_text.setVisibility(View.VISIBLE);


        } else if (mIntent.getStringExtra("check").equals("Video")) {
            mVedImgTitleTv.setText(mIntent.getStringExtra("vidImgTitle").substring(0, 1).toUpperCase() + mIntent.getStringExtra("vidImgTitle").substring(1));

            String str = mIntent.getStringExtra("image");
            if (!str.equals("") && !str.isEmpty()) {
                mUserImageImg.setVisibility(View.GONE);
                myVideo.setVisibility(View.VISIBLE);
                youTubeView.setVisibility(View.GONE);
                ll_text.setVisibility(View.GONE);


                MediaController vidControl = new MediaController(this);
                vidControl.setAnchorView(myVideo);
                Uri vidUri = Uri.parse(str);
                myVideo.setMediaController(vidControl);
                vidControl.setAnchorView(vidControl);
                myVideo.setVideoURI(vidUri);
                myVideo.requestFocus();

                myVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Close the progress bar and play the video
                    public void onPrepared(MediaPlayer mp) {
                        //  pDialog.dismiss();
                        myVideo.start();
                    }
                });

            } else {
                Utils.showAlert(this, "Video is not available!");
            }

        } else if (mIntent.getStringExtra("check").equals("YouTube")) {
            String str = mIntent.getStringExtra("image");
            mVedImgTitleTv.setText(mIntent.getStringExtra("vidImgTitle").substring(0, 1).toUpperCase() + mIntent.getStringExtra("vidImgTitle").substring(1));

            Log.v("str",str+"---");
            ll_text.setVisibility(View.GONE);
            if (!str.equals("") && !str.isEmpty()) {
                if(str.contains("?"))
                {
                    String str1 = str.split("=")[1];

                    mUserImageImg.setVisibility(View.GONE);
                    myVideo.setVisibility(View.GONE);
                    youTubeView.setVisibility(View.VISIBLE);
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

                    mUserImageImg.setVisibility(View.GONE);
                    myVideo.setVisibility(View.GONE);
                    youTubeView.setVisibility(View.VISIBLE);
              /*  Pattern pattern = Pattern.compile(
                        "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                        Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(str);
                if (matcher.matches()){
                    str_new = matcher.group(1);
                }
*/
                    str_new = str1.split("/")[1];
                }
                //  youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

                // Initializing video player with developer key
                youTubeView.initialize(Config.DEVELOPER_KEY, this);

            } else {
                Utils.showAlert(this, "Video is not available!");
            }
        }
    }

    private void setActionListener() {
        mHeaderCommentImg.setOnClickListener(this);
        mHeaderVoteImg.setOnClickListener(this);
        img_left.setOnClickListener(this);
        mAddCommentBtn.setOnClickListener(this);
        mViewAllBtn.setOnClickListener(this);
        add_comment.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_layout_comment_add_see_img:
                startActivity(new Intent(DetailOfVideoImageActivity.this, CommentListOfArticleActivity.class).putExtra("articleid", mIntent.getStringExtra("articleid")));
                break;

            case R.id.header_layout_vote_see_img:
                startActivity(new Intent(DetailOfVideoImageActivity.this, VoteOftheArticleActivity.class).putExtra("articleid", mIntent.getStringExtra("articleid")));
                break;
            case R.id.img_left:
                //finish();
                startActivity(new Intent(DetailOfVideoImageActivity.this, DashBoardActivity.class));
                // finish();
                Pref.setValue(DetailOfVideoImageActivity.this, "offset", "0");
                break;
            case R.id.activity_detail_viewall_comment_btn:
                startActivity(new Intent(DetailOfVideoImageActivity.this, CommentListOfArticleActivity.class).putExtra("articleid", mIntent.getStringExtra("articleid")));
                break;
            case R.id.add_comment:
                if (Pref.getValue(this, Constants.TAG_USER_ID, "").equals("")) {
                    Utils.loginalert(DetailOfVideoImageActivity.this);
                    // Utils.showAlert(this, getResources().getString(R.string.dialog_title), getResources().getString(R.string.dialog_msg));
                } else {
                    userComment = activity_add_comment_edt.getText().toString().trim();
                    if (userComment.equals("")) {
                        Utils.showToast(DetailOfVideoImageActivity.this, "Please enter comment.");
                    } else {
                        callCommentSendAPI();
                    }
                }


                break;

        }
    }

    private void callGetDetailArticleAPI() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("articleId", mIntent.getStringExtra("articleid"));
            //  requestParams.put("articleId", "5");

            if (Pref.getValue(DetailOfVideoImageActivity.this, "user_type", "").equalsIgnoreCase("login")) {
                requestParams.put("authkey", Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_AUTHKAY, ""));
            }
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.GET_ARTICLE_DETAILS_URL, "getarticle_details", "post", requestParams);
            Log.e(TAG, "request is detail " + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void callGetVoteofQuestionApi() {
        mVoteOfQuestionsArrayList.clear();
        SplashActivity.answer_status_list.clear();
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("articleId", mIntent.getStringExtra("articleid"));

            if (Pref.getValue(DetailOfVideoImageActivity.this, "user_type", "").equalsIgnoreCase("login")) {
                requestParams.put("authkey", Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_AUTHKAY, ""));
            }
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.GET_VOTE_QUESTION_URL, "getvote_que", "post", requestParams);
            Log.e(TAG, "request is vote" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void callsetVoteApi() {
        try {
            RequestParams requestParams = new RequestParams();
            if (Pref.getValue(DetailOfVideoImageActivity.this, "user_type", "").equalsIgnoreCase("login")) {
                requestParams.put("authkey", Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_AUTHKAY, ""));
            }
            requestParams.put("articleId", mIntent.getStringExtra("articleid"));
            requestParams.put("type", type);
            requestParams.put("typeoption",typeoption);
            requestParams.put("questionid",questionid);

            // requestParams.put("answer", answer);
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.SETVOTE_OF_QUE_URL, "addvote", "post", requestParams);
            Log.e(TAG, "request is set vote-------" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format("There was an error initializing the YouTubePlayer ", errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
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
                    Utils.showToast(DetailOfVideoImageActivity.this, "Please enter comment.");
                } else {
                    myCustomeDialog.dismiss();
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
            requestParams.put("articleId", mIntent.getStringExtra("articleid"));
            if (Pref.getValue(DetailOfVideoImageActivity.this, "user_type", "").equalsIgnoreCase("login")) {
                requestParams.put("authkey", Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_AUTHKAY, ""));
            }
            requestParams.put("comment", userComment);
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.ADD_ARTICLES_COMMENT_URL, "send_usercomment", "post", requestParams);
            Log.e("send comment", "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void callCommentListAPI() {
        try {
            RequestParams requestParams = new RequestParams();

            //requestParams.put("articleId", "5");
            requestParams.put("articleId", mIntent.getStringExtra("articleid"));
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.GET_ARTICLE_COMMENT_URL, "article_comment", "post", requestParams);
            Log.e("Registration", "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void success(String url, String apiName, String response) {
        Log.e(TAG, "response" + response);
        JSONObject mainObj = null;
        try {
            mainObj = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String status = mainObj.optString("status");
        // String message = mainObj.optString("message");
        String response_code = mainObj.optString("code");

        if (response_code.equals("200")) {
            if (apiName.equals("send_usercomment")) {
                finish();
                startActivity(mIntent);
                myCustomeDialog.dismiss();

                try {
                    JSONObject jsonObjectdata = mainObj.getJSONObject("data");
                    String comment = jsonObjectdata.optString("comment");
                    Log.e(TAG, "comment" + comment);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if(apiName.equals("addvote"))
            {
                // callGetVoteofQuestionApi();
                if(mIntent.getStringExtra("check").equals("Image"))
                {
                    startActivity(new Intent(DetailOfVideoImageActivity.this, DetailOfVideoImageActivity.class).putExtra("vidImgTitle",mIntent.getStringExtra("vidImgTitle")).putExtra("image",mIntent.getStringExtra("image")).putExtra("articleid", mIntent.getStringExtra("articleid")).putExtra("articleType",mIntent.getStringExtra("articleType")).putExtra("check", "Image").putExtra("votable",mIntent.getStringExtra("votable")));
                    finish();

                }else if(mIntent.getStringExtra("check").equals("Text"))
                {

                    startActivity(new Intent(DetailOfVideoImageActivity.this, DetailOfVideoImageActivity.class).putExtra("vidImgTitle",mIntent.getStringExtra("vidImgTitle")).putExtra("image",mIntent.getStringExtra("image")).putExtra("articleid", mIntent.getStringExtra("articleid")).putExtra("articleType",mIntent.getStringExtra("articleType")).putExtra("check", "Text").putExtra("votable",mIntent.getStringExtra("votable")));
                    finish();
                }else if(mIntent.getStringExtra("check").equals("Video"))
                {

                    startActivity(new Intent(DetailOfVideoImageActivity.this, DetailOfVideoImageActivity.class).putExtra("vidImgTitle",mIntent.getStringExtra("vidImgTitle")).putExtra("image",mIntent.getStringExtra("image")).putExtra("articleid", mIntent.getStringExtra("articleid")).putExtra("articleType",mIntent.getStringExtra("articleType")).putExtra("check", "Video").putExtra("votable",mIntent.getStringExtra("votable")));
                    finish();
                }else if(mIntent.getStringExtra("check").equals("YouTube")){

                    startActivity(new Intent(DetailOfVideoImageActivity.this, DetailOfVideoImageActivity.class).putExtra("vidImgTitle",mIntent.getStringExtra("vidImgTitle")).putExtra("image",mIntent.getStringExtra("image")).putExtra("articleid", mIntent.getStringExtra("articleid")).putExtra("articleType",mIntent.getStringExtra("articleType")).putExtra("check", "YouTube").putExtra("votable",mIntent.getStringExtra("votable")));
                    finish();
                }
                String message = mainObj.optString("message");
                Utils.showToast(DetailOfVideoImageActivity.this, message);

                Log.e("mainObjaddvote",""+message);
                // SplashActivity.callTopNewsDetailApi();
                //
            }else if (apiName.equals("article_comment")) {
                try {
                    mAddCommentBtn.setVisibility(View.VISIBLE);


                    // JSONObject dataObj = mainObj.getJSONObject("data");
                    JSONArray commentArray = mainObj.getJSONArray("data");
                    Log.e(TAG, "commentArray " + commentArray.length());
                    if (commentArray.length() == 0) {
                        LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) mCommentLn.getLayoutParams();
                        lParams.weight = 2.0f;
                        mAddCommentBtn.setLayoutParams(lParams);
                        ll_comment_all.setVisibility(View.GONE);
                        mViewAllBtn.setVisibility(View.GONE);
                    } else if (commentArray.length() <= 5) {
                        LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) mCommentLn.getLayoutParams();
                        lParams.weight = 2.0f;
                        mAddCommentBtn.setLayoutParams(lParams);
                        mViewAllBtn.setVisibility(View.GONE);
                        ll_comment_all.setVisibility(View.GONE);
                        CommentDataList[] commentArticles = new CommentDataList[commentArray.length()];
                        for (int i = 0; i <commentArticles.length; i++) {
                            commentArticles[i] = new CommentDataList();
                            commentArticles[i].setCommentId(commentArray.getJSONObject(i).optString("id"));
                            commentArticles[i].setCommentUserName(commentArray.getJSONObject(i).optString("username"));
                            commentArticles[i].setCommentText(commentArray.getJSONObject(i).optString("comment"));
                            commentArticles[i].setCommentId(commentArray.getJSONObject(i).optString("user_id"));
                            commentArticles[i].setCommenttime(commentArray.getJSONObject(i).optString("comment_time"));
                            Collections.reverse(mCommentArrayList);
                            mCommentArrayList.clear();
                            mCommentArrayList.add(commentArticles[i]);
                            Collections.reverse(mCommentArrayList);

                            mCommentArticleAdapter = new CommentArticleAdapter(DetailOfVideoImageActivity.this, mCommentArrayList);
                            for (int j = 0; j < mCommentArticleAdapter.getCount(); j++) {
                                View view = mCommentArticleAdapter.getView(j, null, mCommentLn);
                                mCommentLn.addView(view);
                            }
                        }
                    } else {
                        ll_comment_all.setVisibility(View.VISIBLE);

                        CommentDataList[] commentArticles = new CommentDataList[commentArray.length()];
                        for (int i = 0; i < 5; i++) {
                            commentArticles[i] = new CommentDataList();
                            commentArticles[i].setCommentId(commentArray.getJSONObject(i).optString("id"));
                            commentArticles[i].setCommentUserName(commentArray.getJSONObject(i).optString("username"));
                            commentArticles[i].setCommentText(commentArray.getJSONObject(i).optString("comment"));
                            commentArticles[i].setCommentId(commentArray.getJSONObject(i).optString("user_id"));
                            commentArticles[i].setCommenttime(commentArray.getJSONObject(i).optString("comment_time"));
                            Collections.reverse(mCommentArrayList);
                            mCommentArrayList.clear();
                            mCommentArrayList.add(commentArticles[i]);
                            Collections.reverse(mCommentArrayList);

                            mCommentArticleAdapter = new CommentArticleAdapter(DetailOfVideoImageActivity.this, mCommentArrayList);
                            for (int j = 0; j < mCommentArticleAdapter.getCount(); j++) {
                                View view = mCommentArticleAdapter.getView(j, null, mCommentLn);
                                mCommentLn.addView(view);
                            }
                        }

                        mViewAllBtn.setVisibility(View.VISIBLE);
                        mAddCommentBtn.setVisibility(View.GONE);
                    }



                    //mCommentListView.setAdapter(mCommentArticleAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (apiName.equals("getvote_que")) {
                try {

                    Log.e("getvote_que", "" + mainObj);
                    //JSONObject jsonObjectdata = mainObj.getJSONObject("data");

                    JSONArray jsonArray_voteque = mainObj.getJSONArray("data");

                    VoteOfQuestion[] voteOfQuestions = new VoteOfQuestion[jsonArray_voteque.length()];
                    for (int i = 0; i < jsonArray_voteque.length(); i++) {
                        voteOfQuestions[i] = new VoteOfQuestion();
                        voteOfQuestions[i].setQuestId(jsonArray_voteque.getJSONObject(i).optString("id"));
                        voteOfQuestions[i].setQuestiontitle(jsonArray_voteque.getJSONObject(i).optString("question_title"));
                        voteOfQuestions[i].setAnswerflag(jsonArray_voteque.getJSONObject(i).optString("answerflag"));
                        voteOfQuestions[i].setAnswer(jsonArray_voteque.getJSONObject(i).optString("options_name"));
                        voteOfQuestions[i].setVotetype(jsonArray_voteque.getJSONObject(i).optString("question_type"));

                        //  voteOfQuestions[i].setOptionCount(jsonArray_voteque.getJSONObject(i).optString("options_count"));
                        AnswerStatus[] answerStatus=new AnswerStatus[1];
                        answerStatus[0]=new AnswerStatus();
                        answerStatus[0].setArticle_ida(mIntent.getStringExtra("articleid"));
                        answerStatus[0].setQuestion_ida(jsonArray_voteque.getJSONObject(i).optString("id"));
                        answerStatus[0].setAnswer_flaga(jsonArray_voteque.getJSONObject(i).optString("answerflag"));
                        SplashActivity.answer_status_list.add(answerStatus[0]);


                        if (!jsonArray_voteque.getJSONObject(i).optString("question_type").equalsIgnoreCase("text")) {
                            ArrayList<String> arrayListtitle = new ArrayList<>();
                            for (int count = 0; count < jsonArray_voteque.getJSONObject(i).getJSONArray("question_options").length(); count++) {
                                if (jsonArray_voteque.getJSONObject(i).getJSONArray("question_options").get(count).toString() != null) {
                                    if (jsonArray_voteque.getJSONObject(i).getJSONArray("question_options").get(count).toString().length() > 0) {
                                        arrayListtitle.add(jsonArray_voteque.getJSONObject(i).getJSONArray("question_options").get(count).toString());
                                    }
                                }

                            }
                            voteOfQuestions[i].setVoteoptions(arrayListtitle);
                            ArrayList<String> arrayListtitle1 = new ArrayList<>();

                            for (int j = 0; j < arrayListtitle.size(); j++) {
                                arrayListtitle1.add(jsonArray_voteque.getJSONObject(i).getJSONObject("options_count").get(arrayListtitle.get(j)).toString());

                                //   Log.e("option_count", "" + jsonArray_voteque.getJSONObject(i).getJSONObject("options_count").get(arrayListtitle.get(j)));
                            }
                            voteOfQuestions[i].setOptionCount(arrayListtitle1);
                        }
                        mVoteOfQuestionsArrayList.add(voteOfQuestions[i]);
                        //String voteOptions = jsonArray_voteque.getJSONObject(i).optString("voteoptions");


                        //String voteOptionArray[] = voteOptions.split(",");


                   /* for (int j = 0; j < voteOptionArray.length; j++) {
                        voteOfQuestions[i].setVoteoptions(voteOptionArray[j]);

                        Log.e(TAG, "vote@@@ " + voteOfQuestions[i].getVoteoptions());
                        mVoteOfQuestionsArrayList.add(voteOfQuestions[i]);
                    }*/
                    }
                    Log.e(TAG, "vote@@@ " + mVoteOfQuestionsArrayList);
                    if (jsonArray_voteque.length() == 0) {
                        btn_submit.setVisibility(View.GONE);
                        // mNoRecordTv.setVisibility(View.VISIBLE);
                        // mNoRecordTv.setText("No Record Found!");
                    } else {
                        //   btn_submit.setVisibility(View.VISIBLE);
                    }
                   /*  mVoteOfQuestionAdapter = new VoteOfQuestionAdapter(DetailOfVideoImageActivity.this, mVoteOfQuestionsArrayList);
                     mVoteOfQuestionLv.setAdapter(mVoteOfQuestionAdapter);
*/

                    if(flag_view_create==0) {
                        createview();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (apiName.equals("getarticle_details")) {
                try {

                    Log.e("article_detailObj", "" + mainObj);
                    JSONObject article_detailObj = mainObj.getJSONObject("data");
                    String id = article_detailObj.optString("id");
                    String article_type = article_detailObj.optString("articleType");
                    String title = article_detailObj.optString("title");
                    String description = article_detailObj.optString("description");
                    String no_viewers = article_detailObj.optString("noViewer");
                    String no_commenters = article_detailObj.optString("noCommenter");
                    String image = article_detailObj.optString("mediaSource");
                    String thumb_image = article_detailObj.optString("thumbFormediaSource");
                    String posttime = article_detailObj.optString("posttime");
                    txt_title.setText(title);
                    Pref.setValue(DetailOfVideoImageActivity.this, "Htitle", title);


                    html = Html.fromHtml(description);
                    if(mIntent.getStringExtra("check").equals("Text"))
                    {

                       // txt_text_des.setText(html);
                    }else {
                        flowTextView.setText(html);
                    }

                 //   HtmlString = "<html><head><metacharset="+"UTF-8"+"><title>Description</title></head><body text="+"white"+">";
                    HtmlString1 = "<html>" +
                            "<head><metacharset=\"+\"UTF-8\"+\">" +
                            "<style type=\"text/css\">" +
                            "@font-face {" +
                            "font-family: MyFont;" +
                            "src: url(\"file:///android_asset/fonts/Myriad-Pro-regular.ttf\")" +
                            "}" +
                            "body {" +
                            "font-family: MyFont;" +
                            "font-size: medium;" +
                            "text-align: justify;" +
                            "}" +
                            "</style>" +
                            "</head>" +
                            "<body text=white>";
                    HtmlString="<html>" +
                            "<head><metacharset=\"+\"UTF-8\"+\">" +
                            "<style type=\"text/css\">" +
                            "@font-face {" +
                            "font-family: MyFont;" +
                            "src: url(\"file:///android_asset/fonts/Myriad-Pro-regular.ttf\")" +
                            "}" +
                            "body {" +
                            "font-family: MyFont;" +
                            "font-size: medium;" +
                            "text-align: justify;" +
                            "}" +
                            "</style>" +
                            "</head>" +
                            "<body text=white>";


                    if (mIntent.getStringExtra("check").equals("Image")) {

                        imageviewString = "<a href="+"#popup"+"><img left src="+mIntent.getStringExtra("image")+" style="+"width:150px;height:150px; margin-right: 10px\" align=\"left\"></a>";
                        HtmlString = HtmlString.concat(imageviewString);
                         //EndingStatment= description+"<style>body { font: 'Myriad-Pro' }</style></body></html>";

                        EndingStatment= description+"</body></html>";
                       // EndingStatment=description+"<style>body @font-face {font-family:'Myriad-Pro';src: url('fonts/Myriad-Pro-regular.ttf');}</style></body></html>";

                        HtmlString = HtmlString.concat(EndingStatment);
                        webview.setBackgroundColor(Color.TRANSPARENT);
                        webview.loadDataWithBaseURL("", HtmlString, "text/html", "UTF-8", "");
                        img_click.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Dialog listDialog = new Dialog(DetailOfVideoImageActivity.this);
                                LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                listDialog.setContentView(R.layout.doc_file_image_layout);
                                listDialog.setCanceledOnTouchOutside(true);
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                Window window = listDialog.getWindow();
                                lp.copyFrom(window.getAttributes());
                                DisplayMetrics displaymetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                                int height = displaymetrics.heightPixels;
                                int width = displaymetrics.widthPixels;
                                lp.width = width - 50;
                                lp.height = height - 200;
                                window.setAttributes(lp);

                                listDialog.getWindow().setGravity(Gravity.CENTER);
                                listDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                                title_img = (TextView) listDialog.findViewById(R.id.title);
                                back = (ImageView) listDialog.findViewById(R.id.back);
                                img_main = (ImageView) listDialog.findViewById(R.id.img_main);
                                Picasso.with(DetailOfVideoImageActivity.this).load(mIntent.getStringExtra("image")).fit().into(img_main);
                                title_img.setText(txt_title.getText().toString());


                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        listDialog.dismiss();
                                    }
                                });
                                listDialog.show();
                            }
                        });

                        //   webview.getSettings().setJavaScriptEnabled(true);
                        //  webview.addJavascriptInterface(new JsInterface(this,HtmlString),"#openPopup");



                    }else if(mIntent.getStringExtra("check").equals("Text"))
                    {
                        img_click.setVisibility(View.GONE);
                        EndingStatment= description+"</body></html>";
                        //EndingStatment= description+"</body></html>";
                        HtmlString = HtmlString.concat(EndingStatment);
                        webview.setBackgroundColor(Color.TRANSPARENT);
                        Log.e("EndingStatment",HtmlString+"---");
                        webview.loadDataWithBaseURL("", HtmlString, "text/html", "UTF-8", "");
                    }else if (mIntent.getStringExtra("check").equals("Video")) {
                        img_click.setVisibility(View.GONE);
                       // EndingStatment= description+"<style>body { font: '' }</style></body></html>";
                        EndingStatment= description+"</body></html>";
                        HtmlString = HtmlString.concat(EndingStatment);
                        webview.setBackgroundColor(Color.TRANSPARENT);
                        webview.loadDataWithBaseURL("", HtmlString, "text/html", "UTF-8", "");
                    } else if (mIntent.getStringExtra("check").equals("YouTube")) {

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);

                        int height = metrics.heightPixels;
                        int width = (metrics.widthPixels/2)-100;
                        int width1=(metrics.widthPixels/2);


                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                        int height_new = displaymetrics.heightPixels;
                        int width_new = (displaymetrics.widthPixels)/2;


                        img_click.setVisibility(View.VISIBLE);


                        img_click.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(DetailOfVideoImageActivity.this,YoutubeActivity.class);
                                intent.putExtra("str_new",str_new);
                                startActivity(intent);
                            }
                        });

                        /*if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                            //Do some stuff

                            Log.v("width_new","land:"+width_new+" "+str_new);
                            YoutubeString="<iframe width=100%"+" height="+280+" class =\"youtube-player\" src=\"https://www.youtube.com/embed/"+str_new +"\" frameborder=\"0\" allowfullscreen style=\"+\"width:"+700+"height:"+300+" margin-right: 10px\" align=\"left\" ></iframe>";


                        }
                        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                            //Do some stuff
                            Log.v("width_new","land:"+width_new+" "+str_new);
                            Log.v("width_new","land:"+width_new);
                            YoutubeString="<iframe width=100%"+" height="+300+" class =\"youtube-player\" src=\"https://www.youtube.com/embed/"+str_new +"\" frameborder=\"0\" allowfullscreen style=\"+\"width:"+400+"height:"+300+" margin-right: 10px\" align=\"left\" ></iframe>";

                        }*/

                        YoutubeString="<iframe width="+150+" height="+150+" class =\"youtube-player\" src=\"https://www.youtube.com/embed/"+str_new +"\" frameborder=\"0\" allowfullscreen style=\"+\"width:"+700+"height:"+300+" margin-right: 10px\" align=\"left\" ></iframe>";

                        // YoutubeString = "<iframe width="+200+"height="+200+"src="+"http://www.youtube.com/embed/"+str_new+" class="+"youtube-player"+" frameborder="+1+"allowfullscreen style="+"background:white; position:relative; float:left; margin-right: 10px; left:0; "+"></iframe>";
                        HtmlString1 = HtmlString1.concat(YoutubeString);

                        //EndingStatment= description+"</body></html>";
                        EndingStatment= description+"</body></html>";
                        HtmlString1 = HtmlString1.concat(EndingStatment);
                        Log.e("HtmlString", "" + HtmlString1);

                        //  webview.loadUrl(HtmlString);
                        webview.setWebViewClient(new WebViewClient() {
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
                        webview.setBackgroundColor(Color.TRANSPARENT);
                        webview.getSettings().setJavaScriptEnabled(true);
                      //  webview.loadData(HtmlString1, "text/html", "UTF-8");
                        webview.loadDataWithBaseURL("", HtmlString1, "text/html", "UTF-8", "");


                    }

                    //  activity_detail_desc_tv.setText(Html.fromHtml(description));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {


                String message = mainObj.optString("message");
                Utils.showToast(this, message);
            }
        } else if(response_code.equals("1000")){
            String message = mainObj.optString("message");
            Pref.deleteAll(DetailOfVideoImageActivity.this);
            DashBoardActivity.mDrawerLayout.closeDrawers();
            startActivity(new Intent(DetailOfVideoImageActivity.this, SplashActivity.class));

            Pref.setValue(DetailOfVideoImageActivity.this, "user_type", "guest");
            Utils.showToast(DetailOfVideoImageActivity.this, message);
        }else {

            String message = mainObj.optString("message");
            Utils.showToast(this, message);

        }


    }

    @Override
    public void failure(String url, String apiName, String response) {
        JSONObject mainObj = null;
//        String response_code = mainObj.optString("code");



        if (apiName.equalsIgnoreCase("article_comment")) {
            try {
                mainObj = new JSONObject(response);
                String message = mainObj.optString("message");
                //    Utils.showToast(this,"No Comments");
                TextView tv3 = new TextView(DetailOfVideoImageActivity.this);
                tv3.setText("No Comments");
                tv3.setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));

                tv3.setTextColor(Color.WHITE);
                tv3.setPadding(5, 5, 5, 5);
                mCommentLn.addView(tv3);
                LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) mCommentLn.getLayoutParams();
                lParams.weight = 2.0f;
                mAddCommentBtn.setLayoutParams(lParams);
                mViewAllBtn.setVisibility(View.GONE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiName.equals("addvote")) {

            try {
                mainObj = new JSONObject(response);

                String message = mainObj.optString("message");
                Utils.showToast(DetailOfVideoImageActivity.this, message);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (apiName.equals("getvote_que")) {
            rl.setVisibility(View.GONE);

            String message = mainObj.optString("message");
            Utils.showToast(this, message);

        }
     /*   } else if(response_code.equals("1000")){
            String message = mainObj.optString("message");

            Pref.deleteAll(DetailOfVideoImageActivity.this);

            DashBoardActivity.mDrawerLayout.closeDrawers();
            startActivity(new Intent(DetailOfVideoImageActivity.this, SplashActivity.class));
            Utils.showToast(DetailOfVideoImageActivity.this, message);
        }else {

            String message = mainObj.optString("message");
            Utils.showToast(this, message);

        }*/

    }


    public void createview() {
        rb = new RadioButton[mVoteOfQuestionsArrayList.size()][100];
        txt_vote = new TextView[mVoteOfQuestionsArrayList.size()];
        txt_vote1 = new TextView[mVoteOfQuestionsArrayList.size()];
        rg = new RadioGroup[mVoteOfQuestionsArrayList.size()];
        // ch = new CheckBox[100][100];
        txt1=new TextView[mVoteOfQuestionsArrayList.size()];
        txt3 = new TextView[mVoteOfQuestionsArrayList.size()];
        txtview = new EditText[mVoteOfQuestionsArrayList.size()];
        txt_submit = new TextView[mVoteOfQuestionsArrayList.size()];
        btn_submit = new TextView(DetailOfVideoImageActivity.this);
        btn_submit.setText("SUBMIT");
        btn_submit.setBackgroundColor(getResources().getColor(R.color.login_btn_color));
        btn_submit.setTextColor(Color.WHITE);
        btn_submit.setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));


        for ( int i = 0; i < mVoteOfQuestionsArrayList.size(); i++) {
            LinearLayout ll1 = new LinearLayout(this);
            ll1.setOrientation(LinearLayout.HORIZONTAL);
            ll1.setWeightSum(1f);
            txt = new TextView(this);
            txt.setTypeface(FontCustom.setFontBold(DetailOfVideoImageActivity.this));
            txt.setPadding(0, 5, 0, 5);
            txt.setTextColor(Color.WHITE);
            txt.setTextSize(14);
            txt.setTypeface(FontCustom.setFontBold(DetailOfVideoImageActivity.this));
            txt.setText(mVoteOfQuestionsArrayList.get(i).getQuestiontitle());



            LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
            childParam1.weight = 1f;
            txt.setLayoutParams(childParam1);
            ll1.addView(txt);
            rl.addView(ll1);

            Log.v("votetype",mVoteOfQuestionsArrayList.get(i).getVotetype()+"");
            if (mVoteOfQuestionsArrayList.get(i).getVotetype().equals("text")) {


                LinearLayout ll2 = new LinearLayout(this);
                ll2.setOrientation(LinearLayout.HORIZONTAL);
                ll2.setWeightSum(2f);

                txtview[i] = new EditText(this);
                txtview[i].setHint("Answer");

                txtview[i].setTextSize(13.0f);
                txtview[i].setTextColor(getResources().getColor(R.color.white));
                txtview[i].setHintTextColor(getResources().getColor(R.color.white));
                txtview[i].setBackgroundResource(R.drawable.edittext_field_background_white);
                txtview[i].setGravity(Gravity.CENTER);
                txtview[i].setPadding(5, 15, 5, 15);

                //   ll2.addView(txtview[i]);
                LinearLayout.LayoutParams childParam = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
                childParam.weight = 1.45f;
                txtview[i].setLayoutParams(childParam);
                ll2.addView(txtview[i]);


                txt_submit[i] = new TextView(this);
                txt_submit[i].setText("SUBMIT");
                txt_submit[i].setPadding(5, 15, 5, 15);
                txt_submit[i].setClickable(true);
                txt_submit[i].setVisibility(View.VISIBLE);
                txt_submit[i].setTextColor(getResources().getColor(R.color.white));
                txt_submit[i].setBackgroundResource(R.color.dark_purple);
                txt_submit[i].setGravity(Gravity.CENTER);
                txt_submit[i].setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));

                LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
                childParam2.weight = 0.55f;
                childParam2.setMargins(10,0,0,0);
                txt_submit[i].setLayoutParams(childParam2);
                ll2.addView(txt_submit[i]);
                ll2.setPadding(0, 10, 0, 30);

                // rl.addView(ll2);
                rl.addView(ll2);
                rl.setPadding(0, 50, 0, 50);
               // add_chart(i);
                final int  temp = i;

                final int finalI2 = i;
                txt_submit[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(DetailOfVideoImageActivity.this,"dfffff",Toast.LENGTH_LONG).show();
                        int flag_que = 0;
                        Log.e("Details_articleid", "" + mIntent.getStringExtra("articleid"));
                        Log.e("Details_queid", "" + mVoteOfQuestionsArrayList.get(finalI2).getQuestId());
                        typeoption = txtview[finalI2].getText().toString();

                        if (Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_USER_ID, "").equals("")) {

                            Utils.loginalert(DetailOfVideoImageActivity.this);
                            //  Utils.showAlert(context, context.getResources().getString(R.string.dialog_title), context.getResources().getString(R.string.dialog_msg));
                        } else {


                            for (int temp_answer = 0; temp_answer < SplashActivity.answer_status_list.size(); temp_answer++) {

                                if (SplashActivity.answer_status_list.get(temp_answer).getArticle_ida().equalsIgnoreCase(mIntent.getStringExtra("articleid"))) {
                                    if (SplashActivity.answer_status_list.get(temp_answer).getQuestion_ida().equalsIgnoreCase(mVoteOfQuestionsArrayList.get(finalI2).getQuestId())) {

                                        if (SplashActivity.answer_status_list.get(temp_answer).getAnswer_flaga().equalsIgnoreCase("1")) {

                                            Log.e("question_id_adapter", "" + mVoteOfQuestionsArrayList.get(finalI2).getQuestId() + "-------------------stop");
                                            Utils.showAlert(DetailOfVideoImageActivity.this, "You already selected an answer for this question.");
                                            break;

                                        } else {
                                            Log.e("question_id_adapter", "execute--------------------------------");
                                            if (typeoption.length() == 0) {
                                                Utils.showAlert(DetailOfVideoImageActivity.this, "Please mention your vote");
                                                break;
                                            } else {
                                                callanswermethod_text(finalI2);
                                                break;
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                });

            } else if (mVoteOfQuestionsArrayList.get(i).getVotetype().equals("radio")) {
                if(mIntent.getStringExtra("votable").equals("1")) {
                    //  rg[i] = new RadioGroup(this);

                    int count_radio=0;
                    LinearLayout ll2 = null;
                    Log.e("sizeofjradio",""+mVoteOfQuestionsArrayList.get(i).getVoteoptions().size());
                    for (int j = 0; j < mVoteOfQuestionsArrayList.get(i).getVoteoptions().size(); j++) {

                        LinearLayout ll = new LinearLayout(this);
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.setPadding(30, 0, 30, 0);
                        if(mVoteOfQuestionsArrayList.get(i).getVoteoptions().size()%2==0)
                        {
                            for (int temp=0;temp<2;temp++) {

                                text_radio[i][j] = new TextView(this);
                                text_radio[i][j].setText(mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(j).toString()+"  ("+mVoteOfQuestionsArrayList.get(i).getOptionCount().get(j).toString()+")");
                                SpannableStringBuilder ssb = new SpannableStringBuilder(mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(j)+"  ("+mVoteOfQuestionsArrayList.get(i).getOptionCount().get(j).toString()+")");
                                //ssb.append("By registering you are agreeing to our privacy policy and terms of use.");

                                final int finalI = i;
                                ssb.setSpan(new ClickableSpan() {

                                    @Override
                                    public void onClick(View v) {
                                        txt.callOnClick();
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(getResources().getColor(R.color.white));
                                        ds.setUnderlineText(false);
                                        ds.setTextSize(getResources().getDimension(R.dimen.description_size));
                                    }

                                }, 0, mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(j).length(), 0);

                                ssb.setSpan(new ClickableSpan() {

                                    @Override
                                    public void onClick(View v) {
                                        txt.callOnClick();
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(getResources().getColor(R.color.white));
                                        ds.setUnderlineText(false);
                                        ds.setTextSize(getResources().getDimension(R.dimen.lablesize1));
                                    }

                                }, mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(j).toString().length() + 2, text_radio[i][j].getText().toString().length(), 0);
                                //   text_radio[i][j].setMovementMethod(LinkMovementMethod.getInstance());
                                text_radio[i][j].setText(ssb);




                                text_radio[i][j].setGravity(Gravity.CENTER_VERTICAL);
                                text_radio[i][j].setTextColor(Color.WHITE);
                                text_radio[i][j].setId(j);
                                text_radio[i][j].setPadding(5, 5, 5, 5);
                                text_radio[i][j].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.uncheck_white, 0, 0, 0);
                                text_radio[i][j].setCompoundDrawablePadding(25);
                                text_radio[i][j].setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));

                                LinearLayout.LayoutParams childParamvote1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                                childParamvote1.weight = 0.5f;
                                text_radio[i][j].setLayoutParams(childParamvote1);

                                ll.addView(text_radio[i][j]);
                                final int finalI1 = i;
                                final int finalJ = j;
                                text_radio[i][j].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        radio_flag++;
                                        typeoption=text_radio[finalI1][finalJ].getText().toString().split("\\(")[0].trim();

                                        for(int k = 0;k<mVoteOfQuestionsArrayList.get(finalI1).getVoteoptions().size();k++)
                                        {
                                            Log.e("textradio value", "" + text_radio[finalI1][k].getId());
                                            if(finalJ==text_radio[finalI1][k].getId()) {
                                                //   text_radio[finalI][k].setTextColor(Color.RED);
                                                text_radio[finalI1][k].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_white, 0, 0, 0);


                                            }else
                                            {
                                                //  text_radio[finalI][k].setTextColor(Color.WHITE);
                                                text_radio[finalI1][k].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.uncheck_white, 0, 0, 0);

                                            }
                                        }

                                    }
                                });

                                j++;

                            }
                            j--;
                        }else {
                            for (int temp = 0; temp < 2; temp++)
                            {
                                text_radio[i][j] = new TextView(this);
                                text_radio[i][j].setText(mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(j).toString() + "  (" + mVoteOfQuestionsArrayList.get(i).getOptionCount().get(j).toString() + ")");
                                SpannableStringBuilder ssb = new SpannableStringBuilder(mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(j)+"  ("+mVoteOfQuestionsArrayList.get(i).getOptionCount().get(j).toString()+")");
                                //ssb.append("By registering you are agreeing to our privacy policy and terms of use.");

                                final int finalI = i;
                                ssb.setSpan(new ClickableSpan() {

                                    @Override
                                    public void onClick(View v) {
                                        txt.callOnClick();
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(getResources().getColor(R.color.white));
                                        ds.setUnderlineText(false);
                                        ds.setTextSize(getResources().getDimension(R.dimen.description_size));
                                    }

                                }, 0, mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(j).length(), 0);

                                ssb.setSpan(new ClickableSpan() {

                                    @Override
                                    public void onClick(View v) {
                                        txt.callOnClick();
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(getResources().getColor(R.color.white));
                                        ds.setUnderlineText(false);
                                        ds.setTextSize(getResources().getDimension(R.dimen.lablesize1));
                                    }

                                }, mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(j).toString().length() + 2, text_radio[i][j].getText().toString().length(), 0);
                                //  text_radio[i][j].setMovementMethod(LinkMovementMethod.getInstance());
                                text_radio[i][j].setText(ssb);
                                text_radio[i][j].setGravity(Gravity.CENTER_VERTICAL);
                                text_radio[i][j].setTextColor(Color.WHITE);
                                text_radio[i][j].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.uncheck_white, 0, 0, 0);
                                text_radio[i][j].setCompoundDrawablePadding(25);
                                text_radio[i][j].setId(j);
                                text_radio[i][j].setPadding(5, 5, 5, 5);
                                text_radio[i][j].setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));

                                LinearLayout.LayoutParams childParamvote1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                                childParamvote1.weight = 0.5f;
                                text_radio[i][j].setLayoutParams(childParamvote1);

                                ll.addView(text_radio[i][j]);
                                final int finalI1 = i;
                                final int finalJ = j;

                                text_radio[i][j].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Log.e("textradio value", "" + text_radio[finalI][finalJ].getId());
                                        typeoption=text_radio[finalI1][finalJ].getText().toString().split("\\(")[0].trim();

                                        radio_flag++;
                                        for(int k = 0;k<mVoteOfQuestionsArrayList.get(finalI1).getVoteoptions().size();k++)
                                        {
                                            Log.e("textradio value", "" + text_radio[finalI1][k].getId());

                                            if(finalJ==text_radio[finalI1][k].getId()) {
                                                //   text_radio[finalI][k].setTextColor(Color.RED);
                                                text_radio[finalI1][k].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_white, 0, 0, 0);

                                            }else
                                            {
                                                //     text_radio[finalI][k].setTextColor(Color.WHITE);
                                                text_radio[finalI1][k].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.uncheck_white, 0, 0, 0);

                                            }
                                        }

                                        // text_radio[finalI][finalJ].setId(finalJ);
                                    }
                                });

                                count_radio++;
                                j++;
                            }
                        }

                        rl.addView(ll);

                        if(mVoteOfQuestionsArrayList.get(i).getVoteoptions().size()%2!=0) {

                            if (count_radio == mVoteOfQuestionsArrayList.get(i).getOptionCount().size() - 1) {

                                ll2 = new LinearLayout(this);
                                ll2.setOrientation(LinearLayout.HORIZONTAL);
                                ll2.setPadding(30, 0, 30, 0);
                                for (int temp = 0; temp < 1; temp++) {

                                    text_radio[i][count_radio] = new TextView(this);
                                    text_radio[i][count_radio].setText(mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(count_radio).toString() + "  (" + mVoteOfQuestionsArrayList.get(i).getOptionCount().get(count_radio).toString() + ")");
                                    SpannableStringBuilder ssb = new SpannableStringBuilder(mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(count_radio)+"  ("+mVoteOfQuestionsArrayList.get(i).getOptionCount().get(count_radio).toString()+")");
                                    //ssb.append("By registering you are agreeing to our privacy policy and terms of use.");

                                    final int finalI = i;
                                    ssb.setSpan(new ClickableSpan() {

                                        @Override
                                        public void onClick(View v) {
                                            txt.callOnClick();
                                        }

                                        @Override
                                        public void updateDrawState(TextPaint ds) {
                                            super.updateDrawState(ds);
                                            ds.setColor(getResources().getColor(R.color.white));
                                            ds.setUnderlineText(false);
                                            ds.setTextSize(getResources().getDimension(R.dimen.description_size));
                                        }

                                    }, 0, mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(count_radio).length(), 0);

                                    ssb.setSpan(new ClickableSpan() {

                                        @Override
                                        public void onClick(View v) {
                                            txt.callOnClick();
                                        }

                                        @Override
                                        public void updateDrawState(TextPaint ds) {
                                            super.updateDrawState(ds);
                                            ds.setColor(getResources().getColor(R.color.white));
                                            ds.setUnderlineText(false);
                                            ds.setTextSize(getResources().getDimension(R.dimen.lablesize1));
                                        }

                                    }, mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(count_radio).toString().length() + 2, text_radio[i][count_radio].getText().toString().length(), 0);
                                    // text_radio[i][count_radio].setMovementMethod(LinkMovementMethod.getInstance());
                                    text_radio[i][count_radio].setText(ssb);
                                    text_radio[i][count_radio].setGravity(Gravity.CENTER_VERTICAL);
                                    text_radio[i][count_radio].setTextColor(Color.WHITE);
                                    text_radio[i][count_radio].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.uncheck_white, 0, 0, 0);
                                    text_radio[i][count_radio].setCompoundDrawablePadding(25);
                                    text_radio[i][count_radio].setId(count_radio);
                                    text_radio[i][count_radio].setPadding(5, 5, 5, 5);
                                    text_radio[i][count_radio].setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));



                                    LinearLayout.LayoutParams childParamvote1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                                    childParamvote1.weight = 0.5f;
                                    text_radio[i][count_radio].setLayoutParams(childParamvote1);

                                    ll2.addView(text_radio[i][count_radio]);
                                    final int finalI1 = i;
                                    final int finalJ = count_radio;

                                    text_radio[i][count_radio].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Log.e("textradio value", "" + text_radio[finalI][finalJ].getId());
                                            typeoption = text_radio[finalI1][finalJ].getText().toString().split("\\(")[0].trim();

                                            radio_flag++;
                                            for (int k = 0; k < mVoteOfQuestionsArrayList.get(finalI1).getVoteoptions().size(); k++) {
                                                Log.e("textradio value", "" + text_radio[finalI1][k].getId());

                                                if (finalJ == text_radio[finalI1][k].getId()) {
                                                    //   text_radio[finalI][k].setTextColor(Color.RED);
                                                    text_radio[finalI1][k].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_white, 0, 0, 0);

                                                } else {
                                                    //     text_radio[finalI][k].setTextColor(Color.WHITE);
                                                    text_radio[finalI1][k].setCompoundDrawablesWithIntrinsicBounds(R.mipmap.uncheck_white, 0, 0, 0);

                                                }
                                            }

                                            // text_radio[finalI][finalJ].setId(finalJ);
                                        }
                                    });


                                    count_radio++;
                                }
                                rl.addView(ll2);
                            }


                        }


                    }


                    txt1[i] = new TextView(this);
                    LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    childParam2.setMargins(150, 0, 150, 0);
                    txt1[i].setLayoutParams(childParam2);
                    txt1[i].setTypeface(FontCustom.setFontBold(DetailOfVideoImageActivity.this));
                    txt1[i].setTextColor(Color.WHITE);
                    txt1[i].setTextSize(16);
                    txt1[i].setText("SUBMIT");
                    txt1[i].setGravity(Gravity.CENTER);
                    txt1[i].setPadding(5, 5, 5, 5);
                    txt.setPadding(0, 5, 0, 5);
                    txt1[i].setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));

                    txt.setText(mVoteOfQuestionsArrayList.get(i).getQuestiontitle());
                    txt1[i].setBackgroundColor(getResources().getColor(R.color.dark_purple));
                    txt1[i].setClickable(true);

                } else if (mIntent.getStringExtra("votable").equals("0"))
                {
                    for (int j = 0; j < mVoteOfQuestionsArrayList.get(i).getVoteoptions().size(); j++) {
                        LinearLayout ll_vote = new LinearLayout(DetailOfVideoImageActivity.this);
                        ll_vote.setOrientation(LinearLayout.HORIZONTAL);
                        ll_vote.setWeightSum(1f);

                        LinearLayout.LayoutParams layoutForInner = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        ll_vote.setLayoutParams(layoutForInner);

                        txt_vote[i] = new TextView(this);
                        txt_vote[i].setTypeface(FontCustom.setFontBold(DetailOfVideoImageActivity.this));
                        txt_vote[i].setTextColor(Color.WHITE);
                        txt_vote[i].setTextSize(16);
                        txt_vote[i].setText(mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(j));
                        txt_vote[i].setPadding(35, 5, 0, 5);
                        LinearLayout.LayoutParams childParamvote1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                        childParamvote1.weight = 0.5f;

                        txt_vote[i].setLayoutParams(childParamvote1);
                        ll_vote.addView(txt_vote[i]);

                        txt_vote1[i] = new TextView(this);
                        txt_vote1[i].setTypeface(FontCustom.setFontBold(DetailOfVideoImageActivity.this));
                        txt_vote1[i].setTextColor(Color.WHITE);
                        txt_vote1[i].setTextSize(16);
                        txt_vote1[i].setText(mVoteOfQuestionsArrayList.get(i).getOptionCount().get(j));
                        txt_vote1[i].setPadding(0, 5, 15, 5);
                        LinearLayout.LayoutParams childParamvote2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                        childParamvote2.weight = 0.5f;
                        txt_vote1[i].setLayoutParams(childParamvote2);
                        ll_vote.addView(txt_vote1[i]);
                        rl.addView(ll_vote);
                    }
                }
                rl.setPadding(0, 50, 0, 50);
                add_chart(i);

            } else if (mVoteOfQuestionsArrayList.get(i).getVotetype().equals("checkbox")) {

                int count=0;
                LinearLayout ll2 = null;

                if(mVoteOfQuestionsArrayList.get(i).getVoteoptions().size()%2==0)
                {
                    int count_checkbox = 0;
                    for(int j=0;j<mVoteOfQuestionsArrayList.get(i).getVoteoptions().size()/2;j++)
                    {
                        LinearLayout ll = new LinearLayout(this);
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.setPadding(30, 0, 30, 0);

                        for(int k=0;k<2;k++)
                        {
                            ch[i][j][k] = new CheckBox(this);
                            ch_value[i][j][k]=mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(count_checkbox);
                            ch[i][j][k].setText(" "+mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(count_checkbox)+ "  (" + mVoteOfQuestionsArrayList.get(i).getOptionCount().get(count_checkbox).toString() + ")");
                            ch[i][j][k].setTextColor(Color.WHITE);

                            ch[i][j][k].setPadding(5, 5, 5, 5);
                            ch[i][j][k].setId(j);
                            ch[i][j][k].setButtonDrawable(R.drawable.checkbox_selected);
                            //   ch[i][j].setTextSize(getResources().getDimension(R.dimen.description_size));
                            LinearLayout.LayoutParams childParamvote1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                            childParamvote1.weight = 0.5f;
                            ch[i][j][k].setLayoutParams(childParamvote1);
                            ch[i][j][k].setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));


                            ll.addView(ch[i][j][k]);
                            count_checkbox++;
                        }
                        rl.addView(ll);

                    }


                    Log.v("rinal_addchart","-------------------");


                }else
                {
                    int count_checkbox = 0;
                    if(mVoteOfQuestionsArrayList.get(i).getVoteoptions().size()%2!=0)
                    {
                        for(int j=(mVoteOfQuestionsArrayList.get(i).getVoteoptions().size()/2)+1;j>0;j--)
                        {
                            LinearLayout ll = new LinearLayout(this);
                            ll.setOrientation(LinearLayout.HORIZONTAL);
                            ll.setPadding(30, 0, 30, 0);

                            if(count_checkbox==mVoteOfQuestionsArrayList.get(i).getVoteoptions().size()-1)
                            {
                                for(int k=0;k<1;k++)
                                {
                                    Log.e("ch", "*" + count_checkbox);

                                    ch[i][j][k] = new CheckBox(this);
                                    ch_value[i][j][k]=mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(count_checkbox);
                                    ch[i][j][k].setText(" "+mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(count_checkbox)+ "  (" + mVoteOfQuestionsArrayList.get(i).getOptionCount().get(count_checkbox).toString() + ")");
                                    ch[i][j][k].setTextColor(Color.WHITE);

                                    ch[i][j][k].setPadding(5, 5, 5, 5);
                                    ch[i][j][k].setId(j);
                                    ch[i][j][k].setButtonDrawable(R.drawable.checkbox_selected);
                                    //   ch[i][j].setTextSize(getResources().getDimension(R.dimen.description_size));
                                    LinearLayout.LayoutParams childParamvote1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                                    childParamvote1.weight = 0.5f;
                                    ch[i][j][k].setLayoutParams(childParamvote1);
                                    ch[i][j][k].setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));


                                    ll.addView(ch[i][j][k]);
                                    count_checkbox++;

                                }
                            }else
                            {
                                for(int k=0;k<2;k++)
                                {

                                    Log.e("rinal_24","*"+count_checkbox);
                                    ch[i][j][k] = new CheckBox(this);
                                    ch_value[i][j][k]=mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(count_checkbox);
                                    ch[i][j][k].setText(" "+mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(count_checkbox)+ "  (" + mVoteOfQuestionsArrayList.get(i).getOptionCount().get(count_checkbox).toString() + ")");
                                    ch[i][j][k].setTextColor(Color.WHITE);
                                    ch[i][j][k].setId(j);
                                    ch[i][j][k].setPadding(5, 5, 5, 5);
                                    ch[i][j][k].setButtonDrawable(R.drawable.checkbox_selected);
                                    ch[i][j][k].setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));

                                    //   ch[i][j].setTextSize(getResources().getDimension(R.dimen.description_size));
                                    LinearLayout.LayoutParams childParamvote1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                                    childParamvote1.weight = 0.5f;
                                    ch[i][j][k].setLayoutParams(childParamvote1);

                                    ll.addView(ch[i][j][k]);
                                    count_checkbox++;
                                }

                            }
                            rl.addView(ll);
                        }

                    }else
                    {

                        Log.e("rinal_24","*************else"+count_checkbox);
                        for(int j=(mVoteOfQuestionsArrayList.get(i).getVoteoptions().size()/2)+1;j>0;j--)
                        {
                            LinearLayout ll = new LinearLayout(this);
                            ll.setOrientation(LinearLayout.HORIZONTAL);
                            ll.setPadding(30, 0, 30, 0);


                            for(int k=0;k<2;k++)
                            {

                                Log.e("ch","*"+count_checkbox);
                                ch[i][j][k] = new CheckBox(this);
                                ch_value[i][j][k]=mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(count_checkbox);
                                ch[i][j][k].setText(" "+mVoteOfQuestionsArrayList.get(i).getVoteoptions().get(count_checkbox)+ "  (" + mVoteOfQuestionsArrayList.get(i).getOptionCount().get(count_checkbox).toString() + ")");
                                ch[i][j][k].setTextColor(Color.WHITE);
                                ch[i][j][k].setId(j);
                                ch[i][j][k].setPadding(5, 5, 5, 5);
                                ch[i][j][k].setButtonDrawable(R.drawable.checkbox_selected);
                                ch[i][j][k].setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));

                                //   ch[i][j].setTextSize(getResources().getDimension(R.dimen.description_size));
                                LinearLayout.LayoutParams childParamvote1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                                childParamvote1.weight = 0.5f;
                                ch[i][j][k].setLayoutParams(childParamvote1);

                                ll.addView(ch[i][j][k]);
                                count_checkbox++;
                            }

                            rl.addView(ll);
                        }
                    }
                }

                rl.setPadding(0, 50, 0, 50);
                Log.e("chart_new ", "" + i);
                add_chart(i);

            }



            final int count=i;
        }
    }


    public void add_chart(final int i) {

        final int k = i;
        Log.e("chart  ", "" + k);
        TextView txt2 = new TextView(this);
        txt2.setTypeface(FontCustom.setFontBold(DetailOfVideoImageActivity.this));
        txt2.setTextColor(Color.parseColor("#6D2A85"));
        txt2.setTextSize(16);
        // txt1.setText("Audience Poll display in following chart:");
        //rl.addView(txt1);
        // rl.setPadding(10, 50, 10, 50);

        LinearLayout layout_chart = new LinearLayout(this);
        layout_chart.setOrientation(LinearLayout.HORIZONTAL);
        layout_chart.setWeightSum(1.0f);
        layout_chart.setPadding(0, 10, 0, 30);



        //layout_chart.setBackgroundColor(Color.parseColor("#6D2A85"));
        layout_chart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Log.e("mVoteOfQuestions....", "" + mVoteOfQuestionsArrayList.get(k).getVoteoptions());
        if(mVoteOfQuestionsArrayList.get(i).getVotetype().equals("text"))
        {
            img_pie[i] = new ImageView(this);
            img_pie[i].setVisibility(View.INVISIBLE);
            img_pie[i].setImageResource(R.mipmap.pie_graph2);
             img_pie[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));

         //   img_pie[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f));
            // img_pie[i].setOnClickListener(onclicklistener);

            img_pie[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pref.setValue(DetailOfVideoImageActivity.this, "question", mVoteOfQuestionsArrayList.get(k).getQuestiontitle());
                    Chart_Option[] chart_options = new Chart_Option[mVoteOfQuestionsArrayList.get(k).getVoteoptions().size()];
                    chart_optionArrayList.clear();
                    for (int count = 0; count < mVoteOfQuestionsArrayList.get(k).getVoteoptions().size(); count++) {
                        if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString() != null) {
                            if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString().length() > 0) {
                                chart_options[count] = new Chart_Option();
                                chart_options[count].setOption_title(mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString());
                                chart_options[count].setOption_value(mVoteOfQuestionsArrayList.get(k).getOptionCount().get(count).toString());
                                chart_optionArrayList.add(chart_options[count]);
                            }
                        }
                    }
                    Pref.setValue(DetailOfVideoImageActivity.this, "question", mVoteOfQuestionsArrayList.get(k).getQuestiontitle());
                    Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
//                Log.e("chart_optionArrayList", "" + chart_optionArrayList.get(1).getOption_value());
                    intent.putExtra("chart_optionArrayList", chart_optionArrayList);
                    startActivity(intent);
                }
            });

            layout_chart.addView(img_pie[i]);

            img_bar[i] = new ImageView(this);
            img_bar[i].setVisibility(View.INVISIBLE);
            img_bar[i].setImageResource(R.mipmap.bar_graph2);
              img_bar[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));
          img_bar[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pref.setValue(DetailOfVideoImageActivity.this, "question", mVoteOfQuestionsArrayList.get(k).getQuestiontitle());
                    Chart_Option[] chart_options = new Chart_Option[mVoteOfQuestionsArrayList.get(k).getVoteoptions().size()];
                    chart_optionArrayList.clear();
                    for (int count = 0; count < mVoteOfQuestionsArrayList.get(k).getVoteoptions().size(); count++) {
                        if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString() != null) {
                            if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString().length() > 0) {
                                chart_options[count] = new Chart_Option();
                                chart_options[count].setOption_title(mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString());
                                chart_options[count].setOption_value(mVoteOfQuestionsArrayList.get(k).getOptionCount().get(count).toString());
                                chart_optionArrayList.add(chart_options[count]);
                            }
                        }
                    }


                    Intent intent = new Intent(getApplicationContext(), BarchartActivity.class);
                    intent.putExtra("chart_optionArrayList", chart_optionArrayList);
                    startActivity(intent);
                }
            });

            layout_chart.addView(img_bar[i]);

            txt3[i] = new TextView(this);
            txt3[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 50, 0.4f));
            txt3[i].setVisibility(View.INVISIBLE);
            layout_chart.addView(txt3[i]);
            txt1[i] = new TextView(this);
            txt1[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
       /* LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        txt1[i].setLayoutParams(childParam2);*/
            txt1[i].setTypeface(FontCustom.setFontBold(DetailOfVideoImageActivity.this));
            txt1[i].setTextColor(Color.WHITE);
            txt1[i].setTextSize(16);
            txt1[i].setText("SUBMIT");
            txt1[i].setVisibility(View.VISIBLE);
            txt1[i].setGravity(Gravity.CENTER);
            txt1[i].setPadding(5, 5, 5, 5);
            txt.setPadding(0, 5, 0, 5);
            txt.setText(mVoteOfQuestionsArrayList.get(i).getQuestiontitle());
            txt1[i].setBackgroundColor(getResources().getColor(R.color.dark_purple));
            txt1[i].setClickable(true);
            txt1[i].setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));
            txt1[i].setVisibility(View.GONE);
            layout_chart.addView(txt1[i]);
            rl.addView(layout_chart);


        }else
        {
            if(mVoteOfQuestionsArrayList.get(i).getVotetype().equals("radio"))
            {
                if(mIntent.getStringExtra("votable").equals("0"))
                {


                    img_pie[i] = new ImageView(this);
                  img_pie[i].setPadding(3,3,3,3);
                    img_pie[i].setImageResource(R.mipmap.pie_graph2);
                     img_pie[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));

                    //img_pie[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f));
                    // img_pie[i].setOnClickListener(onclicklistener);

                    img_pie[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pref.setValue(DetailOfVideoImageActivity.this, "question", mVoteOfQuestionsArrayList.get(k).getQuestiontitle());
                            Chart_Option[] chart_options = new Chart_Option[mVoteOfQuestionsArrayList.get(k).getVoteoptions().size()];
                            chart_optionArrayList.clear();
                            for (int count = 0; count < mVoteOfQuestionsArrayList.get(k).getVoteoptions().size(); count++) {
                                if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString() != null) {
                                    if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString().length() > 0) {
                                        chart_options[count] = new Chart_Option();
                                        chart_options[count].setOption_title(mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString());
                                        chart_options[count].setOption_value(mVoteOfQuestionsArrayList.get(k).getOptionCount().get(count).toString());
                                        chart_optionArrayList.add(chart_options[count]);
                                    }
                                }
                            }
                            Pref.setValue(DetailOfVideoImageActivity.this, "question", mVoteOfQuestionsArrayList.get(k).getQuestiontitle());
                            Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
//                Log.e("chart_optionArrayList", "" + chart_optionArrayList.get(1).getOption_value());
                            intent.putExtra("chart_optionArrayList", chart_optionArrayList);
                            startActivity(intent);
                        }
                    });

                    layout_chart.addView(img_pie[i]);

                    img_bar[i] = new ImageView(this);
                 img_bar[i].setPadding(3,3,3,3);
                    img_bar[i].setImageResource(R.mipmap.bar_graph2);
                      img_bar[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));

                    //    img_bar[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f));
                    img_bar[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pref.setValue(DetailOfVideoImageActivity.this, "question", mVoteOfQuestionsArrayList.get(k).getQuestiontitle());
                            Chart_Option[] chart_options = new Chart_Option[mVoteOfQuestionsArrayList.get(k).getVoteoptions().size()];
                            chart_optionArrayList.clear();
                            for (int count = 0; count < mVoteOfQuestionsArrayList.get(k).getVoteoptions().size(); count++) {
                                if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString() != null) {
                                    if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString().length() > 0) {
                                        chart_options[count] = new Chart_Option();
                                        chart_options[count].setOption_title(mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString());
                                        chart_options[count].setOption_value(mVoteOfQuestionsArrayList.get(k).getOptionCount().get(count).toString());
                                        chart_optionArrayList.add(chart_options[count]);
                                    }
                                }
                            }


                            Intent intent = new Intent(getApplicationContext(), BarchartActivity.class);
                            intent.putExtra("chart_optionArrayList", chart_optionArrayList);
                            startActivity(intent);
                        }
                    });

                    layout_chart.addView(img_bar[i]);
                    txt3[i] = new TextView(this);
                    txt3[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f));
                    txt3[i].setVisibility(View.INVISIBLE);
                    layout_chart.addView(txt3[i]);
                    txt1[i] = new TextView(this);
                    txt1[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
       /* LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        txt1[i].setLayoutParams(childParam2);*/
                    txt1[i].setTypeface(FontCustom.setFontBold(DetailOfVideoImageActivity.this));
                    txt1[i].setTextColor(Color.WHITE);
                    txt1[i].setVisibility(View.INVISIBLE);
                    txt1[i].setTextSize(16);
                    txt1[i].setText("SUBMIT");
                    txt1[i].setGravity(Gravity.CENTER);
                    txt1[i].setPadding(5, 5, 5, 5);
                    txt.setPadding(0, 5, 0, 5);

                    txt.setText(mVoteOfQuestionsArrayList.get(i).getQuestiontitle());
                    txt1[i].setBackgroundColor(getResources().getColor(R.color.dark_purple));
                    txt1[i].setClickable(true);
                    txt1[i].setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));

                    layout_chart.addView(txt1[i]);
                    rl.addView(layout_chart);



                }else if(mIntent.getStringExtra("votable").equals("1")){

                    img_pie[i] = new ImageView(this);
                  img_pie[i].setPadding(3,3,3,3);

                    img_pie[i].setImageResource(R.mipmap.pie_graph2);
                     img_pie[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));

                    // img_pie[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f));
                    // img_pie[i].setOnClickListener(onclicklistener);

                    img_pie[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pref.setValue(DetailOfVideoImageActivity.this, "question", mVoteOfQuestionsArrayList.get(k).getQuestiontitle());
                            Chart_Option[] chart_options = new Chart_Option[mVoteOfQuestionsArrayList.get(k).getVoteoptions().size()];
                            chart_optionArrayList.clear();
                            for (int count = 0; count < mVoteOfQuestionsArrayList.get(k).getVoteoptions().size(); count++) {
                                if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString() != null) {
                                    if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString().length() > 0) {
                                        chart_options[count] = new Chart_Option();
                                        chart_options[count].setOption_title(mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString());
                                        chart_options[count].setOption_value(mVoteOfQuestionsArrayList.get(k).getOptionCount().get(count).toString());
                                        chart_optionArrayList.add(chart_options[count]);
                                    }
                                }
                            }
                            Pref.setValue(DetailOfVideoImageActivity.this, "question", mVoteOfQuestionsArrayList.get(k).getQuestiontitle());
                            Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
//                Log.e("chart_optionArrayList", "" + chart_optionArrayList.get(1).getOption_value());
                            intent.putExtra("chart_optionArrayList", chart_optionArrayList);
                            startActivity(intent);
                        }
                    });

                    layout_chart.addView(img_pie[i]);
                    img_bar[i] = new ImageView(this);
                    img_bar[i].setImageResource(R.mipmap.bar_graph2);
                 img_bar[i].setPadding(3,3,3,3);
                      img_bar[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));

                    // img_bar[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f));
                    img_bar[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pref.setValue(DetailOfVideoImageActivity.this, "question", mVoteOfQuestionsArrayList.get(k).getQuestiontitle());
                            Chart_Option[] chart_options = new Chart_Option[mVoteOfQuestionsArrayList.get(k).getVoteoptions().size()];
                            chart_optionArrayList.clear();
                            for (int count = 0; count < mVoteOfQuestionsArrayList.get(k).getVoteoptions().size(); count++) {
                                if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString() != null) {
                                    if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString().length() > 0) {
                                        chart_options[count] = new Chart_Option();
                                        chart_options[count].setOption_title(mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString());
                                        chart_options[count].setOption_value(mVoteOfQuestionsArrayList.get(k).getOptionCount().get(count).toString());
                                        chart_optionArrayList.add(chart_options[count]);
                                    }
                                }
                            }


                            Intent intent = new Intent(getApplicationContext(), BarchartActivity.class);
                            intent.putExtra("chart_optionArrayList", chart_optionArrayList);
                            startActivity(intent);
                        }
                    });

                    layout_chart.addView(img_bar[i]);

                    txt3[i] = new TextView(this);
                    txt3[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f));
                    txt3[i].setVisibility(View.INVISIBLE);
                    layout_chart.addView(txt3[i]);
                    txt1[i] = new TextView(this);
                    txt1[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
       /* LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        txt1[i].setLayoutParams(childParam2);*/
                    txt1[i].setTypeface(FontCustom.setFontBold(DetailOfVideoImageActivity.this));
                    txt1[i].setTextColor(Color.WHITE);
                    txt1[i].setTextSize(16);
                    txt1[i].setText("SUBMIT");
                    txt1[i].setVisibility(View.VISIBLE);
                    txt1[i].setGravity(Gravity.CENTER);
                    txt1[i].setPadding(5, 5, 5, 5);
                    txt.setPadding(0, 5, 0, 5);
                    txt.setText(mVoteOfQuestionsArrayList.get(i).getQuestiontitle());
                    txt1[i].setBackgroundColor(getResources().getColor(R.color.dark_purple));
                    txt1[i].setClickable(true);
                    txt1[i].setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));

                    layout_chart.addView(txt1[i]);
                    rl.addView(layout_chart);

                }

            }else if(mVoteOfQuestionsArrayList.get(i).getVotetype().equals("checkbox"))
            {

                Log.v("checkbox","inside");

                img_pie[i] = new ImageView(this);
                img_pie[i].setImageResource(R.mipmap.pie_graph2);
              img_pie[i].setPadding(3,3,3,3);

                final float inPixels= getResources().getDimension(R.dimen.dimen_entry_in_dp);
                final float scale = getResources().getDisplayMetrics().density;
                int pixels = (int) (40 * scale + 0.5f);

              //   img_pie[i].setLayoutParams(new LinearLayout.LayoutParams(pixels, pixels, 0.1f));

                 img_pie[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));
                // img_pie[i].setOnClickListener(onclicklistener);

                img_pie[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Pref.setValue(DetailOfVideoImageActivity.this, "question", mVoteOfQuestionsArrayList.get(k).getQuestiontitle());
                        Chart_Option[] chart_options = new Chart_Option[mVoteOfQuestionsArrayList.get(k).getVoteoptions().size()];
                        chart_optionArrayList.clear();
                        for (int count = 0; count < mVoteOfQuestionsArrayList.get(k).getVoteoptions().size(); count++) {
                            if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString() != null) {
                                if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString().length() > 0) {
                                    chart_options[count] = new Chart_Option();
                                    chart_options[count].setOption_title(mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString());
                                    chart_options[count].setOption_value(mVoteOfQuestionsArrayList.get(k).getOptionCount().get(count).toString());
                                    chart_optionArrayList.add(chart_options[count]);
                                }
                            }
                        }
                        Pref.setValue(DetailOfVideoImageActivity.this, "question", mVoteOfQuestionsArrayList.get(k).getQuestiontitle());
                        Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
//                Log.e("chart_optionArrayList", "" + chart_optionArrayList.get(1).getOption_value());
                        intent.putExtra("chart_optionArrayList", chart_optionArrayList);
                        startActivity(intent);
                    }
                });

                layout_chart.addView(img_pie[i]);
                img_bar[i] = new ImageView(this);
                img_bar[i].setImageResource(R.mipmap.bar_graph2);
             img_bar[i].setPadding(3,3,3,3);
                img_bar[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));

               // img_bar[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f));
                img_bar[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Pref.setValue(DetailOfVideoImageActivity.this, "question", mVoteOfQuestionsArrayList.get(k).getQuestiontitle());
                        Chart_Option[] chart_options = new Chart_Option[mVoteOfQuestionsArrayList.get(k).getVoteoptions().size()];
                        chart_optionArrayList.clear();
                        for (int count = 0; count < mVoteOfQuestionsArrayList.get(k).getVoteoptions().size(); count++) {
                            if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString() != null) {
                                if (mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString().length() > 0) {
                                    chart_options[count] = new Chart_Option();
                                    chart_options[count].setOption_title(mVoteOfQuestionsArrayList.get(k).getVoteoptions().get(count).toString());
                                    chart_options[count].setOption_value(mVoteOfQuestionsArrayList.get(k).getOptionCount().get(count).toString());
                                    chart_optionArrayList.add(chart_options[count]);
                                }
                            }
                        }


                        Intent intent = new Intent(getApplicationContext(), BarchartActivity.class);
                        intent.putExtra("chart_optionArrayList", chart_optionArrayList);
                        startActivity(intent);
                    }
                });

                layout_chart.addView(img_bar[i]);

                txt3[i] = new TextView(this);
                txt3[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f));
                txt3[i].setVisibility(View.INVISIBLE);
                layout_chart.addView(txt3[i]);
                txt1[i] = new TextView(this);
                txt1[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
       /* LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        txt1[i].setLayoutParams(childParam2);*/
                txt1[i].setTypeface(FontCustom.setFontBold(DetailOfVideoImageActivity.this));
                txt1[i].setTextColor(Color.WHITE);
                txt1[i].setTextSize(16);
                txt1[i].setVisibility(View.VISIBLE);
                txt1[i].setText("SUBMIT");
                txt1[i].setGravity(Gravity.CENTER);
                txt1[i].setPadding(5, 5, 5, 5);
                txt.setPadding(0, 5, 0, 5);
                txt.setText(mVoteOfQuestionsArrayList.get(i).getQuestiontitle());
                txt1[i].setBackgroundColor(getResources().getColor(R.color.dark_purple));
                txt1[i].setClickable(true);
                txt1[i].setTypeface(FontCustom.setFont(DetailOfVideoImageActivity.this));

                layout_chart.addView(txt1[i]);
                rl.addView(layout_chart);

            }

        }



        txt1[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_view_create = 1;
                if (mVoteOfQuestionsArrayList.get(i).getVotetype().equals("radio")) {


                    Log.e("Details_articleid", "" + mIntent.getStringExtra("articleid"));
                    Log.e("Details_queid", "" + mVoteOfQuestionsArrayList.get(i).getQuestId());

                    if (Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_USER_ID, "").equals("")) {

                        Utils.loginalert(DetailOfVideoImageActivity.this);
                        //  Utils.showAlert(context, context.getResources().getString(R.string.dialog_title), context.getResources().getString(R.string.dialog_msg));
                    } else {
                        int count1 = 0;
                        for (int j = 0; j < mVoteOfQuestionsArrayList.get(i).getVoteoptions().size(); j++) {
                            if (radio_flag > 0) {
                                count1++;

                            }

                        }

                        for (int temp_answer = 0; temp_answer < SplashActivity.answer_status_list.size(); temp_answer++) {

                            if (SplashActivity.answer_status_list.get(temp_answer).getArticle_ida().equalsIgnoreCase(mIntent.getStringExtra("articleid"))) {
                                if (SplashActivity.answer_status_list.get(temp_answer).getQuestion_ida().equalsIgnoreCase(mVoteOfQuestionsArrayList.get(i).getQuestId())) {
                                    if (SplashActivity.answer_status_list.get(temp_answer).getAnswer_flaga().equalsIgnoreCase("1")) {

                                        Log.e("question_id_adapter", "" + mVoteOfQuestionsArrayList.get(i).getQuestId() + "-------------------stop");
                                        Utils.showAlert(DetailOfVideoImageActivity.this, "You already selected an answer for this question.");
                                        break;
                                    } else {
                                        Log.e("question_id_adapter", "execute--------------------------------");
                                        if (count1 == 0) {
                                            Utils.showAlert(DetailOfVideoImageActivity.this, "Please mention your vote");
                                            break;
                                        } else {
                                            callanswermethod_radio(i);
                                            break;
                                        }

                                    }
                                }
                            }
                        }
                    }
                } else if (mVoteOfQuestionsArrayList.get(i).getVotetype().equals("text")) {

                    int flag_que = 0;
                    Log.e("Details_articleid", "" + mIntent.getStringExtra("articleid"));
                    Log.e("Details_queid", "" + mVoteOfQuestionsArrayList.get(i).getQuestId());
                    typeoption = txtview[i].getText().toString();

                    if (Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_USER_ID, "").equals("")) {

                        Utils.loginalert(DetailOfVideoImageActivity.this);
                        //  Utils.showAlert(context, context.getResources().getString(R.string.dialog_title), context.getResources().getString(R.string.dialog_msg));
                    } else {


                        for (int temp_answer = 0; temp_answer < SplashActivity.answer_status_list.size(); temp_answer++) {

                            if (SplashActivity.answer_status_list.get(temp_answer).getArticle_ida().equalsIgnoreCase(mIntent.getStringExtra("articleid"))) {
                                if (SplashActivity.answer_status_list.get(temp_answer).getQuestion_ida().equalsIgnoreCase(mVoteOfQuestionsArrayList.get(i).getQuestId())) {

                                    if (SplashActivity.answer_status_list.get(temp_answer).getAnswer_flaga().equalsIgnoreCase("1")) {

                                        Log.e("question_id_adapter", "" + mVoteOfQuestionsArrayList.get(i).getQuestId() + "-------------------stop");
                                        Utils.showAlert(DetailOfVideoImageActivity.this, "You already selected an answer for this question.");
                                        break;

                                    } else {
                                        Log.e("question_id_adapter", "execute--------------------------------");
                                        if (typeoption.length() == 0) {
                                            Utils.showAlert(DetailOfVideoImageActivity.this, "Please mention your vote");
                                            break;
                                        } else {
                                            callanswermethod_text(i);
                                            break;
                                        }

                                    }
                                }
                            }
                        }
                    }
                } else if (mVoteOfQuestionsArrayList.get(i).getVotetype().equals("checkbox")) {

                    int flag_que = 0;
                    String strtext = "";

                    Log.e("valueofcheckij", "" + i + "-----" + mVoteOfQuestionsArrayList.get(i).getVoteoptions().size() + "------");
                    int count_checkbox = 0;
                    if (mVoteOfQuestionsArrayList.get(i).getVoteoptions().size() % 2 != 0) {
                        for (int j = (mVoteOfQuestionsArrayList.get(i).getVoteoptions().size() / 2) + 1; j > 0; j--) {

                            if (count_checkbox == mVoteOfQuestionsArrayList.get(i).getVoteoptions().size() - 1) {
                                for (int k = 0; k < 1; k++) {
                                    Log.e("ch", "*" + ch[i][j][k].getText().toString());
                                    if (ch[i][j][k].isChecked()) {
                                        flag_que++;


                                      //  strtext = strtext + "\"" + ch[i][j][k].getText().toString() + "\"" + ",";
                                        strtext = strtext + "\"" + ch_value[i][j][k]+"\"" + ",";

                                        strnew = "["+strtext.substring(0, strtext.length() - 1)+"]";

                                        typeoption = strnew;
                                    }
                                    count_checkbox++;

                                }
                            } else {
                                for (int k = 0; k < 2; k++) {

                                    Log.e("ch", "*" + ch[i][j][k].getText().toString());
                                    if (ch[i][j][k].isChecked()) {
                                        flag_que++;
                                        //strtext = strtext + "\"" + ch[i][j][k].getText().toString() + "\"" + ",";
                                        strtext = strtext + "\"" + ch_value[i][j][k]+"\"" + ",";
                                        String strnew = "["+strtext.substring(0, strtext.length() - 1)+"]";

                                        typeoption = strnew;
                                    }
                                    count_checkbox++;
                                }

                            }

                        }

                    } else {

                        for (int j = 0 ; j <(mVoteOfQuestionsArrayList.get(i).getVoteoptions().size() / 2); j++) {
                            for (int k = 0; k < 2; k++) {

                                Log.e("ch", "*" + ch[i][j][k].getText().toString());
                                if (ch[i][j][k].isChecked()) {
                                    flag_que++;
                                   // strtext = strtext + "\"" + ch[i][j][k].getText().toString() + "\"" + ",";
                                    strtext = strtext + "\"" + ch_value[i][j][k]+"\"" + ",";
                                    String strnew = "["+strtext.substring(0, strtext.length() - 1)+"]";

                                    typeoption = strnew;
                                }
                                count_checkbox++;
                            }
                        }
                    }


                    if (Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_USER_ID, "").equals("")) {

                        Utils.loginalert(DetailOfVideoImageActivity.this);
                        //  Utils.showAlert(context, context.getResources().getString(R.string.dialog_title), context.getResources().getString(R.string.dialog_msg));
                    } else {

                        for (int temp_answer = 0; temp_answer < SplashActivity.answer_status_list.size(); temp_answer++) {

                            if (SplashActivity.answer_status_list.get(temp_answer).getArticle_ida().equalsIgnoreCase(mIntent.getStringExtra("articleid"))) {
                                if (SplashActivity.answer_status_list.get(temp_answer).getQuestion_ida().equalsIgnoreCase(mVoteOfQuestionsArrayList.get(i).getQuestId())) {

                                    if (SplashActivity.answer_status_list.get(temp_answer).getAnswer_flaga().equalsIgnoreCase("1")) {

                                        Log.e("question_id_adapter", "" + mVoteOfQuestionsArrayList.get(i).getQuestId() + "-------------------stop");
                                        Utils.showAlert(DetailOfVideoImageActivity.this, "You already selected an answer for this question.");
                                        break;
                                    } else {
                                        Log.e("question_id_adapter", "execute--------------------------------");
                                        if (flag_que > 0) {
                                            callanswermethod_checkbox(i);
                                            break;
                                        } else {
                                            Utils.showAlert(DetailOfVideoImageActivity.this, "Please mention your vote");
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });


        // rl.setPadding(0, 50, 0, 50);
    }

    public void callanswermethod_radio(int i){
        JSONArray jsonArray = new JSONArray();

        // for (int i = 0; i < mVoteOfQuestionsArrayList.size(); i++) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("questionid", mVoteOfQuestionsArrayList.get(i).getQuestId());
            jsonObject.put("type", mVoteOfQuestionsArrayList.get(i).getVotetype());
            jsonObject.put("articleId", mIntent.getStringExtra("articleid"));
            jsonObject.put("authkey", Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_AUTHKAY, ""));
            authkey = Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_AUTHKAY, "");
            articleId = Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_AUTHKAY, "");
            type = mVoteOfQuestionsArrayList.get(i).getVotetype();
            questionid = mVoteOfQuestionsArrayList.get(i).getQuestId();

            for (int j = 0; j < mVoteOfQuestionsArrayList.get(i).getVoteoptions().size(); j++) {
                if (radio_flag>0) {


                    JSONArray jsonArrayans = new JSONArray();
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("typeoption", typeoption);
                    jsonArrayans.put(jsonObject1);
                    jsonObject.put("answer", jsonArrayans);

                    jsonObject.put("typeoption", typeoption);
                    jsonArray.put(jsonObject);
                    // typeoption = text_radio[i][j].getText().toString().trim();

                }

            }
            callsetVoteApi();
            AnswerStatus[] answerStatus=new AnswerStatus[1];
            answerStatus[0]=new AnswerStatus();
            answerStatus[0].setArticle_ida(mIntent.getStringExtra("articleid"));
            answerStatus[0].setQuestion_ida(mVoteOfQuestionsArrayList.get(i).getQuestId());
            answerStatus[0].setAnswer_flaga("1");
            SplashActivity.answer_status_list.add(answerStatus[0]);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void callanswermethod_checkbox(int i){
        String strtext = "";
        JSONArray jsonArray = new JSONArray();

        // for (int i = 0; i < mVoteOfQuestionsArrayList.size(); i++) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("questionid", mVoteOfQuestionsArrayList.get(i).getQuestId());
            jsonObject.put("type", mVoteOfQuestionsArrayList.get(i).getVotetype());
            jsonObject.put("articleId", mIntent.getStringExtra("articleid"));
            jsonObject.put("authkey", Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_AUTHKAY, ""));
            authkey = Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_AUTHKAY, "");
            articleId = Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_AUTHKAY, "");
            type = mVoteOfQuestionsArrayList.get(i).getVotetype();
            questionid = mVoteOfQuestionsArrayList.get(i).getQuestId();




            JSONArray jsonArrayans = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            //jsonObject1.put("typeoption", strnew);
            //jsonArrayans.put(jsonObject1);
            //jsonObject.put("answer", jsonArrayans);

            Log.v("strnew",strnew+"");

            jsonObject.put("typeoption", strnew);

            jsonArray.put(jsonObject);
            callsetVoteApi();
            AnswerStatus[] answerStatus=new AnswerStatus[1];
            answerStatus[0]=new AnswerStatus();
            answerStatus[0].setArticle_ida(mIntent.getStringExtra("articleid"));
            answerStatus[0].setQuestion_ida(mVoteOfQuestionsArrayList.get(i).getQuestId());
            answerStatus[0].setAnswer_flaga("1");
            SplashActivity.answer_status_list.add(answerStatus[0]);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void callanswermethod_text(int i)
    {

        JSONArray jsonArray = new JSONArray();

        // for (int i = 0; i < mVoteOfQuestionsArrayList.size(); i++) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("questionid", mVoteOfQuestionsArrayList.get(i).getQuestId());
            jsonObject.put("type", mVoteOfQuestionsArrayList.get(i).getVotetype());
            jsonObject.put("articleId", mIntent.getStringExtra("articleid"));
            jsonObject.put("authkey", Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_AUTHKAY, ""));
            authkey = Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_AUTHKAY, "");
            articleId = Pref.getValue(DetailOfVideoImageActivity.this, Constants.TAG_AUTHKAY, "");
            type = mVoteOfQuestionsArrayList.get(i).getVotetype();
            questionid = mVoteOfQuestionsArrayList.get(i).getQuestId();



            JSONArray jsonArrayans = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("typeoption",txtview[i].getText().toString());
            jsonArrayans.put(jsonObject1);
            jsonObject.put("answer", jsonArrayans);

            jsonObject.put("typeoption",txtview[i].getText().toString());

            typeoption = txtview[i].getText().toString();
            // callsetVoteApi();

            jsonArray.put(jsonObject);

            Log.v("msg", txtview[i].getText() + "--");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        callsetVoteApi();
        AnswerStatus[] answerStatus=new AnswerStatus[1];
        answerStatus[0]=new AnswerStatus();
        answerStatus[0].setArticle_ida(mIntent.getStringExtra("articleid"));
        answerStatus[0].setQuestion_ida(mVoteOfQuestionsArrayList.get(i).getQuestId());
        answerStatus[0].setAnswer_flaga("1");
        SplashActivity.answer_status_list.add(answerStatus[0]);

    }



    @Override
    protected void onResume() {
        super.onResume();

       // initComponents();


    }
   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen for landscape and portrait
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            webview.destroy();
            webview = (WebView)findViewById(R.id.webview);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

            webview.destroy();
            webview = (WebView)findViewById(R.id.webview);
        }
    }*/

    @Override
    public void onPause() {
        super.onPause();

      /*  Log.v("onPause","---------");

        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(webview, (Object[]) null);

        } catch(ClassNotFoundException cnfe) {

        } catch(NoSuchMethodException nsme) {

        } catch(InvocationTargetException ite) {

        } catch (IllegalAccessException iae) {
        }*/
    }

    class myWebChromeClient extends WebChromeClient {
        private Bitmap mDefaultVideoPoster;
        private View mVideoProgressView;

        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            onShowCustomView(view, callback);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onShowCustomView(View view,CustomViewCallback callback) {

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            webview.setVisibility(View.GONE);
            customViewContainer.setVisibility(View.VISIBLE);
            customViewContainer.addView(view);
            customViewCallback = callback;
        }

        @Override
        public View getVideoLoadingProgressView() {

            if (mVideoProgressView == null) {
                LayoutInflater inflater = LayoutInflater.from(DetailOfVideoImageActivity.this);
                mVideoProgressView = inflater.inflate(R.layout.video_progress, null);
            }
            return mVideoProgressView;
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();    //To change body of overridden methods use File | Settings | File Templates.
            if (mCustomView == null)
                return;

            webview.setVisibility(View.VISIBLE);
            customViewContainer.setVisibility(View.GONE);

            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            customViewContainer.removeView(mCustomView);
            customViewCallback.onCustomViewHidden();

            mCustomView = null;
        }
    }

    class myWebViewClient extends WebViewClient {
        String url1;
        public myWebViewClient(String url)

        {
            this.url1 = url;
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url1.contains("#openPopup")) {

                final Dialog listDialog = new Dialog(DetailOfVideoImageActivity.this);
                LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                listDialog.setContentView(R.layout.doc_file_image_layout);
                listDialog.setCanceledOnTouchOutside(true);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = listDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;
                lp.width = width - 50;
                lp.height = height - 200;
                window.setAttributes(lp);

                listDialog.getWindow().setGravity(Gravity.CENTER);
                listDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                title_img = (TextView) listDialog.findViewById(R.id.title);
                back = (ImageView) listDialog.findViewById(R.id.back);
                img_main = (ImageView) listDialog.findViewById(R.id.img_main);
                Picasso.with(DetailOfVideoImageActivity.this).load(mIntent.getStringExtra("image")).fit().into(img_main);
                title_img.setText(txt_title.getText().toString());


                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listDialog.dismiss();
                    }
                });
                listDialog.show();
            }

            return super.shouldOverrideUrlLoading(view, url);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }




}