package ideamc.giftpack.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author xiantiao
 * @date 2024/5/2
 * GiftPack
 */
public class GUIObject implements InventoryHolder {
    @Override
    public Inventory getInventory() {
        return null;
    }

    public static class AdminGUI extends GUIObject implements InventoryHolder {
        @Override public Inventory getInventory() {return super.getInventory();}
    }
    public static class GiftPackListGUI extends GUIObject implements InventoryHolder {
        @Override public Inventory getInventory() {return super.getInventory();}
    }
}
