package com.example.pe_task1.monitoring.tables;

import com.example.pe_task1.monitoring.Subscriber;

public interface TableObserver {

    void subscribe(Subscriber subscriber);

    void unsubscribe(Subscriber subscriber);

    void notifySubscribers(String message);
}
