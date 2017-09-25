package org.dormitory.autobotsoccub.command.querycommand;

import lombok.AllArgsConstructor;
import org.dormitory.autobotsoccub.command.InlineQueryCommand;
import org.dormitory.autobotsoccub.command.keyboard.KeyboardFactory;
import org.dormitory.autobotsoccub.command.keyboard.Button;
import org.dormitory.autobotsoccub.command.result.CommandResult;
import org.dormitory.autobotsoccub.user.UserPool;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;

import static org.dormitory.autobotsoccub.command.keyboard.Button.*;

@AllArgsConstructor
public class RegisterCommand implements InlineQueryCommand {

    private KeyboardFactory keyboardFactory;

    private UserPool userPool;

    @Override
    public Button getCommandButton() {
        return Button.REGISTER;
    }

    @Override
    public CommandResult execute(Update update) {
        User currentUser = update.getCallbackQuery().getFrom();
        if(userPool.containsUser(currentUser.getId())) {
            return CommandResult.builder()
                    .replyMessage(currentUser.getFirstName() + " you are already registered!")
                    .build();
        }
        userPool.addUser(currentUser);

        if (userPool.isFull()) {
            return CommandResult.builder()
                    .replyMessage(currentUser.getFirstName() + " are registered for upcoming game!\nThe game begins...")
                    .keyboardMarkup(keyboardFactory.keyboardOf(START))
                    .build();
        }
        return CommandResult.builder()
                .replyMessage(currentUser.getFirstName() + " are registered for upcoming game!")
                .build();
    }
}