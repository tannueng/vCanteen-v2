package com.example.vcanteen;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcanteen.POJO.orderProgress;
import com.example.vcanteen.POJO.orderStatus;
import com.example.vcanteen.POJO.pickupSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.POWER_SERVICE;

public class progressTabFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ProgressTabFragment";
    static List<orderListData> data = new ArrayList<>();
    static SwipeRefreshLayout mSwipeRefreshLayout;
    static RecyclerView recyclerView;
    CardView cv;

    static String slotString = "";
    static TextView slotNumber;
    public static RatingBar ratingBar;
    static TextView descriptionText;
    static ProgressDialog progressDialog;

    private static final Pattern TEXT_PATTERN =
            Pattern.compile("^[a-zA-Z0-9. ,_\\-*‘\"#&()$@!?]+$");  // Text Constraint

    static SharedPreferences sharedPref;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progress_tab_fragment,container,false);
        recyclerView = view.findViewById(R.id.progress_recycler);
        slotNumber = view.findViewById(R.id.pickup_slot_number);
        cv = view.findViewById(R.id.cardView_done);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mHandler, new IntentFilter("com.example.vcanteen_FCM-MESSAGE"));


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
                mSwipeRefreshLayout.setRefreshing(false);
                List<orderProgress> posts = response.body();
                if(posts.isEmpty()) {
                    Toast.makeText(context, "In-Progress is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (orderProgress post : posts) {
                    if(String.valueOf(post.getOrderStatus()).equals("DONE")) {
                        data.add(new orderListData(Integer.toString(post.getOrderId()),Integer.toString(post.getOrderPrice()),post.getOrderName(),post.getOrderNameExtra(), post.getRestaurantName(), post.getCreatedAt(), post.getOrderStatus(),1,0));
                    } else {
                        data.add(new orderListData(Integer.toString(post.getOrderId()),Integer.toString(post.getOrderPrice()),post.getOrderName(),post.getOrderNameExtra(), post.getRestaurantName(), post.getCreatedAt(), post.getOrderStatus(),0,post.getOrderEstimatedTime()));
                    }
                }
                DifferentRowAdapter adapter = new DifferentRowAdapter(data);;
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                System.out.println("Done Loading");


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

    public static void getSlotInfo(final Context context, final int orderId, final String vendorName, final String orderName,@Nullable String orderNameExtra) {
//        progressDialog = new ProgressDialog(context);
        progressDialog = ProgressDialog.show(context, "","Loading. Please wait...", true);

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit2.create(JsonPlaceHolderApi.class);
        Call<pickupSlot> call =  jsonPlaceHolderApi.getPickupSlot(orderId);

        call.enqueue(new Callback<pickupSlot>() {
            @Override
            public void onResponse(Call<pickupSlot> call, Response<pickupSlot> response) {
                if(!response.isSuccessful()) {
                    if(response.code()==404) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "The order "+orderId+" has expired. ",
                                Toast.LENGTH_LONG).show();
                        dialog = new Dialog(context);
                        dialog.setContentView(R.layout.popup_order_find_slot_over_timeout);
                        dialog.setCancelable(true);
                        TextView overtime = dialog.findViewById(R.id.popup_order_overtime_title);
                        overtime.setText("The order "+orderId+" has expired");
                        Button dismiss_btn = dialog.findViewById(R.id.dismiss_btn);
                        dismiss_btn.setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                        dialog.show();
                        loadRecyclerViewData(context);
                        DifferentRowAdapter adapter = new DifferentRowAdapter(data);;
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    }

                    System.out.println("PROG onResponse getslot unsuccessful");
                    return;
                }


                progressDialog.dismiss();

                pickupSlot slot = response.body();
                System.out.println("SLOT A = "+response.body());
                System.out.println("SLOT = "+Integer.toString(slot.getPickupSlot()));
                slotString = Integer.toString(slot.getPickupSlot());
//                            holder.pickupSlot.setText("321"); //slot.getPickupSlot()

                showConfirmDialog(context,orderId,slot, vendorName, orderName, orderNameExtra);


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
    private static void showConfirmDialog(final Context context, final int orderId, final pickupSlot slot, final String vendorName, final String orderName, final String orderNameExtra) {

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
                        showReviewDialog(context, orderId,vendorName, orderName, orderNameExtra );
                    }
                });

        dialog.show();
    }

    private static void putOrderSlot(final Context context, int orderId) {
        slotNumber.setText("");
        //Send endpoint putOrderStatus

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        loadRecyclerViewData(context);
        System.out.println("entered putOrderSlot");
        System.out.println("Received Order ID : "+orderId);
        Call<orderStatus> call3 =  jsonPlaceHolderApi.putOrderStatus(orderId);
        call3.enqueue(new Callback<orderStatus>() {

            @Override
            public void onResponse(Call<orderStatus> call3, Response<orderStatus> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(context, "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                    System.out.println("PICKUP onResponse collected unsuccessful");
//                                            System.out.println("Current - orderStatus : "+String.valueOf(.orderStatus.getText()));
                    return;
                }
                System.out.println("start reloading page");
                loadRecyclerViewData(context);
                historyTabFragment.loadRecyclerViewData(context);

                //Show review dialog
//                showReviewDialog(context, orderId);
            }

            @Override
            public void onFailure(Call<orderStatus> call3, Throwable t) {
                Toast.makeText(context, "onFailure",
                        Toast.LENGTH_LONG).show();
            }
        });
        System.out.println("END");
    }

    public static void showReviewDialog(final Context context, int orderId, String vendorName, String orderName1, @Nullable String orderNameExtra1) {
        //display popup confirm pickup
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup_vendor_rating);

        TextView restaurantName = dialog.findViewById(R.id.restaurantName);
        TextView orderName = dialog.findViewById(R.id.orderName);
        TextView orderNameExtra = dialog.findViewById(R.id.orderNameExtra);
        restaurantName.setText(vendorName);
        orderName.setText(orderName1);
        orderNameExtra.setText(orderNameExtra1);

        ratingBar = dialog.findViewById(R.id.ratingBar);
        descriptionText = dialog.findViewById(R.id.description_text);
        EditText reviewBox = dialog.findViewById(R.id.reviewBox);
        TextView counter = dialog.findViewById(R.id.counter);
        TextView inline = dialog.findViewById(R.id.inlineError);

        AtomicReference<Double> score = new AtomicReference<>(0.0);
        double score2 = 0.0;
        dialog.setCancelable(true);
        descriptionText.setText("Tap on the stars to rate!");
        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if(String.valueOf(rating).equals("0.0")) descriptionText.setText("\"Tap on the stars to rate!\"");
            if(String.valueOf(rating).equals("1.0")) descriptionText.setText("\"Is it really that bad?\"");
            if(String.valueOf(rating).equals("2.0")) descriptionText.setText("\"Not so bad for a meal!\"");
            if(String.valueOf(rating).equals("3.0")) descriptionText.setText("\"Good taste. Worth a try!\"");
            if(String.valueOf(rating).equals("4.0")) descriptionText.setText("\"Great taste. Would recommend!\"");
            if(String.valueOf(rating).equals("5.0")) descriptionText.setText("\"Awesome, I can eat this everyday!\"");
            score.set(Double.parseDouble(new Float(rating).toString()));
