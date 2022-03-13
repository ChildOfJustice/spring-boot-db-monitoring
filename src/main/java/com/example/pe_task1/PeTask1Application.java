package com.example.pe_task1;

import com.example.pe_task1.database.TableObserverInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableScheduling
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class PeTask1Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PeTask1Application.class);

    @Autowired
    TableObserverInitializer tableObserverInitializer;

    public static void main(String args[]) {
        SpringApplication.run(PeTask1Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        log.info("Starting the database monitoring...");
        tableObserverInitializer.initObserver("student", "student_channel");
    }

}
