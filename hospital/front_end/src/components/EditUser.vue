<template>
  <div>
    <div class="header">
      <button @click="goBack" class="back-button">返回</button>
      <h2>修改用户信息</h2>
    </div>

    <!-- 显示账号名 -->
    <div class="username">
      <p>账号名: {{ username }}</p>
    </div>

    <!-- 用户信息表单 -->
    <form @submit.prevent="updateUser">
      <div v-if="userType === 'doctor'">
        <div>
          <label for="account">账号:</label>
          <input type="text" id="account" v-model="userInfo.account" readonly>
        </div>
        <div>
          <label for="id">身份证号:</label>
          <input type="text" id="id" v-model="userInfo.id" readonly>
        </div>
        <div>
          <label for="name">姓名:</label>
          <input type="text" id="name" v-model="userInfo.name">
        </div>
        <div>
          <label for="age">年龄:</label>
          <input type="number" id="age" v-model="userInfo.age">
        </div>
        <div>
          <label for="sex">性别:</label>
          <select id="sex" v-model="userInfo.sex">
            <option value="1">男</option>
            <option value="2">女</option>
            <option value="0">其他</option>
          </select>
        </div>
        <div>
          <label for="address">住址:</label>
          <input type="text" id="address" v-model="userInfo.address">
        </div>
        <div>
          <label for="contact">联系方式:</label>
          <input type="text" id="contact" v-model="userInfo.contact">
        </div>
        <div>
          <label for="hospital">所在医院:</label>
          <input type="text" id="hospital" v-model="userInfo.hospital">
        </div>
        <div>
          <label for="department">科室:</label>
          <input type="text" id="department" v-model="userInfo.department">
        </div>
        <div>
          <label for="title">职称:</label>
          <input type="text" id="title" v-model="userInfo.title">
        </div>
        <div>
          <label for="speciality">专长:</label>
          <input type="text" id="speciality" v-model="userInfo.speciality">
        </div>
        <div>
          <label for="password">密码:</label>
          <input type="password" id="password" v-model="userInfo.password">
        </div>
      </div>

      <div v-else-if="userType === 'patient'">
        <div>
          <label for="account">账号:</label>
          <input type="text" id="account" v-model="userInfo.account" readonly>
        </div>
        <div>
          <label for="idNumber">身份证号:</label>
          <input type="text" id="idNumber" v-model="userInfo.idNumber" readonly>
        </div>
        <div>
          <label for="name">姓名:</label>
          <input type="text" id="name" v-model="userInfo.name">
        </div>
        <div>
          <label for="age">年龄:</label>
          <input type="number" id="age" v-model="userInfo.age">
        </div>
        <div>
          <label for="gender">性别:</label>
          <select id="gender" v-model="userInfo.gender">
            <option value="1">男</option>
            <option value="2">女</option>
            <option value="0">其他</option>
          </select>
        </div>
        <div>
          <label for="address">住址:</label>
          <input type="text" id="address" v-model="userInfo.address">
        </div>
        <div>
          <label for="contact">联系方式:</label>
          <input type="text" id="contact" v-model="userInfo.contact">
        </div>
        <div>
          <label for="password">密码:</label>
          <input type="password" id="password" v-model="userInfo.password">
        </div>
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
    const currentUsername = route.query.currentUsername || 'test';
    const selectedUsername = route.query.username;
    const userType = route.query.userType;
    const username = selectedUsername;

    const userInfo = ref({
      idName: '',
      age: '',
      sex: 'male',
      address: '',
      contact: '',
      password: '',
      hospital: '',
      department: '',
      title: '',
      speciality: '',
      id: '',
      account: ''
    });

    const updateSuccess = ref(false);

    const fetchUserInfo = async () => {
      try {
        const apiEndpoint = `/api/hospital/${userType}/find_selfinfo`;
        const response = await axios.get(apiEndpoint, { params: { account: selectedUsername } });
        if (response.data && response.data.data) {
          Object.assign(userInfo.value, response.data.data);
        }
      } catch (error) {
        console.error('Error fetching user info:', error);
      }
    };

    onMounted(() => {
      fetchUserInfo();
    });

    const updateUser = async () => {
      try {
        const apiEndpoint = `/api/hospital/${userType}/mod_selfinfo`;
        await axios.post(apiEndpoint, userInfo.value);
        updateSuccess.value = true;
        setTimeout(() => {
          updateSuccess.value = false;
        }, 3000); // 3秒后隐藏提示信息
      } catch (error) {
        console.error('Error updating user info:', error);
      }
    };

    const goBack = () => {
      router.push({ name: 'ChangeOtherInformation', query: { username: currentUsername } });
    };

    return {
      username,
      userInfo,
      updateSuccess,
      updateUser,
      goBack,
      userType,
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

.username {
  margin: 20px 0;
  font-size: 18px;
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
