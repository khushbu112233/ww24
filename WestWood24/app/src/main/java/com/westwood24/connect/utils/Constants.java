package com.westwood24.connect.utils;

import org.jsoup.Jsoup;

/**
 * Created by dharmesh dudhat on 4/8/16.
 */
public class Constants {


    //URL for first v1 data
    public static final String BASE_URL = "https://ww24connect.com/index.php/api/v1/";


    public static final String CHANGE_PWD_URL = BASE_URL + "changepassword";

    public static final String CATEGORY_URL = BASE_URL + "category";
    public static final String ADDPOST_URL = BASE_URL + "addposts";
    public static final String COUNTRY_URL = BASE_URL + "countries";
    public static final String STATE_URL = BASE_URL + "states";

    public static final String ALL_ARTICLES_URL = BASE_URL + "getallarticles";
    public static final String ARTICLECOMMENT_URL = BASE_URL + "getarticlescomment";
    public static final String SENDUSER_COMMENT_URL = BASE_URL + "addarticlescomment";
    public static final String GETVOTE_OF_QUE_URL = BASE_URL + "getvotequestion";
    public static final String GETARTICLE_DETAILS_URL = BASE_URL + "getarticledetails";
    public static final String SETVOTE_OF_QUE_URL_ = BASE_URL + "addvote";

    public static final String RESET_PWD_URL = BASE_URL + "resetpassword";
    public static final String CATEGORYWISEARTICLE_URL = BASE_URL + "categorywisearticles";



    //URL for new changes...

   // public static final String BASE_URL1 = "http://ww24connect.com/new/app_dev.php/api/";

    //live
   public static final String BASE_URL1 = "https://ww24connect.com/api/";

   //public static final String BASE_URL1 ="http://192.168.1.118/ww24connectlive/api/";

    public static final String COUNTRYLIST_URL = BASE_URL1 + "countrylist";
    public static final String STATELIST_URL = BASE_URL1 + "stateslist";
    public static final String STATELIST_NEW_URL = BASE_URL1 + "androidstateslist";

    public static final String LOGIN_URL = BASE_URL1 + "login";
    public static final String REGISTER_URL = BASE_URL1 + "register";
    public static final String FOROGOT_PWD_URL = BASE_URL1 + "forgotpassword";
    public static final String UPDATE_PROFILE_URL = BASE_URL1 + "updateprofile";
    public static final String GET_PROFILE_PIC_URL = BASE_URL1 + "getprofilepic";
    public static final String LOGOUT_URL = BASE_URL1 + "logout";
    public static final String CHANGE_PASSWORD_URL = BASE_URL1 + "changepassword";
    public static final String GET_ARTICLE_DETAILS_URL = BASE_URL1 + "getarticledetails";
    public static final String CATEGORY_LIST_URL = BASE_URL1 + "categorylist";
    public static final String GET_ARTICLES_BY_CATEGORY_URL = BASE_URL1 + "getarticleslistbycategory";
    public static final String GET_ARTICLES_LIST_URL = BASE_URL1 + "getarticleslist";
    public static final String GET_ARTICLE_COMMENT_URL = BASE_URL1 + "getarticlecomment";
    public static final String ADD_ARTICLES_COMMENT_URL = BASE_URL1 + "addarticlecomment";
    public static final String GET_VOTE_QUESTION_URL = BASE_URL1 + "getvotequestion";
    public static final String SETVOTE_OF_QUE_URL = BASE_URL1 + "addvote";
    public static final String ADDVIEWER_OF_QUE_URL = BASE_URL1 + "article/increase";
    public static final String WELCOME_PAGE = BASE_URL1+"dynamicContent";
    public static final String GET_USER_LIVE = BASE_URL1 + "getuserlive";

    //Other constant String value---
    public static final String PREF_DEVICE_ID = "PREF_DEVICE_ID";


    //Crop image
    public static final String TEMP_PHOTO_FILE_NAME = "profile_photo.jpg";

    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;


    //Preference value store for constant

    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String GUEST_IS_LOGIN = "GUEST_IS_LOGIN";
    public static final String TAG_USER_ID = "TAG_USER_ID";
    public static final String TAG_AUTHKAY = "TAG_AUTHKAY";
    public static final String TAG_FIRSTNAME = "TAG_FIRSTNAME";
    public static final String TAG_LASTNAME = "TAG_LASTNAME";
    public static final String TAG_EMAIL = "TAG_EMAIL";
    public static final String TAG_PHONE = "TAG_PHONE";
    public static final String TAG_COUNTRY_CODE = "TAG_COUNTRY_CODE";
    public static final String TAG_STATE_CODE = "TAG_STATE_CODE";
    public static final String TAG_COUNTRY_NAME = "TAG_COUNTRY_NAME";
    public static final String TAG_STATE_NAME = "TAG_STATE_NAME";
    public static final String TAG_ZIPCODE = "TAG_ZIPCODE";
    public static final String TAG_DEVICETYPE = "TAG_DEVICETYPE";
    public static final String TAG_DEVICETOKEN = "TAG_DEVICETOKEN";

    public static final String TAG_PROFILE_PICTURE = "TAG_PROFILE_PICTURE";
    public static final String TAG_VERIFICATION_CODE = "TAG_VERIFICATION_CODE";
    public static String html2text(String html) { return Jsoup.parse(html).text(); }

    public static final String[] state_list={
            "countryId"
    };

    public static final String[] authkey={
            "authkey"
    };

}
