package com.example.vcanteen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class confirmedOrderListAdapter extends ArrayAdapter {

    orderStack orderStack;
    Context c;
    LayoutInflater inflater;
    String restaurantName;

    confirmedOrderListAdapter(Context context, orderStack orderStack, String restaurantName){
        super(context, R.layout.confirmed_order_listview , orderStack.orderList);
        this.c=context;
        this.orderStack = orderStack;
        this.restaurantName = restaurantName;
    }

    public class ViewHolder{
        TextView orderName;
        TextView orderPrice;
        TextView orderNameExtra;
        TextView restaurantName;
        TextView date;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView==null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.confirmed_order_listview,parent,false);
        }

        //view holder object
        final confirmedOrderListAdapter.ViewHolder holder = new confirmedOrderListAdapter.ViewHolder();

        //initalize our view
        holder.orderName = (TextView) convertView.findViewById(R.id.orderName);
        holder.orderPrice = (TextView) convertView.findViewById(R.id.orderPrice);
        holder.orderNameExtra = (TextView) convertView.findViewById(R.id.orderNameExtra);
        holder.restaurantName = (TextView) convertView.findViewById(R.id.restaurantName);
        holder.date = (TextView) convertView.findViewById(R.id.date);

        //assign data
        holder.orderName.setText(orderStack.orderList.get(position).orderName);
        holder.orderPrice.setText(""+orderStack.orderList.get(position).orderPrice+ " ฿");
        holder.restaurantName.setText(""+restaurantName);

        //date
        DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        holder.date.setText(""+dateformat.format(orderStack.getCreatedAt()));
        System.out.println("Create at "+dateformat.format(orderStack.getCreatedAt()));

        if(orderStack.orderList.get(position).orderNameExtra==null){
            holder.orderNameExtra.setText("");
        }else{
            holder.orderNameExtra.setText(orderStack.orderList.get(position).orderNameExtra);
        }

        return convertView;
    }

}
