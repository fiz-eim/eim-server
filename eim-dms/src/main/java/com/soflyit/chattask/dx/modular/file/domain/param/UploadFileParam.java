package com.soflyit.chattask.dx.modular.file.domain.param;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.modular.folder.my.domain.param
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-13  16:59
 * @Description:
 * @Version: 1.0
 */
@Data
public class UploadFileParam {


    private Long folderParentId;


    private Long[] folderParentIds;


    private String relativePath;


    private String type;


    private String sourceFolderPath;

    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    private MultipartFile[] files;


    private String fileUploadKey;
}
