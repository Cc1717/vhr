<template>
    <div>
        <div style="display:flex;justify-content:space-between;width: 60%">
            <div>
                <el-tag size="normal" style="margin-right:10px">奖惩类型</el-tag>
                <el-select v-model="type" placeholder="请选择奖惩类型..." style="width: 160px" clearable
                           @change="initRps">
                    <el-option label="奖励" value="1"></el-option>
                    <el-option label="惩罚" value="0"></el-option>
                </el-select>
            </div>
            <div>
                <el-tag size="normal" style="margin-right:10px">奖惩方式</el-tag>
                <el-select v-model="way" placeholder="请选择奖惩方式..." style="width: 160px" clearable
                           @change="initRps">
                    <el-option label="积分" value="1"></el-option>
                    <el-option label="数额" value="0"></el-option>
                </el-select>
            </div>
            <el-button type="primary" icon="el-icon-plus" @click="showAddDialog">添加规则</el-button>
        </div>
        <div style="margin-top:10px;width: 80%">
            <el-table
                    v-loading="loading"
                    element-loading-text="正在获取数据..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :header-cell-style="{'text-align':'center'}"
                    :cell-style="{'text-align':'center'}"
                    height="400px"
                    :data="rps"
                    stripe
                    border
                    @selection-change="handleSelectionChange">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="奖惩名称"
                        width="200">
                </el-table-column>
                <el-table-column
                        label="奖惩类型"
                        width="80">
                    <template slot-scope="scope">
                        {{scope.row.type==0?'惩罚':'奖励'}}
                    </template>
                </el-table-column>
                <el-table-column
                        label="奖惩方式"
                        width="80">
                    <template slot-scope="scope">
                        {{scope.row.way==0?'数额':'积分'}}
                    </template>
                </el-table-column>
                <el-table-column
                        prop="amount"
                        label="奖惩数额(元)"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="point"
                        label="奖惩积分(分)"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="createTime"
                        label="启用时间"
                        width="170">
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button type="primary" size="mini" @click="showEditDialog(scope.row)">编辑</el-button>
                        <el-button type="danger" size="mini" @click="deleteRp(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-button :disabled="multipleSelection.length==0" type="danger" style="margin-top:10px"
                       icon="el-icon-delete" @click="multipleDelete">批量删除
            </el-button>
        </div>
        <el-dialog
                :title=dialogTitle
                :visible.sync="dialogVisible"
                width="30%">
            <el-form
                    :rules="rules"
                    ref="rpForm"
                    :model="rp">
                <el-form-item prop="name">
                    <el-tag size="normal">奖惩名称</el-tag>
                    <el-input style="width: 180px;margin-left: 10px" v-model="rp.name"
                              placeholder="请输入奖惩名称..."></el-input>
                </el-form-item>
                <el-form-item prop="type">
                    <el-tag size="normal">奖惩类型</el-tag>
                    <el-select v-model="rp.type" placeholder="请选择奖惩类型..."
                               style="width: 180px;margin-left: 10px">
                        <el-option label="奖励" :value="1"></el-option>
                        <el-option label="惩罚" :value="0"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="way">
                    <el-tag size="normal">奖惩方式</el-tag>
                    <el-select v-model="rp.way" placeholder="请选择奖惩方式..."
                               style="width: 180px;margin-left: 10px">
                        <el-option label="积分" :value="1" @click.native="rp.amount=null"></el-option>
                        <el-option label="数额" :value="0" @click.native="rp.point=null"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="amount" v-if="rp.way==0">
                    <el-tag size="normal">奖惩数额</el-tag>
                    <el-input style="width: 180px;margin-left: 10px" v-model="rp.amount"
                              placeholder="请输入奖惩数额..."></el-input>
                </el-form-item>
                <el-form-item prop="point" v-if="rp.way==1">
                    <el-tag size="normal">奖惩积分</el-tag>
                    <el-input style="width: 180px;margin-left: 10px" v-model="rp.point"
                              placeholder="请输入奖惩积分..."></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "RewardPunishmentManager",
        data() {
            return {
                loading: false,
                type: null,
                way: null,
                rps: [],
                dialogTitle: '',
                dialogVisible: false,
                rp: {
                    name: null,
                    type: null,
                    way: null,
                    amount: null,
                    point: null
                },
                rules: {
                    name: [{required: true, message: '请输入奖惩名称', trigger: 'blur'}],
                    type: [{required: true, message: '请选择奖惩类型', trigger: 'blur'}],
                    way: [{required: true, message: '请选择奖惩方式', trigger: 'blur'}],
                    amount: [{required: true, message: '请输入奖惩数额', trigger: 'blur'}],
                    point: [{required: true, message: '请输入奖惩积分', trigger: 'blur'}]
                },
                multipleSelection: []
            }
        },
        mounted() {
            this.initRps();
        },
        methods: {
            initRps() {
                this.loading = true;
                let url = '';
                if (this.type) {
                    url += 'type=' + this.type + '&';
                }
                if (this.way) {
                    url += 'way=' + this.way;
                }
                this.getRequest('/system/basic/rewardPunishment/?' + url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.rps = resp;
                    }
                });
            },
            showAddDialog() {
                this.dialogTitle = '添加规则';
                this.dialogVisible = true;
                this.rp = {
                    name: null,
                    type: null,
                    way: null,
                    amount: null,
                    point: null
                }
            },
            showEditDialog(data) {
                this.dialogTitle = '修改规则';
                this.dialogVisible = true;
                Object.assign(this.rp, data);
            },
            submitForm() {
                this.$refs.rpForm.validate((valid) => {
                    if (valid) {
                        if (!this.rp.id) {
                            this.postRequest('/system/basic/rewardPunishment/', this.rp).then(resp => {
                                if (resp) {
                                    this.initRps();
                                    this.dialogVisible = false;
                                }
                            });
                        } else {
                            this.putRequest('/system/basic/rewardPunishment/', this.rp).then(resp => {
                                if (resp) {
                                    this.initRps();
                                    this.dialogVisible = false;
                                }
                            });
                        }
                    }
                });
            },
            deleteRp(data) {
                this.$confirm('此操作将永久删除【' + data.name + '】奖惩规则, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/rewardPunishment/' + data.id).then(resp => {
                        if (resp) {
                            this.initRps();
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
            multipleDelete(){
                this.$confirm('此操作将永久删除【' + this.multipleSelection.length + '】条记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let param = '?';
                    this.multipleSelection.forEach(item => {
                        param += 'ids=' + item.id + '&';
                    });
                    this.deleteRequest('/system/basic/rewardPunishment/' + param).then(resp => {
                        if (resp) {
                            this.initRps();
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

<style scoped>

</style>