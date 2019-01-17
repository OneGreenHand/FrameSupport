package com.frame.bean;

import java.io.File;

/**
 * description: 上传文件需要的信息
 */
public class FileInfoBean {
    private String paramName;//文件对应的参数名称
    private File file;//文件

    public FileInfoBean(String paramName, File file) {
        this.paramName = paramName;
        this.file = file;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
