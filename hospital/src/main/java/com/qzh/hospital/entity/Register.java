package com.qzh.hospital.entity;

    import com.baomidou.mybatisplus.annotation.TableField;
    import com.baomidou.mybatisplus.annotation.TableName;
    import java.io.Serializable;
    import java.time.LocalDateTime;

    import io.swagger.annotations.ApiModel;
    import io.swagger.annotations.ApiModelProperty;
    import lombok.Data;

/**
* <p>
    * 
    * </p>
*
* @author qiuzihan
* @since 2024-06-30
*/
    @Data
    @TableName("register")
    @ApiModel(value = "Register对象", description = "")
    public class Register implements Serializable {

    private static final long serialVersionUID = 1L;

        @TableField("account")
    private String account;

        @TableField("password")
    private String password;

        @TableField("mode")
    private String mode;

        @TableField("id")
    private String id;

}




