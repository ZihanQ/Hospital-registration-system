<template>
  <div class="container">
    <div class="background"></div>
    <form class="form">
      <div class="welcome">欢迎，{{ username }}！</div>
      <div class="features">
        <button class="button" @click="goToChangePassword">修改密码</button>
        <button class="button" @click="goToAdminChangeMyInformation">修改自身信息</button>
        <button class="button" @click="goToChangeOtherInformation">修改他人信息</button>
        <button class="button" @click="goToExamineOthers">新用户审批</button>
        <button class="button" @click="goToExamineAttenInfo">出诊信息审批</button>
        <button class="button" @click="quit">退出登录</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useRoute } from 'vue-router';

export default {
  name: 'AdminDashboard',
  setup() {
    const route = useRoute(); // 获取当前路由对象
    const username = ref(route.query.username || ''); // 从路由参数中获取用户名
    const router = useRouter();

    const goToChangePassword = () => {
      router.push({
        path: '/change-password',
        query: {
          type: '管理员',
          username: username.value
        }
      });
    };

    const goToAdminChangeMyInformation = () => {
      router.push({
        path: '/admin-change-my-info',
        query: {
          type: 'admin',
          username: username.value
        }
      });
    };

    const goToChangeOtherInformation = () => {
      router.push({
        path: '/change-other-info',
        query: {
          username: username.value
        }
      });
    };

    const goToExamineOthers = () => {
      router.push({
        path: '/examine-others',
        query: {
          username: username.value
        }
      });
    };

    const goToExamineAttenInfo = () => {
      router.push({
        path: '/examine-atten-info',
        query: {
          username: username.value
        }
      });
    };

    const quit = () => {
      router.push({
        path: '/',
      });
    };

    return {
      goToChangePassword,
      goToAdminChangeMyInformation,
      goToChangeOtherInformation,
      goToExamineOthers,
      goToExamineAttenInfo,
      quit,
      username,
    };
  }
}
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #007bff;
  z-index: -1;
}

.form {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.welcome {
  font-size: 24px;
  margin-bottom: 20px;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.button:hover {
  background-color: #0056b3;
}

.button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
</style>
