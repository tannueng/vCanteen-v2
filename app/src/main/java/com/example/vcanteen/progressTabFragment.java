package com.example.vcanteen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcanteen.POJO.orderProgress;
import com.example.vcanteen.POJO.orderStatus;
import com.example.vcanteen.POJO.pickupSlot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class progressTabFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ProgressTabFragment";
    static List<orderListData> data = new ArrayList<>();
    static SwipeRefreshLayout mSwipeRefreshLayout;
    static RecyclerView recyclerView;
    CardView cv;

    static String slotString = "";
    static TextView slotNumber;

    static SharedPreferences sharedPref;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progress_tab_fragment,container,false);
        recyclerView = view.findViewById(R.id.progress_recycler);
        slotNumber = view.findViewById(R.id.pickup_slot_number);
        cv = view.findViewById(R.id.cardView_done);

        sharedPref = this.getActivity().getSharedPreferences("myPref", MODE_PRIVATE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                loadRecyclerViewData(getContext());
            }
        });

         System.out.println("object added..");

         View view2 = inflater.inflate(R.layout.popup_confirm_pickup,container,false);
//         CardView cv = (CardView) view.findViewById(R.id.cardView);
        Button btn = (Button) view2.findViewById(R.id.confirmPickup_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                System.out.println("detected onclick..");
            }
        });



        return view;
    }



    @Override
    public void onRefresh() {
        // Fetching data from server
        loadRecyclerViewData(getContext());
    }



    public static void loadRecyclerViewData(final Context context) {

        data = new ArrayList<>();
        System.out.println("Loading Progress Data");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        Call<List<orderProgress>> call =  jsonPlaceHolderApi.getProgress(sharedPref.getInt("customerId",0));

        call.enqueue(new Callback<List<orderProgress>>() {
            @Override
            public void onResponse(Call<List<orderProgress>> call, Response<List<orderProgress>> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(context, "CODE: "+response.code(),
                            Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }

                List<orderProgress> posts = response.body();
                System.out.println(posts.toString());

                for (orderProgress post : posts) {
                    if(String.valueOf(post.getOrderStatus()).equals("DONE")) {
                        data.add(new orderListData(Integer.toString(post.getOrderId()),Integer.toString(post.getOrderPrice()),post.getOrderName(),post.getOrderNameExtra(), post.getRestaurantName(), post.getCreatedAt(), post.getOrderStatus(),1));
                    } else {
                        data.add(new orderListData(Integer.toString(post.getOrderId()),Integer.toString(post.getOrderPrice()),post.getOrderName(),post.getOrderNameExtra(), post.getRestaurantName(), post.getCreatedAt(), post.getOrderStatus(),0));
                    }
                }
                DifferentRowAdapter adapter = new DifferentRowAdapter(data);;
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                mSwipeRefreshLayout.setRefreshing(false);


            }

            @Override
            public void onFailure(Call<List<orderProgress>> call, Throwable t) {
                Toast.makeText(context, "ERROR: "+t.getMessage(),
                        Toast.LENGTH_LONG).show();
                System.out.println("some error");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public static void getSlotInfo(final Context context, final int orderId) {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("http://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit2.create(JsonPlaceHolderApi.class);
//        System.out.println("My order id is "+ DifferentRowAdapter.DoneViewHolder.orderId.getText().toString().substring(10));
//        int i = Integer.parseInt(holder.orderId.getText().toString().substring(10));
        Call<pickupSlot> call =  jsonPlaceHolderApi.getPickupSlot(orderId);

        call.enqueue(new Callback<pickupSlot>() {
            @Override
            public void onResponse(Call<pickupSlot> call, Response<pickupSlot> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(context, "CODE: "+response.code(),
                            Toast.LENGTH_LONG).show();
                    System.out.println("PROG onResponse getslot unsuccessful");
                    return;
                }


                pickupSlot slot = response.body();
                System.out.println("SLOT A = "+response.body());
                System.out.println("SLOT = "+Integer.toString(slot.getPickupSlot()));
                slotString = Integer.toString(slot.getPickupSlot());
//                            holder.pickupSlot.setText("321"); //slot.getPickupSlot()

                showConfirmDialog(context,orderId,slot);


            }

            @Override
            public void onFailure(Call<pickupSlot> call, Throwable t) {
                Toast.makeText(context, "ERROR: "+t.getMessage(),
                        Toast.LENGTH_LONG).show();
                System.out.println("PROG some error");
            }
        });
    }
    static Dialog dialog;
    private static void showConfirmDialog(final Context context, final int orderId, final pickupSlot slot) {

        //display popup confirm pickup

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup_confirm_pickup);
        slotNumber =(TextView)dialog.findViewById(R.id.pickup_slot_number);
        slotNumber.setText(Integer.toString(slot.getPickupSlot()));
        dialog.setCancelable(true);

        (dialog.findViewById(R.id.dismiss_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        slotNumber.setText("");
                        System.out.println("current slot number = "+slotNumber.getText());
                    }
                });

        (dialog.findViewById(R.id.confirmPickup_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();


                        System.out.println("My orderID is " + orderId);
                        putOrderSlot(context, orderId);



                    }
                });

        dialog.show();
    }

    private static void putOrderSlot(final Context context, int orderId) {
        slotNumber.setText("");
        //Send endpoint putOrderStatus

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//                                System.out.println("My order id is "+holder.orderId.getText().toString().substring(10));

//                                            orderStatus orderStatus2 = new orderStatus("COLLECTED");
//                                            Call<orderStatus> call = jsonPlaceHolderApi.putOrderStatus(orderStatus2);


//                                            Task task = new Task(1, "my task title");
//                                            Call<Task> call = taskService.createTask(task);
//                                            call.enqueue(new Callback<orderStatus>() {});


//                                int i = Integer.parseInt(holder.orderId.getText().toString().substring(10));
        loadRecyclerViewData(context);
        System.out.println("entered putOrderSlot");
        System.out.println("Received Order ID : "+orderId);
        Call<orderStatus> call3 =  jsonPlaceHolderApi.putOrderStatus(orderId);
        call3.enqueue(new Callback<orderStatus>() {

            @Override
            public void onResponse(Call<orderStatus> call3, Response<orderStatus> response) {
                if(!response.isSuccessful()) {
                                            Toast.makeText(context, "CODE: "+response.code(),
                                                    Toast.LENGTH_LONG).show();
                    System.out.println("PICKUP onResponse collected unsuccessful");
//                                            System.out.println("Current - orderStatus : "+String.valueOf(.orderStatus.getText()));
                    return;
                }



//                                                    refresh();
//                                        System.out.println("Current + orderStatus : "+String.valueOf(holder.orderStatus));
//                                                    orderListData delete = holder.getAdapterPosition();
//                                                    list.remove(getAdapterPosition());
//                                                    remove();

            }

            @Override
            public void onFailure(Call<orderStatus> call3, Throwable t) {
                Toast.makeText(context, "onFailure",
                        Toast.LENGTH_LONG).show();
            }
        });
        System.out.println("ENDD");
    }

}
