package org.dormitory.autobotsoccub.command.keyboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Button {

    REGISTER("Register", "btn_register"),
    UNREGISTER("Unregister", "btn_unregister");

    private String text;
    private String callBackQuery;

    public static Button of(String text, String callBackQuery) {
        return Arrays.stream(values())
                .filter(btn -> btn.text.equals(text) && btn.callBackQuery.equals(callBackQuery) )
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No enum constant for specified values found"));
    }
}
