package com.almi.games.server.ai;

import com.almi.games.server.ai.decisiontree.StateTreeGenerator;
import com.almi.games.server.ai.decisiontree.StateTreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * Created by c309044 on 2017-08-28.
 */
@RestController
public class AIRestController {

    @Autowired
    private StateTreeGenerator stateTreeNodeGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping(value = "/api/ai/generate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public StateTreeNode generateStateTree() {
        return stateTreeNodeGenerator.generate(StateTreeNode.EMPTY);
    }

    @GetMapping(value = "/api/ai/generate/{fileName}")
    public void generateTreeAndSaveToFile(@PathVariable("fileName") String fileName) throws Exception {
        StateTreeNode allNodes = stateTreeNodeGenerator.generate(StateTreeNode.parseSingleString("[0, 0, 0, 0, 0, 0, 0, 0, 0]"));
        String treeAsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(allNodes);
        File file = new File("stateTreeNodes.json");
        if(!file.exists()) {
            file.createNewFile();
        }
        FileUtils.write(file, treeAsString);
    }
}
