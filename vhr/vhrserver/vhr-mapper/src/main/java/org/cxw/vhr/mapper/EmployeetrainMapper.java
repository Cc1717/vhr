package org.cxw.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.cxw.vhr.model.Employee;
import org.cxw.vhr.model.Employeetrain;

import java.util.Date;
import java.util.List;

public interface EmployeetrainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employeetrain record);

    int insertSelective(Employeetrain record);

    Employeetrain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employeetrain record);

    int updateByPrimaryKey(Employeetrain record);

    List<Employeetrain> getTrainsByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("employee") Employee employee, @Param("timeScope") Date[] timeScope);

    Long getTotal(@Param("employee") Employee employee, @Param("timeScope") Date[] timeScope);

    Integer deleteTrainsByIds(@Param("ids") Integer[] ids);

    Integer addTrains(@Param("trains") List<Employeetrain> trains);
}