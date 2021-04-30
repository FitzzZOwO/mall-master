<template>
    <el-card class="swiper-container">
        <template #header>
            <div class="header">
                <el-button @click="handleAdd" icon="el-icon-plus" size="small" type="primary">新增商品</el-button>
            </div>
        </template>
        <el-table
                :data="tableData"
                @selection-change="handleSelectionChange"
                ref="multipleTable"
                style="width: 100%"
                tooltip-effect="dark"
                v-loading="loading">
            <el-table-column
                    type="selection"
                    width="55">
            </el-table-column>
            <el-table-column
                    label="商品编号"
                    prop="goodsId"
            >
            </el-table-column>
            <el-table-column
                    label="商品名"
                    prop="goodsName"
            >
            </el-table-column>
            <el-table-column
                    label="商品简介"
                    prop="goodsIntro"
            >
            </el-table-column>
            <el-table-column
                    label="商品图片"
                    width="150px"
            >
                <template #default="scope">
                    <img :key="scope.row.goodsId" :src="$filters.prefix(scope.row.goodsCoverImg)" alt="商品主图"
                         style="width: 100px; height: 100px;">
                </template>
            </el-table-column>
            <el-table-column
                    label="商品库存"
                    prop="stockNum"
            >
            </el-table-column>
            <el-table-column
                    label="商品售价"
                    prop="sellingPrice"
            >
            </el-table-column>
            <el-table-column
                    label="上架状态"
            >
                <template #default="scope">
                    <span style="color: green;" v-if="scope.row.goodsSellStatus == 0">销售中</span>
                    <span style="color: red;" v-else>已下架</span>
                </template>
            </el-table-column>

            <el-table-column
                    label="操作"
                    width="100"
            >
                <template #default="scope">
                    <a @click="handleEdit(scope.row.goodsId)" style="cursor: pointer; margin-right: 10px">修改</a>
                    <a @click="handleStatus(scope.row.goodsId, 1)" style="cursor: pointer; margin-right: 10px"
                       v-if="scope.row.goodsSellStatus == 0">下架</a>
                    <a @click="handleStatus(scope.row.goodsId, 0)" style="cursor: pointer; margin-right: 10px"
                       v-else>上架</a>
                </template>
            </el-table-column>
        </el-table>
        <!--总数超过一页，再展示分页器-->
        <el-pagination
                :current-page="currentPage"
                :page-size="pageSize"
                :total="total"
                @current-change="changePage"
                background
                layout="prev, pager, next"
        />
    </el-card>
</template>

<script>
    import {onMounted, reactive, ref, toRefs} from 'vue'
    import axios from '@/utils/axios'
    import {ElMessage} from 'element-plus'
    import {useRouter} from 'vue-router'

    export default {
        name: 'Good',
        setup() {
            const multipleTable = ref(null);
            const router = useRouter();
            const state = reactive({
                loading: false,
                tableData: [], // 数据列表
                multipleSelection: [], // 选中项
                total: 0, // 总条数
                currentPage: 1, // 当前页
                pageSize: 10 // 分页大小
            });
            onMounted(() => {
                getGoodList()
            });
            // 获取轮播图列表
            const getGoodList = () => {
                state.loading = true;
                axios.get('/goods/list', {
                    params: {
                        pageNumber: state.currentPage,
                        pageSize: state.pageSize
                    }
                }).then(res => {
                    state.tableData = res.list;
                    state.total = res.totalCount;
                    state.currentPage = res.currPage;
                    state.loading = false
                })
            };
            const handleAdd = () => {
                router.push({path: '/add'})
            };
            const handleEdit = (id) => {
                router.push({path: '/add', query: {id}})
            };
            // 选择项
            const handleSelectionChange = (val) => {
                state.multipleSelection = val
            };
            const changePage = (val) => {
                state.currentPage = val;
                getGoodList()
            };
            const handleStatus = (id, status) => {
                axios.put(`/goods/status/${status}`, {
                    ids: id ? [id] : []
                }).then(() => {
                    ElMessage.success('修改成功');
                    getGoodList()
                })
            };
            return {
                ...toRefs(state),
                multipleTable,
                handleSelectionChange,
                handleAdd,
                handleEdit,
                getGoodList,
                changePage,
                handleStatus
            }
        }
    }
</script>

<style scoped>
    .swiper-container {
        min-height: 100%;
    }

    .el-card.is-always-shadow {
        min-height: 100% !important;
    }
</style>