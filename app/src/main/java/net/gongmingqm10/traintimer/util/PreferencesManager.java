package net.gongmingqm10.traintimer.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String PREFERENCES_NAME = "settings";
    private static PreferencesManager instance;

    private static final String KEY_STATION_UPDATE_TIME = "key_station_update_time";

    private SharedPreferences sharedPreferences;

    private PreferencesManager() {}

    public static PreferencesManager getInstance() {
        if (instance == null) {
            instance = new PreferencesManager();
        }
        return instance;
    }

    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void updateStationTime(long updatedTime) {
        sharedPreferences.edit().putLong(KEY_STATION_UPDATE_TIME, updatedTime).apply();
    }

    public long getStationUpdateTime() {
        return sharedPreferences.getLong(KEY_STATION_UPDATE_TIME, 0);
    }
}
