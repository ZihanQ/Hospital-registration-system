<template>
  <div>
    <div class="header">
      <button @click="goBack" class="back-button">返回</button>
      <h2>修改其他用户信息</h2>
    </div>

    <!-- 用户列表 -->
    <div>
      <h3>医生</h3>
      <ul>
        <li v-for="(doctor, index) in doctors" :key="index">
          <input type="checkbox" v-model="selectedUsers" :value="doctor">
          <span>{{ doctor.name }} ({{ doctor.id }})</span>
        </li>
      </ul>

      <h3>患者</h3>
      <ul>
        <li v-for="(patient, index) in patients" :key="index">
          <input type="checkbox" v-model="selectedUsers" :value="patient">
          <span>{{ patient.name }} ({{ patient.id }})</span>
        </li>
      </ul>
    </div>

    <!-- 操作按钮 -->
    <div>
      <button :disabled="selectedUsers.length !== 1" @click="editUser">修改账号信息</button>
      <button :disabled="selectedUsers.length === 0" @click="deleteUsers">删除账号</button>
    </div>

    <!-- 提示信息 -->
    <div v-if="updateSuccess" class="success-message">
      操作成功！
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
    const username = route.query.username || 'test';

    const doctors = ref([]);
    const patients = ref([]);
    const selectedUsers = ref([]);
    const updateSuccess = ref(false);

    // 从后端获取数据
    const fetchData = async () => {
      try {
        const [doctorsResponse, patientsResponse] = await Promise.all([
          axios.get('/api/hospital/doctor/listAll'),
          axios.get('/api/hospital/patient/listAll')
        ]);

        doctors.value = doctorsResponse.data.data;
        patients.value = patientsResponse.data.data;
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    onMounted(() => {
      fetchData();
    });

    const editUser = () => {
      if (selectedUsers.value.length === 1) {
        const user = selectedUsers.value[0];
        const userType = doctors.value.includes(user) ? 'doctor' : 'patient';
        router.push({ name: 'EditUser', query: { username: user.account, userType, currentUsername: username, userId: user.id } });
      }
    };

    const deleteUsers = async () => {
      try {
        for (const user of selectedUsers.value) {
          const userType = doctors.value.includes(user) ? 'doctor' : 'patient';
          const apiEndpoint = `/api/hospital/${userType}/delete`;
          await axios.delete(apiEndpoint, { params: { id: user.id } });
        }

        updateSuccess.value = true;
        setTimeout(() => {
          updateSuccess.value = false;
        }, 3000); // 3秒后隐藏提示信息

        // 从列表中移除已删除用户
        selectedUsers.value.forEach(user => {
          removeUserFromList(doctors.value, user);
          removeUserFromList(patients.value, user);
        });

        // 清空选择
        selectedUsers.value = [];
      } catch (error) {
        console.error('Error deleting users:', error);
      }
    };

    const removeUserFromList = (list, user) => {
      const index = list.findIndex(u => u.id === user.id);
      if (index !== -1) {
        list.splice(index, 1);
      }
    };

    const goBack = () => {
      router.push({ name: 'AdminDashboard', query: { username } });
    };

    return {
      doctors,
      patients,
      selectedUsers,
      updateSuccess,
      editUser,
      deleteUsers,
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

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 10px;
}

button {
  margin-right: 10px;
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
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
