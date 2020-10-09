package com.org.zapayapp.dialogs;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.adapters.CityAdapter;
import com.org.zapayapp.adapters.StateAdapter;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.model.CityModel;
import com.org.zapayapp.model.StateModel;
import com.org.zapayapp.uihelpers.CustomTextInputLayout;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.DatePickerFragmentDialogue;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.WValidationLib;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.RestAPI;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

public class EditProfileDialogActivity extends AppCompatActivity implements View.OnClickListener, APICallback, SimpleAlertFragment.AlertSimpleCallback, DatePickerFragmentDialogue.DatePickerCallback {

    private TextView saveTV, titleTV;
    private ImageView cancelImageView;

    private WValidationLib wValidationLib;
    /*Code for API calling*/
    private ZapayApp zapayApp;
    private APICalling apiCalling;
    private RestAPI restAPI;

    private CustomTextInputLayout nameEditTextInputLayout, mobileInputLayout, address1InputLayout, address2InputLayout, postalCodeInputLayout, ssnInputLayout, dobInputLayout;
    private TextInputEditText nameEditText, mobileEditText, address1EditText, address2EditText, postalCodeEditText, ssnEditText, dobEditText;
    private Spinner stateSpinner, citySpinner;
    private List<StateModel> stateList;
    private StateAdapter stateAdapter;

