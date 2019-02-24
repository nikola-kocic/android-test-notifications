package example.com.testnotifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "notifkanal";
    private int notificationId = 0;

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My channel name";
            String description = "My channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(boolean ongoing) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notificon)
                .setContentTitle("My title")
                .setContentText("My content #" + notificationId)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .setOngoing(ongoing);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
        notificationId++;
    }

    private void clearAllNotifications() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancelAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        findViewById(R.id.show_notif).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                final boolean ongoing = ((CheckBox)findViewById(R.id.checkBox_ongoing)).isChecked();
                showNotification(ongoing);
            }
        });
        findViewById(R.id.show_notif_delayed).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                final Handler handler = new Handler();
                final boolean ongoing = ((CheckBox)findViewById(R.id.checkBox_ongoing)).isChecked();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showNotification(ongoing);
                    }
                }, 5000);
            }
        });
        findViewById(R.id.clear_all_notifs).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                clearAllNotifications();
            }
        });
    }
}
