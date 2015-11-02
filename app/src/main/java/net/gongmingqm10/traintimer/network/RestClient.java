package net.gongmingqm10.traintimer.network;

import retrofit.RestAdapter;

public class RestClient {

    private static RestClient instance;

    private RestAdapter trainAdapter;
    private RestAdapter qiniuAdapter;

    private RestClient() {
        trainAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiEnvironment.TRAIN_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        qiniuAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiEnvironment.QINIU_URL)
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
        return trainAdapter.create(TrainService.class);
    }

    public QiniuService getQiniuService() {
        return qiniuAdapter.create(QiniuService.class);
    }


}
