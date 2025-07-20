package com.altech.electronicstore.assignment.services.deal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DealFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public DealHandler getHandler(String handlerName) {
        try {
            return applicationContext.getBean(handlerName, DealHandler.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("No handler found for name: " + handlerName, e);
        }
    }
}
