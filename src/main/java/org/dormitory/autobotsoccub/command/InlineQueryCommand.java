package org.dormitory.autobotsoccub.command;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Update;

import java.util.Optional;

public interface InlineQueryCommand extends Command {

    String getQueryName();

    @Override
    default boolean accepts(Update update) {
        return Optional.ofNullable(update.getCallbackQuery())
                .map(CallbackQuery::getData)
                .map(queryData -> StringUtils.equals(queryData, getQueryName()))
                .orElse(false);
    }
}