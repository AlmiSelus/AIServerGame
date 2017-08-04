package com.almi.games.server.endpoint;

import com.almi.games.server.game.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by c309044 on 2017-08-03.
 */
@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    List<Game> findAllByFinishedTimestampIsNullAndPlayer2IsNull();
    List<Game> findAllByFinishedTimestampIsNullAndPlayer2IsNotNull();
    List<Game> findAllByFinishedTimestampIsNotNull();
}
