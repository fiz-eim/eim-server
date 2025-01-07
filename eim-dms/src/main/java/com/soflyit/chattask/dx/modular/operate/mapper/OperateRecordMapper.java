package com.soflyit.chattask.dx.modular.operate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.dx.modular.operate.domain.entity.OperateRecordEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文档库的变更记录Mapper接口
 *
 * @author soflyit
 * @date 2023-11-06 14:53:26
 */
@Repository
public interface OperateRecordMapper extends BaseMapper<OperateRecordEntity> {


    List<Long> getLastUpdateByUser(@Param("pageSize") Integer pageSize,@Param("userId") Long userId);
}
