package ru.itmo.wp.web.page;

import java.util.Map;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractPage {
    protected final UserService userService = new UserService();
    private HttpServletRequest request;

    protected void action(HttpServletRequest request, Map<String, Object> view) {
    }

    protected void before(HttpServletRequest request, Map<String, Object> view) {
        this.request = request;
        view.put("userCount", userService.findCount());
        User user = getUser();
        if (user != null) {
            view.put("user", user);
        }
    }

    protected void after(HttpServletRequest request, Map<String, Object> view) {
    }

    protected void setMessage(String message) {
        request.getSession().setAttribute("message", message);
    }

    public User getUser() {
        return (User) request.getSession().getAttribute("user");
    }

    public void setUser(User user) {
        request.getSession().setAttribute("user", user);
    }
}
