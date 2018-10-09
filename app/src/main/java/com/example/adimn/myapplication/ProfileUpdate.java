package com.example.adimn.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class ProfileUpdate extends AppCompatActivity {

     /*private ViewGroup Lview;
    List<ProfileUpdateCon> profileUpdateCon;
    ProfileUpdateAdapter adapter;*/
   // ImageView img_back;

   // ArrayAdapter<String> adapter;


     //ArrayList for LinearLayout;
   //  ArrayList<HashMap<String, String>> profileupdate;


    ImageView img_back;
    String farmerID,mobileno,email,village,mandel,district,state,pincode;
    TextView tv_name,tv_mobileno,tv_email,tv_village,tv_mandal,tv_dist,tv_state,tv_pincode,tv_logout;
    String name,userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);



        SharedPreferences sharedPreferences = getSharedPreferences("LoginForm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        farmerID = sharedPreferences.getString("farmerID","");

        userid = farmerID;

        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_mobileno=(TextView)findViewById(R.id.tv_mobileno);
        tv_email=(TextView)findViewById(R.id.tv_email);
        tv_village=(TextView)findViewById(R.id.tv_village);
        tv_mandal=(TextView)findViewById(R.id.tv_mandal);
        tv_dist=(TextView)findViewById(R.id.tv_dist);
        tv_state=(TextView)findViewById(R.id.tv_state);
        tv_pincode=(TextView)findViewById(R.id.tv_pincode);


        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent add = new Intent(ProfileUpdate.this, Dashboard.class);
                startActivity(add);

            }
        });




        tv_logout = (TextView) findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("LoginForm", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("mobile", "");
                editor.putString("password", "");
                editor.commit();

                Intent k = new Intent(ProfileUpdate.this, LoginForm.class);
                k.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(k);

            }
        });
       /* Lview = (ViewGroup) findViewById(R.id.ll_profile);

        profileUpdateCon = new ArrayList<ProfileUpdateCon>();

*/

       request();
    }
    public void request() {

        RequestQueue queue = Volley.newRequestQueue(ProfileUpdate.this);

        String url = "https://www.nandikrushi.in/services/profile.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject jsonObject=object.getJSONObject("profiledetails");
                    name = jsonObject.getString("cust_username");
                    tv_name.setText(name);
                    System.out.println(name);
                    // Toast.makeText(MyproductListActivity.this, productname, Toast.LENGTH_SHORT).show();
                    mobileno = jsonObject.getString("cust_phno");
                    tv_mobileno.setText(mobileno);
                    System.out.println(mobileno);
                    //Toast.makeText(MyproductListActivity.this, categoryname, Toast.LENGTH_SHORT).show();
                    //quantity = data.getString("quantity").toString();
                    email = jsonObject.getString("cust_email");
                    tv_email.setText(email);
                    System.out.println(email);

                    // Toast.makeText(MyproductListActivity.this, price, Toast.LENGTH_SHORT).show();
                    village = jsonObject.getString("cust_village");
                    tv_village.setText(village);
                    System.out.println(village);
                    //Toast.makeText(MyproductListActivity.this, description, Toast.LENGTH_SHORT).show();
                    mandel = jsonObject.getString("cust_city");
                    tv_mandal.setText(mandel);
                    System.out.println(mandel);
                    // Toast.makeText(MyproductListActivity.this, units, Toast.LENGTH_SHORT).show();
                    district = jsonObject.getString("cust_district");
                    tv_dist.setText(district);
                    System.out.println(district);
                    state = jsonObject.getString("cust_state");
                    tv_state.setText(state);
                    System.out.println(state);
                    pincode = jsonObject.getString("cust_pin");
                    tv_pincode.setText(pincode);
                    System.out.println(pincode);
                    // String product_id = data.getString("product_id");
                    // Toast.makeText(MyproductListActivity.this, product_id, Toast.LENGTH_SHORT).show();

                    // image="http://www.nandikrushi.in/customer/uploads"+image;
                    // System.out.println(image);

                /*profileUpdateCon.add(new ProfileUpdateCon(name,mobileno,email,village,mandel,district,state,pincode));
                adapter = new ProfileUpdateAdapter(ProfileUpdate.this, profileUpdateCon);

                Lview.addView(adapter);
*/

                    //}



                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileUpdate.this);
                    builder.setTitle("Nandi Krushi");



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
                params.put("userid", userid);


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
//
//    private class profileupdate extends AsyncTask<String , String , JSONObject> {
//        // private progressDialog pDialog;
//        private ArrayList<NameValuePair> nameValuePairs;
//        private JSONObject json;
//        String userid;
//        String name,mobileno,email,village,mandel,district,state,pincode;
//
//
//        public profileupdate(String farmerID) {
//            ArrayList<NameValuePair> nameValuePairs;
//            this.userid = farmerID;
//
//        }
//
//
//
//        @Override
//        protected JSONObject doInBackground(String... strings) {
//            nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("userid", userid));
//            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/profile.php", 2, nameValuePairs);
//            return json;
//        }
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject jsonObject) {
//
//            super.onPostExecute(jsonObject);
//            String status ;
//
//            try {
//                JSONArray jsonArray = new JSONArray();
//                JSONObject data = jsonObject.getJSONObject("profiledetails");
//                // status = data.getString("status");
//                //JSONObject result = new JSONObject(jsonObject.toString());
//
//                // JSONObject data = jsonObject.getJSONObject("Products");
//
//                //JSONArray array = new JSONArray(result.getString("data"));
//                //  JSONArray jsonArray = new JSONArray();
//                // JSONObject result = new JSONObject(jsonObject.toString());
//                // JSONArray array = result.getJSONArray("profiledetails");
//                //status= jsonObject.getString("status");
//
//              /*  if (data!= null) {*/
//                //Toast.makeText(ProfileUpdate.this, "hi", Toast.LENGTH_SHORT).show();
//
//                // JSONObject data = new JSONObject(array.getJSONObject(i).toString());
//                    /*for(int i = 0;i<array.length();i++)
//                    {*/
//
//                //JSONObject data = array.getJSONObject();
//                // image = data.getString("image").toString();
//
//                //Log.d("id",videoid);
//                name = data.getString("cust_username").toString();
//                tv_name.setText(name);
//                System.out.println(name);
//                // Toast.makeText(MyproductListActivity.this, productname, Toast.LENGTH_SHORT).show();
//                mobileno = data.getString("cust_phno").toString();
//                tv_mobileno.setText(mobileno);
//                System.out.println(mobileno);
//                //Toast.makeText(MyproductListActivity.this, categoryname, Toast.LENGTH_SHORT).show();
//                //quantity = data.getString("quantity").toString();
//                email = data.getString("cust_email").toString();
//                tv_email.setText(email);
//                System.out.println(email);
//
//                // Toast.makeText(MyproductListActivity.this, price, Toast.LENGTH_SHORT).show();
//                village = data.getString("cust_village").toString();
//                tv_village.setText(village);
//                System.out.println(village);
//                //Toast.makeText(MyproductListActivity.this, description, Toast.LENGTH_SHORT).show();
//                mandel = data.getString("cust_city").toString();
//                tv_mandal.setText(mandel);
//                System.out.println(mandel);
//                // Toast.makeText(MyproductListActivity.this, units, Toast.LENGTH_SHORT).show();
//                district = data.getString("cust_district").toString();
//                tv_dist.setText(district);
//                System.out.println(district);
//                state = data.getString("cust_state").toString();
//                tv_state.setText(state);
//                System.out.println(state);
//                pincode = data.getString("cust_pin").toString();
//                tv_pincode.setText(pincode);
//                System.out.println(pincode);
//                // String product_id = data.getString("product_id");
//                // Toast.makeText(MyproductListActivity.this, product_id, Toast.LENGTH_SHORT).show();
//
//                // image="http://www.nandikrushi.in/customer/uploads"+image;
//                // System.out.println(image);
//
//                /*profileUpdateCon.add(new ProfileUpdateCon(name,mobileno,email,village,mandel,district,state,pincode));
//                adapter = new ProfileUpdateAdapter(ProfileUpdate.this, profileUpdateCon);
//
//                Lview.addView(adapter);
//*/
//
//                //}
//
//
//
//                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileUpdate.this);
//                builder.setTitle("Nandi Krushi");
//              //  builder.setMessage("Product is out of stock");
//
//
//                //String video_image, String video_title, String video_subtitle, String video_url
//
//                       /* productListCon.add(new MyProductListCon(image.toString(), productname.toString(),categoryname.toString(), quantity.toString(),price.toString(), description.toString()));
//                        productAdapter = new MyProductListAdapter(MyproductListActivity.this, productListCon);
//                        Lview.setAdapter(productAdapter);*/
//
//
//              /*  }
//                else {
//                    //  if(result.getString("status").equals(0)){
//                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileUpdate.this);
//                    builder.setTitle("Nandi Krushi");
//                    builder.setMessage("Product is in Stock");
//                    builder.setPositiveButton(R.string.ok, null).show();
//                    // }
//                    //  Validations.MyAlertBox(Signup.this,"User already exist");
//                }*/
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

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

