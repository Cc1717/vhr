package org.cxw.vhr.controller.system.basic;

import org.cxw.vhr.model.RespBean;
import org.cxw.vhr.model.RewardPunishment;
import org.cxw.vhr.service.RewardPunishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/basic/rewardPunishment")
public class RewardPunishmentController {
    @Autowired
    RewardPunishmentService rewardPunishmentService;

    @GetMapping("/")
    public List<RewardPunishment> getRewardPunishments(RewardPunishment rewardPunishment) {
        return rewardPunishmentService.getRewardPunishments(rewardPunishment);
    }

    @PostMapping("/")
    public RespBean addRewardPunishment(@RequestBody RewardPunishment rewardPunishment) {
        if (rewardPunishmentService.addRewardPunishment(rewardPunishment) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PutMapping("/")
    public RespBean updateRewardPunishment(@RequestBody RewardPunishment rewardPunishment) {
        if (rewardPunishmentService.updateRewardPunishment(rewardPunishment) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteRewardPunishmentById(@PathVariable Integer id) {
        if (rewardPunishmentService.deleteRewardPunishmentById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @DeleteMapping("/")
    public RespBean deleteRewardPunishmentsByIds(Integer[] ids) {
        if (rewardPunishmentService.deleteRewardPunishmentsByIds(ids)) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
