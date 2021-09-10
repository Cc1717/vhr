<template>
    <div class="homePage">
        <div style="width: 400px;height: 550px">
            <el-divider content-position="center">
                <el-tag style="font-size: 16px" size="normal">本周通告</el-tag>
                <el-button style="margin-left: 10px" size="normal" type="text" icon="el-icon-plus"></el-button>
            </el-divider>
            <el-timeline style="padding-left:0;margin-top: 20px">
                <el-timeline-item v-for="notice in notices" timestamp="通告" :key="notice.id"
                                  placement="top">
                    <el-card :title="notice.hr.name+'在'+notice.createTime+'创建.'">
                        <div slot="header">
                            <span>{{notice.noticeTitle}}</span>
                            <el-button style="float: right; padding: 3px 0;color:#ff0000" size="normal" type="text"
                                       icon="el-icon-delete"></el-button>
                        </div>
                        <p>{{notice.noticeContent}}</p>
                    </el-card>
                </el-timeline-item>
            </el-timeline>
        </div>
        <div style="width: 400px;height: 550px">
            <el-divider content-position="center">
                <el-tag style="font-size: 16px" size="normal">本月会议</el-tag>
                <el-button style="margin-left: 10px" size="normal" type="text" icon="el-icon-plus"></el-button>
            </el-divider>
            <el-timeline style="padding-left:0;margin-top: 20px">
                <el-timeline-item v-for="meeting in meetings" :key="meeting.id" placement="top"
                                  :timestamp="meeting.remark?'会议-'+meeting.remark:'会议'">
                    <el-card>
                        <div slot="header">
                            <span>{{meeting.meetingName}}</span>
                            <el-button style="float: right; padding: 3px 0;color:#ff0000" size="normal" type="text"
                                       icon="el-icon-delete"></el-button>
                        </div>
                        <p>
                            <span>主持>{{meeting.speaker.department.name}}-{{meeting.speaker.name}}</span>
                            <span style="margin-left: 20px">记录>{{meeting.recorder.department.name}}-{{meeting.recorder.name}}</span>
                        </p>
                        <p>
                            <span>{{meeting.meetingTime}}</span>
                            <span style="margin-left: 20px">{{meeting.meetingPlace}}</span>
                        </p>
                    </el-card>
                </el-timeline-item>
            </el-timeline>
        </div>
    </div>
</template>

<script>
    export default {
        name: "HomePage",
        data() {
            return {
                notices: [],
                meetings: []
            }
        },
        mounted() {
            this.initNotices();
            this.initMeetings();
        },
        methods: {
            initNotices() {
                this.getRequest('/notice/').then(resp => {
                    if (resp) {
                        this.notices = resp;
                    }
                });
            },
            initMeetings() {
                this.getRequest('/meeting/').then(resp => {
                    if (resp) {
                        this.meetings = resp;
                    }
                })
            }
        }
    }
</script>

<style>
    .homePage {
        display: flex;
        justify-content: space-around;
        margin-top: 40px;
    }
</style>