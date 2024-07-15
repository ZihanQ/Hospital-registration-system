package com.qzh.hospital.service.impl;

import com.qzh.hospital.entity.Patient;
import com.qzh.hospital.mapper.PatientMapper;
import com.qzh.hospital.service.PatientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qiuzihan
 * @since 2024-07-02
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

}
