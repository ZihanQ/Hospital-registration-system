package com.qzh.hospital.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qzh.hospital.common.GetFormat;
import com.qzh.hospital.common.ResponseFormat;
import com.qzh.hospital.entity.Atteninfo;
import com.qzh.hospital.entity.Doctor;
import com.qzh.hospital.service.AtteninfoService;
import com.qzh.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
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
@RequestMapping("/hospital/atteninfo")
public class AtteninfoController {
    @Autowired
    private AtteninfoService atteninfoService;

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/save")
    public boolean save(@RequestBody Atteninfo atteninfo) {
        return atteninfoService.save(atteninfo);
    }

    @GetMapping("/list")
    public List<Atteninfo> list() {
        return atteninfoService.list();
    }

    @PostMapping("/mod")
    public boolean mod(@RequestBody Atteninfo atteninfo) {
        return atteninfoService.updateById(atteninfo);
    }

    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody Atteninfo atteninfo) {
        return atteninfoService.saveOrUpdate(atteninfo);
    }

    @PostMapping("/delete")
    public ResponseFormat delete(@RequestBody Atteninfo atteninfo) {
        atteninfoService.removeById(atteninfo);
        return ResponseFormat.suc();
    }

    @PostMapping("/listP")
    public List<Atteninfo> listP(@RequestBody Atteninfo atteninfo) {
        LambdaQueryWrapper<Atteninfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Atteninfo::getSeq, atteninfo.getSeq());
        return atteninfoService.list(queryWrapper);
    }


    @PostMapping("/save_atteninfo")//发布出诊信息
    public ResponseFormat save_atteninfo(@RequestBody Atteninfo atteninfo) {
        String account = atteninfo.getDoctor();
        LambdaQueryWrapper<Doctor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Doctor::getAccount, account);
        Doctor doctor = doctorService.getOne(queryWrapper);
        atteninfo.setDoctor(doctor.getName());
        atteninfo.setDepartment(doctor.getDepartment());
        atteninfo.setHospital(doctor.getHospital());
        atteninfo.setBookedNum(0);
        atteninfo.setFlag(0);
        atteninfoService.save(atteninfo);
        return ResponseFormat.suc();
    }

    @GetMapping("/self_atteninfo")
    public ResponseFormat self_atteninfo(@RequestParam String doctor, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        System.out.println("Doctor: " + doctor);
        System.out.println("Date: " + date);
        LambdaQueryWrapper<Doctor> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Doctor::getAccount,doctor);
        Doctor doctor1 = doctorService.getOne(queryWrapper1);
        String doctor2 = doctor1.getName();
        LambdaQueryWrapper<Atteninfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Atteninfo::getDoctor, doctor2)
                .eq(Atteninfo::getDate, date)
                .eq(Atteninfo::getFlag, 1);
        List<Atteninfo> list = atteninfoService.list(queryWrapper);
        return ResponseFormat.suc(list);
    }

    @PostMapping("find_atteninfo")//（病人）查询出诊信息
    public ResponseFormat find_atteninfo(@RequestBody GetFormat getFormat) {
        LambdaQueryWrapper<Atteninfo> queryWrapper = new LambdaQueryWrapper<>();
        Map<String, String> hs = getFormat.getData();

        if (getFormat.getFlag().equals("1")) {
            queryWrapper.eq(Atteninfo::getDate, getFormat.getDate())
                    .eq(Atteninfo::getHospital, hs.get("hospital"))
                    .eq(Atteninfo::getDepartment, hs.get("department"))
                    .eq(Atteninfo::getDoctor, hs.get("doctor"));
            List<Atteninfo> list = atteninfoService.list(queryWrapper);
        Page<Atteninfo> page = new Page<>();
        page.setCurrent(getFormat.getPage_Num());
        page.setSize(getFormat.getPage_Size());
        IPage result = atteninfoService.page(page,queryWrapper);
            return ResponseFormat.suc(atteninfoService.count(queryWrapper), list);
        }
        queryWrapper.eq(Atteninfo::getDate, getFormat.getDate())
                .eq(Atteninfo::getHospital, hs.get("hospital"))
                .eq(Atteninfo::getDepartment, hs.get("department"));
        List<Atteninfo> list = atteninfoService.list(queryWrapper);
        System.out.println(list);
        return ResponseFormat.suc(atteninfoService.count(queryWrapper), list);
    }

    @PostMapping("/mod_booked_num")//更改已挂号数量
    public ResponseFormat mod_booked_num(@RequestBody Atteninfo atteninfo) {
        System.out.println(atteninfo.getBookedNum());
        LambdaQueryWrapper<Atteninfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Atteninfo::getDoctor, atteninfo.getDoctor())
                .eq(Atteninfo::getDate, atteninfo.getDate())
                .eq(Atteninfo::getDepartment, atteninfo.getDepartment())
                .eq(Atteninfo::getHospital, atteninfo.getHospital());
        //System.out.println(atteninfo);
        Atteninfo atteninfo1 = atteninfoService.getOne(queryWrapper);

        atteninfo1.setBookedNum(atteninfo1.getBookedNum() + 1);
        atteninfoService.updateById(atteninfo1);
        //System.out.println(list);
        return ResponseFormat.suc();
    }

    @GetMapping("/list_atteninfo_ok")
    public ResponseFormat list_atteninfo_ok() {
        LambdaQueryWrapper<Atteninfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Atteninfo::getFlag, 0);
        List<Atteninfo> list = atteninfoService.list(queryWrapper);
        Long count = atteninfoService.count(queryWrapper);
        System.out.println(list);
        return ResponseFormat.suc(count, list);
    }

    @PostMapping("/mod_atteninfo_flag")
    public ResponseFormat mod_atteninfo_flag(@RequestBody Atteninfo atteninfo) {
        LambdaQueryWrapper<Atteninfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Atteninfo::getDoctor, atteninfo.getDoctor())
                .eq(Atteninfo::getDate, atteninfo.getDate())
                .eq(Atteninfo::getDepartment, atteninfo.getDepartment())
                .eq(Atteninfo::getHospital, atteninfo.getHospital());
        //System.out.println(atteninfo);
        Atteninfo atteninfo1 = atteninfo;

        atteninfo1.setFlag(1);
        atteninfoService.updateById(atteninfo1);
        //System.out.println(list);
        return ResponseFormat.suc();
    }



}
