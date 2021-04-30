<template>
    <el-card class="category-container">
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
                    width="55"
            >
            </el-table-column>
            <el-table-column
                    label="分类名称"
                    prop="categoryName"
            >
            </el-table-column>
            <el-table-column
                    label="排序值"
                    prop="categoryRank"
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
                    width="220"
            >
                <template #default="scope">
                    <a @click="handleEdit(scope.row.categoryId)" style="cursor: pointer; margin-right: 10px">修改</a>
                    <a @click="handleNext(scope.row)" style="cursor: pointer; margin-right: 10px">下级分类</a>
                    <el-popconfirm
                            @confirm="handleDeleteOne(scope.row.categoryId)"
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
    <DialogAddCategory :level="level" :parent_id="parent_id" :reload="getCategory" :type="type" ref='addGood'/>
</template>

<script>
    import {onMounted, onUnmounted, reactive, ref, toRefs} from 'vue'
    import {useRoute, useRouter} from 'vue-router'
    import {ElMessage} from 'element-plus'
    import DialogAddCategory from '@/components/DialogAddCategory.vue'
    import axios from '@/utils/axios'

    export default {
        name: 'Category',
        components: {
            DialogAddCategory
        },
        setup() {
            const multipleTable = ref(null);
            const addGood = ref(null);
            const router = useRouter();
            const route = useRoute();
            const state = reactive({
                loading: false,
                tableData: [], // 数据列表
                multipleSelection: [], // 选中项
                total: 0, // 总条数
                currentPage: 1, // 当前页
                pageSize: 10, // 分页大小
                type: 'add', // 操作类型
                level: 1,
                parent_id: 0
            });
            onMounted(() => {
                getCategory()
            });
            onUnmounted(() => {
                unwatch()
            });
            const unwatch = router.afterEach((to) => {
                if (['category', 'level2', 'level3'].includes(to.name)) {
                    getCategory()
                }
            });
            // 获取分类列表
            const getCategory = () => {
                const {level = 1, parent_id = 0} = route.query;
                state.loading = true;
                axios.get('/categories', {
                    params: {
                        pageNumber: state.currentPage,
                        pageSize: state.pageSize,
                        categoryLevel: level,
                        parentId: parent_id
                    }
                }).then(res => {
                    state.tableData = res.list;
                    state.total = res.totalCount;
                    state.currentPage = res.currPage;
                    state.loading = false;
                    state.level = level;
                    state.parentId = parent_id
                })
            };
            // 添加分类
            const handleAdd = () => {
                state.type = 'add';
                addGood.value.open()
            };
            // 修改分类
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
                axios.delete('/categories', {
                    data: {
                        ids: state.multipleSelection.map(i => i.categoryId)
                    }
                }).then(() => {
                    ElMessage.success('删除成功');
                    getCategory()
                })
            };
            // 单个删除
            const handleDeleteOne = (id) => {
                axios.delete('/categories', {
                    data: {
                        ids: [id]
                    }
                }).then(() => {
                    ElMessage.success('删除成功');
                    getCategory()
                })
            };
            const changePage = (val) => {
                state.currentPage = val;
                getCategory()
            };
            const handleNext = (item) => {
                const levelNumber = item.categoryLevel + 1;
                if (levelNumber == 4) {
                    ElMessage.error('没有下一级');
                    return
                }
                router.push({
                    name: `level${levelNumber}`,
                    query: {
                        level: levelNumber,
                        parent_id: item.categoryId
                    }
                })
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
                getCategory,
                changePage,
                handleNext
            }
        }
    }
</script>

<style scoped>
    .category-container {
        min-height: 100%;
    }

    .el-card.is-always-shadow {
        min-height: 100% !important;
    }
</style>