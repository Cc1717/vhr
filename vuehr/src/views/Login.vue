<template>
    <div id="login">
        <el-form
                v-loading="loading"
                element-loading-text="正在登录..."
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.8)"
                :rules="rules"
                ref="loginForm"
                :model="loginForm"
                class="loginContainer">
            <h3 class="loginTitle">系统登录</h3>
            <el-form-item prop="username">
                <el-input size="normal" type="text" v-model="loginForm.username" auto-complete="off"
                          placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input size="normal" type="password" v-model="loginForm.password" auto-complete="off"
                          placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item prop="code">
                <el-input size="normal" type="text" v-model="loginForm.code" auto-complete="off"
                          placeholder="点击图片更换验证码" @keydown.enter.native="submitLogin" style="width: 200px">
                </el-input>
                <img :src="codeURL" alt="验证码" @click="changeCodeURL" style="margin-left: auto;"/>
            </el-form-item>
<!--            <el-checkbox class="loginRemember" v-model="checked"></el-checkbox>-->
            <el-button size="normal" type="primary" style="width:100%;" @click="submitLogin">登录</el-button>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            return {
                codeURL: '/verifyCode',
                loading: false,
                loginForm: {
                    username: 'admin',
                    password: '123',
                    code: ''
                },
                checked: true,
                rules: {
                    username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
                    password: [{required: true, message: '请输入密码', trigger: 'blur'}],
                    code: [{required: true, message: '请输入验证码', trigger: 'blur'}]
                }
            }
        },
        methods: {
            changeCodeURL() {
                this.codeURL = '/verifyCode?time=' + new Date();
            },
            submitLogin() {
                this.$refs.loginForm.validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        this.postKeyValueRequest('/doLogin', this.loginForm).then(resp => {
                            this.loading = false;
                            if (resp) {
                                window.sessionStorage.setItem('user', JSON.stringify(resp.object));
                                let path = this.$route.query.redirect;
                                this.$router.replace(path == undefined ? '/home' : path);
                            } else {
                                this.changeCodeURL();
                            }
                        });
                    }
                });
            }
        }
    }
</script>

<style>
    #login{
        background: url("../../public/login.jpg");
        background-size: 100% 100%;
        height: 100%;
        position: fixed;
        width: 100%
    }

    .loginContainer {
        border-radius: 15px;
        background-clip: padding-box;
        margin: 180px auto;
        width: 350px;
        padding: 35px 35px 15px 35px;
        background: #fff;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 20px #cac6c6;
    }

    .loginTitle {
        margin: 15px auto 20px auto;
        text-align: center;
        color: #505458;
    }

    .loginRemember {
        text-align: left;
        margin: 0 0 15px 0;
    }

    .el-form-item__content {
        display: flex;
        align-items: center;
    }
</style>