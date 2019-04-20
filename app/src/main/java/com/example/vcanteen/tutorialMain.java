package com.example.vcanteen;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class tutorialMain extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDot;

    private tutorialSliderAdapter tutorialSliderAdapter;

    private Button getStartedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.dotLayout);
        getStartedBtn = findViewById(R.id.getStarted);

        tutorialSliderAdapter = new tutorialSliderAdapter(this);

        mSlideViewPager.setAdapter(tutorialSliderAdapter);

        addDot(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEmailActivity();
            }
        });

    }

    public void addDot(int position){
        mDot = new TextView[2];
        mDotLayout.removeAllViews();

        for (int i=0; i < mDot.length; i++){
            mDot[i] = new TextView(this);
            mDot[i].setText(Html.fromHtml("&#8226"));
            mDot[i].setTextSize(35);
            mDot[i].setTextColor(Color.parseColor("#EBEBEB"));

            mDotLayout.addView(mDot[i]);
        }

        if(mDot.length>0){
            mDot[position].setTextColor(Color.parseColor("#C4C4C4"));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDot(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public void startEmailActivity(){
        Intent intent = new Intent(this, emailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

