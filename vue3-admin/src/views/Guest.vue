<template>
    <el-card class="guest-container">
        <template #header>
            <div class="header">
                <el-button @click="handleSolve" icon="el-icon-plus" size="small" type="primary">解除禁用</el-button>
                <el-button @click="handleForbid" icon="el-icon-delete" size="small" type="danger">禁用账户</el-button>
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
                    label="昵称"
                    prop="nickName"
            >
            </el-table-column>
            <el-table-column
                    label="登录名"
                    prop="loginName"
            >
            </el-table-column>
            <el-table-column
                    label="身份状态"
            >
                <template #default="scope">
          <span :style="scope.row.lockedFlag == 0 ? 'color: green;' : 'color: red;'">
            {{ scope.row.lockedFlag == 0 ? '正常' : '禁用' }}
          </span>
                </template>
            </el-table-column>
            <el-table-column
                    label="是否注销"
            >
                <template #default="scope">
          <span :style="scope.row.lockedFlag == 0 ? 'color: green;' : 'color: red;'">
            {{ scope.row.isDeleted == 0 ? '正常' : '注销' }}
          </span>
                </template>
            </el-table-column>
            <el-table-column
                    label="注册时间"
                    prop="createTime"
            >
            </el-table-column>
            <!-- <el-table-column
              label="操作"
              width="100"
            >
              <template #default="scope">
                <a style="cursor: pointer; margin-right: 10px" @confirm="handleSolve(scope.row)">解除禁用</a>
                <el-popconfirm
                  title="确定禁用吗？"
                  @confirm="handleForbid(scope.row)"
                >
                  <template #reference>
                    <a style="cursor: pointer">禁用账户</a>
                  </template>
                </el-popconfirm>
              </template>
            </el-table-column> -->
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

    export default {
        name: 'Guest',
        setup() {
            const multipleTable = ref(null);
            const state = reactive({
                loading: false,
                tableData: [], // 数据列表
                multipleSelection: [], // 选中项
                total: 0, // 总条数
                currentPage: 1, // 当前页
                pageSize: 10 // 分页大小
            });
            onMounted(() => {
                getGuestList()
            });
            // 获取轮播图列表
            const getGuestList = () => {
                state.loading = true;
                axios.get('/users', {
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
            // 选择项
            const handleSelectionChange = (val) => {
                state.multipleSelection = val
            };
            const changePage = (val) => {
                state.currentPage = val;
                getGuestList()
            };
            const handleSolve = () => {
                if (!state.multipleSelection.length) {
                    ElMessage.error('请选择项');
                    return
                }
                axios.put(`/users/0`, {
                    ids: state.multipleSelection.map(item => item.userId)
                }).then(() => {
                    ElMessage.success('解除成功');
                    getGuestList()
                })
            };
            const handleForbid = () => {
                if (!state.multipleSelection.length) {
                    ElMessage.error('请选择项');
                    return
                }
                axios.put(`/users/1`, {
                    ids: state.multipleSelection.map(item => item.userId)
                }).then(() => {
                    ElMessage.success('禁用成功');
                    getGuestList()
                })
            };
            return {
                ...toRefs(state),
                multipleTable,
                handleSelectionChange,
                getGuestList,
                changePage,
                handleSolve,
                handleForbid
            }
        }
    }
</script>

<style scoped>
    .guest-container {
        min-height: 100%;
    }

    .el-card.is-always-shadow {
        min-height: 100% !important;
    }
</style>