package com.qzh.hospital.entity;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableField;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.baomidou.mybatisplus.annotation.TableName;
    import java.io.Serializable;
    import java.time.LocalDate;
    import java.time.LocalTime;
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
    @TableName("reginfo")
    @ApiModel(value = "Reginfo对象", description = "")
    public class Reginfo implements Serializable {

    private static final long serialVersionUID = 1L;

        @TableField("department")
    private String department;

        @TableField("name")
    private String name;

        @TableField("hospital")
    private String hospital;

        @TableField("date")
    private LocalDate date;

        @TableField("hours")
    private LocalTime hours;

        @TableField("doctor")
    private String doctor;

        @TableField("id")
    private String id;

            @TableId(value = "seq", type = IdType.AUTO)
    private Integer seq;

        @TableField("account")
    private String account;


}




