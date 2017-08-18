package com.almi.games.server.logs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by Almi on 8/16/2017.
 *
 * Controller that provides endpoint to check all other Rest Endpoints
 */
@Slf4j
@Controller
public class EndpointsLogsController {

    /**
     * Name of HTML view that will be presented for the endpoint
     */
    private static final String ENDPOINT_VIEW_NAME = "endpoints";

    /**
     * Name of the variable passed to HTML view
     */
    private static final String ENDPOINT_VIEW_PROPERTY = "endPoints";

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private EndpointLogMetadataProcessor metadataProcessor;

    @GetMapping("/endpoints")
    @EndpointDescription(description = "This view offers search through available endpoints")
    public String listEndpointsLog(Model model) {
        model.addAttribute(ENDPOINT_VIEW_PROPERTY, metadataProcessor.convert(requestMappingHandlerMapping));
        return ENDPOINT_VIEW_NAME;
    }

}
