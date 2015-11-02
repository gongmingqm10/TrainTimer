package net.gongmingqm10.traintimer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.gongmingqm10.traintimer.R;
import net.gongmingqm10.traintimer.data.Trip;
import net.gongmingqm10.traintimer.ui.view.TripCardViewHolder;

import java.util.List;

public class TripCardAdapter extends RecyclerView.Adapter<TripCardViewHolder> {

    private List<Trip> trips;

    public TripCardAdapter(List<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public TripCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_card, parent, false);
        return new TripCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TripCardViewHolder holder, int position) {
        holder.populate(trips.get(position));
    }

    @Override
    public int getItemCount() {
        return trips == null ? 0 : trips.size();
    }
}
