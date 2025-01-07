package com.soflyit.common.security.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.List;

/**
 * Jackson 配置：将long类型数据转为字符串类型
 *
 * @author Toney
 * @date 2023-09-14
 */
@ConditionalOnProperty(name = "soflyit.jackson.long2String.enable", havingValue = "true")
public class JacksonConfig {


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {

            jacksonObjectMapperBuilder.serializerByType(Long.class, new Long2StringSerializer());
            jacksonObjectMapperBuilder.serializerByType(Long.TYPE, new Long2StringSerializer());



        };
    }


    static class Long2StringSerializer extends JsonSerializer<Long> {
        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null) {
                gen.writeString(value.toString());
            }
        }
    }


    static class LongArray2StringSerialize extends JsonSerializer<List<Long>> {

        @Override
        public void serialize(List<Long> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            StringBuffer sb = new StringBuffer();
            for (Long str : value) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(str);
            }
            gen.writeString(sb.toString());
        }

        @Override
        public Class<List<Long>> handledType() {
            return super.handledType();
        }
    }
}
