package ru.itmo.wp.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.form.PostForm;
import ru.itmo.wp.form.UserCredentials;
import ru.itmo.wp.service.PostService;
import ru.itmo.wp.service.UserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class WritePostValidation implements Validator {
    public boolean supports(Class<?> clazz) {
        return PostForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            PostForm postForm = (PostForm) target;
            String[] tags = postForm.getTags().trim().split("\\s+");
            if (!Arrays.stream(tags).allMatch(s -> s.matches("[a-z]+"))) {
                errors.rejectValue("tags", "tags.invalid", "tag can have only low latin letters");
            }
            Set<String> setTags = new HashSet<>(Arrays.asList(tags));
            if(setTags.size() != tags.length){
                errors.rejectValue("tags", "tags.invalid", "can't have duplicated tags");
            }
        }
    }
}
