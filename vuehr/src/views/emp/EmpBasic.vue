<template>
    <div>
        <div>
            <div style="display: flex;justify-content: space-between">
                <div>
                    <el-input prefix-icon="el-icon-search"
                              style="width: 300px;margin-right: 10px"
                              placeholder="通过员工名搜索员工..."
                              v-model="empName"
                              @keydown.enter.native="searchTwo"
                              clearable
                              @clear="searchTwo"
                              :disabled="showSearch"></el-input>
                    <el-button icon="el-icon-search" type="primary" @click="searchTwo" :disabled="showSearch">搜索
                    </el-button>
                    <el-button type="primary" @click="showSearch?advanceCancel():advanceSearch()">
                        <i :class="showSearch?'fa fa-angle-double-up':'fa fa-angle-double-down'"
                           style="margin-right: 5px"></i>
                        高级搜索
                    </el-button>
                </div>
                <div>
                    <el-upload
                            :show-file-list="false"
                            :before-upload="beforeUpload"
                            :on-success="onSuccess"
                            :on-error="onError"
                            :disabled="importDataDisabled"
                            action="/employee/basic/import"
                            style="display: inline-flex;margin-right:10px">
                        <el-button type="success" :icon="importDataBtnIcon">{{importDataBtnText}}</el-button>
                    </el-upload>
                    <el-button type="success" :disabled="importDataDisabled" @click="exportData"
                               icon="el-icon-download">
                        导出数据
                    </el-button>
                    <el-button icon="el-icon-plus" type="primary" @click="openAddDialog">添加员工</el-button>
                </div>
            </div>
            <transition name="slide-fade">
                <div v-show="showSearch"
                     style="border:1px solid #409eff;border-radius: 5px;margin:10px 0px;padding: 10px;">
                    <el-row style="margin-bottom: 15px">
                        <el-col :span="5">
                            <el-tag style="margin-right: 5px" size="normal">民族</el-tag>
                            <el-select v-model="searchEmp.nationId" placeholder="请选择民族..." style="width: 140px">
                                <el-option
                                        v-for="nation in allNations"
                                        :key="nation.id"
                                        :label="nation.name"
                                        :value="nation.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="5">
                            <el-tag style="margin-right: 5px" size="normal">职位</el-tag>
                            <el-select v-model="searchEmp.posId" placeholder="请选择职位..." style="width: 140px">
                                <el-option
                                        v-for="position in allPositions"
                                        :key="position.id"
                                        :label="position.name"
                                        :value="position.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="5">
                            <el-tag style="margin-right: 5px" size="normal">职称</el-tag>
                            <el-select v-model="searchEmp.jobLevelId" placeholder="请选择职称..." style="width: 140px">
                                <el-option
                                        v-for="jobLevel in allJobLevels"
                                        :key="jobLevel.id"
                                        :label="jobLevel.name"
                                        :value="jobLevel.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="6">
                            <el-tag style="margin-right: 5px" size="normal">政治面貌</el-tag>
                            <el-select v-model="searchEmp.politicId" placeholder="请选择政治面貌..." style="width: 160px">
                                <el-option
                                        v-for="politic in allPolitics"
                                        :key="politic.id"
                                        :label="politic.name"
                                        :value="politic.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="3">
                            <el-button type="primary" icon="el-icon-search" style="float: right"
                                       @click="searchOne">搜索
                            </el-button>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-tag style="margin-right: 5px" size="normal">所属部门</el-tag>
                            <el-popover
                                    placement="right"
                                    title="请选择部门"
                                    width="250"
                                    trigger="manual"
                                    v-model="popVisible2">
                                <el-tree :data="allDepartments" :props="defaultProps"
                                         @node-click="handleNodeClick2" :default-expand-all="true"></el-tree>
                                <div slot="reference" class="departmentDiv" @click="popVisible2 = !popVisible2">
                                    {{departmentName2}}
                                </div>
                            </el-popover>
                        </el-col>
                        <el-col :span="10">
                            <el-tag style="margin-right: 5px" size="normal">入职日期</el-tag>
                            <el-date-picker
                                    v-model="searchEmp.beginDateScope"
                                    type="daterange"
                                    value-format="yyyy-MM-dd"
                                    :unlink-panels="true"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                            </el-date-picker>
                        </el-col>
                        <el-col :span="6">
                            <el-tag style="margin-right: 5px" size="normal">聘用形式</el-tag>
                            <el-radio-group v-model="searchEmp.engageForm">
                                <el-radio label="劳动合同" style="margin-right: 20px">劳动合同</el-radio>
                                <el-radio label="劳务合同">劳务合同</el-radio>
                            </el-radio-group>
                        </el-col>
                        <el-col :span="2">
                            <el-button type="danger" style="float: right" @click="advanceCancel">取 消
                            </el-button>
                        </el-col>
                    </el-row>
                </div>
            </transition>
        </div>
        <div style="margin-top: 10px;text-align: center">
            <el-table
                    v-loading="loading"
                    element-loading-text="正在获取数据..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :data="emps"
                    stripe
                    border
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
                        width="110"
                        fixed>
                </el-table-column>
                <el-table-column
                        prop="workID"
                        label="工号"
                        width="110">
                </el-table-column>
                <el-table-column
                        prop="workState"
                        label="在职状态"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="gender"
                        label="性别"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="birthday"
                        label="出生日期"
                        width="110">
                </el-table-column>
                <el-table-column
                        prop="idCard"
                        label="身份证号码"
                        width="160">
                </el-table-column>
                <el-table-column
                        prop="wedlock"
                        label="婚姻状况"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="nation.name"
                        label="民族"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="nativePlace"
                        label="籍贯"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="politicsstatus.name"
                        label="政治面貌"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="email"
                        label="电子邮箱"
                        width="160">
                </el-table-column>
                <el-table-column
                        prop="phone"
                        label="电话号码"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="address"
                        label="联系地址"
                        width="220">
                </el-table-column>
                <el-table-column
                        prop="department.name"
                        label="所属部门"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="position.name"
                        label="职位"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="jobLevel.name"
                        label="职称"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="engageForm"
                        label="聘用形式"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="tiptopDegree"
                        label="最高学历"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="school"
                        label="毕业院校"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="specialty"
                        label="专业名称"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="beginDate"
                        label="入职日期"
                        width="110">
                </el-table-column>
                <el-table-column
                        prop="conversionTime"
                        label="转正日期"
                        width="110">
                </el-table-column>
                <el-table-column
                        prop="beginContract"
                        label="合同起始日期"
                        width="110">
                </el-table-column>
                <el-table-column
                        prop="endContract"
                        label="合同终止日期"
                        width="110">
                </el-table-column>
                <el-table-column
                        label="合同期限"
                        width="90">
                    <template slot-scope="scope">
                        <el-tag>{{scope.row.contractTerm}}年</el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                        label="操作"
                        width="160"
                        fixed="right">
                    <template slot-scope="scope">
                        <el-button style="padding: 5px" @click="openEditDialog(scope.row)">编辑</el-button>
                        <el-button style="padding: 5px" type="danger" @click="deleteEmployee(scope.row)">删除</el-button>
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
        <el-dialog
                :title="title"
                :visible.sync="dialogVisible"
                width="80%">
            <div>
                <el-form :model="emp" :rules="rules" ref="employeeForm">
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="姓名:" prop="name">
                                <el-input size="mini" prefix-icon="el-icon-edit" v-model="emp.name" style="width: 150px"
                                          placeholder="请输入员工姓名"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="性别:" prop="gender" style="display:flex;align-items: center">
                                <el-radio-group v-model="emp.gender">
                                    <el-radio label="男">男</el-radio>
                                    <el-radio label="女">女</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="出生日期:" prop="birthday">
                                <el-date-picker
                                        v-model="emp.birthday"
                                        value-format="yyyy-MM-dd"
                                        type="date"
                                        placeholder="出生日期"
                                        size="mini"
                                        style="width: 150px">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="政治面貌:" prop="politicId">
                                <el-select v-model="emp.politicId" placeholder="政治面貌" size="mini" style="width: 200px">
                                    <el-option
                                            v-for="politic in allPolitics"
                                            :key="politic.id"
                                            :label="politic.name"
                                            :value="politic.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="民族:" prop="nationId">
                                <el-select v-model="emp.nationId" placeholder="请选择民族" size="mini" style="width: 150px">
                                    <el-option
                                            v-for="nation in allNations"
                                            :key="nation.id"
                                            :label="nation.name"
                                            :value="nation.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="籍贯:" prop="nativePlace">
                                <el-input size="mini" prefix-icon="el-icon-edit" v-model="emp.nativePlace"
                                          style="width: 120px"
                                          placeholder="员工籍贯"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="电子邮箱:" prop="email">
                                <el-input size="mini" prefix-icon="el-icon-message" v-model="emp.email"
                                          style="width: 150px"
                                          placeholder="电子邮箱地址..."></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="联系地址:" prop="address">
                                <el-input size="mini" prefix-icon="el-icon-edit" v-model="emp.address"
                                          style="width: 200px"
                                          placeholder="联系地址..."></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="职位:" prop="posId">
                                <el-select v-model="emp.posId" placeholder="请选择职位" size="mini" style="width: 150px">
                                    <el-option
                                            v-for="position in allPositions"
                                            :key="position.id"
                                            :label="position.name"
                                            :value="position.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="职称:" prop="jobLevelId">
                                <el-select v-model="emp.jobLevelId" placeholder="请选择职称" size="mini"
                                           style="width: 120px">
                                    <el-option
                                            v-for="jobLevel in allJobLevels"
                                            :key="jobLevel.id"
                                            :label="jobLevel.name"
                                            :value="jobLevel.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="所属部门:" prop="departmentId">
                                <el-popover
                                        placement="right"
                                        title="请选择部门"
                                        width="250"
                                        trigger="manual"
                                        v-model="popVisible">
                                    <el-tree :data="allDepartments" :props="defaultProps"
                                             @node-click="handleNodeClick" :default-expand-all="true"></el-tree>
                                    <div slot="reference" class="departmentDiv" @click="popVisible = !popVisible">
                                        {{departmentName}}
                                    </div>
                                </el-popover>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="电话号码:" prop="phone">
                                <el-input size="mini" v-model="emp.phone" style="width: 200px"
                                          prefix-icon="el-icon-phone"
                                          placeholder="电话号码..."></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="工号:" prop="workID">
                                <el-input size="mini" v-model="emp.workID" style="width: 150px"
                                          disabled placeholder="员工工号"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="最高学历:" prop="tiptopDegree">
                                <el-select v-model="emp.tiptopDegree" placeholder="最高学历" size="mini"
                                           style="width: 120px">
                                    <el-option
                                            v-for="(degree,indexd) in allDegrees"
                                            :key="indexd"
                                            :label="degree"
                                            :value="degree">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="毕业院校:" prop="school">
                                <el-input size="mini" v-model="emp.school" style="width: 150px"
                                          prefix-icon="el-icon-edit" placeholder="请输入毕业院校..."></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="专业名称:" prop="specialty">
                                <el-input size="mini" v-model="emp.specialty" style="width: 200px"
                                          prefix-icon="el-icon-edit" placeholder="请输入专业名称..."></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="入职日期:" prop="beginDate">
                                <el-date-picker
                                        v-model="emp.beginDate"
                                        value-format="yyyy-MM-dd"
                                        type="date"
                                        placeholder="入职日期"
                                        size="mini"
                                        style="width: 150px">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="转正日期:" prop="conversionTime">
                                <el-date-picker
                                        v-model="emp.conversionTime"
                                        value-format="yyyy-MM-dd"
                                        type="date"
                                        placeholder="转正日期"
                                        size="mini"
                                        style="width: 150px">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="合同起始日期:" prop="beginContract">
                                <el-date-picker
                                        v-model="emp.beginContract"
                                        value-format="yyyy-MM-dd"
                                        type="date"
                                        placeholder="合同起始日期"
                                        size="mini"
                                        style="width: 150px">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="合同终止日期:" prop="endContract">
                                <el-date-picker
                                        v-model="emp.endContract"
                                        value-format="yyyy-MM-dd"
                                        type="date"
                                        placeholder="合同终止日期"
                                        size="mini"
                                        style="width: 150px">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="身份证号码:" prop="idCard">
                                <el-input size="mini" v-model="emp.idCard" style="width: 200px"
                                          prefix-icon="el-icon-edit" placeholder="请输入员工身份证号码..."></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="聘用形式:" prop="engageForm" style="display:flex;align-items: center">
                                <el-radio-group v-model="emp.engageForm">
                                    <el-radio label="劳动合同">劳动合同</el-radio>
                                    <el-radio label="劳务合同">劳务合同</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="婚姻状况:" prop="wedlock" style="display:flex;align-items: center">
                                <el-radio-group v-model="emp.wedlock">
                                    <el-radio label="已婚">已婚</el-radio>
                                    <el-radio label="未婚">未婚</el-radio>
                                    <el-radio label="离异">离异</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="在职状态:" prop="workState" style="display:flex;align-items: center">
                                <el-radio-group v-model="emp.workState">
                                    <el-radio label="在职">在职</el-radio>
                                    <el-radio label="离职">离职</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addOrUpdateEmployee">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "EmpBasic",
        data() {
            return {
                importDataBtnText: '导入数据',
                importDataBtnIcon: 'el-icon-upload2',
                importDataDisabled: false,
                title: '',
                popVisible: false,
                popVisible2: false,
                dialogVisible: false,
                empName: '',
                loading: false,
                emps: [],
                emp: {},
                searchEmp: {
                    nationId: null,
                    posId: null,
                    jobLevelId: null,
                    politicId: null,
                    departmentId: null,
                    engageForm: null,
                    beginDateScope: null
                },
                showSearch: false,
                total: 0,
                page: 1,
                size: 10,
                allPolitics: [],
                allNations: [],
                allPositions: [],
                allJobLevels: [],
                allDegrees: [
                    '博士',
                    '硕士',
                    '本科',
                    '大专',
                    '高中',
                    '初中',
                    '小学',
                    '其他'
                ],
                allDepartments: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                departmentName: '',
                departmentName2: '',
                rules: {
                    name: [{required: true, message: '请输入姓名', trigger: 'blur'}],
                    gender: [{required: true, message: '请选择性别', trigger: 'blur'}],
                    birthday: [{required: true, message: '请选择出生日期', trigger: 'blur'}],
                    politicId: [{required: true, message: '请选择政治面貌', trigger: 'blur'}],
                    nationId: [{required: true, message: '请选择民族', trigger: 'blur'}],
                    nativePlace: [{required: true, message: '请输入籍贯', trigger: 'blur'}],
                    email: [
                        {required: true, message: '请输入电子邮箱', trigger: 'blur'},
                        {type: 'email', message: '邮箱格式错误', trigger: 'blur'}
                    ],
                    address: [{required: true, message: '请输入联系地址', trigger: 'blur'}],
                    posId: [{required: true, message: '请选择职位', trigger: 'blur'}],
                    jobLevelId: [{required: true, message: '请选择职称', trigger: 'blur'}],
                    departmentId: [{required: true, message: '请选择所属部门', trigger: 'blur'}],
                    phone: [{required: true, message: '请输入电话号码', trigger: 'blur'}],
                    workID: [{required: true, message: '工号为空', trigger: 'blur'}],
                    tiptopDegree: [{required: true, message: '请选择最高学历', trigger: 'blur'}],
                    school: [{required: true, message: '请输入毕业院校', trigger: 'blur'}],
                    specialty: [{required: true, message: '请输入专业名称', trigger: 'blur'}],
                    beginDate: [{required: true, message: '请选择入职日期', trigger: 'blur'}],
                    conversionTime: [{required: true, message: '请选择转正日期', trigger: 'blur'}],
                    beginContract: [{required: true, message: '请选择合同起始日期', trigger: 'blur'}],
                    endContract: [{required: true, message: '请选择合同终止日期', trigger: 'blur'}],
                    idCard: [
                        {required: true, message: '请输入身份证号码', trigger: 'blur'},
                        {
                            pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
                            message: '身份证号码格式错误',
                            trigger: 'blur'
                        }
                    ],
                    engageForm: [{required: true, message: '请选择聘用形式', trigger: 'blur'}],
                    wedlock: [{required: true, message: '请选择婚姻状况', trigger: 'blur'}],
                    workState: [{required: true, message: '请选择在职状态', trigger: 'blur'}],
                }
            }
        },
        mounted() {
            this.initEmps();
            this.initData();
        },
        methods: {
            advanceCancel() {
                this.showSearch = !this.showSearch;
                this.page = 1;
                this.size = 10;
                this.initEmps();
            },
            searchOne() {
                this.page = 1;
                this.size = 10;
                this.initEmps('advance');
            },
            searchTwo() {
                this.page = 1;
                this.size = 10;
                this.initEmps();
            },
            advanceSearch() {
                this.showSearch = !this.showSearch;
                this.initAllPositions();
                this.searchEmp = {
                    nationId: null,
                    posId: null,
                    jobLevelId: null,
                    politicId: null,
                    departmentId: null,
                    engageForm: null,
                    beginDateScope: null
                };
                this.departmentName2 = '请选择所属部门...';
            },
            beforeUpload() {
                this.importDataBtnText = '正在导入';
                this.importDataBtnIcon = 'el-icon-loading';
                this.importDataDisabled = true;
            },
            onSuccess() {
                this.importDataBtnText = '导入数据';
                this.importDataBtnIcon = 'el-icon-upload2';
                this.importDataDisabled = false;
                this.initEmps();
            },
            onError() {
                this.importDataBtnText = '导入数据';
                this.importDataBtnIcon = 'el-icon-upload2';
                this.importDataDisabled = false;
            },
            exportData() {
                window.open('/employee/basic/export', '_parent');
            },
            openEditDialog(employee) {
                this.title = '修改员工信息';
                this.dialogVisible = true;
                this.initAllPositions();
                this.emp = employee;
                this.departmentName = employee.department.name;
            },
            deleteEmployee(employee) {
                this.$confirm('此操作将永久删除【' + employee.name + '】员工信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/employee/basic/' + employee.id).then(resp => {
                        if (resp) {
                            this.initEmps();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            },
            addOrUpdateEmployee() {
                this.$refs['employeeForm'].validate(valid => {
                    if (valid) {
                        if (!this.emp.id) {
                            this.postRequest('/employee/basic/', this.emp).then(resp => {
                                if (resp) {
                                    this.initEmps();
                                    this.dialogVisible = false;
                                }
                            });
                        } else {
                            this.putRequest('/employee/basic/', this.emp).then(resp => {
                                if (resp) {
                                    this.initEmps();
                                    this.dialogVisible = false;
                                }
                            });
                        }
                    }
                });
            },
            handleNodeClick(data) {
                this.departmentName = data.name;
                this.emp.departmentId = data.id;
                this.popVisible = !this.popVisible;
            },
            handleNodeClick2(data) {
                this.departmentName2 = data.name;
                this.searchEmp.departmentId = data.id;
                this.popVisible2 = !this.popVisible2;
            },
            initData() {
                if (!window.sessionStorage.getItem('nations')) {
                    this.getRequest('/employee/basic/nation').then(resp => {
                        if (resp) {
                            window.sessionStorage.setItem('nations', JSON.stringify(resp));
                            this.allNations = resp;
                        }
                    });
                } else {
                    this.allNations = JSON.parse(window.sessionStorage.getItem('nations'));
                }
                if (!window.sessionStorage.getItem('politics')) {
                    this.getRequest('/employee/basic/politicsstatus').then(resp => {
                        if (resp) {
                            window.sessionStorage.setItem('politics', JSON.stringify(resp));
                            this.allPolitics = resp;
                        }
                    });
                } else {
                    this.allPolitics = JSON.parse(window.sessionStorage.getItem('politics'));
                }
                if (!window.sessionStorage.getItem('jobLevels')) {
                    this.getRequest('/employee/basic/jobLevel').then(resp => {
                        if (resp) {
                            window.sessionStorage.setItem('jobLevels', JSON.stringify(resp));
                            this.allJobLevels = resp;
                        }
                    });
                } else {
                    this.allJobLevels = JSON.parse(window.sessionStorage.getItem('jobLevels'));
                }
                if (!window.sessionStorage.getItem('departments')) {
                    this.getRequest('/employee/basic/department').then(resp => {
                        if (resp) {
                            window.sessionStorage.setItem('departments', JSON.stringify(resp));
                            this.allDepartments = resp;
                        }
                    });
                } else {
                    this.allDepartments = JSON.parse(window.sessionStorage.getItem('departments'));
                }
            },
            openAddDialog() {
                this.title = '添加员工';
                this.dialogVisible = true;
                this.initAllPositions();
                this.emp = {
                    name: null,
                    gender: null,
                    birthday: null,
                    idCard: null,
                    wedlock: null,
                    nationId: null,
                    nativePlace: null,
                    politicId: null,
                    email: null,
                    phone: null,
                    address: null,
                    departmentId: null,
                    jobLevelId: null,
                    posId: null,
                    engageForm: null,
                    tiptopDegree: null,
                    specialty: null,
                    school: null,
                    beginDate: null,
                    workState: null,
                    workID: null,
                    contractTerm: null,
                    conversionTime: null,
                    notWorkDate: null,
                    beginContract: null,
                    endContract: null,
                    workAge: null
                };
                this.getWorkID();
                this.departmentName = '请选择所属部门...';
            },
            getWorkID() {
                this.getRequest('/employee/basic/workID').then(resp => {
                    if (resp) {
                        this.emp.workID = resp.object;
                    }
                });
            },
            initAllPositions() {
                this.getRequest('/employee/basic/position').then(resp => {
                    if (resp) {
                        this.allPositions = resp;
                    }
                });
            },
            initEmps(type) {
                let url = '';
                if (type && type == 'advance') {
                    if (this.searchEmp.nationId) {
                        url += '&nationId=' + this.searchEmp.nationId;
                    }
                    if (this.searchEmp.posId) {
                        url += '&posId=' + this.searchEmp.posId;
                    }
                    if (this.searchEmp.jobLevelId) {
                        url += '&jobLevelId=' + this.searchEmp.jobLevelId;
                    }
                    if (this.searchEmp.politicId) {
                        url += '&politicId=' + this.searchEmp.politicId;
                    }
                    if (this.searchEmp.departmentId) {
                        url += '&departmentId=' + this.searchEmp.departmentId;
                    }
                    if (this.searchEmp.engageForm) {
                        url += '&engageForm=' + this.searchEmp.engageForm;
                    }
                    if (this.searchEmp.beginDateScope) {
                        url += '&beginDateScope=' + this.searchEmp.beginDateScope;
                    }
                } else {
                    url += '&name=' + this.empName;
                }
                this.loading = true;
                this.getRequest('/employee/basic/?page=' + this.page + '&size=' + this.size + url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.emps = resp.data;
                        this.total = resp.total;
                    }
                });
            },
            currentChange() {
                if (this.showSearch) {
                    this.initEmps('advance');
                } else {
                    this.initEmps();
                }
            },
            sizeChange() {
                if (this.showSearch) {
                    this.initEmps('advance');
                } else {
                    this.initEmps();
                }
            }
        }
    }
</script>

<style>
    .departmentDiv {
        display: inline-flex;
        font-size: 13px;
        width: 150px;
        height: 30px;
        border: 1px solid #dedede;
        border-radius: 5px;
        cursor: pointer;
        align-items: center;
        padding-left: 10px;
        box-sizing: border-box;
    }

    /* 可以设置不同的进入和离开动画 */
    /* 设置持续时间和动画函数 */
    .slide-fade-enter-active {
        transition: all .8s ease;
    }

    .slide-fade-leave-active {
        transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
    }

    .slide-fade-enter, .slide-fade-leave-to
        /* .slide-fade-leave-active for below version 2.1.8 */
    {
        transform: translateX(10px);
        opacity: 0;
    }
</style>