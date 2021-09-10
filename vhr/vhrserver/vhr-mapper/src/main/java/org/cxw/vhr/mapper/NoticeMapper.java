package org.cxw.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.cxw.vhr.model.Notice;

import java.util.Date;
import java.util.List;

public interface NoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Notice record);

    int insertSelective(Notice record);

    Notice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Notice record);

    int updateByPrimaryKey(Notice record);

    List<Notice> getThisWeekNotices(@Param("weekStartDate") Date weekStartDate, @Param("weekEndDate") Date weekEndDate);
}