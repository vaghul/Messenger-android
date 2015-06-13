package vaghul.websevice.pura;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.net.URI;

public class GcmMessageHandler extends IntentService {

    String mes,fphone,name,tophone;
    private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("message");
        fphone = extras.getString("fromphone");
        tophone = extras.getString("tophone");
        name =extras.getString("username");
        showToast();

        Log.i("GCM", "Received : (" +messageType+")  "+extras.getString("title"));

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(),mes+fphone , Toast.LENGTH_LONG).show();
                NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.common_signin_btn_icon_normal_light)
                        .setContentTitle("Pura")
                        .setContentText("you have a notification from "+name);
                // Creates an explicit intent for an Activity in your app
                SQLiteDatabase db=openOrCreateDatabase("pura", Context.MODE_PRIVATE,null);

                Log.w("tophne",tophone);
                Log.w("from",fphone);

                db.execSQL("insert into conversation(fromphone,tophone,message,sent ) values('"+tophone+"','"+fphone+"','"+mes+"',0);");
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

                // The stack builder object will contain an artificial back stack for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out of
                // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(MainActivity.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                Uri alarm= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(alarm);
                mBuilder.setVibrate(new long[]{1000,1000,1000,1000,1000});
                mBuilder.setContentIntent(resultPendingIntent);
                mBuilder.setAutoCancel(true);

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // mId allows you to update the notification later on.

                mNotificationManager.notify(1, mBuilder.build());


                //mNotificationManager.cancel(1);


            }
        });

    }
}