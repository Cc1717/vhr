package org.cxw.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.cxw.vhr.model.HrRole;

public interface HrRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrRole record);

    int insertSelective(HrRole record);

    HrRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HrRole record);

    int updateByPrimaryKey(HrRole record);

    void deleteByHrId(Integer hrid);

    Integer insertRecord(@Param("hrid") Integer hrid, @Param("rids") Integer[] rids);
}