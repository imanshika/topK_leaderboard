package com.leaderboard.leaderboardscore.exceptions;

public class kafkaProducerError extends RuntimeException{
    public kafkaProducerError(String message){
        super(message);
    }
}
