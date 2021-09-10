<template>
    <div>
        <div style="margin-bottom: 10px">
            <el-input
                    placeholder="通过员工名称进行搜索..."
                    prefix-icon="el-icon-search"
                    style="width: 250px;margin-right: 10px"
                    v-model="empName"
                    @keydown.enter.native="search"
                    clearable
                    @clear="search">
            </el-input>
            <el-button icon="el-icon-search" type="primary" @click="search">搜索</el-button>
        </div>
        <div>
            <el-table
                    v-loading="loading"
                    element-loading-text="正在获取数据..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :data="emps"
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
                        label="姓名"
                        width="140"
                        fixed>
                </el-table-column>
                <el-table-column
                        prop="workID"
                        label="工号"
                        width="140">
                </el-table-column>
                <el-table-column
                        prop="email"
                        label="电子邮件"
                        width="200">
                </el-table-column>
                <el-table-column
                        prop="phone"
                        label="电话号码"
                        width="140">
                </el-table-column>
                <el-table-column
                        prop="department.name"
                        label="所属部门"
                        width="140">
                </el-table-column>
                <el-table-column
                        label="工资账套"
                        width="200">
                    <template slot-scope="scope">
                        <el-tooltip placement="right" v-if="scope.row.salary">
                            <div slot="content">
                                <table>
                                    <tr>
                                        <td>基本工资</td>
                                        <td>{{scope.row.salary.basicSalary}}</td>
                                    </tr>
                                    <tr>
                                        <td>午餐补助</td>
                                        <td>{{scope.row.salary.lunchSalary}}</td>
                                    </tr>
                                    <tr>
                                        <td>交通补助</td>
                                        <td>{{scope.row.salary.trafficSalary}}</td>
                                    </tr>
                                    <tr>
                                        <td>奖金</td>
                                        <td>{{scope.row.salary.bonus}}</td>
                                    </tr>
                                    <tr>
                                        <td>养老金比率</td>
                                        <td>{{scope.row.salary.pensionPer}}</td>
                                    </tr>
                                    <tr>
                                        <td>养老金基数</td>
                                        <td>{{scope.row.salary.pensionBase}}</td>
                                    </tr>
                                    <tr>
                                        <td>医疗保险比率</td>
                                        <td>{{scope.row.salary.medicalPer}}</td>
                                    </tr>
                                    <tr>
                                        <td>医疗保险基数</td>
                                        <td>{{scope.row.salary.medicalBase}}</td>
                                    </tr>
                                    <tr>
                                        <td>公积金比率</td>
                                        <td>{{scope.row.salary.accumulationFundPer}}</td>
                                    </tr>
                                    <tr>
                                        <td>公积金基数</td>
                                        <td>{{scope.row.salary.accumulationFundBase}}</td>
                                    </tr>
                                    <tr>
                                        <td>启用时间</td>
                                        <td>{{scope.row.salary.createDate}}</td>
                                    </tr>
                                </table>
                            </div>
                            <el-tag>{{scope.row.salary.name}}</el-tag>
                        </el-tooltip>
                        <el-tag v-else>暂未设置</el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                        label="操作"
                        width="200">
                    <template slot-scope="scope">
                        <el-popover
                                placement="left"
                                title="修改员工账套"
                                width="200"
                                @show="showPop(scope.row.salary)"
                                @hide="hidePop(scope.row)"
                                trigger="click">
                            <div>
                                <el-select v-model="salaryId" placeholder="请选择账套">
                                    <el-option
                                            v-for="salary in salaries"
                                            :key="salary.id"
                                            :label="salary.name"
                                            :value="salary.id">
                                    </el-option>
                                </el-select>
                            </div>
                            <el-button slot="reference" size="mini" type="danger">
                                修改员工账套
                            </el-button>
                        </el-popover>
                    </template>
                </el-table-column>
            </el-table>
            <div style="display: flex;justify-content: flex-end;margin-top: 10px">
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
        </div>
    </div>
</template>

<script>
    export default {
        name: "SalSobCfg",
        data() {
            return {
                emps: [],
                page: 1,
                size: 10,
                total: 0,
                loading: false,
                empName: '',
                salaries: [],
                salaryId: null
            }
        },
        mounted() {
            this.initEmps();
            this.initSalaries();
        },
        methods: {
            showPop(salary) {
                if (salary) {
                    this.salaryId = salary.id;
                } else {
                    this.salaryId = null;
                }
            },
            hidePop(employee) {
                if (this.salaryId) {
                    if (employee.salary && employee.salary.id == this.salaryId) {
                        this.$message.info('账套未修改');
                        return;
                    }
                    this.putRequest('/salary/sobcfg/?eid=' + employee.id + '&sid=' + this.salaryId).then(resp => {
                        if (resp) {
                            this.initEmps();
                        }
                    });
                }
            },
            search() {
                this.page = 1;
                this.size = 10;
                this.initEmps();
            },
            initEmps() {
                this.loading = true;
                this.getRequest('/salary/sobcfg/?page=' + this.page + '&size=' + this.size + '&empName=' + this.empName).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.emps = resp.data;
                        this.total = resp.total;
                    }
                });
            },
            initSalaries() {
                this.getRequest('/salary/sobcfg/salary').then(resp => {
                    if (resp) {
                        this.salaries = resp;
                    }
                });
            },
            currentChange() {
                this.initEmps();
            },
            sizeChange() {
                this.initEmps();
            }
        }
    }
</script>

<style scoped>

</style>