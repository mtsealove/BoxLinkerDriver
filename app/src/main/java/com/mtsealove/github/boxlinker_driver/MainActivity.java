package com.mtsealove.github.boxlinker_driver;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.boxlinker_driver.Design.SystemUiTuner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();
    }
}
