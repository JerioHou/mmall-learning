package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2017/7/26.
 */
@Service("iFileService")
public class IFileServiceImpl implements IFileService {
    Logger logger = LoggerFactory.getLogger(IFileServiceImpl.class);
    @Override
    public String upload(MultipartFile file, String path) {

        String filename = file.getOriginalFilename();
        System.out.println("getName()=" +file.getName());
        System.out.println("getOriginalFilename()=" +file.getOriginalFilename());

        File fileDir = new File(path);
        if (!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        //文件扩展名
        String fileExtensionName = filename.substring(filename.lastIndexOf("."));
        String uploadName = UUID.randomUUID().toString()+fileExtensionName;

        logger.info("开始上传文件，文件名：{}，路径：{}，新文件名：{}",filename,path,uploadName);

        File targetFile = new File(path,uploadName);
        try {
            file.transferTo(targetFile);
            FTPUtil.upload(Lists.newArrayList(targetFile));
            logger.info("文件上传成功");
            targetFile.delete();
            System.out.println("uploadeName="+uploadName);
            System.out.println("targetFile.getName()="+targetFile.getName());
            return targetFile.getName();
        } catch (IOException e) {
            logger.error("文件上传失败");
            e.printStackTrace();
        }
        return null;
    }
}
