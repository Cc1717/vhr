package org.cxw.vhr.controller.system;

import org.cxw.vhr.model.Hr;
import org.cxw.vhr.model.RespBean;
import org.cxw.vhr.model.Role;
import org.cxw.vhr.service.HrRoleService;
import org.cxw.vhr.service.HrService;
import org.cxw.vhr.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/hr")
public class HrController {
    @Autowired
    HrService hrService;
    @Autowired
    RoleService roleService;
    @Autowired
    HrRoleService hrRoleService;

    @GetMapping("/")
    public List<Hr> getAllHrs(String hrName) {
        return hrService.getAllHrs(hrName);
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/")
    public RespBean updateHr(@RequestBody Hr hr) {
        if (hrService.updateHr(hr) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @PutMapping("/hrRole")
    public RespBean updateHrRole(Integer hrid, Integer[] rids) {
        if (hrRoleService.updateHrRole(hrid, rids)) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @DeleteMapping("/{hrid}")
    public RespBean deleteHrById(@PathVariable Integer hrid) {
        if (hrService.deleteHrById(hrid) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
