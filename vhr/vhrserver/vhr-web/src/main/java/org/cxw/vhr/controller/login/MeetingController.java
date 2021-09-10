package org.cxw.vhr.controller.login;

import org.cxw.vhr.model.Meeting;
import org.cxw.vhr.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meeting")
public class MeetingController {
    @Autowired
    MeetingService meetingService;

    @GetMapping("/")
    public List<Meeting> getThisMonthMeetings(){
        return meetingService.getThisMonthMeetings();
    }
}
