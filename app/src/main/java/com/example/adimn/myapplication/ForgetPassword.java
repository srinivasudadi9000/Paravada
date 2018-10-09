package com.example.adimn.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgetPassword extends AppCompatActivity {

    EditText et_phone;
    Button bt_submit;
    ImageView img_logo;
    TextView tv_receiveotp;
    Typeface tfc1;
    String mobile;
    int request=1;
    String request_id= Integer.toString(request);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        img_logo=(ImageView)findViewById(R.id.img_logo);
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(ForgetPassword.this ,LoginForm.class);
                startActivity(j);
            }
        });

        tv_receiveotp=(TextView)findViewById(R.id.tv_receiveotp);
        tfc1 = Typeface.createFromAsset(getAssets(),  "fonts/oxygenlight.ttf");
        tv_receiveotp.setTypeface(tfc1);

        et_phone = (EditText)findViewById(R.id.et_phone);
        bt_submit = (Button) findViewById(R.id.bt_submit);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile=et_phone.getText().toString();
                if (et_phone.getText().toString().equals("")){
                    Validations.MyAlertBox(ForgetPassword.this,"Please enter Mobile Number");
                }
                else if (et_phone.getText().toString().equals("<10")){
                    Validations.MyAlertBox(ForgetPassword.this,"Please Enter Correct Mobile Number");

                }
                else {


                    if (Validations.isConnectedToInternet(ForgetPassword.this)){

                        forgot();

                        //  new LoginForm.userLogin(et_phone.getText().toString(),et_password.getText().toString()).execute();
                    } else{
                        Validations.MyAlertBox(ForgetPassword.this,"Internet Connection Failed");

                    }
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("forgot",0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("mobile",et_phone.getText().toString());
                    editor.commit();

                   // new ForgetPassword.forgotpassword(et_phone.getText().toString()).execute();
                    //System.out.println("before intent");

                }
            }
        });

    }



    public void forgot() {

        RequestQueue queue = Volley.newRequestQueue(ForgetPassword.this);

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

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ForgetPassword.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("OTP has been successfuly sent to your mobile number");
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(ForgetPassword.this, VerifyOtp.class));
                            dialogInterface.dismiss();
                               /* Intent login = new Intent(ForgetPassword.this, VerifyOtp.class);
                                startActivity(login);
                                finish();*/
                            }
                        }).show();

                    }else {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ForgetPassword.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("You mobile was not registered with us (or) Registered as another account");
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
                params.put("request_id",request_id);
               Log.d("mobile", mobile);
               Log.d("request_id", request_id);
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
