package com.westwood24.connect.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.loopj.android.http.Base64;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.adapter.CategoryNavDrawerListAdapter;
import com.westwood24.connect.adapter.SettingWithLoginNavDrawerListAdapter;
import com.westwood24.connect.adapter.SettingWithoutLoginNavDrawerListAdapter;
import com.westwood24.connect.fragment.ChangePasswordFragment;
import com.westwood24.connect.fragment.HomeFragment;
import com.westwood24.connect.fragment.ProfileScreenFragment;
import com.westwood24.connect.fragment.TopNewsFirstFragment;
import com.westwood24.connect.fragment.WebViewForAboutusFragment;
import com.westwood24.connect.model.CategoryList;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.ImageDownloaderTask;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;
import com.westwood24.connect.utils.WebserviceCall;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DashBoardActivity extends FragmentActivity implements View.OnClickListener, WebserviceCall.WebserviceResponse, TabLayout.OnTabSelectedListener {

    //UI Object declaration...

    public static DrawerLayout mDrawerLayout;
    ListView mDrawerList_Left;
    public static FrameLayout frame_list_Left;
    public static ListView mDrawerList_Right;
    public static FrameLayout frame_list_Right;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<String> navDrawerItems;
    private SettingWithLoginNavDrawerListAdapter mSettingWithLoginadapter;
    private SettingWithoutLoginNavDrawerListAdapter mSettingWithoutLoginAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    Button mCategoryBtn;
    Button mAddPostBtn;
    Button mVideoBtn;
    EditText mNameEdt;
    EditText mTitleEdt;
    EditText mContentEdt;
    EditText mCategoryEdt;
    EditText mCommentEdt;
    ImageView mCategoryMenuImg;
    ImageView mSettingMenuImg;
    public static Context mContext;
    String TAG = DashBoardActivity.class.getSimpleName();
    String activity;
    String name, title, content, category, comment;
    // nav drawer title
    private CharSequence mDrawerTitle;
    public static Filter filter;
    // used to store app title
    private CharSequence mTitle;
    public static SearchView search;
    TopNewsFirstFragment homefragment;
    //  TopNewsFirstFragment homefragment;
    ArrayList<CategoryList> mCategoryListArrayList;
    CategoryNavDrawerListAdapter mCategoryNavDrawerListAdapter;
    RelativeLayout mHeaderMainRl;
    ImageView mHeaderCloseImg;
    RelativeLayout mHeaderBackRl;
    TextView mHeaderTitleTv;
    ImageView mMenuImg;
    Intent mIntent;
    ImageView img_left;
    //Bitmap profileImageBitmap;
    String fullname;
    String profile_update;
    ProgressDialog   progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        mContext = com.westwood24.connect.activity.DashBoardActivity.this;
        Pref.setValue(DashBoardActivity.this, "current", "dashboard");
        mTitle = mDrawerTitle = getTitle();
        mIntent = getIntent();
        //Pref.setValue(DashBoardActivity.this, Constants.IS_LOGIN, "1");
        mCategoryListArrayList = new ArrayList<>();


        initComponents();
        prepareView();
        setActionListener();
        Pref.setValue(DashBoardActivity.this, "offset_value", "0");
        homefragment = new TopNewsFirstFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homefragment).addToBackStack(null).commit();


    }

    private void initComponents() {
        //mTabLayout = (TabLayout) findViewById(R.id.activity_dash_board_tabLayout);
        //mViewPager = (ViewPager) findViewById(R.id.activity_dash_board_pager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED );

        mDrawerList_Left = (ListView) findViewById(R.id.list_slidermenu_left);
        mDrawerList_Left.setVisibility(View.GONE);
        //frame_list_Left = (FrameLayout) findViewById(R.id.frame_list_left);
        mDrawerList_Right = (ListView) findViewById(R.id.list_slidermenu_right);
        frame_list_Right = (FrameLayout) findViewById(R.id.frame_list_right);
        //mCategoryMenuImg = (ImageView) findViewById(R.id.title_bar_left_menu);
        //mSettingMenuImg = (ImageView) findViewById(R.id.title_bar_right_menu);
        //mHeaderTitleTv = (TextView) findViewById(R.id.activity_dash_title_tv);
        /*mCategoryBtn = (Button) findViewById(R.id.activity_dash_board_category_btn);
        mAddPostBtn = (Button) findViewById(R.id.activity_dash_board_addpost_btn);
        mVideoBtn = (Button) findViewById(R.id.activity_dash_board_video_btn);
        mNameEdt = (EditText) findViewById(R.id.activity_dash_board_name_edt);
        mTitleEdt = (EditText) findViewById(R.id.activity_dash_board_name_edt);
        mContentEdt = (EditText) findViewById(R.id.activity_dash_board_name_edt);
        mCategoryEdt = (EditText) findViewById(R.id.activity_dash_board_name_edt);
        mCommentEdt = (EditText) findViewById(R.id.activity_dash_board_name_edt);*/
        mHeaderMainRl = (RelativeLayout) findViewById(R.id.header_main_rl);
        mHeaderCloseImg = (ImageView) findViewById(R.id.header_layout_vote_see_img);
        mHeaderBackRl = (RelativeLayout) findViewById(R.id.header_layout_back_rl);
        mHeaderTitleTv = (TextView) findViewById(R.id.txt_title);
        mHeaderBackRl.setVisibility(View.GONE);
        mMenuImg = (ImageView) findViewById(R.id.img_left);
        mMenuImg.setVisibility(View.GONE);
        search = (SearchView) findViewById(R.id.search);

    }

    public void prepareView() {
        AutoCompleteTextView search_text = (AutoCompleteTextView) search.findViewById(search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        search.setQueryHint(Html.fromHtml("<font color = #ffffff>" + mContext.getResources().getString(R.string.type_to_search_here) + "</font>"));
        search_text.setTextColor(Color.WHITE);
        search_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.search_size));
        /*mTabLayout.addTab(mTabLayout.newTab().setText("TOP NEWS"));
        mTabLayout.addTab(mTabLayout.newTab().setText("TRENDING"));
        mTabLayout.addTab(mTabLayout.newTab().setText("VIDEOS"));
        mTabLayout.addTab(mTabLayout.newTab().setText("LATEST"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));

        TabViewPagerAdapter mTabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());

        //Adding adapter to pager
        mViewPager.setAdapter(mTabViewPagerAdapter);*/
        //mDrawerList_Left.setBackgroundColor(Color.parseColor("#F2209a8c"));
        //mDrawerList_Right.setBackgroundColor(Color.parseColor("#F2209a8c"));

       /* name = mNameEdt.getText().toString();
        title = mTitleEdt.getText().toString();
        content = mContentEdt.getText().toString();
        category = mCategoryEdt.getText().toString();
        comment = mCommentEdt.getText().toString();*/

        //mHeaderTitleTv.setTypeface(null, Typeface.NORMAL);
        mHeaderTitleTv.setAllCaps(true);
        mHeaderTitleTv.setText(getResources().getText(R.string.app_heading_name));
        mHeaderCloseImg.setVisibility(View.VISIBLE);
        mMenuImg.setImageResource(R.mipmap.categories_menu);
        mHeaderCloseImg.setImageResource(R.mipmap.setting);

        //mHeaderCloseImg.getLayoutParams().height = 50;
        //mHeaderCloseImg.getLayoutParams().width = 50;
        navDrawerItems = new ArrayList<String>();
        if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {
            navDrawerItems.add("");
            navDrawerItems.add(getString(R.string.login1));
            //navDrawerItems.add(getString(R.string.notification));
            navDrawerItems.add(getString(R.string.share_this_app));
            navDrawerItems.add(getString(R.string.rate_this_app));
            navDrawerItems.add(getString(R.string.about_us));
            navDrawerItems.add(getString(R.string.terms_of_use));
            navDrawerItems.add(getString(R.string.privacy_policy));

            mSettingWithoutLoginAdapter = new SettingWithoutLoginNavDrawerListAdapter(getApplicationContext(),
                    navDrawerItems, DashBoardActivity.this, activity);
            mDrawerList_Right.setAdapter(mSettingWithoutLoginAdapter);

        } else {


            navDrawerItems.add("");
            navDrawerItems.add("");
            navDrawerItems.add("WELCOME TO WW24CONNECT");
            navDrawerItems.add(getString(R.string.change_password));
            navDrawerItems.add(getString(R.string.share_this_app));
            navDrawerItems.add(getString(R.string.rate_this_app));
            navDrawerItems.add(getString(R.string.about_us));
            navDrawerItems.add(getString(R.string.terms_of_use));
            navDrawerItems.add(getString(R.string.privacy_policy));
            navDrawerItems.add(getString(R.string.logout));

            fullname = Pref.getValue(DashBoardActivity.this, Constants.TAG_FIRSTNAME, "") + " " + Pref.getValue(DashBoardActivity.this, Constants.TAG_LASTNAME, "");

            // setting the nav drawer list adapter
            Log.e("setting draw size",""+navDrawerItems.size());
            mSettingWithLoginadapter = new SettingWithLoginNavDrawerListAdapter(getApplicationContext(),
                    navDrawerItems, fullname, DashBoardActivity.this, activity);
            mDrawerList_Right.setAdapter(mSettingWithLoginadapter);
        }
        mDrawerList_Right.setOnItemClickListener(new SlideMenuClickListener());


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_navigation_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }

            public void onDrawerOpened(View drawerView) {


                //getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();

                if (drawerView != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
                }

                navDrawerItems = new ArrayList<String>();
                if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {


                } else {

                    profile_update=Pref.getValue(DashBoardActivity.this, "profile_update", "");
                    if(profile_update.equals("true"))
                    {
                        new GetDataExecuteTask().execute(Pref.getValue(DashBoardActivity.this, Constants.TAG_AUTHKAY, ""));

                    }
                    else {
                        if (Pref.getValue(DashBoardActivity.this, "from_profile", "").equals("true")) {
                            navDrawerItems.add("");
                            navDrawerItems.add("");
                            navDrawerItems.add("WELCOME TO WW24CONNECT");
                            navDrawerItems.add(getString(R.string.change_password));
                            navDrawerItems.add(getString(R.string.share_this_app));
                            navDrawerItems.add(getString(R.string.rate_this_app));
                            navDrawerItems.add(getString(R.string.about_us));
                            navDrawerItems.add(getString(R.string.terms_of_use));
                            navDrawerItems.add(getString(R.string.privacy_policy));
                            navDrawerItems.add(getString(R.string.logout));

                            fullname = Pref.getValue(DashBoardActivity.this, Constants.TAG_FIRSTNAME, "") + " " + Pref.getValue(DashBoardActivity.this, Constants.TAG_LASTNAME, "");

                            // setting the nav drawer list adapter
                            Log.e("setting draw size", "" + navDrawerItems.size());
                            mSettingWithLoginadapter = new SettingWithLoginNavDrawerListAdapter(getApplicationContext(),
                                    navDrawerItems, fullname, DashBoardActivity.this, activity);
                            mDrawerList_Right.setAdapter(mSettingWithLoginadapter);
                            Pref.setValue(DashBoardActivity.this, "from_profile", "false");
                        }
                    }
                }
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }

    private void setActionListener() {
        //mCategoryBtn.setOnClickListener(this);
        //mAddPostBtn.setOnClickListener(this);
        //mVideoBtn.setOnClickListener(this);
       /* itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemCalendar.setOnClickListener(this);
        itemSettings.setOnClickListener(this);*/

        //Adding onTabSelectedListener to swipe views
        //mTabLayout.setOnTabSelectedListener(this);

        mMenuImg.setOnClickListener(this);
        mHeaderCloseImg.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.img_left:
                if (Pref.getValue(DashBoardActivity.this, "current", "").equals("dashboard")) {
                    //   mDrawerLayout.openDrawer(Gravity.LEFT);
                } else {
                    mHeaderMainRl.setVisibility(View.GONE);
                    startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
                }
                break;
            case R.id.header_layout_vote_see_img:
                mHeaderBackRl.setVisibility(View.GONE);

                mDrawerLayout.openDrawer(Gravity.RIGHT);

                break;


        }


    }


    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_drawer, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        //   boolean drawerOpen = mDrawerLayout.isDrawerOpen(Gravity.START);
        //  menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    /*private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;

        if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {
            switch (position) {


                case 0:

                    break;

                case 1:

                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {

                        fragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.LEFT);


                    } else {
                        if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {
                            loginalert();
                        } else {

                        }
                    }
                    break;


                case 2:

                    //Utils.showToast(DashBoardActivity.this, "111");
                    String shareBody = "https://play.google.com/store/apps/details?id=westwood";

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "WestWood 24 Connect (Open it in Google Play Store to Download the Application)");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    break;
                case 3:

                    Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                    }

                    break;
                case 4:
                    //Utils.showToast(DashBoardActivity.this, "333");
                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {


                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("menu", "aboutus");
                        fragment = new WebViewForAboutusFragment();

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
                        mMenuImg.setImageResource(R.mipmap.back_btn);
                        mHeaderTitleTv.setText("ABOUT US");
                        mHeaderCloseImg.setVisibility(View.GONE);
                    }
                    break;
                case 5:
                    //Utils.showToast(DashBoardActivity.this, "444");
                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {


                    } else {

                        Bundle bundle = new Bundle();
                        bundle.putString("menu", "terms");
                        fragment = new WebViewForAboutusFragment();

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);

                        mMenuImg.setImageResource(R.mipmap.back_btn);
                        mHeaderTitleTv.setText("TERMS OF USE");
                        mHeaderCloseImg.setVisibility(View.GONE);
                    }

                    break;

                case 6:
                    //Utils.showToast(DashBoardActivity.this, "555");
                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {


                    } else {

                        Bundle bundle = new Bundle();
                        bundle.putString("menu", "privacy");
                        fragment = new WebViewForAboutusFragment();

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);

                        mMenuImg.setImageResource(R.mipmap.back_btn);
                        mHeaderTitleTv.setText("PRIVACY POLICY");
                        mHeaderCloseImg.setVisibility(View.GONE);
                    }

                    break;
                case 7:
                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {


                    } else {
                        if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {
                            loginalert();
                        } else {
                            logoutalert();
                        }
                    }

                    break;

                default:
                    break;
            }
        } else {
            switch (position) {


                case 0:

                    break;

                case 1:

                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {

                        fragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.LEFT);


                    } else {

                    }
                    break;
                case 2:
                    fragment = new ChangePasswordFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                    //mCategoryMenuImg.setImageDrawable(getDrawable(R.mipmap.back_btn));
                    //mHeaderTitleTv.setText("CHANGE PASSWORD");
                    //mSettingMenuImg.setVisibility(View.GONE);
                    mMenuImg.setImageResource(R.mipmap.back_btn);
                    mHeaderTitleTv.setText("CHANGE PASSWORD");
                    mHeaderCloseImg.setVisibility(View.GONE);
                    break;
                case 3:

                    //Utils.showToast(DashBoardActivity.this, "111");
                    String shareBody = "https://play.google.com/store/apps/details?id=westwood";

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "WestWood 24 Connect (Open it in Google Play Store to Download the Application)");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    break;
                case 4:

                    Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                    }

                    break;
                case 5:
                    //Utils.showToast(DashBoardActivity.this, "333");
                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {


                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("menu", "aboutus");
                        fragment = new WebViewForAboutusFragment();

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
                        mMenuImg.setImageResource(R.mipmap.back_btn);
                        mHeaderTitleTv.setText("ABOUT US");
                        mHeaderCloseImg.setVisibility(View.GONE);
                    }
                    break;
                case 6:
                    //Utils.showToast(DashBoardActivity.this, "444");
                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {


                    } else {

                        Bundle bundle = new Bundle();
                        bundle.putString("menu", "terms");
                        fragment = new WebViewForAboutusFragment();

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);

                        mMenuImg.setImageResource(R.mipmap.back_btn);
                        mHeaderTitleTv.setText("TERMS OF USE");
                        mHeaderCloseImg.setVisibility(View.GONE);
                    }

                    break;

                case 7:
                    //Utils.showToast(DashBoardActivity.this, "555");
                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {


                    } else {

                        Bundle bundle = new Bundle();
                        bundle.putString("menu", "privacy");
                        fragment = new WebViewForAboutusFragment();

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);

                        mMenuImg.setImageResource(R.mipmap.back_btn);
                        mHeaderTitleTv.setText("PRIVACY POLICY");
                        mHeaderCloseImg.setVisibility(View.GONE);
                    }

                    break;
                case 8:
                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {


                    } else {
                        if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {
                            loginalert();
                        } else {
                            logoutalert();
                        }
                    }

                    break;

                default:
                    break;
            }
        }


    }*/

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;

        switch (position) {

            case 0:
                Log.e("left drawer", "000");
                break;

            case 1:

                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    Log.e("left drawer", "111");
                   /* fragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Log.e("first", "click");*/

                } else {
                    if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {


                        startActivity(new Intent(DashBoardActivity.this, LoginActivity.class));

                    } else {
                        Intent intent=new Intent(DashBoardActivity.this,WelcomePageActivity.class);
                        startActivity(intent);

                            mDrawerLayout.closeDrawer(Gravity.RIGHT);
                        //mCategoryMenuImg.setImageDrawable(getDrawable(R.mipmap.back_btn));
                        //mHeaderTitleTv.setText("CHANGE PASSWORD");
                        //mSettingMenuImg.setVisibility(View.GONE);

                        mHeaderCloseImg.setVisibility(View.GONE);

                        mMenuImg.setVisibility(View.VISIBLE);
                        mMenuImg.setImageResource(R.mipmap.left_arrow2);
                        mMenuImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mDrawerLayout.openDrawer(Gravity.RIGHT);

                                // startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
                            }
                        });
                    }
                }
                break;
            case 2:

                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    Log.e("left drawer", "222");

                } else {
                    if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {

                        //Utils.showToast(DashBoardActivity.this, "111");
                        String shareBody = "https://play.google.com/store/apps/details?id=westwood";

                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "WestWood 24 Connect (Open it in Google Play Store to Download the Application)");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));

                    } else {
                        Intent intent=new Intent(DashBoardActivity.this,WelcomePageActivity.class);
                        startActivity(intent);

                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
                        //mCategoryMenuImg.setImageDrawable(getDrawable(R.mipmap.back_btn));
                        //mHeaderTitleTv.setText("CHANGE PASSWORD");
                        //mSettingMenuImg.setVisibility(View.GONE);

                        mHeaderCloseImg.setVisibility(View.GONE);

                        mMenuImg.setVisibility(View.VISIBLE);
                        mMenuImg.setImageResource(R.mipmap.left_arrow2);
                        mMenuImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mDrawerLayout.openDrawer(Gravity.RIGHT);

                                // startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
                            }
                        });
                    }
                }
                break;
            case 3:

                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    Log.e("left drawer", "222");

                } else {
                    if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {

                        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                        }


                    } else {
                        Pref.setValue(DashBoardActivity.this, "open_drawer", "true");
                        fragment = new ChangePasswordFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
                        //mCategoryMenuImg.setImageDrawable(getDrawable(R.mipmap.back_btn));
                        //mHeaderTitleTv.setText("CHANGE PASSWORD");
                        //mSettingMenuImg.setVisibility(View.GONE);
                        mHeaderTitleTv.setText("CHANGE PASSWORD");
                        mHeaderCloseImg.setVisibility(View.GONE);

                        mMenuImg.setVisibility(View.VISIBLE);
                        mMenuImg.setImageResource(R.mipmap.left_arrow2);
                        mMenuImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mDrawerLayout.openDrawer(Gravity.RIGHT);

                                // startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
                            }
                        });
                    }
                }
                break;

            case 4:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {

                    Log.e("left drawer", "444");
                } else {
                    if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {

                        Pref.setValue(DashBoardActivity.this, "open_drawer", "true");
                        Bundle bundle = new Bundle();
                        bundle.putString("menu", "aboutus");
                        fragment = new WebViewForAboutusFragment();

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
                        mHeaderTitleTv.setText("ABOUT US");
                        mHeaderCloseImg.setVisibility(View.GONE);
                        mMenuImg.setImageResource(R.mipmap.left_arrow2);
                        mMenuImg.setVisibility(View.VISIBLE);
                        mMenuImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mDrawerLayout.openDrawer(Gravity.RIGHT);

                                // startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
                            }
                        });


                    } else {
                        //Utils.showToast(DashBoardActivity.this, "111");
                        String shareBody = "https://play.google.com/store/apps/details?id=westwood";

                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "WestWood 24 Connect (Open it in Google Play Store to Download the Application)");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    }
                }

                break;
            case 5:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {


                } else {
                    if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {
                        Pref.setValue(DashBoardActivity.this, "open_drawer", "true");
                        Bundle bundle = new Bundle();
                        bundle.putString("menu", "terms");
                        fragment = new WebViewForAboutusFragment();

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);

                        mMenuImg.setImageResource(R.mipmap.left_arrow2);
                        mMenuImg.setVisibility(View.VISIBLE);
                        mMenuImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mDrawerLayout.openDrawer(Gravity.RIGHT);

                                // startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
                            }
                        });

                        mHeaderTitleTv.setText("TERMS OF USE");
                        mHeaderCloseImg.setVisibility(View.GONE);



                    } else {
                        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                        }
                    }

                }
                break;
            case 6:
                //Utils.showToast(DashBoardActivity.this, "333");
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {


                } else {


                    if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {
                        Pref.setValue(DashBoardActivity.this, "open_drawer", "true");

                        Bundle bundle = new Bundle();
                        bundle.putString("menu", "privacy");
                        fragment = new WebViewForAboutusFragment();

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);

                        mMenuImg.setImageResource(R.mipmap.left_arrow2);
                        mMenuImg.setVisibility(View.VISIBLE);
                        mMenuImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mDrawerLayout.openDrawer(Gravity.RIGHT);

                                // startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
                            }
                        });

                        mHeaderTitleTv.setText("PRIVACY POLICY");
                        mHeaderCloseImg.setVisibility(View.GONE);


                    } else {
                        Pref.setValue(DashBoardActivity.this, "open_drawer", "true");
                        Bundle bundle = new Bundle();
                        bundle.putString("menu", "aboutus");
                        fragment = new WebViewForAboutusFragment();

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
                        mMenuImg.setImageResource(R.mipmap.left_arrow2);
                        mMenuImg.setVisibility(View.VISIBLE);
                        mMenuImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mDrawerLayout.openDrawer(Gravity.RIGHT);

                                // startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
                            }
                        });

                        mHeaderTitleTv.setText("ABOUT US");
                        mHeaderCloseImg.setVisibility(View.GONE);
                    }


                }
                break;
            case 7:
                //Utils.showToast(DashBoardActivity.this, "444");
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {


                } else {


                    if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {

                                 } else {
                        Pref.setValue(DashBoardActivity.this, "open_drawer", "true");
                        Bundle bundle = new Bundle();
                        bundle.putString("menu", "terms");
                        fragment = new WebViewForAboutusFragment();

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);

                        mMenuImg.setImageResource(R.mipmap.left_arrow2);
                        mMenuImg.setVisibility(View.VISIBLE);
                        mMenuImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mDrawerLayout.openDrawer(Gravity.RIGHT);

                                // startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
                            }
                        });

                        mHeaderTitleTv.setText("TERMS OF USE");
                        mHeaderCloseImg.setVisibility(View.GONE);
                    }


                }

                break;

            case 8:
                //Utils.showToast(DashBoardActivity.this, "555");
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {


                } else {

                    if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {

                        loginalert();
                    } else {
                        Pref.setValue(DashBoardActivity.this, "open_drawer", "true");
                        Bundle bundle = new Bundle();
                        bundle.putString("menu", "privacy");
                        fragment = new WebViewForAboutusFragment();

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
                        mMenuImg.setImageResource(R.mipmap.left_arrow2);
                        mMenuImg.setVisibility(View.VISIBLE);
                        mMenuImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mDrawerLayout.openDrawer(Gravity.RIGHT);

                                // startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
                            }
                        });

                        mHeaderTitleTv.setText("PRIVACY POLICY");
                        mHeaderCloseImg.setVisibility(View.GONE);
                    }


                }

                break;
            case 9:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {


                } else {
                    if (Pref.getValue(DashBoardActivity.this, Constants.TAG_USER_ID, "").equals("")) {

                    } else {
                        logoutalert();
                    }
                }

                break;

            default:
                break;
        }


    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    public void loginalert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashBoardActivity.this);
        alertDialogBuilder.setTitle("WestWood 24 Connect!");
        alertDialogBuilder.setMessage("Please login first for use this functionality!");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
                startActivity(new Intent(DashBoardActivity.this, LoginActivity.class));


            }
        });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // cancel the alert box and put a Toast to the user
                dialog.cancel();
                startActivity(new Intent(DashBoardActivity.this, SplashActivity.class));

            }
        });
        // set neutral button: Exit the app message

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();

        //Toast.makeText(MainActivity.this,"logout",Toast.LENGTH_LONG).show();

    }


    public void logoutalert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashBoardActivity.this);
        alertDialogBuilder.setTitle("WestWood 24 Connect!");
        alertDialogBuilder.setMessage("Are you sure want to logout ?");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                callLogoutAPI();
                dialog.cancel();


            }
        });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // cancel the alert box and put a Toast to the user
                dialog.cancel();

            }
        });
        // set neutral button: Exit the app message

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();

        //Toast.makeText(MainActivity.this,"logout",Toast.LENGTH_LONG).show();

    }

    public void callfragment() {

        Fragment fragment = null;
        fragment = new ProfileScreenFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
        mDrawerLayout.closeDrawer(Gravity.RIGHT);

        mMenuImg.setImageResource(R.mipmap.back_btn);
        mHeaderTitleTv.setText("EDIT PROFILE");
        mHeaderCloseImg.setVisibility(View.GONE);
        mHeaderBackRl.setVisibility(View.GONE);
        mMenuImg.setImageResource(R.mipmap.left_arrow2);
        mMenuImg.setVisibility(View.VISIBLE);
        mMenuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.openDrawer(Gravity.RIGHT);

                // startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
            }
        });
    }

    public void callHomefragment() {
        /*  Fragment fragment = null;
        fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
        mDrawerLayout.closeDrawer(Gravity.LEFT);*/

       /* finish();
        startActivity(mIntent);*/
/*

        homefragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homefragment).addToBackStack(null).commit();
        mDrawerLayout.closeDrawer(Gravity.LEFT);
*/

        //mMenuImg.setImageResource(R.mipmap.back_btn);
        //mHeaderTitleTv.setText("EDIT PROFILE");
        // mHeaderCloseImg.setVisibility(View.GONE);
    }

    /**
     * this funcation forl logout....
     */

    private void callLogoutAPI() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("authkey", Pref.getValue(DashBoardActivity.this, Constants.TAG_AUTHKAY, ""));
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.LOGOUT_URL, "logout", "post", requestParams);
            Log.e(TAG, "request is logout" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
          this function for call category api
         */
    private void callCategoryAPI() {
        try {
            RequestParams requestParams = new RequestParams();
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.CATEGORY_LIST_URL, "category", "get", requestParams);
            Log.e(TAG, "request is cat" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



        /*
          this function for call addpost api
         */

    private void callAddPostAPI() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("title", title);
            requestParams.put("content", content);
            requestParams.put("category", category);
            requestParams.put("id", "6");
            WebserviceCall webserviceCall = new WebserviceCall(this);
            webserviceCall.setWebserviceResponse(this);
            webserviceCall.callWebservice(Constants.ADDPOST_URL, "addpost", "post", requestParams);
            Log.e(TAG, "request is" + requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void success(String response_url, String apiName, String response) {
        Log.e(TAG, "response " + response);
        Log.e(TAG, "response url " + response_url);
        if (apiName.equals("category")) {

       /*     try {
                JSONObject mainObj = new JSONObject(response);
                //  String status = mainObj.optString("status");
                String message = mainObj.optString("message");
                String response_code = mainObj.optString("code");

                if (response_code.equals("200")) {
                    //    JSONObject dataObj = mainObj.getJSONObject("data");
                    JSONArray categoryJsonArray = mainObj.getJSONArray("data");

                    CategoryList[] categoryLists = new CategoryList[categoryJsonArray.length()];
                    for (int i = 0; i < categoryJsonArray.length(); i++) {
                        JSONObject categoryObj = categoryJsonArray.getJSONObject(i);
                        categoryLists[i] = new CategoryList();
                        //categoryLists[0].setCid("0");
                      //  categoryLists[0].setCategoryname("HOME");
                        categoryLists[i].setCid(categoryObj.optString("id"));
                        categoryLists[i].setCategoryname(categoryObj.optString("name"));

                        mCategoryListArrayList.add(categoryLists[i]);
                        mCategoryNavDrawerListAdapter = new CategoryNavDrawerListAdapter(DashBoardActivity.this, mCategoryListArrayList, DashBoardActivity.this);
                        mDrawerList_Left.setAdapter(mCategoryNavDrawerListAdapter);
                        filter = mCategoryNavDrawerListAdapter.getFilter();
                        search.setIconifiedByDefault(false);
                        search.setSubmitButtonEnabled(true);
                        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {

                                try {
                                    Log.e("newText", "" + newText);

                                    if (TextUtils.isEmpty(newText)) {

                                        filter = mCategoryNavDrawerListAdapter.getFilter();
                                        filter.filter("");
                                    } else {
                                        filter.filter(newText);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                        });
                        search.setOnCloseListener(new SearchView.OnCloseListener() {
                            @Override
                            public boolean onClose() {
                                return false;
                            }
                        });

                        mDrawerList_Left.setOnItemClickListener(new SlideMenuClickListener());

                    }
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        } else if (apiName.equals("logout")) {
            Log.e(TAG, "response for logout" + response);
            try {
                JSONObject mainObj = new JSONObject(response);
                String message = mainObj.optString("message");
                String code = mainObj.optString("code");

                if (code.equals("200")) {
                    Pref.deleteAll(DashBoardActivity.this);
                    startActivity(new Intent(DashBoardActivity.this, SplashActivity.class));
                    Utils.showToast(this, message);
                } else if(code.equals("1000")){
                    Pref.deleteAll(DashBoardActivity.this);
                    mDrawerLayout.closeDrawers();
                    startActivity(new Intent(DashBoardActivity.this, SplashActivity.class));
                    Utils.showToast(this, message);
                }else
                {
                    Utils.showToast(this, message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e(TAG, "response for addpost " + response);
        }
    }

    @Override
    public void failure(String url, String apiName, String response) {
        JSONObject mainObj = null;
        try {
            mainObj = new JSONObject(response);
            if(apiName.equals("logout")) {
                Pref.deleteAll(DashBoardActivity.this);
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(DashBoardActivity.this, SplashActivity.class));
                String message = mainObj.optString("message");
                Utils.showToast(this, message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Pref.setValue(DashBoardActivity.this, "offset", "0");

        if(Pref.getValue(DashBoardActivity.this, "open_drawer", "").equals("true")) {
            mDrawerLayout.openDrawer(Gravity.RIGHT);
            Pref.setValue(DashBoardActivity.this, "open_drawer", "false");
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

       /* if (Pref.getValue(DashBoardActivity.this, "current", "").equalsIgnoreCase("changepassword")) {


            FragmentManager fm = getSupportFragmentManager();

            if (fm.getBackStackEntryCount() > 0) {
                for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                    fm.popBackStack();
                    //finishAffinity();
                }
            }

        } else if (Pref.getValue(DashBoardActivity.this, "current", "").equalsIgnoreCase("aboutus")) {
            getSupportFragmentManager().popBackStack();

        } else if (Pref.getValue(DashBoardActivity.this, "current", "").equalsIgnoreCase("terms")) {
            getSupportFragmentManager().popBackStack();

        } else if (Pref.getValue(DashBoardActivity.this, "current", "").equalsIgnoreCase("privacy")) {
            getSupportFragmentManager().popBackStack();

        } else if (Pref.getValue(DashBoardActivity.this, "current", "").equalsIgnoreCase("profile")) {
            getSupportFragmentManager().popBackStack();

        } else {

            finishAffinity();
        }*/

        if (Pref.getValue(DashBoardActivity.this, "current", "").equals("dashboard")) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                finish();
                startActivity(mIntent);
            } else if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                finish();
                startActivity(mIntent);
            } else {
                finishAffinity();
            }
        } else {

            mHeaderMainRl.setVisibility(View.GONE);
            startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
        }
    }



    class GetDataExecuteTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
               progressDialog = new ProgressDialog(DashBoardActivity.this);
            //progressDialog = new SpotsDialog(context, R.style.Custom);
            //  progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String res = WebserviceCall.PostData(params, Constants.authkey, Constants.GET_USER_LIVE);
            //  Log.e("res....", "" + res);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                progressDialog.cancel();
                JSONObject mainObj = new JSONObject(result);

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

                    Pref.setValue(DashBoardActivity.this, Constants.IS_LOGIN, "1");
                    Pref.setValue(DashBoardActivity.this, Constants.TAG_PROFILE_PICTURE, profilepicture);
                    Pref.setValue(DashBoardActivity.this, Constants.TAG_USER_ID, id);
                    // Pref.setValue(context, Constants.TAG_AUTHKAY, authkey);
                    Pref.setValue(DashBoardActivity.this, Constants.TAG_FIRSTNAME, firstname);
                    Pref.setValue(DashBoardActivity.this, Constants.TAG_LASTNAME, lastname);
                    Pref.setValue(DashBoardActivity.this, Constants.TAG_EMAIL, email);
                    Pref.setValue(DashBoardActivity.this, Constants.TAG_PHONE, phone);
                    Pref.setValue(DashBoardActivity.this, Constants.TAG_COUNTRY_CODE, country);
                    Pref.setValue(DashBoardActivity.this, Constants.TAG_STATE_CODE, state);
                    Pref.setValue(DashBoardActivity.this, Constants.TAG_STATE_NAME, state);
                    Pref.setValue(DashBoardActivity.this, Constants.TAG_ZIPCODE, zipcode);
                    Pref.setValue(DashBoardActivity.this, Constants.TAG_COUNTRY_NAME, CountryName);
                    Pref.setValue(DashBoardActivity.this, Constants.TAG_STATE_NAME, Statename);

                    Pref.setValue(DashBoardActivity.this, "user_type", "login");
                    Pref.setValue(DashBoardActivity.this, "user_login_register", "login");
                    Pref.setValue(DashBoardActivity.this, "profile_update", "false");
                    fullname = Pref.getValue(DashBoardActivity.this, Constants.TAG_FIRSTNAME, "") + " " + Pref.getValue(DashBoardActivity.this, Constants.TAG_LASTNAME, "");

                    navDrawerItems.add("");
                    navDrawerItems.add("");
                    navDrawerItems.add("WELCOME TO WW24CONNECT");
                    navDrawerItems.add(getString(R.string.change_password));
                    navDrawerItems.add(getString(R.string.share_this_app));
                    navDrawerItems.add(getString(R.string.rate_this_app));
                    navDrawerItems.add(getString(R.string.about_us));
                    navDrawerItems.add(getString(R.string.terms_of_use));
                    navDrawerItems.add(getString(R.string.privacy_policy));
                    navDrawerItems.add(getString(R.string.logout));
                    // setting the nav drawer list adapter
                    Log.e("setting draw size", "" + navDrawerItems.size());
                    mSettingWithLoginadapter = new SettingWithLoginNavDrawerListAdapter(getApplicationContext(),
                            navDrawerItems, fullname, DashBoardActivity.this, activity);
                    mDrawerList_Right.setAdapter(mSettingWithLoginadapter);
                    Pref.setValue(DashBoardActivity.this, "from_profile", "false");


                }else if (code.equals("400"))
                {
                    Utils.showToast(DashBoardActivity.this, message);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
