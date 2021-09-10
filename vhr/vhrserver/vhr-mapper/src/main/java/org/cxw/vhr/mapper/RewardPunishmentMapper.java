package org.cxw.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.cxw.vhr.model.RewardPunishment;

import java.util.List;

public interface RewardPunishmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RewardPunishment record);

    int insertSelective(RewardPunishment record);

    RewardPunishment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RewardPunishment record);

    int updateByPrimaryKey(RewardPunishment record);

    List<RewardPunishment> getRewardPunishments(@Param("rewardPunishment") RewardPunishment rewardPunishment);

    Integer deleteRewardPunishmentsByIds(@Param("ids") Integer[] ids);
}