package ideamc.giftpack.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * @author xiantiao
 * @date 2024/5/2
 * GiftPack
 */
@SuppressWarnings("unchecked")
public class ItemStackSerialiser {
    private final static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting().disableHtmlEscaping()
            .create();

    public static String toJson(ItemStack[] itemStacks) {
        ConfigurationSerialization.registerClass(ItemStack.class);
        return gson.toJson(serializeItemStackArray(itemStacks));
    }

    public static ItemStack[] toItemStacks(String json) {
        ConfigurationSerialization.registerClass(ItemStack.class);
        Object[] serializedItems = gson.fromJson(json, Object[].class);
        if (serializedItems == null) {
            return new ItemStack[0];
        }
        return deserializeItemStackArray(serializedItems);
    }

    public static String toJson(ItemStack itemStack) {
        ConfigurationSerialization.registerClass(ItemStack.class);
        return gson.toJson(serializeItemStack(itemStack));
    }

    public static ItemStack toItemStack(String json) {
        ConfigurationSerialization.registerClass(ItemStack.class);
        return deserializeItemStack(gson.fromJson(json, Map.class));
    }

    // 将 ItemStack[] 转换为包含 Map<String, Object> 的列表
    private static Object[] serializeItemStackArray(ItemStack[] items) {
        Object[] serializedItems = new Object[items.length];
        for (int i = 0; i < items.length; i++) {
            serializedItems[i] = serializeItemStack(items[i]);
        }
        return serializedItems;
    }

    // 将 ItemStack 转换为 Map<String, Object>
    private static Object serializeItemStack(ItemStack item) {
        if (item == null) {
            return null;
        }
        return item.serialize();
    }

    // 将包含 Map<String, Object> 的列表转换为 ItemStack[]
    private static ItemStack[] deserializeItemStackArray(Object[] serializedItems) {
        ItemStack[] deserializedItems = new ItemStack[serializedItems.length];
        for (int i = 0; i < serializedItems.length; i++) {
            deserializedItems[i] = deserializeItemStack((Map<String, Object>) serializedItems[i]);
        }
        return deserializedItems;
    }

    // 将 Map<String, Object> 转换为 ItemStack
    private static ItemStack deserializeItemStack(Map<String, Object> serializedItem) {
        if (serializedItem == null) {
            return null;
        }
        return ItemStack.deserialize(serializedItem);
    }
}
