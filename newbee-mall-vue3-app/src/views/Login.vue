<!--
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 陈尼克 all rights reserved.
 * 版权所有，侵权必究！
 *
-->

<template>
  <div class="login">
    <s-header :back="'/home'" :name="type == 'login' ? '登录' : '注册'"></s-header>
    <img alt="" class="logo" src="https://s.yezgea02.com/1604045825972/newbee-mall-vue3-app-logo.png">
    <div class="login-body login" v-if="type == 'login'">
      <van-form @submit="onSubmit">
        <van-field
                :rules="[{ required: true, message: '请填写用户名' }]"
                label="用户名"
                name="username"
                placeholder="用户名"
                v-model="username"
        />
        <van-field
                :rules="[{ required: true, message: '请填写密码' }]"
                label="密码"
                name="password"
                placeholder="密码"
                type="password"
                v-model="password"
        />
        <van-field
                center
                clearable
                label="验证码"
                placeholder="输入验证码"
                v-model="verify"
        >
          <template #button>
            <vue-img-verify ref="verifyRef"/>
          </template>
        </van-field>
        <div style="margin: 16px;">
          <div @click="toggle('register')" class="link-register">立即注册</div>
          <van-button block color="#1baeae" native-type="submit" round>登录</van-button>
        </div>
      </van-form>
    </div>
    <div class="login-body register" v-else>
      <van-form @submit="onSubmit">
        <van-field
                :rules="[{ required: true, message: '请填写用户名' }]"
                label="用户名"
                name="username1"
                placeholder="用户名"
                v-model="username1"
        />
        <van-field
                :rules="[{ required: true, message: '请填写密码' }]"
                label="密码"
                name="password1"
                placeholder="密码"
                type="password"
                v-model="password1"
        />
        <van-field
                center
                clearable
                label="验证码"
                placeholder="输入验证码"
                v-model="verify"
        >
          <template #button>
            <vue-img-verify ref="verifyRef"/>
          </template>
        </van-field>
        <div style="margin: 16px;">
          <div @click="toggle('login')" class="link-login">已有登录账号</div>
          <van-button block color="#1baeae" native-type="submit" round>注册</van-button>
        </div>
      </van-form>
    </div>
  </div>
</template>

<script>
  import {reactive, ref, toRefs} from 'vue'
  import sHeader from '@/components/SimpleHeader'
  import vueImgVerify from '@/components/VueImageVerify'
  import {login, register} from '@/service/user'
  import {setLocal} from '@/common/js/utils'
  import md5 from 'js-md5'
  import {Toast} from 'vant'

  export default {
    setup() {
      const verifyRef = ref(null);
      const state = reactive({
        username: '',
        password: '',
        username1: '',
        password1: '',
        type: 'login',
        imgCode: '',
        verify: ''
      });

      // 切换登录和注册两种模式
      const toggle = (v) => {
        state.type = v;
        state.verify = ''
      };

      // 提交登录或注册表单
      const onSubmit = async (values) => {
        console.log('verifyRef.value.imgCode', verifyRef.value.imgCode);
        state.imgCode = verifyRef.value.imgCode || '';
        if (state.verify.toLowerCase() != state.imgCode.toLowerCase()) {
          Toast.fail('验证码有误');
          return
        }
        if (state.type == 'login') {
          const {data} = await login({
            "loginName": values.username,
            "passwordMd5": md5(values.password)
          });
          setLocal('token', data);
          // 需要刷新页面，否则 axios.js 文件里的 token 不会被重置
          window.location.href = '/'
        } else {
          await register({
            "loginName": values.username1,
            "password": values.password1
          });
          Toast.success('注册成功');
          state.type = 'login';
          state.verify = ''
        }
      };

      return {
        ...toRefs(state),
        toggle,
        onSubmit,
        verifyRef
      }
    },
    components: {
      sHeader,
      vueImgVerify
    }
  }
</script>

<style lang="less">
  .login {
    .logo {
      width: 120px;
      height: 120px;
      display: block;
      margin: 80px auto 20px;
    }

    .login-body {
      padding: 0 20px;
    }

    .login {
      .link-register {
        font-size: 14px;
        margin-bottom: 20px;
        color: #1989fa;
        display: inline-block;
      }
    }

    .register {
      .link-login {
        font-size: 14px;
        margin-bottom: 20px;
        color: #1989fa;
        display: inline-block;
      }
    }

    .verify-bar-area {
      margin-top: 24px;

      .verify-left-bar {
        border-color: #1baeae;
      }

      .verify-move-block {
        background-color: #1baeae;
        color: #fff;
      }
    }

    .verify {
      > div {
        width: 100%;
      }

      display: flex;
      justify-content: center;

      .cerify-code-panel {
        margin-top: 16px;
      }

      .verify-code {
        width: 40% !important;
        float: left !important;
      }

      .verify-code-area {
        float: left !important;
        width: 54% !important;
        margin-left: 14px !important;

        .varify-input-code {
          width: 90px;
          height: 38px !important;
          border: 1px solid #e9e9e9;
          padding-left: 10px;
          font-size: 16px;
        }

        .verify-change-area {
          line-height: 44px;
        }
      }
    }
  }
</style>
