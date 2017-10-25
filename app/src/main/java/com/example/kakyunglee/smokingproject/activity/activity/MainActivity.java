package com.example.kakyunglee.smokingproject.activity.activity;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kakyunglee.smokingproject.R;
import com.example.kakyunglee.smokingproject.activity.activity.model.SelectedLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.kakyunglee.smokingproject.R.layout.report_dialog;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    double currentUserLatitude;
    double currentUserLongitude;
    private GoogleApiClient mGoogleApiClient;
    boolean no_smoking_clicked = false;
    boolean smoking_clicked = false;
    GoogleMap mGoogleMap;
    DrawerLayout drawer;
    private FusedLocationProviderClient mFusedLocationClient;
    SelectedLocation infoLocation = new SelectedLocation();
    MarkerOptions markerOptions = new MarkerOptions();
    // 움직이는 마커

    //위치정보 제공자

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init Api Client

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

       FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton fab_no_smoking = (ImageButton) findViewById(R.id.none_smoking_area);
        fab_no_smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(no_smoking_clicked == false){

                    Toast.makeText(getApplicationContext(), "필터 on", Toast.LENGTH_LONG).show();
                    no_smoking_clicked = true;

                }else{
                    Toast.makeText(getApplicationContext(), "필터 off", Toast.LENGTH_LONG).show();
                    no_smoking_clicked = false;
                }

            }
        });

        ImageButton fab_smoking = (ImageButton) findViewById(R.id.smoking_area);
        fab_smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(no_smoking_clicked == false){

                    Toast.makeText(getApplicationContext(), "필터 on", Toast.LENGTH_LONG).show();
                    smoking_clicked = true;

                }else{
                    Toast.makeText(getApplicationContext(), "필터 off", Toast.LENGTH_LONG).show();
                    smoking_clicked = false;
                }
            }
        });

        ImageButton fab = (ImageButton) findViewById(R.id.my_location);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "내위치 설정", Toast.LENGTH_LONG).show();
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem){
                // Handle navigation view item clicks here.
                int id = menuItem.getItemId();

                if (id == R.id.nav_notice) {
                    Intent intent = new Intent(MainActivity.this,NoticeListActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_law) {
                    Intent intent = new Intent(MainActivity.this,LawListActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_question) {
                    Intent intent = new Intent(MainActivity.this,QuestionActivity.class);
                    startActivity(intent);
                } else if(id == R.id.nav_info){
                    Intent intent = new Intent(MainActivity.this,AppInfoActivity.class);
                    startActivity(intent);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        Button reportBtn = (Button)findViewById(R.id.report);
        reportBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(report_dialog,null);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view);
                TextView textView = (TextView)view.findViewById(R.id.report_dialog_address);
                textView.setText("서울특별시 광진구 군자동 능동로 209");
                final AlertDialog dialog = builder.create();
                dialog.show();

                Button noBtn = (Button)view.findViewById(R.id.no);
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                Button yesBtn = (Button)view.findViewById(R.id.yes);
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();

                        /// DTO에 위도 경도 넣어주기 & 서버 전송송

                       //

                        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.report_detail_dialog,null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setView(view);
                        final AlertDialog dialog2 = builder.create();
                        dialog2.show();

                        Button skipBtn = (Button)view.findViewById(R.id.skip);
                        skipBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.cancel();
                            }
                        });

                        Button writeBtn = (Button)view.findViewById(R.id.write_detail);
                        writeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.cancel();
                                Intent intent = new Intent(MainActivity.this,ReportDetailActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMinZoomPreference(17.0f);
        mGoogleMap.setMaxZoomPreference(19.0f);
        //markerOptions.position(new LatLng(infoLocation.getSelectedLocationLatitude(),infoLocation.getSelectedLocationLongitude()));

        int userLocPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(userLocPermissionCheck== PackageManager.PERMISSION_DENIED){
            //다이얼로그 -> 퍼미션 non
            //default 서울시청 위치
            //Snackbar -> 퍼미션 허용 하시겠습니까?
            // 네트워크 작업이기 때문에 asyncTask 필요?
        }else{

            /*LatLng userLocation = new LatLng(37.566673, 126.978412);
            markerOptions.position(userLocation);
            googleMap.addMarker(markerOptions);*/

        }
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                LatLng targetLocation= latLng;
                mGoogleMap.clear();
                infoLocation.setSelectedLocationLatitude(latLng.latitude);
                infoLocation.setSelectedLocationLongitude(latLng.longitude);
                markerOptions.position(targetLocation);
                mGoogleMap.addMarker(markerOptions);
                Toast.makeText(MainActivity.this,targetLocation.toString(),Toast.LENGTH_SHORT).show();
                //요청
            }
        });
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Location mLastLocation = requestUserLastLocation();
                if (mLastLocation == null) {
                    Toast.makeText(MainActivity.this,"위치정보를 불러 올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                renewPinnedLocation(mLastLocation);
                return false;
            }
        });
    }
    // Permission check
    public Location requestUserLastLocation(){
        int userLocPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(userLocPermissionCheck != PackageManager.PERMISSION_DENIED)
            return LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        return null;
    }
    // selected Location renewing
    public void renewPinnedLocation(Location newLocation){
        mGoogleMap.clear();
        infoLocation.setSelectedLocationLatitude(newLocation.getLatitude());
        infoLocation.setSelectedLocationLongitude(newLocation.getLongitude());
        LatLng target = new LatLng(infoLocation.getSelectedLocationLatitude(),infoLocation.getSelectedLocationLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(target));
        markerOptions.position(target);
        mGoogleMap.addMarker(markerOptions);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation = requestUserLastLocation();
        if (mLastLocation == null) {
            Toast.makeText(MainActivity.this,"위치정보를 불러 올 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        renewPinnedLocation(mLastLocation);
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("api client error",connectionResult.getErrorMessage());
    }
}
