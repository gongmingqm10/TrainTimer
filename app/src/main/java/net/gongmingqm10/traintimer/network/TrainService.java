package net.gongmingqm10.traintimer.network;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public interface TrainService {
    @GET("/map_zwdcx/cx.jsp")
    void query(@Query("cc") String trainNumber,@Query("cz") String stationName, @Query("czEn") String stationCode, @Query("rq") String date, @Query("cxlx") int queryType, Callback<Response> callback);
}
