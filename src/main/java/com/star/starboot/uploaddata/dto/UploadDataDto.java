package com.star.starboot.uploaddata.dto;

import com.star.starboot.person.entity.UserBasicInfo;
import com.star.starboot.uploaddata.entity.UploadData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.system.dto
 * @Description: ${Description}
 * @Author: xpy
 * @Date: Created in 2020年07月02日 10:42
 */
@Data
@Accessors(chain = true)
public class UploadDataDto extends UploadData {

    /**
     * 上报人姓名
     */
    private String userName;

    /**
     * 上报时间范围
     */
    private List<String> sbsjRange;
}
