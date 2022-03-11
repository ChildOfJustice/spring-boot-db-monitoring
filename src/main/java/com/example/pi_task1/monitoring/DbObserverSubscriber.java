package com.example.pi_task1.monitoring;

import com.example.pi_task1.monitoring.logging.FileLogger;
import com.example.pi_task1.monitoring.tables.TableObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//посредник
public class DbObserverSubscriber implements Subscriber{

    @Autowired
    private FileLogger fileLogger;

    public DbObserverSubscriber(TableObserver tableObserver){
        tableObserver.subscribe(this);
    }

    @Override
    public void update(String message) {
        fileLogger.logState(message);
    }
}
