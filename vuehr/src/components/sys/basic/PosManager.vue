<template>
    <div>
        <div>
            <el-input
                    size="small"
                    class="addPosInput"
                    placeholder="添加职位..."
                    prefix-icon="el-icon-plus"
                    @keydown.enter.native="positionAdd"
                    v-model="pos.name">
            </el-input>
            <el-button type="primary" icon="el-icon-plus" size="small" @click="positionAdd">添加</el-button>
        </div>
        <div class="posManagerMain">
            <el-table
                    v-loading="loading"
                    element-loading-text="正在获取数据..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :header-cell-style="{'text-align':'center'}"
                    :cell-style="{'text-align':'center'}"
                    :data="positions"
                    stripe
                    border
                    size="small"
                    style="width: 70%"
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
                        label="职位名称"
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
                                @click="positionDelete(scope.row)">删除
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
                title="修改职位"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <el-tag>编号</el-tag>
                <el-input size="small" class="updatePosInput2" v-model="updatePos.id" disabled></el-input>
            </div>
            <div style="margin-top: 15px">
                <el-tag>职位名称</el-tag>
                <el-input size="small" class="updatePosInput" v-model="updatePos.name"
                          @keydown.enter.native="positionEdit"></el-input>
            </div>
            <div style="margin-top: 10px">
                <el-tag>是否启用</el-tag>
                <el-switch v-model="updatePos.enabled" style="margin-left: 8px"></el-switch>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false" size="small">取 消</el-button>
                <el-button type="primary" @click="positionEdit" size="small">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "PosManager",
        data() {
            return {
                loading: false,
                pos: {
                    name: ''
                },
                updatePos: {
                    id: '',
                    name: '',
                    enabled: false
                },
                positions: [],
                dialogVisible: false,
                multipleSelection: []
            }
        },
        mounted() {
            this.initPositions();
        },
        methods: {
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
                    this.deleteRequest('/system/basic/pos/' + param).then(resp => {
                        if (resp) {
                            this.initPositions();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            openEditDialog(data) {
                Object.assign(this.updatePos, data);
                this.dialogVisible = true;
            },
            initPositions() {
                this.loading = true;
                this.getRequest("/system/basic/pos/").then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.positions = resp;
                    }
                });
            },
            positionAdd() {
                if (this.pos.name) {
                    this.postRequest('/system/basic/pos/', this.pos).then(resp => {
                        if (resp) {
                            this.initPositions();
                            this.pos.name = '';
                        }
                    });
                } else {
                    this.$message.error('职位名称不能为空');
                }
            },
            positionEdit() {
                if (this.updatePos.name) {
                    this.putRequest('/system/basic/pos/', this.updatePos).then(resp => {
                        if (resp) {
                            this.initPositions();
                            this.dialogVisible = false;
                        }
                    });
                } else {
                    this.$message.error('职位名称不能为空');
                }
            },
            positionDelete(data) {
                this.$confirm('此操作将永久删除【' + data.name + '】职位, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/pos/' + data.id).then(resp => {
                        if (resp) {
                            this.initPositions();
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
    .addPosInput {
        width: 300px;
        margin-right: 8px
    }

    .posManagerMain {
        margin-top: 10px;
    }

    .updatePosInput {
        margin-left: 8px;
        width: 200px;
    }

    .updatePosInput2 {
        margin-left: 8px;
        width: 55px;
    }
</style>