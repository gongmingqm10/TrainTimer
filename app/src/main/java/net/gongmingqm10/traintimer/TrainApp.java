package net.gongmingqm10.traintimer;

import android.app.Application;

import net.gongmingqm10.traintimer.data.DaoMaster;
import net.gongmingqm10.traintimer.data.DaoSession;
import net.gongmingqm10.traintimer.data.StationDao;
import net.gongmingqm10.traintimer.data.TripDao;
import net.gongmingqm10.traintimer.util.PreferencesManager;

public class TrainApp extends Application {

    private final static String DB_NAME = "train_trips";

    private static TrainApp instance;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        PreferencesManager.getInstance().init(this);

        initDB();
    }

    private void initDB() {
        DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME, null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    public static TrainApp getInstance() {
        return instance;
    }

    public StationDao getStationDao() {
        return daoSession.getStationDao();
    }

    public TripDao getTripDao() {
        return daoSession.getTripDao();
    }
}
