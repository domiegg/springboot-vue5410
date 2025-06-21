<template>
<div>
    <Card>
        <div slot="title">
            <div class="edit-head">
                <a @click="close" class="back-title">
                    <Icon type="ios-arrow-back" />返回
                </a>
                <div class="head-name">详情</div>
                <span></span>
                <a @click="close" class="window-close">
                    <Icon type="ios-close" size="31" class="ivu-icon-ios-close" />
                </a>
            </div>
        </div>
        <Form ref="form" :model="form" :label-width="100" :rules="formValidate" label-position="left">
            <FormItem label="租赁人" prop="userName">
                <Input v-model="form.userName" readonly maxlength="240" show-word-limit placeholder="请输入租赁人..." style="width:570px" />
            </FormItem>
            <FormItem label="乐器名称" prop="insName">
                <Input v-model="form.insName" readonly maxlength="240" show-word-limit placeholder="请输入乐器名称..." style="width:570px" />
            </FormItem>
            <FormItem label="订单状态" prop="status">
                <Input v-model="form.status" readonly maxlength="240" show-word-limit placeholder="请输入订单状态..." style="width:570px" />
            </FormItem>
            <FormItem label="赔付金额" prop="price2">
                <InputNumber v-model="form.price2" readonly min="0" max="5000000" placeholder="请输入赔付金额..." style="width:570px"></InputNumber>
            </FormItem>
            <FormItem label="维修金额" prop="price3">
                <InputNumber v-model="form.price3" readonly min="0" max="5000000" placeholder="请输入维修金额..." style="width:570px"></InputNumber>
            </FormItem>
            <FormItem label="押金" prop="price4">
                <InputNumber v-model="form.price4" readonly min="0" max="5000000" placeholder="请输入押金..." style="width:570px"></InputNumber>
            </FormItem>
            <FormItem label="租赁费用" prop="price5">
                <InputNumber v-model="form.price5" readonly min="0" max="5000000" placeholder="请输入租赁费用..." style="width:570px"></InputNumber>
            </FormItem>
            <Form-item class="br">
                <!-- <Button @click="handleSubmit" :loading="submitLoading" type="primary">提交并保存</Button>
                <Button @click="handleReset">重置</Button> -->
                <Button type="dashed" @click="close">关闭</Button>
            </Form-item>
        </Form>
    </Card>
</div>
</template>

<script>
import {
    editOrderOffline
} from "./api.js";
export default {
    name: "edit",
    components: {},
    props: {
        data: Object
    },
    data() {
        return {
            submitLoading: false,
            form: {
                userId: "",
                userName: "",
                insId: "",
                insName: "",
                status: "",
                price2: 0,
                price3: 0,
                price4: 0,
                price5: 0,
            },
            formValidate: {}
        };
    },
    methods: {
        init() {
            this.handleReset();
            this.form = this.data;
        },
        handleReset() {
            this.$refs.form.resetFields();
        },
        handleSubmit() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    editOrderOffline(this.form).then(res => {
                        this.submitLoading = false;
                        if (res.success) {
                            this.$Message.success("操作成功");
                            this.submited();
                        }
                    });
                }
            });
        },
        close() {
            this.$emit("close", true);
        },
        submited() {
            this.$emit("submited", true);
        }
    },
    mounted() {
        this.init();
    }
};
</script>

<style lang="less">
.edit-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    position: relative;

    .back-title {
        color: #515a6e;
        display: flex;
        align-items: center;
    }

    .head-name {
        display: inline-block;
        height: 20px;
        line-height: 20px;
        font-size: 16px;
        color: #17233d;
        font-weight: 500;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .window-close {
        z-index: 1;
        font-size: 12px;
        position: absolute;
        right: 0px;
        top: -5px;
        overflow: hidden;
        cursor: pointer;

        .ivu-icon-ios-close {
            color: #999;
            transition: color .2s ease;
        }
    }

    .window-close .ivu-icon-ios-close:hover {
        color: #444;
    }
}
</style>
