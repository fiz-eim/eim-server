package com.soflyit.chattask.dx.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class ResourceUtils {
    private final static String DOC = ".docx";
    private final static String XLS = ".xlsx";
    private final static String PPT = ".pptx";

    public static MultipartFile createWord(String resourceName) {
        XWPFDocument document = new XWPFDocument();
        document.createParagraph();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            document.write(out);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        byte[] bytes = out.toByteArray();
        return MultipartFileUtil.toMultipartFile(bytes, resourceName.concat(DOC));
    }


    public static MultipartFile createExcel(String resourceName) {
        Workbook workbook = new XSSFWorkbook();
        workbook.createSheet("Sheet1");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        byte[] bytes = out.toByteArray();
        return MultipartFileUtil.toMultipartFile(bytes, resourceName.concat(XLS));
    }


    public static MultipartFile createPowerPoint(String resourceName) {
        XMLSlideShow ppt = new XMLSlideShow();
        ppt.createSlide();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ppt.write(out);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        byte[] bytes = out.toByteArray();
        return MultipartFileUtil.toMultipartFile(bytes, resourceName.concat(PPT));
    }


    public static MultipartFile createDrawio(String resourceName, String content) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(content.getBytes());

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        byte[] bytes = out.toByteArray();
        return MultipartFileUtil.toMultipartFile(bytes, resourceName);
    }


    public static MultipartFile createMind(String resourceName, String content) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(content.getBytes());

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        byte[] bytes = out.toByteArray();
        return MultipartFileUtil.toMultipartFile(bytes, resourceName);
    }
}
