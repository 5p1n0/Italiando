package com.example.italiando;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.italiando.data.User;
import com.example.italiando.other.SharedPref;

import java.util.concurrent.TimeUnit;

import static com.example.italiando.other.Utils.cancelJobs;
import static com.example.italiando.other.Utils.jobNotEnqueued;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvUsername = findViewById(R.id.tvUsernameHome);
        CardView cvStartQuiz = findViewById(R.id.cvStartQuiz);
        CardView cvRule = findViewById(R.id.cvRule);
        CardView cvHistory = findViewById(R.id.cvHistory);
        CardView cvEditProfile = findViewById(R.id.cvEditProfile);
        CardView cvLogout = findViewById(R.id.cvLogout);

        SharedPref sharedPref = SharedPref.getInstance();
        User user = sharedPref.getUser(this);
        tvUsername.setText("Ciao, " + user.getUsername());

        cvStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, QuizOptionActivity.class));
            }
        });

        cvRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RuleActivity.class));
            }
        });

        cvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

        cvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref.clearSharedPref(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Notification channel setup
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Channel name
            CharSequence name = "Training";
            // Channel description
            String description = "Training reminder";
            // Priority
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            // Building
            NotificationChannel channel = new NotificationChannel("Reminder", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            name = "Level Up";
            description = "Level Up notification";
            channel = new NotificationChannel("LevelUp", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        // All
        //cancelJobs(WorkManager.getInstance(getApplicationContext()));

        // Launching a Check job when no job is enqueued
        if (jobNotEnqueued(WorkManager.getInstance(getApplicationContext()))) {

            Log.d("MAIN:", "No enqueued job");

            WorkRequest checkRequest = new OneTimeWorkRequest
                    .Builder(Check.class)
                    .addTag("CheckJob")
                    .build();

            WorkManager
                    .getInstance(getApplicationContext())
                    .enqueue(checkRequest);
        }
    }
}