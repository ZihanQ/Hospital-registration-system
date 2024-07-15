package com.qzh.hospital.mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qzh.hospital.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qiuzihan
 * @since 2024-06-30
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    IPage pageC(IPage<User> page);
}

