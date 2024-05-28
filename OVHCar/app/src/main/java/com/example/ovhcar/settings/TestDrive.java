package com.example.ovhcar.settings;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ovhcar.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class TestDrive extends AppCompatActivity {

    SharedPreferences getPrefs;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    String user = "oefuser";
    String pass = "oefuser12345";
    String server = "oefimboboapp.net";
    String ovhkey_literal;
    String ovhkey_literal_tmp;
    String ovhkey;
    //String _password = "lwaziz@spiderblackonline.co.za";
    //String _username = "Zwane";
    String _password = "lwazi.zwane@ovhstudio.co.za";
    String _username = "Zwane";
    String _CURRENT_CHECKIN_STATE = "0";
    private ProgressDialog progress;
    AlertDialog dialog;

    public static String _DEVICE_USER = "0";
    public static String _LEARNER_ID = "0";

    String serialNumber;
    String deviceSerialNo;

    ArrayList<String> DEVICE_USER_;
    ArrayList<String> OWNER_ID_;
    ArrayList<String> VEHICLE_ID_;


    DatePickerDialog datePickerDialog;
    Calendar currentdate;
    int mDay, mMonth, mShowMonth, mYear;
    EditText editTextTextDOB, editTextTextPersonName, editTextTextPersonSurname, editTextCell,editTextEmail;
    Spinner editTextTextGrade;

    JSONObject object_learner;
    JSONObject _object_learner;
    JSONArray learner_array;

    String country_code = "";
    String country_name = "";
    String province = "";
    Button save;
    ArrayList<String> noOfSiblingsArray;
    String _noOfSiblingsArray_value;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_drive);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, proceed with location functionality
            getLocation();
        }


        changeStatusBarColor();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //SharedPreferencde
        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        checkAndRequestPermissions();

        //deviceInfo();

        //OVH EV Model eV1 (25 kWh)   on the title 
        //


        //checkSettingSaved();

        DEVICE_USER_ = new ArrayList<>();
        OWNER_ID_ = new ArrayList<>();
        VEHICLE_ID_ = new ArrayList<>();


        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextTextPersonSurname = findViewById(R.id.editTextTextSurname);
        editTextCell = findViewById(R.id.editTextCell);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextTextDOB = findViewById(R.id.editTextDOT);
        editTextTextGrade = findViewById(R.id.editTextTextGrade);
        save = findViewById(R.id.save);

        save.setOnClickListener(_save);

        editTextTextDOB.setInputType(0);
        editTextTextDOB.setOnClickListener(_editTextTextDOB);

        currentdate = Calendar.getInstance();

        mDay = currentdate.get(Calendar.DAY_OF_MONTH);
        mMonth = currentdate.get(Calendar.MONTH);
        mYear = currentdate.get(Calendar.YEAR);
        mShowMonth = mMonth + 1;

        noOfSiblingsArray = new ArrayList<>();
        noOfSiblingsArray.add("0");
        noOfSiblingsArray.add("1");
        noOfSiblingsArray.add("2");

        editTextTextGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                _noOfSiblingsArray_value = noOfSiblingsArray.get(position);
                Log.w("Position array", _noOfSiblingsArray_value);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


    }

    private void getLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();


                            Log.w("Location", String.valueOf(latitude));

                            TestDrive.this.reverseGeo(latitude, longitude);

                            // Pass the latitude and longitude to your activity
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to show location
                getLocation();
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    View.OnClickListener _editTextTextDOB = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            editTextTextDOB.requestFocus();
            datePickerDialog = new DatePickerDialog(TestDrive.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                    month = month + 1;
                    String _finalDate = year + "/" + month + "/" + dayOfMonth;
                    editTextTextDOB.setText(_finalDate);
                }
            }, mYear, mMonth, mDay);
            //disable days less than current date
            //datePickerDialog.getDatePicker().setMinDate(currentdate.getTimeInMillis());

            //disable a specific date


            //show datePicker
            datePickerDialog.show();

        }
    };

    View.OnClickListener _save = view -> {

        String editTextTextPersonName_ = editTextTextPersonName.getText().toString();
        String editTextTextPersonSurname_ = editTextTextPersonSurname.getText().toString();
        String editTextTextCell = editTextCell.getText().toString();
        String editTextTextEmail_ = editTextEmail.getText().toString();
        String editTextTextDOB_ = editTextTextDOB.getText().toString();


        if (TextUtils.isEmpty(editTextTextPersonName_)) {
            editTextTextPersonName.setError("Field required");
            Toast.makeText(getApplicationContext(), "Name is required", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(editTextTextPersonSurname_)) {
            editTextTextPersonName.setError("Field required");
            Toast.makeText(getApplicationContext(), "Surname is required", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(editTextTextDOB_)) {
            editTextTextDOB.setError("Field required");
            Toast.makeText(getApplicationContext(), "DOB is required", Toast.LENGTH_LONG).show();

        }

        else if (TextUtils.isEmpty(editTextTextCell)) {
            editTextCell.setError("Field required");
            Toast.makeText(getApplicationContext(), "Cell no. is required", Toast.LENGTH_LONG).show();

        }

        else {
            progress = ProgressDialog.show(TestDrive.this, "Saving...", "Please wait!!!");
            progress.show();


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    try {

                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
                        for (NetworkInfo ni : netInfo)
                        {
                            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                                if (ni.isConnected())
                                {
                                    Save();

                                }else if(ni.getTypeName().equalsIgnoreCase("MOBILE"))
                                {
                                    if(ni.isConnected())
                                    {
                                        Save();
                                    }
                                }else
                                {
                                    Toast.makeText(getApplicationContext(), "Please check your network connection...", Toast.LENGTH_LONG).show();
                                }
                        }
                    }
                    catch (Exception e)
                    {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(TestDrive.this);
                        builder
                                .setMessage(e.getMessage()+" : Network low, please check email if details added")
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener()
                                        {
                                            public void onClick(DialogInterface dialog1, int id)
                                            {

                                            }
                                        }
                                );

                        AlertDialog alert = builder.create();
                        alert.show();
                        alert.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#0b4a87"));
                    }

                    progress.dismiss();
                    //whatever you want just you have to launch overhere.

                }
            }, 8000);

        }


    };

    void checkSettingSaved() {

        String _SP_editTextTextPersonName = getPrefs.getString("editTextTextPersonName_pref", null);
        String _SP_editTextTextPersonSurname = getPrefs.getString("editTextTextPersonSurname_pref", null);
        String _SP_editTextTextEmail = getPrefs.getString("editTextTextEmail_pref", null);
        String _SP_editTextTextCell = getPrefs.getString("editTextTextCell_pref", null);
        String _SP_editTextTextDOB = getPrefs.getString("editTextTextDOB_pref", null);
        String _SP_GenderSpinner = getPrefs.getString("nextOfKinSpinner_pref", null);


//        String _SP_gender = getPrefs.getString("gender_pref", null);
//
//        if (_SP_gender != null) {
//            gender = _SP_gender;
//
//            if(gender.equals("Male")){
//
//                gender = "2";
//
//            }else{
//                gender = "1";
//            }
//
//
//        }


        if (_SP_editTextTextPersonName != null) {
            editTextTextPersonName.setText(_SP_editTextTextPersonName);

        }
        if (_SP_editTextTextPersonSurname != null) {
            editTextTextPersonSurname.setText(_SP_editTextTextPersonSurname);
        }

        if (_SP_editTextTextDOB != null) {
            editTextTextDOB.setText(_SP_editTextTextDOB);
        }


        if (_SP_editTextTextEmail != null) {
            editTextEmail.setText(_SP_editTextTextEmail);
        }

        if (_SP_editTextTextCell != null) {
            editTextCell.setText(_SP_editTextTextCell);
        }


    }

