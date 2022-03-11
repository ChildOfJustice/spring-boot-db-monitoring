package com.example.pi_task1.monitoring.logging;

import com.example.pi_task1.monitoring.tables.TableObserverImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component
@PropertySource({"classpath:myapp.properties"})
public class FileLogger implements DbStateLogger{

    private static final Logger log = LoggerFactory.getLogger(TableObserverImpl.class);

    @Value("${logging.logFile}")
    private String filePath;

    @Override
    public void logState(String message) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(message);
            bufferedWriter.write("\n");
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            log.warn("Got exception in file logger: " + e.getMessage());
        }
    }
}
