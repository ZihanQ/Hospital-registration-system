<template>
  <div class="login-container">
    <div class="stars" ref="starsRef">
      <div class="star" v-for="(item, index) in starsCount" :key="index"></div>
    </div>
    <form class="form" @submit.prevent="handleSubmit">
      <h2>登录</h2>
      <div class="user-type">
        <button type="button" :class="{ active: userType === 'normal' }" @click="setType('normal')">普通用户</button>
        <button type="button" :class="{ active: userType === 'doctor' }" @click="setType('doctor')">医生</button>
        <button type="button" :class="{ active: userType === 'admin' }" @click="setType('admin')">管理员</button>
      </div>

      <!-- 用户名和密码输入 -->
      <div class="input">
        <label for="username">用户名:</label>
        <input type="text" id="username" v-model="username" required>
      </div>
      <div class="input">
        <label for="password">密码:</label>
        <input type="password" id="password" v-model="password" required>
      </div>

      <!-- 登录按钮 -->
      <button class="login-button" type="submit">登录</button>
    </form>
  </div>
</template>

<script>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  setup() {
    const router = useRouter(); // 设置路由
    const username = ref('');
    const password = ref('');
    const userType = ref('normal'); // 用户类型

    let starsRef = ref(null);

    const starsCount = 800; // 星星数量
    const distance = 900; // 间距

    onMounted(() => {
      let starNodes = Array.from(starsRef.value.children);
      starNodes.forEach((item) => {
        let speed = 0.2 + Math.random() * 1;
        let thisDistance = distance + Math.random() * 300;
        item.style.transformOrigin = `0 0 ${thisDistance}px`;
        item.style.transform = `
        translate3d(0,0,-${thisDistance}px)
        rotateY(${Math.random() * 360}deg)
        rotateX(${Math.random() * -50}deg)
        scale(${speed},${speed})`;
      });
    });

    // setType 方法用于选择用户类型
    const setType = (type) => {
      userType.value = type;
    };

    // 异步登录请求
    const handleSubmit = async () => {
      try {
        // 根据用户类型选择 API 路径
        let apiPath = '';
        if (userType.value === 'normal') {
          apiPath = '/api/hospital/patient/find';
        } else if (userType.value === 'doctor') {
          apiPath = '/api/hospital/doctor/find';
        } else if (userType.value === 'admin') {
          apiPath = '/api/hospital/admin/find';
        }

        const response = await axios.post(apiPath, {
          account: username.value,
          password: password.value,
        });

        if (response.data.msg === "success") {
          alert('登录成功');
          // 根据用户类型跳转到相应的页面
          const redirectPath = userType.value === 'normal'
              ? { path: '/booking', query: { username: username.value } }
              : userType.value === 'doctor'
                  ? { path: '/doctor-dashboard', query: { username: username.value } }
                  : { path: '/admin-dashboard', query: { username: username.value } };

          router.push(redirectPath);
        } else {
          alert('登录失败，检查用户名和密码');
        }
      } catch (error) {
        console.error('登录请求出错', error);
        alert('登录请求出错，请稍后重试');
      }
    };

    return {
      username,
      password,
      userType,
      setType,
      handleSubmit,
      starsRef,
      starsCount
    };
  },
};
</script>

<style scoped>
.login-container {
  position: absolute;
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  background-attachment: fixed;
  overflow: hidden;
  background-image: url('@/assets/img/background.png');
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
}

.stars {
  transform: perspective(500px);
  transform-style: preserve-3d;
  position: absolute;
  perspective-origin: 50% 100%;
  left: 45%;
  animation: rotate 90s infinite linear;
  bottom: 0;
  z-index: -1;
}

.star {
  width: 2px;
  height: 2px;
  background: #f7f7b6;
  position: absolute;
  left: 0;
  top: 0;
  backface-visibility: hidden;
}

@keyframes rotate {
  0% {
    transform: perspective(400px) rotateZ(20deg) rotateX(-40deg) rotateY(0);
  }
  100% {
    transform: perspective(400px) rotateZ(20deg) rotateX(-40deg) rotateY(-360deg);
  }
}

.form {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 300px;
  position: relative;
  z-index: 1;
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

.input {
  margin-bottom: 15px;
  text-align: left;
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

.login-button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
  width: 100%;
}

.login-button:hover {
  background-color: #0056b3;
}
</style>
