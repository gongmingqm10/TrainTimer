package net.gongmingqm10.traintimer.sync;

import android.accounts.Account;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import net.gongmingqm10.traintimer.R;
import net.gongmingqm10.traintimer.TrainApp;
import net.gongmingqm10.traintimer.data.Trip;
import net.gongmingqm10.traintimer.event.TripsUpdateEvent;
import net.gongmingqm10.traintimer.ui.activity.HomeActivity;

import java.util.List;

import de.greenrobot.event.EventBus;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        List<Trip> trips = TripSyncManager.getInstance().sync();

        if (!trips.isEmpty()) {
            EventBus.getDefault().post(new TripsUpdateEvent(trips));
            updateNotification(trips);
        }
    }

    private void updateNotification(List<Trip> trips) {
        for (Trip trip : trips) {
            if (trip.getHasReminder() && !TextUtils.isEmpty(trip.getArriveTime())) {
                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.mipmap.ic_directions_railway)
                        .setContentTitle(trip.getTrainNumber() + "列车时刻更新")
                        .setContentText(trip.getDepartMessage())
                        .setLargeIcon(bitmap)
                        .setAutoCancel(true);
                Intent resultIntent  = new Intent(TrainApp.getInstance(), HomeActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(TrainApp.getInstance(),
                        0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, builder.build());
            }
        }
    }


}
