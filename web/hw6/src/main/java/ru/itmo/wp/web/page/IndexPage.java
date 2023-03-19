package ru.itmo.wp.web.page;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused"})
public class IndexPage extends AbstractPage {
    @Override
    protected void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        String message = (String) request.getSession().getAttribute("message");
        if (!Strings.isNullOrEmpty(message)) {
            view.put("message", message);
            request.getSession().removeAttribute("message");
        }
    }
}
