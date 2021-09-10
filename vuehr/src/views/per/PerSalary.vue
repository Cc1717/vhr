<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <div>
                <el-input clearable v-model="searchAdjustSalary.name" prefix-icon="el-icon-search"
                          @keydown.enter.native="doSearch"
                          :disabled="nameDisabled" style="width: 180px" placeholder="通过员工姓名搜索...">
                </el-input>
                <el-input clearable v-model="searchAdjustSalary.workID" prefix-icon="el-icon-search"
                          @keydown.enter.native="doSearch"
                          :disabled="workIDDisabled" style="width: 180px;margin-left: 10px" placeholder="通过员工工号搜索...">
                </el-input>
                <el-date-picker
                        v-model="searchAdjustSalary.timeScope"
                        value-format="yyyy-MM-dd"
                        style="margin-left: 10px"
                        type="daterange"
                        range-separator="至"
                        start-placeholder="调薪日期"
                        end-placeholder="调薪日期">
                </el-date-picker>
                <el-button style="margin-left: 10px" icon="el-icon-search" type="primary" @click="doSearch">搜索
                </el-button>
            </div>
            <div>
                <el-button icon="el-icon-plus" type="primary" @click="openAddDialog">添加调薪记录</el-button>
            </div>
        </div>
        <div style="margin-top:10px">
            <el-table
                    v-loading="loading"
                    element-loading-text="正在获取数据..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :header-cell-style="{'text-align':'center'}"
                    :cell-style="{'text-align':'center'}"
                    :data="adjustSalaries"
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
                        width="120"
                        fixed>
                </el-table-column>
                <el-table-column
                        prop="employee.department.name"
                        label="所属部门"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="employee.workID"
                        label="员工工号"
                        width="120">
                </el-table-column>
                <el-table-column
                        label="调薪类型"
                        width="80">
                    <template slot-scope="scope">
                        {{scope.row.type==0?'降薪':'加薪'}}
                    </template>
                </el-table-column>
                <el-table-column
                        prop="content"
                        label="调薪内容"
                        width="80">
                </el-table-column>
                <el-table-column
                        prop="beforeSalary"
                        label="调前薪资"
                        width="80">
                </el-table-column>
                <el-table-column
                        prop="afterSalary"
                        label="调后薪资"
                        width="80">
                </el-table-column>
                <el-table-column
                        prop="asDate"
                        label="调薪日期"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="effectiveDate"
                        label="生效日期"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="reason"
                        label="调薪原因"
                        width="160">
                </el-table-column>
                <el-table-column
                        prop="remark"
                        label="备注"
                        width="160">
                </el-table-column>
                <el-table-column
                        label="操作"
                        width="200">
                    <template slot-scope="scope">
                        <el-button size="mini" @click="openEditDialog(scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="deleteAdjustSalary(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div style="display: flex;justify-content: space-between;margin-top: 10px">
                <el-button disabled type="danger" icon="el-icon-delete">批量删除</el-button>
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
        <el-dialog
                :title="dialogTitle"
                :visible.sync="dialogVisible"
                width="42%">
            <el-form
                    :rules="rules"
                    ref="adjustSalaryForm"
                    :model="adjustSalary">
                <el-row>
                    <el-col :span="12">
                        <el-form-item prop="empName">
                            <el-tag size="normal">员工姓名</el-tag>
                            <el-input disabled style="width: 180px;margin-left: 10px"
                                      v-model="adjustSalary.empName"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item prop="departmentName">
                            <el-tag size="normal">所属部门</el-tag>
                            <el-input disabled style="width: 180px;margin-left: 10px"
                                      v-model="adjustSalary.departmentName"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item prop="workID">
                            <el-tag size="normal">员工工号</el-tag>
                            <el-input :disabled="adjustSalary.id!=null" style="width: 180px;margin-left: 10px"
                                      v-model="adjustSalary.workID"
                                      placeholder="请输入员工工号..." @blur="getEmployee"></el-input>
                        </el-form-item>
                    </el-col>
                    <template v-if="adjustSalary.empName&&adjustSalary.departmentName">
                        <el-col :span="12">
                            <el-form-item prop="content">
                                <el-tag size="normal">调薪内容</el-tag>
                                <el-select :disabled="adjustSalary.id!=null" v-model="adjustSalary.content"
                                           placeholder="请选择调薪内容..."
                                           style="width: 180px;margin-left: 10px" @change="getBeforeSalary">
                                    <el-option label="基本工资" value="基本工资"></el-option>
                                    <el-option label="奖金" value="奖金"></el-option>
                                    <el-option label="午餐补助" value="午餐补助"></el-option>
                                    <el-option label="交通补助" value="交通补助"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </template>
                </el-row>
                <template v-if="adjustSalary.content">
                    <el-row>
                        <el-col :span="12">
                            <el-form-item prop="asDate">
                                <el-tag size="normal">调薪日期</el-tag>
                                <el-date-picker
                                        style="width: 180px;margin-left: 10px"
                                        disabled
                                        v-model="adjustSalary.asDate"
                                        type="date">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item prop="type">
                                <el-tag size="normal">调薪类型</el-tag>
                                <el-select :disabled="effective" v-model="adjustSalary.type" placeholder="请选择调薪类型..."
                                           style="width: 180px;margin-left: 10px">
                                    <el-option label="加薪" :value="1"></el-option>
                                    <el-option label="降薪" :value="0"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">
                            <el-form-item prop="beforeSalary">
                                <el-tag size="normal">调前薪资</el-tag>
                                <el-input disabled style="width: 180px;margin-left: 10px"
                                          v-model="adjustSalary.beforeSalary"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item prop="afterSalary">
                                <el-tag size="normal">调后薪资</el-tag>
                                <el-input :disabled="effective" style="width: 180px;margin-left: 10px"
                                          v-model="adjustSalary.afterSalary"
                                          placeholder="请输入调后薪资..."></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">
                            <el-form-item prop="effectiveDate">
                                <el-tag size="normal">生效日期</el-tag>
                                <el-date-picker
                                        :disabled="effective"
                                        style="width: 180px;margin-left: 10px"
                                        v-model="adjustSalary.effectiveDate"
                                        value-format="yyyy-MM-dd"
                                        type="date"
                                        placeholder="请选择生效日期...">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item prop="reason">
                                <el-tag size="normal">调薪原因</el-tag>
                                <el-input style="width: 180px;margin-left: 10px" v-model="adjustSalary.reason"
                                          placeholder="请输入调薪原因..."></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-form-item prop="remark">
                        <el-tag size="normal">备注</el-tag>
                        <el-input style="width: 180px;margin-left: 10px" v-model="adjustSalary.remark"
                                  placeholder="请输入备注..."></el-input>
                    </el-form-item>
                </template>
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
        name: "PerSalary",
        computed: {
            nameDisabled() {
                return this.searchAdjustSalary.workID != '';
            },
            workIDDisabled() {
                return this.searchAdjustSalary.name != '';
            }
        },
        data() {
            let validateAfterSalary = (rule, value, callback) => {
                if ((this.adjustSalary.type == 0 && this.adjustSalary.beforeSalary <= value) ||
                    (this.adjustSalary.type == 1 && this.adjustSalary.beforeSalary >= value)) {
                    callback(new Error('请输入合适的调后薪资!'));
                } else {
                    callback();
                }
            };
            let validateEffectiveDate = (rule, value, callback) => {
                if (this.adjustSalary.asDate > value) {
                    callback(new Error('请选择合适的生效日期!'));
                } else {
                    callback();
                }
            };
            return {
                loading: false,
                adjustSalaries: [],
                dialogVisible: false,
                dialogTitle: '',
                adjustSalary: null,
                searchAdjustSalary: {
                    name: '',
                    workID: '',
                    timeScope: null
                },
                effective: false,
                rules: {
                    empName: [{required: true, message: '请输入正确的员工工号', trigger: 'change'}],
                    departmentName: [{required: true, message: '请输入正确的员工工号', trigger: 'change'}],
                    workID: [{required: true, message: '请输入员工工号', trigger: 'blur'}],
                    content: [{required: true, message: '请选择调薪内容', trigger: 'change'}],
                    beforeSalary: [{required: true, message: '请检查该员工账套或调薪记录', trigger: 'change'}],
                    type: [{required: true, message: '请选择调薪类型', trigger: 'change'}],
                    afterSalary: [
                        {required: true, message: '请输入调后薪资', trigger: 'blur'},
                        {validator: validateAfterSalary, trigger: 'blur'}
                    ],
                    effectiveDate: [
                        {required: true, message: '请选择生效日期', trigger: 'change'},
                        {validator: validateEffectiveDate, trigger: 'change'}
                    ],
                    reason: [{required: true, message: '请输入调薪原因', trigger: 'blur'}]
                },
                page: 1,
                size: 10,
                total: 0
            }
        },
        mounted() {
            this.initAdjustSalaries();
            this.initAdjustSalary();
        },
        methods: {
            doSearch() {
                this.page = 1;
                this.size = 10;
                this.initAdjustSalaries();
            },
            initAdjustSalaries() {
                this.loading = true;
                let url = '';
                if (this.searchAdjustSalary.name) {
                    url += '&name=' + this.searchAdjustSalary.name;
                }
                if (this.searchAdjustSalary.workID) {
                    url += '&workID=' + this.searchAdjustSalary.workID;
                }
                if (this.searchAdjustSalary.timeScope) {
                    url += '&timeScope=' + this.searchAdjustSalary.timeScope
                }
                this.getRequest('/personnel/salary/?page=' + this.page + '&size=' + this.size + url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.total = resp.total;
                        this.adjustSalaries = resp.data;
                    }
                });
            },
            initAdjustSalary() {
                this.adjustSalary = {
                    eid: null,
                    empName: null,
                    departmentName: null,
                    workID: null,
                    content: null,
                    type: null,
                    beforeSalary: null,
                    afterSalary: null,
                    asDate: this.dateFormat(new Date()),
                    effectiveDate: null,
                    reason: null,
                    remark: null
                };
            },
            deleteAdjustSalary(data) {
                this.$confirm('此操作将永久删除调薪记录【' + data.employee.name + '-' + data.employee.department.name + '-' + data.employee.workID + '-' + data.content + '】, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let today = this.dateFormat(new Date());
                    if (data.effectiveDate < today) {
                        this.$message.error('该调薪记录已生效，无法删除');
                    } else {
                        this.deleteRequest('/personnel/salary/' + data.id).then(resp => {
                            if (resp) {
                                this.initAdjustSalaries();
                            }
                        });
                    }
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            },
            openAddDialog() {
                this.initAdjustSalary();
                this.dialogTitle = '添加调薪记录';
                this.dialogVisible = true;
            },
            openEditDialog(data) {
                this.adjustSalary = Object.assign({}, data);
                this.adjustSalary.empName = data.employee.name;
                this.adjustSalary.departmentName = data.employee.department.name;
                this.adjustSalary.workID = data.employee.workID;
                let today = this.dateFormat(new Date());
                if (this.adjustSalary.effectiveDate < today) {
                    this.effective = true;
                    this.dialogTitle = '修改调薪记录-已生效';
                } else {
                    this.effective = false;
                    this.dialogTitle = '修改调薪记录-未生效';
                }
                this.dialogVisible = true;
            },
            submitForm() {
                this.$refs.adjustSalaryForm.validate((valid) => {
                    if (valid) {
                        this.$message.success('表单校验通过');
                        if (!this.adjustSalary.id) {
                            this.postRequest('/personnel/salary/', this.adjustSalary).then(resp => {
                                if (resp) {
                                    this.initAdjustSalaries();
                                    this.dialogVisible = false;
                                }
                            });
                        } else {
                            this.putRequest('/personnel/salary/', this.adjustSalary).then(resp => {
                                if (resp) {
                                    this.initAdjustSalaries();
                                    this.dialogVisible = false;
                                }
                            });
                        }
                    }
                });
            },
            getEmployee() {
                let workID = this.adjustSalary.workID;
                this.initAdjustSalary();
                this.adjustSalary.workID = workID;
                if (this.adjustSalary.workID) {
                    this.getRequest('/personnel/salary/employee?workID=' + this.adjustSalary.workID).then(resp => {
                        if (resp) {
                            this.adjustSalary.eid = resp.id;
                            this.adjustSalary.empName = resp.name;
                            this.adjustSalary.departmentName = resp.department.name;
                        }
                    });
                }
            },
            getBeforeSalary() {
                this.getRequest('/personnel/salary/beforeSalary?eid=' +
                    this.adjustSalary.eid + '&content=' + this.adjustSalary.content).then(resp => {
                    if (resp) {
                        this.adjustSalary.beforeSalary = resp.object;
                    } else {
                        this.adjustSalary.beforeSalary = null;
                    }
                });
            },
            currentChange() {
                this.initAdjustSalaries();
            },
            sizeChange() {
                this.initAdjustSalaries();
            }
        }
    }
</script>

<style scoped>

</style>