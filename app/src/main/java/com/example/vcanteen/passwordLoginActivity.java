package com.example.vcanteen;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class passwordLoginActivity extends AppCompatActivity {

    public EditText passwordBox;
    public Button showpw;
    public ImageButton next_button;
    private Button pwrecoverbtn;
    private Dialog recoverWarningDialog;
    private Button ok1btn;
    private Button cancelbtn;
    private Dialog confirmDialog;
    private Button ok2btn;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_login_page);
        Bundle bundle = getIntent().getExtras();
        final String var = bundle.getString("cachedemail");
        showpw = (Button) findViewById(R.id.show_pw_btn);
        passwordBox = (EditText) findViewById(R.id.passwordBox);
        next_button = (ImageButton) findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView error1 = (TextView) findViewById(R.id.error1);
                if (TextUtils.isEmpty(passwordBox.getText().toString())) {
                    error1.setVisibility(View.INVISIBLE);
                    error1.setVisibility(View.VISIBLE);
                    //else if to be implemented, send email and hashed pw to backend to check
                } else
                    Toast.makeText(getBaseContext(), var, Toast.LENGTH_LONG).show();
            }
        });

        showpw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    passwordBox.setTransformationMethod(null);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    passwordBox.setTransformationMethod(new PasswordTransformationMethod());
                }
                return false;
            }
        });

        pwrecoverbtn = (Button) findViewById(R.id.password_recov_btn);
        pwrecoverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecoverPopup();
            }
        });

    }

    public void openRecoverPopup() {
        recoverWarningDialog = new Dialog(passwordLoginActivity.this);
        recoverWarningDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        recoverWarningDialog.setContentView(R.layout.recoverwarningdialog);

        ok1btn = (Button)recoverWarningDialog.findViewById(R.id.ok1_btn);
        cancelbtn = (Button)recoverWarningDialog.findViewById(R.id.cancel_btn);

        ok1btn.setEnabled(true);
        cancelbtn.setEnabled(true);

        ok1btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code for send request to back-end here
                Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
                recoverWarningDialog.cancel();
                openConfirmDialog();
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverWarningDialog.cancel();
            }
        });

        recoverWarningDialog.show();
    }
    public void openConfirmDialog(){
        confirmDialog = new Dialog(passwordLoginActivity.this);
        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmDialog.setContentView(R.layout.confirmdialog);

        ok2btn = (Button)confirmDialog.findViewById(R.id.ok2_btn);

        ok2btn.setEnabled(true);

        ok2btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.cancel();
            }
        });
        confirmDialog.show();

    }

}
