package ideamc.giftpack.api;

import ideamc.giftpack.GiftPackMain;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

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
    static int isGiftPack(ItemStack itemStack) {
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
