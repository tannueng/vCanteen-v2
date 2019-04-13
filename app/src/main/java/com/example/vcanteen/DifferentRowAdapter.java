package com.example.vcanteen;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DifferentRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<orderListData> mList;
    public DifferentRowAdapter(List<orderListData> list) {
        this.mList = list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row_layout, parent, false);

                return new CookingViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row_done_layout, parent, false);
                return new DoneViewHolder(view);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        orderListData list = mList.get(position);
        if (list != null) {
            switch (list.getType()) {
                case 0:
                    ((CookingViewHolder) holder).orderName.setText(list.getOrderName());
                    ((CookingViewHolder) holder).orderNameExtra.setText(list.getOrderNameExtra());
                    ((CookingViewHolder) holder).vendorName.setText(list.getVendorName());
                    ((CookingViewHolder) holder).orderId.setText("Order ID: "+list.getOrderId());
                    ((CookingViewHolder) holder).orderPrice.setText(list.getOrderPrice()+" ฿");
                    ((CookingViewHolder) holder).orderDate.setText(list.getOrderDate());
                    ((CookingViewHolder) holder).orderStatus.setText(list.getOrderStatus());

                    if (list.getOrderStatus().equals("COLLECTED")) {
                        ((CookingViewHolder) holder).cv.setForeground(null);
                        ((CookingViewHolder) holder).orderStatus.setTextColor(Color.parseColor("#4DC031")); //green
                        ((CookingViewHolder) holder).orderStatus.setText(list.orderStatus);
                    }
                    if (list.getOrderStatus().equals("TIMEOUT")) {
                        ((CookingViewHolder) holder).cv.setForeground(null);
                        ((CookingViewHolder) holder).orderStatus.setTextColor(Color.parseColor("#FF0606"));// red
                        ((CookingViewHolder) holder).orderStatus.setText(list.orderStatus);
                    }
                    if (list.getOrderStatus().equals("CANCELLED")) {
                        ((CookingViewHolder) holder).cv.setForeground(null);
                        ((CookingViewHolder) holder).orderStatus.setTextColor(Color.parseColor("#FF0606"));// red
                        ((CookingViewHolder) holder).orderStatus.setText(list.orderStatus);
                    }
                    if (list.getOrderStatus().equals("COOKING")) {
                        ((CookingViewHolder) holder).cv.setForeground(null);
                        ((CookingViewHolder) holder).orderStatus.setTextColor(Color.parseColor("#757575"));
                    }
                    break;


                case 1:
                    ((DoneViewHolder) holder).orderName.setText(list.getOrderName());
                    ((DoneViewHolder) holder).orderNameExtra.setText(list.getOrderNameExtra());
                    ((DoneViewHolder) holder).orderId.setText("Order ID: "+list.getOrderId());
                    ((DoneViewHolder) holder).orderPrice.setText(list.getOrderPrice()+" ฿");
                    ((DoneViewHolder) holder).vendorName.setText(list.getVendorName());
                    ((DoneViewHolder) holder).orderDate.setText(list.getOrderDate());
                    if (list.getOrderStatus().equals("DONE")) {

                        ((DoneViewHolder) holder).orderStatus.setTextColor(Color.parseColor("#FF0606")); // red
                        ((DoneViewHolder) holder).orderStatus.setText("WAITING FOR PICK UP");
                    }
                    break;
            }



        }
    }
    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }
    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            orderListData object = mList.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }
    public static class CookingViewHolder extends RecyclerView.ViewHolder {

        public CardView getCv() {
            return cv;
        }

        public void setCv(CardView cv) {
            this.cv = cv;
        }

        public TextView getOrderId() {
            return orderId;
        }

        public void setOrderId(TextView orderId) {
            this.orderId = orderId;
        }

        public TextView getOrderPrice() {
            return orderPrice;
        }

        public void setOrderPrice(TextView orderPrice) {
            this.orderPrice = orderPrice;
        }

        public TextView getOrderName() {
            return orderName;
        }

        public void setOrderName(TextView orderName) {
            this.orderName = orderName;
        }

        public TextView getOrderNameExtra() {
            return orderNameExtra;
        }

        public void setOrderNameExtra(TextView orderNameExtra) {
            this.orderNameExtra = orderNameExtra;
        }

        public TextView getVendorName() {
            return vendorName;
        }

        public void setVendorName(TextView vendorName) {
            this.vendorName = vendorName;
        }

        public TextView getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(TextView orderDate) {
            this.orderDate = orderDate;
        }

        public TextView getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(TextView orderStatus) {
            this.orderStatus = orderStatus;
        }

        private CardView cv;
        private TextView orderId;
        private TextView orderPrice;
        private TextView orderName;
        private TextView orderNameExtra;
        private TextView vendorName;
        private TextView orderDate;
        private TextView orderStatus;

        public CookingViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cardView_normal);
            orderId = itemView.findViewById(R.id.orderId);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderName = itemView.findViewById(R.id.orderName);
            orderNameExtra = itemView.findViewById(R.id.orderNameExtra);
            vendorName = itemView.findViewById(R.id.vendorName);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);



        }


    }
    public static class DoneViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        public TextView orderId;
        private TextView orderPrice;
        private TextView orderName;
        private TextView orderNameExtra;
        private TextView vendorName;
        private TextView orderDate;

        public CardView getCv() {
            return cv;
        }

        public void setCv(CardView cv) {
            this.cv = cv;
        }

        public TextView getOrderId() {
            return orderId;
        }

        public void setOrderId(TextView orderId) {
            this.orderId = orderId;
        }

        public TextView getOrderPrice() {
            return orderPrice;
        }

        public void setOrderPrice(TextView orderPrice) {
            this.orderPrice = orderPrice;
        }

        public TextView getOrderName() {
            return orderName;
        }

        public void setOrderName(TextView orderName) {
            this.orderName = orderName;
        }

        public TextView getOrderNameExtra() {
            return orderNameExtra;
        }

        public void setOrderNameExtra(TextView orderNameExtra) {
            this.orderNameExtra = orderNameExtra;
        }

        public TextView getVendorName() {
            return vendorName;
        }

        public void setVendorName(TextView vendorName) {
            this.vendorName = vendorName;
        }

        public TextView getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(TextView orderDate) {
            this.orderDate = orderDate;
        }

        public TextView getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(TextView orderStatus) {
            this.orderStatus = orderStatus;
        }

        private TextView orderStatus;

        public DoneViewHolder(final View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cardView_done);
            orderId = itemView.findViewById(R.id.orderId);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderName = itemView.findViewById(R.id.orderName);
            orderNameExtra = itemView.findViewById(R.id.orderNameExtra);
            vendorName = itemView.findViewById(R.id.vendorName);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(orderStatus.getText().equals("DONE")) {

                    System.out.println("onClick DONE Detected. "+String.valueOf(orderId.getText()));
                    int order = Integer.parseInt(String.valueOf(orderId.getText()).substring(10));
                    progressTabFragment.getSlotInfo(itemView.getContext(),order);
//                    }
                }
            });
        }
    }
}