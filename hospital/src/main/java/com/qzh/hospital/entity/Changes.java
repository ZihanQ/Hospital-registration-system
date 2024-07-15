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
    @TableName("changes")
    @ApiModel(value = "Changes对象", description = "")
    public class Changes implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId("id")
    private String id;

        @TableField("name")
    private String name;

        @TableField("class1")
    private String class1;

        @TableField("change")
    private Integer change;


}




