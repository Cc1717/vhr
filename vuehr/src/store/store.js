import Vue from 'vue'
import Vuex from 'vuex'
import {getRequest} from '../utils/api'
import SockJS from '../utils/sockjs'
import {Stomp} from '../utils/stomp'
import {Notification} from 'element-ui';

Vue.use(Vuex);

const now = new Date();

const store = new Vuex.Store({
    state: {
        routes: [],
        sessions: {},
        hrs: [],
        currentHr: null,
        currentSession: null,
        filterKey: '',
        stomp: null,
        isDot: {}
    },
    mutations: {
        initRoutes(state, data) {
            state.routes = data;
        },
        changeCurrentSession(state, hr) {
            state.currentSession = hr;
            Vue.set(state.isDot, state.currentHr.username + '#' + hr.username, false);
        },
        addMessage(state, msg) {
            if (!state.sessions[state.currentHr.username + '#' + msg.to]) {
                Vue.set(state.sessions, state.currentHr.username + '#' + msg.to, []);
            }
            state.sessions[state.currentHr.username + '#' + msg.to].push({
                content: msg.content,
                time: new Date().toLocaleString('chinese',{hour12:false}),
                self: !msg.notSelf
            });
        },
        INIT_DATA(state) {
            //历史聊天记录加载
            let data = localStorage.getItem('vue-chat-session');
            if (data) {
                state.sessions = JSON.parse(data);
            }
        },
        INIT_HRS(state, data) {
            state.hrs = data;
        },
        INIT_CURRENTHR(state, hr) {
            state.currentHr = hr;
        }
    },
    actions: {
        connect(context) {
            context.state.stomp = Stomp.over(new SockJS('/ws/ep'));
            context.state.stomp.connect({}, success => {
                context.state.stomp.subscribe('/user/queue/chat', msg => {
                    let receiveMsg = JSON.parse(msg.body);
                    if (!context.state.currentSession || context.state.currentSession.username != receiveMsg.from) {
                        Notification.info({
                            title: '[' + receiveMsg.fromNickName + ']发来一条新消息',
                            message: receiveMsg.content.length > 10 ? receiveMsg.content.substr(0, 10) : receiveMsg.content,
                            position: 'bottom-right'
                        });
                        Vue.set(context.state.isDot, context.state.currentHr.username + '#' + receiveMsg.from, true);
                    }
                    receiveMsg.notSelf = true;
                    receiveMsg.to = receiveMsg.from;
                    context.commit('addMessage', receiveMsg);
                });
            }, error => {

            });
        },
        initList(context) {
            getRequest('/chat/hrs').then(resp => {
                if (resp) {
                    context.commit('INIT_HRS', resp);
                }
            });
        }
    }
});

store.watch(function (state) {
    return state.sessions
}, function (val) {
    localStorage.setItem('vue-chat-session', JSON.stringify(val));
}, {
    deep: true/*这个貌似是开启watch监测的判断,官方说明也比较模糊*/
});


export default store;