<template>
  <div>
    <div class="header">
      <button @click="goBack" class="back-button">返回</button>
      <h2>医生个人信息</h2>
    </div>
    <form @submit.prevent="updateProfile">
      <div>
        <label for="account">账号:</label>
        <input type="text" id="account" v-model="doctorInfo.account" readonly>
      </div>
      <div>
        <label for="id">身份证号:</label>
        <input type="text" id="id" v-model="doctorInfo.id" readonly>
      </div>
      <div>
        <label for="name">姓名:</label>
        <input type="text" id="name" v-model="doctorInfo.name">
      </div>
      <div>
        <label for="age">年龄:</label>
        <input type="number" id="age" v-model="doctorInfo.age">
      </div>
      <div>
        <label for="sex">性别:</label>
        <select id="sex" v-model="doctorInfo.sex">
          <option value="1">男</option>
          <option value="2">女</option>
          <option value="0">其他</option>
        </select>
      </div>
      <div>
        <label for="address">住址:</label>
        <input type="text" id="address" v-model="doctorInfo.address">
      </div>
      <div>
        <label for="contact">联系方式:</label>
        <input type="text" id="contact" v-model="doctorInfo.contact">
      </div>
      <div>
        <label for="hospital">所在医院:</label>
        <input type="text" id="hospital" v-model="doctorInfo.hospital">
      </div>
      <div>
        <label for="department">科室:</label>
        <input type="text" id="department" v-model="doctorInfo.department">
      </div>
      <div>
        <label for="title">职称:</label>
        <input type="text" id="title" v-model="doctorInfo.title">
      </div>
      <div>
        <label for="speciality">专长:</label>
        <input type="text" id="speciality" v-model="doctorInfo.speciality">
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

    const doctorInfo = ref({
      account: '',
      id: '',
      name: '',
      age: '',
      sex: '',
      address: '',
      contact: '',
      hospital: '',
      department: '',
      title: '',
      speciality: '',
    });

    const updateSuccess = ref(false);
    const updateError = ref(false);

    onMounted(() => {
      fetchDoctorInfo();
    });

    const fetchDoctorInfo = async () => {
      try {
        const response = await axios.get('/api/hospital/doctor/find_selfinfo', {
          params: { account: username.value },
        });
        if (response.data && response.data.data) {
          const data = response.data.data;
          doctorInfo.value = {
            account: data.account,
            id: data.id,
            name: data.name,
            age: data.age,
            sex: data.sex,
            address: data.address,
            contact: data.contact,
            hospital: data.hospital,
            department: data.department,
            title: data.title,
            speciality: data.speciality,  // 注意这个字段应该是 speciality
          };
        }
      } catch (error) {
        console.error('获取医生信息失败:', error);
      }
    };

    const updateProfile = async () => {
      try {
        const response = await axios.post('/api/hospital/doctor/mod_selfinfo', {
          account: doctorInfo.value.account,
          id: doctorInfo.value.id,
          name: doctorInfo.value.name,
          age: doctorInfo.value.age,
          sex: doctorInfo.value.sex,
          address: doctorInfo.value.address,
          contact: doctorInfo.value.contact,
          hospital: doctorInfo.value.hospital,
          department: doctorInfo.value.department,
          title: doctorInfo.value.title,
          speciality: doctorInfo.value.speciality,
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
        console.error('更新医生信息失败:', error);
      }
    };

    const goBack = () => {
      router.push({ path: '/doctor-dashboard', query: { username: username.value } });
    };

    return {
      username,
      doctorInfo,
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

back-button:hover {
  background-color: #0056b3;
}

form div {
  margin-bottom: 10px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input, select {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

input[readonly] {
  background-color: #f0f0f0;
  cursor: not-allowed;
}

button {
  margin-top: 10px;
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
