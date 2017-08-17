package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator on 2017/7/10/010.
 */
public interface IFileService {

    public String upload(MultipartFile file, String path);
}
