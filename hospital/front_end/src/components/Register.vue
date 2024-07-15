<template>
  <div class="register-container">
    <h2>注册</h2>
    <form @submit.prevent="handleRegister">
      <div class="user-type">
        <button type="button" :class="{ active: userType === 'normal' }" @click="setType('normal')">普通用户</button>
        <button type="button" :class="{ active: userType === 'doctor' }" @click="setType('doctor')">医生</button>
        <button type="button" :class="{ active: userType === 'admin' }" @click="setType('admin')">管理员</button>
      </div>

      <label for="idNumber">身份证号:</label>
      <input type="text" id="idNumber" v-model="registerForm.idNumber" required>

      <label for="username">账号名:</label>
      <input type="text" id="username" v-model="registerForm.username" required>

      <label for="password">密码:</label>
      <input type="password" id="password" v-model="registerForm.password" required>

      <label for="confirmPassword">再次确认密码:</label>
      <input type="password" id="confirmPassword" v-model="registerForm.confirmPassword" required>

      <button type="submit">注册</button>
    </form>

    <div v-if="registerSuccess">
      <p>注册成功，请登录。</p>
      <router-link to="/login">去登录</router-link>
    </div>

    <div v-if="registerError">
      <p>注册失败：{{ registerErrorMessage }}</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Register',
  data() {
    return {
      registerForm: {
        idNumber: '',
        username: '',
        password: '',
        confirmPassword: '',
      },
      userType: 'normal', // 默认用户类型为普通用户
      registerSuccess: false,
      registerError: false,
      registerErrorMessage: '',
    };
  },
  methods: {
    setType(type) {
      this.userType = type;
    },
    handleRegister() {
      if (this.registerForm.password === this.registerForm.confirmPassword) {
        // 发送注册请求到服务器
        axios.post('/api/hospital/register/save', {
          id: this.registerForm.idNumber,
          account: this.registerForm.username,
          password: this.registerForm.password,
          mode: this.userType,

        })
            .then(response => {
              if (response.data.msg==="success") {
                this.registerSuccess = true;
                this.registerError = false;
              } else {

                this.registerSuccess = false;
                this.registerError = true;
                this.registerErrorMessage = '用户名已重复';
              }
            })
            .catch(error => {
              this.registerSuccess = false;
              this.registerError = true;
              this.registerErrorMessage = error.response.data.message || '注册请求失败';
            });
      } else {
        alert('两次输入的密码不匹配！');
      }
    },
  },
};
</script>

<style scoped>
.register-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('@/assets/img/background.png');
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column; /* 使内容垂直排列 */
}

h2 {
  color: white; /* 使标题颜色与背景形成对比 */
  margin-bottom: 20px;
}

form {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 300px;
}

.user-type {
  margin-bottom: 15px;
}

.user-type button {
  padding: 10px 20px;
  margin: 5px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color: #007bff;
  color: white;
}

.user-type button.active {
  background-color: #FEA4B0;
}

label {
  display: block;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
  margin-bottom: 15px;
}

button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
  width: 100%;
}

button:hover {
  background-color: #0056b3;
}
</style>
