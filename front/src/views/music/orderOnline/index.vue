<template>
<div class="search">
    <add v-if="currView=='add'" @close="currView='index'" @submited="submited" />
    <edit v-if="currView=='edit'" @close="currView='index'" @submited="submited" :data="formData" />
    <Card v-show="currView=='index'">
        <Row v-show="openSearch" @keydown.enter.native="handleSearch">
            <Form ref="searchForm" :model="searchForm" inline :label-width="0">
                <Form-item ref="searchForm" :model="searchForm" inline :label-width="0" style="display:flex;">
                    <Form-item label="" prop="insName">
                        <Input type="text" v-model="searchForm.insName" placeholder="乐器名称" clearable style="width: 150px" />
                    </Form-item>
                    <Form-item style="margin-left:10px;" class="br">
                        <Button @click="handleSearch" type="primary" icon="ios-search" size="small" ghost>搜索</Button>
                        <Button @click="handleReset" type="warning" size="small" icon="md-refresh" ghost>重置</Button>
                        <!-- <Button @click="add" type="info" size="small" icon="md-add" ghost>添加</Button> -->
                        <Button @click="delAll" type="error" icon="md-trash" size="small" ghost :disabled="!$route.meta.permTypes.includes('delete')">取消</Button>
                        <Button @click="excelData" type="success" icon="md-paper-plane" size="small" ghost>导出</Button>
                    </Form-item>
                    <Form-item style="position:fixed;right:50px;top:130px">
                        <Button type="info" @click="showFilterPanelFlag = !showFilterPanelFlag" class="showFilterPanelFlag" icon="md-settings" size="small" ghost>
                            列筛选</Button>
                        <!-- <Button type="warning" @click="modal1 = true" class="showFilterPanelFlag" icon="ios-help-circle-outline" size="small" ghost>
                            使用教程</Button> -->
                        <Modal v-model="modal1" title="使用教程">
                            <p>1.XXXXXXXXXXXXXXXXXXXXXXXX</p>
                            <p>2.XXXXXXXXXXXXXXXXXXXXXXXX</p>
                            <p>3.XXXXXXXXXXXXXXXXXXXXXXXX</p>
                        </Modal>
                    </Form-item>
                </Form-item>
            </Form>
        </Row>
        <Row class="operation" style="position:relative;">
            <transition>
                <div v-show="showFilterPanelFlag" class="filter-panel">
                    <CheckboxGroup v-model="selected">
                        <div v-for="item in mycolumns" :key="item.key">
                            <Checkbox :label="item.title" style="margin: 2px 5px"></Checkbox>
                        </div>
                    </CheckboxGroup>
                </div>
            </transition>
        </Row>
        <Row v-show="openTip"> </Row>
        <Row>
            <Table :loading="loading" :height="tableHeight" border stripe size="small" :columns="columns" :data="data" ref="table" sortable="custom" @on-sort-change="changeSort" @on-selection-change="changeSelect" @on-row-click="rowClick" :row-class-name="rowClassNmae"></Table>
        </Row>
        <Row type="flex" justify="end" class="page">
            <Page :current="searchForm.pageNumber" :total="total" :page-size="searchForm.pageSize" @on-change="changePage" @on-page-size-change="changePageSize" :page-size-opts="[15,20,50]" size="small" show-total show-elevator show-sizer></Page>
        </Row>
    </Card>
    <Modal v-model="reModal" title="乐器归还" draggable :mask="false" footer-hide width="600">
        <Form :label-width="100" label-position="left">
            <Row :gutter="16">
                <Col span="24">
                <FormItem label="归还情况">
                    <Select v-model="reStatus" placeholder="请选择归还情况..." style="width:100%">
                        <Option value="正常归还">正常归还</Option>
                        <Option value="破损归还">破损归还</Option>
                        <Option value="维修归还">维修归还</Option>
                    </Select>
                </FormItem>
                </Col>
                <Col span="24">
                <Divider>
                    <Button @click="reFx" :loading="reLoading" type="primary">提交并保存</Button>
                </Divider>
                </Col>
            </Row>
        </Form>
    </Modal>
</div>
</template>

