package com.westwood24.connect.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.adapter.CountryAdapter;
import com.westwood24.connect.adapter.StateAdapter;
import com.westwood24.connect.model.SignupErrorStep1;
import com.westwood24.connect.utils.CircularImageView;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.Country;
import com.westwood24.connect.utils.FieldsValidator;
import com.westwood24.connect.utils.FontCustom;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.State;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import eu.janmuller.android.simplecropimage.CropImage;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener, WebserviceCall.WebserviceResponse, SearchView.OnQueryTextListener, SearchView.OnCloseListener, AdapterView.OnItemClickListener {

    FieldsValidator mValidator;

    String splashGetData;
    int registerPhoneEmail;
    Intent mIntent;
    CircularImageView mProfileCImg;
    EditText mFirstnameEdt;
    EditText mLastnameEdt;
    EditText mEmailEdt;
    EditText mPhoneEdt;
    EditText mPasswordEdt;
    EditText mConfirmPwdEdt;
    EditText mCountryEdt;
    EditText mStateEdt;
    EditText mZipcodeEdt;
    EditText activity_signup_confirm_email_edt;
    TextView mFirstnameTv;
    TextView mLastnameTv;
    TextView mEmailTv;
    TextView mPhoneTv;
    TextView mPasswordTv;
    TextView mConfirmPwdTv;
    TextView mCountryTv;
    TextView mStateTv;
    TextView mZipcodeTv;
    RelativeLayout mHeaderBackRl;
    TextView mHeaderTitleTv;
    View mEmailView;
    View mPhoneView;
    LinearLayout mEmailLn;
    LinearLayout mPhoneLn;
    Button mRegisterBtn;
    Button mRegisterWIthphoneBtn;
    ImageView img_left;
    String TAG = "SignupActivity";
    String firstname, lastname, email,confirm_email, phone, password, confrimpwd, country, state, zipcode;
    private File mFileTemp;
    JSONArray jsonArray;
    ArrayList<String> countrycode, state_id;
    ArrayList<String> country_name, state_name;
    ArrayList<Country> country_lst;
    ArrayList<State> state_lst;
    Dialog listDialog, listDialog1;
    ListView list_country, list_state;
    SearchView search;
    Filter filter;
    String country_code = "", state_code = "";
    public static String selectedImagePath = "";
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    int REQUEST_CAMERA = 0;
    Bitmap rotatedBitmap;
    int SELECT_FILE = 1;
    File file1;
    int ispassword=0;
    public  static ArrayList<HashMap<String,String>> statelist;
    int isCpassword=0;
    String error_msg="";
    ArrayList<SignupErrorStep1> error_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mIntent = getIntent();
        mValidator = new FieldsValidator(SignupActivity.this);
        error_list=new ArrayList<>();
        initComponents();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //callCountryApi();
            }
        }, 100);
        //callCountryApi();
        prepareView();

        SignupErrorStep1[] signupErrorStep1=new SignupErrorStep1[18];
        signupErrorStep1[0]=new SignupErrorStep1();
        signupErrorStep1[0].setError(getString(R.string.FullName_is_required));
        signupErrorStep1[0].setError_flag("0");
        signupErrorStep1[1]=new SignupErrorStep1();
        signupErrorStep1[1].setError(getString(R.string.LastName_is_required));
        signupErrorStep1[1].setError_flag("0");
        signupErrorStep1[2]=new SignupErrorStep1();
        signupErrorStep1[2].setError(getString(R.string.Email_is_required));
        signupErrorStep1[2].setError_flag("0");
        signupErrorStep1[3]=new SignupErrorStep1();
        signupErrorStep1[3].setError(getString(R.string.Invalid_email));
        signupErrorStep1[3].setError_flag("0");

        signupErrorStep1[4]=new SignupErrorStep1();
        signupErrorStep1[4].setError(getString(R.string.Confirm_email_is_required));
        signupErrorStep1[4].setError_flag("0");
        signupErrorStep1[5]=new SignupErrorStep1();
        signupErrorStep1[5].setError(getString(R.string.Invalid_email));
        signupErrorStep1[5].setError_flag("0");
        signupErrorStep1[6]=new SignupErrorStep1();
        signupErrorStep1[6].setError(getString(R.string.email_is_not_match));
        signupErrorStep1[6].setError_flag("0");




        signupErrorStep1[7]=new SignupErrorStep1();
        signupErrorStep1[7].setError(getString(R.string.Phone_No_is_required));
        signupErrorStep1[7].setError_flag("0");
        signupErrorStep1[8]=new SignupErrorStep1();
        signupErrorStep1[8].setError(getString(R.string.Phone_Number_length_must_be_within));
        signupErrorStep1[8].setError_flag("0");
        signupErrorStep1[9]=new SignupErrorStep1();
        signupErrorStep1[9].setError(getString(R.string.Password_is_required));
        signupErrorStep1[9].setError_flag("0");
        signupErrorStep1[10]=new SignupErrorStep1();
        signupErrorStep1[10].setError(getString(R.string.Password_must_be_withine_6_to_15));
        signupErrorStep1[10].setError_flag("0");
        signupErrorStep1[11] = new SignupErrorStep1();
        signupErrorStep1[11].setError(getString(R.string.Confirm_Password_is_required));
        signupErrorStep1[11].setError_flag("0");
        signupErrorStep1[12] = new SignupErrorStep1();
        signupErrorStep1[12].setError(getString(R.string.CPassword_must_be_withine_6_to_15));
        signupErrorStep1[12].setError_flag("0");
        signupErrorStep1[13] = new SignupErrorStep1();
        signupErrorStep1[13].setError(getString(R.string.CPassword_must_be_same));
        signupErrorStep1[13].setError_flag("0");
        signupErrorStep1[14] = new SignupErrorStep1();
        signupErrorStep1[14].setError(getString(R.string.Country_is_required));
        signupErrorStep1[14].setError_flag("0");
        signupErrorStep1[15] = new SignupErrorStep1();
        signupErrorStep1[15].setError(getString(R.string.State_is_required));
        signupErrorStep1[15].setError_flag("0");
        signupErrorStep1[16] = new SignupErrorStep1();
        signupErrorStep1[16].setError(getString(R.string.zipcode_is_required));
        signupErrorStep1[16].setError_flag("0");
        signupErrorStep1[17] = new SignupErrorStep1();
        signupErrorStep1[17].setError(getString(R.string.Zipcode_must_be_withine_5));
        signupErrorStep1[17].setError_flag("0");

        error_list.add(signupErrorStep1[0]);
        error_list.add(signupErrorStep1[1]);
        error_list.add(signupErrorStep1[2]);
        error_list.add(signupErrorStep1[3]);
        error_list.add(signupErrorStep1[4]);
        error_list.add(signupErrorStep1[5]);
        error_list.add(signupErrorStep1[6]);
        error_list.add(signupErrorStep1[7]);
        error_list.add(signupErrorStep1[8]);
        error_list.add(signupErrorStep1[9]);
        error_list.add(signupErrorStep1[10]);
        error_list.add(signupErrorStep1[11]);
        error_list.add(signupErrorStep1[12]);
        error_list.add(signupErrorStep1[13]);
        error_list.add(signupErrorStep1[14]);
        error_list.add(signupErrorStep1[15]);
        error_list.add(signupErrorStep1[16]);
        error_list.add(signupErrorStep1[17]);



        setActionListener();

        createTempFileImage();


    }

    private void initComponents() {
        mProfileCImg = (CircularImageView) findViewById(R.id.activity_signup_profile_img);
        mFirstnameEdt = (EditText) findViewById(R.id.activity_signup_firstname_edt);
        mLastnameEdt = (EditText) findViewById(R.id.activity_signup_lastname_edt);
        mEmailEdt = (EditText) findViewById(R.id.activity_signup_email_edt);
        mPhoneEdt = (EditText) findViewById(R.id.activity_signup_phone_edt);
        mPasswordEdt = (EditText) findViewById(R.id.activity_signup_pwd_edt);
        mConfirmPwdEdt = (EditText) findViewById(R.id.activity_signup_conpwd_edt);
        mCountryEdt = (EditText) findViewById(R.id.activity_signup_country_edt);
        mStateEdt = (EditText) findViewById(R.id.activity_signup_state_edt);
        mZipcodeEdt = (EditText) findViewById(R.id.activity_signup_zipcode_edt);
        mEmailView = (View) findViewById(R.id.activity_signup_email_view);
        mPhoneView = (View) findViewById(R.id.activity_signup_phone_view);
        activity_signup_confirm_email_edt = (EditText)findViewById(R.id.activity_signup_confirm_email_edt);
        img_left = (ImageView)findViewById(R.id.img_left);
        /*mFirstnameTv = (TextView) findViewById(R.id.tvfirstname);
        mLastnameTv = (TextView) findViewById(R.id.tvlastname);
        mEmailTv = (TextView) findViewById(R.id.tvemail);
        mPhoneTv = (TextView) findViewById(R.id.tvphone);
        mPasswordTv = (TextView) findViewById(R.id.tvpass);
        mConfirmPwdTv = (TextView) findViewById(R.id.tvconpwd);
        mCountryTv = (TextView) findViewById(R.id.tvcountry);
        mStateTv = (TextView) findViewById(R.id.tvstate);
        mZipcodeTv = (TextView) findViewById(R.id.tvzipcode);
        mEmailView = (View) findViewById(R.id.activity_signup_email_view);
        mPhoneView = (View) findViewById(R.id.activity_signup_phone_view);
        mEmailLn = (LinearLayout) findViewById(R.id.activity_signup_email_ln);
        mPhoneLn = (LinearLayout) findViewById(R.id.activity_signup_phone_ln);*/
        mHeaderBackRl = (RelativeLayout) findViewById(R.id.header_layout_back_rl);
        mHeaderTitleTv = (TextView) findViewById(R.id.txt_title);
        mRegisterBtn = (Button) findViewById(R.id.activity_signup_register_btn);
        mRegisterWIthphoneBtn = (Button) findViewById(R.id.activity_signup_registerwithphone_btn);
        country_name = new ArrayList<String>();
        countrycode = new ArrayList<String>();
        state_id = new ArrayList<>();
        state_name = new ArrayList<>();

        statelist = new ArrayList<>();

    }

    private void prepareView() {


        mFirstnameEdt.setTypeface(FontCustom.setFont(this));
        mLastnameEdt.setTypeface(FontCustom.setFont(this));
        mEmailEdt.setTypeface(FontCustom.setFont(this));
        mPhoneEdt.setTypeface(FontCustom.setFont(this));
        mPasswordEdt.setTypeface(FontCustom.setFont(this));
        mConfirmPwdEdt.setTypeface(FontCustom.setFont(this));
        mCountryEdt.setTypeface(FontCustom.setFont(this));
        mStateEdt.setTypeface(FontCustom.setFont(this));
        mZipcodeEdt.setTypeface(FontCustom.setFont(this));
        mRegisterBtn.setTypeface(FontCustom.setFont(this));
        activity_signup_confirm_email_edt.setTypeface(FontCustom.setFont(this));

       /* splashGetData = mIntent.getStringExtra("registerwithphoneemail");
        if (splashGetData.equalsIgnoreCase("0")) {
            mEmailEdt.setVisibility(View.GONE);
            mRegisterWIthphoneBtn.setText(getResources().getText(R.string.registerwithemail));
            mEmailView.setVisibility(View.GONE);
            //mEmailLn.setVisibility(View.GONE);
        } else {
            mPhoneEdt.setVisibility(View.GONE);
            mRegisterWIthphoneBtn.setText(getResources().getText(R.string.registerwithphone));
            mPhoneView.setVisibility(View.GONE);
            //mPhoneLn.setVisibility(View.GONE);
        }*/

        /*SplashActivity.setTextWatcher(SignupActivity.this, mFirstnameEdt, mFirstnameTv);
        SplashActivity.setTextWatcher(SignupActivity.this, mLastnameEdt, mLastnameTv);
        SplashActivity.setTextWatcher(SignupActivity.this, mEmailEdt, mEmailTv);
        SplashActivity.setTextWatcher(SignupActivity.this, mPhoneEdt, mPhoneTv);
        SplashActivity.setTextWatcher(SignupActivity.this, mPasswordEdt, mPasswordTv);
        SplashActivity.setTextWatcher(SignupActivity.this, mConfirmPwdEdt, mConfirmPwdTv);
        SplashActivity.setTextWatcher(SignupActivity.this, mCountryEdt, mCountryTv);
        SplashActivity.setTextWatcher(SignupActivity.this, mStateEdt, mStateTv);
        SplashActivity.setTextWatcher(SignupActivity.this, mZipcodeEdt, mZipcodeTv);*/
        mHeaderTitleTv.setText("Register");
        mHeaderBackRl.setVisibility(View.GONE);

    }

    private void setActionListener() {
        mRegisterBtn.setOnClickListener(this);
        mProfileCImg.setOnClickListener(this);
        mCountryEdt.setOnClickListener(this);
        mStateEdt.setOnClickListener(this);
        mHeaderBackRl.setOnClickListener(this);
        mRegisterWIthphoneBtn.setOnClickListener(this);
        img_left.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_signup_register_btn:

                mRegisterBtnClicked();

                break;

            case R.id.activity_signup_profile_img:
                selectImage();
                break;
            case R.id.img_left:
                finish();
                break;
            case R.id.activity_signup_country_edt:
                Log.e(TAG, "11111");
                showdialog();
                break;
            case R.id.activity_signup_state_edt:
                //callStateApi();
                showdialog1();
                break;
            case R.id.activity_signup_registerwithphone_btn:
                finish();
                if (splashGetData.equalsIgnoreCase("0")) {
                    startActivity(new Intent(SignupActivity.this, SignupActivity.class).putExtra("registerwithphoneemail", "1"));
                } else {
                    startActivity(new Intent(SignupActivity.this, SignupActivity.class).putExtra("registerwithphoneemail", "0"));
                }
                break;
            default:
                break;
        }


    }

    private void showdialog() {

        listDialog = new Dialog(SignupActivity.this);
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        listDialog.setContentView(R.layout.country_listview1);
        listDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = listDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        lp.width = width - 100;
        lp.height = height - 200;
        window.setAttributes(lp);

        listDialog.getWindow().setGravity(Gravity.CENTER);
        listDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        //there are a lot of <span id=\"IL_AD1\" class=\"IL_AD\">settings</span>, for dialog, check them all out!
        list_country = (ListView) listDialog.findViewById(R.id.countrylist);
        TextView title = (TextView) listDialog.findViewById(R.id.title);
        search = (SearchView) listDialog.findViewById(R.id.search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(SignupActivity.this);
        search.setSubmitButtonEnabled(false);
        search.setQueryHint("Search");

        list_country.setOnItemClickListener(SignupActivity.this);
        title.setVisibility(View.VISIBLE);
        title.setText("Select Country");
        // adapter = new CountryAdapter(RegisterActivity.this, country_lst);
        // list_country.setAdapter(adapter);
        CountryAdapter adapter = new CountryAdapter(SignupActivity.this, SplashActivity.countrylist);
        //list_country.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, country_name));
        list_country.setAdapter(adapter);
        error_list.get(14).setError_flag("1");
        mCountryEdt.setError(null);


        filter = adapter.getFilter();
        //now that the dialog is set up, it's time to show it
        listDialog.show();
    }

    private void showdialog1() {

        listDialog1 = new Dialog(SignupActivity.this);
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);

        listDialog1.setContentView(R.layout.country_listview1);
        listDialog1.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = listDialog1.getWindow();
        lp.copyFrom(window.getAttributes());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        lp.width = width - 100;
        lp.height = height - 200;
        window.setAttributes(lp);

        listDialog1.getWindow().setGravity(Gravity.CENTER);
        listDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        //there are a lot of <span id=\"IL_AD1\" class=\"IL_AD\">settings</span>, for dialog, check them all out!
        list_state = (ListView) listDialog1.findViewById(R.id.countrylist);
        TextView title = (TextView) listDialog1.findViewById(R.id.title);
        search = (SearchView) listDialog1.findViewById(R.id.search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(SignupActivity.this);
        search.setSubmitButtonEnabled(false);


        list_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mycode = String.valueOf(StateAdapter.stateArrayList.get(position).get("stateID"));
                if (mycode.equals(" ")) {
                    //   edt_code.setText(CountryAdapter.countryArrayList.get(position).getCountryCode().substring(0, 2));
                    state_code = StateAdapter.stateArrayList.get(position).get("stateID");
                } else {
                    //  edt_code.setText(CountryAdapter.countryArrayList.get(position).getCountryID());
                    state_code = StateAdapter.stateArrayList.get(position).get("stateID");
                }
                Log.e("code", "" + country_code + " state_code " + state_code);
                mStateEdt.setText(StateAdapter.stateArrayList.get(position).get("stateName"));
                error_list.get(15).setError_flag("1");
                mStateEdt.setError(null);

                //  txtcountry_code.setText(CountryAdapter.countryArrayList.get(arg2).getCountryCode());
                listDialog1.dismiss();

            }
        });
        title.setVisibility(View.VISIBLE);
        title.setText("Select State");

        if (statelist.size()>0) {
            Log.e("country_codesign",""+country_code);

            int code=Integer.parseInt(country_code);
            // Log.e("testsign", "state" + SplashActivity.listnew.get(code));
            StateAdapter adapter = new StateAdapter(SignupActivity.this, statelist);
            //list_country.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, country_name));
            list_state.setAdapter(adapter);
            filter = adapter.getFilter();
            //now that the dialog is set up, it's time to show it
            listDialog1.show();
        } else {
            Utils.showToast(this, "Please select country first.");
        }
    }

    private void createTempFileImage() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), Constants.TEMP_PHOTO_FILE_NAME);
        } else {
            // mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
    }


    private void mRegisterBtnClicked() {

        firstname = mFirstnameEdt.getText().toString().trim();
        lastname = mLastnameEdt.getText().toString().trim();
        email = mEmailEdt.getText().toString().trim();
        confirm_email = activity_signup_confirm_email_edt.getText().toString().trim();
        phone = mPhoneEdt.getText().toString().trim();
        password = mPasswordEdt.getText().toString().trim();
        confrimpwd = mConfirmPwdEdt.getText().toString().trim();
        country = mCountryEdt.getText().toString().trim();
        state = mStateEdt.getText().toString().trim();
        zipcode = mZipcodeEdt.getText().toString().trim();

        if(firstname.length()==0)
        {
            error_list.get(0).setError_flag("0");
            mFirstnameEdt.setError(getString(R.string.FullName_is_required));
        }else
        {
            error_list.get(0).setError_flag("1");
        }
        if(lastname.length()==0)
        {
            error_list.get(1).setError_flag("0");
            mLastnameEdt.setError(getString(R.string.LastName_is_required));
        }else
        {
            error_list.get(1).setError_flag("1");
        }

        if(email.length()==0)
        {
            error_list.get(2).setError_flag("0");
            error_list.get(3).setError_flag("1");
            mEmailEdt.setError(getString(R.string.Email_is_required));
        }else {
            if(Patterns.EMAIL_ADDRESS.matcher(mEmailEdt.getText().toString()).matches()) {
                if(email.equalsIgnoreCase(confirm_email)) {
                    error_list.get(2).setError_flag("1");
                    error_list.get(3).setError_flag("1");
                    error_list.get(6).setError_flag("1");
                }else
                {
                    error_list.get(2).setError_flag("1");
                    error_list.get(3).setError_flag("1");
                    error_list.get(6).setError_flag("0");

                    activity_signup_confirm_email_edt.setError(getString(R.string.email_is_not_match));

                }
            }else
            {
                error_list.get(2).setError_flag("1");
                error_list.get(3).setError_flag("0");
                mEmailEdt.setError(getString(R.string.Invalid_email));
            }
        }
        if(confirm_email.length()==0)
        {
            error_list.get(4).setError_flag("0");
            error_list.get(5).setError_flag("1");
            activity_signup_confirm_email_edt.setError(getString(R.string.Confirm_email_is_required));
        }else {
            if(Patterns.EMAIL_ADDRESS.matcher(activity_signup_confirm_email_edt.getText().toString()).matches()) {
                if(email.equalsIgnoreCase(confirm_email)) {

                    error_list.get(4).setError_flag("1");
                    error_list.get(5).setError_flag("1");
                    error_list.get(6).setError_flag("1");
                }else
                {
                    error_list.get(4).setError_flag("1");
                    error_list.get(5).setError_flag("1");
                    error_list.get(6).setError_flag("0");

                    activity_signup_confirm_email_edt.setError(getString(R.string.email_is_not_match));

                }
            }else
            {
                error_list.get(4).setError_flag("1");
                error_list.get(5).setError_flag("0");
                activity_signup_confirm_email_edt.setError(getString(R.string.Invalid_email));
            }
        }

        if(phone.length()==0)
        {
            error_list.get(7).setError_flag("0");
            error_list.get(8).setError_flag("1");
            mPhoneEdt.setError(getString(R.string.Phone_No_is_required));

        }else
        {
            if(phone.length()>=10&&phone.length()<=13)
            {
                error_list.get(7).setError_flag("1");
                error_list.get(8).setError_flag("1");

            }else
            {
                error_list.get(7).setError_flag("1");
                error_list.get(8).setError_flag("0");
                mPhoneEdt.setError(getString(R.string.Phone_Number_length_must_be_within));

            }

        }
        if(password.length()==0)
        {
            error_list.get(9).setError_flag("0");
            error_list.get(10).setError_flag("1");
            error_list.get(13).setError_flag("1");

            mPasswordEdt.setError(getString(R.string.Password_is_required));

        }else
        {
            if(password.length()>=6&&password.length()<=15)
            {
                if(password.equalsIgnoreCase(confrimpwd))
                {
                    error_list.get(9).setError_flag("1");
                    error_list.get(10).setError_flag("1");
                    error_list.get(13).setError_flag("1");

                }else {
                    error_list.get(9).setError_flag("1");
                    error_list.get(10).setError_flag("1");
                    error_list.get(13).setError_flag("0");
                    mConfirmPwdEdt.setError(getString(R.string.CPassword_must_be_same));
                }
            }else
            {
                error_list.get(9).setError_flag("1");
                error_list.get(10).setError_flag("0");
                error_list.get(13).setError_flag("1");

                mPasswordEdt.setError(getString(R.string.Password_must_be_withine_6_to_15));

            }

        }

        if(confrimpwd.length()==0)
        {
            error_list.get(11).setError_flag("0");
            error_list.get(12).setError_flag("1");
            error_list.get(13).setError_flag("1");

            mConfirmPwdEdt.setError(getString(R.string.Confirm_Password_is_required));

        }else
        {
            if(confrimpwd.length()>=6&&confrimpwd.length()<=15)
            {
                if(password.equalsIgnoreCase(confrimpwd))
                {
                    error_list.get(11).setError_flag("1");
                    error_list.get(12).setError_flag("1");
                    error_list.get(13).setError_flag("1");

                }else {
                    error_list.get(11).setError_flag("1");
                    error_list.get(12).setError_flag("1");
                    error_list.get(13).setError_flag("0");
                    mConfirmPwdEdt.setError(getString(R.string.CPassword_must_be_same));
                }
            }else
            {
                error_list.get(11).setError_flag("1");
                error_list.get(12).setError_flag("0");
                error_list.get(13).setError_flag("1");
                mConfirmPwdEdt.setError(getString(R.string.CPassword_must_be_withine_6_to_15));

            }

        }
        if(country_code.length()==0)
        {
            error_list.get(14).setError_flag("0");
            mCountryEdt.setError(getString(R.string.Country_is_required));
        }else
        {
            error_list.get(14).setError_flag("1");
        }
        if(state_code.length()==0)
        {
            error_list.get(15).setError_flag("0");
            mStateEdt.setError(getString(R.string.State_is_required));
        }else
        {
            error_list.get(15).setError_flag("1");
        }

        if(zipcode.length()==0)
        {
            error_list.get(16).setError_flag("0");
            error_list.get(17).setError_flag("1");
            mZipcodeEdt.setError(getString(R.string.zipcode_is_required));

        }else
        {
            if(zipcode.length()==5)
            {
                error_list.get(16).setError_flag("1");
                error_list.get(17).setError_flag("1");

            }else
            {
                error_list.get(16).setError_flag("1");
                error_list.get(17).setError_flag("0");
                mZipcodeEdt.setError(getString(R.string.Zipcode_must_be_withine_5));

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

            callRegisterApi();

        }
        else {
            //  mValidator.showtoast(error_msg);
        }


    }


    /*
      this function for call register api..

     */
    private void callRegisterApi() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("firstname", firstname);
            requestParams.put("lastname", lastname);
            requestParams.put("email", email);
            requestParams.put("mobilenumber", phone);
            requestParams.put("password", confrimpwd);
            if(file1!=null) {
                requestParams.put("profilepicture", file1);
            }
            requestParams.put("countryid", country_code);
            requestParams.put("stateid", state_code);
            requestParams.put("zipcode", zipcode);
            requestParams.put("devicetoken", Pref.getValue(SignupActivity.this, Constants.TAG_DEVICETOKEN, ""));
            requestParams.put("devicetype", "android");
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.REGISTER_URL, "register", "post", requestParams);
            Log.e("Registration", "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void success(String url, String apiName, String response) {
        Log.e(TAG, "response is " + response.toString());

        if (apiName.equalsIgnoreCase("register")) {
            try {
                JSONObject mainObj = new JSONObject(response);

                String code = mainObj.optString("code");
                if (code.equalsIgnoreCase("200")) {
                    Pref.setValue(SignupActivity.this,"login_email",mEmailEdt.getText().toString().trim());
                    Pref.setValue(SignupActivity.this,"login_password",mPasswordEdt.getText().toString().trim());
                   /* JSONObject dataObj = mainObj.getJSONObject("data");
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
                    Log.e(TAG, "country " + country);
                    Log.e(TAG, "state " + state);
                    Utils.showToast(SignupActivity.this, "success");
                    String zipcode = dataObj.optString("zipcode");

                    Pref.setValue(SignupActivity.this, Constants.IS_LOGIN, "0");
                    Pref.setValue(SignupActivity.this, Constants.TAG_PROFILE_PICTURE, profilepicture);
                    Pref.setValue(SignupActivity.this, Constants.TAG_USER_ID, id);
                    Pref.setValue(SignupActivity.this, Constants.TAG_AUTHKAY, authkey);
                    Pref.setValue(SignupActivity.this, Constants.TAG_FIRSTNAME, firstname);
                    Pref.setValue(SignupActivity.this, Constants.TAG_LASTNAME, lastname);
                    Pref.setValue(SignupActivity.this, Constants.TAG_EMAIL, email);
                    Pref.setValue(SignupActivity.this, Constants.TAG_PHONE, phone);
                    Pref.setValue(SignupActivity.this, Constants.TAG_COUNTRY_CODE, country);
                    Pref.setValue(SignupActivity.this, Constants.TAG_STATE_CODE, state);
                    Pref.setValue(SignupActivity.this, Constants.TAG_STATE_NAME, state);
                    Pref.setValue(SignupActivity.this, Constants.TAG_ZIPCODE, zipcode);
                    Pref.setValue(SignupActivity.this, Constants.TAG_COUNTRY_NAME, CountryName);
                    Pref.setValue(SignupActivity.this, Constants.TAG_STATE_NAME, Statename);
                    //Utils.showToast(this, message);
                    Pref.setValue(SignupActivity.this,"offset","0");*/

                    String message = mainObj.optString("message");
                    Utils.showToast(this, message);
                    Pref.setValue(SignupActivity.this, Constants.IS_LOGIN, "0");
                    startActivity(new Intent(SignupActivity.this, DashBoardActivity.class));
                }else if(code.equalsIgnoreCase("400"))
                {
                    String message = mainObj.optString("message");
                    Utils.showToast(this, message);
                }else {
                    String message = mainObj.optString("message");
                    Utils.showToast(this, message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            //Utils.showToast(this, message);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = null;
                Bitmap rotatedBitmap = null;
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    // downsizing image as it throws OutOfMemory Exception for larger
                    // images
                    options.inSampleSize = 8;

                    final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                            options);
                    rotatedBitmap = bitmap;
                    if (bitmap.getWidth() > bitmap.getHeight()) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    Bitmap b = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    Bitmap bmpnew = Bitmap.createScaledBitmap(b, 500, 500, false);
                    mProfileCImg.setImageBitmap(bmpnew);

                    //  image1.setImageBitmap(cut_circular_bitmap(rotatedBitmap));
                    file1 = bitmapToFile(bmpnew);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                        null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
              //  Log.v("width", bm.getWidth() + " " + bm.getHeight());
                //Log.v("image", bm + "");

                rotatedBitmap = bm;

                if (bm.getWidth() > bm.getHeight()) {
                    Matrix matrix = new Matrix();
                    //matrix.postRotate(90);
                    rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Bitmap b = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                Bitmap bmpnew = Bitmap.createScaledBitmap(b, 500, 500, false);


                mProfileCImg.setImageBitmap(bmpnew);
                file1 = bitmapToFile(rotatedBitmap);

            }

        }
    }


    public File bitmapToFile(Bitmap bmp) {
        try {
            final int REQUIRED_SIZE = 200;
            ByteArrayOutputStream bos = new ByteArrayOutputStream(REQUIRED_SIZE);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArr = bos.toByteArray();
            bos.flush();
            bos.close();

            FileOutputStream fos = openFileOutput("profile.png", Context.MODE_WORLD_WRITEABLE);
            fos.write(bArr);
            fos.flush();
            fos.close();
            File mFile = new File(getFilesDir().getAbsolutePath(), "profile.png");
            return mFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("Add Profile Picture!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    try {
                        Uri mImageCaptureUri = null;
                        String state = Environment.getExternalStorageState();
                        if (Environment.MEDIA_MOUNTED.equals(state)) {
                            mImageCaptureUri = Uri.fromFile(mFileTemp);
                        } else {
                            mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, Constants.REQUEST_CODE_TAKE_PICTURE);
                    } catch (ActivityNotFoundException e) {

                        Log.d(TAG, "cannot take picture", e);
                    }*/

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                    // start the image capture Intent
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[item].equals("Choose from Library")) {
                   /* Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image*//*");
                    startActivityForResult(photoPickerIntent, Constants.REQUEST_CODE_GALLERY);*/
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, getString(R.string.select_file)),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void startCropImage() {

        Intent intent = new Intent(SignupActivity.this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 2);

        startActivityForResult(intent, Constants.REQUEST_CODE_CROP_IMAGE);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != this.RESULT_OK) {

            return;
        }

        Bitmap bitmap;

        switch (requestCode) {

            case Constants.REQUEST_CODE_GALLERY:

                try {

                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();

                } catch (Exception e) {

                    Log.e(TAG, "Error while creating temp file", e);
                }

                break;
            case Constants.REQUEST_CODE_TAKE_PICTURE:

                startCropImage();
                break;
            case Constants.REQUEST_CODE_CROP_IMAGE:

                selectedImagePath = data.getStringExtra(CropImage.IMAGE_PATH);
                //Pref.setValue(getActivity(), Constants.TAG_PROFILE_IMAGE, selectedImagePath);
                if (selectedImagePath == null) {

                    return;
                }

                bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                mProfileCImg.setImageBitmap(bitmap);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {


        if (TextUtils.isEmpty(newText)) {

            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            filter.filter("");
        } else {

            filter.filter(newText);

        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

       /* String mycode = String.valueOf(CountryAdapter.countryArrayList.get(position).get("countryID"));
        if (mycode.equals(" ")) {
            //   edt_code.setText(CountryAdapter.countryArrayList.get(position).getCountryCode().substring(0, 2));
            country_code = CountryAdapter.countryArrayList.get(position).get("countryID");
        } else {
            //  edt_code.setText(CountryAdapter.countryArrayList.get(position).getCountryID());
            country_code = CountryAdapter.countryArrayList.get(position).get("countryID");
        }*/

        Log.e("position",""+position);
        String mycode = String.valueOf(CountryAdapter.countryArrayList.get(position).get("countryID"));
        if (mycode.equals(" ")) {
            //   edt_code.setText(CountryAdapter.countryArrayList.get(position).getCountryCode().substring(0, 2));
            country_code = CountryAdapter.countryArrayList.get(position).get("countryID");
        } else {
            //  edt_code.setText(CountryAdapter.countryArrayList.get(position).getCountryID());
            country_code = CountryAdapter.countryArrayList.get(position).get("countryID");
        }
        Log.e("code", "" + country_code);
        statelist.clear();

        new ExecuteTask().execute(country_code);
        Pref.setValue(SignupActivity.this, Constants.TAG_COUNTRY_CODE, country_code);
        mCountryEdt.setText(CountryAdapter.countryArrayList.get(position).get("countryName"));
        mStateEdt.setText("");
        //  txtcountry_code.setText(CountryAdapter.countryArrayList.get(arg2).getCountryCode());
        listDialog.dismiss();

    }

    class ExecuteTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String res = WebserviceCall.PostData(params, Constants.state_list, Constants.STATELIST_NEW_URL);
            //  Log.e("res....", "" + res);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                JSONArray jsonArray;
                jsonArray = new JSONArray(result);
                //   Log.e("jsonobject",""+jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.optString("id");
                    String country_id = jsonObject.optString("country_id");
                    String name = jsonObject.optString("name");
                    map.put("stateID", id);
                    map.put("stateName", name);
                    statelist.add(map);
                }
                //     String response_code = mainObj.optString("code");

                // JSONObject dataObj = mainObj.getJSONObject("data");

                  /*  for (int i = 0; i < dataArray.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonObject = dataArray.getJSONObject(i);
                        String stateID = jsonObject.optString("id");
                        String stateName = jsonObject.optString("name");
                        map.put("stateID", stateID);
                        map.put("stateName", stateName);
                        statelist.add(map);

                    }*/
                /*    Iterator<String> keys2 = dataObj.keys();

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


                    }*/
                // Log.e("listnew", "statelist " + listnew);

                   /* JSONArray demo = dataObj.getJSONArray("2");
                    for (int i = 0; i < demo.length(); i++) {
                        JSONObject obb = demo.getJSONObject(i);
                        Log.e(TAG, "statelist111 " + obb);

                    }*/





            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
