package com.mtsealove.github.boxlinker_driver.Design;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.mtsealove.github.boxlinker_driver.R;

public class TitleView extends RelativeLayout {
    ImageView menuIv, logoIv;
    Context context;

    public TitleView(Context context) {
        super(context);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_title, null, false);
        logoIv = view.findViewById(R.id.logoIv);
        menuIv = view.findViewById(R.id.menuIv);
        menuIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDrawer();
            }
        });
        addView(view);
    }

    //메뉴 버튼으로 열기
    private void OpenDrawer() {
        switch (context.getClass().getSimpleName()) {

        }
    }
}
