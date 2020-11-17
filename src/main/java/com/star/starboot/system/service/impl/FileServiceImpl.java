package com.star.starboot.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.star.starboot.common.utils.DateUtils;
import com.star.starboot.system.dao.FileMapper;
import com.star.starboot.system.entity.File;
import com.star.starboot.system.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Override
    public File upload(MultipartFile file, String parentDictName) {
        try {
            String newName = rename(file.getOriginalFilename());

            if(isQiNiuYun){
                // 上传
                upload(file.getBytes(),newName);

                File dbFile = new File();
                dbFile.setAbsolutePath(null);
                dbFile.setFileSize(file.getSize());

                // 文件类型
                String extendName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                dbFile.setFileType(extendName);
                // 源名称
                dbFile.setOldName(file.getOriginalFilename());
                // 新名称
                dbFile.setRealName(newName);
                // 相对路径
                dbFile.setRelativePath(null);
                fileMapper.insert(dbFile);
                return dbFile;
            } else {
                String dir = uploadRelativePath + java.io.File.separator + DateUtils.getCurrentYear() + java.io.File.separator
                        + DateUtils.getCurrentMonth() + java.io.File.separator;
                if(!StringUtils.isEmpty(parentDictName)){
                    dir = dir + parentDictName + java.io.File.separator;
                }
                String dirName = uploadPhysicalPath + dir + java.io.File.separator + newName;
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

        if(isQiNiuYun){
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
