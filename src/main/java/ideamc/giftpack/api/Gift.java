package ideamc.giftpack.api;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author xiantiao
 * @date 2024/5/4
 * GiftPack
 * <p>
 * 这玩意就是一个阉割的 GiftPack.java
 * 方便开发 储存 GiftPack 的  DisplayItemStack & name
 */
public interface Gift {
    ItemStack getDisplayItemStack();
    String getDisplayName();
    int getUid();
}
