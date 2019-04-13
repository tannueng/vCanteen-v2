package com.example.vcanteen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.Color;
import java.util.ArrayList;

public class menuListAdapter extends ArrayAdapter {

    int s;
    ArrayList<food> foodList;
    Context c;
    LayoutInflater inflater;

    menuListAdapter(Context context, ArrayList<food> foodList, int s){
        super(context, R.layout.menu_listview , foodList);
        this.c = context;
        this.foodList = foodList;
        this.s = s;
    }

    // Hold our view for each row
    public class ViewHolder{
        TextView menuName;
        TextView menuPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_listview,parent,false);
        }

        //view holder object
        final ViewHolder holder = new ViewHolder();

        //initalize our view
        holder.menuName=(TextView) convertView.findViewById(R.id.menuName);
        holder.menuPrice=(TextView) convertView.findViewById(R.id.menuPrice);

        //assign data
        holder.menuName.setText(foodList.get(position).foodName);
        holder.menuPrice.setText(""+foodList.get(position).foodPrice+" ฿");

        if(position>=s) {
            holder.menuName.setTextColor(Color.parseColor("#C4C4C4"));
            holder.menuPrice.setTextColor(Color.parseColor("#C4C4C4"));
        }
        return convertView;
    }

}
