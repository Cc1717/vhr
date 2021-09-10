package org.cxw.vhr.controller.personnel;

import org.cxw.vhr.model.*;
import org.cxw.vhr.service.EmployeeService;
import org.cxw.vhr.service.EmployeeecService;
import org.cxw.vhr.service.RewardPunishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/personnel/ec")
public class EcController {
    @Autowired
    EmployeeecService employeeecService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    RewardPunishmentService rewardPunishmentService;

    @GetMapping("/")
    public RespPageBean getEmployeeecsByPage(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             Employee employee, Date[] timeScope) {
        return employeeecService.getEmployeeecsByPage(page, size, employee, timeScope);
    }

    @GetMapping("/employee")
    public Employee getEmployeeByWorkID(String workID) {
        return employeeService.getEmployeeByWorkID(workID);
    }

    @GetMapping("/rewardPunishment")
    public List<RewardPunishment> getRewardPunishments() {
        return rewardPunishmentService.getRewardPunishments(null);
    }

    @GetMapping("/record")
    public Long getRecord(Employeeec employeeec) {
        return employeeecService.getRecord(employeeec);
    }

    @PostMapping("/")
    public RespBean addEmployeeec(@RequestBody Employeeec employeeec) {
        if (employeeecService.addEmployeeec(employeeec) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @DeleteMapping("/")
    public RespBean deleteEmployeeec(@RequestBody Employeeec employeeec) {
        if (employeeecService.isDeleteOrUpdate(employeeec.getEcDate())) {
            if (employeeecService.deleteEmployeeec(employeeec.getId()) == 1) {
                return RespBean.ok("删除成功!");
            }
            return RespBean.error("删除失败!");
        }
        return RespBean.error("删除失败!不可删除当月外的奖惩记录!");
    }

    @PutMapping("/")
    public RespBean updateEmployeeec(@RequestBody Employeeec employeeec) {
        Employeeec ec = employeeecService.getEmployeeecById(employeeec.getId());
        if (employeeecService.isDeleteOrUpdate(ec.getEcDate())) {
            if (employeeecService.isDeleteOrUpdate(employeeec.getEcDate())) {
                if (employeeecService.updateEmployeeec(employeeec) == 1) {
                    return RespBean.ok("更新成功!");
                }
                return RespBean.error("更新失败!");
            }
            return RespBean.error("更新失败!不可将奖惩记录修改至当月外!");
        }
        return RespBean.error("更新失败!不可修改当月外的奖惩记录!");
    }
}
