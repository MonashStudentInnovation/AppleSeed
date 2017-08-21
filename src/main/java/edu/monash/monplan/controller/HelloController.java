package edu.monash.monplan.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Welcome to the monPlan Springboot API, please refer to the documentation";
    }

}
