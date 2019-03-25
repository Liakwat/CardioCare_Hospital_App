package com.sayed.cardiocare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView imageViewMoney;
    TextView imageViewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.splash_progressbar);
        imageViewMoney = findViewById(R.id.iv_money);
        imageViewText = findViewById(R.id.iv_text);

        imageViewMoney.setTranslationY(-1200);
        imageViewText.setTranslationY(1200);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                startApp();
                //default method to finishthe activity
                finish();
            }
        }).start();

        imageViewMoney.animate().translationYBy(1200).setDuration(800);
        imageViewText.animate().translationYBy(-1200).setDuration(800);

    }

    private void doWork() {

        for (int i = 0; i < 100; i += 10) {
            try {

                Thread.sleep(200);
                progressBar.setProgress(i);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void startApp() {
        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}