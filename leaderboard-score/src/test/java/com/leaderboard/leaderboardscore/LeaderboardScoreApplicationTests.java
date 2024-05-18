package com.leaderboard.leaderboardscore;

import java.util.ArrayList;
import java.util.List;

import com.leaderboard.leaderboardscore.entity.playerScore;
import com.leaderboard.leaderboardscore.repository.scoreRepository;
import com.leaderboard.leaderboardscore.services.kafkaProducerService;
import com.leaderboard.leaderboardscore.services.leaderboardService;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Fail.fail;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
class LeaderboardScoreApplicationTests {

	@Container
	static final MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:8.0.30")
			.withDatabaseName("testcontainer")
			.withUsername("test")
			.withPassword("test");

	@Autowired
	kafkaProducerService KafkaProducerService;

	@Autowired
	scoreRepository ScoreRepository;

	@Autowired
	leaderboardService LeaderboardService;

	@DynamicPropertySource
	static void configureTestProperties(DynamicPropertyRegistry registry){
		registry.add("spring.datasource.url",() -> mySQLContainer.getJdbcUrl());
		registry.add("spring.datasource.username",() -> mySQLContainer.getUsername());
		registry.add("spring.datasource.password",() -> mySQLContainer.getPassword());
		registry.add("spring.jpa.hibernate.ddl-auto",() -> "create");

	}

	public void kafkaIngestion(playerScore PlayerScore) throws RuntimeException{
		try{
			KafkaProducerService.sendMessage(PlayerScore);
			await()
					.pollInterval(Duration.ofSeconds(3))
					.atMost(10, SECONDS)
					.untilAsserted(() -> {
						Optional<playerScore> optionalValue = ScoreRepository.findById(PlayerScore.getPlayerId());
						assertTrue(optionalValue.isPresent());
					});

		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Test
	public void kafkaTest(){
		try{
			playerScore PlayerScore = new playerScore("Test-123", 100L);
			kafkaIngestion(PlayerScore);
		}catch (Exception e){
			fail("Kafka Test is failed with below exception \n"+ e.getMessage());
		}
	}

	@Test
	public void ignoreLesserScoreToUpdateInStorage() throws InterruptedException {
		String playerId = "Test-101";
		Long prevScore = 110L;
		Long newScore = 90L;
		kafkaIngestion(new playerScore(playerId, prevScore));
		kafkaIngestion(new playerScore(playerId, newScore));
		Optional<playerScore> optionalPlayerScore = ScoreRepository.findById(playerId);
		assertTrue(optionalPlayerScore.isPresent());
		assertEquals(prevScore, optionalPlayerScore.get().getScore());
	}

	@Test
	public void topKScoreTest(){
		try {
			playerScore P1 = new playerScore("Test-101", 110L);
			playerScore P2 = new playerScore("Test-102", 100L);
			playerScore P3 = new playerScore("Test-103", 120L);
			playerScore P4 = new playerScore("Test-104", 80L);
			playerScore P5 = new playerScore("Test-105", 100L);
			playerScore P6 = new playerScore("Test-106", 130L);

			kafkaIngestion(P1);
			kafkaIngestion(P2);
			kafkaIngestion(P3);
			kafkaIngestion(P4);
			kafkaIngestion(P5);
			kafkaIngestion(P6);

			List<playerScore> outputList = new ArrayList<>();
			outputList.add(P6);
			outputList.add(P3);
			outputList.add(P1);
			outputList.add(P5);
			outputList.add(P2);

			assertEquals(outputList, LeaderboardService.getTopKPlayerScore());

			P6.setScore(200L);
			kafkaIngestion(P6);
			outputList.set(0, P6);

			assertEquals(outputList, LeaderboardService.getTopKPlayerScore());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

    @AfterEach
    public void tearDown() {
        ScoreRepository.deleteAll();
    }

}
