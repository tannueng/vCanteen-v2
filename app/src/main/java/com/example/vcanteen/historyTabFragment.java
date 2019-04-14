package com.example.vcanteen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vcanteen.POJO.orderHistory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class historyTabFragment extends Fragment {
    private static final String TAG = "HistoryTabFragment";
    static List<orderListData> data = new ArrayList<>();
    static SwipeRefreshLayout mSwipeRefreshLayout;
    static RecyclerView recyclerView ;

    static SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_tab_fragment,container,false);
        recyclerView = view.findViewById(R.id.history_recycler);
        sharedPref = this.getActivity().getSharedPreferences("myPref", MODE_PRIVATE);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Fetching data from server
                loadRecyclerViewData(getContext());
            }
        });
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
        return view;
    }

    private static void loadRecyclerViewData(final Context context) {
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://www.json-generator.com/api/json/get/")
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<orderHistory>> call =  jsonPlaceHolderApi.getHistory(sharedPref.getInt("customerId",0));

        call.enqueue(new Callback<List<orderHistory>>() {
            @Override
            public void onResponse(Call<List<orderHistory>> call, Response<List<orderHistory>> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(context, "CODE: "+response.code(),
                            Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
                data = new ArrayList<>();
                List<orderHistory> posts = response.body();
//                showData(response.body());

                for (orderHistory post : posts) {
                    data.add(new orderListData(Integer.toString(post.getOrderId()),Integer.toString(post.getOrderPrice()),post.getOrderName(),post.getOrderNameExtra(), post.getRestaurantName(), post.getCreatedAt(), post.getOrderStatus(),0));

                }
                DifferentRowAdapter adapter = new DifferentRowAdapter(data);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<orderHistory>> call, Throwable t) {
                Toast.makeText(context, "ERROR: "+t.getMessage(),
                        Toast.LENGTH_LONG).show();
                System.out.println("some error");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }



}
