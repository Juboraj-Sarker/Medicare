package com.juborajsarker.medicinealert.activity.more;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.activity.maps.NearbyMapActivity;
import com.juborajsarker.medicinealert.adapter.PlaceAdapter;
import com.juborajsarker.medicinealert.api.Api;
import com.juborajsarker.medicinealert.model.place.Geometry;
import com.juborajsarker.medicinealert.model.place.Location;
import com.juborajsarker.medicinealert.model.place.ModelPlace;
import com.juborajsarker.medicinealert.model.place.OpeningHours;
import com.juborajsarker.medicinealert.model.place.Photo;
import com.juborajsarker.medicinealert.model.place.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NearestPharmacyActivity extends AppCompatActivity {

    EditText radiusET;
    ImageView searchIV;
    RecyclerView recyclerView;
    LinearLayout mapLayout;


    PlaceAdapter adapter;
    List<Result> resultList;
    List<Geometry> geometryList;
    List<Location> locationList;
    List<Photo> photoList;
    List<OpeningHours> openingHoursList;

    Geometry geometry;
    Location location;
    OpeningHours openingHours;


    int radius;
    String category;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 929;
    boolean clicked = false;
    boolean refresh = false;

    ProgressDialog progressDialog;

    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager lm;
    LocationManager locationManager;

    double lat = 0;
    double lng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_pharmacy);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        init();




    }

    private void init() {

        radiusET = (EditText) findViewById(R.id.radius_ET);
        searchIV = (ImageView) findViewById(R.id.search_IV);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mapLayout = (LinearLayout) findViewById(R.id.mapLayout);


        resultList = new ArrayList<>();
        geometryList = new ArrayList<>();
        locationList = new ArrayList<>();
        photoList = new ArrayList<>();
        openingHoursList = new ArrayList<>();

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                hideInput();

                if (checkConnection()){


                    if (!checkLocationPermission()){

                        checkLocationPermission();

                    }else {

                        if (radiusET.getText().toString().equals("") ||
                                Integer.parseInt(radiusET.getText().toString()) == 0){

                            radiusET.setError("Please enter a valid radius");

                        }else {


                            if (lat == 0 && lng == 0) {

                                radius = Integer.parseInt(radiusET.getText().toString());
                                getCategory();
                                refreshLocation();


                            } else {

                                radius = Integer.parseInt(radiusET.getText().toString());
                                getCategory();
                                getNearbyPlaces();
                            }


                        }
                    }


                }else {

                    Toast.makeText(NearestPharmacyActivity.this, "No internet connection !!!", Toast.LENGTH_SHORT).show();
                }



            }
        });





        mapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                for (int i = 0; i < resultList.size(); i++) {

                    geometry = resultList.get(i).getGeometry();
                    geometryList.add(geometry);

                    location = resultList.get(i).getGeometry().getLocation();
                    locationList.add(location);

                    photoList = resultList.get(i).getPhotos();

                    openingHours = resultList.get(i).getOpeningHours();
                    openingHoursList.add(openingHours);
                }

                hideInput();

                Gson gson = new Gson();

                String locationListGson = gson.toJson(locationList);
                String resultListGson = gson.toJson(resultList);

                Intent intent = new Intent(NearestPharmacyActivity.this, NearbyMapActivity.class);
                intent.putExtra("currentLat", lat);
                intent.putExtra("currentLng", lng);

                intent.putExtra("locationListGson", locationListGson);
                intent.putExtra("resultListGson", resultListGson);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                super.onBackPressed();

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void hideInput(){

        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        } catch (Exception e) {


        }
    }

    public boolean checkConnection(){

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoWifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfoWifi.isConnected();
        NetworkInfo networkInfoMobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfoMobile.isConnected();

        if (isWifiConn || isMobileConn){

            return true;

        }else {

            return false;
        }
    }

    public boolean checkLocationPermission() {


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);


            return false;

        } else {

            return true;
        }
    }

    public void getCategory(){

        category = "pharmacy";
    }

    private void getNearbyPlaces() {


        if (lat != 0 && lng != 0) {

            int finalRadius = radius * 1000;

            String apiKey = "AIzaSyDifthkIH5dAuPeg4rnzs3hjBMI0_5g8hE";
            String url = createUrl(lat, lng, category, finalRadius, apiKey);
            Log.d("finalUrl", url);



            if (refresh){

                progressDialog.dismiss();
                refresh = false;
            }

            final ProgressDialog dialog = new ProgressDialog(NearestPharmacyActivity.this);
            dialog.setMessage("Please wait while loading data ......");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();

            String restUrl = "json?&location=" + Double.toString(lat) + ","
                    + Double.toString(lng)
                    + "&radius=" + finalRadius
                    + "&types=" + category
                    + "&sensor=false&key=" + apiKey;


            Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();

            Api api = retrofit.create(Api.class);
            Call<ModelPlace> call = api.getResults(restUrl);


            call.enqueue(new Callback<ModelPlace>() {
                @Override
                public void onResponse(Call<ModelPlace> call, Response<ModelPlace> response) {

                    ModelPlace modelPlace = response.body();


                    resultList = modelPlace.getResults();
                    adapter = new PlaceAdapter(resultList, NearestPharmacyActivity.this, lat, lng, 1);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NearestPharmacyActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    dialog.dismiss();

                    if (resultList.size() > 0){

                        mapLayout.setVisibility(View.VISIBLE);

                    }else {

                        Toast.makeText(NearestPharmacyActivity.this, "No place found between " + radius + " kilometer", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<ModelPlace> call, Throwable t) {

                    Toast.makeText(NearestPharmacyActivity.this, "Error found", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Log.d("ErrorOccur", t.getMessage());


                }
            });


        }


        clicked = false;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_LOCATION: {


                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {


                    }

                } else {


                }

            }

        }
    }

    public void checkGpsStatus() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("GPS is not enabled ");
            dialog.setIcon(android.R.drawable.ic_menu_mylocation);
            dialog.setMessage("Please turn on GPS first.");
            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings),

                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);

                        }
                    });

            dialog.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {


                }
            });
            dialog.show();
        }
    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onLocationChanged(android.location.Location loc) {

            lat = loc.getLatitude();
            lng = loc.getLongitude();


            progressDialog.dismiss();

            if (clicked) {

                getNearbyPlaces();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private void refreshLocation() {


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(android.location.Location location) {

                if (location != null) {


                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    getNearbyPlaces();


                }else {


                    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


                        refresh = true;
                        progressDialog = new ProgressDialog(NearestPharmacyActivity.this);
                        progressDialog.setMessage("Please wait while tracing your current location .......");
                        progressDialog.setCancelable(false);
                        progressDialog.show();


                        locationManager = (LocationManager)
                                getSystemService(Context.LOCATION_SERVICE);

                        final LocationListener locationListener = new MyLocationListener();

                        if (ActivityCompat.checkSelfPermission(NearestPharmacyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NearestPharmacyActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            progressDialog.dismiss();

                            return;
                        }



                        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                            if (ActivityCompat.checkSelfPermission(NearestPharmacyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(NearestPharmacyActivity.this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {

                                return;
                            }

                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER, 30000, 10, locationListener);

                        }else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER, 30000, 10, locationListener);
                        }


                    }else {

                        checkGpsStatus();
                        Toast.makeText(NearestPharmacyActivity.this, "GPS off", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });




    }

    public String createUrl(double latitude, double longitude,String category, int radius, String API_KEY){

        StringBuilder urlString = new StringBuilder("https://maps.googleapis.com/maps/api/place/search/json?");

        if (category.equals("")) {

            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=" + radius);
            //   urlString.append("&types="+place);
            urlString.append("&sensor=false&key=" + API_KEY);
        } else {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=" + radius);
            urlString.append("&types="+category);
            urlString.append("&sensor=false&key=" + API_KEY);



        }


        return urlString.toString();

    }
}
