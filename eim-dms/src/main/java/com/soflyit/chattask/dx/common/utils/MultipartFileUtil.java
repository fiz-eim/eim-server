package com.soflyit.chattask.dx.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class MultipartFileUtil {


    private MultipartFileUtil() {
    }


    public static MultipartFile toMultipartFile(byte[] bytes, String fileName) {
        final FileItem fileItem = createFileItem(new ByteArrayInputStream(bytes), fileName);
        return new CommonsMultipartFile(fileItem);
    }

    private static FileItem createFileItem(InputStream is, String fileName) {
        return createFileItem(is, "file", fileName);
    }

    private static FileItem createFileItem(InputStream is, String fieldName, String fileName) {
        final DiskFileItemFactory fac = new DiskFileItemFactory(10240, null);
        FileItem fileItem = fac.createItem(fieldName, "multipart/form-data", true, fileName);
        final OutputStream fileItemOutStream;
        try {
            fileItemOutStream = fileItem.getOutputStream();
        } catch (IOException e) {
            log.error("获取FileItem输出流异常：{}", e.getMessage(), e);
            throw new RuntimeException("系统异常");
        }

        try {
            IOUtils.copy(is, fileItemOutStream);
        } catch (IOException e) {
            log.error("写入FileItem异常：{}", e.getMessage(), e);
            throw new RuntimeException("系统异常");
        }
        return fileItem;
    }


}
