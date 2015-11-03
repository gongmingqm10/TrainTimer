package net.gongmingqm10.traintimer.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.gongmingqm10.traintimer.R;
import net.gongmingqm10.traintimer.TrainApp;
import net.gongmingqm10.traintimer.data.Trip;
import net.gongmingqm10.traintimer.util.DateUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TripCardViewHolder extends RecyclerView.ViewHolder {

    private final Context context;

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
    protected Button reminderBtn;

    @Bind(R.id.card_depart_date)
    protected TextView departureDateText;

    @Bind(R.id.card_header_wrapper)
    protected View headWrapper;

    private int[] resIds = {R.mipmap.train_back_1, R.mipmap.train_back_2};

    public TripCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = itemView.getContext();
    }

    public void populate(final Trip trip) {
        trainNumberText.setText(trip.getTrainNumber());
        trainStationText.setText(trip.getStation().getName());
        int randomPosition = trip.hashCode() % 2;
        headWrapper.setBackgroundResource(resIds[randomPosition]);

        String formattedDate = DateUtils.formatDate(trip.getTripDate());
        departureDateText.setText(TrainApp.getInstance().getString(R.string.format_departure_date, formattedDate));

        updateArrivalInfo(trip);
        updateDepartInfo(trip);

        updateReminderBtn(trip.getHasReminder() != null && trip.getHasReminder());
        reminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTripReminder(trip);
            }
        });
    }

    private void updateArrivalInfo(Trip trip) {
        if (TextUtils.isEmpty(trip.getArriveTime())) {
            arrivalTimeText.setText(R.string.no_time);
        } else {
            arrivalTimeText.setText(trip.getArriveTime());
        }

        if (TextUtils.isEmpty(trip.getArriveMessage())) {
            arrivalInfoText.setText(R.string.no_arrive_time_info);
        } else {
            arrivalInfoText.setText(trip.getArriveMessage());
        }
    }

    private void updateDepartInfo(Trip trip) {
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
    }

    private void updateReminderBtn(boolean hasReminder) {
        if (hasReminder) {
            reminderBtn.setEnabled(false);
            reminderBtn.setText(R.string.already_reminded);
        } else {
            reminderBtn.setEnabled(true);
            reminderBtn.setText(R.string.add_reminder);
        }
    }


    public void addTripReminder(final Trip trip) {
        new AlertDialog.Builder(context)
                .setMessage(R.string.confirm_add_reminder)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trip.setHasReminder(true);
                        TrainApp.getInstance().getTripDao().update(trip);
                        updateReminderBtn(true);
                    }
                }).setNegativeButton(android.R.string.cancel, null).show();
    }
}
