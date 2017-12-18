package com.example.user.homework;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient mFusedLocationClient;

    Location mLastLocation;
    GoogleMap mGoogleMap;
    EditText edt;
    double mlong;
    double mlati;
    LocationCallback mLocationCallback;

    final private int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION = 100;
    final private int REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (!checkLocationPermissions()) {
            requestLocationPermissions(REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION);
            requestLocationPermissions(REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES);
        } else{
            startLocationUpdates();
            //getLastLocation();
        }

        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddress();
            }
        });
    }
    //권한 설정
    private boolean checkLocationPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }
    //권한 설정
    private void requestLocationPermissions(int requestCode) {
        ActivityCompat.requestPermissions(
                SearchActivity.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},        // 요청할 권한 목록을 설정한 String 배열
                requestCode    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
        );
    }
    //권한 설정
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                    // startLocationUpdates();
                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT);
                }
            }
            break;
            case REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT);
                }

            }
        }
    }
    // 주소 검색해서 위도,경도 값 얻기.
    public void getAddress() {
        TextView tvt = (TextView) findViewById(R.id.result);
        edt = (EditText) findViewById(R.id.edt);
        String input = edt.getText().toString();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocationName(input, 1);
            if (addresses.size() > 0) {
                Address bestResult = (Address) addresses.get(0);

                mlong = bestResult.getLongitude();
                mlati = bestResult.getLatitude();


                tvt.setText(String.format("[ %s , %s ]",
                        bestResult.getLatitude(),
                        bestResult.getLongitude()));
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(bestResult.getLatitude(), bestResult.getLongitude())).title(edt.getText().toString()));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bestResult.getLatitude(), bestResult.getLongitude()), 15));
                mGoogleMap.setOnMarkerClickListener(new MyMarkerClickListener());
            }

        } catch (IOException e) {
            Log.e(getClass().toString(), "Failed in using Geocoder.", e);
        }
    }
    //마지막 위치 얻기
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        Task task = mFusedLocationClient.getLastLocation();       // Task<Location> 객체 반환
        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    mLastLocation = location;
                    Toast.makeText(getApplicationContext(),
                            "현재 위치",
                            Toast.LENGTH_SHORT)
                            .show();
                    LatLng newLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mGoogleMap.addMarker(new MarkerOptions().position(newLocation));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15));

                }
            }
        });
    } //위치 업데이트
    @SuppressWarnings("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locRequest = new LocationRequest();
        locRequest.setInterval(10000);
        locRequest.setFastestInterval(5000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mLastLocation = locationResult.getLastLocation();
                LatLng now = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                mGoogleMap.addMarker(new MarkerOptions().position(now));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(now, 15));
                mGoogleMap.setOnMarkerClickListener(new MyMarkerClickListener());
            }
        };

        mFusedLocationClient.requestLocationUpdates(locRequest,
                mLocationCallback,
                null /* Looper */);
    }

    //액션바
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.one).setChecked(true);
        return super.onCreateOptionsMenu(menu);
    }
    //액션바 클릭 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.now:
                getLastLocation(); // 마지막 위치(현재 위치)로 이동

                return true;
            case R.id.one:

                return true;
            case R.id.two:

                return true;
            case R.id.three:

                return true;
            /*default:
                return super.onOptionsItemSelected(item);*/
        }

        return super.onOptionsItemSelected(item);
    }
    //기본 화면
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LatLng hansung = new LatLng(37.5817891, 127.008175);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hansung,15));
    }
    // 마커 클릭시 RegisterActivity로 넘어감.
    public void save(){
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("addr",edt.getText().toString());
        intent.putExtra("long", mlong);
        intent.putExtra("lati",mlati);
        startActivityForResult(intent,11);
    }
    class addMarkerClickListener implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {

            return false;
        }
    }
    class MyMarkerClickListener implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            AlertDialog.Builder a= new AlertDialog.Builder(SearchActivity.this);
            a.setTitle("맛집 등록");
            a.setMessage("맛집으로 등록하시겠습니까?");
            a.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    save();
                }
            });
            a.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            a.create();
            a.show();
            //Toast.makeText(getApplicationContext(),"한성대학교를 선택하셨습니다", Toast.LENGTH_SHORT).show();
            return false;
        }
        //Toast.makeText(getApplicationContext(),"한성대학교를 선택하셨습니다", Toast.LENGTH_SHORT).show();

    }

}


