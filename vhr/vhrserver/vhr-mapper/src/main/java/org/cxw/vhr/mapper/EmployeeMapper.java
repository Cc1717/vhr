package org.cxw.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.cxw.vhr.model.Employee;

import java.util.Date;
import java.util.List;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employee record);

    int insertSelective(Employee record);

    Employee selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    List<Employee> getEmployeesByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("employee") Employee employee, @Param("beginDateScope") Date[] beginDateScope);

    Long getTotal(@Param("employee") Employee employee, @Param("beginDateScope") Date[] beginDateScope);

    Integer maxWorkID();

    Integer addEmployees(@Param("employees") List<Employee> employees);

    List<Employee> getEmployeesWithSalaryByPage(@Param("page") Integer page,@Param("size") Integer size,@Param("empName") String empName);

    Employee getEmployeeByWorkID(String workID);

    List<Employee> getTiptopDegree();
}