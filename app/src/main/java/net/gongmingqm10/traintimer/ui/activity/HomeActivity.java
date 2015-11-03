package net.gongmingqm10.traintimer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.gongmingqm10.traintimer.R;
import net.gongmingqm10.traintimer.TrainApp;
import net.gongmingqm10.traintimer.data.Station;
import net.gongmingqm10.traintimer.data.StationDao;
import net.gongmingqm10.traintimer.data.Trip;
import net.gongmingqm10.traintimer.data.TripDao;
import net.gongmingqm10.traintimer.event.TripsUpdateEvent;
import net.gongmingqm10.traintimer.network.RestClient;
import net.gongmingqm10.traintimer.network.model.StationResponse;
import net.gongmingqm10.traintimer.ui.adapter.TripCardAdapter;
import net.gongmingqm10.traintimer.ui.view.SpacesItemDecration;
import net.gongmingqm10.traintimer.util.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.dao.query.Query;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HomeActivity extends BaseActivity {

    private static final long STATION_UPDATE_TIMEOUT = 24 * 3600 * 1000;

    private static final int REQUEST_ADD_TRIP = 100;

    @Bind(R.id.train_list)
    protected RecyclerView tripRecyclerView;

    private TripCardAdapter tripAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestStation();

        initView();
    }

    private void initView() {
        tripRecyclerView.setHasFixedSize(true);
        tripRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.card_margin);
        tripRecyclerView.addItemDecoration(new SpacesItemDecration(spacingInPixels));

        tripAdapter = new TripCardAdapter(new ArrayList<Trip>());
        tripRecyclerView.setAdapter(tripAdapter);
    }

    @OnClick(R.id.fab)
    protected void addNewTravel(View view) {
        startActivityForResult(new Intent(this, NewTripActivity.class), REQUEST_ADD_TRIP);
    }

    private void requestStation() {
        if (shouldUpdateStations()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<StationResponse> response = RestClient.getInstance().getQiniuService().getStations();
                    StationDao stationDao = TrainApp.getInstance().getStationDao();

                    for (StationResponse stationResponse : response) {
                        Query query = stationDao.queryBuilder().where(
                                StationDao.Properties.Code.eq(stationResponse.getCode())).build();


                        if (query.list().isEmpty()) {
                            Station station = new Station();
                            station.setCode(stationResponse.getCode());
                            station.setEnglishName(stationResponse.getEnglishName());
                            station.setName(stationResponse.getName());

                            stationDao.insert(station);
                        }
                    }

                    PreferencesManager.getInstance().updateStationTime(System.currentTimeMillis());
                }
            }).start();
        }
    }

    private boolean shouldUpdateStations() {
        long lastUpdatedTime = PreferencesManager.getInstance().getStationUpdateTime();
        return System.currentTimeMillis() - lastUpdatedTime > STATION_UPDATE_TIMEOUT;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD_TRIP) {
            Trip trip = (Trip) data.getSerializableExtra(NewTripActivity.PARAM_TRIP);
            tripAdapter.addTrip(trip);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.create(new Observable.OnSubscribe<List<Trip>>() {
            @Override
            public void call(Subscriber<? super List<Trip>> subscriber) {
                List<Trip> trips = TrainApp.getInstance().getTripDao().queryBuilder().orderDesc(TripDao.Properties.Id).list();
                subscriber.onNext(trips);
                subscriber.onCompleted();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Action1<List<Trip>>() {
                    @Override
                    public void call(List<Trip> trips) {
                        tripAdapter.updateTrips(trips);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(TripsUpdateEvent event) {
        tripAdapter.updateTrips(event.getTrips());
    }
}
