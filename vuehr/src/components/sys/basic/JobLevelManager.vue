<template>
    <div>
        <div>
            <el-input size="small" v-model="jl.name" style="width:300px;margin-right: 8px" prefix-icon="el-icon-plus"
                      placeholder="添加职称..." @keydown.enter.native="jlAdd"></el-input>
            <el-select v-model="jl.titleLevel" placeholder="职称级别" size="small" style="margin-right: 8px">
                <el-option
                        v-for="item in titleLevels"
                        :key="item"
                        :label="item"
                        :value="item">
                </el-option>
            </el-select>
            <el-button type="primary" icon="el-icon-plus" size="small" @click="jlAdd">添加</el-button>
        </div>
        <div style="margin-top: 10px">
            <el-table
                    v-loading="loading"
                    element-loading-text="正在获取数据..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :header-cell-style="{'text-align':'center'}"
                    :cell-style="{'text-align':'center'}"
                    :data="jls"
                    stripe
                    border
                    size="small"
                    style="width: 80%"
                    @selection-change="handleSelectionChange">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="id"
                        label="编号"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="职称名称"
                        width="140">
                </el-table-column>
                <el-table-column
                        prop="titleLevel"
                        label="职称级别"
                        width="140">
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        label="创建时间"
                        width="180">
                </el-table-column>
                <el-table-column
                        label="是否启用"
                        width="120">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.enabled" type="success" size="small">已启用</el-tag>
                        <el-tag v-else type="danger" size="small">未启用</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                @click="openEditDialog(scope.row)">编辑
                        </el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="jlDelete(scope.row)">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-button icon="el-icon-delete" type="danger" size="small" style="margin-top: 10px" :disabled="multipleSelection.length==0"
                       @click="multipleDelete">
                批量删除
            </el-button>
        </div>
        <el-dialog
                title="修改职称"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <el-tag>编号</el-tag>
                <el-input size="small" style="width:55px;margin-left: 8px" v-model="updateJl.id" disabled></el-input>
            </div>
            <div style="margin-top: 10px">
                <el-tag>职称名称</el-tag>
                <el-input size="small" style="width:200px;margin-left: 8px" v-model="updateJl.name"
                          @keydown.enter.native="jlUpdate"></el-input>
            </div>
            <div style="margin-top: 10px">
                <el-tag>职称级别</el-tag>
                <el-select v-model="updateJl.titleLevel" size="small" style="width:200px;margin-left: 8px">
                    <el-option
                            v-for="item in titleLevels"
                            :key="item"
                            :label="item"
                            :value="item">
                    </el-option>
                </el-select>
            </div>
            <div style="margin-top: 10px">
                <el-tag>是否启用</el-tag>
                <el-switch v-model="updateJl.enabled" style="margin-left: 8px"></el-switch>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false" size="small">取 消</el-button>
                <el-button type="primary" @click="jlUpdate" size="small">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "JobLevelManager",
        data() {
            return {
                loading: false,
                jl: {
                    name: '',
                    titleLevel: ''
                },
                updateJl: {
                    id: '',
                    name: '',
                    titleLevel: '',
                    enabled: false
                },
                dialogVisible: false,
                multipleSelection: [],
                jls: [],
                titleLevels: [
                    '正高级',
                    '副高级',
                    '中级',
                    '初级',
                    '员级'
                ]
            }
        },
        mounted() {
            this.initJls();
        },
        methods: {
            initJls() {
                this.loading = true;
                this.getRequest('/system/basic/joblevel/').then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.jls = resp;
                    }
                });
            },
            jlAdd() {
                if (!this.jl.name) {
                    this.$message.error('职称名称不能为空');
                } else if (!this.jl.titleLevel) {
                    this.$message.error('请选择职称级别');
                } else {
                    this.postRequest('/system/basic/joblevel/', this.jl).then(resp => {
                        if (resp) {
                            this.initJls();
                            this.jl.name = '';
                            this.jl.titleLevel = '';
                        }
                    });
                }
            },
            jlDelete(data) {
                this.$confirm('此操作将永久删除【' + data.name + '】职称, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/joblevel/' + data.id).then(resp => {
                        if (resp) {
                            this.initJls();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            },
            jlUpdate() {
                if (this.updateJl.name) {
                    this.putRequest('/system/basic/joblevel/', this.updateJl).then(resp => {
                        if (resp) {
                            this.initJls();
                            this.dialogVisible = false;
                        }
                    });
                } else {
                    this.$message.error('职称名称不能为空');
                }
            },
            openEditDialog(data) {
                Object.assign(this.updateJl, data);
                this.dialogVisible = true;
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            multipleDelete() {
                this.$confirm('此操作将永久删除【' + this.multipleSelection.length + '】条记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let param = '?';
                    this.multipleSelection.forEach(item => {
                        param += 'ids=' + item.id + '&';
                    });
                    this.deleteRequest('/system/basic/joblevel/' + param).then(resp => {
                        if (resp) {
                            this.initJls();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            }
        }
    }
</script>

<style>

</style>