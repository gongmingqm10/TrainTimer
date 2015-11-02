package net.gongmingqm10.traintimer.network;

import net.gongmingqm10.traintimer.network.model.StationResponse;

import java.util.List;

import retrofit.http.GET;

public interface QiniuService {
    @GET("/stations.json")
    List<StationResponse> getStations();
}
