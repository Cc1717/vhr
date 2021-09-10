<template>
    <div>
        <el-container>
            <el-header class="homeHeader">
                <div class="title">企业人事管理系统</div>
                <div>
                    <el-button icon="el-icon-bell" type="text" style="margin-right:8px;color:#000000" size="big"
                               @click="$router.push('/chat')"></el-button>
                    <el-dropdown class="userInfo" @command="commandHandler">
                        <span class="el-dropdown-link" style="color: #ffffff">
                            {{user.name}}<i><img :src="user.userface"></i>
                        </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="center">个人中心</el-dropdown-item>
                            <el-dropdown-item command="setting">设置</el-dropdown-item>
                            <el-dropdown-item command="logout" divided>注销登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </el-header>
            <el-container>
                <el-aside width="200px">
                    <el-menu router unique-opened>
                        <el-submenu :index="index+''" v-for="(item,index) in routes" :key="index">
                            <template slot="title">
                                <i style="color: #249eff;margin-right: 5px" :class="item.iconCls"></i>
                                <span>{{item.name}}</span>
                            </template>
                            <el-menu-item :index="child.path" v-for="(child,indexj) in item.children" :key="indexj">
                                <i style="color: #249eff;margin-right: 5px" :class="child.iconCls"></i>
                                <span>{{child.name}}</span>
                            </el-menu-item>
                        </el-submenu>
                    </el-menu>
                </el-aside>
                <el-main>
                    <el-breadcrumb separator-class="el-icon-arrow-right" v-if="$router.currentRoute.path!='/home'">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>{{$router.currentRoute.name}}</el-breadcrumb-item>
                    </el-breadcrumb>
                    <HomePage v-if="$router.currentRoute.path=='/home'"></HomePage>
                    <router-view class="homeRouterView"/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    import HomePage from "../components/HomePage";

    export default {
        name: "Home",
        components: {
            HomePage
        },
        data() {
            return {
                //user: JSON.parse(window.sessionStorage.getItem('user'))
            }
        },
        computed: {
            routes() {
                return this.$store.state.routes;
            },
            user() {
                return this.$store.state.currentHr;
            }
        },
        methods: {
            commandHandler(cmd) {
                if (cmd == 'logout') {
                    this.$confirm('此操作将注销登录, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.getRequest('/logout');
                        this.$store.commit('initRoutes', []);
                        window.sessionStorage.removeItem('user');
                        this.$router.replace('/');
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消操作'
                        });
                    });
                } else if (cmd == 'center') {
                    this.$router.push('/myCenter');
                }
            }
        }
    }
</script>

<style>
    .homeHeader {
        background-color: #249eff;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0px 15px;
        box-sizing: border-box;
    }

    .homeHeader .title {
        font-size: 24px;
        font-family: 迷你简菱心;
        color: #ffffff;
    }

    .userInfo {
        cursor: pointer;
    }

    .el-dropdown-link img {
        width: 48px;
        height: 48px;
        border-radius: 24px;
        margin-left: 8px;
    }

    .el-dropdown-link {
        display: flex;
        align-items: center;
    }

    .homeRouterView {
        margin-top: 15px;
    }
</style>