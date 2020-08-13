package com.org.zapayapp.dialogs;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.org.zapayapp.R;
import com.org.zapayapp.ZapayApp;
import com.org.zapayapp.activity.BaseActivity;
import com.org.zapayapp.activity.ProfileActivity;
import com.org.zapayapp.adapters.StateAdapter;
import com.org.zapayapp.alert_dialog.SimpleAlertFragment;
import com.org.zapayapp.model.CityModel;
import com.org.zapayapp.model.StateModel;
import com.org.zapayapp.uihelpers.CustomTextInputLayout;
import com.org.zapayapp.utils.CommonMethods;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.DatePickerFragmentDialogue;
import com.org.zapayapp.utils.SharedPref;
import com.org.zapayapp.utils.WValidationLib;
import com.org.zapayapp.webservices.APICallback;
import com.org.zapayapp.webservices.APICalling;
import com.org.zapayapp.webservices.RestAPI;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

public class EditProfileDialogActivity extends AppCompatActivity implements View.OnClickListener, APICallback,SimpleAlertFragment.AlertSimpleCallback, DatePickerFragmentDialogue.DatePickerCallback {
    private TextView saveTV;
    private ImageView closeTV;
    private String header = "";

    public WValidationLib wValidationLib;
    /*Code for API calling*/
    protected ZapayApp zapayApp;
    protected Gson gson;
    protected APICalling apiCalling;
    protected RestAPI restAPI;

    private CustomTextInputLayout nameEditTextInputLayout;
    private CustomTextInputLayout mobileInputLayout;
    private CustomTextInputLayout address1InputLayout;
    private CustomTextInputLayout address2InputLayout;
    private CustomTextInputLayout postalCodeInputLayout;
    private CustomTextInputLayout ssnInputLayout;
    private CustomTextInputLayout dobInputLayout;

    private TextInputEditText nameEditText;
    private TextInputEditText mobileEditText;
    private TextInputEditText address1EditText;
    private TextInputEditText address2EditText;
    private TextInputEditText postalCodeEditText;
    private TextInputEditText ssnEditText;
    private TextInputEditText dobEditText;

    private Spinner stateSpinner,citySpinner;
    private List<StateModel> stateList;
    private StateAdapter stateAdapter;

