<template>
<div>
    <div id="container55"></div>
</div>
</template>

<script>
import {
    Pie,
    measureTextWidth
} from '@antv/g2plot';
import {
    getAntvVoList
} from './api.js';
export default {
    name: "test-page",
    components: {},
    props: {},
    data() {
        return {
            depCountData: [{
                    title: '零食类',
                    value: 27
                },
                {
                    title: '冷冻类',
                    value: 25
                },
                {
                    title: '烟酒类',
                    value: 18
                },
                {
                    title: '生鲜类',
                    value: 15
                },
                {
                    title: '散装类',
                    value: 10
                },
                {
                    title: '水果类',
                    value: 5
                }
            ],
            piePlot: {},
        }
    },
    methods: {
        init() {
            this.initAntvFx();
            this.getAntvVoListFx();
        },
        getAntvVoListFx() {
            var that = this;
            getAntvVoList().then(res => {
                if(res.success) {
                    that.piePlot.changeData(res.result);
                }
            })
        },
        renderStatistic(containerWidth, text, style) {
            const {
                width: textWidth,
                height: textHeight
            } = measureTextWidth(text, style);
            const R = containerWidth / 2;
            let scale = 1;
            if (containerWidth < textWidth) {
                scale = Math.min(Math.sqrt(Math.abs(Math.pow(R, 2) / (Math.pow(textWidth / 2, 2) + Math.pow(textHeight, 2)))), 1);
            }
            const textStyleStr = `width:${containerWidth}px;`;
            return `<div style="${textStyleStr};font-size:${scale}em;line-height:${scale < 1 ? 1 : 'inherit'};">${text}</div>`;
        },
        initAntvFx() {
            var that = this;
            var data = this.depCountData;
            this.piePlot = new Pie('container55', {
                appendPadding: 10,
                data,
                angleField: 'value',
                colorField: 'title',
                radius: 1,
                innerRadius: 0.64,
                meta: {
                    value: {
                        formatter: (v) => `${v} `,
                    },
                },
                label: {
                    type: 'inner',
                    offset: '-50%',
                    style: {
                        textAlign: 'center',
                    },
                    autoRotate: false,
                    content: '{value}',
                },
                statistic: {
                    title: {
                        offsetY: -4,
                        customHtml: (container, view, datum) => {
                            const {
                                width,
                                height
                            } = container.getBoundingClientRect();
                            const d = Math.sqrt(Math.pow(width / 2, 2) + Math.pow(height / 2, 2));
                            const text = datum ? datum.title : '总收入分析图';
                            return that.renderStatistic(d, text, {
                                fontSize: 28
                            });
                        },
                    },
                    content: {
                        offsetY: 4,
                        style: {
                            fontSize: '32px',
                        },
                        customHtml: (container, view, datum, data) => {
                            const {
                                width
                            } = container.getBoundingClientRect();
                            const text = datum ? ` ${datum.value}元` : ` ${data.reduce((r, d) => r + d.value, 0)}元`;
                            return that.renderStatistic(width, text, {
                                fontSize: 32
                            });
                        },
                    },
                },
                // 添加 中心统计文本 交互
                interactions: [{
                    type: 'element-selected'
                }, {
                    type: 'element-active'
                }, {
                    type: 'pie-statistic-active'
                }],
            });
            this.piePlot.render();
            this.piePlot.on('element:click', (args) => {
                that.getPieUserItemFx(args.data.data.title);
            });
        },
    },
    mounted() {
        this.init();
    },
};
</script>

<style lang="less" scoped>
#container55 {
    width: 100%;
    height: 700px;
    margin-top: 20px;
}

.antvTitle {
    font-size: 20px;
    text-align: center;
    justify-content: center;
    display: flex;

    .antvSecondTitle {
        color: #ff9900;
        font-size: 22px;
    }
}
</style>
