package ideamc.giftpack.gui;

import ideamc.giftpack.GiftPackMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static ideamc.giftpack.GiftPackMain.sign;

/**
 * @author xiantiao
 * @date 2024/4/27
 * GiftPack
 */

// TODO 重写Lang，支持configReload
public class Admin implements Listener {

    // Admin GUI
    private static final Inventory inventory;

    static {
        inventory = Bukkit.createInventory(null,54, Lang.TITLE);

        inventory.setItem(
                11, Lang.Item.packManage
        );
    }

    @EventHandler
    public void a(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        if (!inventory.getViewers().get(0).getOpenInventory().getTitle().equals(Lang.TITLE)) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == Lang.Item.packManage) {
            player.sendMessage("You clicked in the inventory!");
        }
    }

    public static void open(Player player) {
        player.openInventory(inventory);
    }

    static class Lang {
        static final String TITLE = sign + GiftPackMain.getLangConfigManager().gui().admin().title();

        static class Item {
            static ItemStack packManage = new ItemStack(Material.GOLDEN_AXE);

            static {
                packManage.setLore(Collections.singletonList("礼包管理"));
                packManage.getItemMeta().setDisplayName("");
            }
        }
    }
}
