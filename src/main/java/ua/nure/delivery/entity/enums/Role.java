package ua.nure.delivery.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN, USER, MODER, BLOCKED;

    @Override
    public String getAuthority() {
        return name();
    }
}
