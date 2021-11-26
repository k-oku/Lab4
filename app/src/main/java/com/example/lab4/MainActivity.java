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
    private int downloadProgressStatus;

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
            downloadProgressStatus = downloadProgress;
            if(stopThread) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setText("Start");

                    }
                });

            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(stopThread = false) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("Download Progress: " + downloadProgressStatus + "%");
                    }
                });
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(" ");
                    }
                });
            }

        }

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
