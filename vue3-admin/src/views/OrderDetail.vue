<template>
    <el-card class="order-container">
        <div class="data">
            <el-card class="data-item" shadow="hover">
                <template #header>
                    <div class="card-header">
                        <span>订单状态</span>
                    </div>
                </template>
                <div>
                    {{ data.orderStatusString }}
                </div>
            </el-card>
            <el-card class="data-item" shadow="hover">
                <template #header>
                    <div class="card-header">
                        <span>创建时间</span>
                    </div>
                </template>
                <div>
                    {{ data.createTime }}
                </div>
            </el-card>
            <el-card class="data-item" shadow="hover">
                <template #header>
                    <div class="card-header">
                        <span>订单号</span>
                    </div>
                </template>
                <div>
                    {{ data.orderNo }}
                </div>
            </el-card>
        </div>
        <el-table
                :data="tableData"
                style="width: 100%"
                tooltip-effect="dark"
        >
            <el-table-column
                    label="商品图片"
            >
                <template #default="scope">
                    <img :key="scope.row.goodsId" :src="$filters.prefix(scope.row.goodsCoverImg)" alt="商品主图"
                         style="width: 100px">
                </template>
            </el-table-column>
            <el-table-column
                    label="商品编号"
                    prop="goodsId"
            >
            </el-table-column>
            <el-table-column
                    label="商品名"
                    prop="goodsName"
            ></el-table-column>
            <el-table-column
                    label="商品数量"
                    prop="goodsCount"
            >
            </el-table-column>
            <el-table-column
                    label="价格"
                    prop="sellingPrice"
            >
            </el-table-column>
        </el-table>
    </el-card>
</template>

<script>
    import {onMounted, reactive, toRefs} from 'vue'
    import {useRoute} from 'vue-router'
    import axios from '@/utils/axios'

    export default {
        name: 'OrderDetail',
        setup() {
            const route = useRoute();
            const {id} = route.query;
            const state = reactive({
                data: {},
                tableData: []
            });
            onMounted(() => {
                axios.get(`/orders/${id}`).then(res => {
                    console.log(res);
                    state.data = res;
                    state.tableData = res.newBeeMallOrderItemVOS
                })
            });

            return {
                ...toRefs(state)
            }
        }
    }
</script>

<style scoped>
    .data {
        display: flex;
        margin-bottom: 50px;
    }

    .data .data-item {
        flex: 1;
        margin: 0 10px;
    }

    .el-table {
        border: 1px solid #EBEEF5;
        border-bottom: none;
    }

    .has-gutter th {
        border-right: 1px solid #EBEEF5;
    }

    .has-gutter th:last-child {
        border-right: none;
    }

    .el-table__row td {
        border-right: 1px solid #EBEEF5;
    }

    .el-table__row td:last-child {
        border-right: none;
    }
</style>