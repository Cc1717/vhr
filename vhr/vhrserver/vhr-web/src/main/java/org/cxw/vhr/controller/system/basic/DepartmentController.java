package org.cxw.vhr.controller.system.basic;

import org.cxw.vhr.model.Department;
import org.cxw.vhr.model.RespBean;
import org.cxw.vhr.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    @GetMapping("/")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @PostMapping("/")
    public RespBean addDepartment(@RequestBody Department department) {
        departmentService.addDepartment(department);
        if (department.getResult() == 1) {
            return RespBean.ok("添加成功!", department);
        } else if (department.getResult() == -1) {
            return RespBean.error("部门重复，添加失败!");
        }
        return RespBean.error("添加失败!");
    }

    @DeleteMapping("/{did}")
    public RespBean deleteDepartmentById(@PathVariable Integer did) {
        Department department = new Department();
        department.setId(did);
        departmentService.deleteDepartmentById(department);
        if (department.getResult() == 1) {
            return RespBean.ok("删除成功!");
        } else if (department.getResult() == -2) {
            return RespBean.error("该部门下有子部门，删除失败!");
        } else if (department.getResult() == -1) {
            return RespBean.error("该部门下存在员工，删除失败!");
        }
        return RespBean.error("删除失败!");
    }
}
