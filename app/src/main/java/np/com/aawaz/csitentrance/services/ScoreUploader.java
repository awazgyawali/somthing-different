package np.com.aawaz.csitentrance.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import np.com.aawaz.csitentrance.R;
import np.com.aawaz.csitentrance.objects.SPHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScoreUploader extends Service {

    String url;
    Receiver receiver;

    public ScoreUploader() {
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        if (receiver == null)
            receiver = new Receiver();
        registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        uploader();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void uploader() {
        String image_link = "N/A";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        try {
            image_link = user.getPhotoUrl().toString();
        } catch (Exception ignored) {
        }
        url = getString(R.string.uploadScore) + "?title=" + user.getDisplayName()
                + "&email=" + user.getEmail()
                + "&score=" + SPHandler.getInstance().getTotalScore()
                + "&image_link=" + image_link
                + "&phone_number=" + SPHandler.getInstance().getPhoneNo()
                + "&instance_id=" + FirebaseInstanceId.getInstance().getToken();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ScoreUploader.this.stopSelf();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful())
                    SPHandler.getInstance().setScoreChanged(false);
                stopSelf();
            }
        });

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ||
                        activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    uploader();
            }
        }
    }
}
