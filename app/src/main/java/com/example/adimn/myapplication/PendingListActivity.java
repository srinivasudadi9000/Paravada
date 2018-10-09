package com.example.adimn.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.adimn.myapplication.PendingListAdapter.customButtonListener;

public class PendingListActivity extends AppCompatActivity implements customButtonListener {

    String farmerID;
    ImageView img_back;
    private ListView Lview;
    List<PendingListCon> pending;
    PendingListAdapter adapter;
    ImageView img_accept, img_cancel, img_whatsappcall, img_call;
    String  mobile,ProductID;
    int stat=0;
    String status = String.valueOf(stat);
    String img, productname,categoryname ,amount, units,order_id,customer_name;

    // ArrayAdapter<String> adapter;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_list);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent add = new Intent(PendingListActivity.this, Dashboard.class);
                startActivity(add);

            }
        });

        Lview = (ListView) findViewById(R.id.listView_pending);
        img_accept = (ImageView) findViewById(R.id.img_accept);
        img_cancel = (ImageView) findViewById(R.id.img_cancel);
        img_whatsappcall = (ImageView) findViewById(R.id.img_whatsappcall);
        img_call = (ImageView) findViewById(R.id.img_call);


        pending = new ArrayList<PendingListCon>();

        SharedPreferences sharedPreferences = getSharedPreferences("LoginForm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        farmerID = sharedPreferences.getString("farmerID", "");
        //farmerID= "1030";
        System.out.println("farmerid" + farmerID);

        //new productlist(farmerID.toString()).execute();
        pendinglist();
    }
 /*   @Override
    public void onButtonClickListner(int position, View v){
        if(v.getId()==R.id.img_accept){
            String pid=pending.get(position).getPid();
            Toast.makeText(this, "accepted", Toast.LENGTH_SHORT).show();
        }
        else if(v.getId()==R.id.img_cancel){
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    public void onButtonClickListener(int position, View v) {
        if (v.getId() == R.id.img_accept) {
            ProductID = pending.get(position).getPid();
            Toast.makeText(this, "accepted", Toast.LENGTH_SHORT).show();
           Toast.makeText(this, ProductID, Toast.LENGTH_SHORT).show();
            status= String.valueOf(1);

           // status = String.valueOf(1);
        } else if (v.getId() == R.id.img_cancel) {
            String ProductId = pending.get(position).getPid();
            Toast.makeText(this, ProductID, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
            status = String.valueOf(2);

        } else if (v.getId() == R.id.img_whatsappcall) {

            String mobile = pending.get(position).getMobile();
           // Toast.makeText(this, "whatsappcall", Toast.LENGTH_SHORT).show();
            GetWhatsapp(mobile);
        } else if (v.getId() == R.id.img_call) {
            String mobile = pending.get(position).getMobile();
            Toast.makeText(this, "call", Toast.LENGTH_SHORT).show();
            GetPhonecall(mobile);
        }
        //new PendingListActivity.Accept(status,ProductID).execute();
        accept();
    }


    private void GetPhonecall(String mobile) {
        String digits = "\\d+";
        // String mobile = "9505246276";
        final int REQUEST_PHONE_CALL = 1;


        // public void onClickWhatsApp(View view) {

        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + mobile.toString()));
        // startActivity(i);
        if (ContextCompat.checkSelfPermission(PendingListActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PendingListActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            startActivity(i);
        }
    }




    public void accept() {

        RequestQueue queue = Volley.newRequestQueue(PendingListActivity.this);

        String url = "https://www.nandikrushi.in/services/prostatus.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);

                   // JSONArray jsonArray = new JSONArray();
                    JSONObject data = jsonObject.getJSONObject("productstatus");
                    status = data.getString("status");

                   // System.out.println("jsonarray is" + jsonObject);
                    if (status.equalsIgnoreCase("1")) {

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PendingListActivity.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Product has been accepted by farmer");
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent login = new Intent(PendingListActivity.this, Dashboard.class);
                                startActivity(login);
                                finish();
                            }
                        }).show();




                    } else {
                        //  if(result.getString("status").equals(0)){
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PendingListActivity.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Farmer Declined the product");
                        builder.setPositiveButton(R.string.ok, null).show();
                        // }
                        //  Validations.MyAlertBox(Signup.this,"User already exist");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e1) {
                    Validations.MyAlertBox(PendingListActivity.this, "Connection Lost");
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
                params.put("ProductID", ProductID);
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
   /* private class Accept extends AsyncTask<String, String, JSONObject> {

        private ArrayList<NameValuePair> nameValuePairs;
        private JSONObject json;
        String status, ProductID;


        public Accept(String status, String ProductID) {
            ArrayList<NameValuePair> nameValuePairs;
            this.status = status;
            this.ProductID =ProductID;
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("status", status));
            nameValuePairs.add(new BasicNameValuePair("ProductID", ProductID));
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/prostatus.php", 2, nameValuePairs);
            return json;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            String status = "1";

            try {
                //JSONObject result = new JSONObject(jsonObject.toString());

                JSONArray jsonArray = new JSONArray();
                JSONObject data = jsonObject.getJSONObject("productstatus");
                status = data.getString("status");

                System.out.println("jsonarray is" + jsonObject);
                if (status.equalsIgnoreCase("1")) {

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PendingListActivity.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Product has been accepted by farmer");
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           *//* startActivity(new Intent(LoginForm.this, Dashboard.class));
                            dialogInterface.dismiss();*//*
                            Intent login = new Intent(PendingListActivity.this, Dashboard.class);
                            startActivity(login);
                            finish();
                        }
                    }).show();




                } else {
                    //  if(result.getString("status").equals(0)){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PendingListActivity.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Farmer Declined the product");
                    builder.setPositiveButton(R.string.ok, null).show();
                    // }
                    //  Validations.MyAlertBox(Signup.this,"User already exist");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e1) {
                Validations.MyAlertBox(PendingListActivity.this, "Connection Lost");
            }

        }

    }*/



    private void GetWhatsapp(String mobile) {
        String digits="\\d+";
        PackageManager pm = getPackageManager();
        if (mobile.matches(digits)) {
            try {

                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                String text = "YOUR TEXT HERE";

                PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                Uri uri = Uri.parse("https://wa.me/91"+mobile);
                Intent whatsapp=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(whatsapp);
                //Check if package exists or not. If not then code
                //in catch block will be called
                waIntent.setPackage("com.whatsapp");

                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(waIntent, "Share with"));

            } catch (PackageManager.NameNotFoundException e) {

                Toast.makeText(PendingListActivity.this, "whatsapp not installed", Toast.LENGTH_SHORT).show();
            }

        }

    }



    public void pendinglist() {

        RequestQueue queue = Volley.newRequestQueue(PendingListActivity.this);

        String url = "https://www.nandikrushi.in/services/orders.php";

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

                            // cust_phone = data.get("cust_phone").toString();

                            // Toast.makeText(MyproductListActivity.this, price, Toast.LENGTH_SHORT).show();
                            //description = data.getString("product_desc").toString();
                            //Toast.makeText(MyproductListActivity.this, description, Toast.LENGTH_SHORT).show();
                            units = data.getString("units").toString();
                            // Toast.makeText(MyproductListActivity.this, units, Toast.LENGTH_SHORT).show();
                            order_id = data.getString("order_id").toString();
                            // Toast.makeText(MyproductListActivity.this, product_id, Toast.LENGTH_SHORT).show();
                            customer_name = data.getString("customer_name").toString();

                            //  Toast.makeText(OrderListActivity.this, img, Toast.LENGTH_LONG).show();
                            // System.out.println(img)

                            String productid=data.getString("product_id");
                            mobile=data.getString("cust_phone");


                            pending.add(new PendingListCon(img, productname, categoryname, amount, units, order_id,customer_name,productid,mobile));
                            adapter = new PendingListAdapter(PendingListActivity.this, pending);
                            Lview.setAdapter(adapter);
                            adapter.setcustomButtonListener(PendingListActivity.this);
                        }


                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PendingListActivity.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Product is Delivered");


                    } else if(result.getString("status").equals(0)) {
                        //  if(result.getString("status").equals(0)){
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PendingListActivity.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Pending");
                        builder.setPositiveButton(R.string.ok, null).show();
                        // }
                        //  Validations.MyAlertBox(Signup.this,"User already exist");
                    }
                    else{
                        //  if(result.getString("status").equals(0)){
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PendingListActivity.this);
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


   /* private class productlist extends AsyncTask<String , String , JSONObject> {
        // private progressDialog pDialog;
        private ArrayList<NameValuePair> nameValuePairs;
        private JSONObject json;
        String farmerID;
        int stat=0;
        String status = String.valueOf(stat);
        String img, productname,categoryname,quantity ,amount, cust_phone,units,order_id,customer_name;


        public productlist(String farmerID) {
            ArrayList<NameValuePair> nameValuePairs;
            this.farmerID = farmerID;
            this.status= status;

        }



        @Override
        protected JSONObject doInBackground(String... strings) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("farmerID", farmerID));
            nameValuePairs.add(new BasicNameValuePair("status",status));
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/orders.php", 2, nameValuePairs);
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

                        // cust_phone = data.get("cust_phone").toString();

                        // Toast.makeText(MyproductListActivity.this, price, Toast.LENGTH_SHORT).show();
                        //description = data.getString("product_desc").toString();
                        //Toast.makeText(MyproductListActivity.this, description, Toast.LENGTH_SHORT).show();
                        units = data.getString("units").toString();
                        // Toast.makeText(MyproductListActivity.this, units, Toast.LENGTH_SHORT).show();
                        order_id = data.getString("order_id").toString();
                        // Toast.makeText(MyproductListActivity.this, product_id, Toast.LENGTH_SHORT).show();
                        customer_name = data.getString("customer_name").toString();

                        //  Toast.makeText(OrderListActivity.this, img, Toast.LENGTH_LONG).show();
                        // System.out.println(img)

                        String productid=data.getString("product_id");
                        mobile=data.getString("cust_phone");


                        pending.add(new PendingListCon(img, productname, categoryname, amount, units, order_id,customer_name,productid,mobile));
                        adapter = new PendingListAdapter(PendingListActivity.this, pending);
                        Lview.setAdapter(adapter);
                        adapter.setcustomButtonListener(PendingListActivity.this);
                    }


                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PendingListActivity.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Product is Delivered");


                    //String video_image, String video_title, String video_subtitle, String video_url

                       *//* productListCon.add(new MyProductListCon(image.toString(), productname.toString(),categoryname.toString(), quantity.toString(),price.toString(), description.toString()));
                        productAdapter = new MyProductListAdapter(MyproductListActivity.this, productListCon);
                        Lview.setAdapter(productAdapter);*//*


                } else {
                    //  if(result.getString("status").equals(0)){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PendingListActivity.this);
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

