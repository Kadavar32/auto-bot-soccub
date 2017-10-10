package org.dormitory.autobotsoccub.context;

import net.jodah.expiringmap.ExpiringMap;
import org.dormitory.autobotsoccub.user.RegisterMessagesPool;
import org.dormitory.autobotsoccub.user.UserPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.api.objects.User;

import java.util.Map;

@Configuration
public class PoolContext {

    @Value("${pool.expirationMinutes ?: 15}")
    private int expirationMinutes;

    private ExpiringMap<Integer, User> userPool;

    @Bean
    public UserPool userPool() {
        return new UserPool(expirationMinutes, userPool);
    }
}