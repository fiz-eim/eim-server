package com.soflyit.chattask.dx.modular.share.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.dx.modular.share.domain.entity.FileShareEntity;
import com.soflyit.chattask.dx.modular.share.domain.param.FileShareQueryParam;
import com.soflyit.chattask.dx.modular.share.domain.vo.FileShareVO;

import java.util.List;


/**
 * 文件分享Service接口
 *
 * @author soflyit
 * @date 2023-11-07 16:50:36
 */
public interface FileShareService extends IService<FileShareEntity> {

    List<FileShareVO> list(FileShareQueryParam queryParam);
}
