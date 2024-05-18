package com.leaderboard.leaderboardscore.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.leaderboard.leaderboardscore.entity.playerScore;
import com.leaderboard.leaderboardscore.services.leaderboardService;

@RestController
public class leaderboardController {
    Logger logger = LoggerFactory.getLogger(leaderboardController.class);

    @Autowired
    leaderboardService LeaderboardService;

    @GetMapping("/")
    public String root() {
        return "Leaderboard API";
    }

    @GetMapping("/getTopKScores")
    public List<playerScore> getTopScorers() {
        try {
            return LeaderboardService.getTopKPlayerScore();
        } catch (Exception e) {
            logger.error("Couldn't get top K scores - {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while fetching topK score");
        }
    }


    @PostMapping("/deletePlayer")
    public String get(@RequestParam("playerID") String playerID) {
        try {

            LeaderboardService.deletePlayer(playerID);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error occurred while deleting record");
        }
        return "Record is deleted";
    }
}
