package net.gongmingqm10.traintimer.ui.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import net.gongmingqm10.traintimer.R;
import net.gongmingqm10.traintimer.TrainApp;
import net.gongmingqm10.traintimer.data.Station;
import net.gongmingqm10.traintimer.data.StationDao;
import net.gongmingqm10.traintimer.data.Trip;
import net.gongmingqm10.traintimer.ui.fragment.DatePickerFragment;
import net.gongmingqm10.traintimer.ui.fragment.TimePickerFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.dao.query.Query;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewTripActivity extends BaseActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @Bind(R.id.edit_station)
    protected EditText stationEdit;

    @Bind(R.id.depart_date)
    protected EditText departDateEdit;

    @Bind(R.id.depart_time)
    protected EditText departTimeEdit;

    @Bind(R.id.layout_depart_time)
    protected TextInputLayout layoutDepartTime;

    @Bind(R.id.edit_train_number)
    protected EditText trainNumberEdit;

    @Bind(R.id.layout_edit_station)
    protected TextInputLayout layoutEditStation;

    @Bind(R.id.layout_edit_train_number)
    protected TextInputLayout layoutTrainNumber;

    public static final String PARAM_TRIP = "param_trip";

    private Station selectedStation;
    private Calendar departCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);

        initView();
    }

    private void initView() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        departDateEdit.setText(getString(R.string.format_date, year, monthOfYear + 1, dayOfMonth));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_travel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_save) {
            saveTravel();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTravel() {
        String trainNumber = trainNumberEdit.getText().toString().trim();
        String trainStation = stationEdit.getText().toString().trim();
        String departTime = departTimeEdit.getText().toString();
        if (isValid(trainNumber, trainStation, departTime)) {
            clearErrorMessage();

            Trip trip = new Trip();
            trip.setTripDate(departCalendar.getTime());
            trip.setStation(selectedStation);
            trip.setScheduledDepartTime(departTime);
            trip.setTrainNumber(trainNumber);
            trip.setHasReminder(false);

            TrainApp.getInstance().getTripDao().insert(trip);
            Intent intent = new Intent();
            intent.putExtra(PARAM_TRIP, trip);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void clearErrorMessage() {
        layoutTrainNumber.setError(null);
        layoutEditStation.setError(null);
    }

    @OnClick(R.id.depart_date)
    protected void selectDate(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    @OnClick(R.id.depart_time)
    protected void selectTime(View view) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.show(getSupportFragmentManager(), "timePicker");
    }

    private boolean isValid(String trainNumber, String trainStation, String departTime) {

        if (TextUtils.isEmpty(trainStation)) {
            layoutEditStation.setError(getString(R.string.please_input_station));
            return false;
        }

        if (TextUtils.isEmpty(trainNumber)) {
            layoutTrainNumber.setError(getString(R.string.please_input_train_number));
            return false;
        }

        if (TextUtils.isEmpty(departTime)) {
            layoutDepartTime.setError(getString(R.string.please_fill_train_depart_time));
            return false;
        }

        Query query = TrainApp.getInstance().getStationDao().queryBuilder()
                .where(StationDao.Properties.Name.eq(trainStation)).build();
        List<Station> stations = query.list();
        if (stations == null || stations.isEmpty()) {
            showToast(R.string.station_not_exist);
            return false;
        }
        selectedStation = stations.get(0);

        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        departCalendar.set(year, monthOfYear, dayOfMonth);
        departDateEdit.setText(getString(R.string.format_date, year, monthOfYear + 1, dayOfMonth));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        departTimeEdit.setText(getString(R.string.format_time, hourOfDay, minute));
    }
}
