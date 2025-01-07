package com.soflyit.chattask.im.common.web;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 字节文件<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-13 17:00
 */
public class ByteArrayMultipartFile implements MultipartFile {
    private final String name;
    private final String originalFileName;
    @Nullable
    private final String contentType;
    private final byte[] content;

    public ByteArrayMultipartFile(String name, @Nullable byte[] content) {
        this(name, "", null, content);
    }

    public ByteArrayMultipartFile(String name, InputStream contentStream) throws IOException {
        this(name, "", null, FileCopyUtils.copyToByteArray(contentStream));
    }

    public ByteArrayMultipartFile(String name, @Nullable String originalFileName, @Nullable String contentType, @Nullable byte[] content) {
        Assert.hasLength(name, "Name must not be empty");
        this.name = name;
        this.originalFileName = originalFileName != null ? originalFileName : "";
        this.contentType = contentType;
        this.content = content != null ? content : new byte[0];
    }

    public ByteArrayMultipartFile(String name, @Nullable String originalFileName, @Nullable String contentType, InputStream contentStream) throws IOException {
        this(name, originalFileName, contentType, FileCopyUtils.copyToByteArray(contentStream));
    }

    public String getName() {
        return this.name;
    }

    @NonNull
    public String getOriginalFilename() {
        return this.originalFileName;
    }

    @Nullable
    public String getContentType() {
        return this.contentType;
    }

    public boolean isEmpty() {
        return this.content.length == 0;
    }

    public long getSize() {
        return this.content.length;
    }

    public byte[] getBytes() throws IOException {
        return this.content;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    public void transferTo(File dest) throws IOException, IllegalStateException {
        FileCopyUtils.copy(this.content, dest);
    }
}
