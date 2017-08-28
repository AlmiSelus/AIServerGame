package com.almi.games.server.ai;

import com.almi.games.server.ai.decisiontree.StateTreeGenerator;
import com.almi.games.server.ai.decisiontree.StateTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by c309044 on 2017-08-28.
 */
@RestController
public class AIRestController {

    @Autowired
    private StateTreeGenerator stateTreeNodeGenerator;

    @GetMapping(value = "/api/ai/generate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public StateTreeNode generateStateTree() {
        return stateTreeNodeGenerator.generate(StateTreeNode.EMPTY);
    }
}
