package org.dormitory.autobotsoccub.context;

import org.dormitory.autobotsoccub.command.Command;
import org.dormitory.autobotsoccub.command.keyboard.KeyboardFactory;
import org.dormitory.autobotsoccub.command.keyboard.ButtonFactory;
import org.dormitory.autobotsoccub.command.querycommand.RegisterCommand;
import org.dormitory.autobotsoccub.command.querycommand.StickyMessageCommand;
import org.dormitory.autobotsoccub.command.querycommand.UnregisterCommand;
import org.dormitory.autobotsoccub.command.txtcommand.HelloCommand;
import org.dormitory.autobotsoccub.command.txtcommand.ShowPoolCommand;
import org.dormitory.autobotsoccub.user.UserPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandContext {

    @Bean
    public ButtonFactory buttonFactory() {
        return new ButtonFactory();
    }

    @Bean
    public KeyboardFactory keyboardFactory(ButtonFactory buttonFactory) {
        return new KeyboardFactory(buttonFactory);
    }

    @Bean
    public Command helloCommand(KeyboardFactory keyboardFactory) {
        return new HelloCommand(keyboardFactory);
    }

    @Bean
    public Command showPoolCommand(KeyboardFactory keyboardFactory, UserPool userPool) {
        return new ShowPoolCommand(keyboardFactory, userPool);
    }

    @Bean
    public Command registerCommand(KeyboardFactory keyboardFactory, UserPool userPool) {
        return new RegisterCommand(keyboardFactory, userPool);
    }

    @Bean
    public Command unregisterCommand(KeyboardFactory keyboardFactory, UserPool userPool) {
        return new UnregisterCommand(keyboardFactory, userPool);
    }

    @Bean
    public Command stickyMessageCommand(UserPool userPool) {
        return new StickyMessageCommand(userPool);
    }
}