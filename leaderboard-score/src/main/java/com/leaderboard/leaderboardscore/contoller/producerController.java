package com.leaderboard.leaderboardscore.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leaderboard.leaderboardscore.services.kafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.leaderboard.leaderboardscore.entity.playerScore;
import org.springframework.web.server.ResponseStatusException;
import com.leaderboard.leaderboardscore.exceptions.kafkaProducerError;

@RestController
public class producerController {

    Logger logger = LoggerFactory.getLogger(producerController.class);

    @Autowired
    private kafkaProducerService producerService;

    @PostMapping("/sendScoreToKafka")
    public String get(@RequestBody playerScore PlayerScore) {
        try {
            producerService.sendMessage(PlayerScore);
        } catch (Exception e) {
            logger.error("Error occurred while pushing score to kafka for request {}. Error Message -> {}", PlayerScore, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error occurred while sending score to kafka");
        }
        return "Message is pushed to kafka";
    }
}
