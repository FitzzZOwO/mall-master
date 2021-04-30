<template>
    <el-card class="swiper-container">
        <template #header>
            <div class="header">
                <el-button @click="handleAdd" icon="el-icon-plus" size="small" type="primary">增加</el-button>
                <el-popconfirm
                        @confirm="handleDelete"
                        title="确定删除吗？"
                >
                    <template #reference>
                        <el-button icon="el-icon-delete" size="small" type="danger">批量删除</el-button>
                    </template>
                </el-popconfirm>
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
                    label="轮播图"
                    width="200">
                <template #default="scope">
                    <img :src="scope.row.carouselUrl" alt="轮播图" style="width: 150px;height: 150px">
                </template>
            </el-table-column>
            <el-table-column
                    label="跳转链接"
            >
                <template #default="scope">
                    <a :href="scope.row.redirectUrl" target="_blank">{{ scope.row.redirectUrl }}</a>
                </template>
            </el-table-column>
            <el-table-column
                    label="排序值"
                    prop="carouselRank"
                    width="120"
            >
            </el-table-column>
            <el-table-column
                    label="添加时间"
                    prop="createTime"
                    width="200"
            >
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="100"
            >
                <template #default="scope">
                    <a @click="handleEdit(scope.row.carouselId)" style="cursor: pointer; margin-right: 10px">修改</a>
                    <el-popconfirm
                            @confirm="handleDeleteOne(scope.row.carouselId)"
                            title="确定删除吗？"
                    >
                        <template #reference>
                            <a style="cursor: pointer">删除</a>
                        </template>
                    </el-popconfirm>
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
    <DialogAddSwiper :reload="getCarousels" :type="type" ref='addGood'/>
</template>

<script>
    import {onMounted, reactive, ref, toRefs} from 'vue'
    import {ElMessage} from 'element-plus'
    import DialogAddSwiper from '@/components/DialogAddSwiper.vue'
    import axios from '@/utils/axios'

    export default {
        name: 'Swiper',
        components: {
            DialogAddSwiper
        },
        setup() {
            const multipleTable = ref(null);
            const addGood = ref(null);
            const state = reactive({
                loading: false,
                tableData: [], // 数据列表
                multipleSelection: [], // 选中项
                total: 0, // 总条数
                currentPage: 1, // 当前页
                pageSize: 10, // 分页大小
                type: 'add', // 操作类型
            });
            onMounted(() => {
                getCarousels()
            });
            // 获取轮播图列表
            const getCarousels = () => {
                state.loading = true;
                axios.get('/carousels', {
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
            // 添加轮播项
            const handleAdd = () => {
                state.type = 'add';
                addGood.value.open()
            };
            // 修改轮播图
            const handleEdit = (id) => {
                state.type = 'edit';
                addGood.value.open(id)
            };
            // 选择项
            const handleSelectionChange = (val) => {
                state.multipleSelection = val
            };
            // 批量删除
            const handleDelete = () => {
                if (!state.multipleSelection.length) {
                    ElMessage.error('请选择项');
                    return
                }
                axios.delete('/carousels', {
                    data: {
                        ids: state.multipleSelection.map(i => i.carouselId)
                    }
                }).then(() => {
                    ElMessage.success('删除成功');
                    getCarousels()
                })
            };
            // 单个删除
            const handleDeleteOne = (id) => {
                axios.delete('/carousels', {
                    data: {
                        ids: [id]
                    }
                }).then(() => {
                    ElMessage.success('删除成功');
                    getCarousels()
                })
            };
            const changePage = (val) => {
                state.currentPage = val;
                getCarousels()
            };
            return {
                ...toRefs(state),
                multipleTable,
                handleSelectionChange,
                addGood,
                handleAdd,
                handleEdit,
                handleDelete,
                handleDeleteOne,
                getCarousels,
                changePage
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