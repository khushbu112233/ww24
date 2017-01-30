package com.westwood24.connect.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.westwood24.R;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.WebserviceCall;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by aipxperts on 16/1/17.
 */
public class WelcomePageActivity extends Activity {
    WebView webview;
    Boolean youtube_webview_flage=true;
    String des="";
    String str_new;
    String  HtmlString1="";
    String YoutubeString="";
    String   EndingStatment="";
    String imageviewString="";
    TextView txt_title;
    ImageView img_left,header_layout_comment_add_see_img;
    RelativeLayout header_layout_back_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        webview= (WebView)findViewById(R.id.webview1);
        new ExecuteTask().execute();

        img_left = (ImageView)findViewById(R.id.img_left);
        img_left.setVisibility(View.GONE);
        header_layout_comment_add_see_img= (ImageView)findViewById(R.id.header_layout_comment_add_see_img);
        txt_title = (TextView)findViewById(R.id.txt_title);
        header_layout_back_rl = (RelativeLayout)findViewById(R.id.header_layout_back_rl);
        header_layout_back_rl.setVisibility(View.GONE);
        txt_title.setText("Welcome");
        header_layout_comment_add_see_img.setVisibility(View.VISIBLE);
        header_layout_comment_add_see_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomePageActivity.this, DashBoardActivity.class));
                finish();
            }
        });

    }



    class ExecuteTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  WebService.showProgress(DashboardActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            //   String res = WebService.GetDataMt4(WebService.CONTACT,"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvZGFzaGJvYXJkLmphenpyby5jb20iLCJhdWQiOiJodHRwczpcL1wvZGFzaGJvYXJkLmphenpyby5jb20iLCJpYXQiOjE0ODQ1NTc1MjUsImV4cCI6MTQ4NTE2MjMyNSwidXNlcl9pZCI6IjEwMDAwOSJ9.LViYeuL3OaT-cal3O1QulTM8Lchl8rbFqtmwzX-dWK4");
            String res = WebserviceCall.GetData(Constants.WELCOME_PAGE);
            Log.e("res....", "" + res);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                //   WebService.dismissProgress();
                JSONArray json2;
                String description = "";
                String youtube_url = "";
                String image="";

                json2 = new JSONArray(result);
                Log.e("json2",""+json2);
                for (int i = 0; i <json2.length(); i++) {

                    String slug = json2.getJSONObject(i).getString("slug");
                    if(slug.equalsIgnoreCase("welcome_content"))
                    {
                        youtube_url = json2.getJSONObject(i).getString("youtube_url");
                        description = json2.getJSONObject(i).getString("description");
                        image = json2.getJSONObject(i).getString("image");
                    }

                }

                String str = youtube_url;
                if (!str.equals("") && !str.isEmpty()) {
                    if(str.contains("?"))
                    {
                        String str1 = str.split("=")[1];

                        Log.e("str11", "" + str1);
                        if(str1.contains("&"))
                        {
                            str_new = str1.split("&")[0];
                            Log.e("str1", "" + str1);

                        }else {
                            str_new = str1;
                            Log.e("str1", "" + str1);

                        }
                    }else {
                        String str1 = str.substring(str.lastIndexOf("."));
                        Log.e("str12", "" + str1);
                        str_new = str1.split("/")[1];
                    }

                }
                HtmlString1 = "<html><head><title>Description</title></head><body  text="+"white"+">"+description;

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);

                int height = metrics.heightPixels;
                int width = (metrics.widthPixels/2)+50;
                int width1=(metrics.widthPixels/2);
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

                    imageviewString = "<a href=" + "#openPopup" + "><img   click=\"#openPopup\" src=" + image + " style=" + "width:" + 380 + ";height:" + 230 + "; margin-bottom: 10px;\"></a>"+"<p></p><p></p><p></p><p></p><p></p><p></p><p></p><p></p>";
                }else
                {
                    imageviewString = "<a href=" + "#openPopup" + "><img   click=\"#openPopup\" src=" + image + " style=" + "width:" + width + ";height:" + width1 + "; margin-bottom: 10px;\"></a>"+"<p></p><p></p><p></p><p></p><p></p><p></p><p></p><p></p>";
                }
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    YoutubeString="<iframe width=100%"+" height="+230+" class =\"youtube-player\" src=\"https://www.youtube.com/embed/"+str_new +"\" frameborder=\"0\" allowfullscreen style=\"+\"width:"+380+";height:"+230+";align=\"bottom\" ></iframe>";

                }else
                {
                    YoutubeString="<iframe width=100%"+" height="+width1+" class =\"youtube-player\" src=\"https://www.youtube.com/embed/"+str_new +"\" frameborder=\"0\" allowfullscreen style=\"+\"width:"+width+";height:"+width1+"; align=\"bottom\" ></iframe>";

                }


                // YoutubeString = "<iframe width="+200+"height="+200+"src="+"http://www.youtube.com/embed/"+str_new+" class="+"youtube-player"+" frameborder="+1+"allowfullscreen style="+"background:white; position:relative; float:left; margin-right: 10px; left:0; "+"></iframe>";
                if(!image.equalsIgnoreCase(""))
                {
                    HtmlString1 = HtmlString1.concat(imageviewString);
                }
                if(!youtube_url.equalsIgnoreCase("")){
                    HtmlString1 = HtmlString1.concat(YoutubeString);
                }
                EndingStatment=  "</body></html>";
                HtmlString1 = HtmlString1.concat(EndingStatment);
                webview.setBackgroundColor(Color.TRANSPARENT);
                //  webview.loadData(HtmlString1, "text/html", "UTF-8");

                webview.getSettings().setJavaScriptEnabled(true);
                webview.loadDataWithBaseURL("", HtmlString1, "text/html", "UTF-8", "");
                webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        youtube_webview_flage = false;


                    }
                });
                Log.e("HtmlString1", "" + HtmlString1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
