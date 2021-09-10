<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <div>
                <el-input clearable v-model="empName" :disabled="nameDisabled" prefix-icon="el-icon-search"
                          style="width: 180px;margin-right:10px" @keydown.enter.native="doSearch"
                          placeholder="通过员工姓名搜索..."></el-input>
                <el-input clearable v-model="empWorkID" :disabled="workIDDisabled" prefix-icon="el-icon-search"
                          style="width: 180px;margin-right:10px" placeholder="通过员工工号搜索..."
                          @keydown.enter.native="doSearch"></el-input>
                <el-date-picker
                        style="margin-right:10px;width: 300px"
                        v-model="ecDateScope"
                        type="daterange"
                        value-format="yyyy-MM-dd"
                        range-separator="至"
                        start-placeholder="奖惩时间"
                        end-placeholder="奖惩时间">
                </el-date-picker>
                <el-button icon="el-icon-search" type="primary" @click="doSearch">搜索</el-button>
            </div>
            <el-button icon="el-icon-plus" type="primary" @click="openAddDialog">添加奖惩记录</el-button>
        </div>
        <div style="margin-top: 10px">
            <el-table
                    v-loading="loading"
                    element-loading-text="正在获取数据..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :header-cell-style="{'text-align':'center'}"
                    :cell-style="{'text-align':'center'}"
                    :data="ecs"
                    stripe
                    border
                    style="width: 100%">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="employee.name"
                        label="员工姓名"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="employee.workID"
                        label="员工工号"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="employee.department.name"
                        label="所属部门"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="rewardPunishment.name"
                        label="奖惩名称"
                        width="200">
                </el-table-column>
                <el-table-column
                        label="奖惩类别"
                        width="80">
                    <template slot-scope="scope">
                        {{scope.row.rewardPunishment.type==0?'惩罚':'奖励'}}
                    </template>
                </el-table-column>
                <el-table-column
                        label="奖惩方式"
                        width="80">
                    <template slot-scope="scope">
                        {{scope.row.rewardPunishment.way==0?'数额':'积分'}}
                    </template>
                </el-table-column>
                <el-table-column
                        prop="rewardPunishment.amount"
                        label="奖惩数额"
                        width="80">
                </el-table-column>
                <el-table-column
                        prop="rewardPunishment.point"
                        label="奖惩积分"
                        width="80">
                </el-table-column>
                <el-table-column
                        prop="ecDate"
                        label="奖惩时间"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="remark"
                        label="备注"
                        width="120">
                </el-table-column>
                <el-table-column
                        label="操作"
                        width="200">
                    <template slot-scope="scope">
                        <el-button size="mini" @click="openEditDialog(scope.row)">编辑</el-button>
                        <el-button type="danger" size="mini" @click="deleteEc(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div style="display: flex;justify-content: space-between;margin-top: 10px">
            <el-button type="danger" disabled style="margin-right:10px">批量删除</el-button>
            <div>
                <el-tag size="normal">记录截至日期</el-tag>
                <el-input prefix-icon="el-icon-time" disabled :value="closingDate"
                          style="margin-left:10px;width: 80px"></el-input>
            </div>
            <el-pagination
                    background
                    layout="sizes, prev, pager, next, jumper, ->, total, slot"
                    :total="total"
                    :current-page.sync="page"
                    :page-size.sync="size"
                    @current-change="currentChange"
                    @size-change="sizeChange">
            </el-pagination>
        </div>
        <el-dialog
                :title="dialogTitle"
                :visible.sync="dialogVisible"
                width="42%">
            <el-form
                    :rules="rules"
                    ref="ecForm"
                    :model="ec">
                <el-row>
                    <el-col :span="12">
                        <el-form-item prop="empName">
                            <el-tag size="normal">员工姓名</el-tag>
                            <el-input disabled style="width: 180px;margin-left: 10px"
                                      v-model="ec.empName"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item prop="departmentName">
                            <el-tag size="normal">所属部门</el-tag>
                            <el-input disabled style="width: 180px;margin-left: 10px"
                                      v-model="ec.departmentName"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item prop="workID">
                            <el-tag size="normal">员工工号</el-tag>
                            <el-input :disabled="ec.id!=null" style="width: 180px;margin-left: 10px"
                                      v-model="ec.workID"
                                      placeholder="请输入员工工号..." @blur="getEmployee"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item prop="rpId">
                            <el-tag size="normal">奖惩规则</el-tag>
                            <el-select v-model="ec.rpId" placeholder="请选择奖惩规则..."
                                       style="width:180px;margin-left: 10px">
                                <el-option
                                        v-for="rp in rps"
                                        :key="rp.id"
                                        :label="rp.name"
                                        :value="rp.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <template v-if="ec.id!=null">
                        <el-col :span="12">
                            <el-form-item prop="ecDate">
                                <el-tag size="normal">奖惩时间</el-tag>
                                <el-date-picker
                                        style="margin-left: 10px;width: 180px"
                                        v-model="ec.ecDate"
                                        value-format="yyyy-MM-dd"
                                        type="date"
                                        placeholder="请选择奖惩时间...">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                    </template>
                    <el-col :span="12">
                        <el-form-item prop="remark">
                            <el-tag size="normal">备注</el-tag>
                            <el-input style="width: 180px;margin-left: 10px" v-model="ec.remark"
                                      placeholder="请输入备注..."></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
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
        name: "PerEc",
        data() {
            return {
                dialogTitle: '',
                closingDate: null,
                dialogVisible: false,
                ecs: [],
                ec: null,
                page: 1,
                size: 10,
                total: 0,
                loading: false,
                ecDateScope: null,
                empName: '',
                empWorkID: '',
                rules: {
                    empName: [{required: true, message: '请输入正确的员工工号', trigger: 'change'}],
                    departmentName: [{required: true, message: '请输入正确的员工工号', trigger: 'change'}],
                    workID: [{required: true, message: '请输入员工工号', trigger: 'blur'}],
                    rpId: [{required: true, message: '请选择奖惩规则', trigger: 'change'}],
                    ecDate: [{required: true, message: '请选择奖惩时间', trigger: 'change'}]
                },
                rps: []
            }
        },
        computed: {
            nameDisabled() {
                return this.empWorkID != '';
            },
            workIDDisabled() {
                return this.empName != '';
            }
        },
        mounted() {
            this.initEcs();
            this.initEc();
            this.getRequest('/system/config/closingDate').then(resp => {
                if (resp) {
                    this.closingDate = resp;
                }
            });
        },
        methods: {
            submitForm() {
                this.$refs.ecForm.validate((valid) => {
                    if (valid) {
                        if (!this.ec.id) {
                            this.getRequest('/personnel/ec/record?eid=' + this.ec.eid + '&rpId=' + this.ec.rpId).then(resp => {
                                if (resp) {
                                    this.$confirm('该员工在同一天内已有相同记录,是否继续添加?', '提示', {
                                        confirmButtonText: '确定',
                                        cancelButtonText: '取消',
                                        type: 'warning'
                                    }).then(() => {
                                        this.addEc();
                                    }).catch(() => {
                                        this.$message({
                                            type: 'info',
                                            message: '已取消操作'
                                        });
                                    });
                                } else {
                                    this.addEc();
                                }
                            });
                        } else {
                            this.putRequest('/personnel/ec/', this.ec).then(resp => {
                                if (resp) {
                                    this.initEcs();
                                    this.dialogVisible = false;
                                }
                            });
                        }
                    }
                });
            },
            openAddDialog() {
                this.getRewardPunishment();
                this.dialogTitle = '添加奖惩记录';
                this.dialogVisible = true;
                this.initEc();
            },
            openEditDialog(ec) {
                this.getRewardPunishment();
                this.dialogTitle = '修改奖惩记录';
                this.dialogVisible = true;
                Object.assign(this.ec, ec);
                this.ec.workID = ec.employee.workID;
                this.ec.empName = ec.employee.name;
                this.ec.departmentName = ec.employee.department.name;
            },
            addEc() {
                this.postRequest('/personnel/ec/', this.ec).then(resp => {
                    if (resp) {
                        this.initEcs();
                        this.dialogVisible = false;
                    }
                });
            },
            initEc() {
                this.ec = {
                    eid: null,
                    empName: null,
                    departmentName: null,
                    workID: null,
                    rpId: null,
                    ecDate: null,
                    remark: null
                }
            },
            getRewardPunishment() {
                this.getRequest('/personnel/ec/rewardPunishment').then(resp => {
                    if (resp) {
                        this.rps = resp;
                    }
                });
            },
            getEmployee() {
                let workID = this.ec.workID;
                this.initEc();
                this.ec.workID = workID;
                if (this.ec.workID) {
                    this.getRequest('/personnel/ec/employee?workID=' + this.ec.workID).then(resp => {
                        if (resp) {
                            this.ec.eid = resp.id;
                            this.ec.empName = resp.name;
                            this.ec.departmentName = resp.department.name;
                        }
                    });
                }
            },
            initEcs() {
                this.loading = true;
                let url = '';
                if (this.empName) {
                    url += '&name=' + this.empName;
                }
                if (this.empWorkID) {
                    url += '&workID=' + this.empWorkID;
                }
                if (this.ecDateScope) {
                    url += '&timeScope=' + this.ecDateScope;
                }
                this.getRequest('/personnel/ec/?page=' + this.page + '&size=' + this.size + url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.ecs = resp.data;
                        this.total = resp.total;
                    }
                });
            },
            doSearch() {
                this.page = 1;
                this.size = 10;
                this.initEcs();
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            deleteEc(ec) {
                this.$confirm('此操作将永久删除【' + ec.employee.name + '】的奖惩记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/personnel/ec/', ec).then(resp => {
                        if (resp) {
                            this.initEcs();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            },
            currentChange() {
                this.initEcs();
            },
            sizeChange() {
                this.initEcs();
            }
        }
    }
</script>

<style scoped>

</style>