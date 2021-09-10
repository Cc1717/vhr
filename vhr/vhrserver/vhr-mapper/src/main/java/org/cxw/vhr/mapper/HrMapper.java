package org.cxw.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.cxw.vhr.model.Hr;
import org.cxw.vhr.model.Role;

import java.util.List;

public interface HrMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Hr record);

    int insertSelective(Hr record);

    Hr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Hr record);

    int updateByPrimaryKey(Hr record);

    Hr loadUserByUsername(String username);

    List<Role> getHrRolesById(Integer id);

    List<Hr> getAllHrs(@Param("hrid") Integer hrid,@Param("hrName") String hrName);

    List<Hr> getAllHrsExcludeCurrentHr(Integer hrid);

    Integer updatePwd(@Param("id") Integer id,@Param("newPassword") String newPassword);
}