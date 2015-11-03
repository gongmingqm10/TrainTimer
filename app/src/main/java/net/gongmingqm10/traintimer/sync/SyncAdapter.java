package net.gongmingqm10.traintimer.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import net.gongmingqm10.traintimer.TrainApp;
import net.gongmingqm10.traintimer.data.Trip;
import net.gongmingqm10.traintimer.data.TripDao;
import net.gongmingqm10.traintimer.event.TripsUpdateEvent;
import net.gongmingqm10.traintimer.network.RestClient;
import net.gongmingqm10.traintimer.util.DateUtils;
import net.gongmingqm10.traintimer.util.IOUtils;
import net.gongmingqm10.traintimer.util.StationUtils;

import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.client.Response;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private ContentResolver contentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.contentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.contentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        TripDao tripDao = TrainApp.getInstance().getTripDao();
        List<Trip> trips = tripDao.loadAll();

        for (Trip trip : trips) {
            Response arriveResponse = RestClient.getInstance().getTrainService().query(
                    trip.getTrainNumber(),
                    trip.getStation().getName(),
                    StationUtils.encodeStationCode(trip.getStation().getName()),
                    DateUtils.formatDate(trip.getTripDate()),
                    0);
            String arriveMessage = IOUtils.readFromResponse(arriveResponse);


            Response departResponse = RestClient.getInstance().getTrainService().query(
                    trip.getTrainNumber(),
                    trip.getStation().getName(),
                    StationUtils.encodeStationCode(trip.getStation().getName()),
                    DateUtils.formatDate(trip.getTripDate()),
                    1);
            String departMessage = IOUtils.readFromResponse(departResponse);

            trip.setArriveMessage(arriveMessage);
            trip.setArriveTime(IOUtils.extractTime(arriveMessage));
            trip.setDepartMessage(departMessage);
            trip.setDepartTime(IOUtils.extractTime(departMessage));

            tripDao.update(trip);
        }

        if (!trips.isEmpty()) {
            EventBus.getDefault().post(new TripsUpdateEvent(trips));
        }
    }


}
