package org.cxw.vhr.service;

import org.cxw.vhr.mapper.HrRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HrRoleService {
    @Autowired
    HrRoleMapper hrRoleMapper;

    @Transactional
    public boolean updateHrRole(Integer hrid, Integer[] rids) {
        hrRoleMapper.deleteByHrId(hrid);
        if (rids == null || rids.length == 0) {
            return true;
        }
        Integer result = hrRoleMapper.insertRecord(hrid, rids);
        return result == rids.length;
    }
}
