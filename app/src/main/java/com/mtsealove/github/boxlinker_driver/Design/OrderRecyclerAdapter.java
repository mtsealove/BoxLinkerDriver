package com.mtsealove.github.boxlinker_driver.Design;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mtsealove.github.boxlinker_driver.MainActivity;
import com.mtsealove.github.boxlinker_driver.R;
import com.mtsealove.github.boxlinker_driver.Restful.ReqTakeOrder;
import com.mtsealove.github.boxlinker_driver.Restful.Res;
import com.mtsealove.github.boxlinker_driver.Restful.ResOrderSm;
import com.mtsealove.github.boxlinker_driver.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ItemViewHolder> {
    Context context;

    public OrderRecyclerAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<ResOrderSm> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_new_order, parent, false);
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

    public void addItem(ResOrderSm data) {
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTv, stTv, dstTv, itemTv, dstTimeTv;
        LinearLayout clickLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            orderIdTv = itemView.findViewById(R.id.orderIdTv);
            stTv = itemView.findViewById(R.id.stTv);
            dstTv = itemView.findViewById(R.id.dstTv);
            itemTv = itemView.findViewById(R.id.itemTv);
            dstTimeTv = itemView.findViewById(R.id.dstTimeTv);
            clickLayout = itemView.findViewById(R.id.clickLayout);
        }

        void onBind(final ResOrderSm data) {
            orderIdTv.setText("주문번호: " + data.getOrderID());
            stTv.setText("출발:" + data.getStdAddr());
            dstTv.setText("도착: " + data.getDstAddr());
            itemTv.setText("크기: " + data.getSize() + "cm 무게: " + data.getWeight() + "Kg");

            clickLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
                    String phone = pref.getString("phone", "");
                    TakeOrder(data.getOrderID(), phone, data.isStart());
                }
            });
            if(data.getArrPrdtTm()!=null) {
                dstTimeTv.setText("터미널 도착 시간: "+data.getArrPrdtTm().substring(11, 16));
                dstTimeTv.setVisibility(View.VISIBLE);
            }
        }
    }

    //주문 수락
    private void TakeOrder(final String OrderID, final String Phone, final boolean Start) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("주문 수락")
                .setMessage("주문을 수락하시겠습니까?")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RestAPI restAPI = new RestAPI(context);
                ReqTakeOrder reqTakeOrder = new ReqTakeOrder(OrderID, Phone, Start);
                Call<Res> call = restAPI.getRetrofitService().TakeOrder(reqTakeOrder);
                call.enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {
                        if (response.isSuccessful()) {
                            if (response.body().isResult()) {
                                Toast.makeText(context, "주문이 수락되었습니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Res> call, Throwable t) {

                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
