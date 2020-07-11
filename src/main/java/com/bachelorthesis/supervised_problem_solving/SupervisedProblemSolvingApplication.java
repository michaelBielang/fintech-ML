package com.bachelorthesis.supervised_problem_solving;

import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SupervisedProblemSolvingApplication {

    public static void main(String[] args) {
        // SpringApplication.run(SupervisedProblemSolvingApplication.class, args);
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SupervisedProblemSolvingApplication.class, args);
        Test test = applicationContext.getBean(Test.class);
        test.testRsi();
    }
}
