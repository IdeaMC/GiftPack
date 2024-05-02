package ideamc.giftpack.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * @author xiantiao
 * @date 2024/5/2
 * GiftPack
 */
@Deprecated
public class RowsInventory {
    private final Inventory inventory;
    private final int available;
    private final Direction availableDirection;

    /**
     * 创建一个限制行数的Inventory
     * @param title 容器标题
     * @param available 可用的行数 1~5
     * @param availableDirection 可以使用的位置的开始的方向
     */
     RowsInventory(String title, int available, Direction availableDirection) {
        this.inventory = Bukkit.createInventory(null,54,title);

        if (available > 5 || available < 1) {throw new IllegalArgumentException("available can not be '"+available+"', it is must in 1-5");}

        this.available = available;
        this.availableDirection = availableDirection;
    }

    /**
     * 向可用位置添加物品
     * @param itemStack 要添加的物品
     */
    public void addItem(ItemStack itemStack) {

    }

    /**
     * 向可用位置添加物品
     * @param i 要添加到的位置
     * @param itemStack 要添加的物品
     */
    public void setItem(int i,ItemStack itemStack) {

    }
    /**
     * 删除可用位置的物品
     * @param itemStack 要添加的物品
     * @throws IllegalArgumentException 如果传入的值不在可用位置内，抛出IllegalArgumentException
     */
    public void removeItem(int itemStack) {

    }

    public enum Direction {
        Up, Down, Left, Right
    }
}
