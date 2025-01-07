package com.soflyit.system.service;

import com.soflyit.system.api.domain.SysDictData;
import com.soflyit.system.api.domain.SysDictType;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author soflyit
 */
public interface ISysDictTypeService {

    List<SysDictType> selectDictTypeList(SysDictType dictType);


    List<SysDictType> selectDictTypeAll();


    List<SysDictData> selectDictDataByType(String dictType);


    SysDictType selectDictTypeById(Long dictId);


    SysDictType selectDictTypeByType(String dictType);


    void deleteDictTypeByIds(Long[] dictIds);


    void loadingDictCache();


    void clearDictCache();


    void resetDictCache();


    int insertDictType(SysDictType dictType);


    int insertDictTypeInner(SysDictType dictType);


    int updateDictType(SysDictType dictType);


    String checkDictTypeUnique(SysDictType dictType);


    int updateMenuStatus(SysDictType dict);
}
