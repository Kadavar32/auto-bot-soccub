package org.dormitory.autobotsoccub.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class RegisterMessagesPool {

    @Getter @Setter private static Integer messageId;
    public static boolean isNull () {return messageId == null;    }
}