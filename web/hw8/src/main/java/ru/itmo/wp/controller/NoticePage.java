package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmo.wp.domain.Notice;
import ru.itmo.wp.service.NoticeService;

import javax.validation.Valid;

@Controller
public class NoticePage extends Page {
    private final NoticeService noticeService;

    public NoticePage(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @RequestMapping("/notice")
    public String loadPage(Model model) {
        model.addAttribute("noticeForm", new Notice());
        return "NoticePage";
    }

    @PostMapping("/notice")
    public String addNotice(@Valid @ModelAttribute("noticeForm") Notice notice, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            noticeService.save(notice.getContent());
        }
        return "NoticePage";
    }
}
