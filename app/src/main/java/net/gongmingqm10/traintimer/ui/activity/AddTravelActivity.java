package net.gongmingqm10.traintimer.ui.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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

public class AddTravelActivity extends BaseActivity
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

    public static final int REQUEST_SELECT_STATION = 100;

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
            trip.setDepartTime(departTime);
            trip.setTrainNumber(trainNumber);
            trip.setHasReminder(false);

            TrainApp.getInstance().getTripDao().insert(trip);
            finish();
        }
    }

    private Callback<Response> queryCallback = new Callback<Response>() {
        @Override
        public void success(Response result, Response response) {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(result.getBody().in(), "GBK"));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(AddTravelActivity.this, sb.toString().trim(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(AddTravelActivity.this, getString(R.string.query_failed_try_again), Toast.LENGTH_SHORT).show();
        }
    };

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

    private ProgressDialog progressDialog;


//    @OnClick(R.id.edit_station)
//    protected void selectStation() {
//
//        progressDialog = ProgressDialog.show(this, "", getString(R.string.is_fetching_stations));
//
//        Observable.create(new Observable.OnSubscribe<ArrayList<Station>>() {
//            @Override
//            public void call(Subscriber<? super ArrayList<Station>> subscriber) {
//                List<Station> stations = TrainApp.getInstance().getStationDao().loadAll();
//                TrainApp.getInstance().getStationDao().loadAll();
//                if (stations == null || stations.isEmpty()) {
//                    subscriber.onError(new StationsEmptyException("Stations is not loaded"));
//                } else {
//                    ArrayList<Station> wrappedStations = StationUtils.wrapStations(stations);
//                    subscriber.onNext(wrappedStations);
//                    subscriber.onCompleted();
//                }
//            }
//        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                .subscribe(new Action1<ArrayList<Station>>() {
//                    @Override
//                    public void call(ArrayList<Station> stations) {
//                        dismissProgressDialog();
//                        Intent intent = new Intent(AddTravelActivity.this, StationSelectActivity.class);
//                        startActivityForResult(intent, REQUEST_SELECT_STATION);
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        dismissProgressDialog();
//                        showToast(R.string.fetching_station_failed);
//                    }
//                });
//    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing() & !isFinishing()) {
            progressDialog.dismiss();
        }
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_SELECT_STATION && resultCode == RESULT_OK && data != null) {
//            selectedStation = (Station) data.getSerializableExtra(StationSelectActivity.PARAM_STATION_SELECTED);
//            stationEdit.setText(selectedStation.getName());
//        }
//    }
}
