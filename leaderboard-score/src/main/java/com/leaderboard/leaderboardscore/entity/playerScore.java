package com.leaderboard.leaderboardscore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;


@Entity
@Table(name="player_score")
public class playerScore implements Comparable<playerScore>  {
    @Id
    @Column(name="player_id")
    private String playerId;
    private Long score;

    public playerScore(){

    }

    public playerScore(String playerId, Long score) {
        this.playerId = playerId;
        this.score = score;
    }

    public Long getScore() {
        return this.score;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    @Override
    public int compareTo(playerScore o) {
        if (this.score.equals(o.getScore())) {
            return this.playerId.compareTo(o.getPlayerId());
        }
        return Long.compare(this.score, o.getScore());
    }

    @Override
    public boolean equals(Object o) {
        return this.playerId.equals(((playerScore)o).getPlayerId())
                && this.score.equals(((playerScore)o).getScore());
    }

    @Override
    public String toString() {
        return "{" + playerId + " " + score + "}";
    }
}