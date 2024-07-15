<template>
  <div class="doctor-dashboard-container">
    <div class="top-buttons">
      <button @click="goToDoctorProfile">个人信息</button>
      <button class="button5" @click="quit">退出登录</button>
    </div>
    <h2>医生工作台</h2>
    <p>欢迎，{{ username }}！</p>

    <!-- 发布出诊信息 -->
    <form @submit.prevent="publishSchedule">
      <h3>发布出诊信息</h3>
      <div>
        <label for="date">出诊日期:</label>
        <input type="date" id="date" v-model="schedule.date" required>
      </div>
      <div>
        <label for="time">出诊时间:</label>
        <input type="time" id="time" v-model="schedule.time" required>
      </div>
      <div>
        <label for="total">总挂号数量:</label>
        <input type="number" id="total" v-model.number="schedule.total" required>
      </div>
      <button type="submit">发布</button>
    </form>

    <!-- 查看出诊信息 -->
    <h3>查看出诊信息</h3>
    <div>
      <label for="filterDate">筛选日期:</label>
      <input type="date" id="filterDate" v-model="filterDate">
      <button @click="filterSchedules">查询</button>
    </div>
    <table class="info-table" v-if="filteredSchedules.length > 0">
      <thead>
      <tr>
        <th>日期</th>
        <th>时间</th>
        <th>总挂号数量</th>
        <th>已挂号数量</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="entry in filteredSchedules" :key="entry.id">
        <td>{{ entry.date }}</td>
        <td>{{ entry.hours }}</td>
        <td>{{ entry.totalNum }}</td>
        <td>{{ entry.bookedNum }}</td>
        <td><button @click="deleteSchedule(entry)">删除</button></td>
      </tr>
      </tbody>
    </table>

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
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const username = ref(route.query.username || '');

    const schedule = ref({
      date: '',
      time: '',
      total: 0,
    });

    const schedules = ref([]);
    const filterDate = ref('');
    const filteredSchedules = ref([]);

    const updateSuccess = ref(false);
    const updateError = ref(false);

    const publishSchedule = async () => {
      try {
        const response = await axios.post('/api/hospital/atteninfo/save_atteninfo', {
          doctor: username.value,
          date: schedule.value.date,
          hours: schedule.value.time,
          totalNum: schedule.value.total, // 确保为整数
        });
        if (response.data.msg === 'success') {
          alert('发布成功，等待管理员审核！');
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
        console.error('发布出诊信息请求出错', error);
      }
    };

    const deleteSchedule = async (entry) => {
      try {
        const response = await axios.post('/api/hospital/atteninfo/delete', entry);
        if (response.data.msg === 'success') {
          updateSuccess.value = true;
          updateError.value = false;
          schedules.value = schedules.value.filter(item => item.id !== entry.id);
          filteredSchedules.value = schedules.value;
          setTimeout(() => {
            updateSuccess.value = false;
          }, 3000); // 3秒后隐藏提示信息
        } else {
          updateError.value = true;
        }
      } catch (error) {
        updateError.value = true;
        console.error('删除出诊信息请求出错', error);
      }
    };

    const filterSchedules = async () => {
      try {
        const response = await axios.get('/api/hospital/atteninfo/self_atteninfo', {
          params: {doctor: username.value, date: filterDate.value},
        });
        if (response.data.msg === 'success') {
          if(response.data.data.length===0){
            alert('查询为空');
          }
          schedules.value = response.data.data;
          filteredSchedules.value = schedules.value;
        } else {
          alert('获取出诊信息失败');
        }
      } catch (error) {
        console.error('获取出诊信息请求出错', error);
        alert('获取出诊信息请求出错，请稍后重试');
      }
    };

    // 跳转到医生个人资料页面
    const goToDoctorProfile = () => {
      router.push({path: '/doctor-profile', query: {username: username.value}});
    };

    const quit = () => {
      router.push({path: '/'});
    };

    return {
      username,
      schedule,
      schedules,
      filterDate,
      filteredSchedules,
      updateSuccess,
      updateError,
      publishSchedule,
      deleteSchedule,
      filterSchedules,
      goToDoctorProfile,
      quit,
    };
  },
};
</script>

<style scoped>
.doctor-dashboard-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

form div {
  margin-bottom: 10px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input, button {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

button {
  margin-top: 10px;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.info-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  text-align: center;
}

.info-table th, .info-table td {
  border: 1px solid #dddddd;
  padding: 8px;
}

.info-table th {
  background-color: #007bff;
  color: white;
}

li button {
  width: auto;
  padding: 5px 10px;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

li button:hover {
  background-color: #c82333;
}

.top-buttons {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}

.top-buttons button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  margin-left: 10px;
}

.top-buttons button:hover {
  background-color: #0056b3;
}

.button5 {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.button5:hover {
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
