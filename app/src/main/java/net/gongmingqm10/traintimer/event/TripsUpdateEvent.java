package net.gongmingqm10.traintimer.event;

import net.gongmingqm10.traintimer.data.Trip;

import java.util.List;

public class TripsUpdateEvent {

    private List<Trip> trips;

    public TripsUpdateEvent(List<Trip> trips) {
        this.trips = trips;
    }

    public List<Trip> getTrips() {
        return trips;
    }
}
