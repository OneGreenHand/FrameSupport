package com.frame.bean;

import java.io.File;

/**
 * description: 上传文件需要的信息
 */
public class FileInfoBean {
    public String paramName;//文件对应的参数名称
    public File file;//文件

    public FileInfoBean(String paramName, File file) {
        this.paramName = paramName;
        this.file = file;
    }
}