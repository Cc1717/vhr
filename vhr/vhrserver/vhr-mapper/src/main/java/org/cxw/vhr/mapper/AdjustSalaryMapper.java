package org.cxw.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.cxw.vhr.model.AdjustSalary;
import org.cxw.vhr.model.Employee;

import java.util.Date;
import java.util.List;

public interface AdjustSalaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdjustSalary record);

    int insertSelective(AdjustSalary record);

    AdjustSalary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdjustSalary record);

    int updateByPrimaryKey(AdjustSalary record);

    List<AdjustSalary> getAdjustSalariesByPage(@Param("page") Integer page, @Param("size") Integer size,@Param("employee") Employee employee,@Param("timeScope") Date[] timeScope);

    AdjustSalary getNewRecord(@Param("eid") Integer eid, @Param("content") String content);

    Long getTotal(@Param("employee") Employee employee,@Param("timeScope") Date[] timeScope);
}