package org.cxw.vhr.service;

import org.cxw.vhr.mapper.RewardPunishmentMapper;
import org.cxw.vhr.model.RewardPunishment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RewardPunishmentService {
    @Autowired
    RewardPunishmentMapper rewardPunishmentMapper;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public List<RewardPunishment> getRewardPunishments(RewardPunishment rewardPunishment) {
        return rewardPunishmentMapper.getRewardPunishments(rewardPunishment);
    }

    public Integer addRewardPunishment(RewardPunishment rewardPunishment) {
        rewardPunishment.setCreateTime(new Date());
        return rewardPunishmentMapper.insertSelective(rewardPunishment);
    }

    public Integer updateRewardPunishment(RewardPunishment rewardPunishment) {
        return rewardPunishmentMapper.updateByPrimaryKeySelective(rewardPunishment);
    }

    public Integer deleteRewardPunishmentById(Integer id) {
        return rewardPunishmentMapper.deleteByPrimaryKey(id);
    }

    public boolean deleteRewardPunishmentsByIds(Integer[] ids) {
        return rewardPunishmentMapper.deleteRewardPunishmentsByIds(ids) == ids.length;
    }
}
