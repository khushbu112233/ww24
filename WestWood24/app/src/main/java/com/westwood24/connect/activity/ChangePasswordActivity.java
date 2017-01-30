package com.westwood24.connect.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, WebserviceCall.WebserviceResponse {

    //Ui Object declaration..

    EditText mOldPwdEdt;
    EditText mNewpwdEdt;
    EditText mNewConfirmPwdEdt;
    Button mChangePwdBtn;

    //variable declaraion..
    String old_pwd, new_pwd, confirm_pwd;
    String TAG = "ChangePasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initComponents();
        prepareView();
        setActionListener();
    }

    private void initComponents() {
        mOldPwdEdt = (EditText) findViewById(R.id.activity_change_old_pwd_edt);
        mNewpwdEdt = (EditText) findViewById(R.id.activity_change_new_pwd_edt);
        mNewConfirmPwdEdt = (EditText) findViewById(R.id.activity_change_new_confirm_pwd_edt);
        mChangePwdBtn = (Button) findViewById(R.id.activity_change_pwd_btn);

    }

    private void prepareView() {

    }

    private void setActionListener() {
        mChangePwdBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_change_pwd_btn) {
            mChangePwdBtnClicked();
        }
    }

    private void mChangePwdBtnClicked() {
        old_pwd = mOldPwdEdt.getText().toString().trim();
        new_pwd = mNewpwdEdt.getText().toString().trim();
        confirm_pwd = mNewConfirmPwdEdt.getText().toString().trim();

        if (isValidDataForChangePwd()) {
            callChangePwdApi();
        }

    }

    /*
      this function for change pwd validation
     */
    private boolean isValidDataForChangePwd() {
        if (!old_pwd.equals("")) {
            if (!new_pwd.equals("")) {
                if (!confirm_pwd.equals("")) {
                    if (confirm_pwd.equals(new_pwd)) {
                        return true;
                    } else {
                        mNewConfirmPwdEdt.setError("New password and Confirm password must be same!");
                    }
                } else {
                    mNewConfirmPwdEdt.setError("Please enter confirm password");
                }
            } else {
                mNewpwdEdt.setError("Please enter new password");
            }

        } else {
            mOldPwdEdt.setError("Please enter old password");
        }


        return false;
    }

    /*
          this function for call change password api
         */
    private void callChangePwdApi() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("authkey", Pref.getValue(ChangePasswordActivity.this, Constants.TAG_AUTHKAY, ""));
            requestParams.put("oldpassword", old_pwd);
            requestParams.put("newpassword", new_pwd);
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.CHANGE_PASSWORD_URL, "change_password", "post", requestParams);
            Log.e(TAG, "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void success(String url, String apiName, String response) {
        Log.e(TAG, "response  is change" + response);
        try {
            JSONObject mainObj = new JSONObject(response);
            String message = mainObj.optString("message");
            String code = mainObj.optString("code");

            if (code.equals("200")) {

                Utils.showToast(ChangePasswordActivity.this, message);
            } else {

                Intent i = new Intent(ChangePasswordActivity.this,DashBoardActivity.class);
                startActivity(i);
                finish();
                Utils.showToast(ChangePasswordActivity.this, message);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void failure(String url, String apiName, String response) {
        JSONObject mainObj;
        try {
            mainObj = new JSONObject(response);
            String message = mainObj.optString("message");
           // Utils.showToast(this, message);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
