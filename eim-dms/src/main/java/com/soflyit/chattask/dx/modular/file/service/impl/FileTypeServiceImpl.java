package com.soflyit.chattask.dx.modular.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soflyit.chattask.dx.modular.file.domain.entity.FileTypeEntity;
import com.soflyit.chattask.dx.modular.file.mapper.FileTypeMapper;
import com.soflyit.chattask.dx.modular.file.service.FileTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Package: com.soflyit.chattask.dx.modular.file.service.impl
 *
 * @Description:
 * @date: 2023/11/27 19:39
 * @author: dddgoal@163.com
 */
@Service
@Slf4j
public class FileTypeServiceImpl implements FileTypeService {
    @Resource
    private FileTypeMapper fileTypeMapper;

    @Override
    public List<FileTypeEntity> getAllFileType() {
        LambdaQueryWrapper<FileTypeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(FileTypeEntity::getFileTypeKey,FileTypeEntity::getFileTypeName);
        wrapper.groupBy(FileTypeEntity::getFileTypeKey);
        return fileTypeMapper.selectList(wrapper);
    }

    @Override
    public List<String> getAllByFileType(String[] fileTypeKey) {
        LambdaQueryWrapper<FileTypeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(FileTypeEntity::getFileTypeKey, Arrays.asList(fileTypeKey));
        List<FileTypeEntity> list = fileTypeMapper.selectList(wrapper);

        return list.stream().map(FileTypeEntity::getFileTypeValue).collect(Collectors.toList());
    }
}
