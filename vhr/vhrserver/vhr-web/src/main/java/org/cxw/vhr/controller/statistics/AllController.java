package org.cxw.vhr.controller.statistics;

import org.cxw.vhr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics/all")
public class AllController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/tiptopDegree")
    public List<List> getTiptopDegree() {
        return employeeService.getTiptopDegree();
    }
}
