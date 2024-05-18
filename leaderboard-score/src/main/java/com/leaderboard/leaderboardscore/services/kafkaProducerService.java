package com.leaderboard.leaderboardscore.services;

import com.leaderboard.leaderboardscore.config.configProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.leaderboard.leaderboardscore.entity.playerScore;
import com.leaderboard.leaderboardscore.exceptions.kafkaProducerError;

import java.util.Random;

@Service
public class kafkaProducerService {

    Logger logger = LoggerFactory.getLogger(kafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    //@Scheduled(fixedDelay = 5000)
    public void generateRandomMessage(){
        Random r = new Random();
        String playerId = "Player-"+r.nextInt(1000)+1;
        long playerScore = r.nextLong(1000000000);
        sendMessage(new playerScore(playerId, playerScore));
    }

    public void sendMessage(playerScore playerScore) throws kafkaProducerError{
        try{
            kafkaTemplate.send(configProperties.KAFKA_TOPIC, playerScore);
            logger.info("Pushed message to kafka topic: {} | player id = {} and score = {}", configProperties.KAFKA_TOPIC, playerScore.getPlayerId(), playerScore.getScore());
        }catch(Exception e){
            throw new kafkaProducerError("Error occurred while sending message to kafka topic. Error Message -> "+e.getMessage());
        }
    }


}
