package dev.owel.messagesystem.handler;

import dev.owel.messagesystem.service.TerminalService;
import jakarta.websocket.CloseReason;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;

public class WebSocketSessionHandler extends Endpoint {
    private final TerminalService terminalService;

    public WebSocketSessionHandler(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        terminalService.printSystemMessage("WebSocket connected.");
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        terminalService.printSystemMessage("WebSocket closed. CloseReason: " + closeReason);
    }

    @Override
    public void onError(Session session, Throwable thr) {
        terminalService.printSystemMessage("WebSocket Error: " + thr.getMessage());
    }


}
