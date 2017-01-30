package com.westwood24.connect.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.utils.CircularImageView;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.FieldsValidator;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONException;
import org.json.JSONObject;

import eu.janmuller.android.simplecropimage.Util;

public class ProfileScreenActivity extends AppCompatActivity implements View.OnClickListener, WebserviceCall.WebserviceResponse {

    CircularImageView mProfileCImg;
    EditText mFirstnameEdt;
    EditText mLastnameEdt;
    EditText mCountryEdt;
    EditText mStateEdt;
    EditText mZipcodeEdt;
    ImageView mEditPickImg;
    Button mUpdateProfileBtn;
    FieldsValidator mValidator;
    RelativeLayout mHeaderBackRl;
    TextView mHeaderTitleTv;
    TextView mHeaderBackTitleRl;
    //varialble declaration...
    String TAG = "ProfileScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        mValidator = new FieldsValidator(ProfileScreenActivity.this);
        initComponents();
        prepareView();
        setActionListener();
    }

    private void initComponents() {
        mProfileCImg = (CircularImageView) findViewById(R.id.activity_profile_profile_img);
        mFirstnameEdt = (EditText) findViewById(R.id.activity_profile_firstname_edt);
        mLastnameEdt = (EditText) findViewById(R.id.activity_profile_lastname_edt);
        mCountryEdt = (EditText) findViewById(R.id.activity_profile_country_edt);
        mStateEdt = (EditText) findViewById(R.id.activity_profile_state_edt);
        mZipcodeEdt = (EditText) findViewById(R.id.activity_profile_zipcode_edt);
        mEditPickImg = (ImageView) findViewById(R.id.activity_profile_pick_img);
        mHeaderBackRl = (RelativeLayout) findViewById(R.id.header_layout_back_rl);
        mHeaderTitleTv = (TextView) findViewById(R.id.txt_title);
        mHeaderBackTitleRl = (TextView) findViewById(R.id.txt_title_left);
        mUpdateProfileBtn = (Button) findViewById(R.id.activity_profile_updateprofile_btn);
    }

    private void prepareView() {

        /*if (Pref.getValue(ProfileScreenActivity.this, Constants.TAG_PROFILE_PICTURE, "").isEmpty()) {
            mProfileCImg.setImageResource(R.mipmap.ic_launcher);

        } else {
            Bitmap userProfilePath = Utils.base64ToImage(Pref.getValue(ProfileScreenActivity.this, Constants.TAG_PROFILE_PICTURE, ""));
            mProfileCImg.setImageBitmap(userProfilePath);
        }*/

        Log.e("profile_pic",Pref.getValue(ProfileScreenActivity.this, Constants.TAG_PROFILE_PICTURE, "")+"");
        Toast.makeText(ProfileScreenActivity.this, "" + Pref.getValue(ProfileScreenActivity.this, Constants.TAG_COUNTRY_NAME, "") + "state " + Pref.getValue(ProfileScreenActivity.this, Constants.TAG_STATE_NAME, ""), Toast.LENGTH_SHORT).show();
        Bitmap userProfilePath = Utils.base64ToImage(Pref.getValue(ProfileScreenActivity.this, Constants.TAG_PROFILE_PICTURE, ""));
        mProfileCImg.setImageBitmap(userProfilePath);
        mFirstnameEdt.setText(Pref.getValue(ProfileScreenActivity.this, Constants.TAG_FIRSTNAME, ""));
        mLastnameEdt.setText(Pref.getValue(ProfileScreenActivity.this, Constants.TAG_LASTNAME, ""));
        mCountryEdt.setText(Pref.getValue(ProfileScreenActivity.this, Constants.TAG_COUNTRY_NAME, ""));
        mStateEdt.setText(Pref.getValue(ProfileScreenActivity.this, Constants.TAG_STATE_NAME, ""));
        mZipcodeEdt.setText(Pref.getValue(ProfileScreenActivity.this, Constants.TAG_ZIPCODE, ""));
        mHeaderBackTitleRl.setText("Home");
        mHeaderTitleTv.setText("Profile");
    }

    private void setActionListener() {
        mEditPickImg.setOnClickListener(this);
        mUpdateProfileBtn.setOnClickListener(this);
        mHeaderBackRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_profile_pick_img) {

        }
        if (v.getId() == R.id.activity_profile_updateprofile_btn) {
            mUpdateProfilebtnClicked();

        }
        if (v.getId() == R.id.header_layout_back_rl) {
            startActivity(new Intent(ProfileScreenActivity.this, DashBoardActivity.class));
        }
    }

    private void mUpdateProfilebtnClicked() {
        boolean isCorrect = true;

        isCorrect = isCorrect
                && mValidator
                .validateNotEmpty(mFirstnameEdt,
                        getString(R.string.FullName_is_required));
        isCorrect = isCorrect
                && mValidator
                .validateNotEmpty(mLastnameEdt,
                        getString(R.string.LastName_is_required));


        isCorrect = isCorrect
                && mValidator
                .validateNotEmpty(mCountryEdt,
                        getString(R.string.Country_is_required));
        isCorrect = isCorrect
                && mValidator
                .validateNotEmpty(mStateEdt,
                        getString(R.string.State_is_required));

        isCorrect = isCorrect
                && mValidator
                .validateNotEmpty(mZipcodeEdt,
                        getString(R.string.zipcode_is_required));


        if (!isCorrect) {
            //    scrollview.fullScroll(ScrollView.FOCUS_UP);
            return;
        }

        callUpdateProfileApi();
    }

    private void callUpdateProfileApi() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("firstname", mFirstnameEdt.getText().toString().trim());
            requestParams.put("lastname", mLastnameEdt.getText().toString().trim());
            requestParams.put("country", mCountryEdt.getText().toString().trim());
            requestParams.put("state", mStateEdt.getText().toString().trim());
            requestParams.put("zipcode", mZipcodeEdt.getText().toString().trim());
            //requestParams.put("profilepicture", mFirstnameEdt.getText().toString().trim());
            //temp add user id in future add token raplace to userid
            requestParams.put("userid", Pref.getValue(ProfileScreenActivity.this, Constants.TAG_USER_ID, ""));
            requestParams.put("deviceType", "android");
            requestParams.put("deviceToken", "temp");

            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.UPDATE_PROFILE_URL, "update_profile", "post", requestParams);
            Log.e(TAG, "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void success(String url, String apiName, String response) {
        Log.e(TAG, "response is" + response);
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
