package com.example.user.homework;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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

import com.example.user.homework.data.DBHelper;
import com.example.user.homework.detailfragment.DetailMainActivity;
import com.example.user.homework.detailfragment.DetailMainFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback {

    private FusedLocationProviderClient mFusedLocationClient;

    Location mLastLocation;
    GoogleMap mGoogleMap;
    EditText edt;
    double mlong;
    double mlati;
    LocationCallback mLocationCallback;
    private List<Marker> mMarkerList;
    private DBHelper dbHelper;

    // /거리
    private float distanceRange = 1;//default 1km

    final private int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION = 100;
    final private int REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES = 101;

    //메인 마커
    private Marker mainMarker = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHelper = new DBHelper(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (!checkLocationPermissions()) {
            requestLocationPermissions(REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION);
            requestLocationPermissions(REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES);
        } else {
            startLocationUpdates();
            //getLastLocation();
        }

        mMarkerList = new ArrayList<>();

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //현재위치 얻어오는 행동 멈추기
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);

                //구글 화면초기화. 모든 마커 삭제
                mGoogleMap.clear();
                mMarkerList.clear();

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
                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    // 주소 검색해서 위도,경도 값 얻기.
    public void getAddress() {

        TextView tvt = (TextView) findViewById(R.id.result);
        edt = (EditText) findViewById(R.id.edt);
        String input = edt.getText().toString();

        //이름을 LiKE 검색하여 비슷한 이름이 있는지 찾기
        //최종 위치를 저장하기 위한 여러 변수들 선언.
        Cursor cursor = dbHelper.getAllHomeworksBySQLForName(input);
        float minDistance = 10000000;
        boolean checkData = false;
        boolean checkFindData = true;
        Location reLocation = null;
        String name = input;
        int _id = 0;

        //조회된 데이터가 있다면.. 반복문을 통해 찾는다.
        //단 가장 가까운 거리 한개만을 위하기 때문에 min데이터를 찾는다.
        while (cursor.moveToNext()) {

            //거리계산
            Location temp = new Location(LocationManager.GPS_PROVIDER);
            temp.setLatitude(cursor.getDouble(6));
            temp.setLongitude(cursor.getDouble(7));
            float distance = mLastLocation.distanceTo(temp);
            Log.e("distance", distance + " " + distanceRange);

            //가장 짧은 거리의 데이터를 찾지만 거리가 10km가 넘어가면 구글검색을 통해 찾는다.
            if (minDistance > distance && distance < 10000) {
                checkData = true;
                minDistance = distance;
                //최종위치 셋팅
                reLocation = new Location(LocationManager.GPS_PROVIDER);
                reLocation.setLatitude(cursor.getDouble(6));
                reLocation.setLongitude(cursor.getDouble(7));
                name = cursor.getString(1);
                _id = cursor.getInt(0);
            }
        }


        //찾은 데이터가 있다면 그곳으로 갱신
        if (checkData) {

            //조회된 위치를 표시하기
            tvt.setText(String.format("[ %s , %s ]", reLocation.getLatitude(), reLocation.getLongitude()));

            //찾은 데이터의 위도와 경도로 위치객체를 얻고
            LatLng position = new LatLng(reLocation.getLatitude(), reLocation.getLongitude());
            //해당객체를 통해 마커를 등록한다.
            mainMarker = mGoogleMap.addMarker(new MarkerOptions().position(position).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            //등록된 데이터이기 때문에 조회시 사용할 key값을 tag에 넣어놓는다.
            mainMarker.setTag(_id);
            //조회된 장소로 카메라 이동.
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(reLocation.getLatitude(), reLocation.getLongitude()), 15));



        } else {//데이터 베이스에서 조회된 데이터가 없다면 기존 구글검색을 통해 위치갱신

            //위치값 가져오기
            try {
                Geocoder geocoder = new Geocoder(this, Locale.KOREA);
                List<Address> addresses = geocoder.getFromLocationName(input, 1);

                //조회된 주소록이 있다면. 그중에 가장 첫번째 데이터만 이용하여 조회.
                if (addresses.size() > 0) {
                    Address bestResult = (Address) addresses.get(0);

                    //해당위치의 위도와 경도를 가져온다.
                    mlong = bestResult.getLongitude();
                    mlati = bestResult.getLatitude();

                    //최종위치 셋팅
                    //위치의 위도와 경도로 location 객체를 얻는다.
                    reLocation = new Location(LocationManager.GPS_PROVIDER);
                    reLocation.setLatitude(mlati);
                    reLocation.setLongitude(mlong);

                    //위치를 입력하고
                    tvt.setText(String.format("[ %s , %s ]",
                            bestResult.getLatitude(),
                            bestResult.getLongitude()));

                    //마커표시 //주소로 조회된 장소이기 때문에 마커등록을 활성화 환다.(태그입력X)
                    mainMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(bestResult.getLatitude(), bestResult.getLongitude())).title(edt.getText().toString()));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bestResult.getLatitude(), bestResult.getLongitude()), 15));

                }else{

                    //데이터베이스에서도 조회된 것이 없고
                    //구글검색을 통해서도 조회된 것이 없는 경우.
                    checkFindData = false;

                    //현재위치를 기반으로 동작한다.
                    startLocationUpdates();
                }
            } catch (IOException e) {
                Log.e(getClass().toString(), "Failed in using Geocoder.", e);
            }
        }

        //등록된 데이터 또는 주소로 찾은 장소가 있다면..
        if(checkFindData){
            //마지막위치를 검색된 위치로 지정
            mLastLocation = reLocation;

            //주변에 등록된 맛집이 있는지 보여주기.
            showData();
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
                    mainMarker = mGoogleMap.addMarker(new MarkerOptions().position(newLocation));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15));

                    Log.e("데이터조회", "데이터조회");

                    //마지막 위치 근처에 존재하는 등록데이터 보여주기
                    showData();
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

                mainMarker = mGoogleMap.addMarker(new MarkerOptions().position(now));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(now, 15));
                Log.e("startLocationUpdates", "startLocationUpdates");
                showData();
            }
        };

        mFusedLocationClient.requestLocationUpdates(locRequest,
                mLocationCallback,
                null /* Looper */);
    }

    //액션바
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //메뉴정보를 가져온다.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        //1km를 기본 선택정보로 한다.
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

                //보여주는 거리를 1km로 지정
                distanceRange = 1;
                //구글 화면초기화. 모든 마커 삭제
                mGoogleMap.clear();
                mMarkerList.clear();
                //메인마커 다시 추가하기
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
                //주변마커 재생성
                showData();
                return true;
            case R.id.two:
                //보여주는 거리를 2km로 지정
                distanceRange = 2;
                //구글 화면초기화. 모든 마커 삭제
                mGoogleMap.clear();
                mMarkerList.clear();
                //메인마커 다시 추가하기
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
                //주변마커 재생성
                showData();
                return true;
            case R.id.three:
                //보여주는 거리를 3km로 지정
                distanceRange = 3;
                //구글 화면초기화. 모든 마커 삭제
                mGoogleMap.clear();
                mMarkerList.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
                //주변마커 재생성
                showData();
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
        mGoogleMap.setOnMarkerClickListener(this);

        //현재위치를 가져온다.
        getLastLocation();

    }

    // 마커 클릭시 RegisterActivity로 넘어감.
    public void save() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("addr", edt.getText().toString());
        intent.putExtra("long", mlong);
        intent.putExtra("lati", mlati);
        startActivityForResult(intent, 11);
    }

    //등록된 맛집들 마커 보여주기
    public void showData() {

        Cursor cursor = dbHelper.getAllHomeworksBySQL();
        while (cursor.moveToNext()) {

            //거리계산
            Location temp = new Location(LocationManager.GPS_PROVIDER);
            temp.setLatitude(cursor.getDouble(6));
            temp.setLongitude(cursor.getDouble(7));
            float distance = mLastLocation.distanceTo(temp);
            Log.e("distance", distance + " " + distanceRange);

            //조회된 데이터와 현재위치가 간다면.. 통과
            if (mLastLocation.getLatitude() == cursor.getDouble(6)
                    && mLastLocation.getLongitude() == cursor.getDouble(7)) {

                //현재위치와 같으면 통과

            } else {

                //현재위치와 다르다면. 메뉴에서 선택한 거리만큼(1,2,3km) 조회
                if (distanceRange * 1000 >= distance) {

                    //마커위치 객체를 저장된 위도와 경도를 통해 생성하고.
                    LatLng position = new LatLng(cursor.getDouble(6), cursor.getDouble(7));
                    //생성된 위치정보를 기준으로 푸른 마커를 생성한다.
                    Marker newMarker = mGoogleMap.addMarker(new MarkerOptions().position(position).title(cursor.getString(1))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    //데이터베이스에서 조회된 정보기 때문에 클릭시 이동가능하게 표시한다.
                    newMarker.setTag(cursor.getInt(0));
                    mMarkerList.add(newMarker);
                    Log.e("dddb", Integer.toString(cursor.getInt(0)) + " " + Double.toString(cursor.getDouble(6)) + " " + Double.toString(cursor.getDouble(7)));

                }
            }

        }
    }

    //빨간 마커(데이터베이스에서 조회된 빨간마커는 해당 기능이 동작하지 않게..)== 위치 검색 및 맛집등록
    public void onRedMarkerClick() {
        AlertDialog.Builder a = new AlertDialog.Builder(SearchActivity.this);
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

    }

    // 파란 마커 == 등록된 맛집 정보
    public void onBlueMarkerClick(Integer markerId) {
        Intent intent = new Intent(getApplicationContext(), DetailMainActivity.class);
        DetailMainFragment.parentId = markerId + "";
        startActivity(intent);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        //태그정보가 있으면 데이터베이스에서 조회된 데이터.
        //없다면 새로 등록가능한 데이터로 판단한다.
        if (marker.getTag() == null)
            onRedMarkerClick();
        else
            onBlueMarkerClick((Integer) marker.getTag());

        return false;
    }
}







