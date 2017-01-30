package com.westwood24.connect.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import com.loopj.android.http.Base64;
import com.westwood24.connect.activity.LoginActivity;
import com.westwood24.connect.activity.SignupActivity;
import com.westwood24.connect.activity.SplashActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by innovify on 23/9/15.
 */
public class Utils {


    public static File Copy_sourceLocation;
    public static File Paste_Target_Location;
    public static File MY_IMG_DIR, Default_DIR;
    public static Uri uri;
    public static Intent pictureActionIntent = null;
    public static final int CAMERA_PICTURE = 1;
    public static final int GALLERY_PICTURE = 2;


    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public final static boolean isValidEmail(String hex) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return checkValidation(hex, EMAIL_PATTERN);
    }

    public final static boolean isValidPhone(String hex) {
        String PHONE_PATTERN = "^[+]?[0-9]{10,13}$";
        return checkValidation(hex, PHONE_PATTERN);
    }

    private static boolean checkValidation(String hex, String EMAIL_PATTERN) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher;
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public static int getPixel(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static boolean checkInternetConnection(Context context) {

        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean isOnline(Context context, boolean isDialogEnabled) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        if (isDialogEnabled) {
            showNewtworkAlert(context);
        }
        return false;
    }

    public static void showNewtworkAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Internet connection is not available.");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void loginalert(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("WestWood 24 Connect!");
        alertDialogBuilder.setMessage("Please  login first to use this function!");
        // set positive button: Yes message
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialogBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                    context.startActivity(new Intent(context, LoginActivity.class));


                }
            });
            alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                    //context.startActivity(new Intent(context, SplashActivity.class));


                }
            });
            // set negative button: No message
            alertDialogBuilder.setNegativeButton("Register", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // cancel the alert box and put a Toast to the user
                    dialog.cancel();
                    context.startActivity(new Intent(context, LoginActivity.class));
                    context.startActivity(new Intent(context, SignupActivity.class));

                }
            });
        }else {
            alertDialogBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                    context.startActivity(new Intent(context, LoginActivity.class));


                }
            });
            alertDialogBuilder.setNeutralButton("Register", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                    context.startActivity(new Intent(context, SignupActivity.class));


                }
            });
            // set negative button: No message
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // cancel the alert box and put a Toast to the user
                    dialog.cancel();
                    //context.startActivity(new Intent(context, SplashActivity.class));

                }
            });
        }
        // set neutral button: Exit the app message

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();

        //Toast.makeText(MainActivity.this,"logout",Toast.LENGTH_LONG).show();

    }
    public static void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void showAlert(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static Bitmap base64ToImage(String basepath) {
        byte[] decodedString = Base64.decode(basepath, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static String Get_Random_File_Name() {
        final Calendar c = Calendar.getInstance();
        int myYear = c.get(Calendar.YEAR);
        int myMonth = c.get(Calendar.MONTH);
        int myDay = c.get(Calendar.DAY_OF_MONTH);
        String Random_Image_Text = "" + myDay + myMonth + myYear + "_" + Math.random();
        return Random_Image_Text;
    }

    // Copy your image into specific folder
    public static File copyFile(File current_location, File destination_location) {
        Copy_sourceLocation = new File("" + current_location);
        Paste_Target_Location = new File("" + destination_location + "/" + Utils.Get_Random_File_Name() + ".jpg");

        Log.v("Purchase-File", "sourceLocation: " + Copy_sourceLocation);
        Log.v("Purchase-File", "targetLocation: " + Paste_Target_Location);
        try {
            // 1 = move the file, 2 = copy the file
            int actionChoice = 2;
            // moving the file to another directory
            if (actionChoice == 1) {
                if (Copy_sourceLocation.renameTo(Paste_Target_Location)) {
                    Log.i("Purchase-File", "Move file successful.");
                } else {
                    Log.i("Purchase-File", "Move file failed.");
                }
            }

            // we will copy the file
            else {
                // make sure the target file exists
                if (Copy_sourceLocation.exists()) {

                    InputStream in = new FileInputStream(Copy_sourceLocation);
                    OutputStream out = new FileOutputStream(Paste_Target_Location);

                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.i("copyFile", "Copy file successful.");

                } else {
                    Log.i("copyFile", "Copy file failed. Source file missing.");
                }
            }

        } catch (NullPointerException e) {
            Log.i("copyFile", "" + e);

        } catch (Exception e) {
            Log.i("copyFile", "" + e);
        }
        return Paste_Target_Location;
    }

    // 	decode your image into bitmap format
    public static Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            Log.e("decodeFile", "" + e);
        }
        return null;
    }

    // Create New Dir (folder) if not exist
    public static File Create_MY_IMAGES_DIR() {
        try {
            // Get SD Card path & your folder name
            MY_IMG_DIR = new File(Environment.getExternalStorageDirectory(), "/My_Image/");

            // check if exist
            if (!MY_IMG_DIR.exists()) {
                // Create New folder
                MY_IMG_DIR.mkdirs();
                Log.i("path", ">>.." + MY_IMG_DIR);
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Create_MY_IMAGES_DIR", "" + e);
        }
        return MY_IMG_DIR;
    }

}
