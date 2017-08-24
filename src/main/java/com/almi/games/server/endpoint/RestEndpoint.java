package com.almi.games.server.endpoint;

import com.almi.games.server.board.Board;
import com.almi.games.server.endpoint.requests.GameConnectionRequest;
import com.almi.games.server.endpoint.requests.GameFinishRequest;
import com.almi.games.server.endpoint.requests.GameMoveRequest;
import com.almi.games.server.endpoint.requests.UserGameRequest;
import com.almi.games.server.game.Game;
import com.almi.games.server.game.GameMove;
import com.almi.games.server.game.GameStatus;
import com.almi.games.server.game.templates.GameTemplate;
import com.almi.games.server.game.templates.GameTemplateEnum;
import com.almi.games.server.game.templates.GameTemplateFactory;
import com.almi.games.server.logs.EndpointDescription;
import com.almi.games.server.request.GameResponse;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Comparator;
import java.util.List;

/**
 * Created by c309044 on 2017-08-02.
 */
@RestController
@Slf4j
public class RestEndpoint {

    @Autowired
    private GameService gameService;

    @GetMapping(value = "/api/game/new", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @EndpointDescription(description = "This endpoint, when called, returns default board data for TIC TAC TOE game.")
    public GameResponse defaultBoard() {
        return getInitialBoardByGameType(GameTemplateEnum.TIC_TAC_TOE);
    }

    @GetMapping(value = "/api/game/new/{type}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @EndpointDescription(description = "This endpoint, when called, returns default board data for game type specified as type parameter.")
    public GameResponse retreiveBoard(@PathVariable(value = "type") GameTemplateEnum type) {
        return getInitialBoardByGameType(type);
    }

    @GetMapping(value = "/api/game/status/{status}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<Iterable<Game>> getStartedGames(@PathVariable("status") GameStatus status) {
        DeferredResult<Iterable<Game>> startedGamesResponse = new DeferredResult<>();
        startedGamesResponse.setResult(gameService.listAllGames(status));
        return startedGamesResponse;
    }

    @PostMapping(value = "/api/game/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @EndpointDescription(description = "Create game with specific data such as: game creation time and player that created the game. All data are sent in AIGameRequest object")
    public DeferredResult<Game> startGame(@RequestBody UserGameRequest gameRequest) {
        DeferredResult<Game> gameResult = new DeferredResult<>();
        Single<Game> startedGameSingle = gameService.createGame(gameRequest);
        startedGameSingle.subscribe(gameResult::setResult, gameResult::setErrorResult);
        return gameResult;
    }

    @PostMapping(value = "/api/game/join", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<Game> joinToExistingGame(@RequestBody GameConnectionRequest gameRequest) {
        DeferredResult<Game> gameResult = new DeferredResult<>();
        Single<Game> startedGameSingle = gameService.connectToExistingGame(gameRequest);
        startedGameSingle.subscribe(gameResult::setResult, gameResult::setErrorResult);
        return gameResult;
    }

    @PostMapping(value = "/api/game/move", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<GameResponse> addMoveForGame(@RequestBody GameMoveRequest gameRequest) {
        DeferredResult<GameResponse> gameResult = new DeferredResult<>();
        Single<Game> gameAction = gameService.addMoveInGame(gameRequest);
        gameAction.subscribe(success-> gameResult.setResult(GameResponse.builder().build()), gameResult::setErrorResult);
        return gameResult;
    }

    @GetMapping(value = "/api/game/{id}/moves", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<List<GameMove>> getAllMovesForGame(@PathVariable("id") Long id) {
        DeferredResult<List<GameMove>> gameResult = new DeferredResult<>();
        Single<List<GameMove>> gameAction = gameService.getMovesForGame(id);
        gameAction.subscribe(gameResult::setResult, gameResult::setErrorResult);
        return gameResult;
    }

    @PostMapping(value = "/api/game/finish", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<Game> finishGame(@RequestBody GameFinishRequest gameRequest) {
        DeferredResult<Game> gameResult = new DeferredResult<>();
        Single<Game> finishedGame = gameService.finishGame(gameRequest);
        finishedGame.subscribe(gameResult::setResult, gameResult::setErrorResult);
        return gameResult;
    }

    private GameResponse getInitialBoardByGameType(GameTemplateEnum type) {
        GameTemplate template = GameTemplateFactory.of(type);
        return GameResponse.builder()
                .board(Board.builder()
                        .cols(template.cols())
                        .rows(template.rows())
                        .availableFigures(template.availableFigures())
                        .build())
                .build();
    }

}
