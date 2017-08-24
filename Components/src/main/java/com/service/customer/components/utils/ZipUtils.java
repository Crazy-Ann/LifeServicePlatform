package com.service.customer.components.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ZipUtils {

    private static ZipUtils mZipUtil;

    private ZipUtils() {
        // cannot be instantiated
    }

    public static synchronized ZipUtils getInstance() {
        if (mZipUtil == null) {
            mZipUtil = new ZipUtils();
        }
        return mZipUtil;
    }

    public static void releaseInstance() {
        if (mZipUtil != null) {
            mZipUtil = null;
        }
    }

    /**
     * 单个文件压缩
     *
     * @param filePath   要压缩的文件路径
     * @param outZipPath 压缩后zip的保存路径
     */
    public void addFileToZip(String filePath, String outZipPath) {
        List<String> filesPath = new ArrayList<>();
        filesPath.add(filePath);
        addFilesToZip(filesPath, outZipPath);
    }

    /**
     * 多个文件压缩
     *
     * @param filesPath  多个文件路径集合
     * @param outZipPath 压缩后的zip存放路径
     */
    public void addFilesToZip(List<String> filesPath, String outZipPath) {
        try {
            ZipFile zipFile = new ZipFile(outZipPath);
            ArrayList<File> filesToAdd = new ArrayList<>();
            for (int i = 0; i < filesPath.size(); i++) {
                filesToAdd.add(new File(filesPath.get(i)));
            }
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to deflate compression
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipFile.addFiles(filesToAdd, parameters);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单个文件加密压缩
     *
     * @param filePath   要压缩的文件路径
     * @param outZipPath 压缩后zip存储路径
     * @param password   密码
     */
    public void addFileWithAESEncryp(String filePath, String outZipPath, String password) {
        List<String> filesPath = new ArrayList<>();
        filesPath.add(filePath);
        addFilesWithAESEncryp(filesPath, outZipPath, password);
    }

    /**
     * 多个文件加密压缩
     *
     * @param filesPath  多个要压缩的文件路径集合
     * @param outZipPath 压缩后zip存储路径
     * @param password   密码
     */
    public void addFilesWithAESEncryp(List<String> filesPath, String outZipPath, String password) {
        try {
            ZipFile zipFile = new ZipFile(outZipPath);
            ArrayList<File> filesToAdd = new ArrayList<>();
            for (int i = 0; i < filesPath.size(); i++) {
                filesToAdd.add(new File(filesPath.get(i)));
            }
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
//            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            parameters.setPassword(password);
            zipFile.addFiles(filesToAdd, parameters);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过流的方式进行压缩
     *
     * @param filePath    要压缩的文件路径
     * @param outZipPath  压缩后存储路径
     * @param zipfileName 要压缩的文件压缩后的文件名
     */
    public void addStreamToZip(String filePath, String outZipPath, String zipfileName) {
        InputStream is = null;
        try {
            ZipFile zipFile = new ZipFile(outZipPath);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setFileNameInZip(zipfileName);
            parameters.setSourceExternalStream(true);
            is = new FileInputStream(filePath);
            zipFile.addStream(is, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解压
     *
     * @param zipPath     要解压的Zip路径
     * @param outFilePath 解压到的目录
     */
    public void extractAllFiles(String zipPath, String outFilePath) {
        try {
            ZipFile zipFile = new ZipFile(zipPath);
            zipFile.extractAll(outFilePath);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压
     *
     * @param zipPath     要解压的Zip路径
     * @param outFilePath 解压到的目录
     */
    public void extractZipWithDecryp(String zipPath, String outFilePath, String password) {
        try {
            ZipFile zipFile = new ZipFile(zipPath);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(outFilePath);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

}
