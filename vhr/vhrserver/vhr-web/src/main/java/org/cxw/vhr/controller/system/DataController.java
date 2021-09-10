package org.cxw.vhr.controller.system;

import org.cxw.vhr.model.RespBean;
import org.cxw.vhr.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/data")
public class DataController {
    @Autowired
    DataService dataService;

    @GetMapping("/")
    public RespBean backupDatabase() {
        String sqlFilePath = dataService.backupDatabase();
        return RespBean.ok(sqlFilePath);
    }
}
