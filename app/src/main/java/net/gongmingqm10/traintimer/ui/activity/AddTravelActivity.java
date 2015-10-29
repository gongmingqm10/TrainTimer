package net.gongmingqm10.traintimer.ui.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import net.gongmingqm10.traintimer.R;
import net.gongmingqm10.traintimer.network.RestClient;
import net.gongmingqm10.traintimer.ui.fragment.DatePickerFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddTravelActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @Bind(R.id.edit_station)
    protected EditText stationEdit;

    @Bind(R.id.depart_date)
    protected EditText departDateEdit;

    @Bind(R.id.edit_train_number)
    protected EditText trainNumberEdit;

    @Bind(R.id.layout_edit_station)
    protected TextInputLayout layoutEditStation;

    @Bind(R.id.layout_edit_train_number)
    protected TextInputLayout layoutTrainNumber;

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

        departDateEdit.setText(getString(R.string.format_birthday, year, monthOfYear + 1, dayOfMonth));
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTravel() {
        String trainNumber = trainNumberEdit.getText().toString().trim();
        String trainStation = stationEdit.getText().toString().trim();

        if (isValid(trainNumber, trainStation)) {
            clearErrorMessage();
            String stationCode = encodeStationCode(trainStation);
            String departTime = departDateEdit.getText().toString();
            int queryType = 1;

            RestClient.getInstance().getTrainService().query(trainNumber, trainStation, stationCode, departTime, queryType, queryCallback);
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

    private String encodeStationCode(String stationName) {
        String encodedText = null;
        try {
            encodedText = URLEncoder.encode(stationName, "utf-8");
            encodedText = encodedText.replace("/%/g", "-");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedText;
    }

    private void clearErrorMessage() {
        layoutTrainNumber.setError(null);
        layoutEditStation.setError(null);
    }

    @OnClick(R.id.depart_date)
    protected void selectDate(View view) {
        if (view.isFocused()) {
            DatePickerFragment fragment = new DatePickerFragment();
            fragment.show(getSupportFragmentManager(), "dialog");
        }
    }

    private boolean isValid(String trainNumber, String trainStation) {

        if (TextUtils.isEmpty(trainStation)) {
            layoutEditStation.setError(getString(R.string.please_input_station));
            return false;
        }

        if (TextUtils.isEmpty(trainNumber)) {
            layoutTrainNumber.setError(getString(R.string.please_input_train_number));
            return false;
        }

        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        departDateEdit.setText(getString(R.string.format_birthday, year, monthOfYear + 1, dayOfMonth));
    }
}
