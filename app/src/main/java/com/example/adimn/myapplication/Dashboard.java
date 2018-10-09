package com.example.adimn.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {
    ImageView img_myproducts,img_addproducts,img_orders,img_pending,img_mypurchases,img_profile;
    EditText et_location,et_products;
    String farmerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        et_products = (EditText) findViewById(R.id.et_products);
       // et_location = (EditText) findViewById(R.id.et_location);
        img_myproducts =(ImageView) findViewById(R .id.img_myproduct);
        img_addproducts =(ImageView) findViewById(R .id.img_addproduct);
        img_orders =(ImageView) findViewById(R .id.img_orders);
        img_pending =(ImageView) findViewById(R .id.img_pending);
        img_mypurchases =(ImageView) findViewById(R .id.img_mypurchases);
        img_profile =(ImageView) findViewById(R .id.img_profile);


        /*et_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, MapActivity.class);
                startActivity(i);
            }
        });*/

        SharedPreferences sharedPreferences = getSharedPreferences("LoginForm", Context.MODE_PRIVATE);
     //   SharedPreferences.Editor editor = sharedPreferences.edit();
        farmerID = sharedPreferences.getString("farmerID", "");
        et_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Dashboard.this,MapActivity.class);
                startActivity(j);
            }
        });

        img_addproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_addproducts.setImageResource(R.drawable.addproductred);
                Intent add =new Intent(Dashboard.this,AddProducts.class);
                startActivity(add);

            }
        });

        img_myproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_myproducts.setImageResource(R.drawable.myproductred);
                Intent my = new Intent(Dashboard.this,MyproductListActivity.class);
                startActivity(my);
            }
        });

        img_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_orders.setImageResource(R.drawable.orderred);
                Intent orders = new Intent(Dashboard.this,OrderListActivity.class);
                startActivity(orders);
            }

        });

        img_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_pending.setImageResource(R.drawable.pendingred);
                Intent pending = new Intent(Dashboard.this,PendingListActivity.class);
                startActivity(pending);
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_profile.setImageResource(R.drawable.profileupdatered);
                Intent update = new Intent(Dashboard.this,ProfileUpdate.class);
                startActivity(update);
            }
        });
        img_mypurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_mypurchases.setImageResource(R.drawable.mypurchasered);
                Intent purchase = new Intent(Dashboard.this,PurchaseListActivity.class);
                startActivity(purchase);
            }
        });
    }
    /*@Override
    public void onBackPressed() {
        return;
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

