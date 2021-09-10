package org.cxw.vhr.controller.login;

import org.cxw.vhr.Constant;
import org.cxw.vhr.model.Menu;
import org.cxw.vhr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/config")
public class SystemConfigController {
    @Autowired
    MenuService menuService;

    @GetMapping("/menu")
    public List<Menu> getMenusByHrId(){
        return menuService.getMenusByHrId();
    }

    @GetMapping("/closingDate")
    public Integer getClosingDate(){
        return Constant.CLOSING_DATE;
    }
}
