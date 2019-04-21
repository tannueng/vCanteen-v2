package com.example.vcanteen;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcanteen.POJO.currentDensity;
import com.example.vcanteen.POJO.currentDensityAll;
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
    TextView eightAMData, nineAMData, tenAMData, elevenAMData, twelvePMData, onePMData, twoPMData, threePMData, fourPMData;
    TextView lastestTimeStampText, currentCanteenDensityValue;
//    currentDensityAll crowd;
    currentDensityAll preloadCrowdData;
    ArrayList<hourlyCrowdStat> crowd = new ArrayList<>();
    ProgressDialog progressDialog;
    ArrayList<hourlyCrowdStat> hourlyData;
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

        lastestTimeStampText = findViewById(R.id.lastestTimeStampText);
        currentCanteenDensityValue = findViewById(R.id.currentCanteenDensityValue);

        preloadCrowdData = getIntent().getParcelableExtra( "preloadCrowdData"); //IMPORTANT


        hourlyData = getIntent().getParcelableArrayListExtra("hourlyData");
//        hourlyData = new ArrayList<>();
//        hourlyData = getIntent().getParcelableExtra( "hourlyData"); //IMPORTANT

//        crowd.add(new hourlyCrowdStat("08:00",getIntent().getIntExtra("8",150)));
//        crowd.add(new hourlyCrowdStat("09:00",getIntent().getIntExtra("9",150)));
//        crowd.add(new hourlyCrowdStat("10:00",getIntent().getIntExtra("10",150)));
//        crowd.add(new hourlyCrowdStat("11:00",getIntent().getIntExtra("11",150)));
//        crowd.add(new hourlyCrowdStat("12:00",getIntent().getIntExtra("12",150)));
//        crowd.add(new hourlyCrowdStat("13:00",getIntent().getIntExtra("13",150)));
//        crowd.add(new hourlyCrowdStat("14:00",getIntent().getIntExtra("14",150)));
//        crowd.add(new hourlyCrowdStat("15:00",getIntent().getIntExtra("15",150)));
//        crowd.add(new hourlyCrowdStat("16:00",getIntent().getIntExtra("16",150)));
//        System.out.println("density "+preloadCrowdData);
        lastestTimeStampText.setText("Latest Data : Today "+preloadCrowdData.getLatestTime());
        currentCanteenDensityValue.setText(Integer.toString(preloadCrowdData.getPercentDensity())+"%");
//        System.out.println(hourlyData.get(0).getCrowdStat());

        crowd = hourlyData;
//        System.out.println(preloadCrowdData.hourlyCrowdStat.toArray().toString());
//        System.out.println("hi "+preloadCrowdData.hourlyCrowdStat.get(0).getCrowdStat());
        for (hourlyCrowdStat stat : crowd) {
            if(stat.getHourOfDay().equals("08:00")) {
                eightAMData.setText(Integer.toString(stat.getCrowdStat())+"%");
            }
            if(stat.getHourOfDay().equals("09:00")) {
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
            if(stat.getHourOfDay().equals("13:00")) {
                onePMData.setText(Integer.toString(stat.getCrowdStat())+"%");
            }
            if(stat.getHourOfDay().equals("14:00")) {
                twoPMData.setText(Integer.toString(stat.getCrowdStat())+"%");
            }
            if(stat.getHourOfDay().equals("15:00")) {
                threePMData.setText(Integer.toString(stat.getCrowdStat())+"%");
            }
            if(stat.getHourOfDay().equals("16:00")) {
                fourPMData.setText(Integer.toString(stat.getCrowdStat())+"%");
            }
        }






////////REFRESH BUTTON LOGIC//////////
        refreshButton = findViewById(R.id.refreshButton);
        lastestTimeStampText = findViewById(R.id.lastestTimeStampText);
        currentCanteenDensityValue = findViewById(R.id.currentCanteenDensityValue);
        refreshButton.setOnClickListener(v -> {
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

//            lastestTimeStampText.setText("Latest Data : Today "+"12:08");
//            currentCanteenDensityValue.setText("50%");

        });



////////SHOW DATA BUTTON LOGIC//////////
        showDataButton = findViewById(R.id.showDataButton);
        showDataButton.setOnClickListener(v -> {

            System.out.println(String.valueOf(daySpinner.getSelectedItem()));

//            ArrayList<hourlyCrowdStat> crowd;
//            crowd = response.body(); //real data

            //MOCK TIMING
//            crowd = new ArrayList<>();
//            crowd.add(new hourlyCrowdStat("8:00",50));
//            crowd.add(new hourlyCrowdStat("9:00",60));
//            crowd.add(new hourlyCrowdStat("10:00",70));
//            crowd.add(new hourlyCrowdStat("11:00",80));
//            crowd.add(new hourlyCrowdStat("12:00",90));

            // Set to the xml display
//            for (hourlyCrowdStat stat : crowd) {
//                if(stat.getHourOfDay().equals("08:00")) {
//                    eightAMData.setText(Integer.toString(stat.getCrowdStat())+"%");
//                }
//                if(stat.getHourOfDay().equals("09:00")) {
//                    nineAMData.setText(Integer.toString(stat.getCrowdStat())+"%");
//                }
//                if(stat.getHourOfDay().equals("10:00")) {
//                    tenAMData.setText(Integer.toString(stat.getCrowdStat())+"%");
//                }
//                if(stat.getHourOfDay().equals("11:00")) {
//                    elevenAMData.setText(Integer.toString(stat.getCrowdStat())+"%");
//                }
//                if(stat.getHourOfDay().equals("12:00")) {
//                    twelvePMData.setText(Integer.toString(stat.getCrowdStat())+"%");
//                }
//                if(stat.getHourOfDay().equals("13:00")) {
//                    onePMData.setText(Integer.toString(stat.getCrowdStat())+"%");
//                }
//                if(stat.getHourOfDay().equals("14:00")) {
//                    twoPMData.setText(Integer.toString(stat.getCrowdStat())+"%");
//                }
//                if(stat.getHourOfDay().equals("15:00")) {
//                    threePMData.setText(Integer.toString(stat.getCrowdStat())+"%");
//                }
//                if(stat.getHourOfDay().equals("16:00")) {
//                    fourPMData.setText(Integer.toString(stat.getCrowdStat())+"%");
//                }
//            }
            progressDialog = new ProgressDialog(crowdEstimationActivity.this);
            progressDialog = ProgressDialog.show(crowdEstimationActivity.this, "",
                    "Loading. Please wait...", true);

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
                        progressDialog.dismiss();
                        return;
                    }
                    crowd = response.body();

                    for (hourlyCrowdStat stat : crowd) {
                        if(stat.getHourOfDay().equals("08:00")) {
                            eightAMData.setText(Integer.toString(stat.getCrowdStat())+"%");
                        }
                        if(stat.getHourOfDay().equals("09:00")) {
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
                        if(stat.getHourOfDay().equals("13:00")) {
                            onePMData.setText(Integer.toString(stat.getCrowdStat())+"%");
                        }
                        if(stat.getHourOfDay().equals("14:00")) {
                            twoPMData.setText(Integer.toString(stat.getCrowdStat())+"%");
                        }
                        if(stat.getHourOfDay().equals("15:00")) {
                            threePMData.setText(Integer.toString(stat.getCrowdStat())+"%");
                        }
                        if(stat.getHourOfDay().equals("16:00")) {
                            fourPMData.setText(Integer.toString(stat.getCrowdStat())+"%");
                        }
                    }
                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(Call<ArrayList<hourlyCrowdStat>> call, Throwable t) {

                    progressDialog.dismiss();
                }
            });
        });

    }

}
