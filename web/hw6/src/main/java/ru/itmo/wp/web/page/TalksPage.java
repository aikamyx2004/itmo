package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.TalkService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TalksPage extends AbstractPage {
    TalkService talkService = new TalkService();

    @Override
    protected void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        User user = getUser();
        if (user == null) {
            setMessage("You have to be entered");
            throw new RedirectException("/index");
        }
    }

    protected void send(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        String text = request.getParameter("text");
        User sourceUser = (User) request.getSession().getAttribute("user");
        String targetUsername = request.getParameter("targetUser");

        User targetUser = talkService.validateAndFindByTargetUsername(text, sourceUser, targetUsername);

        Talk talk = new Talk();
        talk.setText(text.trim());
        talk.setSourceUserId(sourceUser.getId());
        talk.setTargetUserId(targetUser.getId());

        talkService.save(talk);
    }

    @Override
    protected void after(HttpServletRequest request, Map<String, Object> view) {
        super.after(request, view);
        User user = getUser();
        if (user != null) {
            view.put("data", talkService.findAll(user));
        }
    }
}
