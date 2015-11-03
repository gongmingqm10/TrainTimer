package net.gongmingqm10.traintimer.sync;

import net.gongmingqm10.traintimer.TrainApp;
import net.gongmingqm10.traintimer.data.Trip;
import net.gongmingqm10.traintimer.data.TripDao;
import net.gongmingqm10.traintimer.network.RestClient;
import net.gongmingqm10.traintimer.util.DateUtils;
import net.gongmingqm10.traintimer.util.IOUtils;
import net.gongmingqm10.traintimer.util.StationUtils;

import java.util.Date;
import java.util.List;

import retrofit.client.Response;

public class TripSyncManager {

    private static TripSyncManager instance;

    private TripSyncManager() {
    }

    public static TripSyncManager getInstance() {
        if (instance == null) {
            instance = new TripSyncManager();
        }
        return instance;
    }

    public List<Trip> sync() {
        TripDao tripDao = TrainApp.getInstance().getTripDao();
        List<Trip> trips = tripDao.queryBuilder().orderDesc(TripDao.Properties.Id).list();

        Date floorDate = DateUtils.getFloorDate();

        for (Trip trip : trips) {
            if (trip.getTripDate().after(floorDate)) {
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
            } else {
                tripDao.delete(trip);
            }

        }

        return trips;
    }

}
