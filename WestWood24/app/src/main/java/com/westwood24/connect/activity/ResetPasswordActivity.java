package com.westwood24.connect.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.FontCustom;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends Activity implements View.OnClickListener, WebserviceCall.WebserviceResponse {

    //Ui Object declaration..

    EditText mForgotEmailEdt;
    EditText mVerifcationCodeEdt;
    EditText mPasswordEdt;
    EditText mConfirmPwdEdt;
    View mConPwdView;
    View mVerificationView;
    View mPasswordView;
    TextView mContactSupportTv;
    Button mSendEmailBtn;
    RelativeLayout mHeaderBackRl;
    TextView mHeaderTitleTv;
    Intent mIntent;
    //variable declaraion..
    String email, verificationCode, password, conpasword;
    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mIntent = getIntent();
        initComponents();
        prepareView();
        setActionListener();
    }

    private void initComponents() {
        mForgotEmailEdt = (EditText) findViewById(R.id.activity_forgot_email_edt);
        mVerifcationCodeEdt = (EditText) findViewById(R.id.activity_forgot_verifi_code_edt);
        mPasswordEdt = (EditText) findViewById(R.id.activity_forgot_password_edt);
        mConfirmPwdEdt = (EditText) findViewById(R.id.activity_forgot_con_password_edt);
        mConPwdView = (View) findViewById(R.id.activity_forgot_con_password_view);
        mVerificationView = (View) findViewById(R.id.activity_forgot_veri_view);
        mPasswordView = (View) findViewById(R.id.activity_forgot_password_view);
        mSendEmailBtn = (Button) findViewById(R.id.activity_forgot_send_btn);
        mContactSupportTv = (TextView) findViewById(R.id.activity_forgot_contact_support_team_tv);
        mHeaderBackRl = (RelativeLayout) findViewById(R.id.header_layout_back_rl);
        mHeaderTitleTv = (TextView) findViewById(R.id.txt_title);

    }

    private void prepareView() {
        mHeaderTitleTv.setText("Reset Password");

        mForgotEmailEdt.setTypeface(FontCustom.setFont(this));
        mVerifcationCodeEdt.setTypeface(FontCustom.setFont(this));
        mConfirmPwdEdt.setTypeface(FontCustom.setFont(this));
        mPasswordEdt.setTypeface(FontCustom.setFont(this));
        mSendEmailBtn.setTypeface(FontCustom.setFont(this));

    }

    private void setActionListener() {
        mSendEmailBtn.setOnClickListener(this);
        mHeaderBackRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.activity_forgot_send_btn:
                mResetPwdBtnClicked();
                break;
            case R.id.activity_forgot_contact_support_team_tv:
                break;
            case R.id.header_layout_back_rl:
                finish();
                break;
        }
    }


    private void mResetPwdBtnClicked() {
        email = mForgotEmailEdt.getText().toString().trim();
        verificationCode = mVerifcationCodeEdt.getText().toString().trim();
        password = mPasswordEdt.getText().toString().trim();
        conpasword = mConfirmPwdEdt.getText().toString().trim();

        if (isValidDataForResetPwd()) {
            callResetPwdApi();
        }

    }


    /*
    this function for reset pwd validation
   */
    private boolean isValidDataForResetPwd() {

        if (!verificationCode.equals("")) {
            if (!password.equals("")) {
                if (!conpasword.equals("")) {
                    if (conpasword.equals(password)) {
                        return true;
                    } else {
                        mConfirmPwdEdt.setError("New password and Confirm password must be same!");
                    }
                } else {
                    mConfirmPwdEdt.setError("Please enter confirm password");
                }
            } else {
                mPasswordEdt.setError("Please enter password.");
            }
        } else {
            mVerifcationCodeEdt.setError("Please enter verification code.");
        }


        return false;
    }



 /*
          this function for call reset password api
         */

    private void callResetPwdApi() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("email", mIntent.getStringExtra("email"));
            requestParams.put("verificationCode", verificationCode);
            requestParams.put("password", conpasword);
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.RESET_PWD_URL, "reset_password", "post", requestParams);
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
            String status = mainObj.optString("status");
            String message = mainObj.optString("message");
            String response_code = mainObj.optString("response_code");

            if (apiName.equals("reset_password")) {
                if (status.equals("true")) {
                    Utils.showToast(ResetPasswordActivity.this, message);
                    startActivity(new Intent(ResetPasswordActivity.this, SplashActivity.class));
                } else {
                    Utils.showToast(ResetPasswordActivity.this, message);
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
