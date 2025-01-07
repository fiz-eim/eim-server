package com.soflyit.chattask.dx.modular.storage;

import com.soflyit.chattask.dx.modular.file.domain.entity.OriginalFileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件存储服务 <br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-21 16:10
 */
public interface StorageService {


    String saveFile(String fileName, MultipartFile file);


    InputStream getInputStream(OriginalFileEntity fileEntity);



    OriginalFileEntity multipartFileToEntry(MultipartFile file);

}
