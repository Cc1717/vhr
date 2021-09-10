package org.cxw.vhr.controller.salary;

import org.cxw.vhr.model.RespBean;
import org.cxw.vhr.model.Salary;
import org.cxw.vhr.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary/sob")
public class SobController {
    @Autowired
    SalaryService salaryService;

    @GetMapping("/")
    public List<Salary> getAllSalaries(){
        return salaryService.getAllSalaries();
    }

    @PostMapping("/")
    public RespBean addSalary(@RequestBody Salary salary){
        if(salaryService.addSalary(salary)==1){
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteSalaryById(@PathVariable Integer id){
        if(salaryService.deleteSalaryById(id)==1){
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @PutMapping("/")
    public RespBean updateSalary(@RequestBody Salary salary){
        if(salaryService.updateSalary(salary)==1){
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
}
