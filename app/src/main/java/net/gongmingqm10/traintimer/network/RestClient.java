package net.gongmingqm10.traintimer.network;

import retrofit.RestAdapter;

public class RestClient {

    private static RestClient instance;

    private RestAdapter restAdapter;

    private RestClient() {
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiEnvironment.TRAIN_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    public static RestClient getInstance() {
        if (instance == null) {
            instance = new RestClient();
        }
        return instance;
    }

    public TrainService getTrainService() {
        return restAdapter.create(TrainService.class);
    }


}
