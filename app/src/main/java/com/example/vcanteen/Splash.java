package com.example.vcanteen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.vcanteen.Data.Token;
import com.example.vcanteen.Data.TokenVerification;
import com.facebook.AccessToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Splash extends AppCompatActivity {

    private final int SPLASH_TIME_OUT = 1000;
    private SharedPreferences sharedPref;

    private final String url = "https://vcanteen.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        System.out.println("onCreate Splash JWT "+sharedPref.getString("token", "empty token"));
        System.out.println("onCreate Splash EMAIL "+sharedPref.getString("email", "empty email"));
        System.out.println("onCreate Splash FIREBASE "+sharedPref.getString("firebaseToken", "NO TOKEN"));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        boolean first = sharedPref.getBoolean("firstRun", true);


        Handler handlerFirst = new Handler();
        handlerFirst.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (first) {
                    startTutorial();
                    Log.e("myTag", String.valueOf(sharedPref.getBoolean("firstRun", true)));
                    sharedPref.edit().putBoolean("firstRun", false).commit();
                    Log.e("myTag", String.valueOf(sharedPref.getBoolean("firstRun", true)));
                    Log.e("myTag", "finish startTutorial");
//                    return;
                } else {
                    // POST DATA FOR TOKEN VERIFICATION
                    Log.e("myTag", "start call enqueue");
                    Token token = new Token(sharedPref.getString("email", "empty email"), sharedPref.getString("token", "empty token"));
                    Call<TokenVerification> call = jsonPlaceHolderApi.verifyToken(token);

                    call.enqueue(new Callback<TokenVerification>() {
                        @Override
                        public void onResponse(Call<TokenVerification> call, final Response<TokenVerification> response) {
                            Log.e("myTag", "Pass the onResponse already!!");
                            if (!response.isSuccessful())
                                Toast.makeText(getApplicationContext(), "Error Occured, please try again.", Toast.LENGTH_SHORT);
                            if (response.code() != 200) {
                                //do smth
                                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                            } else {
                                System.out.println(response.body().isExpired());
                                final boolean expired = response.body().isExpired();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        Log.e("myTag", "reach handler postDelayed");
                                        // yourMethod();
                                        if (expired)
                                            startActivity(new Intent(Splash.this, emailActivity.class));
                                        else
                                            startActivity(new Intent(Splash.this, homev2Activity.class));
                                    }
                                }, 500);
                            }

                        }

                        @Override
                        public void onFailure(Call<TokenVerification> call, Throwable t) {

                        }

                    });
                }
                Log.e("myTag", "finished if/else");

            }

        }, 3000);
        Log.e("myTag", "finished first postDelayed");


//        updateWithToken(AccessToken.getCurrentAccessToken());
    }

    // need to be changed to check access token from backendte
    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(Splash.this, vendorListActivity.class);
                    startActivity(i);

                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(Splash.this, emailActivity.class);
                    startActivity(i);

                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }


    public void startTutorial(){
        Intent intent = new Intent(this, tutorialMain.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
