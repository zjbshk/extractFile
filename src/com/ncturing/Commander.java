package com.ncturing;

import com.beust.jcommander.Parameter;

public class Commander {

    @Parameter(names = {"-i", "-iPath"}, required = true)
    private String iPath;

    @Parameter(names = {"-o", "-oPath"})
    private String oPath;

    @Parameter(names = {"-c", "-charset"})
    private String charset = "GBK";

    @Parameter(names = {"-s", "-separator"})
    private String separator = "/";

    @Parameter(names = {"-e", "-exts"})
    private String[] exts = {"png", "jpg", "mp3", "gif"};

    private String zipName;
    private String zipFileName;

    public String getZipFileName() {
        return zipFileName;
    }

    public void setZipFileName(String zipFileName) {
        this.zipFileName = zipFileName;
    }

    public String getZipName() {
        return zipName;
    }

    public void setZipName(String zipName) {
        this.zipName = zipName;
    }

    public String getiPath() {
        return iPath;
    }

    public void setiPath(String iPath) {
        this.iPath = iPath;
    }


    public String getoPath() {
        return oPath;
    }

    public void setoPath(String oPath) {
        this.oPath = oPath;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String[] getExts() {
        return exts;
    }

    public void setExts(String[] exts) {
        this.exts = exts;
    }
}
