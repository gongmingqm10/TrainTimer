package net.gongmingqm10.traintimer.ui.view;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import net.gongmingqm10.traintimer.R;
import net.gongmingqm10.traintimer.TrainApp;
import net.gongmingqm10.traintimer.data.Trip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TripCardViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.card_train_number)
    protected TextView trainNumberText;

    @Bind(R.id.card_train_station)
    protected TextView trainStationText;

    @Bind(R.id.card_train_arrive_time)
    protected TextView arrivalTimeText;

    @Bind(R.id.card_train_depart_time)
    protected TextView departureTimeText;

    @Bind(R.id.card_train_arrival_info)
    protected TextView arrivalInfoText;

    @Bind(R.id.card_train_depart_info)
    protected TextView departureInfoText;

    @Bind(R.id.reminder_btn)
    protected TextView reminderBtn;

    @Bind(R.id.card_depart_date)
    protected TextView departureDateText;

    @Bind(R.id.card_header_wrapper)
    protected View headWrapper;

    private int[] resIds = {R.mipmap.train_back_1, R.mipmap.train_back_2};

    public TripCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void populate(final Trip trip) {
        trainNumberText.setText(trip.getTrainNumber());
        trainStationText.setText(trip.getStation().getName());

        if (TextUtils.isEmpty(trip.getArriveTime())) {
            arrivalTimeText.setText(R.string.no_time);
        } else {
            arrivalTimeText.setText(trip.getArriveTime());
        }

        if (TextUtils.isEmpty(trip.getDepartTime())) {
            departureTimeText.setText(trip.getScheduledDepartTime());
        } else {
            departureTimeText.setText(trip.getDepartTime());
        }

        if (TextUtils.isEmpty(trip.getDepartMessage())) {
            departureInfoText.setText(R.string.no_depart_time_info);
        } else {
            departureInfoText.setText(trip.getDepartMessage());
        }

        if (TextUtils.isEmpty(trip.getArriveMessage())) {
            arrivalInfoText.setText(R.string.no_arrive_time_info);
        } else {
            arrivalInfoText.setText(trip.getArriveTime());
        }

        if (trip.getHasReminder() != null && trip.getHasReminder()) {
            reminderBtn.setSelected(false);
            reminderBtn.setText(R.string.already_reminded);
        } else {
            reminderBtn.setSelected(true);
            reminderBtn.setText(R.string.add_reminder);
        }

        int randomPosition = new Random().nextInt(resIds.length);
        headWrapper.setBackgroundResource(resIds[randomPosition]);

        String formattedDate = formatDate(trip.getTripDate());
        departureDateText.setText(TrainApp.getInstance().getString(R.string.format_departure_date, formattedDate));
    }

    private String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


}
