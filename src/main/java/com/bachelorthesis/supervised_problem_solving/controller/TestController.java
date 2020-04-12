package com.bachelorthesis.supervised_problem_solving.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "hello world";
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "hello world";
    }

/*    @GetMapping("/logout")
    public String logout() {

        Runnable runnable = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SpringApplication.exit(applicationContext);
        };
        runnable.run();

        return "logout";
    }*/
}
