package com.example.gamevui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamevui.Models.PlayerScore;
import com.example.gamevui.databinding.ActivityScoreBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {

    ActivityScoreBinding binding;
    private long timeTaken;
    private int totalQuestions, correctAnsw, wrongAnsw, skipQuestion;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy tham chiếu tới Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("scores");

        // Nhận dữ liệu từ Intent
        totalQuestions = getIntent().getIntExtra("total_questions", 0);
        correctAnsw = getIntent().getIntExtra("correct", 0);
        wrongAnsw = getIntent().getIntExtra("wrong", 0);
        timeTaken = getIntent().getLongExtra("time_taken", 0);

        String remainingTime = String.format("%02d:%02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken)));

        binding.TimeTaken.setText(remainingTime);

        skipQuestion = totalQuestions - (correctAnsw + wrongAnsw);

        binding.question.setText(totalQuestions + "");
        binding.txtCorect.setText(correctAnsw + "");
        binding.txtWrong.setText(wrongAnsw + "");
        binding.txtSkip.setText(skipQuestion + "");

        // Cộng mỗi câu trả lời đúng 10 điểm
        int totalScore = correctAnsw * 10;
        binding.score.setText(totalScore + ""); // Hiển thị điểm đã tính toán

        // Lấy email người chơi từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String playerEmail = sharedPreferences.getString("saved_email", "anonymous@example.com"); // Lấy email thay vì tên

        // Lưu điểm vào Firebase
        saveScoreToFirebase(playerEmail, totalScore);

        binding.btnReAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        binding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity(); // Đóng toàn bộ ứng dụng
            }
        });
    }

    // Hàm lưu điểm vào Firebase
    private void saveScoreToFirebase(String playerEmail, int score) {
        String scoreId = databaseReference.push().getKey();
        PlayerScore playerScore = new PlayerScore(playerEmail, score);
        if (scoreId != null) {
            databaseReference.child(scoreId).setValue(playerScore);
        }
    }
}
