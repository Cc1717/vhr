<template>
    <div id="list">
        <ul style="padding-left:0px">
            <li v-for="item in hrs" :class="{ active: currentSession?item.username === currentSession.username:false }"
                v-on:click="changeCurrentSession(item)">
                <!--   :class="[item.id === currentSession ? 'active':'']" -->
                <img class="avatar" :src="item.userface" :alt="item.name">
                <el-badge :is-dot="isDot[user.username+'#'+item.username]">
                    <p class="name">{{item.name}}</p>
                </el-badge>
            </li>
        </ul>
    </div>
</template>

<script>
    import {mapState} from 'vuex'

    export default {
        name: 'list',
        data() {
            return {
                user: JSON.parse(window.sessionStorage.getItem('user'))
            }
        },
        computed: mapState([
            'hrs',
            'currentSession',
            'isDot'
        ]),
        methods: {
            changeCurrentSession(hr) {
                this.$store.commit('changeCurrentSession', hr);
            }
        }
    }
</script>

<style lang="scss" scoped>
    #list {
        li {
            padding: 15px;
            border-bottom: 1px solid #292C33;
            cursor: pointer;
            list-style: none;

            &:hover {
                background-color: rgba(255, 255, 255, 0.03);
            }
        }

        li.active { /*注意这个是.不是冒号:*/
            background-color: rgba(255, 255, 255, 0.1);
        }

        .avatar {
            border-radius: 2px;
            width: 30px;
            height: 30px;
            vertical-align: middle;
        }

        .name {
            display: inline-block;
            margin: 0 0 0 15px
        }
    }
</style>
