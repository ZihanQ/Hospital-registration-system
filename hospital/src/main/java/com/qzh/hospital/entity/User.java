package com.qzh.hospital.entity;

    import com.baomidou.mybatisplus.annotation.TableField;
    import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableName("user")
    @ApiModel(value = "User对象", description = "")
    public class User implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId("id")
    private String id;

        @TableField("name")
    private String name;

        @TableField("age")
    private Integer age;

        @TableField("gender")
    private Integer gender;

        @TableField("address")
    private String address;

        @TableField("phone")
    private String phone;

        @TableField("medical_record")
    private String medical_record;

        @TableField("hospital")
    private String hospital;

        @TableField("department")
    private String department;

        @TableField("title")
    private String title;

        @TableField("expertise")
    private String expertise;




}






