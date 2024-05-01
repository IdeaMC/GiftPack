package ideamc.giftpack.dataer;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * @author xiantiao
 * @date 2024/5/1
 * GiftPack
 */
public class OptionalTypeAdapter implements JsonSerializer<Optional<?>>, JsonDeserializer<Optional<?>> {

    @Override
    public JsonElement serialize(Optional<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.orElse(null)); // 序列化字段的值
    }

    @Override
    public Optional<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // 反序列化字段的值
        return Optional.ofNullable(context.deserialize(json, typeOfT));
    }
}
