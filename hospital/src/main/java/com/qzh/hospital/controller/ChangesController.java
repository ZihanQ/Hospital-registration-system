package com.qzh.hospital.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qzh.hospital.entity.Admin;
import com.qzh.hospital.entity.Changes;
import com.qzh.hospital.service.AdminService;
import com.qzh.hospital.service.ChangesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qiuzihan
 * @since 2024-06-30
 */
@RestController
@RequestMapping("/hospital/changes")
public class ChangesController {
    @Autowired
    private ChangesService changesService;
    @PostMapping("/save")
    public boolean save(@RequestBody Changes changes) {
        return changesService.save(changes);
    }
    @GetMapping("/list")
    public List<Changes> list() {
        return changesService.list();
    }

    @PostMapping("/mod")
    public boolean mod(@RequestBody Changes changes) {
        return changesService.updateById(changes);
    }

    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody Changes changes) {
        return changesService.saveOrUpdate(changes);
    }
    @GetMapping("/delete")
    public boolean delete(String id) {
        return changesService.removeById(id);
    }
    @PostMapping("/listP")
    public List<Changes> listP(@RequestBody Changes changes) {
        LambdaQueryWrapper<Changes> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Changes::getId, changes.getId());
        return changesService.list(queryWrapper);
    }
}