//            score2 = (double)rating;
        });

        reviewBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                counter.setText(s.toString().length()+"/300");
                if(reviewBox.getText().toString().isEmpty()) {
                    inline.setVisibility(View.INVISIBLE);
                    return;
                }
                if(!TEXT_PATTERN.matcher(reviewBox.getText().toString()).matches()) {
                    System.out.println("print:"+reviewBox.getText().toString());
                    inline.setText("Must be letter, number or these character . , _ - * ‘ \" # & () $ @ ! ?");
                    inline.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });


        (dialog.findViewById(R.id.close_x))
                .setOnClickListener(v -> {
                    dialog.dismiss();
                    DifferentRowAdapter adapter = new DifferentRowAdapter(data);;
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                });

        (dialog.findViewById(R.id.sendButton))
                .setOnClickListener(v -> {
                    //check constraint
                    progressDialog = new ProgressDialog(context);
                    progressDialog = ProgressDialog.show(context, "","Loading. Please wait...", true);

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://vcanteen.herokuapp.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                    Call<Void> call = jsonPlaceHolderApi.postVendorReview(sharedPref.getInt("customerId", 0),score, orderId,reviewBox.getText().toString().trim() );
//                    Call<Void> call = jsonPlaceHolderApi.postVendorReview(1,1.0, 2,"gfdhfhg" );
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(context, "CODE: "+response.code(),Toast.LENGTH_LONG).show();
                                System.out.println("error onResponse post review");
                                return;

                            }
                            System.out.println("-------start");
                            Toast.makeText(context, "Review Submitted", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            progressDialog.dismiss();
                            System.out.println("-------end");

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            System.out.println("review fail");
                            dialog.dismiss();
                        }
                    });

                    Toast.makeText(context, "Review Submitted", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    dialog.dismiss();
                    /*dialog.setOnDismissListener(new PopupWindow.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    })*/
                    DifferentRowAdapter adapter = new DifferentRowAdapter(data);;
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                });

        dialog.findViewById(R.id.relativeLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                return true;
            }
        });
        dialog.show();
    }

    private BroadcastReceiver mHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadRecyclerViewData(getContext());
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mHandler);
    }
}
