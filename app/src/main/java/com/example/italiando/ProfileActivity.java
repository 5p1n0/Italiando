package com.example.italiando;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.italiando.adapter.HistoryAdapter;
import com.example.italiando.data.Attempt;
import com.example.italiando.data.User;
import com.example.italiando.data.UserDatabase;
import com.example.italiando.data.UserDatabaseClient;
import com.example.italiando.other.SharedPref;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.util.ArrayList;
import java.util.Collections;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        CardView cvEditPassword = findViewById(R.id.cvEditPassword);
        TextView tvUsername = findViewById(R.id.tvUsernameProfile);

        SharedPref sharedPref = SharedPref.getInstance();
        User user = sharedPref.getUser(this);
        CharSequence fullText = "Hello, " + user.getUsername();
        tvUsername.setText(fullText);

        String email = user.getEmail();
        CalculateLevelTask calculateLevelTask = new CalculateLevelTask(email);
        calculateLevelTask.execute();

        findViewById(R.id.imageProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cvEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditPasswordActivity.class));
            }
        });

        }

    class CalculateLevelTask extends AsyncTask<Void, Void, Void> {
        private final String email;
        ArrayList<Attempt> attempts = new ArrayList<>();
        int userLevel;

        public CalculateLevelTask(String email) {
            this.email = email;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //to WorkManager
            UserDatabase databaseClient = UserDatabaseClient.getInstance(getApplicationContext());
            attempts = (ArrayList<Attempt>) databaseClient.userDao().getUserAndAttemptsWithSameEmail(email);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            int overallPoints = 0;

            for (Attempt userWithAttempts : attempts) {
                overallPoints += userWithAttempts.getEarned();
            }

            int levelThreshold = 40;
            this.userLevel = 1;
            while (overallPoints > levelThreshold) {
                levelThreshold = (int) (levelThreshold * 2.20);
                this.userLevel++;
            }

            float levelThresholdPerc = (float) levelThreshold / 100;

            Log.d("LevelThresholdPerc ", String.valueOf(levelThresholdPerc));

            int fillPercent = (int) (overallPoints / levelThresholdPerc);

            Log.d("fillPercent ", String.valueOf(fillPercent));

            ColorfulRingProgressView crp = (ColorfulRingProgressView) findViewById(R.id.crpLevel);
            crp.setPercent(fillPercent);

            TextView tvPercent = findViewById(R.id.tvPercent);
            tvPercent.setText(String.valueOf(fillPercent));

            TextView tvLevel = findViewById(R.id.tvLevel);
            tvLevel.setText((CharSequence) "Lv. " + String.valueOf(this.userLevel));

            TextView tvPoints = findViewById(R.id.tvPoints);
            tvPoints.setText((CharSequence) String.valueOf(overallPoints)+ "/" + String.valueOf(levelThreshold) + " pt");

            findViewById(R.id.share).setOnClickListener(view -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                CharSequence  sendValue = (CharSequence) "\uD83E\uDD0C \uD83C\uDDEE\uD83C\uDDF9  Look how much i've improved my italian with Italiando! i'm already at level " + String.valueOf(this.userLevel) + " mamma mia! \uD83C\uDDEE\uD83C\uDDF9 \uD83E\uDD0C";
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendValue);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);}
            );

        }
    }
}
