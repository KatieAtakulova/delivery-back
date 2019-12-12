package ua.nure.delivery.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.nure.delivery.api.config.security.entity.UserPrincipal;
import ua.nure.delivery.entity.enums.Role;

import javax.persistence.EntityNotFoundException;

@Service
public class ValidationService {

    private Repositories repositories;

    @Autowired
    public ValidationService(ListableBeanFactory listableBeanFactory) {
        repositories = new Repositories(listableBeanFactory);
    }

    public void validateExcitement(Long id, Class entityClass, String text, String address) {
        JpaRepository repository = (JpaRepository) repositories.getRepositoryFor(entityClass).get();
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("There is no entity with id " + id);
        }
        if (StringUtils.isEmpty(text) || StringUtils.isEmpty(address)) {
            throw new IllegalArgumentException("Text and address cannot be empty");
        }
    }

    public void validateEntity(Long id, Class entityClass) {
        JpaRepository repository = (JpaRepository) repositories.getRepositoryFor(entityClass).get();
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("There is no entity with id " + id);
        }
    }

    public void validateAdmin() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!principal.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("The user is not an admin");
        }
    }

    public void validateAdminOrModer() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!principal.getRole().equals(Role.ADMIN) && !principal.getRole().equals(Role.MODER)) {
            throw new AccessDeniedException("The user is not an admin or moder");
        }
    }
}
