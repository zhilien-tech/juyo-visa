require(['jquery', 'echarts', 'macarons', 'app'], function ($, ec) {
    var chart = ec.init(document.getElementById('chart'), 'macarons');
    chart.showLoading({
        text: "正在努力加载..."
    });
    $(window).resize(chart.resize);
    var option = {
        tooltip: {
            trigger: 'axis'
        },
        grid: {
            x: 40,
            x2: 10,
            y2: 24
        },
        calculable: true,
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLabel: {
                    formatter: '{value} 人'
                }
            }
        ],
        legend: {
            data: ['最高气温', '最低气温']
        },
        series: [
            {
                name: '最高气温',
                type: 'line',
                data: [11, 11, 15, 13, 12, 13, 11],
            },
            {
                name: '最低气温',
                type: 'line',
                data: [1, -2, 2, 5, 3, 2, 0],
            }
        ]
    };
    $.getJSON("a/statistics", {}, function (result) {
        if ("SUCCESS" == result.code) {
            option.legend.data = result.data.legend;
            option.series = result.data.series;
            chart.setOption(option);
        }
        chart.hideLoading();
    });
});