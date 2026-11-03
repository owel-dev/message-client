package dev.owel.messagesystem.handler;

import dev.owel.messagesystem.dto.Message;
import dev.owel.messagesystem.service.TerminalService;
import dev.owel.messagesystem.util.JsonUtil;
import jakarta.websocket.Session;

public class WebSocketSender {
    private final TerminalService terminalService;

    public WebSocketSender(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    public void sendMessage(Session session, Message message) {
        if (session != null && session.isOpen()) {
            JsonUtil.toJson(message).ifPresent(msg -> {
                try {
                    session.getBasicRemote().sendText(msg);
                } catch (Exception ex) {
                    terminalService.printSystemMessage(
                            String.format("%s send failed. error: %s", msg, ex.getMessage()));
                }
            });
        }
    }
}
