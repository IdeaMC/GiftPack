package ideamc.giftpack.api;

import ideamc.giftpack.GiftPackMain;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static ideamc.giftpack.GiftPackMain.getData;

/**
 * @author xiantiao
 * @date 2024/5/3
 * GiftPack
 */
public interface GiftPack {
    /**
     * 判断物品是否为GiftPack
     * @param itemStack 物品
     * @return 0为不是 其他数字为礼包的uid
     */
    static int isGiftPack(@NotNull ItemStack itemStack) {
        Validate.notNull(itemStack);
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(GiftPackMain.getInstance(), "giftpack-uid");
        if (container.has(key, PersistentDataType.INTEGER)) {
            Integer integer = container.get(key, PersistentDataType.INTEGER);
            if (integer == null) return 0;
            return integer;
        } else {
            return 0;
        }
    }

    static GiftPack getGiftPack(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) return null;
        int uid = isGiftPack(itemStack);
        if (uid != 0) {
            return getData().getGiftPack(uid);
        }return null;
    }

//    /**
//     *
//     * @param giftPack 物品
//     */
//    static ItemStack getGiftPack(GiftPack giftPack) throws IllegalAccessError {
//        Validate.notNull(giftPack);
//        if (giftPack.getUid() == 0) throw new IllegalAccessError("giftPack uid can not be 0");
//
//        ItemStack item = new ItemStack(giftPack.getDisplayItemStack());
//
//        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
//        NamespacedKey key = new NamespacedKey(GiftPackMain.getInstance(), "giftpack-uid");
//        ItemMeta meta = item.getItemMeta();
//        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, uid);
//        item.setItemMeta(meta);
//
//        return item;
//    }

    int getUid();
    void setUid(int uid);

    /**
     * 获取礼包显示的物品
     */
    @NotNull ItemStack getDisplayItemStack();

    /**
     * 设置礼包显示的物品
     */
    void setDisplayItemStack(ItemStack displayItemStack);

    /**
     * 获取 礼包 包含的物品
     * 可以直接使用这个操作内容
     * @return NotNull
     */
    @NotNull Inventory getItemRewards();

    /**
     * 获取打开礼包的物品
     * 顾名思义，会返回一个物品，玩家使用这个物品交互，会打开这个礼包
     *
     * @implNote 应该通过 DisplayItemStack 添加 PDC 判定
     *           返回时应该浅拷贝
     */
    ItemStack getOpenItemStack();

    /**
     * 获取礼包的名字
     * @implNote 从 DisplayItemStack 获取名字
     */
    @NotNull String getDisplayName();

    /**
     * 获取礼包创建者
     */
    @NotNull UUID getCreator();
}
