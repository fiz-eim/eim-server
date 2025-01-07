package com.soflyit.common.core.utils.bean;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Bean 工具类
 *
 * @author soflyit
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    private static final int BEAN_METHOD_PROP_INDEX = 3;


    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");


    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");


    public static void copyBeanProp(Object dest, Object src) {
        try {
            copyProperties(src, dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<Method> getSetterMethods(Object obj) {

        List<Method> setterMethods = new ArrayList<Method>();


        Method[] methods = obj.getClass().getMethods();



        for (Method method : methods) {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1)) {
                setterMethods.add(method);
            }
        }

        return setterMethods;
    }



    public static List<Method> getGetterMethods(Object obj) {

        List<Method> getterMethods = new ArrayList<Method>();

        Method[] methods = obj.getClass().getMethods();

        for (Method method : methods) {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0)) {
                getterMethods.add(method);
            }
        }

        return getterMethods;
    }

    public static <T, S> T convertBean(S s, Class<T> tClass) {
        if (s == null) {
            return null;
        }
        try {
            T t = tClass.newInstance();
            BeanUtils.copyProperties(s, t);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static <T, S> List<T> convertList(Collection<S> s, Class<T> tClass) {
        if (CollectionUtils.isEmpty(s)) {
            return Collections.emptyList();
        }
        return s.stream().map(ele -> convertBean(ele, tClass)).collect(Collectors.toList());
    }



    public static boolean isMethodPropEquals(String m1, String m2) {
        return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
    }
}
