package com.example.pi_task1.monitoring;

import com.example.pi_task1.monitoring.logging.FileLogger;
import com.example.pi_task1.monitoring.mediator.DatabaseLoggersManager;
import com.example.pi_task1.monitoring.tables.TableObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//посредник
public class DbObserverSubscriber implements Subscriber{

    @Autowired
    private DatabaseLoggersManager loggersManager;

    public DbObserverSubscriber(TableObserver tableObserver){
        tableObserver.subscribe(this);
    }

    @Override
    public void update(String message) {
        loggersManager.getAvailableLoggers()
            .forEach(
                logger -> logger.logState(message)
            );
    }
}
