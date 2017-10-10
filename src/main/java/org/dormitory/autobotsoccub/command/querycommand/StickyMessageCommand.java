package org.dormitory.autobotsoccub.command.querycommand;

import lombok.AllArgsConstructor;
import org.dormitory.autobotsoccub.command.InlineQueryCommand;
import org.dormitory.autobotsoccub.command.keyboard.Button;
import org.dormitory.autobotsoccub.command.result.CommandResult;
import org.dormitory.autobotsoccub.user.UserPool;
import org.telegram.telegrambots.api.objects.Update;

@AllArgsConstructor
public class StickyMessageCommand implements InlineQueryCommand {

    private static final String STICKY_MESSAGE = "\uD83D\uDC40 The user pool will be reset after %s minutes";

     private UserPool userPool;

    @Override
    public Button getCommandButton() {
        return null;
    }

    @Override
    public CommandResult execute(Update update) {
        return CommandResult.builder()
                .replyMessage(String.format(STICKY_MESSAGE, userPool.getExpiringTime()))
                .build();
    }
}