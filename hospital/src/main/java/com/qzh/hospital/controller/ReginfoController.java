package com.qzh.hospital.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qzh.hospital.common.ResponseFormat;
import com.qzh.hospital.entity.Admin;
import com.qzh.hospital.entity.Reginfo;
import com.qzh.hospital.service.AdminService;
import com.qzh.hospital.service.ReginfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/hospital/reginfo")
public class ReginfoController {
    @Autowired
    private ReginfoService reginfoService;

    @GetMapping("/find_reginfo")
    public ResponseFormat find_reginfo(@RequestParam Map<String,String> hs){
        LambdaQueryWrapper<Reginfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reginfo::getAccount,hs.get("account"));
        List<Reginfo> list = reginfoService.list(queryWrapper);
        return ResponseFormat.suc((long)reginfoService.count(queryWrapper),list);
    }

    @PostMapping("/save_reginfo")//挂号
    public ResponseFormat save_reginfo(@RequestBody Reginfo reginfo) {
        reginfoService.save(reginfo);
        return ResponseFormat.suc();
    }

}
