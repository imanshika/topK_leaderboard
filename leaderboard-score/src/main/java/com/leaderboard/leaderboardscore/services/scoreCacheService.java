package com.leaderboard.leaderboardscore.services;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leaderboard.leaderboardscore.entity.playerScore;
import com.leaderboard.leaderboardscore.exceptions.cacheUpdateError;
import com.leaderboard.leaderboardscore.exceptions.cacheIntitliazationError;


import org.springframework.stereotype.Service;


@Service
public class scoreCacheService {

    Integer topN;
    HashMap<String, playerScore> topKPlayer;

    Logger logger = LoggerFactory.getLogger(scoreCacheService.class);

    public void initialize(int K, List<playerScore> dataSet) throws cacheIntitliazationError{
        this.topN = K;
        try{
            topKPlayer = new HashMap<String, playerScore>();
            for (playerScore PlayerScore : dataSet) {
                updateCacheService(PlayerScore);
            }
        }catch(Exception e){
            throw new cacheIntitliazationError("Error occurred while initializing cache. Error Message -> "+e.getMessage());
        }
    }

    public void updateCacheService(playerScore PlayerScore) throws cacheUpdateError{
        try{
            String playerId = PlayerScore.getPlayerId();
            Long score = PlayerScore.getScore();

            if(topKPlayer.containsKey(playerId)){
                if(topKPlayer.get(playerId).getScore() < score){
                    topKPlayer.put(playerId, PlayerScore);
                    logger.info("Score Saved to cache | playerID = {} and score = {}", playerId, score);
                }else{
                    logger.info("Score is ignored to save in Cache | playerID = {} and score = {}", playerId, score);
                }
            }else if(topKPlayer.size() < topN){
                topKPlayer.put(playerId, PlayerScore);
                logger.info("Score Saved to cache | playerID = {} and score = {}", playerId, score);
            }else{

                playerScore leastPlayer = PlayerScore;

                for (Map.Entry<String, playerScore> set : topKPlayer.entrySet()) {

                    if(leastPlayer.compareTo(set.getValue()) > 0){
                        leastPlayer = set.getValue();
                    }
                }

                if(!leastPlayer.equals(PlayerScore)){
                    topKPlayer.remove(leastPlayer.getPlayerId());
                    topKPlayer.put(playerId, PlayerScore);
                    logger.info("Score Saved to cache | playerID = {} and score = {}", playerId, score);
                }else{
                    logger.info("Score is ignored to save in Cache | playerID = {} and score = {}", playerId, score);
                }

            }
        }catch (Exception e){
            throw new cacheUpdateError("Error occurred while updating cache. Error Message -> " + e.getMessage());
        }
    }

    public List<playerScore> getTopKplayer(){
        List<playerScore> res = new ArrayList<playerScore>(topKPlayer.values());
        Collections.sort(res, Collections.reverseOrder());
        return res;
    }

}
