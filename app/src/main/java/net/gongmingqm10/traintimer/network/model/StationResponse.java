package net.gongmingqm10.traintimer.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StationResponse implements Serializable {

    @SerializedName("sta_name")
    private String name;

    @SerializedName("sta_ename")
    private String englishName;

    @SerializedName("sta_code")
    private String code;

    public StationResponse(String code, String englishName, String name) {
        this.code = code;
        this.englishName = englishName;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getName() {
        return name;
    }
}
