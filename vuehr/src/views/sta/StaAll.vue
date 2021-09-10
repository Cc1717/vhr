<template>
    <div>
        <div id="degree" style="width:600px;height:400px;"></div>
    </div>
</template>

<script>
    export default {
        name: "StaAll",
        mounted() {
            this.drawDegree();
        },
        methods: {
            drawDegree() {
                let degreeName = [];
                let empNumber = [];
                let degree = this.echarts.init(document.getElementById('degree'));
                this.getRequest('/statistics/all/tiptopDegree').then(resp => {
                    if (resp) {
                        degreeName = resp[0];
                        empNumber = resp[1];
                    }
                    degree.setOption({
                        backgroundColor: '#d1fffb',
                        title: {text: '企业人员学历'},
                        tooltip: {},
                        xAxis: {
                            data: degreeName,
                            axisLabel: {
                                show: true,
                                textStyle: {
                                    fontSize : 14      //更改坐标轴文字大小
                                }
                            }
                        },
                        yAxis: {
                            axisLabel: {
                                show: true,
                                textStyle: {
                                    fontSize : 14      //更改坐标轴文字大小
                                }
                            }
                        },
                        series: [{
                            name: '最高学历',
                            type: 'bar',
                            barWidth: '60%',
                            data: empNumber,
                            itemStyle: {
                                normal: {
                                    barBorderRadius: [12, 12, 12, 12],
                                    color: '#45e5f9'
                                }
                            }
                        }]
                    });
                });
            }
        }
    }
</script>

<style scoped>

</style>