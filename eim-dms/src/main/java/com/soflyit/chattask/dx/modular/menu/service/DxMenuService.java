package com.soflyit.chattask.dx.modular.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.dx.modular.menu.domain.entity.DxMenuEntity;
import com.soflyit.chattask.dx.modular.menu.domain.param.DxMenuAddParam;
import com.soflyit.chattask.dx.modular.menu.domain.param.DxMenuUpdateParam;

import java.util.List;

/**
 * Package: com.soflyit.chattask.dx.modular.dx.service
 *
 * @Description:
 * @date: 2023/12/18 9:58
 * @author: dddgoal@163.com
 */
public interface DxMenuService extends IService<DxMenuEntity> {


    String getPublicZoneFolderId();


    String getPrivateZoneFolderId();


    String getNoValidateFolderId();


    DxMenuEntity getInfo(Long id);


    Boolean addTopMenu(DxMenuAddParam addParam);



    Boolean updateTopMenu(DxMenuUpdateParam editParam);


    Boolean deleteTopMenu(List<Long> ids);
}
