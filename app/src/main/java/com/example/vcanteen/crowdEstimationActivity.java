package com.example.vcanteen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcanteen.POJO.currentDensity;
import com.example.vcanteen.POJO.hourlyCrowdStat;

import java.nio.file.Path;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class crowdEstimationActivity extends AppCompatActivity {

    Spinner daySpinner;
    Button showDataButton, refreshButton;
    TextView eightAMData, nineAMData, tenAMData, elevenAMData, twelvePMData, onePMData, twoPMData, threePMData, fourPMData, fivePMData;
    TextView lastestTimeStampText, currentCanteenDensityValue;
    ArrayList<hourlyCrowdStat> crowd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowd_estimation);

        daySpinner = findViewById(R.id.daySpinner);
        daySpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        eightAMData = findViewById(R.id.eightAMData);
        nineAMData = findViewById(R.id.nineAMData);
        tenAMData = findViewById(R.id.tenAMData);
        elevenAMData = findViewById(R.id.elevenAMData);
        twelvePMData = findViewById(R.id.twelvePMData);
        onePMData = findViewById(R.id.onePMData);
        twoPMData = findViewById(R.id.twoPMData);
        threePMData = findViewById(R.id.threePMData);
        fourPMData = findViewById(R.id.fourPMData);
        fivePMData = findViewById(R.id.fivePMData);


////////REFRESH BUTTON LOGIC//////////
        refreshButton = findViewById(R.id.refreshButton);
        lastestTimeStampText = findViewById(R.id.lastestTimeStampText);
        currentCanteenDensityValue = findViewById(R.id.currentCanteenDensityValue);
        refreshButton.setOnClickListener(v -> {
            //TODO RETROFIT HERE
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://vcanteen.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<currentDensity> call = jsonPlaceHolderApi.getCurrentDensity();

            call.enqueue(new Callback<currentDensity>() {
                @Override
                public void onResponse(Call<currentDensity> call, Response<currentDensity> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(crowdEstimationActivity.this, "CODE: "+response.code(),Toast.LENGTH_LONG).show();
                        return;
                    }
                    currentDensity currentDensity;
                    currentDensity = response.body();
                    lastestTimeStampText.setText("Latest Data : Today "+currentDensity.getLatestTime());
                    currentCanteenDensityValue.setText(Integer.toString(currentDensity.getPercentDensity())+"%");
                }

                @Override
                public void onFailure(Call<currentDensity> call, Throwable t) {

                }
            });
            lastestTimeStampText.setText("Latest Data : Today "+"12:08");
            currentCanteenDensityValue.setText("50%");
        });



////////SHOW DATA BUTTON LOGIC//////////
        showDataButton = findViewById(R.id.showDataButton);
        showDataButton.setOnClickListener(v -> {

            System.out.println(String.valueOf(daySpinner.getSelectedItem()));

//            ArrayList<hourlyCrowdStat> crowd;
//            crowd = response.body(); //real data

            //MOCK TIMING
            crowd = new ArrayList<>();
            crowd.add(new hourlyCrowdStat("8:00",50));
            crowd.add(new hourlyCrowdStat("9:00",60));
            crowd.add(new hourlyCrowdStat("10:00",70));
            crowd.add(new hourlyCrowdStat("11:00",80));
            crowd.add(new hourlyCrowdStat("12:00",90));

            // Set to the xml display
            for (hourlyCrowdStat stat : crowd) {
                if(stat.getHourOfDay().equals("8:00")) {
                    eightAMData.setText(Integer.toString(stat.getCrowdStat())+"%");
                }
                if(stat.getHourOfDay().equals("9:00")) {
                    nineAMData.setText(Integer.toString(stat.getCrowdStat())+"%");
                }
                if(stat.getHourOfDay().equals("10:00")) {
                    tenAMData.setText(Integer.toString(stat.getCrowdStat())+"%");
                }
                if(stat.getHourOfDay().equals("11:00")) {
                    elevenAMData.setText(Integer.toString(stat.getCrowdStat())+"%");
                }
                if(stat.getHourOfDay().equals("12:00")) {
                    twelvePMData.setText(Integer.toString(stat.getCrowdStat())+"%");
                }
                //TODO duplicate to the whole
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://vcanteen.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<ArrayList<hourlyCrowdStat>> call = jsonPlaceHolderApi.getDailyStat(String.valueOf(daySpinner.getSelectedItem()).toUpperCase());

            call.enqueue(new Callback<ArrayList<hourlyCrowdStat>>() {
                @Override
                public void onResponse(Call<ArrayList<hourlyCrowdStat>> call, Response<ArrayList<hourlyCrowdStat>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(crowdEstimationActivity.this, "CODE: "+response.code(),Toast.LENGTH_LONG).show();
                        return;
                    }

                    //TODO assigning textview from response

                }

                @Override
                public void onFailure(Call<ArrayList<hourlyCrowdStat>> call, Throwable t) {

                }
            });
        });

    }

}
