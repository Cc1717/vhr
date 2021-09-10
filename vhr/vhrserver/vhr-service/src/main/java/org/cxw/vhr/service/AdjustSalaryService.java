package org.cxw.vhr.service;

import org.cxw.vhr.mapper.AdjustSalaryMapper;
import org.cxw.vhr.model.AdjustSalary;
import org.cxw.vhr.model.Employee;
import org.cxw.vhr.model.RespBean;
import org.cxw.vhr.model.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdjustSalaryService {
    @Autowired
    AdjustSalaryMapper adjustSalaryMapper;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public RespPageBean getAdjustSalariesByPage(Integer page, Integer size, Employee employee, Date[] timeScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        Long total = adjustSalaryMapper.getTotal(employee, timeScope);
        List<AdjustSalary> data = adjustSalaryMapper.getAdjustSalariesByPage(page, size, employee, timeScope);
        return new RespPageBean(total, data);
    }

    public RespBean getNewRecord(Integer eid, String content) {
        AdjustSalary adjustSalary = adjustSalaryMapper.getNewRecord(eid, content);
        if (adjustSalary != null) {
            Date effectiveDate = adjustSalary.getEffectiveDate();
            Date date;
            try {
                date = sdf.parse(sdf.format(new Date()));
                if (!effectiveDate.before(date)) {
                    return RespBean.error("当前尚有未生效调薪记录，无法添加!");
                } else {
                    return RespBean.ok(null, adjustSalary.getAfterSalary());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Integer addAdjustSalary(AdjustSalary adjustSalary) {
        return adjustSalaryMapper.insertSelective(adjustSalary);
    }

    public Integer deleteAdjustSalaryById(Integer id) {
        return adjustSalaryMapper.deleteByPrimaryKey(id);
    }

    public Integer updateAdjustSalary(AdjustSalary adjustSalary) {
        return adjustSalaryMapper.updateByPrimaryKeySelective(adjustSalary);
    }
}
