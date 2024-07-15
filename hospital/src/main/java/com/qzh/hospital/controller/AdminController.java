package com.qzh.hospital.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qzh.hospital.common.ResponseFormat;
import com.qzh.hospital.entity.Admin;
import com.qzh.hospital.entity.Doctor;
import com.qzh.hospital.service.AdminService;
import com.qzh.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.LambdaConversionException;
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
@RequestMapping("/hospital/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @PostMapping("/save")
    public boolean save(@RequestBody Admin admin) {
        return adminService.save(admin);
    }
    @GetMapping("/list")
    public List<Admin> list() {
        return adminService.list();
    }

    @PostMapping("/mod_password")
    public ResponseFormat mod_password(@RequestBody Map<String, String> request) {
        String account = request.get("account");
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");
        System.out.println(oldPassword);
        System.out.println(newPassword);
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getAccount, account);
        Admin result = adminService.getOne(queryWrapper);
        System.out.println(result);
        if(result != null && oldPassword.equals(result.getPassword())){
            result.setPassword(newPassword);
            System.out.println(result.getPassword());
            System.out.println(result.getId());
            adminService.updateById(result);

            return ResponseFormat.suc();
        }

        return ResponseFormat.fail();
    }

    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody Admin admin) {
        return adminService.saveOrUpdate(admin);
    }
    @GetMapping("/delete")
    public boolean delete(String id) {
        return adminService.removeById(id);
    }
    @PostMapping("/find")//获得信息用于修改
    public ResponseFormat find(@RequestBody Admin admin) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getAccount, admin.getAccount())
                .eq(Admin::getPassword, admin.getPassword());
        if(adminService.count(queryWrapper)>0){
            return ResponseFormat.suc();
        }
        return ResponseFormat.fail();
    }

    @GetMapping("/find_selfinfo") //获取自己信息
    public ResponseFormat find_selfinfo(@RequestParam Map<String,String> hs) {

        String account = hs.get("account");

        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getAccount, account);
        Admin admin = adminService.getOne(queryWrapper);

        if(adminService.count(queryWrapper)>0){
            System.out.println(admin);
            return ResponseFormat.suc(1L,admin);
        }
        return ResponseFormat.fail();
    }

    @PostMapping("/mod_selfinfo")
    public ResponseFormat mod_selfinfo(@RequestBody Admin admin) {
        System.out.println(admin);
        adminService.updateById(admin);
        return ResponseFormat.suc();
    }
}
