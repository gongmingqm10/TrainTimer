package net.gongmingqm10.traintimer.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AuthenticatorService extends Service {

    private TrainAuthenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        authenticator = new TrainAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
