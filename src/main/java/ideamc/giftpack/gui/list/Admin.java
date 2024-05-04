package ideamc.giftpack.gui.list;

import ideamc.giftpack.GiftPackMain;
import ideamc.giftpack.configs.Lang;
import ideamc.giftpack.gui.GUIObject;
import ideamc.giftpack.utils.PAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 40482
 */
public class Admin implements Listener {
    private static final Inventory inventory;
    private static final Lang.Gui.Admin lang = GiftPackMain.getLangConfigManager().gui().admin();

    private static final ItemStack PACK_MANAGE = new ItemStack(lang.PackManage().material());
    private static final ItemStack CREATE_PACK = new ItemStack(lang.CreatePack().material());
    private static final ItemStack MY_PACKS = new ItemStack(lang.MyPacks().material());
    private static final ItemStack FILLER = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);

    private static final int SLOT_PACK_MANAGE = 11; // Adjust as needed
    private static final int SLOT_CREATE_PACK = 13; // Adjust as needed
    private static final int SLOT_MY_PACKS = 15; // Adjust as needed


    static {
        setDisplayName(lang.PackManage().name(), PACK_MANAGE);
        setDisplayName(lang.CreatePack().name(), CREATE_PACK);
        setDisplayName(lang.MyPacks().name(), MY_PACKS);
        setDisplayName(" ", FILLER);

        inventory = Bukkit.createInventory(new GUIObject.AdminGUI(), 54, lang.title());

        inventory.setItem(SLOT_PACK_MANAGE, PACK_MANAGE);
        inventory.setItem(SLOT_CREATE_PACK, CREATE_PACK);
        inventory.setItem(SLOT_MY_PACKS, MY_PACKS);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, FILLER);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;

        if (!((clickedInventory.getHolder()!=null) && clickedInventory.getHolder() instanceof GUIObject.AdminGUI)) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == SLOT_PACK_MANAGE) {
            new GiftPackList().open(player,1);
        }
    }

    public static void open(Player player) {
        setLore(inventory.getItem(SLOT_PACK_MANAGE), lang.PackManage().lore());
        setLore(inventory.getItem(SLOT_CREATE_PACK), lang.CreatePack().lore());
        setLore(inventory.getItem(SLOT_MY_PACKS), lang.MyPacks().lore());

        player.openInventory(inventory);
    }

    private static void setLore(ItemStack itemStack, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore.stream().map(PAPI::to).collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);
    }
    private static void setDisplayName(String displayName, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
    }
}
