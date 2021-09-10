package org.cxw.vhr.controller.chat;

import org.cxw.vhr.model.Hr;
import org.cxw.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    HrService hrService;

    @GetMapping("/hrs")
    public List<Hr> getAllHrsExcludeCurrentHr(){
        return hrService.getAllHrsExcludeCurrentHr();
    }
}
