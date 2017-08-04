package com.almi.games.server.endpoint;

import com.almi.games.server.game.GamePlayer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by c309044 on 2017-08-03.
 */
@Repository
public interface PlayerRepository extends CrudRepository<GamePlayer, Long> {
    Optional<GamePlayer> findGamePlayerByName(String name);
}
