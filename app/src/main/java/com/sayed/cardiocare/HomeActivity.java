package com.sayed.cardiocare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void clicker(View v){
        if(v.getId() == R.id.login_cv){
            Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(intent);

        }else if(v.getId() == R.id.appointment_cv){
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            intent.putExtra("home_msg","Please select your Doctor for the Appointment.");
            startActivity(intent);

        }else if(v.getId() == R.id.test_report_cv){
            Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
            intent.putExtra("home_msg","Please Login to your Account to view the Report.");
            startActivity(intent);

        }else if(v.getId() == R.id.doctor_cv){
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intent);

        }
    }

}
