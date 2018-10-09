package com.example.adimn.myapplication;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class PersonalDetails extends AppCompatActivity {
    EditText et_name,et_pswd,et_email;
    Button bt_next;
    ImageView img_logo,img_camera;
    String imageUploadValue, filePath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int REQUEST_CAMERA = 100, SELECT_FILE = 1;
    static final int PICK_IMAGE_REQUEST = 1;
    private String userChoosenTask;
    Uri uri;
    Uri imagetoupload;

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        img_logo=(ImageView)findViewById(R.id.img_logo);
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PersonalDetails.this ,CreateAccount.class);
                startActivity(i);
            }
        });

        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_pswd = (EditText) findViewById(R.id.et_pswd);
        bt_next = (Button) findViewById(R.id.bt_next);
        img_camera = (ImageView) findViewById(R.id.img_camera);

        activity = PersonalDetails.this;

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().equals("")) {
                    Validations.MyAlertBox(PersonalDetails.this, "Please Enter Name");
                    et_name.requestFocus();
                } else if (et_email.getText().toString().equals("")) {
                    Validations.MyAlertBox(PersonalDetails.this, "Please Enter Email");
                    et_email.requestFocus();
                } else if (et_pswd.getText().toString().length() < 5) {
                    Validations.MyAlertBox(PersonalDetails.this, "Please Enter Valid Password(min 5 Characters)");
                    et_pswd.requestFocus();
                }
                //new PersionalDetails.persionaldetails(et_name.getText().toString(),et_email.getText().toString(),et_pswd.getText().toString()).execute();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("farmPref",0);
                SharedPreferences.Editor editor = pref.edit();
               // System.out.println("mobile is "+pref.getString("mobile","mobile not found"));
                editor.putString("username",et_name.getText().toString());
                editor.putString("email",et_email.getText().toString());
                editor.putString("password",et_pswd.getText().toString());
                editor.commit();

                Intent i = new Intent(PersonalDetails.this,FarmDetails.class);
                startActivity(i);

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
