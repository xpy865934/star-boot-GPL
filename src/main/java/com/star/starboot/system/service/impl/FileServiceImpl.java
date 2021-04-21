package com.star.starboot.system.service.impl;


import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.star.starboot.common.utils.CommonUtils;
import com.star.starboot.common.utils.DateUtils;
import com.star.starboot.common.utils.VideoUtils;
import com.star.starboot.config.aliyunoss.AliYunOssStsUtils;
import com.star.starboot.constant.SystemConstant;
import com.star.starboot.system.dao.FileMapper;
import com.star.starboot.system.entity.File;
import com.star.starboot.system.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件上传表 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2019-07-15
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    /**
     * 是否开阿里云上传
     */
    private Boolean isAliOss = false;
    /**
     * 是否开启七牛云上传
     */
    private Boolean isQiNiuYun = false;

    @Autowired
    private FileMapper fileMapper;

    @Value("${file.upload-physical-path}")
    private String uploadPhysicalPath;

    @Value("${file.qiniu-access-key}")
    private String qiNiuAccessKey;

    @Value("${file.qiniu-secret-key}")
    private String qiNiuSecretKey;

    @Value("${file.qiniu-bucket-key}")
    private String qiNiuBucket;

    private String uploadRelativePath = java.io.File.separator + "upload_file" + java.io.File.separator;

    /**
     * 视频文件扩展名
     */
    private String[] extendFiles = new String[]{"mp4","avi", "wmv", "mpg", "mpeg", "mov", "rm", "ram", "swf", "flv", "rmvb"};

    @Autowired
    private AliYunOssStsUtils aliYunOssStsUtils;

    @Override
    public File upload(MultipartFile file, String parentDictName) {
        try {
            String newName = rename(file.getOriginalFilename());

            if(isAliOss){
                String dir = uploadRelativePath + DateUtils.getCurrentYear() + java.io.File.separator
                        + DateUtils.getCurrentMonth() + java.io.File.separator;
                if(!StringUtils.isEmpty(parentDictName)){
                    dir = dir + parentDictName + java.io.File.separator;
                }
                String dirName = AliYunOssStsUtils.OSS_ROOT_PATH.concat(dir + newName);
                String tmpDirName = uploadPhysicalPath + dir + newName;

                java.io.File newFile = new java.io.File(tmpDirName);
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                newFile.createNewFile();
                file.transferTo(newFile);

                //上传  此处文件流只能被读取一次，再次读取会报错 文件找不到
                aliYunOssStsUtils.uploadObjectToOss("1L",dirName,newFile);

                File dbFile = new File();
                dbFile.setAbsolutePath(AliYunOssStsUtils.OSS_ROOT_PATH);
                dbFile.setFileSize(file.getSize());

                // 文件类型
                String extendName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                dbFile.setFileType(extendName);
                // 源名称
                dbFile.setOldName(file.getOriginalFilename());
                // 新名称
                dbFile.setRealName(newName);
                // 相对路径
                dbFile.setRelativePath(dir);
                // 存储类型
                dbFile.setSaveType(SystemConstant.SAVE_TYPE_OSS);

                // 判断是否是视频，视频需要生成缩略图
                if(ArrayUtil.contains(extendFiles, extendName.toLowerCase())){
                    generateThumb(dbFile, tmpDirName);
                }
                fileMapper.insert(dbFile);

                // 删除临时文件
                newFile.delete();
                return dbFile;
            } else if(isQiNiuYun){
                // TODO 七牛云上传
                return null;
            } else {
                String dir = uploadRelativePath + DateUtils.getCurrentYear() + java.io.File.separator
                        + DateUtils.getCurrentMonth() + java.io.File.separator;
                if(!StringUtils.isEmpty(parentDictName)){
                    dir = dir + parentDictName + java.io.File.separator;
                }
                String dirName = uploadPhysicalPath + dir + newName;
                java.io.File newFile = new java.io.File(dirName);
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                newFile.createNewFile();
                file.transferTo(newFile);

                File dbFile = new File();
                dbFile.setAbsolutePath(uploadPhysicalPath);
                dbFile.setFileSize(file.getSize());

                // 文件类型
                String extendName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                dbFile.setFileType(extendName);
                // 源名称
                dbFile.setOldName(file.getOriginalFilename());
                // 新名称
                dbFile.setRealName(newName);
                // 相对路径
                dbFile.setRelativePath(dir);
                // 存储类型
                dbFile.setSaveType(SystemConstant.SAVE_TYPE_SYSTEM);

                // 判断是否是视频，视频需要生成缩略图
                if(ArrayUtil.contains(extendFiles, extendName.toLowerCase())){
                    generateThumb(dbFile, null);
                }
                fileMapper.insert(dbFile);
                return dbFile;
            }
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @Override
    public ResponseEntity download(String fileId, HttpServletRequest request, HttpServletResponse response) {
        com.star.starboot.system.entity.File file = fileMapper.selectById(fileId);
        String saveType = file.getSaveType();
        if(isAliOss && SystemConstant.SAVE_TYPE_OSS.equals(saveType)){
            // 阿里云下载
            String urlStr = aliYunOssStsUtils.getOssSignUrl(AliYunOssStsUtils.OSS_ROOT_PATH + file.getRelativePath() + file.getRealName(), "1L");
            try {
                URL url = new URL(urlStr);
                HttpHeaders headers = createDownloadFileHeaders(file.getOldName());
                InputStream in = new BufferedInputStream(url.openStream());
                byte[] buffer = FileCopyUtils.copyToByteArray(in);
                return new ResponseEntity<byte[]>(buffer, headers, HttpStatus.OK);
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
                return handlerDownloadFileException("要下载的文件不存在");
            } catch (IOException e) {
                log.error(e.getMessage());
                return handlerDownloadFileException("下载失败，重新下载或联系管理员");
            }
        } else if(isQiNiuYun && SystemConstant.SAVE_TYPE_QINIU.equals(saveType)){
            // 七牛云下载
            try {
                URL url = new URL("http://pzszb0ofy.bkt.clouddn.com/" + file.getRealName() + "-qcnt");
                HttpHeaders headers = createDownloadFileHeaders(file.getOldName());
                InputStream in = new BufferedInputStream(url.openStream());
                byte[] buffer = FileCopyUtils.copyToByteArray(in);
                return new ResponseEntity<byte[]>(buffer, headers, HttpStatus.OK);
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
                return handlerDownloadFileException("要下载的文件不存在");
            } catch (IOException e) {
                log.error(e.getMessage());
                return handlerDownloadFileException("下载失败，重新下载或联系管理员");
            }
        } else {
            try {
                java.io.File pFile = new java.io.File(uploadPhysicalPath + file.getRelativePath() + file.getRealName());
                HttpHeaders headers = createDownloadFileHeaders(file.getOldName());
                InputStream in = new FileInputStream(pFile);
                byte[] buffer = FileCopyUtils.copyToByteArray(in);
                return new ResponseEntity<byte[]>(buffer, headers, HttpStatus.OK);
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
                return handlerDownloadFileException("要下载的文件不存在");
            } catch (IOException e) {
                log.error(e.getMessage());
                return handlerDownloadFileException("下载失败，重新下载或联系管理员");
            }
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadVideoThumb(String fileId, HttpServletRequest request, HttpServletResponse response) {
        com.star.starboot.system.entity.File file = fileMapper.selectById(fileId);
        String saveType = file.getSaveType();
        if(isAliOss  && SystemConstant.SAVE_TYPE_OSS.equals(saveType)){
            // 阿里云下载
            String urlStr = aliYunOssStsUtils.getOssSignUrl(AliYunOssStsUtils.OSS_ROOT_PATH + file.getRelativePath() + file.getThumb(), "1L");
            try {
                URL url = new URL(urlStr);
                HttpHeaders headers = createDownloadFileHeaders(file.getOldName());
                InputStream in = new BufferedInputStream(url.openStream());
                byte[] buffer = FileCopyUtils.copyToByteArray(in);
                return new ResponseEntity<byte[]>(buffer, headers, HttpStatus.OK);
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
                return handlerDownloadFileException("要下载的文件不存在");
            } catch (IOException e) {
                log.error(e.getMessage());
                return handlerDownloadFileException("下载失败，重新下载或联系管理员");
            }
        } else if(isQiNiuYun && SystemConstant.SAVE_TYPE_QINIU.equals(saveType)){
            // 七牛云下载
            try {
                URL url = new URL("http://pzszb0ofy.bkt.clouddn.com/" + file.getRealName() + "-qcnt");
                HttpHeaders headers = createDownloadFileHeaders(file.getOldName());
                InputStream in = new BufferedInputStream(url.openStream());
                byte[] buffer = FileCopyUtils.copyToByteArray(in);
                return new ResponseEntity<byte[]>(buffer, headers, HttpStatus.OK);
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
                return handlerDownloadFileException("要下载的文件不存在");
            } catch (IOException e) {
                log.error(e.getMessage());
                return handlerDownloadFileException("下载失败，重新下载或联系管理员");
            }
        } else {
            try {
                java.io.File pFile = new java.io.File(uploadPhysicalPath + file.getRelativePath() + file.getThumb());
                HttpHeaders headers = createDownloadFileHeaders(file.getThumb());
                InputStream in = new FileInputStream(pFile);
                byte[] buffer = FileCopyUtils.copyToByteArray(in);
                return new ResponseEntity<byte[]>(buffer, headers, HttpStatus.OK);
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
                return handlerDownloadFileException("要下载的文件不存在");
            } catch (IOException e) {
                log.error(e.getMessage());
                return handlerDownloadFileException("下载失败，重新下载或联系管理员");
            }
        }
    }

    @Override
    public void videoFile(String fileId, HttpServletRequest request, HttpServletResponse response) {
        com.star.starboot.system.entity.File file = fileMapper.selectById(fileId);
        String saveType = file.getSaveType();
        if(isAliOss && SystemConstant.SAVE_TYPE_OSS.equals(saveType)){
            // 阿里云下载
            String urlStr = aliYunOssStsUtils.getOssSignUrl(AliYunOssStsUtils.OSS_ROOT_PATH + file.getRelativePath() + file.getRealName(), "1L");
            try {
                URL url = new URL(urlStr);
                InputStream in = new BufferedInputStream(url.openStream());
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                response.setHeader("Content-Disposition", "attachment; filename="+file.getName().replace(" ", "_"));
                IOUtils.copy(in, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else if(isQiNiuYun  && SystemConstant.SAVE_TYPE_QINIU.equals(saveType)){
            // 七牛云下载
            try {
                URL url = new URL("http://pzszb0ofy.bkt.clouddn.com/" + file.getRealName() + "-qcnt");
                InputStream in = new BufferedInputStream(url.openStream());
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                response.setHeader("Content-Disposition", "attachment; filename="+file.getName().replace(" ", "_"));
                IOUtils.copy(in, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            try {
                java.io.File pFile = new java.io.File(uploadPhysicalPath + file.getRelativePath() + file.getRealName());
                InputStream in = new FileInputStream(pFile);
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                response.setHeader("Content-Disposition", "attachment; filename="+file.getName().replace(" ", "_"));
                IOUtils.copy(in, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 生成视频文件缩略图
     * @param file  文件
     * @param realPath  文件在服务器的真实路径，对于远程上传，需要知道服务器临时文件的真实路径
     * @return
     */
    public File generateThumb(File file, String realPath) {
        String realName = file.getRealName();
        int index = realName.lastIndexOf(".");
        String newName = file.getRealName().substring(0,index) + "-thumb.jpeg";
        String saveType = file.getSaveType();
        if(isAliOss  && SystemConstant.SAVE_TYPE_OSS.equals(saveType)){
            // 获取视频文件的缩略图
            String filePath = realPath;
            try {
                InputStream thumbInputStream = VideoUtils.randomGrabberFfmpegVideoImg(filePath, null);

                // 保存缩略图
                String dirName = AliYunOssStsUtils.OSS_ROOT_PATH + file.getRelativePath() + newName;
                String tmpDirName = file.getAbsolutePath() + file.getRelativePath() + newName;

                java.io.File newFile = new java.io.File(tmpDirName);
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                newFile.createNewFile();
                CommonUtils.writeToLocal(dirName, thumbInputStream);
                file.setThumb(newName);

                aliYunOssStsUtils.uploadObjectToOss(String.valueOf(1L),dirName,newFile);

                // 删除临时文件
                newFile.delete();
            } catch (Exception e) {
                log.error("获取视频缩略图失败" + file.getRealName());
                e.printStackTrace();
            }
        } else if(isQiNiuYun && SystemConstant.SAVE_TYPE_QINIU.equals(saveType)){
           // TODO 七牛云获取视频缩略图文件
        } else {
            // 获取视频文件的缩略图
            String filePath = file.getAbsolutePath() + file.getRelativePath() + file.getRealName();
            try {
                InputStream thumbInputStream = VideoUtils.randomGrabberFfmpegVideoImg(filePath, null);

                // 保存缩略图
                String dirName = file.getAbsolutePath() + file.getRelativePath() + newName;
                java.io.File newFile = new java.io.File(dirName);
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                newFile.createNewFile();
                CommonUtils.writeToLocal(dirName, thumbInputStream);
                file.setThumb(newName);
            } catch (Exception e) {
                log.error("获取视频缩略图失败" + file.getRealName());
                e.printStackTrace();
            }
        }
        return file;
    }

    private static ResponseEntity<byte[]> handlerDownloadFileException(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=utf-8");
        return new ResponseEntity<byte[]>(message.getBytes(), headers, HttpStatus.OK);
    }

    public static HttpHeaders createDownloadFileHeaders(String fileName) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/octet-stream;charset=UTF-8");
        // 设置这个头，否则前端获取不到Content-Disposition
        headers.add("Access-Control-Expose-Headers","Content-Disposition");
        /**
         * 兼容浏览器编码问题
         */
        String contentDisposition = "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8");
        headers.add("Content-Disposition", contentDisposition);
        return headers;
    }

    /**
     * 分离文件名称和后缀，并且加入字符串返回
     *
     * @param fileName
     * @return
     */
    private String rename(String fileName) {
        String pattern = "yyyy-MM-dd_HH-mm-ss_SSS";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dateStr = sdf.format(new Date());

        // String name = "";// 文件名
        String ext = ""; // 后缀
        char point = '.';
        int index = fileName.lastIndexOf(point);

        if (index != -1) {// 如果有后缀
            ext = fileName.substring(index + 1);
        }
        return dateStr + point + ext;
    }

    /**
     * 七牛云上传
     * @param file
     * @param key
     * @return
     * @throws Exception
     */
    public DefaultPutRet upload(byte[] file, String key) throws Exception {
        //构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.huadong());

        UploadManager uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(qiNiuAccessKey, qiNiuSecretKey);
        String upToken = auth.uploadToken(qiNiuBucket);

        Response response = uploadManager.put(file, key, upToken);
        // 解析上传的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return putRet;
    }
}
