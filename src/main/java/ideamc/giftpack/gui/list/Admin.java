package ideamc.giftpack.gui.list;

import ideamc.giftpack.GiftPackMain;
import ideamc.giftpack.configs.Lang;
import ideamc.giftpack.gui.GUIObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

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
        GUIObject.setDisplayName(lang.PackManage().name(), PACK_MANAGE);
        GUIObject.setDisplayName(lang.CreatePack().name(), CREATE_PACK);
        GUIObject.setDisplayName(lang.MyPacks().name(), MY_PACKS);
        GUIObject.setDisplayName(" ", FILLER);

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
        GUIObject.setLore(Objects.requireNonNull(inventory.getItem(SLOT_PACK_MANAGE)), lang.PackManage().lore());
        GUIObject.setLore(Objects.requireNonNull(inventory.getItem(SLOT_CREATE_PACK)), lang.CreatePack().lore());
        GUIObject.setLore(Objects.requireNonNull(inventory.getItem(SLOT_MY_PACKS)), lang.MyPacks().lore());

        player.openInventory(inventory);
    }
}
