package com.leaderboard.leaderboardscore.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.leaderboard.leaderboardscore.entity.playerScore;
import com.leaderboard.leaderboardscore.config.configProperties;

import java.util.List;
import java.util.Optional;

@Repository
public interface scoreRepository extends JpaRepository<playerScore, String>{

    @Query("SELECT p from playerScore p order by score, playerId desc LIMIT :K")
    List<playerScore> findTopKPlayer(@Param("K") Integer K);
}
