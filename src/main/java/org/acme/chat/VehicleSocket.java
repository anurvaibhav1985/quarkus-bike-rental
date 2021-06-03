package org.acme.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.acme.service.Vehicle;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.websocket.Session;

@ServerEndpoint("/vehicleInfo/{username}")         
@ApplicationScoped
public class VehicleSocket {

    Map<String, Session> sessions = new ConcurrentHashMap<>(); 

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessions.put(username, session);
        //broadcast("User " + username + " joined");
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
        //broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
        //broadcast("User " + username + " left on error: " + throwable);
    }

    @Incoming("vehicle-in")
    public void sendToFrontEnd(Vehicle v) {
        //broadcast(">> " + username + ": " + message);
        broadcast(v.getId().toString());
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        //broadcast(">> " + username + ": " + message);
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }

}