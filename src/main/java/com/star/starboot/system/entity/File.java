package com.star.starboot.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.star.starboot.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文件上传表
 * </p>
 *
 * @author xpy
 * @since 2019-07-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_file")
public class File extends AbstractEntity {


    @TableId(value = "FILE_ID")
    private String fileId;
    /**
     * 绝对路径
     */
    @TableField("ABSOLUTE_PATH")
    private String absolutePath;
    /**
     * 文件大小
     */
    @TableField("FILE_SIZE")
    private Long fileSize;
    /**
     * 文件类型
     */
    @TableField("FILE_TYPE")
    private String fileType;
    /**
     * 旧文件名
     */
    @TableField("OLD_NAME")
    private String oldName;
    /**
     * 真实文件名
     */
    @TableField("REAL_NAME")
    private String realName;
    /**
     * 相对路径
     */
    @TableField("RELATIVE_PATH")
    private String relativePath;

    /**
     * 缩略图
     */
    @TableField("THUMB")
    private String thumb;

    /**
     * 存储类型 例如 SYSTEM 、OSS、  QINIU
     */
    @TableField("SAVE_TYPE")
    private String saveType;

    public String  getName(){
        return oldName;
    }

}
