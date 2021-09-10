package org.cxw.vhr.service;

import org.cxw.vhr.mapper.MenuRoleMapper;
import org.cxw.vhr.model.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public class MenuRoleService {
    @Autowired
    MenuRoleMapper menuRoleMapper;

    public List<Integer> getMidsByRid(Integer rid) {
        return menuRoleMapper.getMidsByRid(rid);
    }

    @Transactional
    public boolean updateMenuRole(Integer rid, Integer[] mids) {
        menuRoleMapper.deleteByRid(rid);
        if (mids == null || mids.length == 0) {
            return true;
        }
        Integer result = menuRoleMapper.insertRecord(rid, mids);
        return result == mids.length;
    }
}
