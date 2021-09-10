<template>
    <div>
        <el-input
                placeholder="请输入部门名称进行搜索..."
                prefix-icon="el-icon-search"
                v-model="depName"
                style="width: 300px;margin-bottom: 10px">
        </el-input>

        <el-tree
                :data="deps"
                :props="defaultProps"
                :filter-node-method="filterNode"
                :expand-on-click-node="false"
                ref="tree"
                style="width: 550px;">
            <span class="custom-tree-node" style="display: flex;justify-content: space-between;width: 100%"
                  slot-scope="{ node, data }">
                <span>{{ node.label }}</span>
                <span>
                    <el-button
                            type="primary"
                            size="mini"
                            class="depBtn"
                            @click="openAddDialog(data)">
                        添加部门
                    </el-button>
                    <el-button
                            type="danger"
                            size="mini"
                            class="depBtn"
                            @click="deleteDep(data)">
                        删除部门
                    </el-button>
                </span>
            </span>
        </el-tree>
        <el-dialog
                title="添加部门"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <el-tag>父部门</el-tag>
                <el-input style="width:200px;margin-left: 8px" v-model="pname" disabled></el-input>
            </div>
            <div style="margin-top: 10px">
                <el-tag>子部门</el-tag>
                <el-input style="width:200px;margin-left: 8px" v-model="dep.name" placeholder="请输入部门名称..."
                          @keydown.enter.native="addDepartment"></el-input>

            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addDepartment">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "DepManager",
        data() {
            return {
                depName: '',
                deps: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                dialogVisible: false,
                dep: {
                    name: '',
                    parentId: -1
                },
                pname: ''
            }
        },
        mounted() {
            this.initDeps();
        },
        watch: {
            depName(val) {
                this.$refs.tree.filter(val);
            }
        },
        methods: {
            deleteDep(dep) {
                this.$confirm('此操作将永久删除【' + dep.name + '】部门, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/department/' + dep.id).then(resp => {
                        if (resp) {
                            this.afterDelete(this.deps, dep.id);
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            },
            afterDelete(deps, id) {
                for (let i = 0; i < deps.length; i++) {
                    if (deps[i].id == id) {
                        deps.splice(i, 1);
                        return;
                    } else {
                        this.afterDelete(deps[i].children, id);
                    }
                }
            },
            openAddDialog(dep) {
                this.pname = dep.name;
                this.dep.parentId = dep.id;
                this.dialogVisible = true;
            },
            addDepartment() {
                if (!this.dep.name) {
                    this.$message.error('所添加的子部门名称不能为空');
                } else {
                    this.postRequest('/system/basic/department/', this.dep).then(resp => {
                        if (resp) {
                            this.dep.name = '';
                            this.dialogVisible = false;
                            this.afterAdd(this.deps, resp.object);
                        }
                    });
                }
            },
            afterAdd(deps, dep) {
                for (let i = 0; i < deps.length; i++) {
                    if (deps[i].id == dep.parentId) {
                        deps[i].children = deps[i].children.concat(dep);
                        return;
                    } else {
                        this.afterAdd(deps[i].children, dep);
                    }
                }
            },
            filterNode(value, data) {
                if (!value) return true;
                return data.name.indexOf(value) !== -1;
            },
            initDeps() {
                this.getRequest('/system/basic/department/').then(resp => {
                    if (resp) {
                        this.deps = resp;
                    }
                });
            }
        }
    }
</script>

<style>
    .depBtn {
        padding: 4px;
    }
</style>