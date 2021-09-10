package org.cxw.vhr.service;

import org.cxw.vhr.mapper.EmpSalaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpSalaryService {
    @Autowired
    EmpSalaryMapper empSalaryMapper;

    public Integer replaceEmpSalary(Integer eid, Integer sid) {
        return empSalaryMapper.replaceEmpSalary(eid,sid);
    }
}
