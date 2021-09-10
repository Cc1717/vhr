<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <el-button icon="el-icon-plus" type="primary" @click="openAddDialog">添加工资账套</el-button>
            <el-button icon="el-icon-refresh" type="success" @click="refresh"></el-button>
        </div>
        <div style="margin-top: 10px">
            <el-table
                    :data="salaries"
                    border
                    stripe
                    :header-cell-style="{'text-align':'center'}"
                    :cell-style="{'text-align':'center'}"
                    style="width: 100%">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="账套名称"
                        width="140"
                        fixed>
                </el-table-column>
                <el-table-column
                        prop="basicSalary"
                        label="基本工资"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="bonus"
                        label="奖金"
                        width="70">
                </el-table-column>
                <el-table-column
                        prop="lunchSalary"
                        label="午餐补助"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="trafficSalary"
                        label="交通补助"
                        width="90">
                </el-table-column>
                <el-table-column label="养老金">
                    <el-table-column
                            prop="pensionBase"
                            label="基数"
                            width="70">
                    </el-table-column>
                    <el-table-column
                            prop="pensionPer"
                            label="比率"
                            width="70">
                    </el-table-column>
                </el-table-column>
                <el-table-column label="医疗保险">
                    <el-table-column
                            prop="medicalBase"
                            label="基数"
                            width="70">
                    </el-table-column>
                    <el-table-column
                            prop="medicalPer"
                            label="比率"
                            width="70">
                    </el-table-column>
                </el-table-column>
                <el-table-column label="公积金">
                    <el-table-column
                            prop="accumulationFundBase"
                            label="基数"
                            width="70">
                    </el-table-column>
                    <el-table-column
                            prop="accumulationFundPer"
                            label="比率"
                            width="70">
                    </el-table-column>
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        label="启用时间"
                        width="120">
                </el-table-column>
                <el-table-column
                        label="操作"
                        width="200">
                    <template slot-scope="scope">
                        <el-button size="mini" @click="openEditDialog(scope.row)">编辑</el-button>
                        <el-button type="danger" size="mini" @click="deleteSalary(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-dialog
                :title="dialogTitle"
                :visible.sync="dialogVisible"
                width="50%">
            <div style="display: flex;justify-content: space-around;align-items: center">
                <el-steps direction="vertical" :active="itemIndex">
                    <el-step :title="item" v-for="(item,index) of salaryItems" :key="index"></el-step>
                </el-steps>
                <el-input :placeholder="'请输入'+salaryItems[index]+'...'" v-for="(propertyV,propertyN,index) of salary"
                          :key="index"
                          v-model="salary[propertyN]" style="width: 200px" v-show="index==itemIndex"></el-input>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="preStep" v-show="itemIndex!=0">上一步</el-button>
                <el-button type="primary" @click="nextStep">{{itemIndex==10?'完成':'下一步'}}</el-button>
                <el-button type="danger" @click="dialogVisible=false">取 消</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "SalSob",
        data() {
            return {
                dialogTitle: '',
                salaries: [],
                salary: {
                    basicSalary: null,
                    lunchSalary: null,
                    trafficSalary: null,
                    bonus: null,
                    pensionPer: null,
                    pensionBase: null,
                    medicalPer: null,
                    medicalBase: null,
                    accumulationFundPer: null,
                    accumulationFundBase: null,
                    name: null
                },
                salaryItems: [
                    '基本工资',
                    '午餐补助',
                    '交通补助',
                    '奖金',
                    '养老金比率',
                    '养老金基数',
                    '医疗保险比率',
                    '医疗保险基数',
                    '公积金比率',
                    '公积金基数',
                    '账套名称'
                ],
                itemIndex: 0,
                dialogVisible: false
            }
        },
        mounted() {
            this.initSalaries();
        },
        methods: {
            initSalaries() {
                this.getRequest('/salary/sob/').then(resp => {
                    if (resp) {
                        this.salaries = resp;
                    }
                });
            },
            preStep() {
                this.itemIndex--;
            },
            nextStep() {
                if (this.itemIndex == 10) {
                    if (!this.salary.id) {
                        this.postRequest('/salary/sob/', this.salary).then(resp => {
                            if (resp) {
                                this.dialogVisible = false;
                                this.initSalaries();
                            }
                        });
                    } else {
                        this.putRequest('/salary/sob/', this.salary).then(resp => {
                            if (resp) {
                                this.dialogVisible = false;
                                this.initSalaries();
                            }
                        });
                    }
                } else {
                    this.itemIndex++;
                }
            },
            deleteSalary(data) {
                this.$confirm('此操作将永久删除【' + data.name + '】账套信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/salary/sob/' + data.id).then(resp => {
                        if (resp) {
                            this.initSalaries();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            },
            openAddDialog() {
                this.dialogVisible = true;
                this.dialogTitle = '添加工资账套';
                this.salary = {
                    basicSalary: null,
                    lunchSalary: null,
                    trafficSalary: null,
                    bonus: null,
                    pensionPer: null,
                    pensionBase: null,
                    medicalPer: null,
                    medicalBase: null,
                    accumulationFundPer: null,
                    accumulationFundBase: null,
                    name: null
                };
                this.itemIndex = 0;
            },
            openEditDialog(data) {
                this.dialogVisible = true;
                this.dialogTitle = '修改工资账套';
                this.salary.basicSalary = data.basicSalary;
                this.salary.lunchSalary = data.lunchSalary;
                this.salary.trafficSalary = data.trafficSalary;
                this.salary.bonus = data.bonus;
                this.salary.pensionPer = data.pensionPer;
                this.salary.pensionBase = data.pensionBase;
                this.salary.medicalPer = data.medicalPer;
                this.salary.medicalBase = data.medicalBase;
                this.salary.accumulationFundPer = data.accumulationFundPer;
                this.salary.accumulationFundBase = data.accumulationFundBase;
                this.salary.name = data.name;
                this.salary.id = data.id;
                this.itemIndex = 0;
            },
            refresh() {
                this.$message.success('已刷新');
                this.initSalaries();
            }
        }
    }
</script>

<style scoped>

</style>