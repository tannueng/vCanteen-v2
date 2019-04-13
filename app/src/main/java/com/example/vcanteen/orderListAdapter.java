package com.example.vcanteen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.widget.TextView;

import static android.content.Intent.getIntent;

public class orderListAdapter extends ArrayAdapter {

    //declarations
    orderStack orderStack;

    Context c;
    LayoutInflater inflater;
    int orderposition;
    //orderListAdapter orderListAdapter;

    orderListAdapter(Context context, orderStack orderStack){
        super(context, R.layout.order_listview , orderStack.orderList);
        this.c=context;
        this.orderStack = orderStack;
//        this.a=a;
//        this.b=b;
//        this.d=d;
    }

    public class ViewHolder{
        TextView orderName;
        TextView orderPrice;
        TextView orderNameExtra;
        TextView removeTextButton;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView==null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_listview,parent,false);
        }

        //view holder object
        final orderListAdapter.ViewHolder holder = new orderListAdapter.ViewHolder();

        //initalize our view
        holder.orderName=(TextView) convertView.findViewById(R.id.orderName);
        holder.orderPrice=(TextView) convertView.findViewById(R.id.orderPrice);
        holder.orderNameExtra = (TextView) convertView.findViewById(R.id.orderNameExtra);
        holder.removeTextButton = (TextView) convertView.findViewById(R.id.removeTextButton);

        //assign data
        holder.orderName.setText(orderStack.orderList.get(position).orderName);
        holder.orderPrice.setText(""+orderStack.orderList.get(position).orderPrice+ " ฿");

        if(orderStack.orderList.get(position).orderNameExtra==null){
            holder.orderNameExtra.setText("");
        }else{
            holder.orderNameExtra.setText(orderStack.orderList.get(position).orderNameExtra);
        }


        holder.removeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderposition = position;
                ((cartActivity) c).updateOrder();
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public int passPosition(){
        return orderposition;
    }

}

