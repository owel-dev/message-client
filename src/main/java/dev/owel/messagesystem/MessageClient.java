package dev.owel.messagesystem;

import dev.owel.messagesystem.dto.Message;
import dev.owel.messagesystem.handler.WebSocketMessageHandler;
import dev.owel.messagesystem.handler.WebSocketSender;
import dev.owel.messagesystem.service.TerminalService;
import dev.owel.messagesystem.service.WebSocketService;
import dev.owel.messagesystem.util.JsonUtil;

import java.io.IOException;

public class MessageClient {
    public static void main(String[] args) {
        final String WEBSOCKET_BASE_URL = "localhost:8080";
        final String WEBSOCKET_ENDPOINT = "/ws/v1/message";

        TerminalService terminalService;
        try {
            terminalService = TerminalService.create();
        } catch (IOException ex) {
            System.err.println("Failed to run MessageClient");
            return;
        }
        JsonUtil.setTerminalService(terminalService);

        WebSocketSender webSocketSender = new WebSocketSender(terminalService);
        WebSocketService webSocketService =
                new WebSocketService(terminalService, webSocketSender, WEBSOCKET_BASE_URL, WEBSOCKET_ENDPOINT);
        webSocketService.setWebSocketMessageHandler(new WebSocketMessageHandler(terminalService));

        while (true) {
            String input = terminalService.readLine("Enter message: ");
            if (!input.isEmpty() && input.charAt(0) == '/') {
                String command = input.substring(1);

                boolean exit = switch (command) {
                    case "exit" -> {
                        webSocketService.closeSession();
                        yield true;
                    }
                    case "clear" -> {
                        terminalService.clearTerminal();
                        yield false;
                    }
                    case "connect" -> {
                        webSocketService.createSession();
                        yield false;
                    }
                    default -> false;
                };

                if (exit) {
                    break;
                }
            } else if (!input.isEmpty()) {
                terminalService.printMessage("<me>", input);
                webSocketService.sendMessage(new Message("test client", input));
            }
        }
    }
}