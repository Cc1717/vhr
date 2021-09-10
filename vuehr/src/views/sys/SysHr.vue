<template>
    <div>
        <div class="hrSearch">
            <el-input prefix-icon="el-icon-search" style="width: 300px;margin-right: 10px"
                      placeholder="通过用户名搜索用户..." v-model="hrName" @keydown.enter.native="doSearch"></el-input>
            <el-button icon="el-icon-search" type="primary" @click="doSearch">搜索</el-button>
        </div>
        <div class="hrCards">
            <el-card class="hrCard" v-for="(hr,index) of hrs" :key="index">
                <div slot="header" class="clearfix">
                    <span>{{hr.name}}</span>
                    <el-button style="float: right; padding: 3px 0;color: #ff0000" type="text" icon="el-icon-delete"
                               size="normal" @click="deleteHr(hr)"></el-button>
                </div>
                <div>
                    <div style="display: flex;justify-content: center">
                        <img :src="hr.userface" :alt="hr.name+'头像'" :title="hr.name">
                    </div>
                    <div class="hrCard-text">
                        <div>用户名：{{hr.name}}</div>
                        <div>手机号码：{{hr.phone}}</div>
                        <div>电话号码：{{hr.telephone}}</div>
                        <div>地址：{{hr.address}}</div>
                        <div>用户状态：
                            <el-switch
                                    v-model="hr.enabled"
                                    active-color="#13ce66"
                                    inactive-color="#ff4949"
                                    @change="enabledChange(hr)">
                            </el-switch>
                        </div>
                        <div>
                            用户角色：
                            <el-tag type="success" style="margin-right:5px;margin-bottom: 5px"
                                    v-for="(role,indexj) of hr.roles" :key="indexj">{{role.nameZh}}
                            </el-tag>
                            <el-popover
                                    placement="right"
                                    title="角色列表"
                                    width="200"
                                    trigger="click"
                                    @show="showPop(hr)"
                                    @hide="hidePop(hr)">
                                <el-select v-model="selectedRoles" placeholder="请选择" multiple>
                                    <el-option
                                            v-for="(r,indexk) in allRoles"
                                            :key="indexk"
                                            :label="r.nameZh"
                                            :value="r.id">
                                    </el-option>
                                </el-select>
                                <el-button slot="reference" icon="el-icon-edit" type="text"></el-button>
                            </el-popover>
                        </div>
                        <div>备注：{{hr.remark}}</div>
                    </div>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script>
    export default {
        name: "SysHr",
        data() {
            return {
                hrName: '',
                hrs: [],
                allRoles: [],
                selectedRoles: []
            }
        },
        mounted() {
            this.initHrs();
        },
        methods: {
            deleteHr(hr) {
                this.$confirm('此操作将永久删除【' + hr.name + '】用户, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/hr/' + hr.id).then(resp => {
                        if (resp) {
                            this.initHrs();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            },
            doSearch() {
                this.initHrs();
            },
            showPop(hr) {
                this.initAllRoles();
                this.selectedRoles = [];
                hr.roles.forEach(role => {
                    this.selectedRoles.push(role.id);
                });
            },
            hidePop(hr) {
                let sign = true;
                if (this.selectedRoles.length == hr.roles.length) {
                    let i, j;
                    for (i = 0; i < this.selectedRoles.length; i++) {
                        for (j = 0; j < hr.roles.length; j++) {
                            if (this.selectedRoles[i] == hr.roles[j].id) {
                                break;
                            }
                        }
                        if (j == hr.roles.length) {
                            break;
                        }
                    }
                    if (i == this.selectedRoles.length) {
                        sign = false;
                        this.$message.info('角色没有改变');
                    }
                }
                if (sign) {
                    let params = '';
                    this.selectedRoles.forEach(rid => {
                        params += '&rids=' + rid;
                    });
                    this.putRequest('/system/hr/hrRole?hrid=' + hr.id + params).then(resp => {
                        if (resp) {
                            this.initHrs();
                        }
                    });
                }
            },
            initAllRoles() {
                this.getRequest('/system/hr/roles').then(resp => {
                    if (resp) {
                        this.allRoles = resp;
                    }
                });
            },
            initHrs() {
                this.getRequest('/system/hr/?hrName=' + this.hrName).then(resp => {
                    if (resp) {
                        this.hrs = resp;
                    }
                });
            },
            enabledChange(hr) {
                this.putRequest('/system/hr/', hr).then(resp => {
                    if (resp) {
                        this.initHrs();
                    }
                });
            }
        }
    }
</script>

<style>
    .hrSearch {
        margin-top: 10px;
        margin-bottom: 20px;
        display: flex;
        justify-content: center;
    }

    .hrCards {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-around;
    }

    .hrCard {
        width: 350px;
        margin-bottom: 20px;
    }

    .hrCard img {
        width: 68px;
        height: 68px;
        border-radius: 68px;
    }

    .hrCard-text {
        margin-top: 20px;
        font-size: 12px;
        color: #409eff;
    }

    .hrCard-text div {
        margin-bottom: 5px;
    }
</style>