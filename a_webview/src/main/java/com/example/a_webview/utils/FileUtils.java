package com.example.a_webview.utils;

import android.os.Environment;

import java.io.File;

public class FileUtils {
    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            LogUtils.d("delete file no exists " + file.getAbsolutePath());
        }
    }
    /**
     * 图片存储路径
     *
     * @return
     */
    public static File getFilePath() {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        File cameraDataDir = new File(filePath, System.nanoTime() + ".jpg");
        return cameraDataDir;
    }
}
