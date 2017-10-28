package com.example.kakyunglee.smokingproject.activity.activity;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
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
import com.example.kakyunglee.smokingproject.activity.dto.NoticeListDTO;
import com.example.kakyunglee.smokingproject.activity.dto.response.ReverseGeoCodeResult;
import com.example.kakyunglee.smokingproject.activity.geointerface.AddressInfo;
import com.example.kakyunglee.smokingproject.activity.serviceinterface.GetNoticeInfo;
import com.example.kakyunglee.smokingproject.activity.util.GeoRetrofit;
import com.example.kakyunglee.smokingproject.activity.util.ServiceRetrofit;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.kakyunglee.smokingproject.R.layout.report_dialog;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    String currentAddress="";
    GoogleMap mGoogleMap;
    DrawerLayout drawer;
    private FusedLocationProviderClient mFusedLocationClient;
    SelectedLocation infoLocation = new SelectedLocation();
    MarkerOptions markerOptions = new MarkerOptions();


    /////////////////////////////////////
    private ImageButton fab_no_smoking; // 금연 구역 필터 버튼
    private ImageButton fab_smoking; //흡연 구역 필터 버튼
    private Button reportBtn; // 신고하기 버튼
    private NavigationView navigationView; // 내비게이션 뷰
    ///////////////////////////////////
    private boolean no_smoking_clicked = false; // 금연 구역 필터 버튼 눌림 유지
    private boolean smoking_clicked = false;  // 흡연 구역 필터 버튼 눌림 유지

    ////////////////////////////////////////

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

        ///////////////////////////////////////
        fab_no_smoking = (ImageButton) findViewById(R.id.none_smoking_area);
        fab_smoking = (ImageButton) findViewById(R.id.smoking_area);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        reportBtn = (Button)findViewById(R.id.report);

        ////////////////////////////////////////

        // 금연구역 필터 버튼
        fab_no_smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areaNonSmokingButtonClicked();
            }
        });

        // 흡연구역 필터 버튼
        fab_smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areaSmokingButtonClicked();
            }
        });

        // 내비게이션 불러오기
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // 내비게이션에서 선택된 리스트로 이동
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem){
                // Handle navigation view item clicks here.
                int id = menuItem.getItemId();   // 선택된 menu
                selectedNavigationItem(id);       // 선택된 menu로 이동
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // 신고하기 버튼
        reportBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(report_dialog,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view);


                TextView textView = (TextView)view.findViewById(R.id.report_dialog_address);
                //사용자가 설정한 마커 또는 사용자 위치의 주소 입력 ""
                textView.setText("서울특별시 광진구 군자동 능동로 209");

                final AlertDialog dialog = builder.create();
                dialog.show();

                // 신고하지 않을 경우
                Button noBtn = (Button)view.findViewById(R.id.no);
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                // 신고할 경우
                Button yesBtn = (Button)view.findViewById(R.id.yes);
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();

                        /// DTO에 위도 경도 넣어주기 & 서버 전송송
                        /*
                        ReportDTO reportDTO = new ReportDTO();
                        reportDTO.setLatitude(위도변수);
                        reportDTO.setLongititude(경도변수);
                        */
                       //
                        // 두번째 다이얼로그 만들기
                        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.report_detail_dialog,null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setView(view);
                        final AlertDialog  dialog2 = builder.create();
                        dialog2.show();

                        //상세 신고를 하지 않는 경우
                        Button skipBtn = (Button)view.findViewById(R.id.skip);
                        skipBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.cancel();
                            }
                        });

                        //상세신고를 하는 경우
                        Button writeBtn = (Button)view.findViewById(R.id.write_detail);
                        writeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doDetailNotice(dialog2);
                            }
                        });
                    }
                });
            }
        });
    }

    // 금연 구역 필터 on off 확인 함수
    private  void areaNonSmokingButtonClicked(){

        if(no_smoking_clicked == false){

            Toast.makeText(getApplicationContext(), "금연 구역 필터 on", Toast.LENGTH_LONG).show();
            fab_no_smoking.setBackgroundResource(R.drawable.filter_pressed_button);
            no_smoking_clicked = true;

        }else{
            Toast.makeText(getApplicationContext(), "금연 구역 필터 off", Toast.LENGTH_LONG).show();
            fab_no_smoking.setBackgroundResource(R.drawable.filter_button);
            no_smoking_clicked = false;
        }
        return;
    }

    // 흡연 구역 필터 on off 확인 함수
    private void areaSmokingButtonClicked(){
        if(smoking_clicked == false){

            Toast.makeText(getApplicationContext(), "흡연 구역 필터 on", Toast.LENGTH_LONG).show();
            fab_smoking.setBackgroundResource(R.drawable.filter_pressed_button);

            smoking_clicked = true;

        }else{
            Toast.makeText(getApplicationContext(), "흡연 구역 필터 off", Toast.LENGTH_LONG).show();
            fab_smoking.setBackgroundResource(R.drawable.filter_button);
            smoking_clicked = false;
        }
        return;
    }

    //내비게이션에서 선택된 리스트로 이동하는 함수
    private void selectedNavigationItem(int id){
        if (id == R.id.nav_notice) { // 공지사항으로 이동

            GetNoticeInfo getNoticeInfo = ServiceRetrofit.getInstance().getRetrofit().create(GetNoticeInfo.class);

            final Call<NoticeListDTO> call=getNoticeInfo.noticeInfo();
            new GetNoticeList().execute(call);

        } else if (id == R.id.nav_law) { //법률 정보로 이동
            Intent intent = new Intent(MainActivity.this,LawListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_question) { // 정부 문의로 이동
            Intent intent = new Intent(MainActivity.this,QuestionActivity.class);
            startActivity(intent);

        } else if(id == R.id.nav_info){ // 앱 정보로 이동
            Intent intent = new Intent(MainActivity.this,AppInfoActivity.class);
            startActivity(intent);
        }
    }

    //상세신고를 작성하는 경우
    private void doDetailNotice(AlertDialog dialog2){
        dialog2.cancel();
        Intent intent = new Intent(MainActivity.this,ReportDetailActivity.class);
        // 위도 경도 전송
                                /*
                                intent.putExtra("latitude",위도변수);
                                intent.putExtra("longitude",경도변수);
                                intent.putExtra("address",주소변수);
                                */
        startActivity(intent);
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

    ////////////////////// retrofit 삽입 : NoticeList/////////////////////////
    private class GetNoticeList extends AsyncTask<Call,Void, NoticeListDTO> {
        @Override
        protected NoticeListDTO doInBackground(Call ... params){
            try{
                Call<NoticeListDTO> call = params[0];
                Response<NoticeListDTO> response = call.execute();
                return response.body();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(NoticeListDTO result) {
            Bundle extras=new Bundle();
            extras.putSerializable("notice_list",result);
            Intent intent = new Intent(MainActivity.this,NoticeListActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMinZoomPreference(17.0f);
        mGoogleMap.setMaxZoomPreference(21.0f);
        //markerOptions.position(new LatLng(infoLocation.getSelectedLocationLatitude(),infoLocation.getSelectedLocationLongitude()));

        int userLocPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(userLocPermissionCheck== PackageManager.PERMISSION_DENIED){
            //다이얼로그 -> 퍼미션 non
            //default 서울시청 위치
            //Snackbar -> 퍼미션 허용 하시겠습니까?
            // 네트워크 작업이기 때문에 asyncTask 필요?
        }else{
            mGoogleMap.setMyLocationEnabled(true);
            /*LatLng userLocation = new LatLng(37.566673, 126.978412);
            markerOptions.position(userLocation);
            googleMap.addMarker(markerOptions);*/

        }
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                renewPinnedLocation(null,latLng);
            }
        });
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Location mLastLocation = requestUserLastLocation();
                renewPinnedLocation(mLastLocation,null);
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
    public void renewPinnedLocation(Location newLocation, LatLng newLatlng){
        int selectedLogin = 0;
        final int LOCATION_FLAG = 0;
        final int LATLNG_FLAG = 1;
        String refinedLatLng = "";

        if(newLocation == null && newLatlng == null){
            Toast.makeText(MainActivity.this, "위치정보 가져오기 에러",Toast.LENGTH_SHORT).show();
            return;
        }
        if(newLatlng !=null) selectedLogin = 1;
        //set pin Login
        mGoogleMap.clear();
        switch(selectedLogin) {
            case LOCATION_FLAG:
                infoLocation.setSelectedLocationLatitude(newLocation.getLatitude());
                infoLocation.setSelectedLocationLongitude(newLocation.getLongitude());
                LatLng target = new LatLng(infoLocation.getSelectedLocationLatitude(), infoLocation.getSelectedLocationLongitude());
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(target));
                markerOptions.position(target);
                mGoogleMap.addMarker(markerOptions);
                break;
            case LATLNG_FLAG:
                infoLocation.setSelectedLocationLatitude(newLatlng.latitude);
                infoLocation.setSelectedLocationLongitude(newLatlng.longitude);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(newLatlng));
                markerOptions.position(newLatlng);
                mGoogleMap.addMarker(markerOptions);
                break;
            default:
                break;
        }
        DecimalFormat formLat = new DecimalFormat("##.######");
        DecimalFormat formLng = new DecimalFormat("###.######");
        refinedLatLng=formLat.format(infoLocation.getSelectedLocationLatitude()) + "," + formLng.format(infoLocation.getSelectedLocationLongitude());
        Toast.makeText(
                MainActivity.this,
                refinedLatLng,
                Toast.LENGTH_SHORT
        ).show();
        AddressInfo getAddress = GeoRetrofit.getInstance().getRetrofit().create(AddressInfo.class);
        Call<ReverseGeoCodeResult> callGeoCodeResult = getAddress.reverseGeoResult(refinedLatLng,"ko","AIzaSyA8lmYR7nzLGTmbPd1KSl4R-B__-bNOr9k");
        new NetWorkGeoInfo().execute(callGeoCodeResult);
        //get address
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation = requestUserLastLocation();
        renewPinnedLocation(mLastLocation,null);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("api client error",connectionResult.getErrorMessage());
    }

    private class NetWorkGeoInfo extends AsyncTask<Call,Void,ReverseGeoCodeResult>{

        @Override
        protected ReverseGeoCodeResult doInBackground(Call... params) {
            try{
                Call<ReverseGeoCodeResult> call = params[0];
                Response<ReverseGeoCodeResult> response = call.execute();
                return response.body();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(ReverseGeoCodeResult result){
            if(result==null){
                Toast.makeText(MainActivity.this,"요청 failed",Toast.LENGTH_SHORT).show();
            }else{
            Log.d("ckhlogging",result.getStatus());
            currentAddress=result.getResults().get(0).getFormattedAddress();
            Toast.makeText(MainActivity.this,currentAddress,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
