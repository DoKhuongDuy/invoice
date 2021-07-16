package com.viettel.bccs3.config.accesstoken;


import com.viettel.bccs3.domain.dto.ActionUserDTO;
import org.springframework.core.NamedThreadLocal;

public class ActionUserHolder {

    private ActionUserHolder() {
    }

    private static final ThreadLocal<ActionUserDTO> userHolder = new NamedThreadLocal<>("ActionUserHolder");

    public static ActionUserDTO getActionUser() {
        return userHolder.get();
    }

    public static void setActionUser(ActionUserDTO actionUser) {
        ActionUserHolder.userHolder.set(actionUser);
    }

    public static void reset() {
        userHolder.remove();
    }
}
