package com.zxs.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZipUtils {

    /**
     * 打包zip 打包的文件路径 生成zip的路径 zip的名称
     *
     * @author zhangPengyi
     * @date 2020/4/9
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (sourceFile.exists() == false) {
            log.info("待压缩的文件目录：{}不存在.", sourceFilePath);
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
                if (zipFile.exists()) {
                    log.info("{}目录下存在名字为:{}.zip打包文件.", zipFilePath, fileName);
                } else {
                    File[] sourceFiles = sourceFile.listFiles();
                    if (null == sourceFiles || sourceFiles.length < 1) {
                        log.info("待压缩的文件目录：{}里面不存在文件，无需压缩.", sourceFilePath);
                    } else {
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024 * 10];
                        for (int i = 0; i < sourceFiles.length; i++) {
                            // 创建ZIP实体，并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                            zos.putNextEntry(zipEntry);
                            // 读取待压缩的文件并写进压缩包里
                            fis = new FileInputStream(sourceFiles[i]);
                            bis = new BufferedInputStream(fis, 1024 * 10);
                            int read = 0;
                            while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                                zos.write(bufs, 0, read);
                            }
                        }
                        flag = true;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                // 关闭流
                try {
                    if (null != bis) {
                        bis.close();
                    }
                    if (null != zos) {
                        zos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 删除文件 path文件路径 或者文件夹名称
     *
     * @author zhangPengyi
     * @date 2020/4/9
     */
    public static void deleteFile(String path) {
        File riskReports = new File(path);
        if (riskReports.exists()) {
            if (riskReports.isFile()) {
                riskReports.delete();
                return;
            }
            File[] files = riskReports.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    if (!f.delete()) {
                        log.info("{} delete error!", f.getAbsolutePath());
                    }
                }
            }
            riskReports.delete();
            log.info("{},删除成功", riskReports);
        }
    }

}
