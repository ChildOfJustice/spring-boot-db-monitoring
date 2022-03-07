package com.example.pi_task1.monitoring;

import com.example.pi_task1.logging.FileLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbObserverController implements Subscriber{

    @Autowired
    private FileLogger fileLogger;


    @Override
    public void update() {
        fileLogger.logState();
    }
}
