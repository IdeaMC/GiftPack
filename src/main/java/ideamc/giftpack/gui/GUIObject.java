package ideamc.giftpack.gui;

import ideamc.giftpack.utils.PAPI;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

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
    public static class EditorMainGUI extends GUIObject implements InventoryHolder {
        @Override public Inventory getInventory() {return super.getInventory();}
    }

    public static void setLore(ItemStack itemStack, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore.stream().map(PAPI::to).collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);
    }
    public static void setDisplayName(String displayName, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
    }
}
