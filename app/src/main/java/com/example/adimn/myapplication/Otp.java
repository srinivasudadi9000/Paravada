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

public class Otp extends AppCompatActivity {
    Button bt_confirmed;
    EditText et_otp_verify;
    ImageView img_logo;
    String mobile,otp;
    TextView tv_resend;
    SharedPreferences.Editor editor;
    public static final int  REQUEST_ID_MULTIPLE_PERMISSIONS=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        tv_resend=(TextView)findViewById(R.id.tv_resend);
        img_logo=(ImageView)findViewById(R.id.img_logo);
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Otp.this ,CreateAccount.class);
                startActivity(i);
            }
        });
        givepermissionaccess();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("mobiles",0);
        editor = pref.edit();
        mobile = pref.getString("mobile","");
        System.out.println("the mobile is "+mobile);

        et_otp_verify = (EditText)findViewById(R.id.et_otp_verify);
        bt_confirmed = (Button) findViewById(R.id.bt_confirmed);


        bt_confirmed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (et_otp_verify.getText().toString().equals("")){
                    Validations.MyAlertBox(Otp.this,"Please enter OTP");
                    et_otp_verify.requestFocus();
                }else{
                   // new Otp.otp(et_otp_verify.getText().toString()).execute();
                    otp=et_otp_verify.getText().toString();
                     otp();

                }


            }
        });

        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  new Otp.Resendotp(mobile).execute();
                resend();
            }
        });
        //  Bundle bundle

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {


                String msg = intent.getStringExtra("message");
                String number = msg.replaceAll("\\D+","");
                et_otp_verify.setText(number);
                otp();
            }
        }
    };

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


    public void resend() {

        RequestQueue queue = Volley.newRequestQueue(Otp.this);

        String url = "https://www.nandikrushi.in/services/resendotp.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject jsonObject=object.getJSONObject("otpstatus");
                    String status=jsonObject.getString("status");
                    if (status.contains("1")){
                       /* String  farmerid;
                        farmerid = jsonObject.getString("farmerid");

                        SharedPreferences sharedPreferences = getSharedPreferences("LoginForm", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("moble", ph);
                        editor.putString("password", pass);
                        editor.putString("farmerID", farmerid);
                        editor.commit();*/


                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Otp.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("OTP Sent Successfully");
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                           /* startActivity(new Intent(LoginForm.this, Dashboard.class));
                            dialogInterface.dismiss();*/
                                /*Intent login = new Intent(LoginForm.this, Dashboard.class);
                                startActivity(login);
                                finish();*/
                            }
                        }).show();

                    }else {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Otp.this);
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
              //  params.put("password",pass);
              //  Log.d("mobile", ph);
              //  Log.d("password", pass);
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


  /*  private class  Resendotp extends AsyncTask<String, String,JSONObject>
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
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Otp.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("OTP Sent Successfully");
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //startActivity(new Intent(Otp.this, PersonalDetails.class));
                            dialogInterface.dismiss();
                        }
                    }).show();
                }
                else if (status.equalsIgnoreCase("-1")){

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Otp.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Incorrect OTP");
                    builder.setPositiveButton(R.string.ok, null).show();

                    //  Validations.MyAlertBox(Signup.this,"User already exist");
                }
                else {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Otp.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Enter Valid OTP");
                    builder.setPositiveButton(R.string.ok, null).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }*/

    public void otp() {

        RequestQueue queue = Volley.newRequestQueue(Otp.this);

        String url = "https://www.nandikrushi.in/services/verifyotp.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject object = new JSONObject(response);
                    JSONObject jsonObject=object.getJSONObject("otpstatus");
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Otp.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("The Mobile Number Verified Successfully");
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                           /* startActivity(new Intent(LoginForm.this, Dashboard.class));
                            dialogInterface.dismiss();*/
                                startActivity(new Intent(Otp.this, PersonalDetails.class));
                                dialogInterface.dismiss();
                                /*Intent login = new Intent(LoginForm.this, Dashboard.class);
                                startActivity(login);
                                finish();*/
                            }
                        }).show();

                    }
                    else if (status.equalsIgnoreCase("-1")){

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Otp.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Incorrect OTP");
                        builder.setPositiveButton(R.string.ok, null).show();

                        //  Validations.MyAlertBox(Signup.this,"User already exist");
                    }
                    else {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Otp.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Enter Valid OTP");
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
              //  params.put("mobile", mobile);
                params.put("otp",otp);
                params.put("mobile",mobile);
                //  params.put("password",pass);
                //  Log.d("mobile", ph);
                //  Log.d("password", pass);
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


  /*  private class  otp extends AsyncTask<String, String,JSONObject>
    {

        private ArrayList<NameValuePair> nameValuePairs;
        private JSONObject json;
        String otp;

        public otp(String otp){
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
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/verifyotp.php", 2, nameValuePairs);
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
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Otp.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("OTP Verified Successfully");
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Otp.this, PersonalDetails.class));
                            dialogInterface.dismiss();
                        }
                    }).show();
                }
                else if (status.equalsIgnoreCase("-1")){

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Otp.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Incorrect OTP");
                    builder.setPositiveButton(R.string.ok, null).show();

                    //  Validations.MyAlertBox(Signup.this,"User already exist");
                }
                else {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Otp.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Enter Valid OTP");
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
