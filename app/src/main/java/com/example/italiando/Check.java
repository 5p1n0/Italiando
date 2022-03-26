package com.example.italiando;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.italiando.data.Attempt;
import com.example.italiando.data.UserDatabaseClient;
import com.example.italiando.other.SharedPref;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Check extends Worker {

    public Check(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public ListenableWorker.Result doWork() {

        // Attempts fetch
        String email = SharedPref.getInstance().getUser(getApplicationContext()).getEmail();
        ArrayList<Attempt> attempts = new ArrayList<>();
        attempts = (ArrayList<Attempt>) UserDatabaseClient
            .getInstance(getApplicationContext())
            .userDao()
            .getUserAndAttemptsWithSameEmail(email);

        // No attempts check
        if (attempts.isEmpty()) {
            Log.d("IN: No attempts ", String.valueOf(true));

            // Getting now time
            int rightNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

            // hours to delay
            int hoursDiff;
            if (rightNow >= 10) hoursDiff = 10 + (24 - rightNow);
            else hoursDiff = 10 - rightNow;

            Log.d("IN: Next CheckJob delay", String.valueOf(hoursDiff));

            // Scheduling next job
            WorkRequest checkRequest = new OneTimeWorkRequest
                    .Builder(Check.class)
                    .setInitialDelay(hoursDiff, TimeUnit.HOURS)
                    .addTag("CheckJob")
                    .build();

            WorkManager
                    .getInstance(getApplicationContext())
                    .enqueue(checkRequest);

            return  ListenableWorker.Result.success();
        }

        // Stale time check
        long nowTime = Calendar.getInstance().getTimeInMillis();
        long staleTime = nowTime - attempts.get(attempts.size() - 1).getCreatedTime();
        double staleDays = (double) staleTime/86400000;

        // Getting now time
        int rightNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        // Logs
        Log.d("IN: Stale days ", String.valueOf(staleDays));

        if (staleDays >= 1.0 && rightNow >= 10 && rightNow < 22) {

            // Launch Activity from notification
            Intent resultIntent = new Intent(getApplicationContext(),QuizOptionActivity.class);
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            // Get the PendingIntent containing the entire back stack
            PendingIntent quizPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            // Notification setup
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"Reminder" )
                    // custom app icon
                    .setSmallIcon(R.drawable.icon)
                    // Title
                    .setContentTitle("Hey! You aren't training enough!")
                    // text expansion
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Remember to train everyday! Tap me to start training ;)"))
                    .setContentText("Remember to train everyday! Tap me to start training ;)")
                    // Activity to start when selected
                    .setContentIntent(quizPendingIntent)
                    // Priority
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Show notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            notificationManager.notify(43, builder.build());

            // Log
            Log.d("IN: CheckJob ", "launched notification");

            int hoursDiff = 24 - (rightNow - 10);
            Log.d("IN: Next CheckJob delay", String.valueOf(hoursDiff));

            // Scheduling next job
            WorkRequest checkRequest = new OneTimeWorkRequest
                    .Builder(Check.class)
                    .setInitialDelay(hoursDiff, TimeUnit.HOURS)
                    .addTag("CheckJob")
                    .build();

            WorkManager
                    .getInstance(getApplicationContext())
                    .enqueue(checkRequest);

            // Job end
            return ListenableWorker.Result.success();
        } else {

            Log.d("IN: CheckJob ", "delayed");

            // hours to delay
            int hoursDiff;
            if (rightNow >= 10) hoursDiff = 10 + (24 - rightNow);
            else hoursDiff = 10 - rightNow;

            Log.d("IN: Next CheckJob delay", String.valueOf(hoursDiff));

            // Scheduling next job
            WorkRequest checkRequest = new OneTimeWorkRequest
                    .Builder(Check.class)
                    .setInitialDelay(hoursDiff, TimeUnit.HOURS)
                    .addTag("CheckJob")
                    .build();

            WorkManager
                    .getInstance(getApplicationContext())
                    .enqueue(checkRequest);

            // Job end
            return ListenableWorker.Result.success();
        }


    }
}
