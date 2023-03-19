package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserService {
    private static final String PASSWORD_SALT = "1174f9d7bc21e00e9a5fd0a783a44c9a9f73413c";

    private final UserRepository userRepository = new UserRepositoryImpl();

    public void validateRegistration(User user, String password, String passwordConfirmation) throws ValidationException {
        // login
        if (Strings.isNullOrEmpty(user.getLogin())) {
            throw new ValidationException("Login is required");
        }
        if (!user.getLogin().matches("[a-z]+")) {
            throw new ValidationException("Login can contain only lowercase Latin letters");
        }
        if (user.getLogin().length() > 8) {
            throw new ValidationException("Login can't be longer than 8 letters");
        }
        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new ValidationException("Login is already in use");
        }
        // email
        if (Strings.isNullOrEmpty(user.getEmail())) {
            throw new ValidationException("Email is required");
        }
        if (user.getEmail().length() > 255) {
            throw new ValidationException("Email can't be longer than 255 letters");
        }
        if (countAts(user.getEmail()) != 1) {
            throw new ValidationException("Email is incorrect");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ValidationException("Email is already in use");
        }
        // password
        if (Strings.isNullOrEmpty(password)) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < 4) {
            throw new ValidationException("Password can't be shorter than 4 characters");
        }
        if (password.length() > 64) {
            throw new ValidationException("Password can't be longer than 64 characters");
        }
        //passwordConfirmation
        if (Strings.isNullOrEmpty(passwordConfirmation)) {
            throw new ValidationException("Password confirmation is required");
        }
        if (passwordConfirmation.length() > 64) {
            throw new ValidationException("Password confirmation can't be longer than 64 characters");
        }
        if (!password.equals(passwordConfirmation)) {
            throw new ValidationException("Password confirmation and password are not equal");
        }
    }

    private long countAts(String email) {
        return email.chars().filter(c -> c == '@').count();
    }

    public void register(User user, String password) {
        userRepository.save(user, getPasswordSha(password));
    }

    private String getPasswordSha(String password) {
        return Hashing.sha256().hashBytes((PASSWORD_SALT + password).getBytes(StandardCharsets.UTF_8)).toString();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void validateEnter(String loginOrEmail, String password) throws ValidationException {
        User user = userRepository.findByLoginOrEmailAndPasswordSha(loginOrEmail, getPasswordSha(password));
        if (user == null) {
            throw new ValidationException("Invalid login/email or password");
        }
    }

    public User findByLoginOrEmailAndPassword(String login, String password) {
        return userRepository.findByLoginOrEmailAndPasswordSha(login, getPasswordSha(password));
    }

    public long findCount() {
        return userRepository.findCount();
    }
}
