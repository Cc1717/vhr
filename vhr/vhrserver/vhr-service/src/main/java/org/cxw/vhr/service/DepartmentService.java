package org.cxw.vhr.service;

import org.cxw.vhr.mapper.DepartmentMapper;
import org.cxw.vhr.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;

    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartments(-1);
    }

    public void addDepartment(Department department) {
        department.setEnabled(true);
        departmentMapper.addDepartment(department);
    }

    public void deleteDepartmentById(Department department) {
        departmentMapper.deleteDepartmentById(department);
    }

    public List<Department> getAllDeps() {
        return departmentMapper.getAllDeps();
    }
}
