package com.example.adimn.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View;
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

public class CreateAccount extends AppCompatActivity {
    EditText et_mobile;
    Button bt_nexts;
    ImageView img_logo;
    CheckBox chk;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        img_logo=(ImageView)findViewById(R.id.img_logo);
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(CreateAccount.this ,LoginForm.class);
                startActivity(j);
            }
        });

     /*   String whatsapp;
        CheckBox chk = (CheckBox) findViewById(R.id.chk);
        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String whatsapp;
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    // Do your coding
                    whatsapp="1";
                    Toast.makeText(getApplicationContext(), "The number has whatsapp", Toast.LENGTH_LONG).show();
                }
                else{
                    // Do your coding
                    whatsapp="2";
                    Toast.makeText(getApplicationContext(), "whatsapp not exit to this number", Toast.LENGTH_LONG).show();
                }
            }
        });
*/

        et_mobile = (EditText)findViewById(R.id.et_mobile);
        bt_nexts = (Button) findViewById(R.id.bt_nexts);

        bt_nexts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = et_mobile.getText().toString();
                if (et_mobile.getText().toString().equals("")){
                    Validations.MyAlertBox(CreateAccount.this,"Please enter Mobile Number");
                }

                else if (et_mobile.getText().toString().trim().length() != 10){
                    Validations.MyAlertBox(CreateAccount.this,"Please Enter Valid Mobile Number");
                }
                else {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("mobiles",0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("mobile",mobile);
                    editor.commit();
                    //new CreateAccount.createaccount(et_mobile.getText().toString()).execute();
                    createaccount();
                    /*Toast.makeText(getApplicationContext(), "OTP has sent to your mobile", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(CreateAccount.this,Otp.class);
                    startActivity(i);*/
                }
            }
        });
    }


    public void createaccount() {



        RequestQueue queue = Volley.newRequestQueue(CreateAccount.this);

        String url = "https://www.nandikrushi.in/services/sendotp.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                   // JSONArray jsonArray = new JSONArray();
                    JSONObject object = new JSONObject(response);
                    JSONObject jsonObject=object.getJSONObject("otpstatus");
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1"))
                    {
                        // String  farmerid=  jsonObject.getString("farmerid");

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateAccount.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("OTP has been successfuly sent to your mobile number");
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(CreateAccount.this,Otp.class));
                                dialogInterface.dismiss();
                               /* Intent login = new Intent(ForgetPassword.this, VerifyOtp.class);
                                startActivity(login);
                                finish();*/
                            }
                        }).show();

                    }else if (status.equalsIgnoreCase("0")) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateAccount.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Entered Mobile was Already Registered");
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
               // params.put("request_id",request_id);
                Log.d("mobile", mobile);
               // Log.d("request_id", request_id);
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


   /* private class  createaccount extends AsyncTask<String, String,JSONObject>
    {

        private ArrayList<NameValuePair> nameValuePairs;
        private JSONObject json;
        String mobile;

        public createaccount(String mobile){
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
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/sendotp.php", 2, nameValuePairs);
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

                if (status.equalsIgnoreCase("1")) {

                    Toast.makeText(getBaseContext(),data.toString(), Toast.LENGTH_SHORT).show();

                    //Log.d("mobile",data.getString("mobile").toString());

                    String mobile;
                    mobile = data.getString("mobile").toString();

                    SharedPreferences sharedPreferences = getSharedPreferences("Mobile Number", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("mobile", mobile);
                    editor.commit();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateAccount.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Otp Sent Successfully");
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           // startActivity(new Intent(OtpScreen.this, DashboardActivity.class));
                            dialogInterface.dismiss();
                            Intent login = new Intent(CreateAccount.this, PersonalDetails.class);
                            startActivity(login);
                            finish();
                        }
                    }).show();
                }
                else {
                    //  if(result.getString("status").equals(0)){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateAccount.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("The mobile number already exist");
                    builder.setPositiveButton(R.string.ok, null).show();
                    // }
                    //  Validations.MyAlertBox(Signup.this,"User already exist");
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


