package com.westwood24.connect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.model.AnswerStatus;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.FontCustom;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * Created by dharmesh on 3/8/16.
 */
public class SplashActivity extends Activity implements WebserviceCall.WebserviceResponse {

    // variable declaration...
    boolean isFinish = false;
    int _splashTime = 2000;


    //Object declaration...
    LinearLayout splash_layout;
    LinearLayout mMiddleLn;
    Handler mSplashHandler;
    Button mLoginWithPhoneBtn;
    Button mLoginWithEmailBtn;
    Button mRegisterWithPhoneBtn;
    Button mRegisterWithEmailBtn;
    Button mLoginBtn;
    Button mExploreGuestBtn;
    public static ArrayList<AnswerStatus> answer_status_list = new ArrayList<>();

    public static ArrayList<HashMap<String, String>> countrylist;
    public static ArrayList<HashMap<String, String>> statelist;
    public static ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> listnew;
    JSONArray dataInnerArray;
    //other declaration..
    String TAG = SplashActivity.class.getSimpleName();


    private Runnable mSplashRunnable = new Runnable() {
        @Override
        public void run() {
            isFinish = true;

        /*    if (Pref.getValue(SplashActivity.this, Constants.IS_LOGIN, "").equals("1") && !Pref.getValue(SplashActivity.this, Constants.IS_LOGIN, "").isEmpty()) {
                mMiddleLn.setVisibility(View.GONE);
                Pref.setValue(SplashActivity.this,"offset","0");
                startActivity(new Intent(SplashActivity.this, DashBoardActivity.class));
            } else {
                mMiddleLn.setVisibility(View.VISIBLE);
            }

            mLoginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pref.setValue(SplashActivity.this,"user_type","login");
                    Pref.setValue(SplashActivity.this,"offset","0");
                    startActivity(new Intent(SplashActivity.this,
                            LoginActivity.class).putExtra("loginwithphoneemail", "0"));
                }
            });

            mExploreGuestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            mRegisterWithPhoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pref.setValue(SplashActivity.this,"offset","0");
                    startActivity(new Intent(SplashActivity.this,
                            SignupActivity.class).putExtra("registerwithphoneemail", "0"));

                }
            });

            mRegisterWithEmailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pref.setValue(SplashActivity.this,"offset","0");
                    startActivity(new Intent(SplashActivity.this,
                            SignupActivity.class).putExtra("registerwithphoneemail", "1"));

                }
            });
            mLoginWithPhoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pref.setValue(SplashActivity.this,"offset","0");
                    startActivity(new Intent(SplashActivity.this,
                            LoginActivity.class).putExtra("loginwithphoneemail", "0"));

                }
            });

            mLoginWithEmailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pref.setValue(SplashActivity.this,"offset","0");
                    startActivity(new Intent(SplashActivity.this,
                            LoginActivity.class).putExtra("loginwithphoneemail", "1"));

                }
            });*/
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        initComponents();

        StartAnimations();
        callCountryApi();
       // callStateApi();
        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e(TAG, "deviceId " + deviceId);
        Pref.setValue(this, Constants.TAG_DEVICETOKEN, deviceId);
        Pref.setValue(SplashActivity.this, "offset", "0");

      //  Pref.setValue(SplashActivity.this, "user_type", "guest");

       // Pref.setValue(SplashActivity.this, Constants.TAG_USER_ID, "");
        startActivity(new Intent(SplashActivity.this, DashBoardActivity.class));


    }

    private void initComponents() {
        splash_layout = (LinearLayout) findViewById(R.id.splash_layout);
        mMiddleLn = (LinearLayout) findViewById(R.id.activity_middle_ln);
        mLoginWithPhoneBtn = (Button) findViewById(R.id.activity_splash_loginphone_btn);
        mLoginWithEmailBtn = (Button) findViewById(R.id.activity_splash_loginemail_btn);
        mRegisterWithPhoneBtn = (Button) findViewById(R.id.activity_splash_regphone_btn);
        mRegisterWithEmailBtn = (Button) findViewById(R.id.activity_splash_regemail_btn);
        mLoginBtn = (Button) findViewById(R.id.activity_splash_login_btn);
        mExploreGuestBtn = (Button) findViewById(R.id.activity_splash_explore_btn);

        mLoginBtn.setTypeface(FontCustom.setFont(this));
        mExploreGuestBtn.setTypeface(FontCustom.setFont(this));
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSplashHandler = new Handler();
        mSplashHandler.postDelayed(mSplashRunnable, _splashTime);
        Pref.setValue(SplashActivity.this,"profile_update","true");



    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isFinish) {
            if (mSplashRunnable != null) {
                if (mSplashHandler != null) {
                    mSplashHandler.removeCallbacks(mSplashRunnable);
                }
            }
        }

    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();

        splash_layout.clearAnimation();
        splash_layout.startAnimation(anim);

       /* anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);*/


    }