//    void deviceInfo() {
//        try {
//
//            Thread thread = new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
//                        serialNumber = adInfo != null ? adInfo.getId() : null;
//                        Log.w("advertisingId", serialNumber);
//
//
//                        SharedPreferences.Editor editor = getPrefs.edit();
//                        editor.putString("TSerialNumber", serialNumber);
//                        editor.commit();
//
//
//                        String _getTSerialNumber = getPrefs.getString("TSerialNumber", null);
//                        Log.w("_getTSerialNumber", _getTSerialNumber);
//
//
//                    } catch (IOException | GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException exception) {
//                        exception.printStackTrace();
//                    }
//                }
//            };
//
//            // call thread start for background process
//            thread.start();
//
//
//        } catch (Exception e) {
//
//        }
//    }

    void Save() {

        String results = "";
        JSONArray json_;

        String editTextTextPersonName_ = editTextTextPersonName.getText().toString();
        String editTextTextPersonSurname_ = editTextTextPersonSurname.getText().toString();
        String editTextTextCell_ = editTextCell.getText().toString();
        String editTextTextEmail_ = editTextEmail.getText().toString();
        String editTextTextDOB_ = editTextTextDOB.getText().toString();
        String genderSpinner_ = editTextTextGrade.getSelectedItem().toString();



        SharedPreferences.Editor editor = getPrefs.edit();
        editor.putString("editTextTextPersonName_pref", editTextTextPersonName_);
        editor.putString("editTextTextPersonSurname_pref", editTextTextPersonSurname_);
        editor.putString("editTextTextCell_pref", editTextTextCell_);
        editor.putString("editTextTextEmail_pref", editTextTextEmail_);
        editor.putString("editTextTextDOB_pref", editTextTextDOB_);
        editor.putString("genderSpinner_pref", genderSpinner_);

        editor.commit();




        object_learner = new JSONObject();
        _object_learner = new JSONObject();
        learner_array = new JSONArray();

        try
        {
            object_learner.put("_name", editTextTextPersonName_);
            object_learner.put("_surname", editTextTextPersonSurname_);
            object_learner.put("_cell", editTextTextCell_);
            object_learner.put("_email", editTextTextEmail_);
            object_learner.put("_dot", editTextTextDOB_);
            object_learner.put("_gender", genderSpinner_);
            object_learner.put("_country_code", country_code);
            object_learner.put("_province", province);
            object_learner.put("_country_name", country_name);





            learner_array.put(object_learner);
            _object_learner.put("owner", learner_array);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try
        {
            //ovhkey_literal = "lwaziz@spiderblackonline.co.za" + ";;" + "Zwane" + ";;4;;1";// 14 for NEW STORE module
            ovhkey_literal = _username + ";;" + _password + ";;1;;1;;1";

            ovhkey_literal_tmp = Base64.encodeToString(ovhkey_literal.getBytes(), 1);
            ovhkey = Base64.encodeToString(ovhkey_literal_tmp.getBytes(), 1);
            //Log.w("KEYYYYYYYY", ovhkey_literal);
            String dateStamp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String timeStamp = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            String _getTSerialNumber = getPrefs.getString("TSerialNumber", null);
            if (_getTSerialNumber != null) {
                deviceSerialNo = _getTSerialNumber;
            }

            String _uri = "&_DEVICE_USER=" + URLEncoder.encode(_DEVICE_USER);
            _uri += "&_LEARNER_ID=" + URLEncoder.encode(_LEARNER_ID);
            _uri += "&device_serial_no=" + URLEncoder.encode(String.valueOf(deviceSerialNo));
            _uri += "&owner=" + URLEncoder.encode(String.valueOf(_object_learner));

            URL url_main_ = new URL("http://" + user + ":" + pass + "@" + server + "/webservice/rest/post/mod_settings.php?_ovhkey="+ ovhkey + _uri);

            Log.w("ERRRRRDXXXXEEEERRR111", url_main_.toString());

            URLConnection conn1_ = url_main_.openConnection();
            conn1_.setDoInput(true);
            results = "";
            String inputLine_;
            BufferedReader in_ = new BufferedReader(new InputStreamReader(url_main_.openStream()));

            while ((inputLine_ = in_.readLine()) != null){
                results = inputLine_;
            }
            in_.close();

            if((results == null) || (results.isEmpty()))
            {
                Toast.makeText(TestDrive.this, "Network connection problem", Toast.LENGTH_LONG).show();
            }else
            {
                if((results == null) || (results.isEmpty()))
                {
                    Toast.makeText(TestDrive.this, "Please refresh app..", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        json_ = new JSONArray(results);
                        JSONObject row = json_.getJSONObject(0);
                        if (row.getInt("status") == 0)
                        {
                            Toast.makeText(TestDrive.this, row.getString("desc"), Toast.LENGTH_LONG).show();
                        } else {
                            if ((row.getString("status") != null) && (!(((String) row.getString("status")).isEmpty()))) {


                                final AlertDialog.Builder builder = new AlertDialog.Builder(TestDrive.this);
                                builder
                                        .setMessage("" + row.getString("desc") + " ")
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                (dialog1, id) -> {


                                                }
                                        );

                                AlertDialog alert = builder.create();
                                alert.show();
                                alert.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#0b4a87"));

                                DEVICE_USER_.add(row.getString("learner"));

                                editor.putString("SP_CHECKIN_STATE_vehicle", String.valueOf(DEVICE_USER_));

                                editor.commit();

                                String driver_ = getPrefs.getString("SP_CHECKIN_STATE_vehicle", null);

                                //Save all the infomation on preference

                                editor = getPrefs.edit();
                                editor.putString("editTextTextPersonName_pref", editTextTextPersonName_);
                                editor.putString("editTextTextPersonSurname_pref", editTextTextPersonSurname_);
                                editor.putString("editTextTextCell_pref", editTextTextCell_);
                                editor.putString("editTextTextEmail_pref", editTextTextEmail_);
                                editor.putString("editTextTextDOB_pref", editTextTextDOB_);
                                editor.putString("genderSpinner_pref", genderSpinner_);

                                editor.commit();

                                Log.w("driver_", driver_);


                                driver_ = getPrefs.getString("SP_CHECKIN_STATE_vehicle", null);


                                if ((driver_ == null) || (driver_.isEmpty())) {

                                } else {

                                    try {
                                        JSONArray _road_surface_json = new JSONArray(driver_);
                                        JSONObject _road_surface_row = _road_surface_json.getJSONObject(0);

                                        Iterator<String> _road_surface_keys = _road_surface_row.keys();

                                        while (_road_surface_keys.hasNext()) {
                                            String keyValue = (String) _road_surface_keys.next();
                                            String status_ = _road_surface_row.getString("status");
                                            String desc_ = _road_surface_row.getString("desc");
                                            String data_ = _road_surface_row.getString("data");


                                            editor.putString("learner_desc", desc_);
                                            editor.putString("learner_data", data_);
                                            editor.commit();


                                            Log.w("valueStringstatus_", status_);
                                            Log.w("valueStringdesc_", desc_);
                                            Log.w("valueStringdata_", data_);
                                        }

                                    } catch (Exception e) {

                                    }

                                }

                                /*notice.setText(row.getString("desc"));notice.setTextColor(Color.parseColor("#3b8105"));*/
                                Toast.makeText(TestDrive.this, _CURRENT_CHECKIN_STATE, Toast.LENGTH_LONG).show();

                                Log.w("_CURRENT_CHECKIN_STATE", _CURRENT_CHECKIN_STATE);

                                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                float vol = 1f;
                                am.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);

                            } else {
                                Toast.makeText(TestDrive.this, row.getString("desc"), Toast.LENGTH_LONG).show();
                            }
                        }
                    }catch(Exception e)
                    {
                        Toast.makeText(TestDrive.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

        } catch (Exception e) {
            Log.w("ErrorIIIIIIIIII",e.getMessage());
            Toast.makeText(TestDrive.this, e.getMessage(), Toast.LENGTH_LONG).show();/*notice.setText(e.getMessage());notice.setTextColor(Color.parseColor("#ff0000"));*/
        }


    }
    // add access fine location and access background location to get allow location
    private boolean checkAndRequestPermissions() {
        int permissionACCESS_BACKGROUND_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        int permissionReadPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int permissionACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionProcessReadContacts = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int permissionACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionWRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionReadPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (permissionACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (permissionACCESS_BACKGROUND_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }

        if (permissionProcessReadContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (permissionACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (permissionWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    public void reverseGeo(Double Latitude, Double Longitude) {


        try {
            Geocoder geo = new Geocoder(TestDrive.this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(Latitude, Longitude, 1);

            if (addresses.isEmpty()) {

            } else {
                if (addresses.size() > 0) {

                    String  getFeatureName,
                            city, postal_code, city_municipality, suburb;
                    String street_address = "";
                    String road = "";


                    province = addresses.get(0).getAdminArea();
                    country_code = addresses.get(0).getCountryCode();
                    country_name = addresses.get(0).getCountryName();
//                    getFeatureName = addresses.get(0).getFeatureName();
//                    city = addresses.get(0).getLocality();
//                    postal_code = addresses.get(0).getPostalCode();
//                    city_municipality = addresses.get(0).getSubAdminArea();
//                    suburb = addresses.get(0).getSubLocality();
//                    street_address = addresses.get(0).getThoroughfare();

                    Log.w("country_code",country_code);
                    Log.w("country_name",country_name);


                }
            }
        } catch (Exception e) {
            Log.w("print_error3", e.getMessage());
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }


    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
    }
}