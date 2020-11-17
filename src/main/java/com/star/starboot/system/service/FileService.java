package com.star.starboot.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.starboot.system.entity.File;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 文件上传表 服务类
 * </p>
 *
 * @author xpy
 * @since 2019-07-15
 */
public interface FileService extends IService<File> {

    /**
     * 文件上传
     * @param file
     */
    File upload(MultipartFile file, String parentDictName);

    /**
     * 文件下载
     * @param fileId
     * @return
     */
    ResponseEntity download(String fileId, HttpServletRequest request, HttpServletResponse response);
}
