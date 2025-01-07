package com.soflyit.system.service.impl;

import com.soflyit.common.core.utils.DateUtils;
import com.soflyit.system.api.domain.SysPassword;
import com.soflyit.system.mapper.SysPasswordMapper;
import com.soflyit.system.service.ISysPasswordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 密码设置Service业务层处理
 *
 * @author soflyit
 * @date 2023-04-24 11:25:34
 */
@Service
public class SysPasswordServiceImpl implements ISysPasswordService {
    @Autowired
    private SysPasswordMapper sysPasswordMapper;

    private SysPassword defaultPasswordConfig;


    @Override
    public SysPassword selectSysPasswordById(Long id) {
        return sysPasswordMapper.selectSysPasswordById(id);
    }


    @Override
    public List<SysPassword> selectSysPasswordList(SysPassword sysPassword) {
        return sysPasswordMapper.selectSysPasswordList(sysPassword);
    }


    @Override
    public int insertSysPassword(SysPassword sysPassword) {
        sysPassword.setCreateTime(DateUtils.getNowDate());
        return sysPasswordMapper.insertSysPassword(sysPassword);
    }


    @Override
    public int updateSysPassword(SysPassword sysPassword) {
        sysPassword.setUpdateTime(DateUtils.getNowDate());
        return sysPasswordMapper.updateSysPassword(sysPassword);
    }


    @Override
    public int deleteSysPasswordByIds(Long[] ids) {
        return sysPasswordMapper.deleteSysPasswordByIds(ids);
    }


    @Override
    public int deleteSysPasswordById(Long id) {
        return sysPasswordMapper.deleteSysPasswordById(id);
    }


    @Override
    public SysPassword selectSysPassword() {
        List<SysPassword> passwords = selectSysPasswordList(new SysPassword());
        SysPassword result = defaultPasswordConfig;
        if (CollectionUtils.isNotEmpty(passwords)) {
            result = passwords.get(0);
            processDefaultConfig(result);
        }
        return result;
    }

    private void processDefaultConfig(SysPassword result) {

        if (result.getSingleLogin() == null) {
            result.setSingleLogin(Integer.valueOf(0));
        }
        if (result.getKickType() == null) {
            result.setKickType(Integer.valueOf(0));
        }
        if (result.getStrengthLimit() == null) {
            result.setStrengthLimit(Integer.valueOf(0));
        }
        if (result.getNeedLength() == null) {
            result.setNeedLength(Integer.valueOf(0));
        }
        if (result.getMinLength() == null) {
            result.setNeedLength(Integer.valueOf(8));
        }
        if (result.getNeedDigit() == null) {
            result.setNeedDigit(Integer.valueOf(0));
        }
        if (result.getNeedLowerCase() == null) {
            result.setNeedLowerCase(Integer.valueOf(0));
        }
        if (result.getNeedUpperCase() == null) {
            result.setNeedUpperCase(Integer.valueOf(0));
        }
        if (result.getNeedSpecialCharacter() == null) {
            result.setNeedSpecialCharacter(Integer.valueOf(0));
        }
        if (result.getNotUserName() == null) {
            result.setNotUserName(Integer.valueOf(0));
        }
        if (result.getForceChange() == null) {
            result.setForceChange(Integer.valueOf(0));
        }
        if (result.getLimitFailedCount() == null) {
            result.setLimitFailedCount(Integer.valueOf(5));
        }
    }

    @PostConstruct
    private void initDefaultPasswordConfig() {
        defaultPasswordConfig = new SysPassword();
        defaultPasswordConfig.setId(-1L);
        defaultPasswordConfig.setSingleLogin(Integer.valueOf(0));
        defaultPasswordConfig.setKickType(Integer.valueOf(0));
        defaultPasswordConfig.setStrengthLimit(Integer.valueOf(0));
        defaultPasswordConfig.setNeedLength(Integer.valueOf(0));
        defaultPasswordConfig.setNeedLength(Integer.valueOf(8));
        defaultPasswordConfig.setNeedDigit(Integer.valueOf(0));
        defaultPasswordConfig.setNeedLowerCase(Integer.valueOf(0));
        defaultPasswordConfig.setNeedUpperCase(Integer.valueOf(0));
        defaultPasswordConfig.setNeedSpecialCharacter(Integer.valueOf(0));
        defaultPasswordConfig.setNotUserName(Integer.valueOf(0));
        defaultPasswordConfig.setForceChange(Integer.valueOf(0));
        defaultPasswordConfig.setLimitFailedCount(Integer.valueOf(5));
    }
}
