package com.riyad.go4lunch.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.riyad.go4lunch.MainActivity;
import com.riyad.go4lunch.R;

import java.util.concurrent.TimeUnit;

public class WorkerNotification extends Worker {
    public WorkerNotification(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("DoWork", "inDo work");
        //TODO appel firestore pour récupérer l'utilisateur
        //TODO si le booking restaurant n'est pas null, prendre l'id du restaurant et récuperer ce restaurant dans firestore.
        //TODO récuperer la liste des users qui ont reservé sans oublier d'enlever le current user.
        //TODO appeler show notification.
        showNotification();
        return Result.success();
    }




    public static void periodRequest(){

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(WorkerNotification.class,
                1, TimeUnit.SECONDS)
                .setInitialDelay(1, TimeUnit.SECONDS)
//                .setConstraints(setConst())
                .build();


        WorkManager.getInstance().enqueueUniquePeriodicWork("periodic", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }


    public void showNotification(){

        Log.e("Notif", "here");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(getApplicationContext(), "4")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Title")
                .setContentText("text")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(4, notificationCompat.build());

    }

    private static Constraints setConst() {
        Constraints constraints = new Constraints.Builder()
                .build();
        return constraints;
    }
}
