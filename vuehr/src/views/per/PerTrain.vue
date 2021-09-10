<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <div>
                <el-input clearable v-model="empName" :disabled="nameDisabled" prefix-icon="el-icon-search"
                          style="width: 180px;margin-right:10px"
                          placeholder="通过员工姓名搜索..."></el-input>
                <el-input clearable v-model="empWorkID" :disabled="workIDDisabled" prefix-icon="el-icon-search"
                          style="width: 180px;margin-right:10px" placeholder="通过员工工号搜索..."></el-input>
                <el-date-picker
                        style="margin-right:10px"
                        v-model="datetime"
                        type="datetimerange"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        range-separator="至"
                        start-placeholder="培训时间"
                        end-placeholder="培训时间">
                </el-date-picker>
                <el-button icon="el-icon-search" type="primary" @click="doSearch">搜索</el-button>
            </div>
            <el-button icon="el-icon-plus" type="primary">添加培训记录</el-button>
        </div>
        <div style="margin-top: 10px">
            <el-table
                    v-loading="loading"
                    element-loading-text="正在获取数据..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :header-cell-style="{'text-align':'center'}"
                    :cell-style="{'text-align':'center'}"
                    :data="trains"
                    stripe
                    border
                    style="width: 100%"
                    @selection-change="handleSelectionChange">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="trainContent"
                        label="培训内容"
                        width="250">
                </el-table-column>
                <el-table-column
                        prop="remark"
                        label="培训备注"
                        width="250">
                </el-table-column>
                <el-table-column
                        prop="trainDate"
                        label="培训时间"
                        width="200">
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
                        label="操作"
                        width="200">
                    <template slot-scope="scope">
                        <el-button size="mini">编辑</el-button>
                        <el-button type="danger" size="mini" @click="deleteTrain(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div style="display: flex;justify-content: space-between;margin-top: 10px">
            <div>
                <el-button type="danger" :disabled="multipleSelection.length==0" @click="multipleDelete"
                           style="margin-right:10px">批量删除
                </el-button>
                <el-upload
                        :show-file-list="false"
                        :before-upload="beforeUpload"
                        :on-success="onSuccess"
                        :on-error="onError"
                        :disabled="importDataDisabled"
                        action="/personnel/train/import"
                        style="display: inline-flex;margin-right:10px">
                    <el-button type="success" :icon="importDataBtnIcon">{{importDataBtnText}}</el-button>
                </el-upload>
                <el-button type="success" :disabled="importDataDisabled" @click="exportData"
                           icon="el-icon-download">
                    导出数据
                </el-button>
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
    </div>
</template>

<script>
    export default {
        name: "PerTrain",
        data() {
            return {
                importDataBtnIcon: 'el-icon-upload2',
                importDataBtnText: '导入数据',
                importDataDisabled: false,
                trains: [],
                page: 1,
                size: 10,
                total: 0,
                loading: false,
                datetime: null,
                empName: '',
                empWorkID: '',
                multipleSelection: []
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
            this.initTrains();
        },
        methods: {
            beforeUpload() {
                this.importDataBtnIcon = 'el-icon-loading';
                this.importDataBtnText = '正在导入';
                this.importDataDisabled = true;
            },
            onSuccess() {
                this.importDataBtnIcon = 'el-icon-upload2';
                this.importDataBtnText = '导入数据';
                this.importDataDisabled = false;
                this.initTrains();
            },
            onError() {
                this.importDataBtnIcon = 'el-icon-upload2';
                this.importDataBtnText = '导入数据';
                this.importDataDisabled = false;
            },
            exportData() {
                window.open('/personnel/train/export', '_parent');
            },
            initTrains() {
                this.loading = true;
                let url = '';
                if (this.empName) {
                    url += '&name=' + this.empName;
                }
                if (this.empWorkID) {
                    url += '&workID=' + this.empWorkID;
                }
                if (this.datetime) {
                    url += '&timeScope=' + this.datetime;
                }
                this.getRequest('/personnel/train/?page=' + this.page + '&size=' + this.size + url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.trains = resp.data;
                        this.total = resp.total;
                    }
                });
            },
            doSearch() {
                this.page = 1;
                this.size = 10;
                this.initTrains();
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
                    this.deleteRequest('/personnel/train/' + param).then(resp => {
                        if (resp) {
                            this.initTrains();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            },
            deleteTrain(train) {
                this.$confirm('此操作将永久删除【' + train.employee.name + '】的培训记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/personnel/train/' + train.id).then(resp => {
                        if (resp) {
                            this.initTrains();
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
                this.initTrains();
            },
            sizeChange() {
                this.initTrains();
            }
        }
    }
</script>

<style scoped>

</style>