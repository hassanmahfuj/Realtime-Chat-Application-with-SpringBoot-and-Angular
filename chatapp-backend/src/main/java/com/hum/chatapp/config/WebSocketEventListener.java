package com.hum.chatapp.config;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

/**
 * WebSocket event listener for handling various WebSocket session events.
 * This component is disabled by commenting out @Component annotation for now.
 */
//@Component
public class WebSocketEventListener {
    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        System.out.println("A client connected");
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        System.out.println("A client disconnected");
    }

    @EventListener
    private void handleSessionSubscribe(SessionSubscribeEvent event) {
        System.out.println("A client Subscribe");
    }

    @EventListener
    private void handleSessionUnsubscribe(SessionUnsubscribeEvent event) {
        System.out.println("A client Unsubscribe");
    }
}
