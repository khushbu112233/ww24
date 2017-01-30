package com.westwood24.connect.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.fragment.WebViewForAboutusFragment;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.FontCustom;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends Activity implements View.OnClickListener, WebserviceCall.WebserviceResponse {

    //Ui Object declaration..

    EditText mForgotEmailEdt;
    TextView mContactSupportTv;
    Button mSendEmailBtn;
    RelativeLayout mHeaderBackRl;
    TextView mHeaderTitleTv;
    Intent mIntent;
    TextView txt_need_help;
    ImageView img_left;
    //variable declaraion..
    String email, verificationCode, password, conpasword;
    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mIntent = getIntent();
        initComponents();
        prepareView();
        setActionListener();
    }

    private void initComponents() {
        mForgotEmailEdt = (EditText) findViewById(R.id.activity_forgot_email_edt);
        mSendEmailBtn = (Button) findViewById(R.id.activity_forgot_send_btn);
        img_left = (ImageView)findViewById(R.id.img_left);
        mContactSupportTv = (TextView) findViewById(R.id.activity_forgot_contact_support_team_tv);
        mHeaderBackRl = (RelativeLayout) findViewById(R.id.header_layout_back_rl);
        mHeaderTitleTv = (TextView) findViewById(R.id.txt_title);
        mHeaderBackRl.setVisibility(View.GONE);
        txt_need_help = (TextView)findViewById(R.id.txt_need_help);

    }

    private void prepareView() {
        mHeaderTitleTv.setText("Forgot Password");
        mForgotEmailEdt.setTypeface(FontCustom.setFont(this));
        mSendEmailBtn.setTypeface(FontCustom.setFont(this));

    }

    private void setActionListener() {
        mSendEmailBtn.setOnClickListener(this);
        img_left.setOnClickListener(this);
        txt_need_help.setOnClickListener(this);
        mContactSupportTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.activity_forgot_send_btn:
                mChangePwdBtnClicked();
                break;
            case R.id.activity_forgot_contact_support_team_tv:
                Intent i = new Intent(ForgotPasswordActivity.this,WebviewActivity.class);
                Pref.setValue(ForgotPasswordActivity.this,"fromweb","contact");
                startActivity(i);
                break;
            case R.id.txt_need_help:
                Intent i1 = new Intent(ForgotPasswordActivity.this,WebviewActivity.class);
                Pref.setValue(ForgotPasswordActivity.this,"fromweb","needhelp");
                startActivity(i1);
                break;
            case R.id.img_left:
                finish();
                break;
        }
    }

    private void mChangePwdBtnClicked() {
        email = mForgotEmailEdt.getText().toString().trim();

        if (isValidDataForForgotPwd()) {
            callForogtPwdApi();
        }

    }


    /*
      this function for change pwd validation
     */
    private boolean isValidDataForForgotPwd() {
        if (!email.equals("")) {
            if (Utils.isValidEmail(email)) {
                return true;
            } else {
                mForgotEmailEdt.setError(getResources().getString(R.string.Invalid_email));
            }

        } else {
            mForgotEmailEdt.setError(getResources().getString(R.string.Email_is_required));
        }


        return false;
    }


    /*
          this function for call change password api
         */
    private void callForogtPwdApi() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("email", mForgotEmailEdt.getText().toString().trim());
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.FOROGOT_PWD_URL, "forogot_password", "post", requestParams);
            Log.e(TAG, "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void success(String url, String apiName, String response) {
        Log.e(TAG, "response  is" + response);
        try {
            JSONObject mainObj = new JSONObject(response);
            String message = mainObj.optString("message");
            String code = mainObj.optString("code");

            if (apiName.equals("forogot_password")) {
                if (code.equals("200")) {
                    // String resverificationCode = mainObj.optString("verificationCode");
                    // Log.e(TAG, "code " + resverificationCode);
                    // Pref.setValue(ForgotPasswordActivity.this, Constants.TAG_VERIFICATION_CODE, resverificationCode);
                    Utils.showToast(ForgotPasswordActivity.this, message);
                    finish();
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                }else if(code.equals("1000"))
                {


                    Pref.deleteAll(ForgotPasswordActivity.this);
                    DashBoardActivity.mDrawerLayout.closeDrawers();
                    startActivity(new Intent(ForgotPasswordActivity.this, SplashActivity.class));
                    Pref.setValue(ForgotPasswordActivity.this, "user_type", "guest");
                    Utils.showToast(ForgotPasswordActivity.this, message);
                }else {
                    Utils.showToast(ForgotPasswordActivity.this, message);
                }
            } else {

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
