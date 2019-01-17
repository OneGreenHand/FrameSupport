package com.frame.config;

public enum Config {
    dev(true, "https://www.apiopen.top/"),//开发
    test(true, "https://www.apiopen.top/"),//测试
    product(false, "https://www.apiopen.top/");//生产

    private boolean isDebug;
    private String url;

    Config(boolean isDebug, String url) {
        this.isDebug = isDebug;
        this.url = url;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public String getUrl() {
        return url;
    }
}
