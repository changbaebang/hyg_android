package sample.com.smartbulletinandwear;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class ListnerService extends WearableListenerService {
    private static final String START_ACTIVITY = "/start_activity";
    private static final String WEAR_MESSAGE_PATH = "/message";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String data_string = new String(messageEvent.getData());
        Log.d("wear message", "data : " + data_string);
        Log.d("wear message", "path : " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase( START_ACTIVITY ) ) {
            Intent intent = new Intent( this, MainActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if( messageEvent.getPath().equalsIgnoreCase( WEAR_MESSAGE_PATH ) ) {
            Intent i = new Intent();
            i.setAction("sample.com.smartbulletinandwear.SHOW_NOTIFICATION");
            i.putExtra(PostNotificationReceiver.POST_MESSAGE, data_string);
            i.putExtra(PostNotificationReceiver.CONTENT_KEY, getString(R.string.title));
            sendBroadcast(i);
        } else {
            Toast.makeText(this, data_string, Toast.LENGTH_SHORT).show();
            super.onMessageReceived(messageEvent);
        }

    }
}
