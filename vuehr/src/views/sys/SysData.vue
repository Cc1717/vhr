<template>
    <div>
        <el-button type="success" icon="el-icon-download" :onclick="backupDatabase">备份数据库</el-button>
    </div>
</template>

<script>
    export default {
        name: "SysData",
        data() {
            return {}
        },
        methods: {
            backupDatabase() {
                this.getRequest('/system/data/').then(resp => {
                    if (resp) {

                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>