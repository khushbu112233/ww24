package com.westwood24.connect.fragment;

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
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.westwood24.R;
import com.westwood24.connect.activity.DashBoardActivity;
import com.westwood24.connect.activity.SplashActivity;
import com.westwood24.connect.activity.WelcomePageActivity;
import com.westwood24.connect.adapter.CountryAdapter;
import com.westwood24.connect.adapter.StateAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ProfileScreenFragment extends Fragment implements View.OnClickListener, WebserviceCall.WebserviceResponse, SearchView.OnQueryTextListener, SearchView.OnCloseListener, AdapterView.OnItemClickListener {

    CircularImageView mProfileCImg;
    EditText mFirstnameEdt;
    EditText mLastnameEdt;
    EditText mCountryEdt;
    EditText mStateEdt;
    EditText mZipcodeEdt;
    EditText mPhoneEdt;
    ImageView mEditPickImg;
    Button mUpdateProfileBtn;
    FieldsValidator mValidator;
    RelativeLayout mHeaderBackRl;
    TextView mHeaderTitleTv;
    TextView mHeaderBackTitleRl;
    //varialble declaration...
    String TAG = "ProfileScreenActivity";
    View mRootView;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    int REQUEST_CAMERA = 0;
    Bitmap rotatedBitmap;
    int SELECT_FILE = 1;
    File file1;
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
    Context context;
    public  static ArrayList<HashMap<String,String>> statelist;
    String profile_update;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        mRootView = inflater.inflate(R.layout.fragment_profile_screen, container, false);
        DashBoardActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Pref.setValue(getActivity(), "current", "profile");
        mValidator = new FieldsValidator(getActivity());
        context = getActivity();
        //callCountryApi();
        initComponents();

        prepareView();
        setActionListener();
        return mRootView;
    }


    private void initComponents() {
        mProfileCImg = (CircularImageView) mRootView.findViewById(R.id.fragment_profile_profile_img);
        mFirstnameEdt = (EditText) mRootView.findViewById(R.id.fragment_profile_firstname_edt);
        mLastnameEdt = (EditText) mRootView.findViewById(R.id.fragment_profile_lastname_edt);
        mCountryEdt = (EditText) mRootView.findViewById(R.id.fragment_profile_country_edt);
        mStateEdt = (EditText) mRootView.findViewById(R.id.fragment_profile_state_edt);
        mZipcodeEdt = (EditText) mRootView.findViewById(R.id.fragment_profile_zipcode_edt);
        mEditPickImg = (ImageView) mRootView.findViewById(R.id.fragment_profile_pick_img);
        mPhoneEdt = (EditText) mRootView.findViewById(R.id.fragment_profile_phone_edt);
        /*mHeaderBackRl = (RelativeLayout) mRootView.findViewById(R.id.header_layout_back_rl);
        mHeaderTitleTv = (TextView) mRootView.findViewById(R.id.txt_title);
        mHeaderBackTitleRl = (TextView) mRootView.findViewById(R.id.txt_title_left);*/
        mUpdateProfileBtn = (Button) mRootView.findViewById(R.id.fragment_profile_updateprofile_btn);
        country_name = new ArrayList<String>();
        countrycode = new ArrayList<String>();
        state_id = new ArrayList<>();
        state_name = new ArrayList<>();
        statelist = new ArrayList<>();
    }

    private void prepareView() {


        if (!Pref.getValue(getActivity(), Constants.TAG_PROFILE_PICTURE, "").isEmpty()) {
            int width = Utils.getPixel(100, getActivity());
            Log.e("image url", "" + "https://ww24connect.com" + Pref.getValue(getActivity(), Constants.TAG_PROFILE_PICTURE, ""));


            Picasso.with(getActivity()).load("https://ww24connect.com"+Pref.getValue(getActivity(), Constants.TAG_PROFILE_PICTURE, "")).resize(width, width).error(R.mipmap.ic_launcher).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(mProfileCImg);
        }
        mFirstnameEdt.setText(Pref.getValue(getActivity(), Constants.TAG_FIRSTNAME, ""));
        mLastnameEdt.setText(Pref.getValue(getActivity(), Constants.TAG_LASTNAME, ""));
        mCountryEdt.setText(Pref.getValue(getActivity(), Constants.TAG_COUNTRY_NAME, ""));
        mStateEdt.setText(Pref.getValue(getActivity(), Constants.TAG_STATE_NAME, ""));
        mZipcodeEdt.setText(Pref.getValue(getActivity(), Constants.TAG_ZIPCODE, ""));
        mPhoneEdt.setText(Pref.getValue(getActivity(), Constants.TAG_PHONE, ""));

        country_code=Pref.getValue(getActivity(), Constants.TAG_COUNTRY_CODE, "");
        state_code=Pref.getValue(getActivity(), Constants.TAG_STATE_CODE, "");
        Log.v("state_code",state_code+"");
        //mHeaderBackTitleRl.setText("Home");
        //mHeaderTitleTv.setText("Profile");


        mFirstnameEdt.setTypeface(FontCustom.setFont(getActivity()));
        mLastnameEdt.setTypeface(FontCustom.setFont(getActivity()));
        mCountryEdt.setTypeface(FontCustom.setFont(getActivity()));
        mStateEdt.setTypeface(FontCustom.setFont(getActivity()));
        mZipcodeEdt.setTypeface(FontCustom.setFont(getActivity()));
        mPhoneEdt.setTypeface(FontCustom.setFont(getActivity()));
        mUpdateProfileBtn.setTypeface(FontCustom.setFont(getActivity()));

        if(country_code.length()>0)
        {
            new ExecuteTask().execute(country_code);
        }

        mProfileCImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void setActionListener() {
        mEditPickImg.setOnClickListener(this);
        mUpdateProfileBtn.setOnClickListener(this);
        mCountryEdt.setOnClickListener(this);
        mStateEdt.setOnClickListener(this);
        //mHeaderBackRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_profile_pick_img:
                selectImage();
                break;

            case R.id.fragment_profile_updateprofile_btn:
                mUpdateProfilebtnClicked();
                break;
            case R.id.fragment_profile_country_edt:

                showdialog();
                break;
            case R.id.fragment_profile_state_edt:
                //callStateApi();
                showdialog1();
                break;
        }
       /* if (v.getId() == R.id.header_layout_back_rl) {
            startActivity(new Intent(getActivity(), DashBoardActivity.class));
        }*/
    }


    private void showdialog() {

        listDialog = new Dialog(getActivity());
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        listDialog.setContentView(R.layout.country_listview1);
        listDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = listDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
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

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setSubmitButtonEnabled(false);


        list_country.setOnItemClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("Select Country");
        // adapter = new CountryAdapter(RegisterActivity.this, country_lst);
        // list_country.setAdapter(adapter);
        CountryAdapter adapter = new CountryAdapter(getActivity(), SplashActivity.countrylist);
        //list_country.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, country_name));
        list_country.setAdapter(adapter);
        filter = adapter.getFilter();
        //now that the dialog is set up, it's time to show it
        listDialog.show();
    }

    private void showdialog1() {

        listDialog1 = new Dialog(getActivity());
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);

        listDialog1.setContentView(R.layout.country_listview1);
        listDialog1.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = listDialog1.getWindow();
        lp.copyFrom(window.getAttributes());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
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

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
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
                Log.e("code", "" + country_code);
                mStateEdt.setText(StateAdapter.stateArrayList.get(position).get("stateName"));
                //  txtcountry_code.setText(CountryAdapter.countryArrayList.get(arg2).getCountryCode());
                listDialog1.dismiss();

            }
        });
        title.setVisibility(View.VISIBLE);
        title.setText("Select State");
        country_code = Pref.getValue(context, Constants.TAG_COUNTRY_CODE, "");
        Log.e("code", "" + country_code);
        if (statelist.size()>0) {
            //if(country_code.length()>0)


                StateAdapter adapter = new StateAdapter(getActivity(), statelist);
                //list_country.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, country_name));
                list_state.setAdapter(adapter);
                filter = adapter.getFilter();
                //now that the dialog is set up, it's time to show it
                listDialog1.show();

            }else{
                Utils.showToast(getActivity(), "Please select country first.");
            }
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
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
                Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null,
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
                Log.v("width", bm.getWidth() + " " + bm.getHeight());
                Log.v("image", bm + "");

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

            FileOutputStream fos = getActivity().openFileOutput("profile.png", Context.MODE_WORLD_WRITEABLE);
            fos.write(bArr);
            fos.flush();
            fos.close();
            File mFile = new File(getActivity().getFilesDir().getAbsolutePath(), "profile.png");
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                .validateNotEmpty(mPhoneEdt,
                        getString(R.string.Phone_No_is_required));
        isCorrect = isCorrect
                && mValidator
                .validateLength(mPhoneEdt, 10, 13,
                        getString(R.string.Phone_Number_length_must_be_within));

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

        isCorrect = isCorrect
                && mValidator
                .validateMinLength(mZipcodeEdt, 5,
                        getString(R.string.Zipcode_must_be_withine_5));

        if (!isCorrect) {
            //    scrollview.fullScroll(ScrollView.FOCUS_UP);
            return;
        }

        callUpdateProfileApi();
    }

    private void callUpdateProfileApi() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("authkey", Pref.getValue(getActivity(), Constants.TAG_AUTHKAY, ""));
            if (file1 == null) {
                requestParams.put("profilepicture", Pref.getValue(getActivity(), Constants.TAG_PROFILE_PICTURE, ""));
            } else {
                requestParams.put("profilepicture", file1);
            }
            requestParams.put("firstname", mFirstnameEdt.getText().toString().trim());
            requestParams.put("lastname", mLastnameEdt.getText().toString().trim());
            requestParams.put("mobilenumber", mPhoneEdt.getText().toString().trim());
            if (country_code.equals("")) {
                requestParams.put("countryid", Pref.getValue(getActivity(), Constants.TAG_COUNTRY_CODE, ""));
            } else {
                requestParams.put("countryid", country_code);
            }
            requestParams.put("stateid", state_code);
          /*  if (state_code.equals("")) {
                requestParams.put("stateid", Pref.getValue(getActivity(), Constants.TAG_STATE_CODE, ""));
            } else {
                requestParams.put("stateid", state_code);
            }*/
            requestParams.put("zipcode", mZipcodeEdt.getText().toString().trim());
            requestParams.put("userid", Pref.getValue(getActivity(), Constants.TAG_USER_ID, ""));
            requestParams.put("devicetoken", Pref.getValue(getActivity(), Constants.TAG_DEVICETOKEN, ""));
            requestParams.put("devicetype", "android");

            WebserviceCall webserviceCall = new WebserviceCall(getActivity());
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
                Log.e(TAG, "country " + country);
                Log.e(TAG, "state " + state);
                String zipcode = dataObj.optString("zipcode");

                Pref.setValue(getActivity(), Constants.IS_LOGIN, "1");
                Pref.setValue(getActivity(), Constants.TAG_PROFILE_PICTURE, profilepicture);
                Pref.setValue(getActivity(), Constants.TAG_USER_ID, id);
              //  Pref.setValue(getActivity(), Constants.TAG_AUTHKAY, authkey);
                Pref.setValue(getActivity(), Constants.TAG_FIRSTNAME, firstname);
                Pref.setValue(getActivity(), Constants.TAG_LASTNAME, lastname);
                Pref.setValue(getActivity(), Constants.TAG_EMAIL, email);
                Pref.setValue(getActivity(), Constants.TAG_PHONE, phone);
                Pref.setValue(getActivity(), Constants.TAG_COUNTRY_CODE, country);
                Pref.setValue(getActivity(), Constants.TAG_STATE_CODE, state);
                Pref.setValue(getActivity(), Constants.TAG_ZIPCODE, zipcode);
                Pref.setValue(getActivity(), Constants.TAG_COUNTRY_NAME, CountryName);
                Pref.setValue(getActivity(), Constants.TAG_STATE_NAME, Statename);
                Utils.showToast(getActivity(), "Update profile successfully.");
                Pref.setValue(context, "open_drawer","true");
                Intent i = new Intent(getActivity(),DashBoardActivity.class);
                startActivity(i);
                getActivity().finish();
            } else if(code.equalsIgnoreCase("1000"))
            {
                Pref.deleteAll(context);
                DashBoardActivity.mDrawerLayout.closeDrawers();
                startActivity(new Intent(context, SplashActivity.class));
                Utils.showToast(context, message);
                Pref.setValue(context, "user_type", "guest");
            } else {
                Utils.showToast(getActivity(), message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failure(String url, String apiName, String response) {

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
                Log.e(TAG, "country " + country);
                Log.e(TAG, "state " + state);
                String zipcode = dataObj.optString("zipcode");

                Pref.setValue(getActivity(), Constants.IS_LOGIN, "1");
                Pref.setValue(getActivity(), Constants.TAG_PROFILE_PICTURE, profilepicture);
                Pref.setValue(getActivity(), Constants.TAG_USER_ID, id);
               // Pref.setValue(getActivity(), Constants.TAG_AUTHKAY, authkey);
                Pref.setValue(getActivity(), Constants.TAG_FIRSTNAME, firstname);
                Pref.setValue(getActivity(), Constants.TAG_LASTNAME, lastname);
                Pref.setValue(getActivity(), Constants.TAG_EMAIL, email);
                Pref.setValue(getActivity(), Constants.TAG_PHONE, phone);
                Pref.setValue(getActivity(), Constants.TAG_COUNTRY_CODE, country);
                Pref.setValue(getActivity(), Constants.TAG_STATE_NAME, state);
                Pref.setValue(getActivity(), Constants.TAG_ZIPCODE, zipcode);
                Pref.setValue(getActivity(), Constants.TAG_COUNTRY_NAME, CountryName);
                Pref.setValue(getActivity(), Constants.TAG_STATE_NAME, Statename);
                Utils.showToast(getActivity(), "Update profile successfully!");

                Intent i = new Intent(getActivity(),DashBoardActivity.class);
                startActivity(i);
                getActivity().finish();
            } else if(code.equalsIgnoreCase("1000"))
            {
                Pref.deleteAll(context);
                DashBoardActivity.mDrawerLayout.closeDrawers();
                startActivity(new Intent(context, SplashActivity.class));
                Utils.showToast(context, message);
                Pref.setValue(context, "user_type", "guest");
            } else {
                Utils.showToast(getActivity(), message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            filter.filter("");
        } else {

            filter.filter(newText);

        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        Pref.setValue(getActivity(), Constants.TAG_COUNTRY_CODE, country_code);
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

    @Override
    public void onResume() {
        super.onResume();
        Pref.setValue(context,"from_profile","true");
    }

    

}
