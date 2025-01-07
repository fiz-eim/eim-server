package com.soflyit.chattask.im.system.service.impl;

import com.soflyit.common.redis.service.RedisService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户名服务<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-29 17:08
 */
@Service
public class UserNickNameService {

    private RedisService redisService;

    private static final String userIdNameMapKey = "system:user:nickName";


    public String getNickName(Long userId) {
        if (userId == null) {
            return null;
        }
        return redisService.getCacheMapValue(userIdNameMapKey, String.valueOf(userId));
    }


    public Map<Long, String> getNickNameByIds(List<Long> userIds) {
        Map<Long, String> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIds)) {
            Map<String, String> idNameMap = redisService.getCacheMap(userIdNameMapKey);
            for (Long userId : userIds) {
                String nickName = idNameMap.get(String.valueOf(userId));
                result.put(userId, nickName);
            }
        }
        return result;
    }


    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }
}
