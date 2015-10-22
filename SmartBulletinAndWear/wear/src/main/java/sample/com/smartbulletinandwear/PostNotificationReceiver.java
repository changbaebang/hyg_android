package sample.com.smartbulletinandwear;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PostNotificationReceiver extends BroadcastReceiver {
    public static final String CONTENT_KEY = "contentText";
    public static final String POST_MESSAGE = "postMessage";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent displayIntent = new Intent(context, MainActivity.class);
        PendingIntent displayPendingIntent = PendingIntent.getActivity(context, 0, displayIntent, 0);

        String text = intent.getStringExtra(CONTENT_KEY);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(text)
                .setContentIntent(displayPendingIntent)
                .build();

        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
        String postMessage = intent.getStringExtra(POST_MESSAGE);
        if(postMessage==null ||  postMessage.isEmpty())
            postMessage = context.getString(R.string.notification_posted);
        Toast.makeText(context, postMessage, Toast.LENGTH_SHORT).show();
    }
}
