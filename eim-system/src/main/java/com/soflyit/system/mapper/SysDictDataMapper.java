package com.soflyit.system.mapper;

import com.soflyit.system.api.domain.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author soflyit
 */
public interface SysDictDataMapper {

    List<SysDictData> selectDictDataList(SysDictData dictData);


    List<SysDictData> selectDictDataByType(String dictType);


    String selectDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);


    SysDictData selectDictDataById(Long dictCode);


    int countDictDataByType(String dictType);


    int deleteDictDataById(Long dictCode);


    int deleteDictDataByIds(Long[] dictCodes);


    int insertDictData(SysDictData dictData);


    int updateDictData(SysDictData dictData);


    int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);


    int checkDictValueUnique(SysDictData data);


    int checkDictLabelUnique(SysDictData data);

}
