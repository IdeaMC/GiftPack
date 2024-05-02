package ideamc.giftpack.gui;

import ideamc.giftpack.GiftPackMain;
import ideamc.giftpack.configs.Lang;
import ideamc.giftpack.utils.PAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static ideamc.giftpack.GiftPackMain.getInstance;
import static ideamc.giftpack.GiftPackMain.sign;

/**
 * @author xiantiao
 * @date 2024/4/27
 * GiftPack
 */

// TODO 重写Lang，支持configReload
public class Admin implements InventoryHolder, Listener {
    @Override public Inventory getInventory() {return null;}

    public static void initialization() {        lang = GiftPackMain.getLangConfigManager().gui().admin();

        inventory = Bukkit.createInventory(new Admin(),54, lang.title());

        inventory.setItem(
                Item.slotPackManage, Item.packManage
        );
        inventory.setItem(
                Item.slotCreatePack, Item.createPack
        );
        inventory.setItem(
                Item.slotMyPacks, Item.myPacks
        );

        int i = 0;
        while (i < inventory.getSize()) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, Item.fill);
            }
            i++;
        }
    }

    // Admin GUI
    private static Inventory inventory;

    private static ideamc.giftpack.configs.Lang.GuiTitle.Admin lang;

    @EventHandler
    public void a(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        if (!(inventory.getHolder() instanceof Admin)) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        player.sendMessage("inventory!");
    }

    public static void open(Player player) {
        inventory.getItem(Item.slotPackManage).setLore(lang.PackManage().lore().stream().map(PAPI::to).toList()); // 使用PAPI.to处理每个文字

        player.openInventory(inventory);
    }

    static class Item {
        static ItemStack packManage = new ItemStack(lang.PackManage().material());
        static ItemStack createPack = new ItemStack(Material.REDSTONE_TORCH);
        static ItemStack myPacks = new ItemStack(Material.NETHER_STAR);
        static ItemStack fill = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);

        static {
            setDisplayName(lang.PackManage().name(),packManage);
            setDisplayName("创建礼包",createPack);
            setDisplayName("我的礼包",myPacks);
            setDisplayName(" ",fill);
        }

        static int slotPackManage = lang.PackManage().slot();
        static int slotCreatePack = 13;
        static int slotMyPacks = 15;
    }
    static void setDisplayName(String displayName, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
    }
}
