package sample.com.smartbulletinandwear;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class GoogleApiClientSender {
    public static final String START_ACTIVITY = "/start_activity";
    public static final String WEAR_MESSAGE_PATH = "/message";

    private static GoogleApiClient mApiClient;
    private static Context mContext;


    public static void init(Context context){
        if(mContext != null && mContext == context)
            return;

        mContext = context;
        initGoogleApiClient(mContext);
    }
    public static void destory(){
        mContext = null;

        if(mApiClient == null)
            return;

        mApiClient.disconnect();
        mApiClient = null;
    }

    private static void initGoogleApiClient(Context context) {
        mApiClient = new GoogleApiClient.Builder( context )
                .addApi( Wearable.API )
                .build();

        mApiClient.connect();
    }

    public static void sendMessage(final String path, final String text) {
        if(mApiClient == null)
            return;

        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }
}
