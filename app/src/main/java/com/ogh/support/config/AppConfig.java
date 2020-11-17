package com.ogh.support.config;

import com.blankj.utilcode.util.PathUtils;

import java.io.File;

public class AppConfig {
    public static final class FilePath {
        public static final String FILE_FOLDER = PathUtils.getExternalAppDownloadPath() + File.separator; //   /storage/emulated/0/Android/data/package/files/Download/
    }
}
