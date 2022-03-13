package com.example.pi_task1.monitoring.mediator;

import com.example.pi_task1.monitoring.logging.DbStateLogger;

import java.util.ArrayList;

public interface DatabaseLoggersManager {

    void addLogger(DbStateLogger logger);

    ArrayList<DbStateLogger> getAvailableLoggers();

}
