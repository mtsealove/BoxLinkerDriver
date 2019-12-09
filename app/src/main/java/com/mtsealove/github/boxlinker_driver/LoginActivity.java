package com.mtsealove.github.boxlinker_driver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mtsealove.github.boxlinker_driver.Design.SystemUiTuner;
import com.mtsealove.github.boxlinker_driver.Restful.ReqLogin;
import com.mtsealove.github.boxlinker_driver.Restful.ResLogin;
import com.mtsealove.github.boxlinker_driver.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText idEt, pwEt;
    Button loginBtn;
    TextView signUpTv, loginTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        idEt = findViewById(R.id.idEt);
        idEt.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        pwEt = findViewById(R.id.pwEt);
        loginBtn = findViewById(R.id.loginBtn);
        signUpTv = findViewById(R.id.signUpTv);
        loginTv = findViewById(R.id.loginTv);
        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        loginTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SetIpActivity.class);
                startActivity(intent);
                return false;
            }
        });


        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInput();
            }
        });

        SetPhone();
        autoLogin();
    }

    //자동 전화번호
    @SuppressLint("MissingPermission")
    private void SetPhone() {
        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String PhoneNum = telManager.getLine1Number();
        if (PhoneNum.startsWith("+82")) {
            PhoneNum = PhoneNum.replace("+82", "0");
        }
        idEt.setText(PhoneNum);
        idEt.setEnabled(false);
    }

    private void SignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    private void CheckInput() {
        if (idEt.getText().toString().length() == 0) {
            Toast.makeText(this, "전화번호를 입력하세요", Toast.LENGTH_SHORT).show();
        } else if (pwEt.getText().toString().length() == 0) {
            Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            Login();
        }
    }

    private void Login() {
        String phone = idEt.getText().toString();
        final String pw = pwEt.getText().toString();
        String token = FirebaseInstanceId.getInstance().getToken();
        ReqLogin reqLogin = new ReqLogin(phone, pw, token);
        RestAPI restAPI = new RestAPI(this);
        Call<ResLogin> call = restAPI.getRetrofitService().Login(reqLogin);
        call.enqueue(new Callback<ResLogin>() {
            @Override
            public void onResponse(Call<ResLogin> call, Response<ResLogin> response) {
                if (response.isSuccessful()) {
                    ResLogin resLogin = response.body();
                    if (resLogin.getName() != null && resLogin.getPhone() != null) {
                        String phone = resLogin.getPhone();
                        String name = resLogin.getName();
                        SetAccount(phone, name, pw);
                        Toast.makeText(LoginActivity.this, "환영합니다 " + name + "님", Toast.LENGTH_SHORT).show();
                        moveMain();
                    } else {
                        Toast.makeText(LoginActivity.this, "비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResLogin> call, Throwable t) {

            }
        });

    }

    //계정 저장
    private void SetAccount(String phone, String name, String pw) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("phone", phone);
        editor.putString("name", name);
        editor.putString("pw", pw);
        editor.commit();
    }

    private void moveMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void autoLogin() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String pw = pref.getString("pw", "");
        if (pw.length() != 0) {
            pwEt.setText(pw);
            Login();
        }
    }
}
