package com.westwood24.connect.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;


/**
 * Created by user on 5/5/15.
 */
public class WebserviceCall {
    AsyncHttpClient asyncHttpClient;
    WebserviceResponse webserviceResponse;
    Context context;
    String url;
    //SpotsDialog progressDialog;
    ProgressDialog progressDialog;
    String response = "";

    public WebserviceCall(Context context) {
        this.context = context;
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("Accept-Encoding", "gzip");
        asyncHttpClient.setTimeout(60000);
        progressDialog = new ProgressDialog(context);
        //progressDialog = new SpotsDialog(context, R.style.Custom);
        //  progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
    }

    public void callWebservice(final String url, final String apiName, String type, final RequestParams requestParams) {
        this.url = url;
        if (isInternetConnected()) {
            if (type.equalsIgnoreCase("post")) {
                asyncHttpClient.post(context, url, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        try {
                            Log.v("reqparams",requestParams.toString());
                            progressDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                        try {
                            response = new String(bytes);
                            progressDialog.dismiss();
                            Log.e("Success Response", response);
                            webserviceResponse.success(url, apiName, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable error) {
                        try {
                            progressDialog.dismiss();
                            //  Utils.showAlert(context, "Alert!", getError(statusCode));
                            if (response != null) {
                                response = new String(bytes);
                                Log.e("Success Response", response);
                                //MaterialUtils.showLongToast(context, getError(statusCode));
                                webserviceResponse.failure(url, apiName, response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }



                });

            } else {
                asyncHttpClient.get(context, url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        try {
                            progressDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                        try {

                            response = new String(bytes);
                            progressDialog.dismiss();
                            webserviceResponse.success(url, apiName, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable error) {
                        try {
                            progressDialog.dismiss();
                            //Utils.showAlert(context, "Alert!", getError(statusCode));
                            if (response != null) {
                                response = new String(bytes);
                                Log.e("Success Response", response);
                                //MaterialUtils.showLongToast(context, getError(statusCode));
                                webserviceResponse.failure(url, apiName, response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                });
            }
        } else {
            try {
                Toast.makeText(context, "Internet Connection Unavailable", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void callWebserviceWithoutLoader(final String url, final String apiName, String type, RequestParams requestParams) {
        this.url = url;
        if (isInternetConnected()) {
            if (type.equalsIgnoreCase("post")) {
                asyncHttpClient.post(context, url, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                        try {
                            response = new String(bytes);
                            Log.e("Success Response", response);
                            webserviceResponse.success(url, apiName, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                        try {
                            progressDialog.dismiss();
                            //  Utils.showAlert(context, "Alert!", getError(statusCode));
                            if (response != null) {
                                response = new String(bytes);
                                Log.e("Success Response", response);
                                //MaterialUtils.showLongToast(context, getError(statusCode));
                                webserviceResponse.failure(url, apiName, response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } else {
                asyncHttpClient.get(context, url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                        try {
                            response = new String(bytes);
                            webserviceResponse.success(url, apiName, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                        try {
                            progressDialog.dismiss();
                            //    Utils.showAlert(context, "Alert!", getError(statusCode));
                            if (response != null) {
                                response = new String(bytes);
                                Log.e("Success Response", response);
                                //MaterialUtils.showLongToast(context, getError(statusCode));
                                webserviceResponse.failure(url, apiName, response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } else {
            try {
                Toast.makeText(context, "Internet Connection Unavailable", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void setWebserviceResponse(WebserviceResponse webserviceResponse) {
        this.webserviceResponse = webserviceResponse;
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

    public String getError(int errorCode) {
        String error = "";

        switch (errorCode) {
           /* case 400:
                error = "Bad Request";
                break;*/
            case 401:
                error = "Unauthorized";
                break;
            case 404:
                error = "Not Found";
                break;
            case 408:
                error = "Request Timeout";
                break;
            case 415:
                error = "Unsupported Media Type";
                break;
            case 500:
                error = "Internal Server Error";
                break;
            case 502:
                error = "Bad Gateway";
                break;
            case 503:
                error = "Service Unavailable";
                break;
            case 504:
                error = "Gateway Timeout";
                break;
            /*default:
                error = "Please check your internet connection and try again later.";
                break;*/
        }
        return error;
    }

    public interface WebserviceResponse {
        void success(String url, String apiName, String response);

        void failure(String url, String apiName, String response);
    }
    public static String GetData(String url) { InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();

            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;


    }
    public static String PostData(String[] valuse,String[] values,String url) {
        String s="";
        try
        {
            cz.msebera.android.httpclient.client.HttpClient httpClient=new cz.msebera.android.httpclient.impl.client.DefaultHttpClient();
            cz.msebera.android.httpclient.client.methods.HttpPost httpPost=new cz.msebera.android.httpclient.client.methods.HttpPost(url);

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            for (int i =0;i<valuse.length;i++) {
                //     list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(values[i], URLEncoder.encode(valuse[i], "UTF-8")));
                list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(values[i],valuse[i]));

            }

            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            //   httpPost.addHeader("Authorization","Bearer "+ value);
            httpPost.setEntity(new cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity(list,"utf-8"));
            cz.msebera.android.httpclient.HttpResponse httpResponse=  httpClient.execute(httpPost);

            cz.msebera.android.httpclient.HttpEntity httpEntity=httpResponse.getEntity();
            s= readResponse(httpResponse);
            Log.e("s",""+s);
        }
        catch(Exception exception)  {}
        return s;


    }
    public static  String readResponse(cz.msebera.android.httpclient.HttpResponse res) {
        InputStream is=null;
        String return_text="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            return_text=sb.toString();
        } catch (Exception e)
        {

        }
        return return_text;

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}