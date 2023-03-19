package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.Role;
import ru.itmo.wp.domain.Tag;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.form.PostForm;
import ru.itmo.wp.form.UserCredentials;
import ru.itmo.wp.repository.RoleRepository;
import ru.itmo.wp.repository.TagRepository;
import ru.itmo.wp.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    /**
     * @noinspection FieldCanBeLocal, unused
     */
    private final RoleRepository roleRepository;
    private final TagRepository tagRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, TagRepository tagRepository) {
        this.userRepository = userRepository;

        this.roleRepository = roleRepository;
        this.tagRepository = tagRepository;
        for (Role.Name name : Role.Name.values()) {
            if (!roleRepository.existsByName(name)) {
                roleRepository.save(new Role(name));
            }
        }
    }

    public User register(UserCredentials userCredentials) {
        User user = new User();
        user.setLogin(userCredentials.getLogin());
        userRepository.save(user);
        userRepository.updatePasswordSha(user.getId(), userCredentials.getLogin(), userCredentials.getPassword());
        return user;
    }

    public boolean isLoginVacant(String login) {
        return userRepository.countByLogin(login) == 0;
    }

    public User findByLoginAndPassword(String login, String password) {
        return login == null || password == null ? null : userRepository.findByLoginAndPassword(login, password);
    }

    public User findById(Long id) {
        return id == null ? null : userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAllByOrderByIdDesc();
    }

    public void writePost(User user, PostForm postForm) {
        Post post = createPostFromForm(postForm);
        user.addPost(post);
        post.setUser(user);
        userRepository.save(user);
    }

    private Post createPostFromForm(PostForm postForm) {
        Post post = new Post();
        post.setText(postForm.getText());
        post.setTitle(postForm.getTitle());
        post.setTags(new HashSet<>());
        Arrays.stream(postForm.getTags().trim().split("\\s+"))
                .map(tagName -> {
                    Tag tag = tagRepository.findByName(tagName);
                    return tag != null ? tag : new Tag(tagName);
                }).forEach(post::addTag);
        return post;
    }
}
