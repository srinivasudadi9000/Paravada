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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class FarmDetails extends AppCompatActivity {
    Spinner spinner_state,spinner_dst,spinner_mandal,spinner_village;
    String uri;
    String statestring;
    EditText et_acers,et_pincode;
    Button bt_signup;
    String name;
    String email;
    String password;
    String mobile,states,dists,mandals,villages,pincode,acers;
    String stateId,districtId,mandalId,id;
    ImageView img_logo;
    SharedPreferences.Editor editor;
    List<String> stateNameList = new ArrayList<String>();
    List<String> stateIdList = new ArrayList<String>();
    ArrayAdapter<String> stateDataAdapter ;

    List<String> districtNameList = new ArrayList<String>();
    List<String> districtIdList = new ArrayList<String>();
    ArrayAdapter<String> districtDataAdapter ;

    List<String> mandalNameList = new ArrayList<String>();
    List<String> mandalIdList = new ArrayList<String>();
    ArrayAdapter<String> mandalDataAdapter ;

    List<String> villageNameList = new ArrayList<String>();
    List<String> villageIdList = new ArrayList<String>();
    ArrayAdapter<String> villageDataAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_details);
        img_logo=(ImageView)findViewById(R.id.img_logo);
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FarmDetails.this ,PersonalDetails.class);
                startActivity(i);
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("farmPref",0);
        editor = pref.edit();
       // mobile= pref.getString("mobile","mobile");
        name = pref.getString("username","UserName");
        email = pref.getString("email","Email");
        password = pref.getString("password","Password");

        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("mobiles",0);
        editor = pref2.edit();
        mobile=pref2.getString("mobile","mobile");

        System.out.println("mobile is" + mobile);

        System.out.println("name --->"+name+email+password+mobile);
        editor.remove("username");
        editor.remove("email");
        editor.remove("password");
        editor.commit();


        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_dst = (Spinner) findViewById(R.id.spinner_dst);
        spinner_mandal = (Spinner) findViewById(R.id.spinner_mandal);
        spinner_village = (Spinner) findViewById(R.id.spinner_village);
        et_acers= (EditText)findViewById(R.id.et_acers);
        et_pincode = (EditText) findViewById(R.id.et_pin);
        bt_signup = (Button) findViewById(R.id.bt_signup);


        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pincode=et_pincode.getText().toString();
                if (et_pincode.getText() .toString().trim().length() !=6){
                    Validations.MyAlertBox(FarmDetails.this,"Please Enter Correct Pin Number");
                    et_pincode.requestFocus();
                }
                 acers=et_acers.getText().toString();
                 if (et_acers.getText().toString().equals("")) {
                    Validations.MyAlertBox(FarmDetails.this,"Please enter area in acers");
                    et_acers.requestFocus();
                }
                else if (spinner_state.getSelectedItem().toString().trim().equals("")) {
                    Validations.MyAlertBox(FarmDetails.this,"Please Select a State");
                    spinner_state.requestFocus();
                }
                else if (spinner_dst.getSelectedItem().toString().trim().equals("")) {
                    Validations.MyAlertBox(FarmDetails.this, "Please Select a District");
                    spinner_dst.requestFocus();

                }
                else if (spinner_mandal.getSelectedItem().toString().trim().equals("")){
                    Validations.MyAlertBox(FarmDetails.this,"Please Select a Mandal");
                    spinner_mandal.requestFocus();
                }
                else if (spinner_village.getSelectedItem().toString().trim().equals("")){
                    Validations.MyAlertBox(FarmDetails.this,"Please Select a Village");
                    spinner_village.requestFocus();
                }

                else if (et_pincode.getText().toString().equals("")){
                    Validations.MyAlertBox(FarmDetails.this,"Please enter Pin Code");
                    et_pincode.requestFocus();
                }
                else if (et_pincode.getText().toString().trim().length() !=6){
                    Validations.MyAlertBox(FarmDetails.this,"Please Enter Valid Pin Code");
                    et_pincode.requestFocus();
                }
                else {
                    if (Validations.isConnectedToInternet(FarmDetails.this)){
                                        farmdetails();
                    } else{
                        Validations.MyAlertBox(FarmDetails.this,"Internet Connection Failed");
                    }

                }
                /*Intent home = new Intent(FarmDetails.this,LoginForm.class);
                SharedPreferences farm = getApplicationContext().getSharedPreferences("farmdetails",0);
                editor = farm.edit();
                //System.out.println("mobile is "+pref.getString("mobile","mobile not found"));
                editor.putString("acers",et_acers.getText().toString());
                editor.putString("state",spinner_state.getSelectedItem().toString());
                editor.putString("dist",spinner_dst.getSelectedItem().toString());
                editor.putString("mandal",spinner_mandal.getSelectedItem().toString());
                editor.putString("village",spinner_village.getSelectedItem().toString());
                editor.putString("pincode",et_pin.getText().toString());
                editor.commit();
                startActivity(home);*/

            }
        });

       // new FarmDetails.GetStates().execute();
        states();

        stateDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stateNameList);
        stateDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_state.setAdapter(stateDataAdapter);

        districtDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, districtNameList);
        districtDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dst.setAdapter(districtDataAdapter);

        mandalDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mandalNameList);
        districtDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mandal.setAdapter(mandalDataAdapter);

        villageDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, villageNameList);
        districtDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_village.setAdapter(villageDataAdapter);

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
              String  state = spinner_state.getSelectedItem().toString();
                stateId = stateIdList.get(i);
                states = state;
               System.out.println("state id is" +stateId);
                if( null!= stateId) {
                    //new FarmDetails.GetDistricts(stateId).execute();
                    districts();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_dst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
               String dist = spinner_dst.getSelectedItem().toString();
                districtId = districtIdList.get(i);
                dists = dist;
                System.out.println("districtid is" + districtId);
                //new FarmDetails.GetMandals(districtId).execute();
                mandals();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_mandal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
               String mandal = spinner_mandal.getSelectedItem().toString();
               mandalId = mandalIdList.get(i);
               mandals = mandal;
               System.out.println("mandal id is" + mandalId);
                //new FarmDetails.GetVillage(mandalId).execute();
                villages();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
               String village = spinner_village.getSelectedItem().toString();
                String id = villageIdList.get(i);
                villages = village;
                System.out.println("village id is" + id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void states() {

        RequestQueue queue = Volley.newRequestQueue(FarmDetails.this);

        String url = "https://www.nandikrushi.in/services/states.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                   //JSONObject jsonObject=object.getJSONObject("states");
                    JSONArray jsonArray = jsonObject.getJSONArray("states");
                   // String status=jsonObject.getString("status");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject stateObject = jsonArray.getJSONObject(i);
                        stateNameList.add((String)stateObject.get("stateName"));
                        stateIdList.add((String)stateObject.get("stateID"));
                    }
                    stateDataAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e1) {
                    Validations.MyAlertBox(FarmDetails.this, "Connection Lost");
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
  /*  public class GetStates extends AsyncTask<String,String,JSONObject> {
        private JSONObject json;
        @Override
        protected JSONObject doInBackground(String... strings) {
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/states.php", 2, null);
            return json;
        }

        public GetStates() {
            ArrayList<NameValuePair> nameValuePairs;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                JSONObject result = new JSONObject(jsonObject.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("states");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject stateObject = jsonArray.getJSONObject(i);
                    stateNameList.add((String)stateObject.get("stateName"));
                    stateIdList.add((String)stateObject.get("stateID"));
                }
                stateDataAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e1) {
                Validations.MyAlertBox(FarmDetails.this, "Connection Lost");
            }
        }
    }*/


    public void districts() {

        RequestQueue queue = Volley.newRequestQueue(FarmDetails.this);

        String url = "https://www.nandikrushi.in/services/districts.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //JSONObject jsonObject=object.getJSONObject("states");
                    JSONArray jsonArray = jsonObject.getJSONArray("districts");
                    districtNameList.clear();
                    districtIdList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject stateObject = jsonArray.getJSONObject(i);
                        districtNameList.add((String)stateObject.get("districtName"));
                        districtIdList.add((String)stateObject.get("districtID"));
                    }
                    districtDataAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e1) {
                    Validations.MyAlertBox(FarmDetails.this, "Connection Lost");
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
                 params.put("stateid",stateId);
                // params.put("password",pass);
                // Log.d("stateId", stateId);
                //Log.d("password", pass);
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



   /* public class GetDistricts extends AsyncTask<String,String,JSONObject>{
        private JSONObject json;
        String stateId;
        ArrayList<NameValuePair> nameValuePairs;
        @Override
        protected JSONObject doInBackground(String... strings) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("stateid", stateId));
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/districts.php", 2, nameValuePairs);
            return json;
        }

        public GetDistricts(String stateId) {
            this.stateId = stateId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                JSONObject result = new JSONObject(jsonObject.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("districts");
                districtNameList.clear();
                districtIdList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject stateObject = jsonArray.getJSONObject(i);
                    districtNameList.add((String)stateObject.get("districtName"));
                    districtIdList.add((String)stateObject.get("districtID"));
                }
                districtDataAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e1) {
                Validations.MyAlertBox(FarmDetails.this, "Connection Lost");
            }
        }
    }*/

    public void mandals() {

        RequestQueue queue = Volley.newRequestQueue(FarmDetails.this);

        String url = "https://www.nandikrushi.in/services/mandals.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //JSONObject jsonObject=object.getJSONObject("states");
                    JSONArray jsonArray = jsonObject.getJSONArray("mandals");
                    mandalNameList.clear();
                    mandalIdList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject stateObject = jsonArray.getJSONObject(i);
                        mandalNameList.add((String)stateObject.get("cityName"));
                        mandalIdList.add((String)stateObject.get("cityID"));
                    }
                    mandalDataAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e1) {
                    Validations.MyAlertBox(FarmDetails.this, "Connection Lost");
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
                params.put("districtid",districtId);
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



  /*  public class GetMandals extends AsyncTask<String,String,JSONObject>{
        private JSONObject json;
        String districtId;
        ArrayList<NameValuePair> nameValuePairs;
        @Override
        protected JSONObject doInBackground(String... strings) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("districtid", districtId));
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/mandals.php", 2, nameValuePairs);
            return json;
        }

        public GetMandals(String districtId) {
            this.districtId = districtId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                JSONObject result = new JSONObject(jsonObject.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("mandals");
                mandalNameList.clear();
                mandalIdList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject stateObject = jsonArray.getJSONObject(i);
                    mandalNameList.add((String)stateObject.get("cityName"));
                    mandalIdList.add((String)stateObject.get("cityID"));
                }
                mandalDataAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e1) {
                Validations.MyAlertBox(FarmDetails.this, "Connection Lost");
            }
        }
    }*/


    public void villages() {

        RequestQueue queue = Volley.newRequestQueue(FarmDetails.this);

        String url = "https://www.nandikrushi.in/services/villages.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //JSONObject jsonObject=object.getJSONObject("states");
                    JSONArray jsonArray = jsonObject.getJSONArray("villages");
                    villageIdList.clear();
                    villageNameList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject stateObject = jsonArray.getJSONObject(i);
                        villageNameList.add((String)stateObject.get("villageName"));
                        villageIdList.add((String)stateObject.get("villageID"));
                    }
                    villageDataAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e1) {
                    Validations.MyAlertBox(FarmDetails.this, "Connection Lost");
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
                params.put("mandalid",mandalId);
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

   /* public class GetVillage extends AsyncTask<String,String,JSONObject>{
        private JSONObject json;
        String mandalId;
        ArrayList<NameValuePair> nameValuePairs;

        @Override
        protected JSONObject doInBackground(String... strings) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("mandalid", mandalId));
            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/villages.php", 2, nameValuePairs);
            return json;
        }

        public GetVillage(String mandalId) {
            this.mandalId = mandalId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                JSONObject result = new JSONObject(jsonObject.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("villages");
                villageIdList.clear();
                villageNameList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject stateObject = jsonArray.getJSONObject(i);
                    villageNameList.add((String)stateObject.get("villageName"));
                    villageIdList.add((String)stateObject.get("villageID"));
                }
                villageDataAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e1) {
                Validations.MyAlertBox(FarmDetails.this, "Connection Lost");
            }
        }
    }
*/

    public void farmdetails() {

        RequestQueue queue = Volley.newRequestQueue(FarmDetails.this);

        String url = "https://www.nandikrushi.in/services/register.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject jsonObject=object.getJSONObject("regstatus");
                    String status=jsonObject.getString("status");
                    if (status.contains("1")){
                       /* String  farmerid;
                        farmerid = jsonObject.getString("farmerid");

                        SharedPreferences sharedPreferences = getSharedPreferences("LoginForm", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("moble", ph);
                        editor.putString("password", pass);
                        editor.putString("farmerID", farmerid);
                        editor.commit();*/


                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FarmDetails.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Farmer Registered Successfully");
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                           /* startActivity(new Intent(LoginForm.this, Dashboard.class));
                            dialogInterface.dismiss();*/
                                Intent login = new Intent(FarmDetails.this, Dashboard.class);
                                startActivity(login);
                                finish();
                            }
                        }).show();

                    }else {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FarmDetails.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Please try again later");
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
                params.put("password",password);
                params.put("email",email);
                params.put("username",name);
                params.put("acers",acers);
                params.put("state",states);
                params.put("district",dists);
                params.put("mandal",mandals);
                params.put("village",villages);
                params.put("pincode",pincode);

                System.out.println("values are" +mobile +password +email +name +acers +states +dists +mandals + villages + pincode);
               // Log.d("mobile", ph);
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



    /* private class farmdetails extends AsyncTask<String, String, JSONObject> {

        private ArrayList<NameValuePair> nameValuePairs;
        private JSONObject json;
        String email,mobile,username,password,acers,state,district,mandal,village,pincode;

        public farmdetails(String name,
                           String email,
                           String password,
                           String mobile,
                           String acers,
                           String state,
                           String district,
                           String mandal,
                           String village,
                           String pincode) {
            ArrayList<NameValuePair> nameValuePairs;
            this.email=email;
            this.mobile=mobile;
            this.username=name;
            this.password=password;
            this.acers=acers;
            this.state=state;
            this.district=district;
            this.mandal=mandal;
            this.village=village;
            this.pincode=pincode;

        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email",email));
            nameValuePairs.add(new BasicNameValuePair("mobile",mobile));
            nameValuePairs.add(new BasicNameValuePair("username",username));
            nameValuePairs.add(new BasicNameValuePair("password",password));
            nameValuePairs.add(new BasicNameValuePair("acers",acers));
            nameValuePairs.add(new BasicNameValuePair("state", state));
            nameValuePairs.add(new BasicNameValuePair("district", district));
            nameValuePairs.add(new BasicNameValuePair("mandal",mandal));
            nameValuePairs.add(new BasicNameValuePair("village",village));
            nameValuePairs.add(new BasicNameValuePair("pincode",pincode));




            json = JSONParser.makeServiceCall("https://www.nandikrushi.in/services/register.php", 2, nameValuePairs);
            return json;
        }


        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            String status = "1";
            try {
                JSONArray jsonArray = new JSONArray();
                JSONObject data = jsonObject.getJSONObject("regstatus");
                status = data.getString("status");
                if (status.equalsIgnoreCase("1")) {

                    //Toast.makeText(getBaseContext(),data.toString(), Toast.LENGTH_SHORT).show();

                *//*    Log.d("email", data.getString("email").toString());
                    Log.d("mobile", data.getString("mobile").toString());
                    Log.d("userName", data.getString("userName").toString());
                    Log.d("password", data.getString("password").toString());
                    Log.d("acers", data.getString("acers").toString());
                    Log.d("state", data.getString("state").toString());
                    Log.d("district", data.getString("district").toString());
                    Log.d("mandal", data.getString("mandal").toString());
                    Log.d("village", data.getString("village").toString());
                    //Log.d("phone", data.getString("phone").toString());
                    Log.d("pincode",data.getString("pincode").toString());*//*


                   *//* String  email,mobile,  username, password, acers, state,district,mandal,village,pincode;
                    email = data.getString("email").toString();
                    mobile = data.getString("mobile").toString();
                    username = data.getString("userName").toString();
                    password = data.getString("password").toString();
                    // phone = data.getString("phone").toString();
                    acers = data.getString("acers").toString();
                    state = data.getString("state").toString();
                    district = data.getString("district").toString();
                    mandal = data.getString("mandal").toString();
                    village = data.getString("village").toString();
                    pincode = data.getString("pincode").toString();*//*

                    overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FarmDetails.this);
                    builder.setTitle("Nandi Krushi");
                    builder.setMessage("User Registered Successfully");
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(FarmDetails.this, Dashboard.class));
                            dialogInterface.dismiss();
                        }
                    }).show();
                }
                else {

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FarmDetails.this);
                        builder.setTitle("Nandi Krushi");
                        builder.setMessage("Please try again later");
                        builder.setPositiveButton(R.string.ok, null).show();

                    //  Validations.MyAlertBox(Signup.this,"User already exist");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e1) {
                Validations.MyAlertBox(FarmDetails.this, "Connection Lost");
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
