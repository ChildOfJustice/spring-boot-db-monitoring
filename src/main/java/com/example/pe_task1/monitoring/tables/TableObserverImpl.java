package com.example.pe_task1.monitoring.tables;

import com.example.pe_task1.monitoring.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Component
public class TableObserverImpl implements TableObserver {

    private static final Logger log = LoggerFactory.getLogger(TableObserverImpl.class);

    private final ArrayList<Subscriber> mySubscribers = new ArrayList<>();
    
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        //log.info("The time is now {}", dateFormat.format(new Date()));
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        mySubscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        mySubscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers(String message) {
        mySubscribers.forEach(subscriber -> subscriber.update(message));
    }
}