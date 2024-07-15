<template>
  <div>
    <div class="header">
      <button @click="goBack" class="back-button">返回</button>
      <h2>审批新用户</h2>
    </div>

    <!-- 用户列表 -->
    <div>
      <h3>新用户</h3>
      <ul>
        <li v-for="(user, index) in newUsers" :key="index">
          <input type="radio" name="user" v-model="selectedUser" :value="user">
          <span>{{ user.account }} ({{ user.mode }})</span>
        </li>
      </ul>
    </div>

    <!-- 操作按钮 -->
    <div>
      <button :disabled="!selectedUser" @click="approveUser">审批通过</button>
      <button :disabled="!selectedUser" @click="rejectUser">审批不通过</button>
    </div>

    <!-- 提示信息 -->
    <div v-if="updateSuccess" class="success-message">
      操作成功！
    </div>
    <div v-if="updateError" class="error-message">
      操作失败，请重试。
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

    const newUsers = ref([]);
    const selectedUser = ref(null);
    const updateSuccess = ref(false);
    const updateError = ref(false);

    onMounted(() => {
      fetchRegistrationInfo();
    });

    const fetchRegistrationInfo = async () => {
      try {
        const response = await axios.get('/api/hospital/register/list');
        if (response.data.msg === 'success') {
          newUsers.value = response.data.data;
        }
      } catch (error) {
        console.error('获取注册信息失败:', error);
      }
    };

    const approveUser = async () => {
      if (!selectedUser.value) return;

      try {
        const response = await axios.post('/api/hospital/register/mod_register_ok', {
          account: selectedUser.value.account,
          password: selectedUser.value.password,
          id: selectedUser.value.id,
          mode: selectedUser.value.mode,
        });
        if (response.data.msg === 'success') {
          updateSuccess.value = true;
          updateError.value = false;
          removeSelectedUser();
          setTimeout(() => {
            updateSuccess.value = false;
          }, 3000); // 3秒后隐藏提示信息
        } else {
          updateError.value = true;
        }
      } catch (error) {
        updateError.value = true;
        console.error('审批通过失败:', error);
      }
    };

    const rejectUser = async () => {
      if (!selectedUser.value) return;

      try {
        const response = await axios.post('/api/hospital/register/mod_register_notok', {
          account: selectedUser.value.account,
          password: selectedUser.value.password,
          id: selectedUser.value.id,
          mode: selectedUser.value.mode,
        });
        if (response.data.msg === 'success') {
          updateSuccess.value = true;
          updateError.value = false;
          removeSelectedUser();
          setTimeout(() => {
            updateSuccess.value = false;
          }, 3000); // 3秒后隐藏提示信息
        } else {
          updateError.value = true;
        }
      } catch (error) {
        updateError.value = true;
        console.error('审批不通过失败:', error);
      }
    };

    const removeSelectedUser = () => {
      newUsers.value = newUsers.value.filter(user => user !== selectedUser.value);
      selectedUser.value = null;
    };

    const goBack = () => {
      router.push({ name: 'AdminDashboard', query: { username } });
    };

    return {
      newUsers,
      selectedUser,
      updateSuccess,
      updateError,
      approveUser,
      rejectUser,
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

.error-message {
  margin-top: 20px;
  padding: 10px;
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
  border-radius: 5px;
}
</style>


