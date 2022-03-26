package com.example.italiando;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.example.italiando.data.Attempt;
import com.example.italiando.data.UserDatabase;
import com.example.italiando.data.UserDatabaseClient;
import com.example.italiando.other.Constants;
import com.example.italiando.other.SharedPref;
import com.example.italiando.other.Utils;

import java.util.Calendar;

public class FinalResultActivity extends AppCompatActivity {

    private TextView tvSubject, tvCorrect, tvIncorrect, tvEarned, tvOverallStatus, tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);

        Intent intent = getIntent();
        int correctAnswer = intent.getIntExtra(Constants.CORRECT, 0);
        int incorrectAnswer = intent.getIntExtra(Constants.INCORRECT, 0);
        String subject = intent.getStringExtra(Constants.SUBJECT);
        String email = SharedPref.getInstance().getUser(this).getEmail();
        int earnedPoints = (correctAnswer * Constants.CORRECT_POINT) - (incorrectAnswer * Constants.INCORRECT_POINT);

        tvSubject = findViewById(R.id.textView16);
        tvCorrect = findViewById(R.id.textView19);
        tvIncorrect = findViewById(R.id.textView27);
        tvEarned = findViewById(R.id.textView28);
        tvOverallStatus = findViewById(R.id.textView29);
        tvDate = findViewById(R.id.textView30);

        findViewById(R.id.imageViewFinalResultQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinalResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.btnFinishQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinalResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Attempt attempt = new Attempt(
                Calendar.getInstance().getTimeInMillis(),
                subject,
                correctAnswer,
                incorrectAnswer,
                earnedPoints,
                email
        );

        getOverAllPoints(attempt);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getOverAllPoints(Attempt attempt) {
        GetOverallPointsTask getOverallPointsTask = new GetOverallPointsTask(attempt);
        getOverallPointsTask.execute();
    }

    class GetOverallPointsTask extends AsyncTask<Void, Void, Void> {

        private final Attempt attempt;
        private int overallPoints = 0;
        private int oldUserLevel = 1;
        private int newUserLevel = 1;
        private int levelThreshold = 40;

        public GetOverallPointsTask(Attempt attempt) {
            this.attempt = attempt;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            UserDatabase databaseClient = UserDatabaseClient.getInstance(getApplicationContext());
            overallPoints = databaseClient.userDao().getOverAllPoints(attempt.getEmail());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            while (overallPoints > levelThreshold) {
                levelThreshold = (int) (levelThreshold * 2.20);
                oldUserLevel++;
            }

            levelThreshold = 40;
            int newOverallPoints = overallPoints + attempt.getEarned();

            while (newOverallPoints > levelThreshold) {
                levelThreshold = (int) (levelThreshold * 2.20);
                newUserLevel++;
            }

            Log.d("FINAL: old", String.valueOf(oldUserLevel));
            Log.d("FINAL: new", String.valueOf(newUserLevel));

            if (newUserLevel > oldUserLevel) {
                // Notification setup
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"LevelUp" )
                        // custom app icon
                        .setSmallIcon(R.drawable.icon)
                        // Title
                        .setContentTitle("You've leveled up!")
                        .setContentText("Keep the good work! \uD83D\uDCAA")
                        // Priority
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                // Show notification
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(42, builder.build());
            }

            attempt.setOverallPoints(overallPoints + attempt.getEarned());
            displayData(attempt);
            SaveUserAttemptTask saveUserAttemptTask = new SaveUserAttemptTask(attempt);
            saveUserAttemptTask.execute();

            Log.d("OVERALL POINTS", String.valueOf(overallPoints));
        }
    }

    private void displayData(Attempt attempt) {

        tvSubject.setText(attempt.getSubject());
        tvCorrect.setText(String.valueOf(attempt.getCorrect()));
        tvIncorrect.setText(String.valueOf(attempt.getIncorrect()));
        tvEarned.setText(String.valueOf(attempt.getEarned()));
        tvOverallStatus.setText(String.valueOf(attempt.getOverallPoints()));
        tvDate.setText(Utils.formatDate(attempt.getCreatedTime()));

    }

    class SaveUserAttemptTask extends AsyncTask<Void, Void, Void> {

        private Attempt attempt;

        public SaveUserAttemptTask(Attempt attempt) {
            this.attempt = attempt;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            UserDatabase databaseClient = UserDatabaseClient.getInstance(getApplicationContext());
            databaseClient.userDao().insertAttempt(attempt);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Attempt Saved", attempt.toString());
        }
    }

}
