package org.dormitory.autobotsoccub.user;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.telegram.telegrambots.api.objects.User;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class UserPool{

    private static final int POOL_SIZE = 4;

    private int expirationMinutes;
    private ExpiringMap<Integer, User> userPool;

    @PostConstruct
    public void init() {
        userPool = ExpiringMap.builder()
                .expiration(expirationMinutes, TimeUnit.MINUTES)
                .variableExpiration()
                .maxSize(POOL_SIZE)
                .build();
    }

    public Map<Integer, User> getUserPool() {
        return ImmutableMap.copyOf(userPool);
    }

    public void addUser(User user) {
        if (!isFull() && !containsUser(user.getId())) {
            userPool.put(user.getId(), user, ExpirationPolicy.CREATED);
        }
    }

    public boolean isFull() {
        return userPool.size() == POOL_SIZE;
    }

    public boolean isEmpty() {
        return userPool.size() == 0;
    }

    public boolean containsUser(int userId) {
        return userPool.containsKey(userId);
    }

    public long getExpiringTime() {
        return TimeUnit.MILLISECONDS.toMinutes(userPool.getExpectedExpiration(userPool.entrySet().iterator().next().getKey()));
    }

    public void removeUser(User user) {
        if (!isEmpty() && containsUser(user.getId())) {
            userPool.remove(user.getId());
        }
    }
}