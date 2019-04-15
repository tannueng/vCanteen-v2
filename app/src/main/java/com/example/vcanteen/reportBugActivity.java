package com.example.vcanteen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class reportBugActivity extends AppCompatActivity {
    private Button report;
    private EditText reportText;
    private TextView counter;
    private TextView inline;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report);
        report = findViewById(R.id.reportBtn);
        reportText = findViewById(R.id.reportText);
        inline = findViewById(R.id.inline_blank);
        counter = findViewById(R.id.textCounter);

        report.setOnClickListener(v -> {
            System.out.println(reportText.getText());

            if(reportText.getText().toString().trim().isEmpty()){
                inline.setVisibility(View.VISIBLE);
                return;
            }

            //TODO Include retrofit here for sending

            reportText.setText("");
            Toast.makeText(this, "Bug report successfully submitted", Toast.LENGTH_LONG).show();
        });

        reportText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                counter.setText(s.toString().length()+"/300");

                if(s.toString().length()>0) {
                    inline.setVisibility(View.INVISIBLE);
                }
            }
        });
        findViewById(R.id.relativeLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
    }

}
