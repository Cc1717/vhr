package org.cxw.vhr.controller.salary;

import org.cxw.vhr.model.RespBean;
import org.cxw.vhr.model.RespPageBean;
import org.cxw.vhr.model.Salary;
import org.cxw.vhr.service.EmpSalaryService;
import org.cxw.vhr.service.EmployeeService;
import org.cxw.vhr.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary/sobcfg")
public class SobcfgController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    SalaryService salaryService;
    @Autowired
    EmpSalaryService empSalaryService;

    @GetMapping("/")
    public RespPageBean getEmployeesWithSalaryByPage(@RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size,
                                                     String empName) {
        return employeeService.getEmployeesWithSalaryByPage(page, size, empName);
    }

    @GetMapping("/salary")
    public List<Salary> getAllSalaries() {
        return salaryService.getAllSalaries();
    }

    @PutMapping("/")
    public RespBean replaceEmpSalary(Integer eid, Integer sid) {
        Integer result = empSalaryService.replaceEmpSalary(eid, sid);
        if (result == 1 || result == 2) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
}
