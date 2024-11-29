package com.example.gamevui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;  // TextView cho "Đăng ký"
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);  // TextView cho "Đăng ký"

        mAuth = FirebaseAuth.getInstance();

        // Khôi phục thông tin đăng nhập nếu có
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("saved_email", "");
        String savedPassword = sharedPreferences.getString("saved_password", "");

        if (!TextUtils.isEmpty(savedEmail) && !TextUtils.isEmpty(savedPassword)) {
            emailEditText.setText(savedEmail);
            passwordEditText.setText(savedPassword);
        }

        // Xử lý sự kiện khi nhấn nút đăng nhập
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String userName = user.getEmail();  // Sử dụng email làm tên người dùng

                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                                        // Lưu thông tin đăng nhập vào SharedPreferences
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("saved_email", email);
                                        editor.putString("saved_password", password);
                                        editor.putString("player_name", userName);  // Lưu tên người dùng
                                        editor.apply();

                                        // Chuyển đến màn hình Play Now
                                        startActivity(new Intent(LoginActivity.this, play_now.class));
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        // Chuyển đến RegisterActivity nếu người dùng chưa có tài khoản
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }
}
