package com.soflyit.system.mapper;

import com.soflyit.system.api.domain.SysDictType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author soflyit
 */
@Mapper
public interface SysDictTypeMapper {

    List<SysDictType> selectDictTypeList(SysDictType dictType);


    List<SysDictType> selectDictTypeAll();


    SysDictType selectDictTypeById(Long dictId);


    SysDictType selectDictTypeByType(String dictType);


    int deleteDictTypeById(Long dictId);


    int deleteDictTypeByIds(Long[] dictIds);


    int insertDictType(SysDictType dictType);


    int updateDictType(SysDictType dictType);


    SysDictType checkDictTypeUnique(String dictType);
}