    public static void setTextWatcher(final Context context, final EditText editText, final TextView textView) {

        TextWatcher textWatcher = new TextWatcher() {
            boolean flag = false;

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                if (arg0.length() == 0) {
                    flag = true;
                } else {
                    flag = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {


                if (s.length() == 0) {
                    Animation slidedown = AnimationUtils.loadAnimation(context, R.anim.hint_slide_down);
                    textView.startAnimation(slidedown);
                    textView.setVisibility(View.GONE);
                } else {
                    if (s.length() == 1 && flag) {
                        Animation slideup = AnimationUtils.loadAnimation(context, R.anim.hint_slide_up);
                        textView.startAnimation(slideup);

                    }
                    editText.setError(null);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        };
        editText.addTextChangedListener(textWatcher);
    }


    /**
     * this function for get country list api....
     */
    private void callCountryApi() {
        try {
            RequestParams requestParams = new RequestParams();
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.COUNTRYLIST_URL, "country", "get", requestParams);
            //Log.e("Registration", "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * this funcation for get statelist api...
     */
    private void callStateApi() {
        try {
            RequestParams requestParams = new RequestParams();
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.STATELIST_URL, "state", "get", requestParams);
            //Log.e("Registration", "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public void success(String url, String apiName, String response) {
   //     Log.e(TAG, "response  is" + response);
        try {
            JSONObject mainObj = new JSONObject(response);
            String response_code = mainObj.optString("code");

            if (apiName.equals("country")) {
                if (response_code.equals("200")) {
                    JSONArray dataArray = mainObj.getJSONArray("data");

                    countrylist = new ArrayList<HashMap<String, String>>();

                    for (int i = 0; i < dataArray.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonObject = dataArray.getJSONObject(i);
                        String countryID = jsonObject.optString("id");
                        String countryName = jsonObject.optString("name");
                        map.put("countryID", countryID);
                        map.put("countryName", countryName);
                        countrylist.add(map);

                    }
                    Log.e("countrylist",""+countrylist);
                }else if(response_code.equals("1000"))
                {
                    Pref.deleteAll(SplashActivity.this);
                    startActivity(new Intent(SplashActivity.this, SplashActivity.class));
                    Pref.setValue(SplashActivity.this, "user_type", "guest");
                   // startActivity(new Intent(SplashActivity.this, SplashActivity.class));
                    //Utils.showToast(this, message);
                }
            } else if (apiName.equals("state")) {
                if (response_code.equals("200")) {
                    JSONObject dataObj = mainObj.getJSONObject("data");
                    Log.e("jsonobject",""+mainObj.toString());
                   /* for (int i = 0; i < dataArray.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonObject = dataArray.getJSONObject(i);
                        String stateID = jsonObject.optString("id");
                        String stateName = jsonObject.optString("name");
                        map.put("stateID", stateID);
                        map.put("stateName", stateName);
                        statelist.add(map);

                    }*/
                    Iterator<String> keys2 = dataObj.keys();

                    listnew = new ArrayList<>();
                    while (keys2.hasNext()) {
                        String key = keys2.next();
                        Log.e("keys2",""+key);
                        dataInnerArray = dataObj.getJSONArray(key);
                        HashMap<String, ArrayList<HashMap<String, String>>> map1 = new HashMap<>();
                        statelist = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < dataInnerArray.length(); i++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject jsonObject = dataInnerArray.getJSONObject(i);
                            String stateID = jsonObject.optString("id");
                            String stateName = jsonObject.optString("name");
                            map.put("stateID", stateID);
                            map.put("stateName", stateName);
                            statelist.add(map);
                        }
                        map1.put(key, statelist);
                        listnew.add(map1);


                    }
                  Log.e("listnew", "statelist " + listnew);

                   /* JSONArray demo = dataObj.getJSONArray("2");
                    for (int i = 0; i < demo.length(); i++) {
                        JSONObject obb = demo.getJSONObject(i);
                        Log.e(TAG, "statelist111 " + obb);

                    }*/

                }else if(response_code.equals("1000"))
                {
                    Pref.deleteAll(SplashActivity.this);

                   // startActivity(new Intent(SplashActivity.this, SplashActivity.class));

                    //Utils.showToast(this, message);
                }
            }
        } catch (Exception e) {

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