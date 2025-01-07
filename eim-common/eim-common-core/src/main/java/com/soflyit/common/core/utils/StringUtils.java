package com.soflyit.common.core.utils;

import com.soflyit.common.core.constant.Constants;
import com.soflyit.common.core.text.StrFormatter;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 字符串工具类
 *
 * @author soflyit
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final String NULLSTR = "";


    private static final char SEPARATOR = '_';


    private static final char DASH = '-';


    public static <T> T nvl(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }


    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }


    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }


    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }


    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }


    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }


    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }


    public static boolean isEmpty(String str) {
        return isNull(str) || NULLSTR.equals(str.trim());
    }


    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


    public static boolean isNull(Object object) {
        return object == null;
    }


    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }


    public static boolean isArray(Object object) {
        return isNotNull(object) && object.getClass().isArray();
    }


    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }


    public static String substring(final String str, int start) {
        if (str == null) {
            return NULLSTR;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return NULLSTR;
        }

        return str.substring(start);
    }


    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return NULLSTR;
        }

        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }

        if (end > str.length()) {
            end = str.length();
        }

        if (start > end) {
            return NULLSTR;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }


    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }


    public static String format(String template, Object... params) {
        if (isEmpty(params) || isEmpty(template)) {
            return template;
        }
        return StrFormatter.format(template, params);
    }


    public static boolean ishttp(String link) {
        return StringUtils.startsWithAny(link, Constants.HTTP, Constants.HTTPS, Constants.BASE_URL);
    }


    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        boolean preCharIsUpperCase = true;

        boolean curreCharIsUpperCase = true;

        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1)) {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }


    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }


    public static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();

        if (name == null || name.isEmpty()) {

            return "";
        } else if (!name.contains("_")) {

            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }

        String[] camels = name.split("_");
        for (String camel : camels) {

            if (camel.isEmpty()) {
                continue;
            }

            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }


    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    public static String camelCaseToKebabCase(String s) {
        StringBuffer kebab = new StringBuffer();
        if (isEmpty(s)) {
            return s;
        }
        kebab.append(s.charAt(0));
        for (int i = 1; i < s.length(); i++) {
            if (Character.isUpperCase(s.charAt(i))) {
                kebab.append(DASH);
            }
            kebab.append(s.charAt(i));
        }
        return kebab.toString().toLowerCase();
    }


    public static boolean matches(String str, List<String> strs) {
        if (isEmpty(str) || isEmpty(strs)) {
            return false;
        }
        for (String pattern : strs) {
            if (isMatch(pattern, str)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }


    public static final String padl(final Number num, final int size) {
        return padl(num.toString(), size, '0');
    }


    public static final String padl(final String s, final int size, final char c) {
        final StringBuilder sb = new StringBuilder(size);
        if (s != null) {
            final int len = s.length();
            if (s.length() <= size) {
                for (int i = size - len; i > 0; i--) {
                    sb.append(c);
                }
                sb.append(s);
            } else {
                return s.substring(len - size, len);
            }
        } else {
            for (int i = size; i > 0; i--) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
