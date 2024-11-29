package com.example.gamevui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamevui.databinding.ActivityPlayNowBinding;

public class play_now extends AppCompatActivity {

    ActivityPlayNowBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayNowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bắt sự kiện khi nhấn vào nút "Chơi ngay"
        binding.btnPlayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(play_now.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Sự kiện khi nhấn nút "Thoát"
        binding.btnthoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity(); // Đóng toàn bộ ứng dụng
            }
        });

    }
}