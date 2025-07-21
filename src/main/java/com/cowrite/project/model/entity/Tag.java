package com.cowrite.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * Tag 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_tag")
public class Tag extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId
    private Long id;

    /**
    * 标签名称
    */
    @TableField("name")
    private String name;

    /**
    * 标签颜色（Hex）
    */
    @TableField("color")
    private String color;

    public Long getId() {
    return id;
    }

    public void setId(Long id) {
    this.id = id;
    }
    public String getName() {
    return name;
    }

    public void setName(String name) {
    this.name = name;
    }
    public String getColor() {
    return color;
    }

    public void setColor(String color) {
    this.color = color;
    }
}
