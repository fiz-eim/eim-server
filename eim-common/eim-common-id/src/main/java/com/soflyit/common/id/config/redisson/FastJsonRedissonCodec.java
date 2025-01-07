package com.soflyit.common.id.config.redisson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.springframework.stereotype.Component;

/**
 * redis序列号<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-09 17:22
 */
@Component
public class FastJsonRedissonCodec extends BaseCodec {

    private Encoder encoder;

    private Decoder decoder;

    @Override
    public Decoder<Object> getValueDecoder() {
        return decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return encoder;
    }

    private void init() {
        encoder = o -> {
            ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
            ByteBufOutputStream bos = new ByteBufOutputStream(out);
            JSON.writeJSONString(bos, o, SerializerFeature.WriteClassName);
            return out;
        };

        decoder = (byteBuf, state) -> JSON.parseObject(new ByteBufInputStream(byteBuf), Object.class);
    }
}
