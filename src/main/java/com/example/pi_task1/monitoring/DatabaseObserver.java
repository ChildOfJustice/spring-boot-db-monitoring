package com.example.pi_task1.monitoring;

public interface DatabaseObserver {

    void subscribe(Subscriber subscriber);

    void unsubscribe(Subscriber subscriber);

    void notifySubscribers();
}
