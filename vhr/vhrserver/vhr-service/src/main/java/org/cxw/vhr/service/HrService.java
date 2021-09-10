package org.cxw.vhr.service;

import org.cxw.vhr.mapper.HrMapper;
import org.cxw.vhr.model.Hr;
import org.cxw.vhr.utils.HrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HrService implements UserDetailsService {
    @Autowired
    HrMapper hrMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Hr hr = hrMapper.loadUserByUsername(username);
        if (hr == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        hr.setRoles(hrMapper.getHrRolesById(hr.getId()));
        return hr;
    }

    public List<Hr> getAllHrs(String hrName) {
        return hrMapper.getAllHrs(HrUtils.getCurrentHr().getId(), hrName);
    }

    public Integer updateHr(Hr hr) {
        return hrMapper.updateByPrimaryKeySelective(hr);
    }

    public Integer deleteHrById(Integer hrid) {
        return hrMapper.deleteByPrimaryKey(hrid);
    }

    public List<Hr> getAllHrsExcludeCurrentHr() {
        return hrMapper.getAllHrsExcludeCurrentHr(HrUtils.getCurrentHr().getId());
    }

    public Hr getMySelf(Integer id) {
        Hr hr = hrMapper.selectByPrimaryKey(id);
        hr.setPassword(null);
        return hr;
    }

    public boolean updateHrPwd(Integer id, String oldPwd, String newPwd) {
        Hr hr = hrMapper.selectByPrimaryKey(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPwd, hr.getPassword())) {
            String newPassword = encoder.encode(newPwd);
            if (hrMapper.updatePwd(id,newPassword) == 1) {
                return true;
            }
        }
        return false;
    }
}
