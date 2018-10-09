package com.example.adimn.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("LoginForm", Context.MODE_PRIVATE);
                if (sharedPreferences.contains("mobile") && sharedPreferences.contains("password")){
                    Intent i = new Intent(SplashScreen.this,Dashboard.class);
                    startActivity(i);
                    finish();

                }else{
                    Intent j = new Intent(SplashScreen.this,LoginForm.class);
                    startActivity(j);
                    finish();
                }

            }

        },3000);


    }
}
