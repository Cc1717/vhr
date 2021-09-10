<template>
    <div>
        <div>
            <el-input placeholder="请输入角色英文名称..." style="width: 250px;margin-right: 8px" size="small"
                      v-model="role.name">
                <template slot="prepend">ROLE_</template>
            </el-input>
            <el-input placeholder="请输入角色中文名称..." style="width: 200px;margin-right: 8px" size="small"
                      v-model="role.nameZh" @keydown.enter.native="addRole">
            </el-input>
            <el-button type="primary" size="small" icon="el-icon-plus" @click="addRole">添加角色</el-button>
        </div>
        <div class="permissCollapse">
            <el-collapse
                    v-loading="loading"
                    element-loading-text="正在获取数据..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    v-model="activeName"
                    @change="getSelectedMenus"
                    accordion>
                <el-collapse-item v-for="(r,index) in roles" :title="r.nameZh" :name="r.id" :key="index">
                    <el-card class="box-card">
                        <div slot="header" class="clearfix">
                            <span>可访问的资源</span>
                            <el-button style="float: right; padding: 3px 0;color: #ff0000" size="normal"
                                       icon="el-icon-delete" type="text" @click="deleteRole(r)"></el-button>
                        </div>
                        <div>
                            <el-tree ref="tree" :data="allMenus" :props="defaultProps" show-checkbox
                                     node-key="id" :default-checked-keys="selectedKeys" :key="index"></el-tree>
                            <div style="display:flex;justify-content: flex-end;margin-top:5px">
                                <el-button @click="activeName=-1">取消修改</el-button>
                                <el-button type="primary" @click="updatePermiss(r.id,index)">确认修改</el-button>
                            </div>
                        </div>
                    </el-card>
                </el-collapse-item>
            </el-collapse>
        </div>
    </div>
</template>

<script>
    export default {
        name: "PermissManager",
        data() {
            return {
                loading: false,
                role: {
                    name: '',
                    nameZh: ''
                },
                roles: [],
                allMenus: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                activeName: -1,
                selectedKeys: []
            }
        },
        mounted() {
            this.initRoles();
        },
        methods: {
            deleteRole(role) {
                this.$confirm('此操作将永久删除【' + role.nameZh + '】角色, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/permiss/' + role.id).then(resp => {
                        if (resp) {
                            this.initRoles();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            },
            addRole() {
                if (!this.role.name) {
                    this.$message.error('角色英文名称不能为空');
                } else if (!this.role.nameZh) {
                    this.$message.error('角色中文名称不能为空');
                } else {
                    this.postRequest('/system/basic/permiss/', this.role).then(resp => {
                        if (resp) {
                            this.initRoles();
                            this.role.name = '';
                            this.role.nameZh = '';
                        }
                    });
                }
            },
            initRoles() {
                this.loading = true;
                this.getRequest('/system/basic/permiss/roles').then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.roles = resp;
                    }
                });
            },
            initAllMenus() {
                this.getRequest('/system/basic/permiss/menus').then(resp => {
                    if (resp) {
                        this.allMenus = resp;
                    }
                });
            },
            getSelectedMenus(rid) {
                if (rid) {
                    this.initAllMenus();
                    this.getRequest('/system/basic/permiss/mids/' + rid).then(resp => {
                        if (resp) {
                            this.selectedKeys = resp;
                        }
                    });
                }
            },
            updatePermiss(rid, index) {
                let tree = this.$refs.tree[index];
                let checkedMids = tree.getCheckedKeys(true);
                let params = '';
                checkedMids.forEach(item => {
                    params += '&mids=' + item;
                });
                this.putRequest('/system/basic/permiss/?rid=' + rid + params).then(resp => {
                    if (resp) {
                        this.activeName = -1;
                    }
                });
            }
        }
    }
</script>

<style>
    .permissCollapse {
        margin-top: 15px;
        width: 40%
    }
</style>