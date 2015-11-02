package net.gongmingqm10.traintimer.util;

import net.gongmingqm10.traintimer.data.Station;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class StationUtils {

    public static ArrayList<Station> wrapStations(List<Station> stations) {
        ArrayList<Station> data = new ArrayList<>();

        for (Station station : stations) {
            String lastStationAlpha = getLastStationAlpha(data);
            String currentStationAlpha = station.getEnglishName().substring(0, 1);

            if (!currentStationAlpha.equalsIgnoreCase(lastStationAlpha)) {
                Station alphaStation = new Station();
                alphaStation.setCode(currentStationAlpha.toUpperCase());
                data.add(alphaStation);
            }

            data.add(station);
        }
        return data;
    }

    private static String getLastStationAlpha(List<Station> data) {
        if (data == null || data.isEmpty()) return null;
        return data.get(data.size() - 1).getEnglishName().substring(0, 1);
    }

    public static String encodeStationCode(String stationName) {
        String encodedText = null;
        try {
            encodedText = URLEncoder.encode(stationName, "utf-8");
            encodedText = encodedText.replace("/%/g", "-");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedText;
    }

}
