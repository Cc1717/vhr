package org.cxw.vhr.controller.personnel;

import org.cxw.vhr.model.*;
import org.cxw.vhr.service.AdjustSalaryService;
import org.cxw.vhr.service.EmployeeService;
import org.cxw.vhr.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/personnel/salary")
public class SalaryController {
    @Autowired
    AdjustSalaryService adjustSalaryService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    SalaryService salaryService;

    @GetMapping("/")
    public RespPageBean getAdjustSalariesByPage(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                Employee employee, Date[] timeScope) {
        return adjustSalaryService.getAdjustSalariesByPage(page, size, employee, timeScope);
    }

    @GetMapping("/employee")
    public Employee getEmployeeByWorkID(String workID) {
        return employeeService.getEmployeeByWorkID(workID);
    }

    @GetMapping("/beforeSalary")
    public RespBean getBeforeSalary(Integer eid, String content) {
        RespBean respBean = adjustSalaryService.getNewRecord(eid, content);
        if (respBean == null) {
            Salary salary = salaryService.getSalaryByEid(eid);
            if (salary != null) {
                switch (content) {
                    case "基本工资":
                        respBean = RespBean.ok(null, salary.getBasicSalary());
                        break;
                    case "奖金":
                        respBean = RespBean.ok(null, salary.getBonus());
                        break;
                    case "午餐补助":
                        respBean = RespBean.ok(null, salary.getLunchSalary());
                        break;
                    case "交通补助":
                        respBean = RespBean.ok(null, salary.getTrafficSalary());
                }
            } else {
                respBean = RespBean.error("此员工未设置工资账套!");
            }
        }
        return respBean;
    }

    @PostMapping("/")
    public RespBean addAdjustSalary(@RequestBody AdjustSalary adjustSalary) {
        if (adjustSalaryService.addAdjustSalary(adjustSalary) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteAdjustSalaryById(@PathVariable Integer id) {
        if (adjustSalaryService.deleteAdjustSalaryById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @PutMapping("/")
    public RespBean updateAdjustSalary(@RequestBody AdjustSalary adjustSalary) {
        if (adjustSalaryService.updateAdjustSalary(adjustSalary) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
}
