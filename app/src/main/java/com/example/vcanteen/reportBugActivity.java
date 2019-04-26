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
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class reportBugActivity extends AppCompatActivity {
    private Button report;
    private EditText reportText;
    private TextView counter;
    private TextView inline;
    SharedPreferences sharedPref;

    private static final Pattern TEXT_PATTERN =
            Pattern.compile("^[a-zA-Z0-9. ,_\\-*‘\"#&()$@!?]+$");  // Text Constraint
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

            if(!TEXT_PATTERN.matcher(reportText.getText().toString()).matches()) {
                System.out.println("btn print:"+reportText.getText().toString());
                inline.setText("Must be letter, number or these character . , _ - * ‘ \" # & () $ @ ! ?");
                inline.setVisibility(View.VISIBLE);
                return;
            }
            //TODO Check Emoji

            BugReport report = new BugReport();
            report.setCustomerId(sharedPref.getInt("customerId", 0));
            report.setMessage(reportText.getText().toString().trim());

            //TODO Include retrofit here for sending
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://vcanteen.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<Void> call =  jsonPlaceHolderApi.postBugReport(report);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(reportBugActivity.this, "CODE: "+response.code(),Toast.LENGTH_LONG).show();
                        return;

                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
            reportText.setText("");
            inline.setVisibility(View.INVISIBLE);
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

                if(!TEXT_PATTERN.matcher(reportText.getText().toString()).matches()) {
                    if(s.toString().isEmpty()) {
                        inline.setText("This field cannot be blank.");
                        inline.setVisibility(View.VISIBLE);
                        return;
                    }

                    System.out.println("print:"+reportText.getText().toString());
                    inline.setText("Must be letter, number or these character . , _ - * ‘ \" # & () $ @ ! ?");
                    inline.setVisibility(View.VISIBLE);
                    return;
                }
//                else if(!TEXT_PATTERN.matcher(s.toString()).matches()) {
//
//                    System.out.println("print:"+s.toString());
//                    inline.setText("Must be letter, number or these character . , _ - * ‘ \" # & () $ @ ! ?");
//                    inline.setVisibility(View.VISIBLE);
//                    return;
//                }
                else {
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
