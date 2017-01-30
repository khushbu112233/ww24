package com.westwood24.connect.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.activity.DashBoardActivity;
import com.westwood24.connect.activity.SplashActivity;
import com.westwood24.connect.model.SignupErrorStep1;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.FieldsValidator;
import com.westwood24.connect.utils.FontCustom;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener, WebserviceCall.WebserviceResponse {

    //Ui Object declaration..

    EditText mOldPwdEdt;
    EditText mNewpwdEdt;
    EditText mNewConfirmPwdEdt;
    Button mChangePwdBtn;
    FieldsValidator mValidator;
    //variable declaraion..
    String old_pwd, new_pwd, confirm_pwd;
    String TAG = "ChangePasswordFragment";
    View mRootView;
    Context context;

    String error_msg="";
    ArrayList<SignupErrorStep1> error_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        mRootView = inflater.inflate(R.layout.activity_change_password, container, false);
        Pref.setValue(getActivity(), "current", "changepassword");
        DashBoardActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        initComponents();
        prepareView();
        setActionListener();
        SignupErrorStep1[] signupErrorStep1=new SignupErrorStep1[7];
        signupErrorStep1[0]=new SignupErrorStep1();
        signupErrorStep1[0].setError("Please enter old password");
        signupErrorStep1[0].setError_flag("0");
        signupErrorStep1[1]=new SignupErrorStep1();
        signupErrorStep1[1].setError(getString(R.string.Password_must_be_withine_6_to_15));
        signupErrorStep1[1].setError_flag("0");
        signupErrorStep1[2]=new SignupErrorStep1();
        signupErrorStep1[2].setError("Please enter new password");
        signupErrorStep1[2].setError_flag("0");
        signupErrorStep1[3]=new SignupErrorStep1();
        signupErrorStep1[3].setError(getString(R.string.Password_must_be_withine_6_to_15));
        signupErrorStep1[3].setError_flag("0");

        signupErrorStep1[4]=new SignupErrorStep1();
        signupErrorStep1[4].setError("Please enter confirm password");
        signupErrorStep1[4].setError_flag("0");
        signupErrorStep1[5]=new SignupErrorStep1();
        signupErrorStep1[5].setError(getString(R.string.CPassword_must_be_withine_6_to_15));
        signupErrorStep1[5].setError_flag("0");
        signupErrorStep1[6]=new SignupErrorStep1();
        signupErrorStep1[6].setError("New password and Confirm password must be same!");
        signupErrorStep1[6].setError_flag("0");

        error_list.add(signupErrorStep1[0]);
        error_list.add(signupErrorStep1[1]);
        error_list.add(signupErrorStep1[2]);
        error_list.add(signupErrorStep1[3]);
        error_list.add(signupErrorStep1[4]);
        error_list.add(signupErrorStep1[5]);
        error_list.add(signupErrorStep1[6]);

        return mRootView;
    }


    private void initComponents() {
        mOldPwdEdt = (EditText) mRootView.findViewById(R.id.activity_change_old_pwd_edt);
        mNewpwdEdt = (EditText) mRootView.findViewById(R.id.activity_change_new_pwd_edt);
        mNewConfirmPwdEdt = (EditText) mRootView.findViewById(R.id.activity_change_new_confirm_pwd_edt);
        mChangePwdBtn = (Button) mRootView.findViewById(R.id.activity_change_pwd_btn);
        context = getActivity();
        error_list=new ArrayList<>();
        mValidator = new FieldsValidator(getActivity());


    }

    private void prepareView() {
        mOldPwdEdt.setTypeface(FontCustom.setFont(getActivity()));
        mNewpwdEdt.setTypeface(FontCustom.setFont(getActivity()));
        mNewConfirmPwdEdt.setTypeface(FontCustom.setFont(getActivity()));
        mChangePwdBtn.setTypeface(FontCustom.setFont(getActivity()));
    }

    private void setActionListener() {
        mChangePwdBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_change_pwd_btn) {
            mChangePwdBtnClicked();
            Log.e("button ", "button click");
        }
    }

    private void mChangePwdBtnClicked() {
        old_pwd = mOldPwdEdt.getText().toString().trim();
        new_pwd = mNewpwdEdt.getText().toString().trim();
        confirm_pwd = mNewConfirmPwdEdt.getText().toString().trim();

        isValidDataForChangePwd();
    }

    /*
      this function for change pwd validation
     */
    private void isValidDataForChangePwd() {

        old_pwd = mOldPwdEdt.getText().toString().trim();
        new_pwd = mNewpwdEdt.getText().toString().trim();
        confirm_pwd = mNewConfirmPwdEdt.getText().toString().trim();


        if(old_pwd.length()==0)
        {
            error_list.get(0).setError_flag("0");
            error_list.get(1).setError_flag("1");

            mOldPwdEdt.setError("Please enter old password");

        }else
        {
            if(old_pwd.length()>=6&&old_pwd.length()<=14) {
                error_list.get(0).setError_flag("1");
                error_list.get(1).setError_flag("1");
            }else
            {
                error_list.get(0).setError_flag("1");
                error_list.get(1).setError_flag("0");
                mOldPwdEdt.setError(getString(R.string.Password_must_be_withine_6_to_15));
            }

        }

        if(new_pwd.length()==0)
        {
            error_list.get(2).setError_flag("0");
            error_list.get(3).setError_flag("1");
            error_list.get(6).setError_flag("1");
            mNewpwdEdt.setError("Please enter new password");

        }else
        {
            if(new_pwd.length()>=6&&new_pwd.length()<=14) {
                if(mNewpwdEdt.getText().toString().equalsIgnoreCase(mNewConfirmPwdEdt.getText().toString()))
                {
                    error_list.get(2).setError_flag("1");
                    error_list.get(3).setError_flag("1");
                    error_list.get(6).setError_flag("1");
                }else
                {
                    error_list.get(2).setError_flag("1");
                    error_list.get(3).setError_flag("1");
                    error_list.get(6).setError_flag("0");
                    mNewConfirmPwdEdt.setError("New password and Confirm password must be same!");
                }
            }else
            {

                error_list.get(2).setError_flag("1");
                error_list.get(3).setError_flag("0");
                error_list.get(6).setError_flag("1");
                mNewpwdEdt.setError(getString(R.string.Password_must_be_withine_6_to_15));



            }

        }
        if(confirm_pwd.length()==0)
        {
            error_list.get(4).setError_flag("0");
            error_list.get(5).setError_flag("1");
            error_list.get(6).setError_flag("1");
            mNewConfirmPwdEdt.setError("Please enter confirm password");

        }else
        {
            if(confirm_pwd.length()>=6&&confirm_pwd.length()<=14) {
                if(mNewpwdEdt.getText().toString().equalsIgnoreCase(mNewConfirmPwdEdt.getText().toString()))
                {
                    error_list.get(4).setError_flag("1");
                    error_list.get(5).setError_flag("1");
                    error_list.get(6).setError_flag("1");
                }else
                {
                    error_list.get(4).setError_flag("1");
                    error_list.get(5).setError_flag("1");
                    error_list.get(6).setError_flag("0");
                    mNewConfirmPwdEdt.setError("New password and Confirm password must be same!");
                }
            }else
            {

                error_list.get(4).setError_flag("1");
                error_list.get(5).setError_flag("0");
                error_list.get(6).setError_flag("1");
                mNewConfirmPwdEdt.setError(getString(R.string.CPassword_must_be_withine_6_to_15));



            }

        }
        int index = 0;
        error_msg="";
        for(int i=0;i<error_list.size();i++)
        {
            if(error_list.get(i).getError_flag().equals("0"))
            {
                index++;
                error_msg = error_msg+error_list.get(i).getError()+"\n";
            }
        }

        if(index==0)
        {
            callChangePwdApi();

        }
        else {
            //  mValidator.showtoast(error_msg);
        }


    }

    /*
          this function for call change password api
         */
    private void callChangePwdApi() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("authkey", Pref.getValue(getActivity(), Constants.TAG_AUTHKAY, ""));
            requestParams.put("oldpassword", old_pwd);
            requestParams.put("newpassword", new_pwd);
            WebserviceCall webserviceCall = new WebserviceCall(getActivity());
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.CHANGE_PASSWORD_URL, "change_password", "post", requestParams);
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
            String response_code = mainObj.optString("code");

            Log.e("response_code",""+response_code);
            if (response_code.equals("200")) {
                Intent i = new Intent(getActivity(),DashBoardActivity.class);
                startActivity(i);
                getActivity().finish();
                Utils.showToast(getActivity(), message);
            }else if(response_code.equals("400"))
            {
                Utils.showToast(getActivity(), message);
            }
            else if(response_code.equalsIgnoreCase("1000"))
            {
                Pref.deleteAll(context);
                DashBoardActivity.mDrawerLayout.closeDrawers();
                startActivity(new Intent(context, SplashActivity.class));
                Utils.showToast(context, message);
                Pref.setValue(context, "user_type", "guest");
            }else {

                Utils.showToast(getActivity(), message);
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
            String response_code = mainObj.optString("code");
            if(response_code.equalsIgnoreCase("400"))
            {
                Utils.showToast(getActivity(), message);
            }else if(response_code.equalsIgnoreCase("1000"))
            {
                Pref.deleteAll(context);
                DashBoardActivity.mDrawerLayout.closeDrawers();
                startActivity(new Intent(context, SplashActivity.class));
                Utils.showToast(context, message);
            }
            //Utils.showToast(getActivity(), message);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
