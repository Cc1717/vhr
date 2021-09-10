package org.cxw.vhr.controller.login;

import org.cxw.vhr.model.Hr;
import org.cxw.vhr.model.RespBean;
import org.cxw.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/hr")
public class HrInfoController {
    @Autowired
    HrService hrService;

    @GetMapping("/info")
    public Hr getHrInfo(Authentication authentication) {
        return ((Hr) authentication.getPrincipal());
    }

    @PutMapping("/info")
    public RespBean updateHrInfo(@RequestBody Hr hr, Authentication authentication) {
        if (hrService.updateHr(hr) == 1) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(hr, authentication.getCredentials(), authentication.getAuthorities()));
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @PutMapping("/pwd")
    public RespBean updateHrPwd(@RequestBody Map<String, Object> hrMap) {
        String oldPwd = ((String) hrMap.get("old"));
        String newPwd = ((String) hrMap.get("new"));
        Integer id = ((Integer) hrMap.get("id"));
        if (hrService.updateHrPwd(id, oldPwd, newPwd)) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
}
