package com.leaderboard.leaderboardscore.strategy;

import java.util.Comparator;
import com.leaderboard.leaderboardscore.entity.playerScore;

public interface playerScoreStrategy extends Comparator<playerScore>{
    int compareTo(playerScore score1, playerScore score2);
}
