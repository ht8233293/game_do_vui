package com.example.gamevui.Models;

public class PlayerScore {
    private String playerName;
    private int score;

    // Constructor rỗng cho Firebase
    public PlayerScore() {
    }

    // Constructor với tham số
    public PlayerScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
