package com.nuance.hackathon.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nuance.hackathon.models.EngagementResponse;
import com.nuance.hackathon.models.Message;
import com.nuance.hackathon.services.AnalyticsService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/analyze/v1")
public class AnalyticsController {

    @Autowired
    AnalyticsService analyticsService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:3000")
    public EngagementResponse sayHello(@RequestParam("engagementId") String engagementId) throws URISyntaxException, JSONException, JsonProcessingException, InterruptedException {
        return  analyticsService.getEngagement(engagementId);
    }
}
