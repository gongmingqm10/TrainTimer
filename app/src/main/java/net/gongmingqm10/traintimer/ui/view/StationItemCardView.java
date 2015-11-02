package net.gongmingqm10.traintimer.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.gongmingqm10.traintimer.R;
import net.gongmingqm10.traintimer.data.Station;

import butterknife.Bind;
import butterknife.ButterKnife;


public class StationItemCardView extends RelativeLayout {

    @Bind(R.id.station_info)
    protected TextView stationInfo;

    public StationItemCardView(Context context) {
        super(context);
        init(context);
    }

    public StationItemCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StationItemCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        inflate(context, R.layout.cardview_station_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
    }

    public void populate(Station station) {
        if (TextUtils.isEmpty(station.getName())) {
            stationInfo.setTypeface(null, Typeface.BOLD);
            stationInfo.setTextColor(getResources().getColor(R.color.font_color_primary));
            stationInfo.setText(station.getCode().toUpperCase());
        } else {
            stationInfo.setTypeface(null, Typeface.NORMAL);
            stationInfo.setTextColor(getResources().getColor(R.color.font_color_secondary));
            stationInfo.setText(getResources().getString(R.string.format_station_info, station.getName(), station.getEnglishName()));
        }
    }
}
