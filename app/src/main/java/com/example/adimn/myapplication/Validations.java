package com.example.adimn.myapplication;

/**
 * Created by Adimn on 25-05-2018.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import static android.content.ContentValues.TAG;

/**
 * Created by pari on 04-02-2018.
 */

public class Validations {
    private static int internetStatus;
    private static String internetType;

    public static void MyAlertBox(final Context ctx, String alert_msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);

        // set title
        alertDialogBuilder.setTitle("Farmer");

        // set dialog message
        alertDialogBuilder.setMessage(alert_msg).setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    public static boolean email(String name) {
        String spl = "!#$%^&*()+=-[]\\\';,/{}|\":<>?";
        if (name.length() > 2) {
            boolean flag = true;
            for (int i = 0; i < name.length(); i++) {
                if (spl.indexOf(name.charAt(i)) != -1) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                if (name.contains("@") && name.contains(".")) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null
                && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView()
                    .getWindowToken(), 0);
        }
    }


    @SuppressLint("LongLogTag")
    public static boolean isConnectedToInternet(Context context) {
        Log.i(TAG, "Checking Internet Connection...");
        Boolean found = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                found = true;
                internetStatus = 0;
            }
            NetworkInfo wifi = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo _3g = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifi.isConnected())
                internetType = "WiFi";

            if (_3g.isConnected())
                internetType = "3G";

        } catch (Exception e) {
            Log.e("CheckConnectivity Exception", e.getMessage(), e);
        }
        if (found)
            Log.i(TAG, "Internet Connection found.");
        else
            Log.i(TAG, "Internet Connection not found.");

        return found;
    }
}
