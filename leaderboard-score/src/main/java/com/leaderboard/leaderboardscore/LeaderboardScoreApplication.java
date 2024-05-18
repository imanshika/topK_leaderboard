package com.leaderboard.leaderboardscore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.leaderboard.leaderboardscore.config.configProperties;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class LeaderboardScoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaderboardScoreApplication.class, args);
	}

}
