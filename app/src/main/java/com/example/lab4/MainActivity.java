package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button startButton;
    private Button stopButton;
    private volatile boolean stopThread = false;
    private TextView textView;
    private String downloadProgressStatus;

    public class ExampleRunnable implements Runnable {
        //nested runnable interface
        //tell our thread what task we want towork on
        @Override
        public void run() {
            mockFileDownloader();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload(this);
            }
        });

        stopButton = findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDownload(this);
            }
        });

        textView = findViewById(R.id.textView);
        textView.setText("no download in progress");

    }

    //output progress of our "download" to Logcat
    public void mockFileDownloader() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Downloading...");

            }
        });

        //mock "Download Progress" increases by 10% each second
        for (int downloadProgress = 0; downloadProgress <= 100; downloadProgress = downloadProgress + 10) {
            downloadProgressStatus = String.valueOf(downloadProgress);
            Log.d(TAG, "Download Progress:" + downloadProgress + "%");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("Download Progress:" + downloadProgressStatus + "%");
                }

            });


            if(stopThread) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setText("Start");

                    }
                });

            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("no download in progress");

                    }
                });
            }

        }
        //M1-10 last working part - make code look like that if you want to start from scratch for M-11
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Start");
            }
        });
    }

    public void startDownload(View.OnClickListener view) {
        stopThread = false;
        ExampleRunnable runnable = new MainActivity.ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View.OnClickListener view) {
        stopThread = true;
    }

}
