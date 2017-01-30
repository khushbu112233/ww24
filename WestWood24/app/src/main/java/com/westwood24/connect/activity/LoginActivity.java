package com.westwood24.connect.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.FieldsValidator;
import com.westwood24.connect.utils.FontCustom;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import eu.janmuller.android.simplecropimage.Util;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, WebserviceCall.WebserviceResponse {


    //UI Object declaration...
    EditText mEmailPhoneEdt;
    EditText mEmailEdt;
    EditText mPhoneEdt;
    EditText mPasswordEdt;
    TextView mEmailTv;
    TextView mPhoneTv;
    TextView mPasswordTv;
    ImageView img_left;
    LinearLayout mEmailLn;
    LinearLayout mPhoneLn;
    View mPhoneView;
    View mEmailView;
    Button mLoginBtn;
    TextView mRegisterBtn;
    RelativeLayout mHeaderMainRl;
    ImageView mHeaderCloseImg;
    RelativeLayout mHeaderBackRl;
    TextView mHeaderTitleTv;
    TextView mForgotPwdTv;
    Intent mIntent;
    ImageView header_layout_comment_add_see_img;
    FieldsValidator mValidator;
    ImageView activity_login_logo_img;
    TextView activity_login_forogt_pwd_tv;
    String splashGetData;
    String TAG = "LoginActivity";
    String email, phone, password, emailPhone;
    int loginPhoneEmail;
    ByteArrayOutputStream bytearrayoutputstream;
    byte[] BYTE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mIntent = getIntent();
        mValidator = new FieldsValidator(LoginActivity.this);
        initComponents();
        prepareView();
        setActionListener();

    }

    private void initComponents() {
        mEmailEdt = (EditText) findViewById(R.id.activity_login_email_edt);
        mPhoneEdt = (EditText) findViewById(R.id.activity_login_phone_edt);
        activity_login_logo_img = (ImageView)findViewById(R.id.activity_login_logo_img);
        mEmailTv = (TextView) findViewById(R.id.activity_login_email_tv);
        mPhoneTv = (TextView) findViewById(R.id.activity_login_phone_tv);
        mPasswordTv = (TextView) findViewById(R.id.activity_login_pwd_tv);
        mEmailLn = (LinearLayout) findViewById(R.id.activity_login_email_ln);
        mPhoneLn = (LinearLayout) findViewById(R.id.activity_login_phone_ln);
        mEmailView = (View) findViewById(R.id.activity_login_email_view);
        mPhoneView = (View) findViewById(R.id.activity_login_phone_view);
        mHeaderMainRl = (RelativeLayout) findViewById(R.id.header_main_rl);
        mHeaderCloseImg = (ImageView) findViewById(R.id.header_layout_vote_see_img);
        mHeaderBackRl = (RelativeLayout) findViewById(R.id.header_layout_back_rl);
        mHeaderTitleTv = (TextView) findViewById(R.id.txt_title);
        mForgotPwdTv = (TextView) findViewById(R.id.activity_login_forogt_pwd_tv);
        img_left = (ImageView)findViewById(R.id.img_left);
        img_left.setVisibility(View.INVISIBLE);
        header_layout_comment_add_see_img = (ImageView)findViewById(R.id.header_layout_comment_add_see_img);
        header_layout_comment_add_see_img.setVisibility(View.VISIBLE);

//        BYTE = bytearrayoutputstream.toByteArray();
        /**
         * new changes so new edittext field..
         */
        mEmailPhoneEdt = (EditText) findViewById(R.id.activity_login_emailphone_edt);
        mPasswordEdt = (EditText) findViewById(R.id.activity_login_pwd_edt);
        mLoginBtn = (Button) findViewById(R.id.activity_login_login_btn);
        mRegisterBtn = (TextView) findViewById(R.id.activity_login_register_btn);
        mEmailPhoneEdt.setText(Pref.getValue(LoginActivity.this, "login_email",""));
        mPasswordEdt.setText(Pref.getValue(LoginActivity.this, "login_password", ""));

    }

    private void prepareView() {
        /*splashGetData = mIntent.getStringExtra("loginwithphoneemail");
        if (splashGetData.equalsIgnoreCase("0")) {
            mEmailEdt.setVisibility(View.GONE);
            mEmailLn.setVisibility(View.GONE);
            mEmailView.setVisibility(View.GONE);
        } else {
            mPhoneEdt.setVisibility(View.GONE);
            mPhoneLn.setVisibility(View.GONE);
            mPhoneView.setVisibility(View.GONE);
        }*/

        mEmailPhoneEdt.setTypeface(FontCustom.setFont(this));
        mPasswordEdt.setTypeface(FontCustom.setFont(this));
        mLoginBtn.setTypeface(FontCustom.setFont(this));
        mRegisterBtn.setTypeface(FontCustom.setFont(this));
        mForgotPwdTv.setTypeface(FontCustom.setFont(this));

        SplashActivity.setTextWatcher(LoginActivity.this, mPhoneEdt, mPhoneTv);
        SplashActivity.setTextWatcher(LoginActivity.this, mEmailEdt, mEmailTv);
        SplashActivity.setTextWatcher(LoginActivity.this, mPasswordEdt, mPasswordTv);
        //mHeaderMainRl.setBackgroundColor(getResources().getColor(R.color.loginscreen_bg));

        mHeaderBackRl.setVisibility(View.GONE);
        mHeaderTitleTv.setTypeface(FontCustom.setFontBold(this));
        mHeaderTitleTv.setAllCaps(false);
        mHeaderTitleTv.setText("LOGIN");
        mHeaderCloseImg.setVisibility(View.GONE);
      /*  Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ww24connect_logo);
      //  icon.compress(Bitmap.CompressFormat.PNG, 0, bytearrayoutputstream);


        Bitmap b1= ThumbnailUtils.extractThumbnail(icon, 200, 200);
*/
        //      activity_login_logo_img.setImageBitmap(b1);


        // BYTE = bytearrayoutputstream.toByteArray();

        //Bitmap bitmap2 = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

        // activity_login_logo_img.setImageBitmap(bitmap2);
    }

    private void setActionListener() {
        mLoginBtn.setOnClickListener(this);
        mHeaderCloseImg.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        mForgotPwdTv.setOnClickListener(this);
        header_layout_comment_add_see_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.activity_login_login_btn:
                mLoginBtnClicked();
                break;
            case R.id.header_layout_comment_add_see_img:
                //   Log.e("user_type",""+Pref.getValue(LoginActivity.this, "user_type", ""));
                finish();

                break;
            case R.id.img_left:
                finish();
                break;
            case R.id.activity_login_register_btn:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class).putExtra("registerwithphoneemail", "1"));
                break;
            case R.id.activity_login_forogt_pwd_tv:

               startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
             //    startActivity(new Intent(LoginActivity.this, WelcomePageActivity.class));
                break;


        }
    }

    private void mLoginBtnClicked() {

        boolean isCorrect = true;




        if(mEmailPhoneEdt.getText().toString().length()>0)
        {
            if(Patterns.EMAIL_ADDRESS.matcher(mEmailPhoneEdt.getText().toString()).matches()) {
                isCorrect = true;
            }else
            {
                isCorrect =false;
                mEmailPhoneEdt.setError(getString(R.string.Invalid_email));
            }
        }else
        {

            isCorrect=false;
            mEmailPhoneEdt.setError(getString(R.string.Email_is_required));
        }
        if(mPasswordEdt.getText().toString().length()>0)
        {
            if(mPasswordEdt.getText().toString().length()>=6&&mPasswordEdt.getText().toString().length()<=15)

            {
                isCorrect = true;
            }else
            {
                isCorrect=false;
                mPasswordEdt.setError( getString(R.string.Password_is_required));

            }

        }else
        {
            isCorrect=false;
            mPasswordEdt.setError(getString(R.string.Password_must_be_withine_6_to_15));
        }




        if (isCorrect) {
            emailPhone = mEmailPhoneEdt.getText().toString().trim();
            password = mPasswordEdt.getText().toString();

            callLoginApi();
            String deviceId = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Log.e(TAG, "deviceId " + deviceId);
            Pref.setValue(this, Constants.TAG_DEVICETOKEN, deviceId);
            Pref.setValue(LoginActivity.this, "offset", "0");


            //    scrollview.fullScroll(ScrollView.FOCUS_UP);
            return;
        }else
        {
            // Toast.makeText(LoginActivity.this,"Internal server error",Toast.LENGTH_LONG).show();

            Log.e("error",""+"not call" );


        }


        //email = mEmailEdt.getText().toString().trim();
        // phone = mPhoneEdt.getText().toString().trim();



    }


    /*
          this function for call login api
         */
    private void callLoginApi() {
        try {
            RequestParams requestParams = new RequestParams();

            requestParams.put("email", emailPhone);
            requestParams.put("password", password);
            requestParams.put("devicetoken", Pref.getValue(LoginActivity.this, Constants.TAG_DEVICETOKEN, ""));
            requestParams.put("devicetype", "android");
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.LOGIN_URL, "login", "post", requestParams);
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

            String code = mainObj.optString("code");
            String message = mainObj.optString("message");
            if (code.equals("200")) {
                JSONObject dataObj = mainObj.getJSONObject("data");
                String authkey = dataObj.optString("authkey");
                String CountryName = dataObj.optString("country");
                String Statename = dataObj.optString("state");

                String id = dataObj.optString("id");
                String firstname = dataObj.optString("firstname");
                String lastname = dataObj.optString("lastname");
                String email = dataObj.optString("email");
                String phone = dataObj.optString("mobilenumber");
                String profilepicture = dataObj.optString("profilepicture");
                String country = dataObj.optString("countryid");
                String state = dataObj.optString("stateid");
                String loginCount=dataObj.optString("loginCount");
                Log.e(TAG, "country " + country);
                Log.e(TAG, "authkey " + authkey);
                String zipcode = dataObj.optString("zipcode");

                Pref.setValue(LoginActivity.this, Constants.IS_LOGIN, "1");
                Pref.setValue(LoginActivity.this, Constants.TAG_PROFILE_PICTURE, profilepicture);
                Pref.setValue(LoginActivity.this, Constants.TAG_USER_ID, id);
                Pref.setValue(LoginActivity.this, Constants.TAG_AUTHKAY, authkey);
                Pref.setValue(LoginActivity.this, Constants.TAG_FIRSTNAME, firstname);
                Pref.setValue(LoginActivity.this, Constants.TAG_LASTNAME, lastname);
                Pref.setValue(LoginActivity.this, Constants.TAG_EMAIL, email);
                Pref.setValue(LoginActivity.this, Constants.TAG_PHONE, phone);
                Pref.setValue(LoginActivity.this, Constants.TAG_COUNTRY_CODE, country);
                Pref.setValue(LoginActivity.this, Constants.TAG_STATE_CODE, state);
                Pref.setValue(LoginActivity.this, Constants.TAG_STATE_NAME, state);
                Pref.setValue(LoginActivity.this, Constants.TAG_ZIPCODE, zipcode);
                Pref.setValue(LoginActivity.this, Constants.TAG_COUNTRY_NAME, CountryName);
                Pref.setValue(LoginActivity.this, Constants.TAG_STATE_NAME, Statename);

                Pref.setValue(LoginActivity.this, "user_type", "login");
                Pref.setValue(LoginActivity.this, "user_login_register", "login");
                if(loginCount.equalsIgnoreCase("1")) {
                    startActivity(new Intent(LoginActivity.this, WelcomePageActivity.class));
                }else
                {
                    startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                }
            }else if (code.equals("400"))
            {
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

            Pref.setValue(LoginActivity.this, "user_type", "login");
            mainObj = new JSONObject(response);
            String message = mainObj.optString("message");
            Utils.showToast(this, message);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
