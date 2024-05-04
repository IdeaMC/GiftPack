package ideamc.giftpack.utils;

import ideamc.giftpack.GiftPackMain;
import ideamc.giftpack.api.GiftPack;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author xiantiao
 * @date 2024/4/30
 * GiftPack
 */
public class DefaultGiftPack implements GiftPack {
    private final Inventory inventory;
    private int uid;
    private ItemStack displayItemStack;
    private final UUID creator;

    /**
     * 创建一个全新的礼包
     * <p>
     * 请记得使用getInventory()获取礼包内容修改，inventory内容不能为空
     * @param displayItemStack 礼包的显示物品
     * @param creator 礼包的创建者 null 时为
     */
    public DefaultGiftPack(@NotNull ItemStack displayItemStack, @NotNull UUID creator) {
        Validate.notNull(displayItemStack, "displayItemStack can not be null");
        this.displayItemStack = displayItemStack;
        this.creator = creator;
        this.inventory = Bukkit.createInventory(null,54,"ideamc.giftpack.utils.GiftPack");
    }


    @Override
    public int getUid() {
        return this.uid;
    }

    @Override
    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override @NotNull @ShallowCopy
    public ItemStack getDisplayItemStack() {
        return this.displayItemStack.clone();
    }

    @Override
    public void setDisplayItemStack(@NotNull ItemStack displayItemStack) {
        this.displayItemStack = displayItemStack;
    }

    @Override @NotNull
    public Inventory getItemRewards() {
        return this.inventory;
    }

    @Override @NotNull @ShallowCopy
    public ItemStack getOpenItemStack() {
        ItemStack itemStack = new ItemStack(this.displayItemStack);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(GiftPackMain.getInstance(), "giftpack-uid"), PersistentDataType.INTEGER, this.uid);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override @NotNull
    public String getDisplayName() {
        return this.displayItemStack.getItemMeta().getDisplayName();
    }

    @NotNull @Override
    public UUID getCreator() {
        return this.creator;
    }
}