<script>
import {
    getOrderOnlineList,
    deleteOrderOnline,
    re,
    auditOrder
} from "./api.js";
import add from "./add.vue";
import edit from "./edit.vue";
export default {
    name: "single-window",
    components: {
        add,
        edit
    },
    data() {
        return {
            reModal: false,
            reLoading: false,
            reStatus: "",
            reObj: {},
            tableHeight: 0,
            selected: [
                "选择",
                "序号",
                "租赁人",
                "乐器名称",
                "订单状态",
                "赔付金额",
                "维修金额",
                "押金",
                "租赁费用",
                "租赁时间",
                "操作",
            ],
            modal1: false,
            openSearch: true,
            openTip: true,
            formData: {},
            currView: "index",
            loading: true,
            searchForm: {
                pageNumber: 1,
                pageSize: 15,
                sort: "createTime",
                order: "desc",
            },
            selectList: [],
            selectCount: 0,
            selectRow: 0,
            columns: [{
                    type: "selection",
                    width: 60,
                    title: "选择",
                    align: "center",
                    fixed: "left",
                },
                {
                    title: "序号",
                    width: 90,
                    align: "center",
                    fixed: "left",
                    sortType: true,
                    render: (h, params) => {
                        return h(
                            "span",
                            params.index +
                            (this.searchForm.pageNumber - 1) * this.searchForm.pageSize +
                            1
                        );
                    },
                },
                {
                    title: "租赁人ID",
                    key: "userId",
                    minWidth: 180,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "租赁人",
                    key: "userName",
                    minWidth: 180,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "乐器ID",
                    key: "insId",
                    minWidth: 180,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "乐器名称",
                    key: "insName",
                    minWidth: 180,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "订单状态",
                    key: "status",
                    minWidth: 180,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "赔付金额",
                    key: "price2",
                    minWidth: 180,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "维修金额",
                    key: "price3",
                    minWidth: 180,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "押金",
                    key: "price4",
                    minWidth: 180,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "租赁费用",
                    key: "price5",
                    minWidth: 180,
                    tooltip: true,
                    sortable: false,
                },
                {
                    title: "租赁时间",
                    key: "time",
                    sortable: true,
                    sortType: "desc",
                    minWidth: 180,
                    align: "center",
                    tooltip: true,
                },
                {
                    title: "租赁人",
                    key: "createBy",
                    sortable: true,
                    sortType: "desc",
                    minWidth: 180,
                    align: "center",
                    tooltip: true,
                },
                {
                    title: "修改时间",
                    key: "updateTime",
                    minWidth: 180,
                    align: "center",
                    tooltip: true,
                },
                {
                    title: "修改者",
                    key: "updateBy",
                    minWidth: 180,
                    align: "center",
                    tooltip: true,
                },

                {
                    title: "操作",
                    key: "action",
                    align: "center",
                    width: 360,
                    fixed: "right",
                    render: (h, params) => {
                        var that = this;
                        return h("div", [
                            h(
                                "Button", {
                                    props: {
                                        type: "success",
                                        size: "small",
                                        icon: "ios-create-outline",
                                        ghost: true,
                                        disabled: params.row.status != "未审核" || !(that.$route.meta.permTypes && that.$route.meta.permTypes.includes("enable"))
                                    },
                                    style: {
                                        marginRight: "5px"
                                    },
                                    on: {
                                        click: () => {
                                            this.auditOrderFx(params.row);
                                        }
                                    }
                                },
                                "审核"
                            ),
                            h(
                                "Button", {
                                    props: {
                                        type: "success",
                                        size: "small",
                                        icon: "ios-create-outline",
                                        ghost: true,
                                        disabled: params.row.status != "未归还" || !(that.$route.meta.permTypes && that.$route.meta.permTypes.includes("add"))
                                    },
                                    style: {
                                        marginRight: "5px"
                                    },
                                    on: {
                                        click: () => {
                                            this.reObj = params.row;
                                            this.reStatus = "正常归还";
                                            this.reModal = true;
                                        }
                                    }
                                },
                                "归还"
                            ),
                            h(
                                "Button", {
                                    props: {
                                        type: "primary",
                                        size: "small",
                                        icon: "ios-create-outline",
                                        ghost: true
                                    },
                                    style: {
                                        marginRight: "5px"
                                    },
                                    on: {
                                        click: () => {
                                            this.edit(params.row);
                                        }
                                    }
                                },
                                "详情"
                            ),
                            h(
                                "Button", {
                                    props: {
                                        type: "error",
                                        size: "small",
                                        icon: "md-trash",
                                        ghost: true,
                                        disabled: !(that.$route.meta.permTypes && that.$route.meta.permTypes.includes("delete"))
                                    },
                                    on: {
                                        click: () => {
                                            this.remove(params.row);
                                        }
                                    }
                                },
                                "取消"
                            )
                        ]);
                    }
                }
            ],
            data: [],
            pageNumber: 1,
            pageSize: 10,
            total: 0,
            showFilterPanelFlag: false,
        };
    },
    methods: {
        init() {
            this.getDataList();
        },
        reFx() {
            var that = this;
            that.reLoading = true;
            re({
                id: that.reObj.id,
                status: that.reStatus
            }).then(res => {
                that.reLoading = false;
                if (res.success) {
                    that.reModal = false;
                    this.$Message.success("归还成功");
                    that.getDataList();
                }
            })
        },
        submited() {
            this.currView = "index";
            this.getDataList();
        },
        changePage(v) {
            this.searchForm.pageNumber = v;
            this.getDataList();
            this.clearSelectAll();
        },
        changePageSize(v) {
            this.searchForm.pageSize = v;
            this.getDataList();
        },
        rowClick(row, index) {
            this.selectRow = row;
        },
        rowClassNmae(row, index) {
            if (row.id == this.selectRow.id) {
                return "rowClassNmaeColor";
            }
            return "";
        },
        excelData() {
            this.$refs.table.exportCsv({
                filename: "导出结果",
            });
        },
        handleSearch() {
            this.searchForm.pageNumber = 1;
            this.searchForm.pageSize = 15;
            this.getDataList();
        },
        handleReset() {
            this.$refs.searchForm.resetFields();
            this.searchForm.pageNumber = 1;
            this.searchForm.pageSize = 15;
            this.getDataList();
        },
        changeSort(e) {
            this.searchForm.sort = e.key;
            this.searchForm.order = e.order;
            if (e.order === "normal") {
                this.searchForm.order = "";
            }
            this.getDataList();
        },
        clearSelectAll() {
            this.$refs.table.selectAll(false);
        },
        changeSelect(e) {
            this.selectList = e;
            this.selectCount = e.length;
        },
        getDataList() {
            this.loading = true;
            getOrderOnlineList(this.searchForm).then(res => {
                this.loading = false;
                if (res.success) {
                    this.data = res.result.records;
                    this.total = res.result.total;
                }
            });
        },
        add() {
            this.currView = "add";
        },
        edit(v) {
            for (let attr in v) {
                if (v[attr] == null) {
                    v[attr] = "";
                }
            }
            let str = JSON.stringify(v);
            let data = JSON.parse(str);
            this.formData = data;
            this.currView = "edit";
        },
        remove(v) {
            this.$Modal.confirm({
                title: "确认取消",
                content: "您确认要取消 ?",
                loading: true,
                onOk: () => {
                    deleteOrderOnline({
                        ids: v.id
                    }).then(res => {
                        this.$Modal.remove();
                        if (res.success) {
                            this.$Message.success("操作成功");
                            this.getDataList();
                        }
                    });
                }
            });
        },
        auditOrderFx(v) {
            this.$Modal.confirm({
                title: "确认审核",
                content: "您确认要审核 ?",
                loading: true,
                onOk: () => {
                    auditOrder({
                        id: v.id
                    }).then(res => {
                        this.$Modal.remove();
                        if (res.success) {
                            this.$Message.success("审核通过");
                            this.getDataList();
                        }
                    });
                }
            });
        },
        delAll() {
            if (this.selectCount <= 0) {
                this.$Message.warning("您还未选择要取消的数据");
                return;
            }
            this.$Modal.confirm({
                title: "确认取消",
                content: "您确认要取消所选的 " + this.selectCount + " 条数据?",
                loading: true,
                onOk: () => {
                    let ids = "";
                    this.selectList.forEach(function (e) {
                        ids += e.id + ",";
                    });
                    ids = ids.substring(0, ids.length - 1);
                    deleteOrderOnline({
                        ids: ids
                    }).then(res => {
                        this.$Modal.remove();
                        if (res.success) {
                            this.$Message.success("操作成功");
                            this.clearSelectAll();
                            this.getDataList();
                        }
                    });
                }
            });
        }
    },
    mounted() {
        this.init();
        this.tableHeight = Number(window.innerHeight - 273);
        this.mycolumns = this.columns;
        let showcolumns = [];
        for (var i = 0; i < this.selected.length; i++) {
            var item = this.selected[i];
            for (var j = 0; j < this.columns.length; j++) {
                if (this.columns[j].title == item) {
                    showcolumns.push(this.columns[j]);
                }
            }
        }
        this.columns = showcolumns;
    },
    watch: {
        selected: function (newcolumns) {
            let showcolumns = [];
            for (var i = 0; i < this.mycolumns.length; i++) {
                var item = this.mycolumns[i];
                if (item.title == undefined) showcolumns.push(item);
                else if (newcolumns.includes(item.title)) showcolumns.push(item);
            }
            this.columns = showcolumns;
        },
    },
};
</script>

<style lang="less">
.search {
    .operation {
        margin-bottom: 2vh;
    }

    .select-count {
        font-weight: 600;
        color: #40a9ff;
    }

    .select-clear {
        margin-left: 10px;
    }

    .page {
        margin-top: 2vh;
    }

    .drop-down {
        margin-left: 5px;
    }
}

.filter-panel {
    width: 166px;
    min-height: 120px;
    height: 200px;
    position: absolute;
    background-color: white;
    z-index: 9999;
    margin-left: 1px;
    overflow-y: scroll;
    border: 1px solid blue;
    top: 35px;
    right: 10px;
}

.openSearch {
    position: absolute;
    right: 240px;
}

.openTip {
    position: absolute;
    right: 130px;
}

.showFilterPanelFlag {
    position: static !important;
    right: 10px;
    margin-right: 10px;
}

.ivu-table td {
    height: 38px !important;
}

.ivu-table-cell-with-expand {
    height: 38px !important;
    line-height: 38px !important;
}

.ivu-table .rowClassNmaeColor td {
    background-color: #b0b3b6 !important;
    color: #ffffff !important;
    font-size: 12px;
}
</style>
