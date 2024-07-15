<template>
  <div>
    <div class="header">
      <button @click="goBack" class="back-button">返回</button>
      <h2>个人信息</h2>
    </div>
    <p>用户名: {{ username }}</p>

    <!-- 患者信息表单 -->
    <form @submit.prevent="updateProfile">
      <div>
        <label for="id">身份证号:</label>
        <input type="text" id="id" v-model="patientInfo.id" readonly>
      </div>
      <div>
        <label for="name">姓名:</label>
        <input type="text" id="name" v-model="patientInfo.name">
      </div>
      <div>
        <label for="age">年龄:</label>
        <input type="number" id="age" v-model="patientInfo.age">
      </div>
      <div>
        <label for="gender">性别:</label>
        <select id="gender" v-model="patientInfo.gender">
          <option value="1">男</option>
          <option value="2">女</option>
          <option value="0">其他</option>
        </select>
      </div>
      <div>
        <label for="address">住址:</label>
        <input type="text" id="address" v-model="patientInfo.address">
      </div>
      <div>
        <label for="contact">联系方式:</label>
        <input type="text" id="contact" v-model="patientInfo.contact">
      </div>
      <div>
        <label for="password">密码:</label>
        <input type="password" id="password" v-model="patientInfo.password">
      </div>
      <button type="submit">保存修改</button>
    </form>

    <!-- 提示信息 -->
    <div v-if="updateSuccess" class="success-message">
      信息更新成功！
    </div>
    <div v-if="updateError" class="error-message">
      信息更新失败，请重试。
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const username = ref(route.query.username || '');

    const patientInfo = ref({
      id: '',
      name: '',
      age: '',
      gender: '',
      address: '',
      contact: '',
      account: '',
      password: '',
    });

    const updateSuccess = ref(false);
    const updateError = ref(false);

    onMounted(() => {
      fetchPatientInfo();
    });

    const fetchPatientInfo = async () => {
      try {
        const response = await axios.get('/api/hospital/patient/find_selfinfo', {
          params: { account: username.value },
        });
        if (response.data && response.data.data) {
          console.log('成功');
          const data = response.data.data;
          patientInfo.value = {
            id: data.id,
            name: data.name,
            age: data.age,
            gender: data.sex,
            address: data.address,
            contact: data.contact,
            account: data.account,
            password: data.password,
          };
        }
      } catch (error) {
        console.error('获取患者信息失败:', error);
      }
    };

    const updateProfile = async () => {
      try {
        const response = await axios.post('/api/hospital/patient/mod_selfinfo', {
          ...patientInfo.value,
        });
        if (response.data.msg === 'success') {
          updateSuccess.value = true;
          updateError.value = false;
          setTimeout(() => {
            updateSuccess.value = false;
          }, 3000); // 3秒后隐藏提示信息
        } else {
          updateError.value = true;
        }
      } catch (error) {
        updateError.value = true;
        console.error('更新患者信息失败:', error);
      }
    };

    const goBack = () => {
      router.push({ name: 'Booking', query: { username: username.value } });
    };

    return {
      username,
      patientInfo,
      updateSuccess,
      updateError,
      updateProfile,
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

input, select, textarea {
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

.error-message {
  margin-top: 20px;
  padding: 10px;
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
  border-radius: 5px;
}
</style>
