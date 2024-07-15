package com.qzh.hospital.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qzh.hospital.common.ResponseFormat;
import com.qzh.hospital.entity.Admin;
import com.qzh.hospital.entity.Atteninfo;
import com.qzh.hospital.entity.Doctor;
import com.qzh.hospital.entity.Patient;
import com.qzh.hospital.service.DoctorService;
import com.qzh.hospital.service.PatientService;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qiuzihan
 * @since 2024-06-30
 */
@RestController
@RequestMapping("/hospital/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/delete")
    public ResponseFormat delete(@RequestParam String id) {
        doctorService.removeById(id);
        return ResponseFormat.suc();
    }

    @GetMapping("/listAll")
    public ResponseFormat listAll(){
        return ResponseFormat.suc((Long)doctorService.count(),doctorService.list());
    }


    @PostMapping("/find")
    public ResponseFormat find(@RequestBody Doctor doctor) {
        LambdaQueryWrapper<Doctor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Doctor::getAccount, doctor.getAccount())
                .eq(Doctor::getPassword, doctor.getPassword());
        if(doctorService.count(queryWrapper)>0){
            return ResponseFormat.suc();
        }
        return ResponseFormat.fail();
    }

    @GetMapping("/find_selfinfo") //获取自己信息
    public ResponseFormat find_selfinfo(@RequestParam Map<String,String> hs) {

        String account = hs.get("account");

        LambdaQueryWrapper<Doctor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Doctor::getAccount, account);
        Doctor doctor = doctorService.getOne(queryWrapper);

        if(doctorService.count(queryWrapper)>0){
            System.out.println(doctor);
            return ResponseFormat.suc(1L,doctor);
        }
        return ResponseFormat.fail();
    }

    @GetMapping("/find_doctor")
    public ResponseFormat find_doctor(@RequestParam String hospital,@RequestParam String department){
        LambdaQueryWrapper<Doctor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Doctor::getHospital, hospital)
                .eq(Doctor::getDepartment,department);
        List<Doctor> list = doctorService.list(queryWrapper);
        List<String> doctors = list.stream()
                .map(Doctor::getName)
                .distinct()  // 去重
                .collect(Collectors.toList());
        return ResponseFormat.suc(doctors);
    }
    @PostMapping("/mod_selfinfo")
    public ResponseFormat mod_selfinfo(@RequestBody Doctor doctor) {
        System.out.println(doctor);
        doctorService.updateById(doctor);
        return ResponseFormat.suc();
    }
}
