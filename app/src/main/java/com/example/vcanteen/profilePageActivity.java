package com.example.vcanteen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class profilePageActivity extends AppCompatActivity {

    private Button editProfileButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        editProfileButton = findViewById(R.id.editProfileButton);

        editProfileButton.setOnClickListener(v -> {
            startActivity(new Intent(profilePageActivity.this, editProfilePageActivity.class));
        });

    }
}
