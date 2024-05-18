package com.leaderboard.leaderboardscore.services;

import com.leaderboard.leaderboardscore.entity.playerScore;
import com.leaderboard.leaderboardscore.repository.scoreRepository;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.List;

import com.leaderboard.leaderboardscore.exceptions.cacheIntitliazationError;
import com.leaderboard.leaderboardscore.exceptions.leaderboardInitializationError;
import com.leaderboard.leaderboardscore.config.configProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class leaderboardService {
    Logger logger = LoggerFactory.getLogger(leaderboardService.class);

    boolean leaderboardInitialized;

    @Autowired
    scoreCacheService ScoreCacheService;

    @Autowired
    scoreUpdateDbService ScoreUpdateDbService;

    @Autowired
    scoreRepository ScoreRepository;

    @PostConstruct
    public void createBoard() throws leaderboardInitializationError{
        leaderboardInitialized = false;
        try {
            List<playerScore> allScores = ScoreRepository.findTopKPlayer(configProperties.K);
            ScoreCacheService.initialize(configProperties.K, allScores);
            leaderboardInitialized = true;
            logger.info("Leader Board is Initialization");
        } catch (cacheIntitliazationError e) {
            logger.error("Leader Board Initialization Failed - " + e.getMessage());
            throw new leaderboardInitializationError(e.getMessage());
        }
    }

    public List<playerScore> getTopKPlayerScore() throws leaderboardInitializationError{
        if(!leaderboardInitialized){
            throw new leaderboardInitializationError("Leaderboard Not Initialized - Cannot retrieve top players");
        }
        return ScoreCacheService.getTopKplayer();
    }

    public void deletePlayer(String playerID) throws RuntimeException {
        try{
            ScoreRepository.deleteById(playerID);
            List<playerScore> allScores = ScoreRepository.findTopKPlayer(configProperties.K);
            ScoreCacheService.initialize(configProperties.K, allScores);
        }catch(Exception e){
            throw new RuntimeException("Error Ocurred while deleting playerID "+ playerID+ " with Error message "+e.getMessage());
        }
    }

}
