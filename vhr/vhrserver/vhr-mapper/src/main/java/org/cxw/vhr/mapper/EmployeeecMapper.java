package org.cxw.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.cxw.vhr.model.Employee;
import org.cxw.vhr.model.Employeeec;

import java.util.Date;
import java.util.List;

public interface EmployeeecMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employeeec record);

    int insertSelective(Employeeec record);

    Employeeec selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employeeec record);

    int updateByPrimaryKey(Employeeec record);

    Long getTotal(@Param("employee") Employee employee, @Param("timeScope") Date[] timeScope);

    List<Employeeec> getEmployeeecsByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("employee") Employee employee, @Param("timeScope") Date[] timeScope);

    Long getRecord(@Param("employeeec") Employeeec employeeec);
}