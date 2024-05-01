package ideamc.giftpack.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author xiantiao
 * @date 2024/4/30
 * GiftPack
 */
public class GiftPack {
    private final Inventory inventory;
    public int uid;
    public String displayName;
    public ItemStack displayItemStack;
    public UUID creator;

    /**
     * 创建一个全新的礼包
     * <p>
     * 请记得使用getInventory()获取礼包内容修改，inventory内容不能为空
     * @param displayName 礼包的显示名字
     * @param displayItemStack 礼包的显示物品
     * @param creator 礼包的创建者
     */
    public GiftPack(String displayName, ItemStack displayItemStack, UUID creator) {
        this.displayName = displayName;
        this.displayItemStack = displayItemStack;
        this.creator = creator;
        this.inventory = Bukkit.createInventory(null,54,"ideamc.giftpack.utils.GiftPack");
    }

//    /**
//     *
//     * @param inventory
//     * @param displayName
//     * @param creator
//     * @param displayItemStack
//     */
//    public GiftPack(Inventory inventory, String displayName, UUID creator, ItemStack displayItemStack) {
//        this.displayName = displayName;
//        this.creator = creator;
//        this.displayItemStack = displayItemStack;
//        this.inventory = Bukkit.createInventory(null,54,"ideamc.giftpack.utils.GiftPack");
//        this.inventory.addItem(inventory.getContents());
//    }
    public Inventory getInventory() {
        return inventory;
    }
}
