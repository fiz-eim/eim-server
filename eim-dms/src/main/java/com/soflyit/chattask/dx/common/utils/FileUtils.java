package com.soflyit.chattask.dx.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.soflyit.chattask.dx.modular.file.domain.entity.OriginalFileEntity;
import com.soflyit.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.FileSystems;

/**
 * @author jiaozhishang
 * @date 2023/11/10&18:23
 * 文件拷贝
 * 将指定文件到目录文件夹
 */
@Slf4j
public class FileUtils {


    public static MultipartFile getMultipartFile(File file) {
        FileItem item = new DiskFileItemFactory().createItem("file"
                , MediaType.MULTIPART_FORM_DATA_VALUE
                , true
                , file.getName());
        try (InputStream input = new FileInputStream(file);
             OutputStream os = item.getOutputStream()) {

            IOUtils.copy(input, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }

        return new CommonsMultipartFile(item);
    }


    public static String getFilePath() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return "D:\\chat-dx\\";
        } else {
            return "/chat-dx/";
        }
    }

    public static void main(String[] args) {
        File zip = ZipUtil.zip("D:\\chat-dx\\tmp\\1");
        System.out.println(zip.getAbsolutePath());
    }


    public static String mkdir(String parentPath, String folderName) {
        String sep = FileSystems.getDefault().getSeparator();
        String path = parentPath + folderName + sep;
        FileUtil.mkdir(path);
        return path;
    }


    public static File copyFile(File file, String path, String rename) {
        File dest;//文件名称含后缀
        String fileName = file.getName();
        String name = fileName.substring(0, fileName.lastIndexOf("."));

        if (StringUtils.isNotEmpty(rename)) {
            fileName = fileName.replace(name, rename);
        }


        path = path + fileName;
        dest = new File(path);

        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();//新建文件夹
        }
        FileUtil.copyFile(file, dest);//文件写入

        return dest;
    }


    public static String getFileType(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }


    public static String getFileType(String fileName) {
        if (StrUtil.isBlank(fileName)) {
            return "";
        }
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }


    public static void downLoadFile(File file, HttpServletResponse response, String fileName) {
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename =" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            byte[] bytes = FileUtil.readBytes(file);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private static byte[] readInputStream(InputStream fis) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }

        return outStream.toByteArray();
    }


    public static void downLoadFile(InputStream is, HttpServletResponse response, String fileName, OriginalFileEntity file) {
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename =" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            int contentLength = file.getOriginalSize().intValue();
            response.setContentLength(contentLength);
            int inputSize = is.available();
            byte[] bytes = readInputStream(is);
            log.debug("文件长度, orgSize: {}, inputSize: {}, contentLength: {}, bytes: {}", file.getOriginalSize(), inputSize, contentLength, bytes.length);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}




