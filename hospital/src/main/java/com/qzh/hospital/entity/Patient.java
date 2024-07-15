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
* @since 2024-07-02
*/
    @Data
    @TableName("patient")
    @ApiModel(value = "Patient对象", description = "")
    public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId("id")
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


}




