package com.qzh.hospital.entity;

    import com.baomidou.mybatisplus.annotation.TableField;
    import com.baomidou.mybatisplus.annotation.TableName;
    import java.io.Serializable;
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
    @TableName("doctor")
    @ApiModel(value = "Doctor对象", description = "")
    public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

        @TableField("id")
    private String id;

        @TableField("name")
    private String name;

        @TableField("age")
    private Integer age;

        @TableField("sex")
    private String sex;

        @TableField("address")
    private String address;

        @TableField("contact")
    private String contact;

        @TableField("account")
    private String account;

        @TableField("password")
    private String password;

        @TableField("hospital")
    private String hospital;

        @TableField("department")
    private String department;

        @TableField("title")
    private String title;

        @TableField("speciality")
    private String speciality;


}




