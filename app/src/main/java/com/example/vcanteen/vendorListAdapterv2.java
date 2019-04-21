package com.example.vcanteen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class vendorListAdapterv2 extends ArrayAdapter<vendorList> {
    RequestOptions option = new RequestOptions().centerCrop();
    String vendorStatus;

    Context context;
    List<vendorList> vendorLists;

    public vendorListAdapterv2(Context context, List<vendorList> vendorLists) {
        super(context, R.layout.vendor_listview_v2, vendorLists);
        this.context = context;
        this.vendorLists = vendorLists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        vendorList item = vendorLists.get(position);
        vendorStatus = item.getVendorStatus();
        if(vendorStatus.equals("CLOSED")){
            convertView = LayoutInflater.from(context).inflate(R.layout.vendor_listview_disabled_v2, parent, false);
            TextView vendorName = (TextView) convertView.findViewById(R.id.vendorName);
            vendorName.setText(item.getRestaurantName());
            convertView.setEnabled(false);
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.vendor_listview_v2, parent, false);
            TextView vendorName = (TextView) convertView.findViewById(R.id.vendorName);
            ImageView vendorImage = convertView.findViewById(R.id.vendorImage);
            TextView estimate_time = (TextView) convertView.findViewById(R.id.estimate_time);
            vendorName.setText(item.getRestaurantName());
            estimate_time.setText(item.getQueuingTime() + " mins");
            Glide.with(context).load(item.getVendorImage()).apply(option).into(vendorImage);
            vendorImage.setClipToOutline(true);
        }
        return convertView;
    }


}


