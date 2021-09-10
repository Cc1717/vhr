package org.cxw.vhr.service;

import org.cxw.vhr.mapper.MeetingMapper;
import org.cxw.vhr.model.Meeting;
import org.cxw.vhr.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingService {
    @Autowired
    MeetingMapper meetingMapper;

    public List<Meeting> getThisMonthMeetings() {
        return meetingMapper.getThisMonthMeetings(DateUtils.getMonthStartDate(),DateUtils.getMonthEndDate());
    }
}
