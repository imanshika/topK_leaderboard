package com.leaderboard.leaderboardscore.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

import com.leaderboard.leaderboardscore.entity.playerScore;
import com.leaderboard.leaderboardscore.exceptions.DBStorageError;
import com.leaderboard.leaderboardscore.repository.scoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class scoreUpdateDbService {

    Logger logger = LoggerFactory.getLogger(scoreUpdateDbService.class);

    @Autowired
    private scoreRepository ScoreRepository;

    public void saveToDB(playerScore PlayerScore) throws DBStorageError {
        try{
            Optional<playerScore> playerScoreExists = ScoreRepository.findById(PlayerScore.getPlayerId());
            if (playerScoreExists.isEmpty() || playerScoreExists.get().getScore() < PlayerScore.getScore()) {
                ScoreRepository.save(PlayerScore);
                logger.info("Score Saved to DB | playerID = {} and score = {}", PlayerScore.getPlayerId(), PlayerScore.getScore());
            }else{
                logger.info("Score is ignored to save in DB | playerID = {} and score = {}", PlayerScore.getPlayerId(), PlayerScore.getScore());
            }
        }catch(Exception e){
            throw new DBStorageError("Error occurred while saving score to My SQL DB. Error Message -> " + e.getMessage());
        }
    }
}
