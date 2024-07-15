<template>
  <div>
    <div class="header">
      <button @click="goBack" class="back-button">返回挂号页面</button>
      <h2>挂号记录</h2>
    </div>
    <p>用户名: {{ username }}</p>
    <!-- 显示挂号记录 -->
    <ul>
      <li v-for="record in bookingRecords" :key="record.id">
        日期: {{ record.date }} | 时间: {{ record.hours }} | 医院: {{ record.hospital }} | 科室: {{ record.department }}
        | 医生: {{ record.doctor }}
      </li>
    </ul>
  </div>
</template>

<script>
import {ref, onMounted} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import axios from 'axios';

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const username = ref(route.query.username || '');
    const bookingRecords = ref([]);

    onMounted(async () => {
      try {
        const response = await axios.get('/api/hospital/reginfo/find_reginfo', {
          params: {account: username.value},
        });
        if (response.data.msg === "success") {
          bookingRecords.value = response.data.data; // 假设后端返回的数据结构中，挂号记录在data字段中
        } else {
          alert('获取挂号记录失败');
        }
      } catch (error) {
        console.error('获取挂号记录请求出错', error);
        alert('获取挂号记录请求出错，请稍后重试');
      }
    });

    const goBack = () => {
      router.push({name: 'Booking', query: {username: username.value}});
    };

    return {
      username,
      bookingRecords,
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
  padding: 10px;
  border-bottom: 1px solid #ccc;
}
</style>
