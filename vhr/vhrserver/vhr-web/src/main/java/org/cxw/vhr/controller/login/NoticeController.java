package org.cxw.vhr.controller.login;

import org.cxw.vhr.model.Notice;
import org.cxw.vhr.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    NoticeService noticeService;

    @GetMapping("/")
    public List<Notice> getThisWeekNotices() {
        return noticeService.getThisWeekNotices();
    }
}
