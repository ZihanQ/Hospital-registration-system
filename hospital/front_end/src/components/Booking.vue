<template>
  <div class="booking-container">
    <!-- 个人信息和挂号记录按钮 -->
    <div class="top-buttons">
      <button @click="goToPatientProfile">个人信息</button>
      <button @click="goToBookingHistory">挂号记录</button>
      <button class="button5" @click="quit">退出登录</button>
    </div>

    <h2>挂号页面</h2>
    <p>欢迎，{{ username }}！</p> <!-- 显示用户名 -->

    <!-- 选择日期 -->
    <div class="date-picker">
      <label for="date">选择日期:</label>
      <input type="date" id="date" v-model="selectedDate" required @change="validateDate">
    </div>

    <!-- 选择医院 -->
    <div class="hospital-selector">
      <label for="hospital">选择医院:</label>
      <select v-model="selectedHospital" @change="loadDepartments">
        <option value="">请选择医院</option>
        <option v-for="hosp in hospitals" :key="hosp.id" :value="hosp.id">
          {{ hosp.name }}
        </option>
      </select>
    </div>

    <!-- 选择科室 -->
    <div class="department-selector">
      <label for="department">选择科室:</label>
      <select v-model="selectedDepartment" @change="loadDoctors">
        <option value="">请选择科室</option>
        <option v-for="dept in departments" :key="dept.id" :value="dept.id">
          {{ dept.name }}
        </option>
      </select>
    </div>

    <!-- 选择医生 -->
    <div class="doctor-selector" v-if="doctors.length > 0">
      <label>选择医生:</label>
      <select v-model="selectedDoctor">
        <option v-for="doctor in doctors" :key="doctor.pseudoId" :value="doctor.pseudoId">
          {{ doctor.name }}
        </option>
      </select>
      <button @click="queryDoctors">查询</button>
    </div>

    <!-- 挂号信息展示 -->
    <div class="appointment-info" v-if="appointmentInfo.length > 0">
      <p>日期: {{ selectedDate }}</p>
      <p>医院: {{ selectedHospitalName }}</p>
      <p>科室: {{ selectedDepartmentName }}</p>
      <p v-if="selectedDoctor === 'all'">医生: 全部医生</p>
      <p v-else>医生: {{ selectedDoctorName }}</p>
      <ul>
        <li v-for="info in appointmentInfo" :key="info.id">
          医生: {{ info.doctor }} | 时间: {{ info.hours }} | 剩余数量: {{ info.totalNum - info.bookedNum }}
          <button @click="confirmBooking(info)">选择挂号</button>
        </li>
      </ul>
    </div>

    <!-- 挂号成功提示 -->
    <div v-if="bookingSuccess">
      <p>挂号成功！</p>
    </div>
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const username = ref(route.query.username || ''); // 从路由查询参数获取用户名
    const selectedDate = ref('');
    const selectedHospital = ref('');
    const selectedDepartment = ref('');
    const selectedDoctor = ref('all');
    const hospitals = ref([
      {id: '1', name: '北邮校医院'},
      {id: '2', name: '协和医院'},
      {id: '3', name: '湘雅医院'},
      {id: '4', name: '北京大学附属医院'},
      {id: '5', name: '北医三院'},
      // ...其他医院
    ]);
    const departments = ref([]);
    const doctors = ref([]);
    const appointmentInfo = ref([]);
    const bookingSuccess = ref(false);
    const formatDate = (date) => {
      return date.split('T')[0];
    };

    const validateDate = () => {
      const selected = new Date(selectedDate.value);
      const today = new Date();
      today.setDate(today.getDate());
      const threeDaysFromNow = new Date();
      threeDaysFromNow.setDate(today.getDate() + 3);

      if (selected < today || selected > threeDaysFromNow) {
        alert('请选择三天内的日期');
        selectedDate.value = '';
      }
    };

    const loadDepartments = () => {
      // 根据选中的医院加载科室列表
      if (selectedHospital.value) {
        departments.value = [
          {id: '1', name: '口腔科', hospitalId: selectedHospital.value},
          {id: '2', name: '泌尿科', hospitalId: selectedHospital.value},
          {id: '3', name: '眼科', hospitalId: selectedHospital.value},
          {id: '4', name: '骨科', hospitalId: selectedHospital.value},
          {id: '5', name: '神经外科', hospitalId: selectedHospital.value},
          // ...其他科室
        ];
        // 清空医生选择
        selectedDepartment.value = '';
        selectedDoctor.value = 'all';
        doctors.value = [];
      }
    };

    const loadDoctors = async () => {
      // 根据选中的科室和医院加载医生列表
      if (selectedHospital.value && selectedDepartment.value) {
        try {
          const response = await axios.get(`/api/hospital/doctor/find_doctor`, {
            params: {
              hospital: selectedHospitalName.value,
              department: selectedDepartmentName.value
            }
          });

          if (response.data.msg === 'success') {
            /// 生成前端ID或使用名字作为键值
            doctors.value = [{name: '全部医生', pseudoId: 'all'}];
            response.data.data.forEach((doctorName, index) => {
              doctors.value.push({name: doctorName, pseudoId: `doctor-${index}`});
            });
          } else {
            alert('加载医生列表失败，请稍后再试');
            doctors.value = [{name: '全部医生', pseudoId: 'all'}]; // 保证至少有“全部医生”这个选项
          }
        } catch (error) {
          console.error('加载医生列表出错:', error);
          alert('加载医生列表请求出错，请稍后重试');
          doctors.value = [{name: '全部医生', pseudoId: 'all'}];
        }
        selectedDoctor.value = 'all'; // 默认选择'全部医生'
      }
    };

    const queryDoctors = async () => {
      try {
        const requestPayload = {
          flag: selectedDoctor.value === 'all' ? '0' : '1',
          date: selectedDate.value,
          data: {
            hospital: selectedHospitalName.value,
            department: selectedDepartmentName.value,
            ...(selectedDoctor.value !== 'all' && {doctor: selectedDoctorName.value}),
          },
        };

        const response = await axios.post('/api/hospital/atteninfo/find_atteninfo', requestPayload);

        if (response.data.msg === "success") {
          appointmentInfo.value = response.data.data;
          if(response.data.data.length===0){
            alert('查询为空');
          }

        } else {
          alert('获取挂号信息失败');
        }
      } catch (error) {
        console.error('获取挂号信息请求出错', error);
        alert('获取挂号信息请求出错，请稍后重试');
      }
    };

    const confirmBooking = async (info) => {
      if (selectedDate.value && selectedHospital.value && selectedDepartment.value && selectedDoctor.value) {
        alert('请确认支付');
        // 模拟支付成功
        const paymentSuccess = true;
        if (paymentSuccess) {
          try {
            // 第一个API请求
            const saveReginfoResponse = await axios.post('/api/hospital/reginfo/save_reginfo', {
              account: username.value,
              doctor: info.doctor,
              hospital: selectedHospitalName.value,
              department: selectedDepartmentName.value,
              date: selectedDate.value,
              hours: info.hours,
            });

            if (saveReginfoResponse.data.msg !== 'success') {
              alert('挂号失败，请重试');
              return;
            }

            // 第二个API请求
            const modBookedNumResponse = await axios.post('/api/hospital/atteninfo/mod_booked_num', {
              doctor: info.doctor,
              hospital: selectedHospitalName.value,
              department: selectedDepartmentName.value,
              date: selectedDate.value,
            });

            if (modBookedNumResponse.data.msg !== 'success') {
              alert('挂号失败，请重试');
              return;
            }

            alert('支付成功！');
            bookingSuccess.value = true;
          } catch (error) {
            console.error('挂号请求出错', error);
            alert('挂号请求出错，请稍后重试');
          }
        }
      } else {
        alert('请填写所有必填项。');
      }
    };

    const selectedHospitalName = computed(() => {
      return hospitals.value.find(hosp => hosp.id === selectedHospital.value)?.name || '';
    });
    const selectedDepartmentName = computed(() => {
      return departments.value.find(dept => dept.id === selectedDepartment.value)?.name || '';
    });
    const selectedDoctorName = computed(() => {
      return selectedDoctor.value === 'all' ? '全部医生' : (doctors.value.find(doctor => doctor.pseudoId === selectedDoctor.value)?.name || '');
    });

    const goToPatientProfile = () => {
      router.push({path: '/patient-profile', query: {username: username.value}});
    };

    const goToBookingHistory = () => {
      router.push({path: '/booking-history', query: {username: username.value}});
    };

    const quit = () => {
      router.push({path: '/'});
    };

    return {
      username,
      selectedDate,
      selectedHospital,
      selectedDepartment,
      selectedDoctor,
      hospitals,
      departments,
      doctors,
      appointmentInfo,
      bookingSuccess,
      formatDate,
      validateDate,
      loadDepartments,
      loadDoctors,
      queryDoctors,
      confirmBooking,
      selectedHospitalName,
      selectedDepartmentName,
      selectedDoctorName,
      goToPatientProfile,
      goToBookingHistory,
      quit,
    };
  },
};
</script>

<style scoped>
.booking-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

/* 按钮样式 */
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

/* 选择医生按钮样式 */
.doctor-selector button {
  margin-top: 10px;
  padding: 10px 20px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.doctor-selector button:hover {
  background-color: #218838;
}

/* 其他样式 */
.appointment-info ul {
  list-style-type: none;
  padding: 0;
}

.appointment-info li {
  padding: 10px;
  border-bottom: 1px solid #ccc;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.appointment-info li button {
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.appointment-info li button:hover {
  background-color: #0056b3;
}
</style>