    private List<CityModel> cityList;
    private CityAdapter cityAdapter;
    private String stateShortCode = "";
    private String cityName = "";
    private String dob = "";
    private int ageInYear = 0;
    private String stateName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_dialog);
        apiCodeInit();
        init();
        initAction();
    }

    private void apiCodeInit() {
        zapayApp = (ZapayApp) getApplicationContext();
        restAPI = APICalling.webServiceInterface();
        apiCalling = new APICalling(this);
    }

    private void init() {
        wValidationLib = new WValidationLib(EditProfileDialogActivity.this);
        stateList = new ArrayList<>();
        cityList = new ArrayList<>();

        saveTV = findViewById(R.id.saveTV);
        titleTV = findViewById(R.id.titleTV);
        cancelImageView = findViewById(R.id.cancelImageView);
        cancelImageView.setVisibility(View.VISIBLE);
        titleTV.setText(getString(R.string.edit_profile));

        nameEditTextInputLayout = findViewById(R.id.nameEditTextInputLayout);
        mobileInputLayout = findViewById(R.id.mobileInputLayout);
        address1InputLayout = findViewById(R.id.address1InputLayout);
        address2InputLayout = findViewById(R.id.address2InputLayout);
        postalCodeInputLayout = findViewById(R.id.postalCodeInputLayout);
        ssnInputLayout = findViewById(R.id.ssnInputLayout);
        dobInputLayout = findViewById(R.id.dobInputLayout);

        nameEditText = findViewById(R.id.nameEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        address1EditText = findViewById(R.id.address1EditText);
        address2EditText = findViewById(R.id.address2EditText);
        postalCodeEditText = findViewById(R.id.postalCodeEditText);
        ssnEditText = findViewById(R.id.ssnEditText);
        dobEditText = findViewById(R.id.dobEditText);

        stateSpinner = findViewById(R.id.stateSpinner);
        citySpinner = findViewById(R.id.citySpinner);
        stateName = SharedPref.getPrefsHelper().getPref(Const.Var.STATE).toString();

        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();
            }
        });

        setDataOnScreen();
    }

    private void initAction() {
        saveTV.setOnClickListener(this);
        cancelImageView.setOnClickListener(this);

        callAPIGetState();

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateShortCode = stateList.get(position).getShort_code();//bankAccountType= (String) parent.getItemAtPosition(position);
                String stateId = stateList.get(position).getId();
                String name = stateList.get(position).getState();

                callAPIGetCity(stateId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityName = cityList.get(position).getCity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void setDataOnScreen() {
        if (SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME) != null && SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME).toString().length() > 1) {
            String name = SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME, "") + " " + SharedPref.getPrefsHelper().getPref(Const.Var.LAST_NAME, "");
            nameEditText.setText(name);
        }
        if (SharedPref.getPrefsHelper().getPref(Const.Var.MOBILE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.MOBILE).toString().length() > 1) {
            mobileEditText.setText(SharedPref.getPrefsHelper().getPref(Const.Var.MOBILE, ""));
        }
        if (SharedPref.getPrefsHelper().getPref(Const.Var.ADDRESS1) != null && SharedPref.getPrefsHelper().getPref(Const.Var.ADDRESS1).toString().length() > 1) {
            address1EditText.setText(SharedPref.getPrefsHelper().getPref(Const.Var.ADDRESS1, ""));
        }
        if (SharedPref.getPrefsHelper().getPref(Const.Var.ADDRESS2) != null && SharedPref.getPrefsHelper().getPref(Const.Var.ADDRESS2).toString().length() > 1) {
            address2EditText.setText(SharedPref.getPrefsHelper().getPref(Const.Var.ADDRESS2, ""));
        }
        if (SharedPref.getPrefsHelper().getPref(Const.Var.POSTAL_CODE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.POSTAL_CODE).toString().length() > 1) {
            postalCodeEditText.setText(SharedPref.getPrefsHelper().getPref(Const.Var.POSTAL_CODE, ""));
        }
        if (SharedPref.getPrefsHelper().getPref(Const.Var.SSN) != null && SharedPref.getPrefsHelper().getPref(Const.Var.SSN).toString().length() > 1) {
            ssnEditText.setText(SharedPref.getPrefsHelper().getPref(Const.Var.SSN, ""));
        }
        if (SharedPref.getPrefsHelper().getPref(Const.Var.DOB) != null && SharedPref.getPrefsHelper().getPref(Const.Var.DOB).toString().length() > 1) {
            if (!SharedPref.getPrefsHelper().getPref(Const.Var.DOB).toString().trim().equalsIgnoreCase("0000-00-00"))
                dobEditText.setText(SharedPref.getPrefsHelper().getPref(Const.Var.DOB, ""));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(saveTV)) {
            updateProfileFunc();
        } else if (v.equals(cancelImageView)) {
            finish();
        }
    }

    private void updateProfileFunc() {
        if (wValidationLib.isFullName(nameEditTextInputLayout, nameEditText, getString(R.string.important), getString(R.string.please_enter_valid_full_name), true)) {
            if (wValidationLib.isValidNumeric(mobileInputLayout, mobileEditText, getString(R.string.important), getString(R.string.please_enter_valid_mobile), true)) {
                if (wValidationLib.isValidAddress1(address1InputLayout, address1EditText, getString(R.string.important), getString(R.string.must_be_50_characters_or_less), true)) {
                    if (wValidationLib.isValidAddress2(address2InputLayout, address2EditText, getString(R.string.important), getString(R.string.must_be_50_characters_or_less), false)) {
                        if (wValidationLib.isValidPostalCode(postalCodeInputLayout, postalCodeEditText, getString(R.string.important), getString(R.string.postal_code_should_be_5_digit), true)) {
                            if (wValidationLib.isValidSSNcode(ssnInputLayout, ssnEditText, getString(R.string.important), getString(R.string.ssn_code_should_be_5_digit), true)) {
                                if (wValidationLib.isEmpty(dobInputLayout, dobEditText, getString(R.string.important), true)) {
                                    callAPIUpdateProfile();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void callAPIGetState() {
        try {
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.getApi(getString(R.string.api_get_states_list));
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_states_list), saveTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIGetCity(String state_id) {
        try {
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    "state_id", state_id);
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postApi(getString(R.string.api_get_cities_list), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_cities_list), saveTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIUpdateProfile() {
        String firstNameLastName = nameEditText.getText().toString().trim();
        String[] firstNameLastName1 = firstNameLastName.split(" ");
        String firstName = firstNameLastName1[0];
        String lastName = firstNameLastName1[1];

        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    "first_name", firstName,
                    "last_name", lastName,
                    "mobile", mobileEditText.getText().toString().trim(),
                    "address1", address1EditText.getText().toString().trim(),
                    "address2", address2EditText.getText().toString().trim(),
                    "state", stateShortCode,
                    "city", cityName,
                    "postal_code", postalCodeEditText.getText().toString().trim(),
                    "ssn", ssnEditText.getText().toString().trim(),
                    "dob", dobEditText.getText().toString().trim()
            );
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_update_profile), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_update_profile), saveTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void apiCallback(JsonObject json, String from) {
        if (from != null) {
            int status = 0;
            String msg = "";
            try {
                status = json.get("status").getAsInt();
                msg = json.get("message").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (from.equals(getResources().getString(R.string.api_get_states_list))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonArray() != null) {
                        // JsonArray jsonArray = json.get("data").getAsJsonArray();
                        List<StateModel> list = apiCalling.getDataList(json, "data", StateModel.class);
                        stateList.clear();
                        stateList.addAll(list);
                        setStateAdapter();
                    }
                } else if (status == 401) {

                } else {
                    showSimpleAlert(msg, "");
                }
            } else if (from.equals(getResources().getString(R.string.api_get_cities_list))) {
                if (status == 200) {
                    if (json.get("data").getAsJsonArray() != null) {
                        List<CityModel> list = apiCalling.getDataList(json, "data", CityModel.class);
                        cityList.clear();
                        cityList.addAll(list);
                        setCityAdapter();
                    }
                } else {
                    cityName = "";
                    cityList.clear();
                    setCityAdapter();
                }
            } else if (from.equals(getResources().getString(R.string.api_update_profile))) {
                if (status == 200) {
                    showSimpleAlert(msg, getResources().getString(R.string.api_update_profile));
                } else {
                    showSimpleAlert(msg, "");
                }
            }
        }
    }

    public void showSimpleAlert(String message, String from) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            Bundle args = new Bundle();
            args.putString("header", message);
            args.putString("textOk", getString(R.string.ok));
            args.putString("textCancel", getString(R.string.cancel));
            args.putString("from", from);
            SimpleAlertFragment alert = new SimpleAlertFragment();
            alert.setArguments(args);
            alert.show(fm, "");
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSimpleCallback(String from) {
        if (from.equals(getResources().getString(R.string.api_update_profile))) {
            finish();
        }
    }

    private void setStateAdapter() {
        if (stateAdapter != null) {
            stateAdapter.notifyDataSetChanged();
        } else {
            stateAdapter = new StateAdapter(this, stateList);
            stateSpinner.setAdapter(stateAdapter);
        }
        if (stateName != null && stateName.length() > 0) {
            int pos = 0;
            for (int i = 0; i < stateList.size(); i++) {
                if (stateList.get(i).getShort_code().equals(stateName)) {
                    pos = i;
                }
            }
            stateSpinner.setSelection(pos);
        }
    }


    private void setCityAdapter() {
        cityAdapter = new CityAdapter(this, cityList);
        citySpinner.setAdapter(cityAdapter);
    }

    private void datePickerDialog() {
        final Calendar myCalendar = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MyAlertDialogStyle, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // timeText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                dob = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                ageInYear = getAge(year, monthOfYear + 1, dayOfMonth);
                if (ageInYear >= 18) {
                    dobEditText.setText(dob);
                } else {
                    showSimpleAlert(getString(R.string.age_must_be_18_years_or_older), "");
                }
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();



     /*  try {
            DialogFragment newFragment1 = new DatePickerFragmentDialogue();
            Bundle args1 = new Bundle();
            args1.putString(getString(R.string.show), getString(R.string.min_current));
            newFragment1.setArguments(args1);
            newFragment1.show(getSupportFragmentManager(), getString(R.string.date_picker));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void datePickerCallback(String selectedDate, int year, int month, int day, String from) throws ParseException {
        dob = selectedDate;
        dobEditText.setText(dob);
    }


    private int getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        // String ageS = ageInt.toString();

        return ageInt;
    }

}
