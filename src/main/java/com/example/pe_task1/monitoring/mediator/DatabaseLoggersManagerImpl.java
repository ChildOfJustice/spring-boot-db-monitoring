package com.example.pe_task1.monitoring.mediator;

import com.example.pe_task1.monitoring.logging.DbStateLogger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DatabaseLoggersManagerImpl implements DatabaseLoggersManager {

    private final ArrayList<DbStateLogger> availableLoggers;

    public DatabaseLoggersManagerImpl(){
        availableLoggers = new ArrayList<>();
    }

    @Override
    public void addLogger(DbStateLogger logger) {
        availableLoggers.add(logger);
    }

    @Override
    public ArrayList<DbStateLogger> getAvailableLoggers() {
        return availableLoggers;
    }
}
