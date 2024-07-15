package com.qzh.hospital.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qzh.hospital.common.ResponseFormat;
import com.qzh.hospital.entity.Admin;
import com.qzh.hospital.entity.Patient;
import com.qzh.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qiuzihan
 * @since 2024-06-30
 */
@RestController
@RequestMapping("/hospital/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @PostMapping("/delete")//管理员删除账号
    public ResponseFormat delete(@RequestParam String id) {
        patientService.removeById(id);
        return ResponseFormat.suc();
    }

    @PostMapping("/find") //查找是否有某人信息
    public ResponseFormat find(@RequestBody Patient patient) {
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Patient::getAccount, patient.getAccount())
                .eq(Patient::getPassword, patient.getPassword());
        if(patientService.count(queryWrapper)>0){
            return ResponseFormat.suc();
        }
        return ResponseFormat.fail();
    }

    @GetMapping("/listAll")
    public ResponseFormat listAll(){
        return ResponseFormat.suc((Long)patientService.count(),patientService.list());
    }

    @GetMapping("/find_selfinfo") //获取自己信息
    public ResponseFormat find_selfinfo(@RequestParam Map<String,String> hs) {

        String account = hs.get("account");

        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Patient::getAccount, account);
        Patient patient = patientService.getOne(queryWrapper);

        if(patientService.count(queryWrapper)>0){
            System.out.println(patient);
            return ResponseFormat.suc(1L,patient);
        }
        return ResponseFormat.fail();
    }

    @PostMapping("/mod_selfinfo")
    public ResponseFormat mod_selfinfo(@RequestBody Patient patient) {
        System.out.println(patient);
        patientService.updateById(patient);
        return ResponseFormat.suc();
    }


}
