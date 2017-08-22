package com.almi.games.server.endpoint;

import com.almi.games.server.board.ThirdPlayerConnectsToBoardException;
import com.almi.games.server.endpoint.requests.GameConnectionRequest;
import com.almi.games.server.endpoint.requests.GameFinishRequest;
import com.almi.games.server.endpoint.requests.GameMoveRequest;
import com.almi.games.server.endpoint.requests.UserGameRequest;
import com.almi.games.server.game.Game;
import com.almi.games.server.game.GameMove;
import com.almi.games.server.game.GamePlayer;
import com.almi.games.server.game.GameStatus;
import io.reactivex.Single;
import io.reactivex.exceptions.Exceptions;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static io.vavr.API.*;
import static io.vavr.Patterns.$None;
import static io.vavr.Patterns.$Some;

/**
 * Created by c309044 on 2017-08-03.
 */
@Service
@Transactional
@Slf4j
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public Single<Game> createGame(UserGameRequest gameRequest) {
        GamePlayer gamePlayer = findGamePlayerOrCreateNew(gameRequest);

        return Single.just(gameRequest)
                .map(request-> Game.builder()
                        .gameState(GameStatus.STARTED)
                        .gameLink(createGameHash(gameRequest))
                        .createdTimestamp(gameRequest.getTimestamp())
                        .player1(gamePlayer)
                        .build())
                .doOnEvent((gameEvent, error) -> gameRepository.save(gameEvent));
    }

    private String createGameHash(UserGameRequest gameRequest) {
        String hashTimeStamp = new String(Base64.encode(gameRequest.getTimestamp().toString().getBytes()));
        hashTimeStamp += gameRequest.getUserID();
        return new String(Base64.encode(hashTimeStamp.getBytes()));
    }

    public Single<Game> connectToExistingGame(GameConnectionRequest gameRequest) {
        GamePlayer gamePlayer = findGamePlayerOrCreateNew(gameRequest);
        return Single.just(gameRequest)
                .map(request -> gameRepository.findOne(gameRequest.getGameId()))
                .map(game -> {
                    if(Optional.ofNullable(game.getPlayer2()).isPresent()) {
                        throw Exceptions.propagate(new ThirdPlayerConnectsToBoardException());
                    }
                    game.setGameState(GameStatus.IN_PROGRESS);
                    game.setPlayer2(gamePlayer);
                    return game;
                })
                .doOnEvent((game, error)->gameRepository.save(game));
    }

    public Iterable<Game> listAllGames(GameStatus started) {
        return Match(started).of(
                Case($(GameStatus.STARTED), () -> gameRepository.findAllByFinishedTimestampIsNullAndPlayer2IsNull()),
                Case($(GameStatus.IN_PROGRESS), () -> gameRepository.findAllByFinishedTimestampIsNullAndPlayer2IsNotNull()),
                Case($(GameStatus.FINISHED), () -> gameRepository.findAllByFinishedTimestampIsNotNull()),
                Case($(), () -> gameRepository.findAll())
        );
    }

    public Single<Game> addMoveInGame(GameMoveRequest gameRequest) {
        GamePlayer player = findGamePlayerOrCreateNew(gameRequest);
        return Single.just(gameRequest)
                .map(gameRequest1 -> gameRepository.findOne(gameRequest.getGameId()))
                .map(game->{
                    game.getGameMoves().add(
                            GameMove.builder()
                                    .game(game)
                                    .player(player)
                                    .col(gameRequest.getCol())
                                    .row(gameRequest.getRow())
                                    .moveTimestamp(gameRequest.getTimestamp())
                                    .build());
                    log.info("Game = " + game.toString());
                    return game;
                })
                .doOnEvent((game, error)-> gameRepository.save(game));
    }

    public Single<Game> finishGame(GameFinishRequest gameRequest) {
        return Single.just(gameRequest)
                .map(request-> gameRepository.findOne(request.getGameId()))
                .map(game-> {
                    if(gameRequest.getGameStatus() == GameStatus.FINISHED) {
                        game.setFinishedTimestamp(gameRequest.getTimestamp());
                        game.setGameState(GameStatus.FINISHED);
                    }
                    return game;
                })
                .map(game-> {
                    Option<GamePlayer> gameWinner = Match(gameRequest.getWinner()).option(
                            Case($(game.getPlayer1().getName()), game::getPlayer1),
                            Case($(game.getPlayer2().getName()), game::getPlayer2)
                    );

                    return Match(gameWinner).of(
                            Case($Some($()), (gamePlayer)-> Match(game.getGameState()).of(
                                    Case($(GameStatus.FINISHED), () -> game.toBuilder().winner(gamePlayer).build()),
                                    Case($(), () -> game)
                            )),
                            Case($None(), ()-> game)
                    );
                })
                .doOnEvent((game, e)-> gameRepository.save(game));
    }

    private GamePlayer findGamePlayerOrCreateNew(UserGameRequest gameRequest) {
        return playerRepository.findGamePlayerByName(gameRequest.getUserID())
                .orElse(GamePlayer.builder().name(gameRequest.getUserID()).build());
    }

    public Single<List<GameMove>> getMovesForGame(Long id) {
        return Single.just(id)
                .map(i -> gameRepository.findOne(i))
                .map(Game::getGameMoves)
                .map(gameMoves-> {
                    gameMoves.sort(Comparator.comparing(GameMove::getMoveTimestamp));
                    return gameMoves;
                });
    }
}
