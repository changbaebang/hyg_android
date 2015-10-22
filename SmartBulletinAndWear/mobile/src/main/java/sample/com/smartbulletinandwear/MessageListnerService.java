package sample.com.smartbulletinandwear;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MessageListnerService extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String data_string = new String(messageEvent.getData());

        super.onMessageReceived(messageEvent);

        Toast.makeText(this, data_string, Toast.LENGTH_SHORT).show();

        Intent calIntent = new Intent(Intent.ACTION_MAIN);
        calIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        calIntent.addCategory(Intent.CATEGORY_APP_CALENDAR);
        startActivity(calIntent);
    }
}
