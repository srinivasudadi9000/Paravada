package com.example.adimn.myapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.adimn.myapplication.Otp.REQUEST_ID_MULTIPLE_PERMISSIONS;


public class VerifyOtp extends AppCompatActivity {

    EditText et_otp;
    Button bt_Confirm;
    String mobile;
    ImageView img_logo;
    TextView tv_resend;
    public static final int  REQUEST_ID_MULTIPLE_PERMISSIONS=1;
    String phn,otp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        tv_resend=(TextView)findViewById(R.id.tv_resend);
        img_logo=(ImageView)findViewById(R.id.img_logo);
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VerifyOtp.this ,ForgetPassword.class);
                startActivity(i);
            }
        });
        givepermissionaccess();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("forgot",0);
        mobile = pref.getString("mobile","No Mobile was stored");

        et_otp = (EditText)findViewById(R.id.et_otp);
        bt_Confirm = (Button)findViewById(R.id.bt_Confirm);

        bt_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_otp.getText().toString().equals("")){
                    Validations.MyAlertBox(VerifyOtp.this,"Please enter OTP");
                    et_otp.requestFocus();
                }else{
                  /*  new VerifyOtp.forgotpasswordotp(et_otp.getText().toString()).execute();*/
                    verifyotp();
                }

            }
        });
        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*new VerifyOtp.Resendotp(mobile).execute();*/
                resendotp();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED ) {
            //resume tasks needing this permission
            givepermissionaccess();
            //  Toast.makeText(getBaseContext(), "Resume Task needing this permission", Toast.LENGTH_SHORT).show();
        } else {
            //finish();
            Toast.makeText(getBaseContext(), "you can not use this application without givivng access to ur location Thanks!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void givepermissionaccess() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS,Manifest.permission.READ_PHONE_STATE}, 0);
        } else {
            Toast.makeText(getBaseContext(), "All permissions granted.", Toast.LENGTH_SHORT).show();
//            givepermissionaccess();
        }

    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                 String message = intent.getStringExtra("message");
                String number = message.replaceAll("\\D+","");
                et_otp.setText(number);
               verifyotp();
            }
        }
    };

    public void resendotp() {

        int req=1;
        final String request= Integer.toString(req);

        RequestQueue queue = Volley.newRequestQueue(VerifyOtp.this);

        String url = "https://www.nandikrushi.in/services/forgotpassword.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject object = new JSONObject(response);
                    JSONObject jsonObject=object.getJSONObject("forgotstatus");
                    String status=jsonObject.getString("status");
                    if (status.contains("1"))
                    {
                        // String  farmerid=  jsonObject.getString("farmerid");

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VerifyOtp.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("OTP has been successfuly sent to your mobile number");
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(VerifyOtp.this, VerifyOtp.class));
                                dialogInterface.dismiss();
                               /* Intent login = new Intent(ForgetPassword.this, VerifyOtp.class);
                                startActivity(login);
                                finish();*/
                            }
                        }).show();

                    }else {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VerifyOtp.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Entered Mobile was NOT Registered");
                        builder.setPositiveButton(R.string.ok, null).show();
                    }




                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mprocessingdialog.dismiss();

                // Snackbar.make(linear, "Error in Connection", Snackbar.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobile);
                params.put("request_id",request);
               // Log.d("mobile", mobile);
                //Log.d("request_id", request_id);
                return params;
            }
        };
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);



    }


    public void verifyotp() {

        otp = et_otp.getText().toString();
        int request=2;
        final String request_id= Integer.toString(request);

        RequestQueue queue = Volley.newRequestQueue(VerifyOtp.this);

        String url = "https://www.nandikrushi.in/services/forgotpassword.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject jsonObject=object.getJSONObject("forgotstatus");
                    String status=jsonObject.getString("status");
                    if (status.contains("1")){

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VerifyOtp.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("OTP Verified Successfully");
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                           /* startActivity(new Intent(LoginForm.this, Dashboard.class));
                            dialogInterface.dismiss();*/
                                Intent login = new Intent(VerifyOtp.this, ResetPassword.class);
                                startActivity(login);
                                finish();
                            }
                        }).show();

                    }
                    else if (status.equalsIgnoreCase("0")) {

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VerifyOtp.this);
                        builder.setTitle("Nandi Krushi...!");
                        builder.setMessage("OTP Enter Doesn't Match, Please Enter Correct OTP ");
                        builder.setPositiveButton(R.string.ok, null).show();

                        //Log.d("otp",data.getString("otp").toString());
                    }
                    else {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VerifyOtp.this);
                        builder.setTitle("Nandi Krushi...!");
                        builder.setMessage("Please Enter Valid OTP ");
                        builder.setPositiveButton(R.string.ok, null).show();

                    }



                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mprocessingdialog.dismiss();

                // Snackbar.make(linear, "Error in Connection", Snackbar.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                //params.put("mobile", phn);
                params.put("otp",otp);
                params.put("request_id", request_id);
                //Log.d("password", pass);
                return params;
            }
        };
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);



    }


   /* private class  Resendotp extends AsyncTask<String, String,JSONObject>
    {

        private ArrayList<NameValuePair> nameValuePairs;
        private JSONObject json;
        String mobile;

        public Resendotp(String mobile){
            this.mobile= mobile;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            nameValuePairs = new ArrayList<NameValuePair>();
            //System.out.println("mobile is "+mobile);
            nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/resendotp.php", 2, nameValuePairs);
            // System.out.println("return json object is "+json);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            String status = "1";
            //  super.onPostExecute(jsonObject);
            //  Toast.makeText(getBaseContext(),jsonObject.toString(), Toast.LENGTH_SHORT).show();
            try {
                JSONArray jsonArray = new JSONArray();
                JSONObject data = jsonObject.getJSONObject("otpstatus");
                status = data.getString("status");
                if (status.equalsIgnoreCase("1"))  {
                    // Toast.makeText(getBaseContext(),data.toString(), Toast.LENGTH_SHORT).show();

                  *//*  Log.d("otp",data.getString("otp").toString());
                    Log.d("mobile",data.getString("mobile").toString());*//*
                    *//*Intent signup = new Intent(Otp.this, Filter.class);
                    startActivity(signup);
                    finish();*//*
                    overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VerifyOtp.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("OTP sent Successfully");
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //startActivity(new Intent(VerifyOtp.this, ResetPassword.class));
                            dialogInterface.dismiss();
                        }
                    }).show();
                }
                else if (status.equalsIgnoreCase("-1")){

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VerifyOtp.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Incorrect OTP");
                    builder.setPositiveButton(R.string.ok, null).show();

                    //  Validations.MyAlertBox(Signup.this,"User already exist");
                }
                else {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VerifyOtp.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Enter Valid OTP");
                    builder.setPositiveButton(R.string.ok, null).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }*/








   /* private class  forgotpasswordotp extends AsyncTask<String, String,JSONObject>
    {

        private ArrayList<NameValuePair> nameValuePairs;
        private JSONObject json;
        String otp;
        int request=2;
        String request_id= Integer.toString(request);

        public forgotpasswordotp(String otp){
            this.otp= otp;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("otp", otp));
            nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
            nameValuePairs.add(new BasicNameValuePair("request_id",request_id));
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/forgotpassword.php", 2, nameValuePairs);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            String status = "1";
            //  super.onPostExecute(jsonObject);
            //  Toast.makeText(getBaseContext(),jsonObject.toString(), Toast.LENGTH_SHORT).show();
            try {
                JSONArray jsonArray = new JSONArray();
                JSONObject data = jsonObject.getJSONObject("forgotstatus");
                status = data.getString("status");
                if (status.equalsIgnoreCase("1")) {
                    // JSONObject data = new JSONObject(result.getString("message"));

                  //  Toast.makeText(getBaseContext(),data.toString(), Toast.LENGTH_SHORT).show();

                   *//* Log.d("otp",data.getString("otp").toString());
                    Log.d("mobile",data.getString("mobile").toString());
                    Log.d("request_id",data.getString("request_id").toString());*//*

                    overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VerifyOtp.this);
                    builder.setTitle("NAndi Krushi...!");
                    builder.setMessage("OTP Verified Successfully");
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent signup = new Intent(VerifyOtp.this, ResetPassword.class);
                            startActivity(signup);
                            finish();
                        }
                    }).show();
                }
                else if (status.equalsIgnoreCase("0")) {

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VerifyOtp.this);
                    builder.setTitle("Nandi Krushi...!");
                    builder.setMessage("OTP Enter Doesn't Match, Please Enter Correct OTP ");
                    builder.setPositiveButton(R.string.ok, null).show();

                    //Log.d("otp",data.getString("otp").toString());
                }
                else {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VerifyOtp.this);
                    builder.setTitle("Nandi Krushi...!");
                    builder.setMessage("Please Enter Valid OTP ");
                    builder.setPositiveButton(R.string.ok, null).show();

                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }*/

    //******* HIDING KEYBOARD **********
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null
                && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && v instanceof EditText
                && !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop()
                    || y > v.getBottom())
                Validations.hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}


