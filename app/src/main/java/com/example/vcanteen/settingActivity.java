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

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

public class settingActivity extends AppCompatActivity {
    private LinearLayout tappable_password, tappable_payment;
    private SharedPreferences sharedPref;
    private Button logout;
    private Dialog logoutWarningDialog;
    private Button confirmLogoutBtn;
    private Button cancelLogoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        tappable_password = (LinearLayout)findViewById(R.id.tappable_password);
        logout = findViewById(R.id.logOutButton);

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        String account_type = sharedPref.getString("account_type", "UNKONWN");
        System.out.println(account_type);
        if (account_type.equals("FACEBOOK"))
            tappable_password.setVisibility(View.GONE);

        //tappable_payment = (LinearLayout)findViewById(R.id.tappable_payment);

        tappable_password.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openChangePassword();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutWarningDialog = new Dialog(settingActivity.this);
                logoutWarningDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                logoutWarningDialog.setContentView(R.layout.logoutwarning);

                confirmLogoutBtn = (Button) logoutWarningDialog.findViewById(R.id.logout_confirm_btn);
                cancelLogoutBtn = (Button) logoutWarningDialog.findViewById(R.id.cancel_logout_btn);

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
                        sharedPref.edit().putString("token", "NO TOKEN JA EDOK").commit();
                        sharedPref.edit().putInt("customerId", 0);
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
