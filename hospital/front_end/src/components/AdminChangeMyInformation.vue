<template>
  <div>
    <div class="header">
      <button @click="goBack" class="back-button">返回</button>
      <h2>管理员信息</h2>
    </div>
    <p>用户名: {{ adminInfo.account }}</p>

    <!-- 管理员信息表单 -->
    <form @submit.prevent="updateAdminProfile">
      <div>
        <label for="id">身份证号:</label>
        <input type="text" id="id" v-model="adminInfo.id" readonly>
      </div>
      <div>
        <label for="account">账号:</label>
        <input type="text" id="account" v-model="adminInfo.account" readonly>
      </div>
      <div>
        <label for="name">姓名:</label>
        <input type="text" id="name" v-model="adminInfo.name">
      </div>
      <div>
        <label for="address">住址:</label>
        <input type="text" id="address" v-model="adminInfo.address">
      </div>
      <div>
        <label for="contact">联系方式:</label>
        <input type="text" id="contact" v-model="adminInfo.contact">
      </div>
      <div>
        <label for="password">密码:</label>
        <input type="password" id="password" v-model="adminInfo.password">
      </div>
      <button type="submit">保存修改</button>
    </form>

    <!-- 提示信息 -->
    <div v-if="updateSuccess" class="success-message">
      信息更新成功！
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const account = route.query.username || 'admin';

    const adminInfo = ref({
      id: '',
      account,
      name: '',
      address: '',
      contact: '',
      password: '',
    });

    const updateSuccess = ref(false);

    onMounted(async () => {
      try {
        const response = await axios.get('/api/hospital/admin/find_selfinfo', {
          params: { account },
        });
        if (response.data.msg === "success") {
          Object.assign(adminInfo.value, response.data.data);
        } else {
          alert('获取管理员信息失败');
        }
      } catch (error) {
        console.error('获取管理员信息请求出错', error);
        alert('获取管理员信息请求出错，请稍后重试');
      }
    });

    const updateAdminProfile = async () => {
      try {
        const response = await axios.post('/api/hospital/admin/mod_selfinfo', {
          id: adminInfo.value.id,
          account: adminInfo.value.account,
          name: adminInfo.value.name,
          address: adminInfo.value.address,
          contact: adminInfo.value.contact,
          password: adminInfo.value.password,
        });
        if (response.data.msg === "success") {
          updateSuccess.value = true;
          setTimeout(() => {
            updateSuccess.value = false;
          }, 3000); // 3秒后隐藏提示信息
        } else {
          alert('更新管理员信息失败');
        }
      } catch (error) {
        console.error('更新管理员信息请求出错', error);
        alert('更新管理员信息请求出错，请稍后重试');
      }
    };

    const goBack = () => {
      router.push({ name: 'AdminDashboard', query: { username: adminInfo.value.account } });
    };

    return {
      adminInfo,
      updateSuccess,
      updateAdminProfile,
      goBack,
    };
  },
};
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
}

.back-button {
  margin-right: 20px;
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.back-button:hover {
  background-color: #0056b3;
}

form div {
  margin-bottom: 10px;
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

input[readonly] {
  background-color: #f0f0f0;
  cursor: not-allowed;
}

button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.success-message {
  margin-top: 20px;
  padding: 10px;
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
  border-radius: 5px;
}
</style>
