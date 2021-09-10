package org.cxw.vhr.service;

import org.cxw.vhr.mapper.NoticeMapper;
import org.cxw.vhr.model.Notice;
import org.cxw.vhr.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    @Autowired
    NoticeMapper noticeMapper;

    public List<Notice> getThisWeekNotices() {
        return noticeMapper.getThisWeekNotices(DateUtils.getWeekStartDate(), DateUtils.getWeekEndDate());
    }
}
