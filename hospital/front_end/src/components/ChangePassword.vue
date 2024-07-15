<template>
  <div class="container">
    <form class="form" @submit.prevent="changePassword">
      <div class="type">账号类型：{{ type }}</div>
      <div class="account">用户名：{{ username }}</div>
      <div class="input">
        <label for="OldPassword">旧密码：</label>
        <input type="password" id="OldPassword" v-model="OldPassword" required>
      </div>
      <div class="input">
        <label for="NewPassword">新密码：</label>
        <input type="password" id="NewPassword" v-model="NewPassword" required>
      </div>
      <button class="button" type="submit">修改密码</button>
      <p class="message error" v-if="error">{{ error }}</p>
      <p class="message success" v-if="output">{{ output }}</p>
      <button class="button return" @click="goToAdminSystem">返回首页</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useRoute } from 'vue-router';

export default {
  name: 'ChangePassword',
  setup() {
    const route = useRoute(); // 获取当前路由对象
    const username = ref(route.query.username || ''); // 从路由参数中获取用户名
    const type = ref(route.query.type);
    const OldPassword = ref('');
    const NewPassword = ref('');
    const router = useRouter();
    const error = ref(''); // 用于存储错误信息
    const output = ref(''); // 用于存储成功消息或其他信息

    const changePassword = async () => {
      try {
        const response = await axios.post('/api/hospital/admin/mod_password', {
          account: username.value,
          oldPassword: OldPassword.value,
          newPassword: NewPassword.value
        });

        if(response.data.msg === "success") {
          alert('修改成功！');
          output.value = response.data.message;
          error.value = '';
        } else {
          alert('修改失败！');
        }
      } catch (err) {
        error.value = err.response.data.message || '修改密码失败';
        output.value = '';
      }
    };

    const goToAdminSystem = () => {
      router.push({
        path: '/admin-dashboard',
        query: {
          username: username.value
        }
      });
    };

    return {
      username,
      type,
      OldPassword,
      NewPassword,
      error,
      output,
      changePassword,
      goToAdminSystem
    };
  }
};
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.form {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 300px;
}

.type, .account {
  font-size: 18px;
  margin-bottom: 10px;
}

.input {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

.button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
  width: 100%;
}

.button:hover {
  background-color: #0056b3;
}

.message {
  margin-top: 10px;
}

.error {
  color: red;
}

.success {
  color: green;
}

.return {
  margin-top: 20px;
  background-color: #6c757d;
}

.return:hover {
  background-color: #5a6268;
}
</style>
