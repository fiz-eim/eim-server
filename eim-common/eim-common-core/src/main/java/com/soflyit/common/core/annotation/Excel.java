package com.soflyit.common.core.annotation;

import com.soflyit.common.core.utils.poi.ExcelHandlerAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

/**
 * 自定义导出Excel数据注解
 *
 * @author soflyit
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {

    int sort() default Integer.MAX_VALUE;


    String name() default "";


    String dateFormat() default "";


    String readConverterExp() default "";


    String separator() default ",";


    int scale() default -1;


    int roundingMode() default BigDecimal.ROUND_HALF_EVEN;


    ColumnType cellType() default ColumnType.STRING;


    double height() default 14;


    double width() default 16;


    String suffix() default "";


    String defaultValue() default "";


    String prompt() default "";


    String[] combo() default {};


    boolean isExport() default true;


    String targetAttr() default "";


    boolean isStatistics() default false;


    Align align() default Align.AUTO;


    Class<?> handler() default ExcelHandlerAdapter.class;


    String[] args() default {};

    enum Align {
        AUTO(0), LEFT(1), CENTER(2), RIGHT(3);
        private final int value;

        Align(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }


    Type type() default Type.ALL;

    enum Type {
        ALL(0), EXPORT(1), IMPORT(2);
        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    enum ColumnType {
        NUMERIC(0), STRING(1), IMAGE(2);
        private final int value;

        ColumnType(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}
