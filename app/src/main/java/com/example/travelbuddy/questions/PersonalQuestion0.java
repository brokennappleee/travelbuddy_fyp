package com.example.travelbuddy.questions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelbuddy.R;
import com.example.travelbuddy.Signup1;

public class PersonalQuestion0 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_question0);
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

        //Back Button
        ImageButton back_btn = findViewById(R.id.imageButton1);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalQuestion0.this, Signup1.class);
                startActivity(intent);
            }
        });

        //Skip all Button
        TextView skipall_btn = findViewById(R.id.skipall_textview);
        skipall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalQuestion0.this, CreatedAccount.class);
                startActivity(intent);
            }
        });


        //Next question button
        Button next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalQuestion0.this, CreatedAccount.class);
                startActivity(intent);
            }
        });
    }
}