package com.example.adimn.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ResetPassword extends AppCompatActivity {

    EditText et_newpassword,et_confirmpassword;
    Button bt_confirm;
    String mobile,password;
    ImageView img_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        img_logo=(ImageView)findViewById(R.id.img_logo);
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResetPassword.this ,VerifyOtp.class);
                startActivity(i);
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("forgot",0);
        mobile = pref.getString("mobile","No Mobile was stored");

        et_newpassword = (EditText)findViewById(R.id.et_newpassword);
        et_confirmpassword = (EditText)findViewById(R.id.et_confirmpassword);
        bt_confirm = (Button)findViewById(R.id.bt_Confirm);


        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_newpassword.getText().toString().equals("")) {
                    Validations.MyAlertBox(ResetPassword.this, "Please Enter Password");
                    et_newpassword.requestFocus();
                } else if (et_confirmpassword.getText().toString().equals("")) {
                    Validations.MyAlertBox(ResetPassword.this, "Please Re-Enter Password");
                    et_confirmpassword.requestFocus();
                }
                String password = et_newpassword.getText().toString();
                String newpassword = et_confirmpassword.getText().toString();
                if (password.equals(newpassword)){
                    Validations.MyAlertBox(ResetPassword.this,"Password Match");
                }
                else
                {
                    Validations.MyAlertBox(ResetPassword.this,"Password Does Not Match ");
                }

               // new ResetPassword.newpassword(et_newpassword.getText().toString()).execute();
                reset();

                Intent i = new Intent(ResetPassword.this,LoginForm.class);
                startActivity(i);

            }
        });
    }


    public void reset() {

        password = et_newpassword.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(ResetPassword.this);

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

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ResetPassword.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Password Has Changed Successfully");
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(ResetPassword.this, LoginForm.class));
                                dialogInterface.dismiss();
                               /* Intent login = new Intent(ForgetPassword.this, VerifyOtp.class);
                                startActivity(login);
                                finish();*/
                            }
                        }).show();

                    }else {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ResetPassword.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Password Entered Mismatch");
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

                int request=3;
                String request_id= Integer.toString(request);

                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobile);
                params.put("password",password);
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

    /* private class  newpassword extends AsyncTask<String, String,JSONObject>
    {

        private ArrayList<NameValuePair> nameValuePairs;
        private JSONObject json;
        String password;
        int request=3;
        String request_id= Integer.toString(request);

        public newpassword(String password){
            this.password= password;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("password", password));
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

                System.out.println("jsonarray is" + jsonObject);
                if (status.equalsIgnoreCase("1")) {

                    System.out.println("status is " + status) ;
                    Toast.makeText(getBaseContext(), data.toString(), Toast.LENGTH_SHORT).show();

                    *//*Log.d("password",data.getString("password").toString());
                    Log.d("mobile",data.getString("mobile").toString());
                    Log.d("request_id",data.getString("request_id").toString());*//*
                    *//*Intent signup = new Intent(NewPassword.this, LoginScreen.class);
                    startActivity(signup);
                    finish();*//*
                    overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ResetPassword.this);
                    builder.setTitle("Nandi Krushi...!");
                    builder.setMessage("Password Has Changed Successfully");
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(ResetPassword.this, LoginForm.class));
                            dialogInterface.dismiss();
                        }
                    }).show();
                }
                else {

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ResetPassword.this);
                    builder.setTitle("Nandi Krushi...!");
                    builder.setMessage("Password Entered Mismatch");
                    builder.setPositiveButton(R.string.ok, null).show();

                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
*/
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
