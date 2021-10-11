package edu.kpi.fict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FormulaOneProcessorApplication {

    public static void main(String[] args) {

        SpringApplication.run(FormulaOneProcessorApplication.class, args);
    }
}
