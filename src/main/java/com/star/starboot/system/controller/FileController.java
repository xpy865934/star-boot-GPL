package com.star.starboot.system.controller;

import com.star.starboot.annotation.SysLog;
import com.star.starboot.common.enums.ResultCode;
import com.star.starboot.common.vo.Result;
import com.star.starboot.system.entity.File;
import com.star.starboot.system.service.FileService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.system.controller
 * @Description: 文件服务
 * @Author: xpy
 * @Date: Created in 2020年11月10日 7:56 下午
 */
@RestController
@RequestMapping("/file")
@CrossOrigin
@Slf4j
public class FileController {
    @Autowired
    private FileService fileService;

    /**
     * 上传文件
     * @return
     */
    @ApiOperation("上传文件")
    @PostMapping("/upload")
    @SysLog(description = "上传文件")
    public Result upload(@RequestParam MultipartFile file, @RequestParam String parentDictName){
        try {
            File upload = fileService.upload(file,parentDictName);
            return Result.success(upload);
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            return Result.create(ResultCode.ERROR_UPLOAD_FAILED);
        }
    }


    /**
     * 下载文件
     * @return
     */
    @ApiOperation("下载文件")
    @GetMapping("/downloadFile")
    @SysLog(description = "下载文件")
    public ResponseEntity<byte[]> downloadFile(String fileId, String token, HttpServletRequest request, HttpServletResponse response){
        return fileService.download(fileId,request,response);
    }
}
