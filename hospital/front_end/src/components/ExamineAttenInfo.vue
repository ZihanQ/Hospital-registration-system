<template>
  <div>
    <div class="header">
      <button @click="goBack" class="back-button">返回</button>
      <h2>审批出诊信息</h2>
    </div>

    <!-- 出诊信息表格 -->
    <div>
      <table class="info-table">
        <thead>
        <tr>
          <th>选择</th>
          <th>日期</th>
          <th>时间</th>
          <th>医院</th>
          <th>科室</th>
          <th>医生</th>
          <th>总数量</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(info, index) in attenInfoList" :key="index">
          <td><input type="radio" v-model="selectedInfo" :value="info"></td>
          <td>{{ info.date }}</td>
          <td>{{ info.hours }}</td>
          <td>{{ info.hospital }}</td>
          <td>{{ info.department }}</td>
          <td>{{ info.doctor }}</td>
          <td>{{ info.totalNum }}</td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- 操作按钮 -->
    <div class="button-group">
      <button :disabled="!selectedInfo" @click="approveInfo">审批通过</button>
      <button :disabled="!selectedInfo" @click="rejectInfo">审批不通过</button>
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

    const attenInfoList = ref([]);
    const selectedInfo = ref(null);
    const updateSuccess = ref(false);
    const updateError = ref(false);

    const fetchData = async () => {
      try {
        const response = await axios.get('/api/hospital/atteninfo/list_atteninfo_ok');
        if (response.data.msg === 'success') {
          attenInfoList.value = response.data.data;
        } else {
          throw new Error('Failed to fetch data');
        }
      } catch (error) {
        updateError.value = true;
        setTimeout(() => {
          updateError.value = false;
        }, 3000); // 3秒后隐藏提示信息
      }
    };

    onMounted(() => {
      fetchData();
    });

    const approveInfo = async () => {
      try {
        const response = await axios.post('/api/hospital/atteninfo/mod_atteninfo_flag', selectedInfo.value);
        if (response.data.msg === 'success') {
          updateSuccess.value = true;
          setTimeout(() => {
            updateSuccess.value = false;
          }, 3000); // 3秒后隐藏提示信息

          // 重新获取数据
          await fetchData();

          // 清空选择
          selectedInfo.value = null;
        } else {
          throw new Error('Approval failed');
        }
      } catch (error) {
        updateError.value = true;
        setTimeout(() => {
          updateError.value = false;
        }, 3000); // 3秒后隐藏提示信息
      }
    };

    const rejectInfo = async () => {
      try {
        const response = await axios.post('/api/hospital/atteninfo/delete', selectedInfo.value);
        if (response.data.msg === 'success') {
          updateSuccess.value = true;
          setTimeout(() => {
            updateSuccess.value = false;
          }, 3000); // 3秒后隐藏提示信息

          // 重新获取数据
          await fetchData();

          // 清空选择
          selectedInfo.value = null;
        } else {
          throw new Error('Rejection failed');
        }
      } catch (error) {
        updateError.value = true;
        setTimeout(() => {
          updateError.value = false;
        }, 3000); // 3秒后隐藏提示信息
      }
    };

    const goBack = () => {
      router.push({ name: 'AdminDashboard', query: { username } });
    };

    return {
      attenInfoList,
      selectedInfo,
      updateSuccess,
      updateError,
      approveInfo,
      rejectInfo,
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

.info-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.info-table th, .info-table td {
  border: 1px solid #dddddd;
  text-align: center;
  padding: 8px;
}

.info-table th {
  background-color: #007bff;
  color: white;
}

.button-group {
  margin-top: 20px;
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
