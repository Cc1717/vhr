package org.cxw.vhr.service;

import org.cxw.vhr.mapper.EmployeetrainMapper;
import org.cxw.vhr.model.Employee;
import org.cxw.vhr.model.Employeetrain;
import org.cxw.vhr.model.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EmployeetrainService {
    @Autowired
    EmployeetrainMapper employeetrainMapper;

    public RespPageBean getTrainsByPage(Integer page, Integer size, Employee employee, Date[] timeScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Employeetrain> data = employeetrainMapper.getTrainsByPage(page, size, employee, timeScope);
        Long total = employeetrainMapper.getTotal(employee, timeScope);
        return new RespPageBean(total, data);
    }

    public boolean deleteTrainsByIds(Integer[] ids) {
        return employeetrainMapper.deleteTrainsByIds(ids) == ids.length;
    }

    public Integer deleteTrainById(Integer id) {
        return employeetrainMapper.deleteByPrimaryKey(id);
    }

    public Integer addTrains(List<Employeetrain> trains) {
        return employeetrainMapper.addTrains(trains);
    }
}
