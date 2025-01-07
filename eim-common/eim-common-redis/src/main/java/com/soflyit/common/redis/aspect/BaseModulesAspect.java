package com.soflyit.common.redis.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.soflyit.common.core.annotation.Depart;
import com.soflyit.common.core.annotation.EntityName;
import com.soflyit.common.core.annotation.User;
import com.soflyit.common.core.constant.Constants;
import com.soflyit.common.core.utils.reflect.ReflectUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.domain.BaseEntity;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.soflyit.common.core.annotation.EntityName.*;

/**
 * @author lichao
 * @description id名称转换aop
 * @date 2024-1-10 21:50
 * @Version 1.0
 */
@Aspect
@Slf4j
public class BaseModulesAspect {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private RedisService redisService;

    private static final String JAVA_UTIL_DATE = "java.util.Date";


    @Pointcut("execution(public * com.soflyit..*.*Controller.*(..)) " +
            "&& (@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.RequestMapping))")
    public void excudeService() {
    }


    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch sw = new StopWatch();
        sw.start("执行原始方法");
        Object result = pjp.proceed();
        sw.stop();
        sw.start("转化配置信息");
        result = this.parseModelText(result);
        sw.stop();
        log.debug("注解转化共耗费时间：{}", sw.prettyPrint());
        return result;
    }

    private Object parseModelText(Object result) {
        if (result == null) {
            return null;
        }
        if (result instanceof TableDataInfo) {
            TableDataInfo ajaxResult = (TableDataInfo) result;
            if (ajaxResult.getCode() != Constants.SUCCESS) {
                return result;
            }
            return parseTableData(ajaxResult);
        }
        if (result instanceof AjaxResult) {
            AjaxResult ajaxResult = (AjaxResult) result;
            if (ajaxResult.getCode() != Constants.SUCCESS) {
                return result;
            }
            Object dataObj = ajaxResult.getData();
            if (dataObj instanceof PageInfo) {
                dataObj = parsePageInfo((PageInfo) dataObj);
            }
            if (dataObj instanceof BaseEntity) {
                List<Field> fields = ReflectUtils.getFieldList(dataObj.getClass());
                dataObj = parseBaseEntity((BaseEntity) dataObj, fields);
            }
            ajaxResult.setData(dataObj);
            return ajaxResult;
        }
        return result;
    }

    private Object parseTableData(TableDataInfo dataInfo) {
        List<Object> list = dataInfo.getRows();
        if (list == null || list.isEmpty()) {
            return dataInfo;
        }
        Object first = list.get(0);

        if (!(first instanceof BaseEntity)) {
            return dataInfo;
        }
        List<Field> fields = ReflectUtils.getFieldList(first.getClass());
        dataInfo.setRows(list.stream().map(item -> parseBaseEntity((BaseEntity) item, fields)).collect(Collectors.toList()));
        return dataInfo;
    }


    private Object parsePageInfo(PageInfo info) {
        List<Object> list = info.getList();
        if (list == null || list.isEmpty()) {
            return info;
        }
        Object first = list.get(0);

        if (!(first instanceof BaseEntity)) {
            return info;
        }
        List<Field> fields = ReflectUtils.getFieldList(first.getClass());
        return list.stream().map(item -> parseBaseEntity((BaseEntity) item, fields)).collect(Collectors.toList());
    }

    private Object parseBaseEntity(BaseEntity entity, List<Field> fields) {

        try {
            String jsonStr = objectMapper.writeValueAsString(entity);
            JSONObject json =
                    JSON.parseObject(jsonStr);
            for (Field field : fields) {
                Object value = json.get(field.getName());
                if (value == null) {
                    continue;
                }


                EntityName entityName = AnnotationUtils.getAnnotation(field, EntityName.class);

                if (entityName != null) {
                    processEntityName(entityName, json, field, value, entity);
                } else {
                    Depart d = field.getAnnotation(Depart.class);
                    if (d != null) {
                        Optional.ofNullable(redisService.getCacheMapValue(DEPART_REDIS_KEY, value.toString())).ifPresent(departName -> {
                            json.put(StringUtils.isNotBlank(d.value()) ? d.value() : field.getName() + DEPART_NAME_SUFFIX, departName);
                        });
                    }
                    User u = field.getAnnotation(User.class);
                    if (u != null) {
                        Optional.ofNullable(redisService.getCacheMapValue(USER_REDIS_KEY, value.toString())).ifPresent(nickName -> {
                            json.put(StringUtils.isNotBlank(u.value()) ? u.value() : field.getName() + USER_NAME_SUFFIX, nickName);
                        });
                    }
                }
            }
            return json;
        } catch (JsonProcessingException e) {
            log.error("json解析失败" + e.getMessage(), e);
        }
        return entity;
    }

    private void processEntityName(EntityName entityName, JSONObject json, Field field, Object value, BaseEntity entity) {
        String redisKey = entityName.cacheKey();
        if (StringUtils.isEmpty(redisKey)) {
            log.warn("未配置缓存key, 无法进行数据转换处理");
        } else {
            String fieldName = buildFieldName(entityName, field);
            Optional.ofNullable(redisService.getCacheMapValue(redisKey, value.toString())).ifPresent(entityFieldName -> {
                json.put(fieldName, entityFieldName);
            });
        }
    }

    private String buildFieldName(EntityName entityName, Field field) {
        String result = entityName.fileName();
        if (StringUtils.isEmpty(result)) {
            String suffix = entityName.suffix();
            if (StringUtils.isNotEmpty(suffix)) {
                result = field.getName() + suffix;
            }
        }
        if (StringUtils.isEmpty(result)) {
            result = field.getName() + "__EntityName";
        }
        return result;
    }
}
