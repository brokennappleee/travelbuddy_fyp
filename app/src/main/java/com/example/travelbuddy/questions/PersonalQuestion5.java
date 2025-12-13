package com.example.travelbuddy.questions;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelbuddy.R;

public class PersonalQuestion5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_question5);
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

    }
}