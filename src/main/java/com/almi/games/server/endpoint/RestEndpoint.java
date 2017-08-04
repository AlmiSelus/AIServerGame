package com.almi.games.server.endpoint;

import com.almi.games.server.board.Board;
import com.almi.games.server.game.Game;
import com.almi.games.server.game.GameMove;
import com.almi.games.server.game.GameStatus;
import com.almi.games.server.game.templates.GameTemplate;
import com.almi.games.server.game.templates.GameTemplateEnum;
import com.almi.games.server.game.templates.GameTemplateFactory;
import com.almi.games.server.request.AIGameRequest;
import com.almi.games.server.request.GameResponse;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * Created by c309044 on 2017-08-02.
 */
@RestController
@Slf4j
public class RestEndpoint {

    @Autowired
    private GameService gameService;

    @GetMapping("/api/game/new")
    public GameResponse defaultBoard() {
        return getInitialBoardByGameType(GameTemplateEnum.TIC_TAC_TOE);
    }

    @GetMapping("/api/game/new/{type}")
    public GameResponse retreiveBoard(@PathVariable(value = "type") String type) {
        return getInitialBoardByGameType(GameTemplateEnum.of(type));
    }

    @GetMapping("/api/game/status/{status}")
    public DeferredResult<Iterable<Game>> getStartedGames(@PathVariable("status") String status) {
        DeferredResult<Iterable<Game>> startedGamesResponse = new DeferredResult<>();
        startedGamesResponse.setResult(gameService.listAllGames(GameStatus.of(status)));
        return startedGamesResponse;
    }

    @PostMapping("/api/game/connect")
    public DeferredResult<Game> startGame(@RequestBody AIGameRequest gameRequest) {
        DeferredResult<Game> gameResult = new DeferredResult<>();
        Single<Game> startedGameSingle = gameService.startGame(gameRequest);
        startedGameSingle.subscribe(gameResult::setResult, gameResult::setErrorResult);
        return gameResult;
    }

    @PostMapping("/api/game/join")
    public DeferredResult<Game> joinToExistingGame(@RequestBody AIGameRequest gameRequest) {
        DeferredResult<Game> gameResult = new DeferredResult<>();
        Single<Game> startedGameSingle = gameService.connectToExistingGame(gameRequest);
        startedGameSingle.subscribe(gameResult::setResult, gameResult::setErrorResult);
        return gameResult;
    }

    @PostMapping("/api/game/move")
    public DeferredResult<GameResponse> addMoveForGame(@RequestBody AIGameRequest gameRequest) {
        DeferredResult<GameResponse> gameResult = new DeferredResult<>();
        Single<Game> gameAction = gameService.addMoveInGame(gameRequest);
        gameAction.subscribe(success-> gameResult.setResult(GameResponse.builder().build()), gameResult::setErrorResult);
        return gameResult;
    }

    @GetMapping("/api/game/{id}/moves")
    public DeferredResult<List<GameMove>> getAllMovesForGame(@PathVariable("id") Long id) {
        DeferredResult<List<GameMove>> gameResult = new DeferredResult<>();
        Single<List<GameMove>> gameAction = gameService.getMovesForGame(id);
        gameAction.subscribe(gameResult::setResult, gameResult::setErrorResult);
        return gameResult;
    }

    @PostMapping("/api/game/finish")
    public DeferredResult<Game> finishGame(@RequestBody AIGameRequest gameRequest) {
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
