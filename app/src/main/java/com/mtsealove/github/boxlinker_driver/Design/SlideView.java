package com.mtsealove.github.boxlinker_driver.Design;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtsealove.github.boxlinker_driver.LastOrderActivity;
import com.mtsealove.github.boxlinker_driver.R;
import com.mtsealove.github.boxlinker_driver.Restful.ResRecent;
import com.mtsealove.github.boxlinker_driver.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlideView extends LinearLayout {
    String tag = getClass().getSimpleName();
    Context context;
    LinearLayout loginLayout;
    TextView loginTv, inquireTv, nameTv, contentTv, logoutTv;

    public SlideView(Context context) {
        super(context);
        init(context);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_slide, this, false);
        loginLayout = view.findViewById(R.id.loginLayout);
        nameTv = view.findViewById(R.id.nameTv);
        contentTv = view.findViewById(R.id.contentTv);
        loginTv = view.findViewById(R.id.loginTv);

        logoutTv = view.findViewById(R.id.logoutTv);
        logoutTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });
        inquireTv = view.findViewById(R.id.inquireTv);
        inquireTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InquireLog();
            }
        });
        addView(view);
    }


    //로그인 체크
    public void CheckLogin() {
        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        String phone = pref.getString("phone", "");
        String name = pref.getString("name", "");
        //로그인되어 있을 경우
        if (name.length() != 0 && phone.length() != 0) {
            //최근 사용 내역
            GetRecent();
            loginTv.setVisibility(GONE);
            loginLayout.setVisibility(VISIBLE);
            nameTv.setText(name);
        } else {    //로그아웃 되었을 경우
            loginLayout.setVisibility(GONE);
            loginTv.setVisibility(VISIBLE);
        }
    }

    //로그아웃
    private void Logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //데이터 삭제
                SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("name");
                editor.remove("phone");
                editor.commit();
                //액티비티 재시작
                Intent intent = new Intent(context, context.getClass());
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void InquireLog() {
        Intent intent = new Intent(context, LastOrderActivity.class);
        context.startActivity(intent);
    }


    //최근 배송
    private void GetRecent() {
        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        String phone = pref.getString("phone", "");
        RestAPI restAPI=new RestAPI(context);
        Call<ResRecent> call=restAPI.getRetrofitService().GetRecent(phone);
        call.enqueue(new Callback<ResRecent>() {
            @Override
            public void onResponse(Call<ResRecent> call, Response<ResRecent> response) {
               if(response.isSuccessful()) {
                   ResRecent resRecent=response.body();
                   contentTv.setText("최근 배송: "+resRecent.getRecent()+"건");
               }
            }

            @Override
            public void onFailure(Call<ResRecent> call, Throwable t) {

            }
        });
    }
}
