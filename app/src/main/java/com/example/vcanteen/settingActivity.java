package com.example.vcanteen;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vcanteen.POJO.availablePaymentMethod;
import com.example.vcanteen.POJO.paymentMethod;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class settingActivity extends AppCompatActivity {
    private LinearLayout tappable_password, tappable_payment;
    private SharedPreferences sharedPref;
    private Button logout;
    private Button reportBug;
    private Dialog logoutWarningDialog;
    private Button confirmLogoutBtn;
    private Button cancelLogoutBtn;
    ArrayList<paymentList> paymentList;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        tappable_password = (LinearLayout)findViewById(R.id.tappable_password);
        tappable_payment = findViewById(R.id.tappable_payment);
        logout = findViewById(R.id.logOutButton);
        reportBug = findViewById(R.id.report_bug_button);

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        String account_type = sharedPref.getString("account_type", "UNKONWN");
        System.out.println("Account Type: " + account_type);
        if (account_type.equals("FACEBOOK"))
            tappable_password.setVisibility(View.GONE);

        //tappable_payment = (LinearLayout)findViewById(R.id.tappable_payment);

        tappable_password.setOnClickListener(v -> openChangePassword());

        tappable_payment.setOnClickListener(v -> {

//            progressDialog = new ProgressDialog(settingActivity.this);
            progressDialog = ProgressDialog.show(settingActivity.this
                    , "",
                    "Loading. Please wait...", true);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://vcanteen.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<paymentMethod> call =  jsonPlaceHolderApi.getPaymentMethod(sharedPref.getInt("customerId",0));


            call.enqueue(new Callback<paymentMethod>() {
                @Override
                public void onResponse(Call<paymentMethod> call, Response<paymentMethod> response) {
                    if(!response.isSuccessful()) {
                        Toast.makeText(settingActivity.this, "CODE: "+response.code(),
                                Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        return;
                    }
                    progressDialog.dismiss();

                    paymentMethod methods;
                    methods = response.body(); //IMPORTANT
                    ArrayList<availablePaymentMethod> lists = methods.availablePaymentMethod; //IMPORTANT
                    Intent intent = new Intent(settingActivity.this, EditPaymentMethodActivity.class); //IMPORTANT
                    for (availablePaymentMethod list :lists){ //just to print
                        System.out.println("payment");
                        System.out.println(list.getCustomerMoneyAccountId()+","+list.getServiceProvider());
                    }

                    intent.putExtra("availablePaymentMethodList", lists ); //IMPORTANT
                    methods.setAvailablePaymentMethod(new ArrayList<>());
//                    progressDialog.dismiss();

                    call.cancel();
                    System.out.println("done retrofit");
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<paymentMethod> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });


            //MOCK DATA
//<<<<<<< Updated upstream
//            paymentMethod methods = new paymentMethod();
//            ArrayList<availablePaymentMethod> availMethods = new ArrayList<>();
//            availMethods.add(new availablePaymentMethod(1,"SCB EASY"));
//            availMethods.add(new availablePaymentMethod(2,"K PLUS"));
//            methods.setAvailablePaymentMethod(availMethods);
//            ArrayList<availablePaymentMethod> lists = methods.availablePaymentMethod;
//
//            for (availablePaymentMethod list :lists){
//                System.out.println("payment");
//                System.out.println(list.getCustomerMoneyAccountId()+","+list.getServiceProvider());
//
////                paymentList.add(new paymentList(list.getCustomerMoneyAccountId(), list.getServiceProvider()));
//            }
//=======
            paymentMethod methods = new paymentMethod();
            ArrayList<availablePaymentMethod> availMethods = new ArrayList<>();
            //availMethods.add
            methods.setAvailablePaymentMethod(availMethods);
            ArrayList<availablePaymentMethod> lists = methods.availablePaymentMethod;

            for (availablePaymentMethod list :lists){
                paymentList.add(new paymentList(list.getCustomerMoneyAccountId(), list.getServiceProvider()));
            }
//>>>>>>> Stashed changes

//            startActivity(new Intent(settingActivity.this, EditPaymentMethodActivity.class));
        });


        reportBug.setOnClickListener(v -> startActivity(new Intent(settingActivity.this, reportBugActivity.class)));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutWarningDialog = new Dialog(settingActivity.this);
                logoutWarningDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                logoutWarningDialog.setContentView(R.layout.logoutwarning);

                confirmLogoutBtn = logoutWarningDialog.findViewById(R.id.logout_confirm_btn);
                cancelLogoutBtn = logoutWarningDialog.findViewById(R.id.cancel_logout_btn);

                confirmLogoutBtn.setEnabled(true);
                cancelLogoutBtn.setEnabled(true);

                confirmLogoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginManager.getInstance().logOut();
                        boolean isSignedIn = FirebaseAuth.getInstance().isSignInWithEmailLink(sharedPref.getString("email", "no email"));
                        System.out.println(sharedPref.getString("email", "no email"));
                        System.out.println(isSignedIn);
                        if (isSignedIn)
                            FirebaseAuth.getInstance().signOut();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    FirebaseInstanceId.getInstance().deleteInstanceId();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        sharedPref.edit().putString("token", "NO TOKEN").commit();
                        sharedPref.edit().putInt("customerId", 0);
                        sharedPref.edit().putString("email","").commit();
                        logoutWarningDialog.cancel();




                        startActivity(new Intent(settingActivity.this, emailActivity.class));
                    }
                });

                cancelLogoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logoutWarningDialog.cancel();
                    }
                });
            logoutWarningDialog.show();
            }
        });

//        tappable_payment.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                openMain();
//            }
//        });
    }

    public void openChangePassword(){
        Intent intent = new Intent(this, changePasswordActivity.class);
        startActivity(intent);
    }
}
