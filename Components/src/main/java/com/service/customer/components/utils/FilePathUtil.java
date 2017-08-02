package com.service.customer.components.utils;

import android.net.Uri;
import android.os.Environment;

import com.service.customer.components.constant.Regex;

import java.io.File;

/**
 * Created by guojb on 2017/6/28.
 */
public class FilePathUtil {

    private FilePathUtil() {
        // cannot be instantiated
    }

    private static FilePathUtil filePathUtil;
    private static String picDir;

    public static synchronized FilePathUtil getInstance() {
        if (filePathUtil == null) {
            filePathUtil = new FilePathUtil();
        }
        return filePathUtil;
    }

    public static void releaseInstantce() {
        if (filePathUtil != null) {
            filePathUtil = null;
        }
    }

    public FilePathUtil getDir(String empId) {
        if (empId != null) {
            picDir = Environment.getExternalStoragePublicDirectory("MergePay") + Regex.LEFT_SLASH.getRegext() + empId;
            File filePath = new File(picDir);
            if (!filePath.isDirectory()) {
                filePath.delete();
            }
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
        } else {
            picDir = Environment.getExternalStoragePublicDirectory("MergePay").getAbsolutePath();
            File filePath = new File(picDir);
            if (!filePath.isDirectory()) {
                filePath.delete();
            }
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
        }
        return this;
    }

    public Uri getImageFileUri(String imageType) {
        return Uri.parse(Regex.FILE_HEAD.getRegext() + picDir + Regex.LEFT_SLASH.getRegext() + imageType + Regex.IMAGE_JPG.getRegext());
    }

    public String getImageFilePath(String imageType) {
        return picDir + Regex.LEFT_SLASH.getRegext() + imageType + Regex.IMAGE_JPG.getRegext();
    }

    public String getImageZipPath(String imageType) {
        return picDir + Regex.LEFT_SLASH.getRegext() + imageType + Regex.ZIP.getRegext();
    }
}
