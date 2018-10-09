package com.example.adimn.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Map;

public class LoginForm extends AppCompatActivity {
    EditText et_phone , et_password;
    TextView tv_forgot, tv_signup;
    Button bt_login;
    String ph,pass,farmerID;
    //Typeface tfc1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginform);

        et_phone =(EditText)findViewById(R.id.et_phone);
        et_password=(EditText)findViewById(R.id.et_password);
        tv_signup=(TextView)findViewById(R.id.tv_signup);
       /* tfc1 = Typeface.createFromAsset(getAssets(),  "fonts/ParisienneRegular.ttf");
        tv_signup.setTypeface(tfc1);*/
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginForm.this ,CreateAccount.class);
                startActivity(i);
            }
        });

        tv_forgot=(TextView)findViewById(R.id.tv_forgot);
        /*tfc1 = Typeface.createFromAsset(getAssets(),  "fonts/ParisienneRegular.ttf");
        tv_forgot.setTypeface(tfc1);*/
        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent frgt= new Intent(LoginForm.this,ForgetPassword.class);
                startActivity(frgt);
            }
        });
        bt_login=(Button)findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 ph=et_phone.getText().toString();
                 pass=et_password.getText().toString();
                if (et_phone.getText() .toString().equals("")){
                    Validations.MyAlertBox(LoginForm.this,"Please Enter Phn Number");
                    et_phone.requestFocus();
                }
                else if (et_phone.getText().toString().trim().length() != 10){
                    Validations.MyAlertBox(LoginForm.this,"Please Enter Valid Mobile Number");
                }

                else if (et_password.getText().toString().trim().length() <5){
                    Validations.MyAlertBox(LoginForm.this,"Please Enter Password");
                    et_password.requestFocus();
                }
                else {
                    if (Validations.isConnectedToInternet(LoginForm.this)){
                        request();
                      //  new LoginForm.userLogin(et_phone.getText().toString(),et_password.getText().toString()).execute();
                    } else{
                        Validations.MyAlertBox(LoginForm.this,"Internet Connection Failed");
                    }


                }
                SharedPreferences pref = getApplicationContext().getSharedPreferences("LoginForm",0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("mobile",ph);
                editor.putString("password",pass);
                editor.commit();


            }
        });
    }

    public void request() {

        RequestQueue queue = Volley.newRequestQueue(LoginForm.this);

        String url = "https://www.nandikrushi.in/services/login.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject jsonObject=object.getJSONObject("logstatus");
                    String status=jsonObject.getString("status");
                    if (status.contains("1")){
                        String  farmerid;
                    farmerid = jsonObject.getString("farmerid");
                    System.out.println("farmer id" +farmerid);

                    SharedPreferences sharedPreferences = getSharedPreferences("LoginForm", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("moble", ph);
                    editor.putString("password", pass);
                    editor.putString("farmerID", farmerid);
                    editor.putString("token",jsonObject.getString("token"));
                    editor.commit();


                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginForm.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Farmer Login Successfully");
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           /* startActivity(new Intent(LoginForm.this, Dashboard.class));
                            dialogInterface.dismiss();*/
                            Intent login = new Intent(LoginForm.this, Dashboard.class);
                            startActivity(login);
                            finish();
                        }
                    }).show();

                    }else {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginForm.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Farmer Login Failed");
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
                params.put("mobile", ph);
                params.put("password",pass);
                Log.d("mobile", ph);
                Log.d("password", pass);
               // Log.d("farmerid",farmerID);
                return params;
            }
        };
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(policy);



    }


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


   /* @Override
    public void onBackPressed() {
        return;
    }*/

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



