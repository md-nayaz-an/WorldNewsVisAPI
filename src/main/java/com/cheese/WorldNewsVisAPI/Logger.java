package com.cheese.WorldNewsVisAPI;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = "prototype")
public class Logger {
    private String context;
    private long startTime;

    public void start(String context) {
        this.context = context;
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        long endTime = System.currentTimeMillis();

        System.out.println("Log://" + context + ": " + (endTime - startTime) + "ms");
    }
}
