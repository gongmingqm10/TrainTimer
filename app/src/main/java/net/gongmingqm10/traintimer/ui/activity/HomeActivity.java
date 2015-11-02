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
import net.gongmingqm10.traintimer.network.RestClient;
import net.gongmingqm10.traintimer.network.model.StationResponse;
import net.gongmingqm10.traintimer.ui.adapter.TripCardAdapter;
import net.gongmingqm10.traintimer.ui.view.SpacesItemDecration;
import net.gongmingqm10.traintimer.util.PreferencesManager;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.dao.query.Query;

public class HomeActivity extends BaseActivity {

    private static final long STATION_UPDATE_TIMEOUT = 24 * 3600 * 1000;

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

        List<Trip> trips = TrainApp.getInstance().getTripDao().loadAll();
        tripAdapter = new TripCardAdapter(trips);
        tripRecyclerView.setAdapter(tripAdapter);
    }

    @OnClick(R.id.fab)
    protected void addNewTravel(View view) {
        startActivity(new Intent(this, AddTravelActivity.class));
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

}
