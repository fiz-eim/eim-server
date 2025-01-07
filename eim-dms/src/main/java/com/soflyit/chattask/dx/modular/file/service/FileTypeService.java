package com.soflyit.chattask.dx.modular.file.service;

import com.soflyit.chattask.dx.modular.file.domain.entity.FileTypeEntity;

import java.util.List;

/**
 * Package: com.soflyit.chattask.dx.modular.file.service
 *
 * @Description:
 * @date: 2023/11/27 19:39
 * @author: dddgoal@163.com
 */
public interface FileTypeService {


    List<String> getAllByFileType(String[] fileType);


    List<FileTypeEntity> getAllFileType();
}
