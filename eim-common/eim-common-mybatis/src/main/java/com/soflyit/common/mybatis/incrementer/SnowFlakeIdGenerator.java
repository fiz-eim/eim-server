package com.soflyit.common.mybatis.incrementer;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.soflyit.common.id.util.SnowflakeIdUtil;

/**
 * 生成雪花Id <br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-15 11:02
 */
public class SnowFlakeIdGenerator implements IdentifierGenerator {

    @Override
    public Number nextId(Object entity) {
        return SnowflakeIdUtil.nextId();
    }

}
