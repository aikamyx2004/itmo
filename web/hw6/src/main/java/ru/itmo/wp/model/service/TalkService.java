package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.TalkRepository;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.TalkRepositoryImpl;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TalkService {
    UserRepository userRepository = new UserRepositoryImpl();

    TalkRepository talkRepository = new TalkRepositoryImpl();

    public User validateAndFindByTargetUsername(String text, User sourceUser, String targetUsername) throws ValidationException {
        if (Strings.isNullOrEmpty(text) || text.isBlank()) {
            throw new ValidationException("Text can not be empty");
        }

        if (text.trim().length() > 255) {
            throw new ValidationException("Text cant ve longer than 255");
        }

        if (sourceUser == null) {
            throw new ValidationException("Has to be logged");
        }

        if (Strings.isNullOrEmpty(targetUsername) || targetUsername.isBlank()) {
            throw new ValidationException("Target user can not be empty");
        }

        if (targetUsername.length() > 255) {
            throw new ValidationException("Target username cant ve longer than 255");
        }

        User targetUser = userRepository.findByLogin(targetUsername);

        if (targetUser == null) {
            throw new ValidationException("Target user wasn't find");
        }

        if (targetUser.getId() == sourceUser.getId()) {
            throw new ValidationException("Can't send messages to yourself.");
        }

        return targetUser;
    }

    public User findByLoginOrEmail(String loginOrEmail) {
        try {
            return userRepository.findByLogin(loginOrEmail);
        } catch (RepositoryException ignored) {
            // no operations
        }
        try {
            return userRepository.findByEmail(loginOrEmail);
        } catch (RepositoryException e) {
            throw new RepositoryException("User not found by login or email", e);
        }
    }

    public void save(Talk talk) {
        talkRepository.save(talk);
    }

    public List<Map<String, Object>> findAll(User user) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (Talk talk : talkRepository.findAll(user)) {
            User sourceUser = userRepository.find(talk.getSourceUserId());
            User targetUser = userRepository.find(talk.getTargetUserId());
            data.add(Map.of("text", talk.getText(),
                    "sourceUser", sourceUser.getLogin(),
                    "targetUser", targetUser.getLogin(),
                    "creationTime", talk.getCreationTime())
            );
        }
        return data;
    }
}
