package com.example.gamevui;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamevui.Models.PlayerScore;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ScoreBoardActivity extends AppCompatActivity {

    private TableLayout scoreTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        scoreTable = findViewById(R.id.scoreTable);

        loadScoresFromFirebase();
    }

    private void loadScoresFromFirebase() {
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("scores");

        scoresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Sử dụng HashMap để lưu trữ tên player và tổng điểm của họ
                HashMap<String, Integer> playerScoresMap = new HashMap<>();

                // Lấy toàn bộ dữ liệu điểm số từ Firebase và lưu vào HashMap
                for (DataSnapshot scoreSnapshot : dataSnapshot.getChildren()) {
                    String playerEmail = scoreSnapshot.child("playerName").getValue(String.class);
                    Integer score = scoreSnapshot.child("score").getValue(Integer.class);

                    if (playerEmail != null && score != null) {
                        // Nếu player đã tồn tại, cộng điểm mới vào tổng điểm
                        if (playerScoresMap.containsKey(playerEmail)) {
                            int currentScore = playerScoresMap.get(playerEmail);
                            playerScoresMap.put(playerEmail, currentScore + score);
                        } else {
                            playerScoresMap.put(playerEmail, score);
                        }
                    }
                }

                // Chuyển từ HashMap sang danh sách để sắp xếp và hiển thị
                List<PlayerScore> playerScores = new ArrayList<>();
                for (String playerName : playerScoresMap.keySet()) {
                    playerScores.add(new PlayerScore(playerName, playerScoresMap.get(playerName)));
                }

                // Sắp xếp danh sách theo thứ tự giảm dần của điểm số
                Collections.sort(playerScores, new Comparator<PlayerScore>() {
                    @Override
                    public int compare(PlayerScore p1, PlayerScore p2) {
                        return Integer.compare(p2.getScore(), p1.getScore()); // Sắp xếp giảm dần
                    }
                });

                // Xóa các hàng cũ trước khi thêm hàng mới
                scoreTable.removeViews(1, scoreTable.getChildCount() - 1);

                // Hiển thị điểm số đã sắp xếp cùng với thứ hạng
                int rank = 1;
                for (PlayerScore playerScore : playerScores) {
                    addScoreRow(rank++, playerScore);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void addScoreRow(int rank, PlayerScore playerScore) {
        // Tạo một hàng mới
        TableRow row = new TableRow(this);

        // Xen kẽ màu nền cho các hàng
        int backgroundColor = (rank % 2 == 0) ? getResources().getColor(R.color.row_even) : getResources().getColor(R.color.row_odd);
        row.setBackgroundColor(backgroundColor);

        // TextView cho thứ hạng
        TextView rankView = new TextView(this);
        rankView.setText(String.valueOf(rank));
        rankView.setPadding(16, 16, 16, 16);
        rankView.setTextSize(18);
        rankView.setGravity(Gravity.CENTER);  // Căn giữa

        // TextView cho tên người chơi
        TextView nameView = new TextView(this);
        nameView.setText(playerScore.getPlayerName());
        nameView.setPadding(16, 16, 16, 16);
        nameView.setTextSize(18);
        nameView.setGravity(Gravity.CENTER);  // Căn giữa

        // TextView cho điểm số
        TextView scoreView = new TextView(this);
        scoreView.setText(String.valueOf(playerScore.getScore()));
        scoreView.setPadding(16, 16, 16, 16);
        scoreView.setTextSize(18);
        scoreView.setGravity(Gravity.CENTER);  // Căn giữa

        // Thêm các TextView vào hàng
        row.addView(rankView);
        row.addView(nameView);
        row.addView(scoreView);

        // Thêm hàng vào TableLayout
        scoreTable.addView(row);
    }
}
