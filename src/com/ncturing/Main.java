package com.ncturing;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Main {

    public static void main(String args[]) {
        dealArgs(args);
    }

    private static void dealArgs(String[] args) {
        Commander commander = new Commander();
        JCommander jCommander = JCommander.newBuilder().addObject(commander).build();
        try {
            jCommander.parse(args);
            dealCommander(commander);
        } catch (ParameterException e) {
            jCommander.usage();
        }
    }

    private static void dealCommander(Commander commander) {
        String iPath = commander.getiPath();
        int end = iPath.lastIndexOf(commander.getSeparator());

        String zipName = iPath.substring(end + 1);
        commander.setZipFileName(zipName);
        if (zipName.contains(".")) {
            zipName = zipName.substring(0, zipName.lastIndexOf("."));
        }
        commander.setZipName(zipName);
        if (end != -1) {
            commander.setoPath(iPath.substring(0, end + 1) + zipName);
        } else {
            commander.setoPath("." + commander.getSeparator() + zipName);
        }

        unZip(commander);
    }

    private static void unZip(Commander commander) {
        String iPath = commander.getiPath();
        String oPath = commander.getoPath();
        String charset = commander.getCharset();
        String separator = "/";
        String[] exts = commander.getExts();


        File oRootDir = new File(oPath);
        if (!oRootDir.exists()) oRootDir.mkdirs();

        try {
            ZipFile zipFile = new ZipFile(iPath, Charset.forName(charset));
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                if (!zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    String targetname = name.substring(name.lastIndexOf(separator) + 1);
                    if (isIn(exts, targetname)) {
                        String ext = targetname.substring(targetname.lastIndexOf(".") + 1);
                        File targetDir = new File(oRootDir, ext);
                        if (!targetDir.exists()) targetDir.mkdirs();
                        File targetFile = new File(targetDir, targetname);
                        targetFile = isExistsOrgetExistsFile(targetFile, zipEntry);
                        if (targetFile == null)continue;
                        outputEntry(targetFile, zipFile, zipEntry);
                        System.out.println(name + "\t\t提取成功");
                    }
                    if (name.endsWith(".zip") && !name.equalsIgnoreCase(commander.getZipFileName())) {
                        name = name.substring(name.lastIndexOf(separator) + 1);
                        File targetFile = new File(oRootDir, name);
                        targetFile = isExistsOrgetExistsFile(targetFile, zipEntry);
                        outputEntry(targetFile, zipFile, zipEntry);
                        Commander tmpCommander = new Commander();
                        tmpCommander.setiPath(targetFile.getPath());
                        tmpCommander.setSeparator(File.separator);
                        dealCommander(tmpCommander);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File isExistsOrgetExistsFile(File targetFile, ZipEntry zipEntry) {
        if (!targetFile.exists()) return targetFile;
        if (targetFile.length() != zipEntry.getSize()) {
            return getNoExistFile(targetFile);
        } else {
            return null;
        }
    }

    private static void outputEntry(String targetFilePath, ZipFile zipFile, ZipEntry zipEntry) throws IOException {
        InputStream is = zipFile.getInputStream(zipEntry);
        outputStream(is, targetFilePath);
    }

    private static void outputEntry(File targetFile, ZipFile zipFile, ZipEntry zipEntry) throws IOException {
        InputStream is = zipFile.getInputStream(zipEntry);
        outputStream(is, targetFile);
    }

    private static void outputStream(InputStream is, String targetFilePath) throws IOException {
        File targetFile = new File(targetFilePath);
        outputStream(is, targetFile);
    }

    private static void outputStream(InputStream is, File targetFile) throws IOException {
        OutputStream os = new FileOutputStream(targetFile);
        byte[] bs = new byte[1024];
        int len;
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        os.flush();
        os.close();
        is.close();
    }

    private static File getNoExistFile(File targetFile) {
        if (!targetFile.exists()) return targetFile;

        String fileName = targetFile.getName();
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        int i = 1;
        while (true) {
            File tmpFile = new File(targetFile.getParentFile(), String.format("%s_%d.%s", name, i, ext));
            if (!tmpFile.exists()) {
                return tmpFile;
            }
            i++;
        }
    }

    private static boolean isIn(String[] exts, String name) {
        for (String ext : exts) {
            if (name.endsWith("." + ext)) {
                return true;
            }
        }
        return false;
    }
}
