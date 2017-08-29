package com.almi.games.server.ai;

import com.almi.games.server.ai.decisiontree.StateTreeGenerator;
import com.almi.games.server.ai.decisiontree.StateTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by c309044 on 2017-08-29.
 */
@Controller
public class AIVisualizationController {

    @Autowired
    private StateTreeGenerator stateTreeNodeGenerator;

    @GetMapping(value = "/ai/visualize/states")
    public String visualizeStatesTree(@RequestParam("state") String stateString, Model model) {
        model.addAttribute("generatedAttribute", stateTreeNodeGenerator.generate(StateTreeNode.parseSingleString(stateString)));
        return "ai/state-tree-visualisation";
    }

}
