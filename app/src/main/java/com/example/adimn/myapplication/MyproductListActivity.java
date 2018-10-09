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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adimn.myapplication.MyProductListAdapter.customButtonListener;

public class MyproductListActivity extends AppCompatActivity  implements customButtonListener{

    String farmerID;
    private ListView Lview;
    List<MyProductListCon> productListCon;
    MyProductListAdapter productAdapter;
    ImageView img_back,img_edit,img_avaliable;
    String image, productname,categoryname,quantity ,price, description,units,date;

    ArrayAdapter<String> adapter;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_list);

        img_edit=(ImageView)findViewById(R.id.img_edit);
        img_avaliable=(ImageView)findViewById(R.id.img_avaliable);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent add = new Intent(MyproductListActivity.this, Dashboard.class);
                startActivity(add);

            }
        });



        Lview = (ListView) findViewById(R.id.list_products);

        productListCon = new ArrayList<MyProductListCon>();

        SharedPreferences sharedPreferences = getSharedPreferences("LoginForm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        farmerID = sharedPreferences.getString("farmerID","");


       // new productlist(farmerID.toString()).execute();
        productlist();
    }

/*
    @Override
    public void onButtonClickListner(int position, View v) {
        View view = v;
        if(view.getId() == R.id.img_accept)
        {
           *//* String pid = productListCon.get(position).getPid();*//*
            Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.img_cancel)
        {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        }

    }*/

    @Override
    public void onButtonClickListener(int position, View v) {
        if (v.getId() == R.id.img_edit) {
            String pid = productListCon.get(position).getProduct_id();
            String category = productListCon.get(position).getProduct_name();
            String subcategory = productListCon.get(position).getCat_name();
            String quant = productListCon.get(position).getQuantity();
            String money = productListCon.get(position).getPrice();
            String desc  = productListCon.get(position).getDescription();
            String img = productListCon.get(position).getImage();



            SharedPreferences sharedPreferences = getSharedPreferences("Edit", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("category", category);
            editor.putString("subcategory", subcategory);
            editor.putString("quant", quant);
            editor.putString("money", money);
            editor.putString("desc", desc);
            editor.putString("img", img);
            editor.putString("pid",pid);
            editor.commit();
           // Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show();
            //status = String.valueOf(1);

            GetEdit();
        }

        else if (v.getId() == R.id.img_avaliable) {
            // String pid = productListCon.get(position).getPid();
            Toast.makeText(this, "in stock", Toast.LENGTH_SHORT).show();
            //status = String.valueOf(1);
        }


    }

    public void GetEdit(){
        Intent edit = new Intent(MyproductListActivity.this,EditProduct.class);
        startActivity(edit);
    }


    public void productlist() {

        RequestQueue queue = Volley.newRequestQueue(MyproductListActivity.this);

        String url = "https://www.nandikrushi.in/services/productslist.php";


        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("Products");
                    String status = jsonObject.getString("status");


                    if (array!= null) {
                        // Toast.makeText(MyproductListActivity.this, "hi", Toast.LENGTH_SHORT).show();

                        // JSONObject data = new JSONObject(array.getJSONObject(i).toString());
                        for(int i = 0;i<array.length();i++)
                        {

                            JSONObject data = array.getJSONObject(i);
                            image = data.getString("image");

                            //Log.d("id",videoid);
                            productname = data.getString("subcategory");
                            // Toast.makeText(MyproductListActivity.this, productname, Toast.LENGTH_SHORT).show();
                            categoryname = data.getString("category");
                            //Toast.makeText(MyproductListActivity.this, categoryname, Toast.LENGTH_SHORT).show();
                            //quantity = data.getString("quantity").toString();
                            price = data.getString("price");

                            // Toast.makeText(MyproductListActivity.this, price, Toast.LENGTH_SHORT).show();
                            description = data.getString("product_desc");
                            //Toast.makeText(MyproductListActivity.this, description, Toast.LENGTH_SHORT).show();
                            units = data.getString("units");
                            // Toast.makeText(MyproductListActivity.this, units, Toast.LENGTH_SHORT).show();
                            date = data.getString("date");
                             String product_id = data.getString("product_id");
                            // Toast.makeText(MyproductListActivity.this, product_id, Toast.LENGTH_SHORT).show();

                            // image="http://www.nandikrushi.in/customer/uploads"+image;
                            // System.out.println(image);

                            productListCon.add(new MyProductListCon(image,productname,categoryname,quantity,price,description,units,date,product_id));
                            productAdapter = new MyProductListAdapter(MyproductListActivity.this, productListCon);
                            Lview.setAdapter(productAdapter);
                            productAdapter.setCustomButtonListener(MyproductListActivity.this);
                        }



                       /* android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyproductListActivity.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Product is out of stock");*/


                        //String video_image, String video_title, String video_subtitle, String video_url

                     /*  productListCon.add(new MyProductListCon(image.toString(), productname.toString(),categoryname.toString(), quantity.toString(),price.toString(), description.toString()));
                        productAdapter = new MyProductListAdapter(MyproductListActivity.this, productListCon);
                        Lview.setAdapter(productAdapter);*/


                    }
                    else {
                        //  if(result.getString("status").equals(0)){
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyproductListActivity.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("No products found");
                        builder.setPositiveButton(R.string.ok, null).show();
                        // }
                        //  Validations.MyAlertBox(Signup.this,"User already exist");
                    }
                     /*if (status.contains("-1")){
                        //  if(result.getString("status").equals(0)){
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyproductListActivity.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("No products found");
                        builder.setPositiveButton(R.string.ok, null).show();
                        // }
                        //  Validations.MyAlertBox(Signup.this,"User already exist");
                    }*/

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
                params.put("farmerID", farmerID);
               /* params.put("password",pass);
                Log.d("mobile", ph);
                Log.d("password", pass);*/
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



    /*private class productlist extends AsyncTask<String , String , JSONObject> {
        // private progressDialog pDialog;
        private ArrayList<NameValuePair> nameValuePairs;
        private JSONObject json;
        String farmerID;
        String image, productname,categoryname,quantity ,price, description,units,date;


        public productlist(String farmerID) {
            ArrayList<NameValuePair> nameValuePairs;
            this.farmerID = farmerID;

        }



        @Override
        protected JSONObject doInBackground(String... strings) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("farmerID", farmerID));
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/productslist.php", 2, nameValuePairs);
            return json;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            super.onPostExecute(jsonObject);
            String status ;

            try {

                JSONObject result = new JSONObject(jsonObject.toString());
                JSONArray array = result.getJSONArray("Products");


                if (array!= null) {
                   // Toast.makeText(MyproductListActivity.this, "hi", Toast.LENGTH_SHORT).show();

                    // JSONObject data = new JSONObject(array.getJSONObject(i).toString());
                    for(int i = 0;i<array.length();i++)
                    {

                        JSONObject data = array.getJSONObject(i);
                        image = data.getString("image");

                        //Log.d("id",videoid);
                        productname = data.getString("subcategory");
                        // Toast.makeText(MyproductListActivity.this, productname, Toast.LENGTH_SHORT).show();
                        categoryname = data.getString("category");
                        //Toast.makeText(MyproductListActivity.this, categoryname, Toast.LENGTH_SHORT).show();
                        //quantity = data.getString("quantity").toString();
                        price = data.getString("price");

                        // Toast.makeText(MyproductListActivity.this, price, Toast.LENGTH_SHORT).show();
                        description = data.getString("product_desc");
                        //Toast.makeText(MyproductListActivity.this, description, Toast.LENGTH_SHORT).show();
                        units = data.getString("units");
                        // Toast.makeText(MyproductListActivity.this, units, Toast.LENGTH_SHORT).show();
                        date = data.getString("date");
                      //  String product_id = data.getString("product_id");
                        // Toast.makeText(MyproductListActivity.this, product_id, Toast.LENGTH_SHORT).show();

                       // image="http://www.nandikrushi.in/customer/uploads"+image;
                       // System.out.println(image);

                        productListCon.add(new MyProductListCon(image,productname,categoryname,quantity,price,description,units,date));
                        productAdapter = new MyProductListAdapter(MyproductListActivity.this, productListCon);
                        Lview.setAdapter(productAdapter);
                        productAdapter.setCustomButtonListener(MyproductListActivity.this);
                    }



                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyproductListActivity.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Product is out of stock");


                    //String video_image, String video_title, String video_subtitle, String video_url

                       *//* productListCon.add(new MyProductListCon(image.toString(), productname.toString(),categoryname.toString(), quantity.toString(),price.toString(), description.toString()));
                        productAdapter = new MyProductListAdapter(MyproductListActivity.this, productListCon);
                        Lview.setAdapter(productAdapter);*//*


                }
                else {
                    //  if(result.getString("status").equals(0)){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyproductListActivity.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Product is in Stock");
                    builder.setPositiveButton(R.string.ok, null).show();
                    // }
                    //  Validations.MyAlertBox(Signup.this,"User already exist");
                }

            } catch (JSONException e) {
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
