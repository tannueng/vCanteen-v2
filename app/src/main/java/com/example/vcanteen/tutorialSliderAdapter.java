package com.example.vcanteen;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.load.engine.Resource;

public class tutorialSliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public tutorialSliderAdapter(Context context){
        this.context = context;
    }

    public int[] slide_header = {
//            Resources.getSystem().getString(R.string.tutorial_one_title),
//            Resources.getSystem().getString(R.string.tutorial_two_title)
            R.string.tutorial_one_title,
            R.string.tutorial_two_title
    };

    public int[] slide_desc = {
//            Resources.getSystem().getString(R.string.tutorial_one_desc),
//            Resources.getSystem().getString(R.string.tutorial_two_desc),
            R.string.tutorial_one_desc,
            R.string.tutorial_two_desc
    };

    public int[] slide_image = {
            R.drawable.ic_tutorialone,
            R.drawable.ic_tutorialtwo
    };


    @Override
    public int getCount() {
        return slide_header.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.tutorial_layout, container, false);

        TextView slideHeader = view.findViewById(R.id.tutorialTitle);
        TextView slideDesc = view.findViewById(R.id.tutorialDesc);
        ImageView slideImage = view.findViewById(R.id.tutorialPic);

        slideHeader.setText(slide_header[position]);
        slideDesc.setText(slide_desc[position]);
        slideImage.setImageResource(slide_image[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
