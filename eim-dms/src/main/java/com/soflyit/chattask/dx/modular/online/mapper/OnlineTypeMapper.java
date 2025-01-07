package com.soflyit.chattask.dx.modular.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.dx.modular.online.domain.entity.OnlineTypeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 可在线创建文档类型Mapper接口
 *
 * @author soflyit
 * @date 2023-11-06 14:35:44
 */
@Repository
@Mapper
public interface OnlineTypeMapper extends BaseMapper<OnlineTypeEntity> {

}
