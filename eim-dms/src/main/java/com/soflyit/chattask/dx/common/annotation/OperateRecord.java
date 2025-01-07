package com.soflyit.chattask.dx.common.annotation;


import com.soflyit.chattask.dx.common.enums.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某个方法需要进行功能字段自动填充处理
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.common.aspect
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-13  18:04
 * @Description:
 * @Version: 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateRecord {

    OperationType.BodyType value();
}
