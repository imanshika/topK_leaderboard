package com.leaderboard.leaderboardscore.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.leaderboard.leaderboardscore.config.configProperties;

import com.leaderboard.leaderboardscore.entity.playerScore;

@Service
public class kafkaConsumerService {

    Logger logger = LoggerFactory.getLogger(kafkaConsumerService.class);

    @Autowired
    scoreUpdateDbService ScoreUpdateDbService;

    @Autowired
    scoreCacheService ScoreCacheService;

    @KafkaListener(topics = configProperties.KAFKA_TOPIC, groupId = configProperties.MySQL_CONSUMER_GRP)
    public void listen(playerScore playerScore) {
        try{
            logger.info("Received message | playerID = {} and score = {}", playerScore.getPlayerId(), playerScore.getScore());
            ScoreUpdateDbService.saveToDB(playerScore);
            ScoreCacheService.updateCacheService(playerScore);
        }
        catch(Exception e){
            logger.error("Error occurred while consuming kafka messages. Error Message -> {}", e.getMessage());
        }
    }
}
