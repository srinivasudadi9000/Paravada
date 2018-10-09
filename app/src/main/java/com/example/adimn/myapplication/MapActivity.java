package com.example.adimn.myapplication;

import android.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    //String farmerID;
    // String product,location;
    private ListView Lview;
    List<MapCon> mapCon;
    MapAdapter mapAdapter;
    EditText et_search;
    Button bt_search;
    String image, productname,categoryname,units,price, description;
    String product;
    //int lct;

    // ArrayAdapter<String> adapter;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> mapList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // getSupportActionBar().setTitle("Map Location Activity");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        Lview = (ListView) findViewById(R.id.listview_search);
        et_search= (EditText)findViewById(R.id.et_search);
        bt_search = (Button)findViewById(R.id.bt_search);

        mapCon = new ArrayList<MapCon>();

      /*  SharedPreferences sharedPreferences = getSharedPreferences("LoginForm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        farmerID = sharedPreferences.getString("farmerID","");*/

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product= et_search.getText().toString();
                if(et_search.getText().toString().equals("")){
                    Validations.MyAlertBox(MapActivity.this,"Please Enter  A Keyword");
                    et_search.requestFocus();
                }
                else {
                    if (Validations.isConnectedToInternet(MapActivity.this)){
                        // new LoginForm.userLogin(et_search.getText().toString(),et_password.getText().toString()).execute();
                        // new MapActivity.maplist(et_search.getText().toString()).execute();
                        map();
                    } else{
                        Validations.MyAlertBox(MapActivity.this,"Internet Connection Failed");
                    }


                }
            }
        });

        // new maplist(et_search.getText().toString()).execute();
         map ();

    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                System.out.println("lat and long is" + latLng);
              //  lct = latLng;
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    public void map() {

      //  final String location = String.valueOf(lct);

        RequestQueue queue = Volley.newRequestQueue(MapActivity.this);

        String url = "https://www.nandikrushi.in/services/search.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    //JSONObject result = new JSONObject(jsonObject.toString());

                    // JSONObject data = jsonObject.getJSONObject("Products");

                    //JSONArray array = new JSONArray(result.getString("data"));
                    //  JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("Products");
                    //status= jsonObject.getString("status");

                    if (array!= null) {
                        // Toast.makeText(MyproductListActivity.this, "hi", Toast.LENGTH_SHORT).show();

                        // JSONObject data = new JSONObject(array.getJSONObject(i).toString());
                        for(int i = 0;i<array.length();i++)
                        {

                            JSONObject data = array.getJSONObject(i);
                            image = data.getString("image");
                            System.out.println(image);

                            //Log.d("id",videoid);
                            productname = data.getString("subcategory");
                            System.out.println(productname);
                            // Toast.makeText(MyproductListActivity.this, productname, Toast.LENGTH_SHORT).show();
                            categoryname = data.getString("category");
                            System.out.println(categoryname);
                            //Toast.makeText(MyproductListActivity.this, categoryname, Toast.LENGTH_SHORT).show();
                            //quantity = data.getString("quantity").toString();

                            units = data.getString("units");
                            System.out.println(units);


                            price = data.getString("price");
                            System.out.println(price);

                            // Toast.makeText(MyproductListActivity.this, price, Toast.LENGTH_SHORT).show();
                            description = data.getString("product_desc");
                            System.out.println(description);
                            //Toast.makeText(MyproductListActivity.this, description, Toast.LENGTH_SHORT).show();

                            // Toast.makeText(MyproductListActivity.this, units, Toast.LENGTH_SHORT).show();
                        /*date = data.getString("date");
                            System.out.println(date);*/


                            //  String product_id = data.getString("product_id");
                            // Toast.makeText(MyproductListActivity.this, product_id, Toast.LENGTH_SHORT).show();

                            // image="http://www.nandikrushi.in/customer/uploads"+image;
                            // System.out.println(image);

                            mapCon.add(new MapCon(image,productname,categoryname,units,price,description));
                            mapAdapter = new MapAdapter(MapActivity.this, mapCon);
                            Lview.setAdapter(mapAdapter);

                            System.out.print("adapter is" +mapAdapter);
                            // mapAdapter.setCustomButtonListener(MapActivity.this);
                        }



                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MapActivity.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Product is out of stock");


                        //String video_image, String video_title, String video_subtitle, String video_url

                        /*productListCon.add(new MyProductListCon(image.toString(), productname.toString(),categoryname.toString(), quantity.toString(),price.toString(), description.toString()));
                        productAdapter = new MyProductListAdapter(MyproductListActivity.this, productListCon);
                        Lview.setAdapter(productAdapter);*/


                    }
                    else {
                        //  if(result.getString("status").equals(0)){
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MapActivity.this);
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
                params.put("product", product);
                //params.put("location",location);
                //  Log.d("mobile", ph);
                //  Log.d("password", pass);
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

    /* private class maplist extends AsyncTask<String , String , JSONObject> {
         // private progressDialog pDialog;
         private ArrayList<NameValuePair> nameValuePairs;
         private JSONObject json;
         String product,location;

         String image, productname,categoryname,units,price, description;


         public maplist(String id) {
             ArrayList<NameValuePair> nameValuePairs;
             this.product = product;
             this.location = location;

         }



         @Override
         protected JSONObject doInBackground(String... strings) {
             nameValuePairs = new ArrayList<NameValuePair>();
             nameValuePairs.add(new BasicNameValuePair("product", product));
             nameValuePairs.add(new BasicNameValuePair("location", location));
             json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/search.php", 2, nameValuePairs);
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
                 //JSONObject result = new JSONObject(jsonObject.toString());

                 // JSONObject data = jsonObject.getJSONObject("Products");

                 //JSONArray array = new JSONArray(result.getString("data"));
                 //  JSONArray jsonArray = new JSONArray();
                 JSONObject result = new JSONObject(jsonObject.toString());
                 JSONArray array = result.getJSONArray("Products");
                 //status= jsonObject.getString("status");

                 if (array!= null) {
                     // Toast.makeText(MyproductListActivity.this, "hi", Toast.LENGTH_SHORT).show();

                     // JSONObject data = new JSONObject(array.getJSONObject(i).toString());
                     for(int i = 0;i<array.length();i++)
                     {

                         JSONObject data = array.getJSONObject(i);
                         image = data.getString("image");
                         System.out.println(image);

                         //Log.d("id",videoid);
                         productname = data.getString("subcategory");
                         System.out.println(productname);
                         // Toast.makeText(MyproductListActivity.this, productname, Toast.LENGTH_SHORT).show();
                         categoryname = data.getString("category");
                         System.out.println(categoryname);
                         //Toast.makeText(MyproductListActivity.this, categoryname, Toast.LENGTH_SHORT).show();
                         //quantity = data.getString("quantity").toString();

                         units = data.getString("units");
                         System.out.println(units);


                         price = data.getString("price");
                         System.out.println(price);

                         // Toast.makeText(MyproductListActivity.this, price, Toast.LENGTH_SHORT).show();
                         description = data.getString("product_desc");
                         System.out.println(description);
                         //Toast.makeText(MyproductListActivity.this, description, Toast.LENGTH_SHORT).show();

                         // Toast.makeText(MyproductListActivity.this, units, Toast.LENGTH_SHORT).show();
                        *//* date = data.getString("date");
                        System.out.println(date);*//*


                        //  String product_id = data.getString("product_id");
                        // Toast.makeText(MyproductListActivity.this, product_id, Toast.LENGTH_SHORT).show();

                        // image="http://www.nandikrushi.in/customer/uploads"+image;
                        // System.out.println(image);

                        mapCon.add(new MapCon(image,productname,categoryname,units,price,description));
                        mapAdapter = new MapAdapter(MapActivity.this, mapCon);
                        Lview.setAdapter(mapAdapter);

                        System.out.print("adapter is" +mapAdapter);
                       // mapAdapter.setCustomButtonListener(MapActivity.this);
                    }



                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MapActivity.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("Product is out of stock");


                    //String video_image, String video_title, String video_subtitle, String video_url

                       *//* productListCon.add(new MyProductListCon(image.toString(), productname.toString(),categoryname.toString(), quantity.toString(),price.toString(), description.toString()));
                        productAdapter = new MyProductListAdapter(MyproductListActivity.this, productListCon);
                        Lview.setAdapter(productAdapter);*//*


                }
                else {
                    //  if(result.getString("status").equals(0)){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MapActivity.this);
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