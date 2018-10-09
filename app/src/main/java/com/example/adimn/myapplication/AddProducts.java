package com.example.adimn.myapplication;

/**
 * Created by pari on 31-05-2018.
 */

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.android.volley.Request;
//import com.android.volley.Response;


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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class
AddProducts extends AppCompatActivity {
    // Button bt_addproduct;
    ImageView img_browse,img_back;
    EditText et_price, et_description, et_quantity;
    Spinner spinner_category, spinner_subcategory, spinner_units;
    TextView tv_addproduct;
    String imageUploadValue, filePath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int REQUEST_CAMERA = 100, SELECT_FILE = 1;
    static final int PICK_IMAGE_REQUEST = 1;
    private String userChoosenTask;
    private SharedPreferences sharedpreferences;
    String farmerID;
    int categoryid, subcategoryid;
    public static String BASE_URL = "http://nandikrushi.in/customer/uploads";
    Uri uri;
    Uri imagetoupload;
    Bitmap photo;


    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    Activity activity;


    String[] units = {"units", "KG", "Grams", "Liters", "Milli Liters", "Ton"};

    List<String> categoryNameList = new ArrayList<String>();
    List<String> categoryIdList = new ArrayList<String>();
    //  List<String> categoryImgList= new ArrayList<String>();
    ArrayAdapter<String> categoryDataAdapter;

    List<String> subcategoryNameList = new ArrayList<String>();
    List<String> subcategoryIdList = new ArrayList<String>();
    ArrayAdapter<String> subcategoryDataAdapter;

    List<String> ImageList = new ArrayList<String>();
    ArrayAdapter<String> imageDataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        img_back=(ImageView)findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddProducts.this ,Dashboard.class);
                startActivity(i);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("LoginForm", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();

        farmerID = sharedPreferences.getString("farmerID","");
        //farmerID = sharedPreferences.getString("mobile","");

        System.out.println("farmerid"+farmerID);
//farmerID="1";
        spinner_category = (Spinner) findViewById(R.id.spinner_category);
        spinner_subcategory = (Spinner) findViewById(R.id.spinner_subcategory);
        spinner_units = (Spinner) findViewById(R.id.spinner_units);
        et_description = (EditText) findViewById(R.id.et_description);
        et_quantity = (EditText) findViewById(R.id.et_quantity);
        et_price = (EditText) findViewById(R.id.et_price);
        img_browse = (ImageView) findViewById(R.id.img_browse);
        tv_addproduct= (TextView) findViewById(R.id.tv_addproduct);

        activity = AddProducts.this;
        Camerapermission();

        img_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });

        tv_addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_quantity.getText() .toString().equals("")){
                    Validations.MyAlertBox(AddProducts.this,"Please Enter Quantity");
                    et_quantity.requestFocus();
                }
                else if(img_browse == null){
                   // img_browse.getResources(R.drawable.imagedefault);
                   img_browse.setImageResource(R.drawable.imagedefault);
                }
                else if (et_price.getText().toString().equals("")){
                    Validations.MyAlertBox(AddProducts.this, "Please Enter the Price");
                }
                else if (et_description.getText().toString().equals("")){
                    Validations.MyAlertBox(AddProducts.this,"Please Enter Detailed Description");
                }
                else {
                    if (Validations.isConnectedToInternet(AddProducts.this)){

                        addProduct(farmerID, imagetoupload ,categoryid, subcategoryid,et_quantity.getText().toString(), spinner_units.getSelectedItem().toString().trim(), et_price.getText().toString(),et_description.getText().toString());

                    }
                    else{
                        Validations.MyAlertBox(AddProducts.this,"Internet Connection Failed");
                    }



                }

            }
        });



       // new AddProducts.GetCategory().execute();

        category();

        categoryDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categoryNameList);
        categoryDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(categoryDataAdapter);

        subcategoryDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, subcategoryNameList);
        subcategoryDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_subcategory.setAdapter(subcategoryDataAdapter);


        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                // long categoryId=spinner_category.getSelectedItemId();
                String category = spinner_category.getSelectedItem().toString();
                categoryid = Integer.parseInt(categoryIdList.get(i));
                //categoryId = spinner_category.getSelectedItemId();

                System.out.println("category id 1    " + categoryid) ;
                if( 0 != categoryid) {
                    //new AddProducts.GetSubcategory(categoryid).execute();
                    subcategory();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                String subcategory = spinner_subcategory.getSelectedItem().toString();
                subcategoryid= Integer.parseInt(subcategoryIdList.get(i));
                // subcategoryId= spinner_subcategory.getSelectedItemId();
                System.out.println("subcategory id 2    " + subcategoryid) ;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter value = new ArrayAdapter(this,android.R.layout.simple_spinner_item, units);
        value.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_units.setAdapter(value);

        spinner_units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //   Toast.makeText(getApplicationContext(),units[position] ,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }





    private void Camerapermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddProducts.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(AddProducts.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if(requestCode == PICK_IMAGE_REQUEST){
                uri= data.getData();
                filePath = getPath(uri);
                Log.d("picUri", uri.toString());
                Log.d("filePath", filePath);

                img_browse.setImageURI(uri);
                imagetoupload = uri;
                if (filePath != null) {
                    //  imageUpload(filePath);
                    Toast.makeText(this, "image upload sucess", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                }

            }  else if (requestCode == REQUEST_CAMERA){

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                img_browse.setImageBitmap(photo);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), photo);
                imagetoupload = tempUri;

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                filePath=finalFile.toString();
                if (filePath != null) {
                    //   imageUpload(filePath);
                    Toast.makeText(this, "image upload sucess", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        if (cursor != null) {
            cursor.moveToFirst();
        }

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public void category() {

        RequestQueue queue = Volley.newRequestQueue(AddProducts.this);

        String url = "https://www.nandikrushi.in/services/category.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject stateObject = jsonArray.getJSONObject(i);
                        categoryNameList.add((String)stateObject.get("cat_name"));
                        categoryIdList.add((String)stateObject.get("cat_id"));
                        // System.out.println("cat_id is  " +categoryId);
                    }
                    categoryDataAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e1) {
                    Validations.MyAlertBox(AddProducts.this, "Connection Lost");
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
                // params.put("mobile", ph);
                // params.put("password",pass);
                // Log.d("mobile", ph);
                // Log.d("password", pass);
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
   /* public class GetCategory extends AsyncTask<String,String,JSONObject>{
        private JSONObject json;
        @Override
        protected JSONObject doInBackground(String... strings) {
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/category.php", 2, null);
            return json;
        }

        public GetCategory() {
            ArrayList<NameValuePair> nameValuePairs;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                JSONObject result = new JSONObject(jsonObject.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("categories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject stateObject = jsonArray.getJSONObject(i);
                    categoryNameList.add((String)stateObject.get("cat_name"));
                    categoryIdList.add((String)stateObject.get("cat_id"));
                    // System.out.println("cat_id is  " +categoryId);
                }
                categoryDataAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e1) {
                Validations.MyAlertBox(AddProducts.this, "Connection Lost");
            }
        }
    } */

    public void subcategory() {

        RequestQueue queue = Volley.newRequestQueue(AddProducts.this);

        String url = "https://www.nandikrushi.in/services/subcategory.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("subcategories");
                    subcategoryNameList.clear();
                    subcategoryIdList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject stateObject = jsonArray.getJSONObject(i);
                        subcategoryNameList.add((String)stateObject.get("subcat_name"));
                        subcategoryIdList.add((String)stateObject.get("subcat_id"));
                    }
                    subcategoryDataAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e1) {
                    Validations.MyAlertBox(AddProducts.this, "Connection Lost");
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
                 params.put("catid", String.valueOf(categoryid));
                // params.put("password",pass);
                // Log.d("mobile", ph);
                // Log.d("password", pass);
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



   /* public class GetSubcategory extends AsyncTask<String,String,JSONObject>{
        private JSONObject json;
        String categoryid;
        ArrayList<NameValuePair> nameValuePairs;
        @Override
        protected JSONObject doInBackground(String... strings) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("catid", categoryid));
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/subcategory.php", 2, nameValuePairs);
            return json;
        }

        public GetSubcategory(int catId) {
            this.categoryid = String.valueOf(catId);
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                JSONObject result = new JSONObject(jsonObject.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("subcategories");
                subcategoryNameList.clear();
                subcategoryIdList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject stateObject = jsonArray.getJSONObject(i);
                    subcategoryNameList.add((String)stateObject.get("subcat_name"));
                    subcategoryIdList.add((String)stateObject.get("subcat_id"));
                }
                subcategoryDataAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e1) {
                Validations.MyAlertBox(AddProducts.this, "Connection Lost");
            }
        }
    }
*/


    /*public class addproduct extends AsyncTask<String,String,JSONObject>{

        private ArrayList<NameValuePair> nameValuePairs;
        private JSONObject json;
        String Image,quantity,units,price,description;
        //String categoryId,subcategoryId;
        //Long cId,scId,fId;
        //String farmerID = String.valueOf(fId);
        //String  = String.valueOf(cId);
        //String  subcategoryId= String.valueOf(scId);
        public addproduct(long farmerID,
                          String Image,
                          String categoryId,
                          String subcategoryId,
                          String quantity,
                          String units,
                          String price,
                          String description) {
            ArrayList<NameValuePair> nameValuePairs;
            //this.farmerID=String.valueOf(farmerID);
            this.Image = Image;
            // this.categoryId=categoryId;
            // this.subcategoryId=subcategoryId;
            //this.categoryId = String.valueOf(categoryId);
            //this.subcategoryId = String.valueOf(subcategoryId);
            this.quantity = quantity;
            this.units = units;
            this.price = price;
            this.description = description;
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("farmerID",String.valueOf(farmerID)));
            nameValuePairs.add(new BasicNameValuePair("productPic",Image));
            nameValuePairs.add(new BasicNameValuePair("categoryId",categoryid));
            nameValuePairs.add(new BasicNameValuePair("subcategoryId",subcategoryid));
            nameValuePairs.add(new BasicNameValuePair("quantity",quantity));
            nameValuePairs.add(new BasicNameValuePair("units",units));
            nameValuePairs.add(new BasicNameValuePair("price", price));
            nameValuePairs.add(new BasicNameValuePair("description", description));

            System.out.println("category id & subcategory id is    " + categoryid +subcategoryid) ;

            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/addproduct.php", 2, nameValuePairs);
            return json;
            //  return null;

        }
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                JSONObject result = new JSONObject(jsonObject.toString());
                if (result.getString("status").toString().equals("success")) {
                } else {
                    Validations.MyAlertBox(AddProduct.this, "Please Enter Valid Credentials");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e1) {
                Validations.MyAlertBox(AddProduct.this, "Connection Lost");
            }
            System.out.println("category id & subcategory id is    " + categoryid +subcategoryid) ;
        }
    }
*/



    private void addProduct(String farmer_id, Uri imagetoupload, int categoryid, int subcategoryid, String quant, String units, String price, String description ) {

        String fid = String.valueOf(farmer_id);
        File originalFile = new File(filePath);

        RequestBody filePart = RequestBody.create(MediaType.parse(getContentResolver().getType(imagetoupload)), originalFile);
        MultipartBody.Part file = MultipartBody.Part.createFormData("productPic", originalFile.getName(), filePart);
        RequestBody Farmer_id = RequestBody.create(MultipartBody.FORM, fid);
        RequestBody Cat_id = RequestBody.create(MultipartBody.FORM, String.valueOf(categoryid));
        RequestBody Subcat_id = RequestBody.create(MultipartBody.FORM, String.valueOf(subcategoryid));
        RequestBody Quant = RequestBody.create(MultipartBody.FORM, quant);
        RequestBody Units = RequestBody.create(MultipartBody.FORM, units);
        RequestBody Price = RequestBody.create(MultipartBody.FORM, price);
        RequestBody Desc = RequestBody.create(MultipartBody.FORM, description);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://www.nandikrushi.in/services/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        WebService service = retrofit.create(WebService.class);

        Call<ResponseBody> call = service.addProduct(Farmer_id, file, Cat_id, Subcat_id, Quant, Units, Price, Desc);
        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String status="1";

                try {

                    String jsonData = response.body().string();
                    System.out.println("data"+jsonData);
                    try {
                        JSONObject jsonObject;
                        JSONObject result = new JSONObject(jsonData);
                        status=result.getString("productstatus");
                        System.out.println("response is"+status);
                        JSONObject result1 = new JSONObject(status);
                        status=result1.getString("status");

                        if (result1.getString("status").equalsIgnoreCase("1")) {


                            Toast.makeText(AddProducts.this, "Product Added successfully", Toast.LENGTH_SHORT).show();
                            Intent home = new Intent(AddProducts.this,Dashboard.class);
                            startActivity(home);
                        } else {

                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddProducts.this);
                            builder.setTitle("Nandi Krushi...!");
                            builder.setMessage("Failed to update");
                            builder.setPositiveButton("ok", null).show();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }


        });
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

