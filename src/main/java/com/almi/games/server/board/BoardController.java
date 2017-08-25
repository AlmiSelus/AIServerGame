package com.almi.games.server.board;

import com.almi.games.server.ai.Generator;
import com.almi.games.server.ai.decisiontree.MinmaxTrainingMethod;
import com.almi.games.server.ai.decisiontree.StateTreeNode;
import com.almi.games.server.endpoint.GameRepository;
import com.almi.games.server.game.Game;
import com.almi.games.server.game.GameMove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Comparator;

/**
 * Created by c309044 on 2017-08-23.
 */
@Controller
public class BoardController {

    private static final String GAME_DATA_PROPERTY = "gameData";

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MinmaxTrainingMethod minmaxTraningMethod;

    @Autowired
    private Generator<StateTreeNode> stateTreeNodeGenerator;


    @GetMapping("/boards")
    public String showAllBoards() {
        return "tic-tac-toe";
    }

    @GetMapping("/board/{gameHash}")
    public String ticTacToeBoard(@PathVariable("gameHash") String gameHash, Model model) {
        Game selectedGame = gameRepository.findByGameLink(gameHash);
        selectedGame.getGameMoves().sort(Comparator.comparing(GameMove::getMoveTimestamp));
        model.addAttribute(GAME_DATA_PROPERTY, selectedGame);
        return "tic-tac-toe";
    }

    @GetMapping("/board/training/minmax")
    public void trainMinMax() {
        minmaxTraningMethod.train(stateTreeNodeGenerator.generate());
    }

}
