package net.gongmingqm10.traintimer.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import net.gongmingqm10.traintimer.data.Trip;
import net.gongmingqm10.traintimer.event.TripsUpdateEvent;

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
        }
    }


}
