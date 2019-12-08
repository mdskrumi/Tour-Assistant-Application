package com.example.rumi.tourassistant;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class GeofenceService extends IntentService {

    private final String CHANNEL_ID = "geofence channel";

    public GeofenceService() {
        super("GeofenceService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        int transition = event.getGeofenceTransition();
        String transitionType = "";
        List<String> names = new ArrayList<>();

        switch (transition){
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                transitionType = "entered";
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                transitionType = "exited";
                break;
        }

        List<Geofence> triggeringGeofenceList =
                event.getTriggeringGeofences();

        for(Geofence g : triggeringGeofenceList){
            names.add(g.getRequestId());
        }

        String notificationString =
                "You have "+transitionType+" "+
                        TextUtils.join(", ",names);
        sendNotifacation(notificationString);
    }

    private void sendNotifacation(String notificationString) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setContentTitle("Geofence Detected");
        builder.setContentText(notificationString);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID,"default",
                            NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(555,builder.build());
    }


}