    private List<CityModel> cityList;
    private CityAdapter cityAdapter;
    private String stateShortCode="";
    //private String cityId="";
    private String cityName="";
    private String firstName="";
    private String lastName="";
    private String dob="";
    private int ageInYear=0;
    private  String stateName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setBackgroundDrawable(CommonMethods.getDrawableWrapper(this, android.R.color.transparent));
       // supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_profile_dialog);
       // getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       // getIntentValues();
        apicodeInit();
        init();
        initAction();

    }


    private void apicodeInit() {
        zapayApp = (ZapayApp) getApplicationContext();
        restAPI = APICalling.webServiceInterface();
        gson = new Gson();
        apiCalling = new APICalling(this);
    }

    private void getIntentValues() {
        try {
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.getStringExtra("header") != null) {
                    header = intent.getStringExtra("header");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        wValidationLib=new WValidationLib(EditProfileDialogActivity.this);
        stateList=new ArrayList<>();
        cityList=new ArrayList<>();

        saveTV = findViewById(R.id.saveTV);
        closeTV = findViewById(R.id.closeTV);


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



        stateSpinner=findViewById(R.id.stateSpinner);
        citySpinner=findViewById(R.id.citySpinner);
         stateName=SharedPref.getPrefsHelper().getPref(Const.Var.STATE).toString();


      /*  dobEditText.setOnTouchListener((v, event) -> {
            datePickerDialog();
            return false;
        });*/

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
        closeTV.setOnClickListener(this);

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
                 //cityId = cityList.get(position).getId();
                 cityName = cityList.get(position).getCity();
                Log.e("bankAccountType","name======="+cityName);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void setDataOnScreen() {
        if (SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME) != null && SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME).toString().length() > 1) {
            nameEditText.setText(SharedPref.getPrefsHelper().getPref(Const.Var.FIRST_NAME, "") + " " + SharedPref.getPrefsHelper().getPref(Const.Var.LAST_NAME, ""));

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

        if (SharedPref.getPrefsHelper().getPref(Const.Var.POSTEL_CODE) != null && SharedPref.getPrefsHelper().getPref(Const.Var.POSTEL_CODE).toString().length() > 1) {
            postalCodeEditText.setText(SharedPref.getPrefsHelper().getPref(Const.Var.POSTEL_CODE, ""));
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
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            //finish();

           updateProfileFunc();
        }else if (v.equals(closeTV)){
            finish();
        }
    }


    private void updateProfileFunc(){
        try {
            if (wValidationLib.isEmpty(nameEditTextInputLayout, nameEditText, getString(R.string.important),true)) {
                String firstNameLastName = nameEditText.getText().toString().trim();
                String[] firstNameLastName1 = firstNameLastName.split(" ");
                if (firstNameLastName1.length > 1) {
                    firstName = firstNameLastName1[0];
                    lastName = firstNameLastName1[1];
                    if (wValidationLib.isEmpty(mobileInputLayout, mobileEditText, getString(R.string.important), true)) {
                        if (mobileEditText.getText().toString().trim().length()==10){
                            if (wValidationLib.isEmpty(address1InputLayout, address1EditText, getString(R.string.important), true)) {
                                String address1 = address1EditText.getText().toString().trim();
                                if (address1.length()<=51){
                                    if (address2EditText.getText().toString().trim().length()==0||address2EditText.getText().toString().trim().length()<=51){
                                        if (wValidationLib.isEmpty(postalCodeInputLayout, postalCodeEditText, getString(R.string.important), true)) {
                                            if (postalCodeEditText.getText().toString().trim().length()>=5){
                                                if (wValidationLib.isEmpty(ssnInputLayout, ssnEditText, getString(R.string.important), true)) {
                                                    if (ssnEditText.getText().toString().trim().length()>=4){
                                                        if (wValidationLib.isEmpty(dobInputLayout, dobEditText, getString(R.string.important), true)) {
                                                           // if (ageInYear>=18){
                                                                callAPIUpdateProfile();
                                                           //}else {
                                                            //    showSimpleAlert(getString(R.string.age_must_be_18_years_or_older), "");
                                                           // }
                                                        }


                                                    }else {
                                                        showSimpleAlert(getString(R.string.ssn_code_should_be_5_digit), "");

                                                    }

                                                }

                                            }else {
                                                showSimpleAlert(getString(R.string.postal_code_should_be_5_digit), "");
                                            }
                                        }

                                    }else {
                                        showSimpleAlert(getString(R.string.must_be_50_characters_or_less), "");
                                    }

                                }else {
                                    showSimpleAlert(getString(R.string.must_be_50_characters_or_less), "");

                                }

                            }
                        }else {
                            showSimpleAlert(getString(R.string.enter_valid_mobile),"");
                        }
                    }

                }else {
                    showSimpleAlert(getString(R.string.enter_last_name), "");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
            Call<JsonElement> call = restAPI.postApi(getString(R.string.api_get_cities_list),values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_get_cities_list), saveTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAPIUpdateProfile() {
        String token=SharedPref.getPrefsHelper().getPref(Const.Var.TOKEN).toString();
        try {
            HashMap<String, Object> values = apiCalling.getHashMapObject(
                    "first_name", firstName,
                    "last_name", lastName,
                    "mobile", mobileEditText.getText().toString().trim(),
                    "address1", address1EditText.getText().toString().trim(),
                    "address2", address2EditText.getText().toString().trim(),
                    "state",stateShortCode,
                    //"city",cityId,AAAA
                    "city",cityName,
                    "postal_code",postalCodeEditText.getText().toString().trim(),
                    "ssn",ssnEditText.getText().toString().trim(),
                    "dob",dobEditText.getText().toString().trim()
            );
            zapayApp.setApiCallback(this);
            Call<JsonElement> call = restAPI.postWithTokenApi(token,getString(R.string.api_update_profile),values);
            if (apiCalling != null) {
                apiCalling.callAPI(zapayApp, call, getString(R.string.api_update_profile), saveTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void apiCallback(JsonObject json, String from) {
        Log.e("json", "json======" + json);
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
                if (status==200){
                    if (json.get("data").getAsJsonArray()!=null){
                      // JsonArray jsonArray = json.get("data").getAsJsonArray();
                        List<StateModel> list = apiCalling.getDataList(json, "data", StateModel.class);
                        stateList.clear();
                        stateList.addAll(list);
                        setStateAdapter();

                    }
                }else {
                    showSimpleAlert(msg, "");
                }
            }else if (from.equals(getResources().getString(R.string.api_get_cities_list))){
                if (status==200){
                    if (json.get("data").getAsJsonArray()!=null){
                         //JsonArray jsonArray = json.get("data").getAsJsonArray();
                        List<CityModel> list = apiCalling.getDataList(json, "data", CityModel.class);
                        cityList.clear();
                        cityList.addAll(list);
                        setCityAdapter();
                    }
                }else {
                    cityName="";
                    cityList.clear();
                    setCityAdapter();
                   // showSimpleAlert(msg, "");
                }

            }else if (from.equals(getResources().getString(R.string.api_update_profile))){
                if (status==200){

                    showSimpleAlert(msg, getResources().getString(R.string.api_update_profile));
                }else {
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
        if (from.equals(getResources().getString(R.string.api_update_profile))){
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


    private void setCityAdapter(){
     /*   if (cityAdapter != null) {
            cityAdapter.notifyDataSetChanged();
        } else {
            cityAdapter = new CityAdapter(this, cityList);
            citySpinner.setAdapter(cityAdapter);
        }*/

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
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
             //  timeText.setText(sdf.format(myCalendar.getTime()));
               // dob=sdf.format(myCalendar.getTime());



                 dob= year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                 ageInYear=getAge(year,monthOfYear+1,dayOfMonth);

                if (ageInYear>=18){
                    dobEditText.setText(dob);
                }else {
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
        dob=selectedDate;
        dobEditText.setText(dob);

        Log.e("ageInYear","ageInYear==="+ageInYear);
        Log.e("ageInYear","dob==="+dob);
    }


    private int getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
       // String ageS = ageInt.toString();

        return ageInt;
    }

}
