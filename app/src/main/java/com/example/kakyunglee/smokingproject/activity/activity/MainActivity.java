package com.example.kakyunglee.smokingproject.activity.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kakyunglee.smokingproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.kakyunglee.smokingproject.R.layout.report_dialog;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    DrawerLayout drawer;

    // 움직이는 마커
    MarkerOptions markerOptions = new MarkerOptions();
    //위치정보 제공자

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab_no_smoking = (FloatingActionButton) findViewById(R.id.none_smoking_area);
        fab_no_smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab_smoking = (FloatingActionButton) findViewById(R.id.smoking_area);
        fab_smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.my_location);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        int userLocPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(userLocPermissionCheck== PackageManager.PERMISSION_DENIED){
            //다이얼로그 -> 퍼미션 non
            //default 서울시청 위치
            //Snackbar -> 퍼미션 허용 하시겠습니까?
        // 네트워크 작업이기 때문에 asyncTask 필요?
        }else{
            try{
                final LocationListener mLocationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        //여기서 위치값이 갱신되면 이벤트가 발생한다.
                        //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.
                        //정확도 테스트 요구
                        float accuracy = location.getAccuracy();    //정확도
                        String provider = location.getProvider();   //위치제공자

                        Log.d("test", "onLocationChanged, location:" + location);
                        double longitude = location.getLongitude(); //경도
                        double latitude = location.getLatitude();   //위도
                        //double altitude = location.getAltitude();   //고도

                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        markerOptions.position(userLocation);
                        //markerOptions.title("서울시청");
                        //markerOptions.snippet("서울특별시 시청 건물");
                        googleMap.addMarker(markerOptions);
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

                        //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
                        //Network 위치제공자에 의한 위치변화
                        //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
                        //tv.setText("위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
                        //       + "\n고도 : " + altitude + "\n정확도 : "  + accuracy);
                    }
                    public void onProviderDisabled(String provider) {
                        // Disabled시
                        Log.d("test", "onProviderDisabled, provider:" + provider);
                    }

                    public void onProviderEnabled(String provider) {
                        // Enabled시
                        Log.d("test", "onProviderEnabled, provider:" + provider);
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        // 변경시
                        Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
                    }
                };
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //Loc Provider : GPS
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        2000, // 통지사이의 최소 시간간격 (miliSecond)
                        1, // 통지사이의 최소 변경거리 (m)
                        mLocationListener);
                //Loc Provider : Network
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        2000, // 통지사이의 최소 시간간격 (miliSecond)
                        1, // 통지사이의 최소 변경거리 (m)
                        mLocationListener);

            }catch (SecurityException e){
                e.printStackTrace();
            }
        }

    }

}
