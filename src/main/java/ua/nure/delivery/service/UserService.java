package ua.nure.delivery.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.nure.delivery.api.config.security.entity.UserPrincipal;
import ua.nure.delivery.entity.User;
import ua.nure.delivery.entity.enums.Role;
import ua.nure.delivery.repo.UserRepository;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private ValidationService validationService;

    public UserService(UserRepository userRepository, ValidationService validationService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    public User getCurrentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(principal.getId()).get();
        user.setLogin(null);
        user.setPassword(null);
        user.setRole(null);
        return user;
    }

    public void updateUserInfo(Long id, String fullName, String phone) {

        User user = getNeedFulUser(id);

        if (!StringUtils.isEmpty(fullName)) {
            user.setFullName(fullName);
        }
        if (!StringUtils.isEmpty(phone)) {
            user.setPhoneNumber(phone);
        }

        userRepository.save(user);
    }

    public String getFullNameByLogin(String login) {
        User byLogin = userRepository.findByLogin(login);
        return byLogin != null ? byLogin.getFullName() : null;
    }

    private User getNeedFulUser(Long id) {
        if (id != null) {
            validationService.validateAdmin();
            validationService.validateEntity(id, User.class);
            return userRepository.findById(id).get();
        } else {
            return getCurrentUser();
        }
    }

    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login);
    }

    public void changeRole(Long id, Role newRole) {
        validationService.validateEntity(id, User.class);

        User user = userRepository.findById(id).get();
        user.setRole(newRole);
        userRepository.save(user);
    }

    public List<User> searchByParams(String name, Role role) {

        Specification<User> userSpecification = null;

        if (!StringUtils.isEmpty(name) && role != null) {
            userSpecification = Specification
                    .where((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("fillName"), name));
            userSpecification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"), role));
        } else if (!StringUtils.isEmpty(name) && role == null) {
            userSpecification = Specification
                    .where((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("fillName"), name));
        }
        if (StringUtils.isEmpty(name) && role != null) {
            userSpecification = Specification
                    .where((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"), role));
        }
        return userRepository.findAll(userSpecification);
    }

    public User getUserById(Long id) {
        validationService.validateEntity(id, User.class);
        User user = userRepository.findById(id).get();
        user.setLogin(null);
        user.setPassword(null);
        user.setRole(null);
        return user;
    }
}
