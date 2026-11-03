package dev.owel.messagesystem.handler;

import dev.owel.messagesystem.dto.Message;
import dev.owel.messagesystem.service.TerminalService;
import dev.owel.messagesystem.util.JsonUtil;
import jakarta.websocket.MessageHandler;

public class WebSocketMessageHandler implements MessageHandler.Whole<String> {
    private final TerminalService terminalService;

    public WebSocketMessageHandler(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @Override
    public void onMessage(String payload) {
        JsonUtil.fromJson(payload, Message.class).ifPresent(message
                -> terminalService.printMessage(message.username(), message.content()));

    }
}
