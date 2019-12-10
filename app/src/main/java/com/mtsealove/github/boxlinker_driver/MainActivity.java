package com.mtsealove.github.boxlinker_driver;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mtsealove.github.boxlinker_driver.Design.OrderRecyclerAdapter;
import com.mtsealove.github.boxlinker_driver.Design.SlideView;
import com.mtsealove.github.boxlinker_driver.Design.SystemUiTuner;
import com.mtsealove.github.boxlinker_driver.Restful.ResOrderSm;
import com.mtsealove.github.boxlinker_driver.Restful.RestAPI;
import com.mtsealove.github.boxlinker_driver.Service.UpdateLocationService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView newOrderRv;
    SlideView slideView;
    Button myOrderBtn, stopBtn;
    private LocationManager locationManager;
    double Latitude, Longitude;
    String tag = getClass().getSimpleName();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myOrderBtn = findViewById(R.id.myOrderBtn);
        stopBtn = findViewById(R.id.stopBtn);
        newOrderRv = findViewById(R.id.newOrderRv);
        slideView = findViewById(R.id.slideView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        newOrderRv.setLayoutManager(lm);
        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        GetLocation();
        myOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyOrderActivity.class);
                startActivity(intent);
            }
        });

        StartService();
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("운행 종료")
                        .setMessage("운행을 종료하시겠습니까?")
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StopService();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    //위치정보 받아오기
    @SuppressLint("MissingPermission")
    private void GetLocation() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("새 주문을 받아오는 중입니다");
        progressDialog.setCancelable(false);
        progressDialog.show();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            locationManager.removeUpdates(this);
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            GetNewOrders();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    //위치정보 기반 데이터 새 주문 받아오기
    private void GetNewOrders() {
        RestAPI restAPI = new RestAPI(this);
        Call<List<ResOrderSm>> call = restAPI.getRetrofitService().GetNewOrders(Latitude, Longitude);
        call.enqueue(new Callback<List<ResOrderSm>>() {
            @Override
            public void onResponse(Call<List<ResOrderSm>> call, Response<List<ResOrderSm>> response) {
                if (response.isSuccessful()) {
                    List<ResOrderSm> list = response.body();
                    OrderRecyclerAdapter adapter = new OrderRecyclerAdapter(MainActivity.this);
                    for (ResOrderSm resOrderSm : list) {
                        adapter.addItem(resOrderSm);
                    }
                    newOrderRv.setAdapter(adapter);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<ResOrderSm>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(tag, t.toString());
            }
        });
    }

    //서비스 실행중인지 확인
    public boolean isServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (UpdateLocationService.class.getName().equals(serviceInfo.service.getClassName())) return true;
        }
        return false;
    }

    //서비스 시작
    private void StartService() {
        if (!isServiceRunning()) {  //서비스가 실행중이 아니라면
            Intent service = new Intent(getApplicationContext(), UpdateLocationService.class);   //인텐트에 서비스를 넣어줌
            startService(service);  //서비스 시작
        }
    }

    //서비스 종료
    private void StopService() {
        if (isServiceRunning()) {   //서비스가 실행중이면
            Intent service = new Intent(getApplicationContext(), UpdateLocationService.class);   //인텐트에 서비스를 넣어줌
            stopService(service);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideView.CheckLogin();
    }
}
