package com.example.vcanteen;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import io.reactivex.Observable;

public class foodListAdapterForCombi extends ArrayAdapter implements CompoundButton.OnCheckedChangeListener{

    int s;
    ArrayList<food> foodList;
    Context c;
    LayoutInflater inflater;
    SparseBooleanArray mCheckStates;
    Observable check = Observable.just("1");


    foodListAdapterForCombi(Context context, ArrayList<food> foodList, int s) {
        super(context, R.layout.food_listview, foodList);
        this.c = context;
        this.foodList = foodList;
        this.s = s;
        mCheckStates = new SparseBooleanArray(foodList.size());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCheckStates.put((Integer) buttonView.getTag(), isChecked);
        ((customizeOrderActivity) c).notifyExtraChange();

    }

    public class ViewHolder {
        TextView addFoodName;
        TextView addFoodPrice;
        CheckBox chkSelect;
    }

    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);

    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.food_listview, parent, false);
        }

        //view holder object
        final foodListAdapterForCombi.ViewHolder holder = new foodListAdapterForCombi.ViewHolder();

        //initalize our view
        holder.addFoodName = (TextView) convertView.findViewById(R.id.addFoodName);
        holder.addFoodPrice = (TextView) convertView.findViewById(R.id.addFoodPrice);
        holder.chkSelect = (CheckBox) convertView.findViewById(R.id.checkBox2);

        //assign data
        holder.addFoodName.setText(foodList.get(position).foodName);
        holder.addFoodPrice.setText("+ " + foodList.get(position).foodPrice + " ฿");
        holder.chkSelect.setChecked(mCheckStates.get(position, false));
        if (position < s) {
            holder.chkSelect.setOnCheckedChangeListener(this);

        }
        if (position >= s) { //for sold out
            holder.addFoodName.setTextColor(Color.parseColor("#E5E5E5"));
            holder.addFoodPrice.setTextColor(Color.parseColor("#C4C4C4"));
            holder.chkSelect.setEnabled(false);
        }
        holder.chkSelect.setTag(position);
        return convertView;
    }

}
