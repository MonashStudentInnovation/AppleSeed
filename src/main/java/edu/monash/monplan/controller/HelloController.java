package edu.monash.monplan.controller;

import edu.monash.monplan.service.DieselService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final DieselService dieselService;

    public HelloController(DieselService dieselService) {
        this.dieselService = dieselService;
    }

    @RequestMapping("/")
    public String index() {
        return "Welcome to the monPlan Springboot API, please refer to the documentation";
    }

}
