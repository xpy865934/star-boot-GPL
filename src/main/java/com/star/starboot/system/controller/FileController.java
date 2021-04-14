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
import org.springframework.lang.Nullable;
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
     *
     * @return
     */
    @ApiOperation(value = "文件服务-上传文件", notes = "文件服务-上传文件")
    @PostMapping("/upload")
    @SysLog(description = "文件服务-上传文件")
    public Result upload(@RequestParam MultipartFile file, @RequestParam @Nullable String parentDictName) {
        File upload = fileService.upload(file, parentDictName);
        return Result.success(upload);
    }


    /**
     * 下载文件
     *
     * @return
     */
    @ApiOperation(value = "文件服务-下载文件", notes = "文件服务-下载文件")
    @GetMapping("/downloadFile")
    @SysLog(description = "文件服务-下载文件")
    public ResponseEntity<byte[]> downloadFile(String fileId, String token, HttpServletRequest request, HttpServletResponse response) {
        return fileService.download(fileId, request, response);
    }


    /**
     * 视频文件播放-暂时弃用，使用下载文件接口
     *
     * @return
     */
    @ApiOperation(value = "文件服务-视频文件播放", notes = "文件服务-视频文件播放")
    @GetMapping("/videoFile")
    @SysLog(description = "文件服务-视频文件播放")
    @Deprecated
    public void videoFile(String fileId, String token, HttpServletRequest request, HttpServletResponse response) {
        fileService.videoFile(fileId, request, response);
    }


    /**
     * 下载视频缩略图
     *
     * @return
     */
    @ApiOperation(value = "文件服务-下载视频缩略图", notes = "文件服务-下载视频缩略图")
    @GetMapping("/downloadVideoThumb")
    @SysLog(description = "文件服务-下载视频缩略图")
    public ResponseEntity<byte[]> downloadVideoThumb(String fileId, String token, HttpServletRequest request, HttpServletResponse response) {
        return fileService.downloadVideoThumb(fileId, request, response);
    }
}
