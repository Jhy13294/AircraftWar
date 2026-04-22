package edu.hitsz.dao;
import java.util.Objects;


public class PlayerRecord implements Comparable<PlayerRecord> {
    private String userName;
    private int score;
    private String recordTime;

    public PlayerRecord(String userName, int score, String recordTime) {
        this.userName = userName;
        this.score = score;
        this.recordTime = recordTime;
    }

    // Getters and Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getRecordTime() { return recordTime; }
    public void setRecordTime(String recordTime) { this.recordTime = recordTime; }

    // 重写 compareTo 方法，实现按分数降序排序 (分数高的排在前面)
    @Override
    public int compareTo(PlayerRecord other) {
        // 如果当前分数比 other 小，返回正数，使其往后排
        return Integer.compare(other.getScore(), this.score);
    }

    @Override
    public String toString() {
        return userName + "," + score + "," + recordTime;
    }
}
