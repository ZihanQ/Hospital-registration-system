package com.qzh.hospital.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qzh.hospital.common.ResponseFormat;
import com.qzh.hospital.entity.Admin;
import com.qzh.hospital.entity.Doctor;
import com.qzh.hospital.entity.Patient;
import com.qzh.hospital.entity.Register;
import com.qzh.hospital.service.AdminService;
import com.qzh.hospital.service.DoctorService;
import com.qzh.hospital.service.PatientService;
import com.qzh.hospital.service.RegisterService;
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
@RequestMapping("/hospital/register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AdminService adminService;
    @PostMapping("/save")
    public ResponseFormat save(@RequestBody Register register) {
        LambdaQueryWrapper<Doctor> queryWrapper_D = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Admin> queryWrapper_A = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Patient> queryWrapper_P = new LambdaQueryWrapper<>();
        queryWrapper_D.eq(Doctor::getAccount, register.getAccount());
        queryWrapper_A.eq(Admin::getAccount, register.getAccount());
        queryWrapper_P.eq(Patient::getAccount, register.getAccount());
        System.out.println(register.getMode());
        if(register.getMode().equals("normal")){
            System.out.printf("111");
            long count_P = patientService.count(queryWrapper_P);
            if(count_P==0){
                registerService.save(register);
                return ResponseFormat.suc();
            }
        }

        else if(register.getMode().equals("doctor"))
        {
            long count_D = doctorService.count(queryWrapper_D);
            if(count_D==0){
                registerService.save(register);
                return ResponseFormat.suc();
            }
        }
        else if(register.getMode().equals("admin")) {
            long count_A = adminService.count(queryWrapper_A);
            if(count_A==0){
                registerService.save(register);
                return ResponseFormat.suc();
            }
        }
        System.out.println("222");
        return ResponseFormat.fail();
    }
    @PostMapping("/mod")
    public boolean mod(@RequestBody Register register) {
        return registerService.updateById(register);
    }
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody Register register) {
        return registerService.saveOrUpdate(register);
    }
    @GetMapping("/delete")
    public boolean delete(String id) {
        return registerService.removeById(id);
    }
    @PostMapping("/listP")
    public List<Register> listP(@RequestBody Register register) {
        LambdaQueryWrapper<Register> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Register::getId, register.getId());
        return registerService.list(queryWrapper);
    }

    @GetMapping("/list")
    public ResponseFormat list() {
        return ResponseFormat.suc(registerService.list());
    }

    @PostMapping("/mod_register_ok")
    public ResponseFormat mod_register_ok(@RequestBody Register register) {

        Register register1 = register;
        System.out.println(register.getMode());
        if (register1.getMode().equals("admin")){
            Admin admin1 =  new Admin();
            admin1.setPassword(register1.getPassword());
            admin1.setId(register1.getId());
            admin1.setAccount(register1.getAccount());
            registerService.removeById(register);
            adminService.save(admin1);
        } else if (register1.getMode().equals("doctor")){
            Doctor doctor1= new Doctor();
            doctor1.setPassword(register1.getPassword());
            doctor1.setId(register1.getId());
            doctor1.setAccount(register1.getAccount());
            registerService.removeById(register);
            doctorService.save(doctor1);
        } else {
            Patient patient1= new Patient();
            patient1.setPassword(register1.getPassword());
            patient1.setId(register1.getId());
            patient1.setAccount(register1.getAccount());
            registerService.removeById(register);
            patientService.save(patient1);
        }
        return ResponseFormat.suc();
    }
    @PostMapping("/mod_register_notok")
    public ResponseFormat mod_register_notok(@RequestBody Register register){
        registerService.removeById(register);
        return ResponseFormat.suc();
    }


}
