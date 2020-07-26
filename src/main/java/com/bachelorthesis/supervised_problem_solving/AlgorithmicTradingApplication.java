package com.bachelorthesis.supervised_problem_solving;

import com.bachelorthesis.supervised_problem_solving.frameworks.matlab.MatlabLinearRegression;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AlgorithmicTradingApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(AlgorithmicTradingApplication.class, args);

        final MatlabLinearRegression matlabLinearRegression = applicationContext.getBean(MatlabLinearRegression.class);
        matlabLinearRegression.startMatlabRegressionExperiment();
        SpringApplication.exit(applicationContext);
    }
}
