package com.example.vcanteen;

import android.content.Context;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ListView;
import java.util.ArrayList;

public class combinationBaseListAdapter extends ArrayAdapter  {

    ArrayList<food> foodList;
    Context c;
    LayoutInflater inflater;
    private int lastCheckedPos = 0;
    private RadioButton lastChecked = null;

    int p;

    combinationBaseListAdapter(Context context, ArrayList<food> foodList){
        super(context, R.layout.combination_base_listview, foodList);
        this.c = context;
        this.foodList = foodList;
    }

    public class ViewHolder{
        TextView baseComName;
        TextView baseComPrice;
        RadioButton selectedBase;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView==null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.combination_base_listview,parent,false);
        }

        //view holder object
        final combinationBaseListAdapter.ViewHolder holder = new combinationBaseListAdapter.ViewHolder();

        //initalize our view
        holder.baseComName=(TextView) convertView.findViewById(R.id.baseComName);
        holder.baseComPrice=(TextView) convertView.findViewById(R.id.baseComPrice);
        holder.selectedBase=(RadioButton)convertView.findViewById(R.id.selectBase);



        //holder.selectedBase.setChecked(mCheckStates.get(position, false));

        holder.selectedBase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int clickedPos = position;
                RadioButton rd = (RadioButton) v;
                if(rd.isChecked()) {
                    if(lastChecked != null ) {
                        lastChecked.setChecked(false);
                    }

                    lastChecked = rd;
                    lastCheckedPos = clickedPos;
                } else lastChecked = null;
                p=position;
                System.out.println("Position of selected radio button: "+p);
                ((customizeOrderActivity) c).notifyExtraChangeBase();
            }
        });
        //assign data
        holder.baseComName.setText(foodList.get(position).foodName);
        holder.baseComPrice.setText("+ "+foodList.get(position).foodPrice+" ฿");

        return convertView;
    }

    //try new
    public food getSelectedBase(){
        food selected = new food(foodList.get(p).foodId,foodList.get(p).foodName,foodList.get(p).foodPrice,foodList.get(p).foodType);
        return selected;
    }


}
