package com.coetusstudio.iimtufaculty.Activity.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.coetusstudio.iimtufaculty.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread(){

            public void run(){
                try {
                    sleep(500);

                }catch (Exception e){
                    e.printStackTrace();

                }finally {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();

                }
            }
        };thread.start();
    }
}