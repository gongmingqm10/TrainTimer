package net.gongmingqm10.traintimer;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.os.Bundle;

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

        startSync();
    }

    private void startSync() {

        Account account = getStubAccount();

        ContentResolver.addPeriodicSync(account, getString(R.string.content_authority),
                Bundle.EMPTY, 60);
    }

    private Account getStubAccount() {
        String accountType = getString(R.string.account_type);

        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] accounts = accountManager.getAccountsByType(accountType);

        if (accounts == null || accounts.length == 0) {
            Account account = new Account(getString(R.string.account_name), getString(R.string.account_type));
            accountManager.addAccountExplicitly(account, null, null);
            return account;
        } else {
            return accounts[0];
        }
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
