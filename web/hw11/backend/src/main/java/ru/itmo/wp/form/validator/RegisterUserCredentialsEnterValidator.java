package ru.itmo.wp.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.form.RegisterUserCredentials;
import ru.itmo.wp.form.UserCredentials;
import ru.itmo.wp.service.UserService;

@Component
public class RegisterUserCredentialsEnterValidator implements Validator {
    private final UserService userService;

    public RegisterUserCredentialsEnterValidator(UserService userService) {
        this.userService = userService;
    }

    public boolean supports(Class<?> clazz) {
        return RegisterUserCredentialsEnterValidator.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            RegisterUserCredentials registerForm = (RegisterUserCredentials) target;
            if (!userService.findByLogin(registerForm.getLogin())) {
                errors.rejectValue("login", "login.is-in-use", "login is in use already");
            }
        }
    }
}
