package com.example.travelbuddy.questions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelbuddy.HomepageActivity;
import com.example.travelbuddy.R;

public class PersonalQuestion1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_question1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            float scale = getResources().getDisplayMetrics().density;
            int basePx = (int) (16 * scale);   // 16dp

            v.setPadding(
                    systemBars.left + basePx,
                    systemBars.top + basePx,
                    systemBars.right + basePx,
                    systemBars.bottom + basePx
            );
            return insets;
        });

        // Set Progress Bar
        ProgressBar progressBar = findViewById(R.id.progressBar);

        // For question 1 of 5:
        progressBar.setMax(5);
        progressBar.setProgress(1);

        //Back button
        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalQuestion1.this, PersonalQuestion0.class);
                startActivity(intent);
            }
        });


        //Skip all button
        TextView skip_btn = findViewById(R.id.skipall_textview);
        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalQuestion1.this, HomepageActivity.class);
                startActivity(intent);
            }
        });


        //Next page button
        Button next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalQuestion1.this, PersonalQuestion2.class);
            }
        });


        // ===== Image buttons: multi-select toggle =====
        ImageButton btnNature = findViewById(R.id.btn_nature);
        ImageButton btnCity = findViewById(R.id.btn_city);
        ImageButton btnShopping = findViewById(R.id.btn_shopping);
        ImageButton btnLandmarks = findViewById(R.id.btn_landmarks);

        View.OnClickListener toggleListener = v -> {
            v.setSelected(!v.isSelected());
            // simple visual feedback: dim when selected
            v.setAlpha(v.isSelected() ? 0.5f : 1f);
        };

        btnNature.setOnClickListener(toggleListener);
        btnCity.setOnClickListener(toggleListener);
        btnShopping.setOnClickListener(toggleListener);
        btnLandmarks.setOnClickListener(toggleListener);



    }
}