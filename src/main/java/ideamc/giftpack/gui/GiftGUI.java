package ideamc.giftpack.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author xiantiao
 * @date 2024/5/2
 * GiftPack
 */
public class GiftGUI implements InventoryHolder {
    @Override
    public Inventory getInventory() {
        return null;
    }

    static class AdminGUI extends GiftGUI implements InventoryHolder {
        @Override public Inventory getInventory() {return super.getInventory();}
    }
}
