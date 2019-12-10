package com.mtsealove.github.boxlinker_driver;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mtsealove.github.boxlinker_driver.Design.MyOrderRecyclerAdapter;
import com.mtsealove.github.boxlinker_driver.Design.SlideView;
import com.mtsealove.github.boxlinker_driver.Design.SystemUiTuner;
import com.mtsealove.github.boxlinker_driver.Restful.ResOrder;
import com.mtsealove.github.boxlinker_driver.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MyOrderActivity extends AppCompatActivity {
    RecyclerView myOrderRv;
    SlideView slideView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        myOrderRv = findViewById(R.id.MyOrderRv);
        slideView=findViewById(R.id.slideView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        myOrderRv.setLayoutManager(lm);

        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        GetMyOrders();
    }

    private void GetMyOrders() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("정보를 가져오는 중입니다");
        progressDialog.show();
        RestAPI restAPI = new RestAPI(this);
        Call<List<ResOrder>> call = restAPI.getRetrofitService().GetMyOrders(GetPhone());
        call.enqueue(new Callback<List<ResOrder>>() {
            @Override
            public void onResponse(Call<List<ResOrder>> call, Response<List<ResOrder>> response) {
                if (response.isSuccessful()) {
                    MyOrderRecyclerAdapter adapter = new MyOrderRecyclerAdapter(MyOrderActivity.this);
                    List<ResOrder> list = response.body();
                    for (ResOrder order : list) {
                        adapter.addItem(order);
                    }
                    myOrderRv.setAdapter(adapter);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<ResOrder>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private String GetPhone() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString("phone", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideView.CheckLogin();
    }
}
