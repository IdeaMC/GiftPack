package ideamc.giftpack.utils;

import ideamc.giftpack.api.Gift;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author xiantiao
 * @date 2024/5/4
 * GiftPack
 */
public class DefaultGift implements Gift {
    ItemStack itemStack;
    String name;
    int uid;
    public DefaultGift(ItemStack itemStack,String name, int uid) {
        this.itemStack = itemStack;
        this.name = name;
        this.uid = uid;
    }

    @Override
    public ItemStack getDisplayItemStack() {
        return this.itemStack;
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public int getUid() {
        return this.uid;
    }
}
