package ru.javawebinar.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasIdAndEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.web.ExceptionInfoHandler;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class UniqueMailValidator implements Validator {
    private final UserRepository repository;

    @Autowired
    @Nullable  // inmemory tests has no web context
    private HttpServletRequest request;

    public UniqueMailValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        if (StringUtils.hasText(user.getEmail())) {
            User dbUser = repository.getByEmail(user.getEmail().toLowerCase());
            if (dbUser != null) {
                Assert.notNull(request, "HttpServletRequest missed");
                if (request.getMethod().equals("PUT")) {
                    // it is ok, if update ourself
                    int dbId = dbUser.id();
                    if (user.getId() != null && dbId == user.id()) return;

                    // workaround for update with nullable id user
                    String requestURI = request.getRequestURI();
                    if (requestURI.endsWith("/" + dbId) || dbId == SecurityUtil.get().getId()) return;
                }
                errors.rejectValue("email", ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL);
            }
        }
    }
}
