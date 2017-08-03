package com.service.customer.components.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import com.google.common.collect.Lists;
import com.service.customer.components.constant.Regex;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class IOUtil {

    private static IOUtil ioUtil;

    private IOUtil() {
    }

    public static synchronized IOUtil getInstance() {
        if (ioUtil == null) {
            ioUtil = new IOUtil();
        }
        return ioUtil;
    }

    private File getFilePath(Context ctx) {
        File path;
        if (isSDCardExsist()) {
//            Cache = getExternalStoragePublicDirectory(ctx, Regex.LOG.getRegext());
            path = ctx.getExternalFilesDir(Regex.LOG.getRegext());

        } else {
            path = new File(ctx.getFilesDir(), Regex.LOG.getRegext());
//            Cache = ctx.getFilesDir();
        }
        if (path != null) {
            if (path.exists()) {
                return path;
            } else {
                path.mkdirs();
            }
        }
        return path;
    }

    public List<File> getFiles(Context ctx) {
        File path = getFilePath(ctx);
        LogUtil.getInstance().print("Cache:" + path.getAbsolutePath());
        LogUtil.getInstance().print("listFiles:" + path.listFiles());
        LogUtil.getInstance().print("exists:" + path.exists());
        List<File> files = Lists.newArrayList();
        if (path.listFiles() != null && path.exists()) {
            for (File file : path.listFiles()) {
                LogUtil.getInstance().print("file:" + file.getAbsolutePath());
                if (file.getName().endsWith(Regex.LOG.getRegext()) && file.getName().contains(DeviceUtil.getInstance().getDeviceId(ctx))) {
                    files.add(file);
                }
            }
            return files;
        } else {
            return null;
        }
    }

    public void deleteFile(final Context ctx) {
        for (File file : getFilePath(ctx).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
//                return pathname.isFile() && pathname.getName().endsWith(Regex.LOG_TYPE.getRegext());
                return pathname.isFile()
                        && pathname.getName().endsWith(Regex.LOG_TYPE.getRegext())
                        && pathname.getName().contains(DeviceUtil.getInstance().getDeviceId(ctx));
            }
        })) {
            file.delete();
        }
    }

    public void deleteFile(String path) {
        if (isSDCardExsist()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    public void deleteFile(File folder) {
        if (isSDCardExsist()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    public void deleteFile(String path, String fileName) {
        if (isSDCardExsist()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.getName().split("\\.")[0].equals(fileName)) {
                    file.delete();
                }
            }
        }
    }

    public File forceMkdir(File directory) throws IOException {
        if (directory != null) {
            if (directory.exists()) {
                LogUtil.getInstance().print("文件夹存在:" + directory.getAbsolutePath());
                if (!directory.isDirectory()) {
                    throw new IOException("File " + directory + " exists and is not a directory. Unable to create directory.");
                }
            } else {
                LogUtil.getInstance().print("文件夹不存在，创建:" + directory.getAbsolutePath());
                if (!directory.mkdirs()) {
                    if (!directory.isDirectory()) {
                        throw new IOException("Unable to create directory " + directory);
                    }
                }
            }
        }
        return directory;
    }

    public void forceMkdir(String directoryPath) throws IOException {
        forceMkdir(new File(directoryPath));
    }

    public void writeObject(Object object, File file) {
        if (object != null && file != null) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            ObjectOutputStream objectOutputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(byteArrayOutputStream.toByteArray());
                fileOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                    if (objectOutputStream != null) {
                        objectOutputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void writeObject(Object object, String filePath) {
        writeObject(object, new File(filePath));
    }

    public String readString(InputStream inputStream) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    public Object readObject(File file) {
        if (file.exists()) {
            Object object = null;
            FileInputStream fileInputStream = null;
            ObjectInputStream objectInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fileInputStream);
                object = objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (objectInputStream != null) {
                        objectInputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return object;
        } else {
            return null;
        }
    }

    public Object readObject(String filePath) {
        return readObject(new File(filePath));
    }

    public boolean writeFile(Context ctx, String message, boolean isAppend) {
        File path = getFilePath(ctx);
        if (path != null) {
            try {
                File file = new File(path, DeviceUtil.getInstance().getDeviceId(ctx) + Regex.LOG_TYPE.getRegext());
                FileOutputStream stream = new FileOutputStream(file, isAppend);
                stream.write(message.getBytes(Regex.UTF_8.getRegext()));
                stream.flush();
                stream.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public String readByte(Context ctx) {
        File dir = getFilePath(ctx);
        String[] files = dir.list();
        StringBuilder builder = new StringBuilder();
        for (String data : files) {
            File file = new File(getFilePath(ctx) + File.separator + data);
            LogUtil.getInstance().print("file_name:" + getFilePath(ctx) + File.separator + data);
            if (file.getName().endsWith(Regex.LOG_TYPE.getRegext())) {
                BufferedInputStream bufferedInputStream = null;
                try {
                    bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                    byte[] buffer = new byte[1024];
                    int length;
                    showAvailableBytes(bufferedInputStream);
                    while ((length = bufferedInputStream.read(buffer)) != -1) {
                        // System.out.write(buffer, 0, length);
                        builder.append(new String(buffer, 0, length, Regex.UTF_8.getRegext()));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                return null;
            }
        }
        return builder.toString();
    }

    public String readLine(Context ctx) {
        String[] files = getFilePath(ctx).list();
        StringBuilder builder = new StringBuilder();
        for (String data : files) {
            File file = new File(getFilePath(ctx) + File.separator + data);
            LogUtil.getInstance().print("file_name:" + getFilePath(ctx) + File.separator + data);
            if (file.getName().endsWith(Regex.LOG_TYPE.getRegext()) && file.getName().contains(DeviceUtil.getInstance().getDeviceId(ctx))) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Regex.UTF_8.getRegext()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append(System.getProperty(Regex.LINE_SEPARATOR.getRegext()));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                return null;
            }
        }
        return builder.toString();
    }

    private void showAvailableBytes(InputStream inputStream) {
        try {
            LogUtil.getInstance().print("当前输入流中的字节数为：" + inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(InputStream in, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getMimeType(Context ctx, Uri uri) {
        String extension;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(ctx.getContentResolver().getType(uri));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        return extension;
    }


    //进行复制的函数
    public void copyFile(String oldPath, String newPath) {
        File oldfile = new File(oldPath);
        if (oldfile.isDirectory()) {
            return;
        }
        int bytesum = 0;
        int byteread = 0;
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            if (oldfile.exists()) { //文件存在时
                inStream = new FileInputStream(oldPath); //读入原文件
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean isSDCardExsist() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public File getExternalStoragePublicDirectory(Context ctx, String directoryName, String fileName) throws IOException {
        if (isSDCardExsist()) {
            return new File(IOUtil.getInstance().forceMkdir(Environment.getExternalStoragePublicDirectory(directoryName)).getAbsolutePath() + fileName);
        } else {
            return new File(ctx.getExternalFilesDir(directoryName).getAbsolutePath() + fileName);
        }
    }

    public File getExternalStorageDirectory(Context ctx, String dirName) {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/android/data/"
                                + ctx.getPackageName()
                                + File.separator
                                + dirName + File.separator);
    }
}
