package ua.nure.delivery.api.config.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.nure.delivery.entity.User;
import ua.nure.delivery.entity.enums.Role;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.security.Principal;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserPrincipal implements Principal, Serializable {

    private Long id;

    private String phoneNumber;

    private String fullName;

    private String login;

    @JsonIgnore
    private Role role;

    public UserPrincipal(User user) {
        this.id = user.getId();
        this.phoneNumber = user.getPhoneNumber();
        this.fullName = user.getFullName();
        this.login = user.getLogin();
        this.role = user.getRole();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
