<template>
    <div>
        <el-tabs v-model="activeName" type="card">
            <el-tab-pane label="部门管理" name="depmanager">
                <DepManager></DepManager>
            </el-tab-pane>
            <el-tab-pane label="职位管理" name="posmanager">
                <PosManager></PosManager>
            </el-tab-pane>
            <el-tab-pane label="职称管理" name="joblevelmanager">
                <JobLevelManager></JobLevelManager>
            </el-tab-pane>
            <el-tab-pane label="奖惩规则" name="rewardpunishmentmanager">
                <RewardPunishmentManager></RewardPunishmentManager>
            </el-tab-pane>
            <el-tab-pane label="权限组" name="permissmanager">
                <PermissManager></PermissManager>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    import DepManager from "../../components/sys/basic/DepManager";
    import PosManager from "../../components/sys/basic/PosManager";
    import JobLevelManager from "../../components/sys/basic/JobLevelManager";
    import RewardPunishmentManager from "../../components/sys/basic/RewardPunishmentManager";
    import PermissManager from "../../components/sys/basic/PermissManager";

    export default {
        name: "SysBasic",
        data() {
            return {
                activeName: 'posmanager'
            }
        },
        components: {
            DepManager,
            PosManager,
            JobLevelManager,
            RewardPunishmentManager,
            PermissManager
        }
    }
</script>

<style scoped>

</style>