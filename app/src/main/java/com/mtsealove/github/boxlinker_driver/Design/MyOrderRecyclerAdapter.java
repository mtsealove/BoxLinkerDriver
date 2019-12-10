package com.mtsealove.github.boxlinker_driver.Design;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mtsealove.github.boxlinker_driver.MainActivity;
import com.mtsealove.github.boxlinker_driver.MyOrderActivity;
import com.mtsealove.github.boxlinker_driver.R;
import com.mtsealove.github.boxlinker_driver.Restful.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyOrderRecyclerAdapter extends RecyclerView.Adapter<MyOrderRecyclerAdapter.ItemViewHolder> {
    Context context;
    String tag = getClass().getSimpleName();
    boolean click = true;

    public MyOrderRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    private ArrayList<ResOrder> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_my_order, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(ResOrder data) {
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTv, stTv, dstTv, itemTv, stPhoneTv, dstPhoneTv, dstTimeTv, corpTv;
        LinearLayout clickLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            orderIdTv = itemView.findViewById(R.id.orderIdTv);
            stTv = itemView.findViewById(R.id.stTv);
            dstTv = itemView.findViewById(R.id.dstTv);
            itemTv = itemView.findViewById(R.id.itemTv);
            stPhoneTv = itemView.findViewById(R.id.stPhoneTv);
            dstPhoneTv = itemView.findViewById(R.id.dstPhoneTv);
            clickLayout = itemView.findViewById(R.id.clickLayout);
            corpTv = itemView.findViewById(R.id.corpTv);
            dstTimeTv = itemView.findViewById(R.id.dstTimeTv);
        }

        void onBind(final ResOrder data) {
            orderIdTv.setText("주문번호: " + data.getOrderID());
            stTv.setText("출발:" + data.getStdAddr());
            dstTv.setText("도착: " + data.getDstAddr());
            itemTv.setText("크기: " + data.getSize() + "cm 무게: " + data.getWeight() + "Kg");
            stPhoneTv.setText("보내는 사람: " + data.getStdPhone());
            dstPhoneTv.setText("받는 사람: " + data.getDstPhone());
            final String path = getImageDir() + data.getImagePath();
            if (click) {
                clickLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowDialog(data.getOrderID(), path, data.getDstAddr());
                    }
                });
            }
            if (data.getArrPrdtTm() != null) {
                dstTimeTv.setText("터미널 도착 시간:" + data.getArrPrdtTm().substring(11, 16));
                dstTimeTv.setVisibility(View.VISIBLE);
                corpTv.setText("회사명: "+data.getCorpNm());
                corpTv.setVisibility(View.VISIBLE);
            }
        }
    }

    private String getImageDir() {
        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        String result = pref.getString("ip", "") + "/images/";
        return result;
    }

    @SuppressLint("MissingPermission")
    private void ShowDialog(final String orderID, String path, final String dstAddr) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_order, null, false);
        ImageView imgIV = view.findViewById(R.id.imgIv);
        Glide.with(context)
                .load(path)
                .into(imgIV);
        TextView orderIdTv = view.findViewById(R.id.orderIdTv);
        orderIdTv.setText("주문번호: " + orderID);
        final ListView changeLV = view.findViewById(R.id.changeLv);
        final Button changeBtn = view.findViewById(R.id.completeBtn);
        Button findLocationBtn = view.findViewById(R.id.findLocationBtn);

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                list.add("상품 픽업");
                list.add("터미널 픽업");
                list.add("배송 완료");
                ArrayAdapter adapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, list);
                changeLV.setAdapter(adapter);
                changeLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int status = 1;
                        switch (position) {
                            case 0:
                                status = 5;
                                break;
                            case 1:
                                status = 6;
                                break;
                            case 2:
                                status = 4;
                                break;
                        }
                        UpdateOrderStatus(orderID, status);
                    }
                });
                changeLV.setVisibility(View.VISIBLE);
            }
        });

        //길찾기 버튼
        findLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location == null) {
                    location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                Geocoder geocoder = new Geocoder(context);
                try {
                    List<Address> addressList = geocoder.getFromLocationName(dstAddr, 10);
                    if (addressList != null && addressList.size() != 0) {
                        double latitude = addressList.get(0).getLatitude();
                        double longitude = addressList.get(0).getLongitude();
                        String url = "daummaps://route?sp=" + location.getLatitude() + "," + location.getLongitude() + "&ep=" + latitude + "," + longitude + "&by=CAR";
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(intent2);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //상태 업데이트
    private void UpdateOrderStatus(String OrderID, int status) {
        ReqOrderStatus reqOrderStatus = new ReqOrderStatus(OrderID, status);
        RestAPI restAPI = new RestAPI(context);
        Call<Res> call = restAPI.getRetrofitService().UpdateStatus(reqOrderStatus);
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body().isResult()) {
                        Toast.makeText(context, "상태가 변경되었습니다", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MyOrderActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                }
                Log.e(tag, response.toString());
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {

            }
        });
    }

}
