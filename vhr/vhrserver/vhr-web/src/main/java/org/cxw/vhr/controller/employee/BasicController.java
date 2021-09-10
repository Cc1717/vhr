package org.cxw.vhr.controller.employee;

import org.cxw.vhr.model.*;
import org.cxw.vhr.service.*;
import org.cxw.vhr.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/employee/basic")
public class BasicController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    NationService nationService;
    @Autowired
    PoliticsstatusService politicsstatusService;
    @Autowired
    JobLevelService jobLevelService;
    @Autowired
    PositionService positionService;
    @Autowired
    DepartmentService departmentService;

    @GetMapping("/")
    public RespPageBean getEmployeesByPage(@RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           Employee employee, Date[] beginDateScope) {
        return employeeService.getEmployeesByPage(page, size, employee, beginDateScope);
    }

    @GetMapping("/nation")
    public List<Nation> getAllNations() {
        return nationService.getAllNations();
    }

    @GetMapping("/politicsstatus")
    public List<Politicsstatus> getAllPoliticsstatus() {
        return politicsstatusService.getAllPoliticsstatus();
    }

    @GetMapping("/jobLevel")
    public List<JobLevel> getAllJobLevels() {
        return jobLevelService.getAllJobLevels();
    }

    @GetMapping("/position")
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    @GetMapping("/department")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/workID")
    public RespBean maxWorkID() {
        return RespBean.ok(null, String.format("%08d", employeeService.maxWorkID() + 1));
    }

    @PostMapping("/")
    public RespBean addEmployee(@RequestBody Employee employee) {
        if (employeeService.addEmployee(employee) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @DeleteMapping("/{empId}")
    public RespBean deleteEmployeeById(@PathVariable Integer empId) {
        if (employeeService.deleteEmployeeById(empId) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @PutMapping("/")
    public RespBean updateEmployee(@RequestBody Employee employee) {
        if (employeeService.updateEmployee(employee) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() {
        List<Employee> allEmployees = (List<Employee>) employeeService.getEmployeesByPage(null, null, null, null).getData();
        return POIUtils.employeeToExcel(allEmployees);
    }

    @PostMapping("/import")
    public RespBean importData(MultipartFile file) throws IOException {
        List<Employee> employees = POIUtils.excelToEmployee(file, nationService.getAllNations(), politicsstatusService.getAllPoliticsstatus(), departmentService.getAllDeps(), positionService.getAllPositions(), jobLevelService.getAllJobLevels());
        if (employeeService.addEmployees(employees) == employees.size()) {
            return RespBean.ok("导入成功!");
        }
        return RespBean.error("导入失败!");
    }
}
