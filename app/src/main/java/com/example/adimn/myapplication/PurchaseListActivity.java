package com.example.adimn.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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

public class PurchaseListActivity extends AppCompatActivity {
    String farmerID;
    ImageView img_back;
    private ListView Lview;
    List<PurchaseListCon> purchaseListcon;
    PurchaseListAdapter adapters;
    int stat=0;
    String status = String.valueOf(stat);
    String img, productname,categoryname ,amount, cust_city,units,customer_name;

    // ArrayAdapter<String> adapter;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;


    ImageView img_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases_list);
        img_logo=(ImageView)findViewById(R.id.img_logo);
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PurchaseListActivity.this ,Dashboard.class);
                startActivity(i);
            }
        });
        Lview = (ListView) findViewById(R.id.listView_purchase);

        purchaseListcon = new ArrayList<PurchaseListCon>();

        SharedPreferences sharedPreferences = getSharedPreferences("LoginForm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        farmerID = sharedPreferences.getString("farmerID","");
        //farmerID= "1030";
        System.out.println("farmerid"+farmerID);

       // new PurchaseListActivity.purchaselist(farmerID.toString()).execute();

        purchaselists ();
    }



    public void purchaselists() {

        RequestQueue queue = Volley.newRequestQueue(PurchaseListActivity.this);

        String url = "https://www.nandikrushi.in/services/purchaselist.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {

                    JSONObject result = new JSONObject(response);
                    JSONArray array = result.getJSONArray("orders");
                    String status= result.getString("status");

                    if (array != null) {
                        // Toast.makeText(MyproductListActivity.this, "hi", Toast.LENGTH_SHORT).show();

                        // JSONObject data = new JSONObject(array.getJSONObject(i).toString());
                        for (int i = 0; i < array.length(); i++) {

                            JSONObject data = array.getJSONObject(i);
                            img = data.getString("image");

                            //Log.d("id",videoid);
                            productname = data.getString("subcategory").toString();
                            // Toast.makeText(MyproductListActivity.this, productname, Toast.LENGTH_SHORT).show();
                            categoryname = data.getString("category").toString();
                            //Toast.makeText(MyproductListActivity.this, categoryname, Toast.LENGTH_SHORT).show();
                            //quantity = data.getString("quantity").toString();
                            amount = data.getString("amount").toString();

                            cust_city = data.get("customer_city").toString();

                            // Toast.makeText(MyproductListActivity.this, price, Toast.LENGTH_SHORT).show();
                            //description = data.getString("product_desc").toString();
                            //Toast.makeText(MyproductListActivity.this, description, Toast.LENGTH_SHORT).show();
                            units = data.getString("units").toString();
                            // Toast.makeText(MyproductListActivity.this, units, Toast.LENGTH_SHORT).show();
                            // order_id = data.getString("order_id").toString();
                            // Toast.makeText(MyproductListActivity.this, product_id, Toast.LENGTH_SHORT).show();
                            customer_name = data.getString("customer_name").toString();


                            //  Toast.makeText(OrderListActivity.this, img, Toast.LENGTH_LONG).show();
                            // System.out.println(img);

                            purchaseListcon.add(new PurchaseListCon(img, productname, categoryname, amount,cust_city , units,customer_name));
                            adapters = new PurchaseListAdapter(PurchaseListActivity.this, purchaseListcon);
                            Lview.setAdapter(adapters);
                        }


                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PurchaseListActivity.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Product is Delivered");





                    } else if(result.getString("status").equals(0)) {
                        //  if(result.getString("status").equals(0)){
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PurchaseListActivity.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Pending");
                        builder.setPositiveButton(R.string.ok, null).show();
                        // }
                        //  Validations.MyAlertBox(Signup.this,"User already exist");
                    }
                    else{
                        //  if(result.getString("status").equals(0)){
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PurchaseListActivity.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("No products found");
                        builder.setPositiveButton(R.string.ok, null).show();
                        // }
                        //  Validations.MyAlertBox(Signup.this,"User already exist");
                    }


                } catch(JSONException e){
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
                params.put("farmerID", farmerID);
                params.put("status",status);

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



  /*  private class purchaselist extends AsyncTask<String , String , JSONObject> {
        // private progressDialog pDialog;
        private ArrayList<NameValuePair> nameValuePairs;
        private JSONObject json;
        String farmerID;
        int stat=0;
        String status = String.valueOf(stat);
        String img, productname,categoryname ,amount, cust_city,units,customer_name;


        public purchaselist(String farmerID) {
            ArrayList<NameValuePair> nameValuePairs;
            this.farmerID = farmerID;
            this.status= status;

        }



        @Override
        protected JSONObject doInBackground(String... strings) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("farmerID", farmerID));
          //  nameValuePairs.add(new BasicNameValuePair("status",status));
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/purchaselist.php", 2, nameValuePairs);
            return json;

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            super.onPostExecute(jsonObject);
            String status;

            try {

                JSONObject result = new JSONObject(jsonObject.toString());
                JSONArray array = result.getJSONArray("orders");
                //status= jsonObject.getString("status");

                if (array != null) {
                    // Toast.makeText(MyproductListActivity.this, "hi", Toast.LENGTH_SHORT).show();

                    // JSONObject data = new JSONObject(array.getJSONObject(i).toString());
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject data = array.getJSONObject(i);
                        img = data.getString("image");

                        //Log.d("id",videoid);
                        productname = data.getString("subcategory").toString();
                        // Toast.makeText(MyproductListActivity.this, productname, Toast.LENGTH_SHORT).show();
                        categoryname = data.getString("category").toString();
                        //Toast.makeText(MyproductListActivity.this, categoryname, Toast.LENGTH_SHORT).show();
                        //quantity = data.getString("quantity").toString();
                        amount = data.getString("amount").toString();

                        cust_city = data.get("customer_city").toString();

                        // Toast.makeText(MyproductListActivity.this, price, Toast.LENGTH_SHORT).show();
                        //description = data.getString("product_desc").toString();
                        //Toast.makeText(MyproductListActivity.this, description, Toast.LENGTH_SHORT).show();
                        units = data.getString("units").toString();
                        // Toast.makeText(MyproductListActivity.this, units, Toast.LENGTH_SHORT).show();
                       // order_id = data.getString("order_id").toString();
                        // Toast.makeText(MyproductListActivity.this, product_id, Toast.LENGTH_SHORT).show();
                        customer_name = data.getString("customer_name").toString();


                        //  Toast.makeText(OrderListActivity.this, img, Toast.LENGTH_LONG).show();
                        // System.out.println(img);

                        purchaseListcon.add(new PurchaseListCon(img, productname, categoryname, amount,cust_city , units,customer_name));
                        adapters = new PurchaseListAdapter(PurchaseListActivity.this, purchaseListcon);
                        Lview.setAdapter(adapters);
                    }


                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PurchaseListActivity.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Product is Delivered");


                    //String video_image, String video_title, String video_subtitle, String video_url

                       *//* productListCon.add(new MyProductListCon(image.toString(), productname.toString(),categoryname.toString(), quantity.toString(),price.toString(), description.toString()));
                        productAdapter = new MyProductListAdapter(MyproductListActivity.this, productListCon);
                        Lview.setAdapter(productAdapter);*//*


                } else {
                    //  if(result.getString("status").equals(0)){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PurchaseListActivity.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Pending");
                    builder.setPositiveButton(R.string.ok, null).show();
                    // }
                    //  Validations.MyAlertBox(Signup.this,"User already exist");
                }


            } catch(JSONException e){
                e.printStackTrace();
            }
        }


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


