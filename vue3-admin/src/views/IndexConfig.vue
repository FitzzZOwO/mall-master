<template>
    <el-card class="index-container">
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
                    label="商品名称"
                    prop="configName"
            >
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
                    prop="configRank"
                    width="120"
            >
            </el-table-column>
            <el-table-column
                    label="商品编号"
                    prop="goodsId"
                    width="200"
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
                    <a @click="handleEdit(scope.row.configId)" style="cursor: pointer; margin-right: 10px">修改</a>
                    <el-popconfirm
                            @confirm="handleDeleteOne(scope.row.configId)"
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
    <DialogAddGood :configType="configType" :reload="getIndexConfig" :type="type" ref='addGood'/>
</template>

<script>
    import {onMounted, onUnmounted, reactive, ref, toRefs} from 'vue'
    import {ElMessage} from 'element-plus'
    import DialogAddGood from '@/components/DialogAddGood.vue'
    import {useRoute, useRouter} from 'vue-router'
    import axios from '@/utils/axios'
    // 首页配置类型参数
    const configTypeMap = {
        hot: 3,
        new: 4,
        recommend: 5
    };
    export default {
        name: 'Hot',
        components: {
            DialogAddGood
        },
        setup() {
            const router = useRouter();
            const route = useRoute();
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
                configType: 3 // 3-(首页)热销商品 4-(首页)新品上线 5-(首页)为你推荐
            });
            // 监听路由变化
            const unwatch = router.beforeEach((to) => {
                if (['hot', 'new', 'recommend'].includes(to.name)) {
                    state.configType = configTypeMap[to.name];
                    state.currentPage = 1;
                    getIndexConfig()
                }
            });
            // 初始化
            onMounted(() => {
                state.configType = configTypeMap[route.name];
                getIndexConfig()
            });
            onUnmounted(() => {
                unwatch()
            });
            // 首页热销商品列表
            const getIndexConfig = () => {
                state.loading = true;
                axios.get('/indexConfigs', {
                    params: {
                        pageNumber: state.currentPage,
                        pageSize: state.pageSize,
                        configType: state.configType
                    }
                }).then(res => {
                    state.tableData = res.list;
                    state.total = res.totalCount;
                    state.currentPage = res.currPage;
                    state.loading = false
                })
            };
            // 添加商品
            const handleAdd = () => {
                state.type = 'add';
                addGood.value.open()
            };
            // 修改商品
            const handleEdit = (id) => {
                state.type = 'edit';
                addGood.value.open(id)
            };
            // 选择项
            const handleSelectionChange = (val) => {
                state.multipleSelection = val
            };
            // 删除
            const handleDelete = () => {
                if (!state.multipleSelection.length) {
                    ElMessage.error('请选择项');
                    return
                }
                axios.post('/indexConfigs/delete', {
                    ids: state.multipleSelection.map(i => i.configId)
                }).then(() => {
                    ElMessage.success('删除成功');
                    getIndexConfig()
                })
            };
            // 单个删除
            const handleDeleteOne = (id) => {
                axios.post('/indexConfigs/delete', {
                    ids: [id]
                }).then(() => {
                    ElMessage.success('删除成功');
                    getIndexConfig()
                })
            };
            const changePage = (val) => {
                state.currentPage = val;
                getIndexConfig()
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
                getIndexConfig,
                changePage
            }
        }
    }
</script>

<style scoped>
    .index-container {
        min-height: 100%;
    }

    .el-card.is-always-shadow {
        min-height: 100% !important;
    }
</style>