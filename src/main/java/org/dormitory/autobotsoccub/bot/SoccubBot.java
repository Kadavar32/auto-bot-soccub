package org.dormitory.autobotsoccub.bot;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dormitory.autobotsoccub.command.Command;
import org.dormitory.autobotsoccub.user.RegisterMessagesPool;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

import static org.dormitory.autobotsoccub.command.result.CommandResultConverter.*;

@Slf4j
@AllArgsConstructor
public class SoccubBot extends TelegramLongPollingBot {

    private String name;
    private String token;
    private List<Command> commands;

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Got update: {}", update);

        commands.stream()
                .filter(command -> command.accepts(update))
                .findFirst()
                .map(command -> command.execute(update))
                .map(cmdResult -> buildMessage(cmdResult, update))
                .ifPresent(cmdResult -> tryReply(cmdResult, update));
    }

    private void tryReply(SendMessage sendMessage, Update update) {
        if (!RegisterMessagesPool.isNull()) {
            tryDelete(buildMessage(RegisterMessagesPool.getMessageId(), update));
            trySend(sendMessage);
            sendAndEditStickyMessage(update);
        } else {
            trySend(sendMessage);
            sendAndEditStickyMessage(update);
        }
    }

    private Message trySend(SendMessage message) {
        try {
            return execute(message);
        } catch (TelegramApiException e) {
            log.error("An error occurred while sending response", e);
        }
        return null;
    }

    private void tryDelete(DeleteMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("An error occurred while sending response", e);
        }
    }

    private void sendAndEditStickyMessage(Update update) {
        Message message = trySend(generateStickyMessage(update));
        RegisterMessagesPool.setMessageId(message != null ? message.getMessageId() : null);
    }

    private SendMessage generateStickyMessage(Update update) {
        return commands.stream()
                .filter(command -> StringUtils.equals(command.getClass().getSimpleName(), "StickyMessageCommand"))
                .findFirst()
                .map(command -> command.execute(update))
                .map(cmdResult -> buildMessage(cmdResult, update))
                .orElse(null);
    }
}