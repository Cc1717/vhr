package org.cxw.vhr.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Employeeec {
    private Integer id;

    private Integer eid;

    private Integer rpId;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private Date ecDate;

    private String remark;

    private Employee employee;

    private RewardPunishment rewardPunishment;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public RewardPunishment getRewardPunishment() {
        return rewardPunishment;
    }

    public void setRewardPunishment(RewardPunishment rewardPunishment) {
        this.rewardPunishment = rewardPunishment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Integer getRpId() {
        return rpId;
    }

    public void setRpId(Integer rpId) {
        this.rpId = rpId;
    }

    public Date getEcDate() {
        return ecDate;
    }

    public void setEcDate(Date ecDate) {
        this.ecDate = ecDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}