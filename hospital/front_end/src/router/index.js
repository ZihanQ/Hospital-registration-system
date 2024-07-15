import { createRouter, createWebHistory } from 'vue-router';
import Home from '../components/Home.vue'; // 确保路径正确
import Login from '../components/Login.vue'; // 确保路径正确
import Register from '../components/Register.vue'; // 确保路径正确
// 用户部分
import Booking from '../components/Booking.vue';
import PatientProfile from "../components/PatientProfile.vue";
import BookingHistory from "../components/BookingHistory.vue"; // 确保路径正确
// 医生部分
import DoctorDashboard from "../components/DoctorDashboard.vue"; // 新增医生页面
import DoctorProfile from "../components/DoctorProfile.vue"; // 新增医生个人信息页面
// 管理员部分
import AdminDashboard from "../components/AdminDashboard.vue";
import ChangePassword from "../components/ChangePassword.vue";
import AdminChangeMyInformation from "../components/AdminChangeMyInformation.vue";
import ChangeOtherInformation from "../components/ChangeOtherInformation.vue";
import ExamineOthers from "../components/ExamineOthers.vue";
import EditUser from "../components/EditUser.vue"; // 确保路径正确
import ExamineAttenInfo from "../components/ExamineAttenInfo.vue"; // 新增的审批出诊信息文件路径

const routes = [
    { path: '/', name: 'Home', component: Home },
    { path: '/login', name: 'Login', component: Login },
    { path: '/register', name: 'Register', component: Register},
    { path: '/booking', name: 'Booking', component: Booking },
    { path: '/patient-profile', name: "PatientProfile", component: PatientProfile},
    { path: '/booking-history', name: "BookingHistory", component: BookingHistory},
    { path: '/doctor-dashboard', name: 'DoctorDashboard', component: DoctorDashboard }, // 新增医生页面路由
    { path: '/doctor-profile', name: 'DoctorProfile', component: DoctorProfile }, // 新增医生个人信息页面路由
    { path: '/admin-dashboard', name: "AdminDashboard", component: AdminDashboard},
    { path: '/change-password', name: "ChangePassword", component: ChangePassword},
    { path: '/admin-change-my-info', name: "AdminChangeMyInformation", component: AdminChangeMyInformation},
    { path: '/change-other-info', name: "ChangeOtherInformation", component: ChangeOtherInformation},
    { path: '/examine-others', name: "ExamineOthers", component: ExamineOthers},
    { path: '/edit-user', name: "EditUser", component: EditUser}, // 新增的路由
    { path: '/examine-atten-info', name: "ExamineAttenInfo", component: ExamineAttenInfo}, // 新增的审批出诊信息路由
];

const router = createRouter({
    history: createWebHistory('/'),
    routes
});

export default router;
