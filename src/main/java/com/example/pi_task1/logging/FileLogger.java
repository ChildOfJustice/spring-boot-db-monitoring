package com.example.pi_task1.logging;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileLogger implements DbStateLogger{

    //todo: take this from properties file
    String filePath = "databaseState.andy";

    @Override
    public void logState() {
        File file = new File(filePath);
    }
}
