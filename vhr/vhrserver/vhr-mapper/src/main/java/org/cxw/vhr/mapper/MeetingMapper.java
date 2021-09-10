package org.cxw.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.cxw.vhr.model.Meeting;

import java.util.Date;
import java.util.List;

public interface MeetingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Meeting record);

    int insertSelective(Meeting record);

    Meeting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Meeting record);

    int updateByPrimaryKey(Meeting record);

    List<Meeting> getThisMonthMeetings(@Param("monthStartDate") Date monthStartDate,@Param("monthEndDate") Date monthEndDate);
}