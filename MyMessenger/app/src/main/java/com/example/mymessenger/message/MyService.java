package com.example.mymessenger.message;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import com.example.mymessenger.MainActivity;
import com.example.mymassanger.R;
import com.example.mymessenger.users.UserListFr;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service {
    NotificationManager notificationManager;
    String string = UserListFr.KEY;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        writeDatabase();
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void writeDatabase() {
        final DatabaseReference myRef = database.getReference("notification").child(string);
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value != null && value.equals("send")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        createNotificationChannel();
                    }
                    getNotificationTitle();
                    myRef.setValue("aaa");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getNotificationTitle() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                DatabaseReference myRef = database.getReference("notification").child(string + 1);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String value = dataSnapshot.getValue(String.class);
                        sendNotification(value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {

        NotificationChannel channel = new NotificationChannel("CHANAL_ID", "CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("channel description");
        channel.enableLights(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationManager.createNotificationChannel(channel);

    }

    public void sendNotification(final String str) {
        final DatabaseReference myRef = database.getReference("notification").child(string + 2);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.setAction(Intent.ACTION_MAIN);
                resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationCompat.Builder notificationBuilder
                            = new NotificationCompat.Builder(getApplicationContext(), "CHANAL_ID").setOngoing(true);
                    notificationBuilder.setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.drawable.ic_arrow_forward_black_24dp)
                            .setContentTitle(str)
                            .setContentIntent(resultPendingIntent)
                            .setContentText(value)
                            .setVisibility(Notification.VISIBILITY_PUBLIC)
                            .setContentInfo("Info");
                    startForeground(1, notificationBuilder.build());
//                    notificationManager.notify(1, notificationBuilder.build());

                } else {
                    Notification.Builder notification = new Notification.Builder(getApplicationContext())
                            .setCategory(Notification.CATEGORY_MESSAGE)
                            .setContentTitle(str)
                            .setContentText(value)
                            .setWhen(System.currentTimeMillis())

                            .setContentIntent(resultPendingIntent)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setAutoCancel(true)
                            .setVisibility(Notification.VISIBILITY_PUBLIC);
                    // notificationManager.notify(1,notification.build());
                    startForeground(1, notification.build());

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}
