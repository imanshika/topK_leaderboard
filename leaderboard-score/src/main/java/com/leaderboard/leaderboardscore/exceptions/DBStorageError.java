package com.leaderboard.leaderboardscore.exceptions;

public class DBStorageError extends RuntimeException {
    public DBStorageError(String message) {
        super(message);
    }
}
