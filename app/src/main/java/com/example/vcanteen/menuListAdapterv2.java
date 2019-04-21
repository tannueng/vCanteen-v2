package com.example.vcanteen;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class menuListAdapterv2 extends ArrayAdapter {

    int s;
    ArrayList<food> foodList;
    Context c;
    LayoutInflater inflater;
    menuListAdapterv2(Context context, ArrayList<food> foodList, int s){
        super(context, R.layout.menu_listview_v2 , foodList);
        this.c = context;
        this.foodList = foodList;
        this.s = s;
    }

    // Hold our view for each row
    public class ViewHolder{
        TextView menuName;
        TextView menuNameAvailable;
        TextView menuPrice;
        TextView availablity;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_listview_v2,parent,false);
        }

            //view holder object
        final ViewHolder holder = new ViewHolder();

        //initalize our view
        holder.menuNameAvailable=(TextView) convertView.findViewById(R.id.menuNameAvailable);
        holder.menuName=(TextView) convertView.findViewById(R.id.menuName);
        holder.menuPrice=(TextView) convertView.findViewById(R.id.menuPrice);
        holder.availablity=(TextView) convertView.findViewById(R.id.availability);
        holder.menuNameAvailable.setVisibility(View.VISIBLE);
        holder.menuName.setVisibility(View.INVISIBLE);
        holder.availablity.setVisibility(View.INVISIBLE);
        holder.menuNameAvailable.setText(foodList.get(position).foodName);
        holder.menuPrice.setText(""+foodList.get(position).foodPrice+" ฿");
        if(position>=s) {
            holder.menuNameAvailable.setVisibility(View.INVISIBLE);
            holder.menuName.setVisibility(View.VISIBLE);
            holder.availablity.setVisibility(View.VISIBLE);
            holder.menuName.setText(foodList.get(position).foodName);
            holder.menuName.setTextColor(Color.parseColor("#C4C4C4"));
            holder.menuPrice.setTextColor(Color.parseColor("#C4C4C4"));
        }
        return convertView;
    }
}


