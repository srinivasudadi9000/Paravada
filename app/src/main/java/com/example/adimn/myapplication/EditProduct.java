package com.example.adimn.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class EditProduct extends AppCompatActivity {
    String productID;
    ImageView img_back,img_camera;
    TextView tv_update,tv_category,tv_subcategory,tv_quantity;
    EditText et_price,et_description;
    String camera,category,subcategory,quantity,price,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent add = new Intent(EditProduct.this, MyproductListActivity.class);
                startActivity(add);

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("Edit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        camera = sharedPreferences.getString("img","img");
        category = sharedPreferences.getString("category","category");
        subcategory = sharedPreferences.getString("subcategory","subcategory");
        quantity = sharedPreferences.getString("quant","quant");
        price = sharedPreferences.getString("money","money");
        description = sharedPreferences.getString("desc","desc");
        productID = sharedPreferences.getString("pid","pid");

        img_camera=(ImageView)findViewById(R.id.img_camera);
        tv_category=(TextView)findViewById(R.id.tv_category);
        tv_subcategory=(TextView)findViewById(R.id.tv_subcategory);
        tv_quantity=(TextView)findViewById(R.id.tv_quantity);
        tv_update=(TextView)findViewById(R.id.tv_update);
        et_price=(EditText)findViewById(R.id.et_price);
        et_description=(EditText)findViewById(R.id.et_description);

        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_price.getText() .toString().equals("")){
                    Validations.MyAlertBox(EditProduct.this,"Please Enter Value for Price");
                    et_price.requestFocus();
                }
                else if (et_description.getText().toString().equals("")){
                    Validations.MyAlertBox(EditProduct.this,"Please Enter Valid Mobile Number");
                }

                 else {
                    if (Validations.isConnectedToInternet(EditProduct.this)){
                        edit();
                        //  new LoginForm.userLogin(et_phone.getText().toString(),et_password.getText().toString()).execute();
                    } else{
                        Validations.MyAlertBox(EditProduct.this,"Internet Connection Failed");
                    }


                }

            }
        });

       // new EditProduct.editproduct(productID.toString()).execute();
    }



    public void edit() {

        RequestQueue queue = Volley.newRequestQueue(EditProduct.this);

        String url = "https://www.nandikrushi.in/services/editproduct.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject jsonObject=object.getJSONObject("Products");
                   // name = jsonObject.getString("cust_username");

                    //System.out.println(name);
                    // Toast.makeText(MyproductListActivity.this, productname, Toast.LENGTH_SHORT).show();
                  //  mobileno = jsonObject.getString("cust_phno");
                    tv_category.setText(category);
                    //System.out.println(mobileno);
                    //Toast.makeText(MyproductListActivity.this, categoryname, Toast.LENGTH_SHORT).show();
                    //quantity = data.getString("quantity").toString();
                    //email = jsonObject.getString("cust_email");
                    tv_subcategory.setText(subcategory);
                    //System.out.println(email);

                    // Toast.makeText(MyproductListActivity.this, price, Toast.LENGTH_SHORT).show();
                    //village = jsonObject.getString("cust_village");
                    tv_quantity.setText(quantity);
                    //System.out.println(village);
                    //Toast.makeText(MyproductListActivity.this, description, Toast.LENGTH_SHORT).show();
                    //mandel = jsonObject.getString("cust_city");
                    et_price.setText(price);
                    //System.out.println(mandel);
                    // Toast.makeText(MyproductListActivity.this, units, Toast.LENGTH_SHORT).show();
                    //district = jsonObject.getString("cust_district");
                    et_description.setText(description);
                    //System.out.println(district);
                    //state = jsonObject.getString("cust_state");
                   // tv_state.setText(state);
                    //System.out.println(state);
                    //pincode = jsonObject.getString("cust_pin");
                    //tv_pincode.setText(pincode);
                    //System.out.println(pincode);
                    // String product_id = data.getString("product_id");
                    // Toast.makeText(MyproductListActivity.this, product_id, Toast.LENGTH_SHORT).show();

                    // image="http://www.nandikrushi.in/customer/uploads"+image;
                    // System.out.println(image);

                /*profileUpdateCon.add(new ProfileUpdateCon(name,mobileno,email,village,mandel,district,state,pincode));
                adapter = new ProfileUpdateAdapter(ProfileUpdate.this, profileUpdateCon);

                Lview.addView(adapter);
*/

                    //}



                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditProduct.this);
                    builder.setTitle("Product Updated Successfully");



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
                params.put("productid", productID);


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






/*

    private class editproduct extends AsyncTask<String , String , JSONObject> {
    private ArrayList<NameValuePair> nameValuePairs;
    private JSONObject json;
    String productID;
    String camera,category,subcategory,quantity,price,description;








    public editproduct(String productID) {
        ArrayList<NameValuePair> nameValuePairs;
        this.productID = productID;

    }



    @Override
    protected JSONObject doInBackground(String... strings) {
        nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("productid", productID));
        json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/editproduct.php", 2, nameValuePairs);
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
            JSONArray jsonArray = new JSONArray();
            JSONObject data = jsonObject.getJSONObject("Products");

            camera = data.getString("image").toString();
            //img_camera.setIm(camera);
            System.out.println(camera);
            // Toast.makeText(MyproductListActivity.this, productname, Toast.LENGTH_SHORT).show();
            category = data.getString("category").toString();
            tv_category.setText(category);
            System.out.println(category);
            //Toast.makeText(MyproductListActivity.this, categoryname, Toast.LENGTH_SHORT).show();
            //quantity = data.getString("quantity").toString();
            subcategory = data.getString("subcategory").toString();
            tv_subcategory.setText(subcategory);
            System.out.println(subcategory);

            // Toast.makeText(MyproductListActivity.this, price, Toast.LENGTH_SHORT).show();
            quantity = data.getString("units").toString();
            tv_quantity.setText(quantity);
            System.out.println(quantity);
            //Toast.makeText(MyproductListActivity.this, description, Toast.LENGTH_SHORT).show();
            price = data.getString("price").toString();
            et_price.setText(price);
            System.out.println(price);
            // Toast.makeText(MyproductListActivity.this, units, Toast.LENGTH_SHORT).show();
            description = data.getString("product_desc").toString();
            et_description.setText(description);
            System.out.println(description);

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditProduct.this);
            builder.setTitle("Nandi Krushi");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
*/

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



