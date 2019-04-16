package com.example.vcanteen;

import android.content.SharedPreferences;
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

import com.example.vcanteen.POJO.BugReport;
import com.facebook.share.Share;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class reportBugActivity extends AppCompatActivity {
    private Button report;
    private EditText reportText;
    private TextView counter;
    private TextView inline;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report);
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);

        report = findViewById(R.id.reportBtn);
        reportText = findViewById(R.id.reportText);
        inline = findViewById(R.id.inline_blank);
        counter = findViewById(R.id.textCounter);

        report.setOnClickListener(v -> {
            System.out.println(reportText.getText());

            if(reportText.getText().toString().trim().isEmpty()){
                inline.setText("This field cannot be blank.");
                inline.setVisibility(View.VISIBLE);
                return;
            }

            //TODO Check Emoji

            BugReport report = new BugReport();
            report.setCustomerId(sharedPref.getInt("customerId", 0));
            report.setMessage(reportText.getText().toString().trim());

            //TODO Include retrofit here for sending
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://vcanteen.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<Void> call =  jsonPlaceHolderApi.postBugReport(report);

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
