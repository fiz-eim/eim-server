package com.soflyit.system.service;

import com.soflyit.system.api.domain.SysDictData;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author soflyit
 */
public interface ISysDictDataService {

    List<SysDictData> selectDictDataList(SysDictData dictData);


    String selectDictLabel(String dictType, String dictValue);


    SysDictData selectDictDataById(Long dictCode);


    void deleteDictDataByIds(Long[] dictCodes);


    int insertDictData(SysDictData dictData);


    int insertDictDataInner(SysDictData dictData);


    int updateDictData(SysDictData dictData);

}
