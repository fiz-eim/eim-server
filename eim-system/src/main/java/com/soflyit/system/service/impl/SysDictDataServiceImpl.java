package com.soflyit.system.service.impl;

import com.soflyit.common.security.utils.DictUtils;
import com.soflyit.system.api.domain.SysDictData;
import com.soflyit.system.mapper.SysDictDataMapper;
import com.soflyit.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author soflyit
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService {
    @Autowired
    private SysDictDataMapper dictDataMapper;


    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        return dictDataMapper.selectDictDataList(dictData);
    }


    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }


    @Override
    public SysDictData selectDictDataById(Long dictCode) {
        return dictDataMapper.selectDictDataById(dictCode);
    }


    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysDictData data = selectDictDataById(dictCode);
            if (!Boolean.TRUE.equals(data.getInherDict())) {
                dictDataMapper.deleteDictDataById(dictCode);
                List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
                DictUtils.setDictCache(data.getDictType(), dictDatas);
            }
        }
    }


    @Override
    public int insertDictData(SysDictData data) {
        int flagDictValue = dictDataMapper.checkDictValueUnique(data);
        int flagDictLabel = dictDataMapper.checkDictLabelUnique(data);
        if (flagDictValue > 0) {
            return -1;
        } else if (flagDictLabel > 0) {
            return -2;
        } else {
            int row = dictDataMapper.insertDictData(data);
            if (row > 0) {
                List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
                DictUtils.setDictCache(data.getDictType(), dictDatas);
            }
            return row;
        }
    }


    @Override
    public int insertDictDataInner(SysDictData data) {
        int flagDictValue = dictDataMapper.checkDictValueUnique(data);
        int flagDictLabel = dictDataMapper.checkDictLabelUnique(data);
        if (flagDictValue > 0) {
            return -1;
        } else if (flagDictLabel > 0) {
            return -2;
        } else {
            int row = dictDataMapper.insertDictData(data);
            if (row > 0) {
                List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
                DictUtils.setDictCache(data.getDictType(), dictDatas);
            }
            return row;
        }
    }


    @Override
    public int updateDictData(SysDictData data) {
        SysDictData sysDictData = dictDataMapper.selectDictDataById(data.getDictCode());
        int flagDictValue = dictDataMapper.checkDictValueUnique(data);
        int flagDictLabel = dictDataMapper.checkDictLabelUnique(data);
        if (flagDictValue > 0 && !sysDictData.getDictValue().equals(data.getDictValue())) {
            return -1;
        } else if (flagDictLabel > 0 && !sysDictData.getDictLabel().equals(data.getDictLabel())) {
            return -2;
        } else {
            int row = dictDataMapper.updateDictData(data);
            if (row > 0) {
                List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
                DictUtils.setDictCache(data.getDictType(), dictDatas);
            }
            return row;
        }
    }

}
