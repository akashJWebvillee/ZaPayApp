package com.org.zapayapp.dialogs;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.activity.SignatureActivity;
import com.org.zapayapp.adapters.CityAdapter;
import com.org.zapayapp.adapters.GenderAdapter;
import com.org.zapayapp.adapters.IncomeAdapter;
import com.org.zapayapp.adapters.StateAdapter;
import com.org.zapayapp.alert_dialog.AlertForcePopup;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.model.CityModel;
import com.org.zapayapp.model.StateModel;
import com.org.zapayapp.uihelpers.CustomTextInputLayout;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.DateFormat;
import com.org.zapayapp.utils.DatePickerFragmentDialogue;
import com.org.zapayapp.utils.ImagePathUtil;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.WValidationLib;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.RestAPI;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class EditProfileDialogActivity extends AppCompatActivity implements View.OnClickListener, APICallback, SimpleAlertFragment.AlertSimpleCallback, DatePickerFragmentDialogue.DatePickerCallback,AlertForcePopup.AlertForceCallback {
    private TextView saveTV, titleTV;
    private ImageView cancelImageView;

    private WValidationLib wValidationLib;
    /*Code for API calling*/
    private ZapayApp zapayApp;
    private APICalling apiCalling;
    private RestAPI restAPI;

    private CustomTextInputLayout nameEditTextInputLayout, mobileInputLayout, address1InputLayout, address2InputLayout, postalCodeInputLayout, ssnInputLayout, dobInputLayout, ageInputLayout, ethnicityInputLayout;
    private TextInputEditText nameEditText, mobileEditText, address1EditText, address2EditText, postalCodeEditText, ssnEditText, dobEditText, ageEditText, ethnicityEditText;
    private Spinner stateSpinner, citySpinner, genderSpinner, incomeBracketSpinner;
    private List<StateModel> stateList;
    private StateAdapter stateAdapter;

    private List<CityModel> cityList;
    private CityAdapter cityAdapter;
    private String stateShortCode = "";
    private String cityName = "";
    private String dob = "";
    private int ageInYear = 0;
    private String stateName;
    private String showCityName;
    private String selectDOB;

    private List<String> genderList;
    private String gender;
    private List<String> incomeList;
    private String incomeValue;
    private String ethnicity = "";
    private ImageView signatureImageView;
    private TextView signatureHintTV;
    private String signaturePath = "";

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
        ageInputLayout = findViewById(R.id.ageInputLayout);

        nameEditText = findViewById(R.id.nameEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        address1EditText = findViewById(R.id.address1EditText);
        address2EditText = findViewById(R.id.address2EditText);
        postalCodeEditText = findViewById(R.id.postalCodeEditText);
        ssnEditText = findViewById(R.id.ssnEditText);
        dobEditText = findViewById(R.id.dobEditText);
        ageEditText = findViewById(R.id.ageEditText);
        ethnicityEditText = findViewById(R.id.ethnicityEditText);

        stateSpinner = findViewById(R.id.stateSpinner);
        citySpinner = findViewById(R.id.citySpinner);
        genderSpinner = findViewById(R.id.genderSpinner);
        incomeBracketSpinner = findViewById(R.id.incomeBracketSpinner);
        signatureHintTV = findViewById(R.id.signatureHintTV);
        signatureImageView = findViewById(R.id.signatureImageView1);
        signatureImageView.setOnClickListener(this);
        stateName = SharedPref.getPrefsHelper().getPref(Const.Var.STATE).toString();
        showCityName = SharedPref.getPrefsHelper().getPref(Const.Var.CITY).toString();

        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();
            }
        });
        setDataOnScreen();
    }

    private void initAction() {
        String[] genderArray = getResources().getStringArray(R.array.genderArray);
        String[] incomeArray = getResources().getStringArray(R.array.incomeArray);

        genderList = new ArrayList<>();
        genderList.add(genderArray[0]);
        genderList.add(genderArray[1]);
        setGenderAdapter();

        incomeList = new ArrayList<>();
        for (String s : incomeArray) {
            incomeList.add(s);
        }

        setIncomeAdapter();
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


        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = genderList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        incomeBracketSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                incomeValue = incomeList.get(position);
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
                dobEditText.setText(DateFormat.dateFormatConvert(SharedPref.getPrefsHelper().getPref(Const.Var.DOB, "")));
            selectDOB = SharedPref.getPrefsHelper().getPref(Const.Var.DOB).toString();
        }

        if (SharedPref.getPrefsHelper().getPref(Const.Var.AGE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.AGE).toString().length() > 1) {
            ageEditText.setText(SharedPref.getPrefsHelper().getPref(Const.Var.AGE, ""));
            ageInYear = Integer.parseInt(SharedPref.getPrefsHelper().getPref(Const.Var.AGE).toString());
        }

        if (SharedPref.getPrefsHelper().getPref(Const.Var.ETHNICITY) != null && SharedPref.getPrefsHelper().getPref(Const.Var.ETHNICITY).toString().length() > 1) {
            ethnicityEditText.setText(SharedPref.getPrefsHelper().getPref(Const.Var.ETHNICITY, ""));
        }

        if (SharedPref.getPrefsHelper().getPref(Const.Var.SIGNATURE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.SIGNATURE).toString().length() > 1) {
            signatureHintTV.setVisibility(View.GONE);
            signatureImageView.setClickable(false);

            Glide.with(EditProfileDialogActivity.this)
                    .load(APICalling.getImageUrl(SharedPref.getPrefsHelper().getPref(Const.Var.SIGNATURE).toString()))
                    //.placeholder(R.mipmap.default_profile)
                    .into(signatureImageView);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(saveTV)) {
            updateProfileFunc();
        } else if (v.equals(cancelImageView)) {
            finish();
        } else if (v.getId() == R.id.signatureImageView1) {
            Intent intent = new Intent(EditProfileDialogActivity.this, SignatureActivity.class);
            startActivityForResult(intent, 200);
        }
    }

    private void updateProfileFunc() {
        if (wValidationLib.isFullName(nameEditTextInputLayout, nameEditText, getString(R.string.important), getString(R.string.please_enter_valid_full_name), true)) {
            if (wValidationLib.isValidNumeric(mobileInputLayout, mobileEditText, getString(R.string.important), getString(R.string.please_enter_valid_mobile), true)) {
                if (wValidationLib.isValidAddress1(address1InputLayout, address1EditText, getString(R.string.important), getString(R.string.must_be_50_characters_or_less), true)) {
                    if (wValidationLib.isValidAddress2(address2InputLayout, address2EditText, getString(R.string.important), getString(R.string.must_be_50_characters_or_less), false)) {
                        if (wValidationLib.isValidPostalCode(postalCodeInputLayout, postalCodeEditText, getString(R.string.important), getString(R.string.postal_code_should_be_5_digit), true)) {
                            if (wValidationLib.isValidSSNcode(ssnInputLayout, ssnEditText, getString(R.string.important), getString(R.string.ssn_code_should_be_4_digit), true)) {
                                if (wValidationLib.isEmpty(dobInputLayout, dobEditText, getString(R.string.important), true)) {
                                    if (wValidationLib.isEmpty(ageInputLayout, ageEditText, getString(R.string.important), true)) {
                                        if (SharedPref.getPrefsHelper().getPref(Const.Var.SIGNATURE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.SIGNATURE).toString().length() > 0||signaturePath.length()>0) {
                                            if (signaturePath != null && signaturePath.length() > 0) {
                                                showSimpleAlert11(getString(R.string.signature_conformation_msg), getString(R.string.signature_conformation_msg), false, getString(R.string.cancel), false);
                                            } else {
                                                callAPIUpdateProfile();
                                            }
                                        } else {
                                            showSimpleAlert(getString(R.string.plz_add_signature), getResources().getString(R.string.plz_add_signature));
                                        }
                                    }
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
        ethnicity = ethnicityEditText.getText().toString().trim();
        String firstNameLastName = nameEditText.getText().toString().trim();
        String[] firstNameLastName1 = firstNameLastName.split(" ");
        String firstName = firstNameLastName1[0];
        String lastName = firstNameLastName1[1];

        String token = SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
      /*  try {
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
                    "dob", selectDOB,

                    "age", ageInYear,
                    "sex", gender.toLowerCase(),
                    "ethnicity", ethnicity,
                    "income", incomeValue.toLowerCase()
            );
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token, getString(R.string.api_update_profile), values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_update_profile), saveTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        MultipartBody.Part fileToUpload = null;
        if (signaturePath != null && signaturePath.length() > 0) {
            File file = new File(signaturePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            fileToUpload = MultipartBody.Part.createFormData("signature", file.getName(), requestFile);
        } else {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            fileToUpload = MultipartBody.Part.createFormData("signature", "", requestFile);
        }


        try {
            HashMap<String, RequestBody> values = apiCalling.getHashMapObjectPart(
                    "first_name", firstName,
                    "last_name", lastName,
                    "mobile", mobileEditText.getText().toString().trim(),
                    "address1", address1EditText.getText().toString().trim(),
                    "address2", address2EditText.getText().toString().trim(),
                    "state", stateShortCode,
                    "city", cityName,
                    "postal_code", postalCodeEditText.getText().toString().trim(),
                    "ssn", ssnEditText.getText().toString().trim(),
                    "dob", selectDOB,
                    "age", ageInYear,
                    "sex", gender.toLowerCase(),
                    "ethnicity", ethnicity,
                    "income", incomeValue.toLowerCase());
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenMultiPartWithDataApi(token, getString(R.string.api_update_profile), values, fileToUpload);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_update_profile), saveTV);
            }

            Log.e("postdata","update profile post data==="+values.toString());
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
        } else if (from.equals(getResources().getString(R.string.signature_conformation_msg))) {
            callAPIUpdateProfile();
        }
    }

    public void showSimpleAlert11(String from, String headerMsg, boolean isAddress, String btnCancel, boolean isCancel) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            Bundle args = new Bundle();
            args.putString("header", headerMsg);
            args.putString("textOk", getString(R.string.ok));
            args.putString("textCancel", btnCancel);
            args.putString("from", from);
            args.putBoolean("isAddress", isAddress);
            AlertForcePopup alert = new AlertForcePopup();
            alert.setCancelable(isCancel);
            alert.setArguments(args);
            alert.show(fm, "");
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onForceCallback(String from, boolean isAddress) {
        if (from.equals(getResources().getString(R.string.signature_conformation_msg))) {
            callAPIUpdateProfile();
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
        //cityAdapter = new CityAdapter(this, cityList);
        //citySpinner.setAdapter(cityAdapter);
        if (cityAdapter!=null){
            cityAdapter.notifyDataSetChanged();
        }else {
            cityAdapter = new CityAdapter(this, cityList);
            citySpinner.setAdapter(cityAdapter);
        }

        if (showCityName != null && showCityName.length() > 0) {
            int pos = 0;
            for (int i = 0; i < cityList.size(); i++) {
                if (cityList.get(i).getCity().equals(showCityName)) {
                    pos = i;
                }
            }
            citySpinner.setSelection(pos);
        }
    }

    private void setGenderAdapter() {
        String genderName = "";
        if (SharedPref.getPrefsHelper().getPref(Const.Var.SEX) != null && SharedPref.getPrefsHelper().getPref(Const.Var.SEX).toString().length() > 0) {
            genderName = SharedPref.getPrefsHelper().getPref(Const.Var.SEX).toString();
        }


        GenderAdapter genderAdapter = new GenderAdapter(this, genderList);
        genderSpinner.setAdapter(genderAdapter);

        if (genderName.length() > 0) {
            int pos = 0;
            for (int i = 0; i < genderList.size(); i++) {
                if (genderList.get(i).toLowerCase().equals(genderName)) {
                    pos = i;
                }
            }
            genderSpinner.setSelection(pos);
        }
    }

    private void setIncomeAdapter() {
        String income = "";
        if (SharedPref.getPrefsHelper().getPref(Const.Var.INCOME) != null && SharedPref.getPrefsHelper().getPref(Const.Var.INCOME).toString().length() > 0) {
            income = SharedPref.getPrefsHelper().getPref(Const.Var.INCOME).toString();
        }

        IncomeAdapter incomeAdapter = new IncomeAdapter(this, incomeList);
        incomeBracketSpinner.setAdapter(incomeAdapter);

        if (income.length() > 0) {
            int pos = 0;
            for (int i = 0; i < incomeList.size(); i++) {
                if (incomeList.get(i).equals(income)) {
                    pos = i;
                }
            }
            incomeBracketSpinner.setSelection(pos);
        }
    }

    private void datePickerDialog() {
        final Calendar myCalendar = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        myCalendar.add(Calendar.YEAR,-18);

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
                    selectDOB = dob;
                    dobEditText.setText(DateFormat.dateFormatConvert(dob));
                    ageEditText.setText(String.valueOf(ageInYear));
                } else {
                    showSimpleAlert(getString(R.string.age_must_be_18_years_or_older), "");
                }
            }
        }, mYear, mMonth, mDay);

        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
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
       /* if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }*/
        Integer ageInt = new Integer(age);
        return ageInt;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && data != null) {
            signatureHintTV.setVisibility(View.GONE);

            if (data.hasExtra("byteArray") && data.getByteArrayExtra("byteArray") != null) {
                int byteArrayLenth = Objects.requireNonNull(data.getByteArrayExtra("byteArray")).length;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data.getByteArrayExtra("byteArray"), 0, byteArrayLenth);
                //signatureImageView.setImageBitmap(bitmap);
                if (bitmap != null) {
                    Uri signatureUri = ImagePathUtil.getImageUri(EditProfileDialogActivity.this, bitmap);
                    signatureImageView.setImageURI(signatureUri);
                    try {
                        signaturePath = ImagePathUtil.getPath(EditProfileDialogActivity.this, signatureUri);
                   Log.e("signaturePath","signaturePath==="+signaturePath);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
