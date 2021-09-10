package org.cxw.vhr.service;

import org.cxw.vhr.mapper.EmployeeMapper;
import org.cxw.vhr.model.Employee;
import org.cxw.vhr.model.RespPageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public RespPageBean getEmployeesByPage(Integer page, Integer size, Employee employee, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Employee> data = employeeMapper.getEmployeesByPage(page, size, employee, beginDateScope);
        Long total = employeeMapper.getTotal(employee, beginDateScope);
        return new RespPageBean(total, data);
    }

    public Integer addEmployee(Employee employee) {
        Double contractTerm = calculateDate(employee.getBeginContract(), employee.getEndContract());
        employee.setContractTerm(contractTerm);
        Integer result = employeeMapper.insertSelective(employee);
        if (result == 1) {
            Employee emp = new Employee();
            emp.setId(employee.getId());
            emp = employeeMapper.getEmployeesByPage(null, null, emp, null).get(0);
            logger.info("消息发送:" + emp.toString());
            rabbitTemplate.convertAndSend("vhr.employee.welcome", emp);
        }
        return result;
    }

    public Integer maxWorkID() {
        return employeeMapper.maxWorkID();
    }

    public Integer deleteEmployeeById(Integer empId) {
        return employeeMapper.deleteByPrimaryKey(empId);
    }

    public Integer updateEmployee(Employee employee) {
        Double contractTerm = calculateDate(employee.getBeginContract(), employee.getEndContract());
        employee.setContractTerm(contractTerm);
        return employeeMapper.updateByPrimaryKeySelective(employee);
    }

    private Double calculateDate(Date begin, Date end) {
        int months = (Integer.parseInt(yearFormat.format(end)) - Integer.parseInt(yearFormat.format(begin))) * 12;
        months = months + Integer.parseInt(monthFormat.format(end)) - Integer.parseInt(monthFormat.format(begin));
        return Double.parseDouble(decimalFormat.format(months / 12.0));
    }

    public Integer addEmployees(List<Employee> employees) {
        return employeeMapper.addEmployees(employees);
    }

    public RespPageBean getEmployeesWithSalaryByPage(Integer page, Integer size, String empName) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Employee> data = employeeMapper.getEmployeesWithSalaryByPage(page, size, empName);
        Employee employee = new Employee();
        employee.setName(empName);
        Long total = employeeMapper.getTotal(employee, null);
        return new RespPageBean(total, data);
    }

    public Employee getEmployeeByWorkID(String workID) {
        return employeeMapper.getEmployeeByWorkID(workID);
    }

    public List<List> getTiptopDegree() {
        List<List> list=new ArrayList<>();
        List<String> degreeList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        for (Employee employee : employeeMapper.getTiptopDegree()) {
            degreeList.add(employee.getTiptopDegree());
            numberList.add(employee.getNumber());
        }
        list.add(degreeList);
        list.add(numberList);
        return list;
    }
}
