<template>
    <div>
        <el-card style="width: 380px">
            <div slot="header">
                <span>{{myself.name}}</span>
            </div>
            <div>
                <div style="display: flex;justify-content: center;margin-bottom: 10px">
                    <img :src="myself.userface" title="点击修改用户头像" :alt="myself.name"
                         style="width: 100px;height: 100px;border-radius: 30px;"/>
                </div>
                <div style="margin-bottom:10px">
                    电话号码：
                    <el-tag size="normal">{{myself.telephone}}</el-tag>
                </div>
                <div style="margin-bottom:10px">
                    手机号码：
                    <el-tag size="normal">{{myself.phone}}</el-tag>
                </div>
                <div style="margin-bottom:10px">
                    居住地址：
                    <el-tag size="normal">{{myself.address}}</el-tag>
                </div>
                <div style="margin-bottom:10px">
                    用户标签：
                    <el-tag style="margin-right:8px;margin-bottom:8px" size="normal"
                            v-for="(role,index) of myself.roles" :key="index"
                            type="success">
                        {{role.nameZh}}
                    </el-tag>
                </div>
                <div style="display: flex;justify-content: space-around">
                    <el-button type="primary" @click="showEditInfoDialog">修改信息</el-button>
                    <el-button type="danger" @click="pwdDialogVisible = true">修改密码</el-button>
                </div>
            </div>
        </el-card>
        <el-dialog
                title="修改信息"
                :visible.sync="infoDialogVisible"
                width="30%">
            <div>
                <table>
                    <tr>
                        <td>
                            <el-tag size="normal">用户昵称：</el-tag>
                        </td>
                        <td>
                            <el-input v-model="myself2.name"></el-input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag size="normal">电话号码：</el-tag>
                        </td>
                        <td>
                            <el-input v-model="myself2.telephone"></el-input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag size="normal">手机号码：</el-tag>
                        </td>
                        <td>
                            <el-input v-model="myself2.phone"></el-input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag size="normal">居住地址：</el-tag>
                        </td>
                        <td>
                            <el-input v-model="myself2.address"></el-input>
                        </td>
                    </tr>
                </table>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="infoDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="doUpdateInfo">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog
                title="修改密码"
                :visible.sync="pwdDialogVisible"
                width="30%">
            <el-form :model="pwdForm" status-icon :rules="rules" ref="pwdForm" label-width="120px"
                     class="demo-ruleForm">
                <el-form-item label="请输入旧密码" prop="old">
                    <el-input :type="checked?'text':'password'" style="width: 200px" v-model="pwdForm.old"
                              autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="请输入新密码" prop="new">
                    <el-input :type="checked?'text':'password'" style="width: 200px" v-model="pwdForm.new"
                              autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="确认新密码" prop="confirm">
                    <el-input :type="checked?'text':'password'" style="width: 200px" v-model="pwdForm.confirm"
                              autocomplete="off"></el-input>
                    <el-checkbox v-model="checked" style="margin-left:10px"></el-checkbox>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('pwdForm')">提交</el-button>
                    <el-button @click="resetForm('pwdForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "MyCenter",
        data() {
            let validateConfirm = (rule, value, callback) => {
                if (value !== this.pwdForm.new) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                checked: false,
                myself: null,
                myself2: null,
                infoDialogVisible: false,
                pwdDialogVisible: false,
                pwdForm: {
                    old: '',
                    new: '',
                    confirm: ''
                },
                rules: {
                    old: [
                        {required: true, message: '请输入初始密码', trigger: 'blur'}
                    ],
                    new: [
                        {required: true, message: '请输入新密码', trigger: 'blur'}
                    ],
                    confirm: [
                        {required: true, message: '请确认新密码', trigger: 'blur'},
                        {validator: validateConfirm, trigger: 'blur'}
                    ]
                },
            }
        },
        mounted() {
            this.initMyself();
        },
        methods: {
            initMyself() {
                this.getRequest('/hr/info').then(resp => {
                    if (resp) {
                        this.myself = resp;
                        this.myself2 = Object.assign({}, this.myself);
                        window.sessionStorage.setItem('user', JSON.stringify(resp));
                        this.$store.commit('INIT_CURRENTHR', resp);
                    }
                });
            },
            showEditInfoDialog() {
                this.infoDialogVisible = true;
                this.myself2 = Object.assign({}, this.myself);
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            doUpdateInfo() {
                if (this.myself.name != this.myself2.name || this.myself.telephone != this.myself2.telephone
                    || this.myself.phone != this.myself2.phone || this.myself.address != this.myself2.address) {
                    this.putRequest('/hr/info', this.myself2).then(resp => {
                        if (resp) {
                            this.initMyself();
                            this.infoDialogVisible = false;
                        }
                    });
                } else {
                    this.$message.info('信息未修改');
                }
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.pwdForm.id = this.myself.id;
                        this.putRequest('/hr/pwd', this.pwdForm).then(resp => {
                            if (resp) {
                                this.getRequest('/logout');
                                this.$store.commit('initRoutes', []);
                                window.sessionStorage.removeItem('user');
                                this.$router.replace('/');
                            }
                        });
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>