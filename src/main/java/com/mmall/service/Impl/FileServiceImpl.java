package com.mmall.service.Impl;

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
 * Created by Administrator on 2017/7/10/010.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        //获取原始文件名
        String fileName = file.getOriginalFilename();
        //获取扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID()+"."+fileExtensionName;
        logger.info("开始上传文件，上传的文件名:{}，上传的路径:{}，新文件名:{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);
        try {
            file.transferTo(targetFile);
            //文件上传成功
            // 将targetFile上传到FTP服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            // 删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return  null;
        }
        return targetFile.getName();
    }
}
