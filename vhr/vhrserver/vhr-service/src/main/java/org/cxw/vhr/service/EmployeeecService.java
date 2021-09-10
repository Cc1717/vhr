package org.cxw.vhr.service;

import org.cxw.vhr.Constant;
import org.cxw.vhr.mapper.EmployeeecMapper;
import org.cxw.vhr.model.Employee;
import org.cxw.vhr.model.Employeeec;
import org.cxw.vhr.model.RespBean;
import org.cxw.vhr.model.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeecService {
    @Autowired
    EmployeeecMapper employeeecMapper;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Employeeec getEmployeeecById(Integer id) {
        return employeeecMapper.selectByPrimaryKey(id);
    }

    public RespPageBean getEmployeeecsByPage(Integer page, Integer size, Employee employee, Date[] timeScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Employeeec> data = employeeecMapper.getEmployeeecsByPage(page, size, employee, timeScope);
        Long total = employeeecMapper.getTotal(employee, timeScope);
        return new RespPageBean(total, data);
    }

    public Long getRecord(Employeeec employeeec) {
        Date date = new Date();
        try {
            employeeec.setEcDate(sdf.parse(sdf.format(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return employeeecMapper.getRecord(employeeec);
    }

    public Integer addEmployeeec(Employeeec employeeec) {
        employeeec.setEcDate(new Date());
        return employeeecMapper.insertSelective(employeeec);
    }

    public Integer deleteEmployeeec(Integer id) {
        return employeeecMapper.deleteByPrimaryKey(id);
    }

    public Integer updateEmployeeec(Employeeec employeeec) {
        return employeeecMapper.updateByPrimaryKeySelective(employeeec);
    }

    public Boolean isDeleteOrUpdate(Date ecDate) {
        LocalDate localDate = LocalDate.now();
        LocalDate deleteDate = ecDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (localDate.getDayOfMonth() < Constant.CLOSING_DATE) {
            return !localDate.minusMonths(1).withDayOfMonth(Constant.CLOSING_DATE).isAfter(deleteDate);
        }
        return !localDate.withDayOfMonth(Constant.CLOSING_DATE).isAfter(deleteDate);
    }
}
