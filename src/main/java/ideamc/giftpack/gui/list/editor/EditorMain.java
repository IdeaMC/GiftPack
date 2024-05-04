package ideamc.giftpack.gui.list.editor;

import ideamc.giftpack.GiftPackMain;
import ideamc.giftpack.api.GiftPack;
import ideamc.giftpack.api.GiftPackData;
import ideamc.giftpack.configs.Lang;
import ideamc.giftpack.gui.GUIObject;
import ideamc.giftpack.gui.list.Admin;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author xiantiao
 * @date 2024/5/4
 * GiftPack
 */
public class EditorMain implements Listener {
    private static final Inventory defaultInventory;
    private static final Lang.Gui.EditorMain lang = GiftPackMain.getLangConfigManager().gui().editorMain();
    private static final ItemStack REDISPLAY = new ItemStack(lang.SetDisplay().material());
    private static final ItemStack REWARDS = new ItemStack(lang.Rewards().material());
    private static final ItemStack CLOSE = new ItemStack(lang.Close().material());
    private static final ItemStack FILLER = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);

    private static final int SLOT_DISPLAY = 13;
    private static final int SLOT_REDISPLAY = 30;
    private static final int SLOT_REWARDS = 32;
    private static final int SLOT_CLOSE = 53;


    static {
        GUIObject.setDisplayName(lang.SetDisplay().name(), REDISPLAY);
        GUIObject.setDisplayName(lang.Rewards().name(), REWARDS);
        GUIObject.setDisplayName(lang.Close().name(), CLOSE);
        GUIObject.setDisplayName(" ", FILLER);

        defaultInventory = Bukkit.createInventory(new GUIObject.AdminGUI(), 54, lang.title());

        defaultInventory.setItem(SLOT_REDISPLAY, REDISPLAY);
        defaultInventory.setItem(SLOT_REWARDS, REWARDS);
        defaultInventory.setItem(SLOT_CLOSE, CLOSE);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;

        if (!((clickedInventory.getHolder()!=null) && clickedInventory.getHolder() instanceof GUIObject.EditorMainGUI)) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == SLOT_CLOSE) {
            Admin.open(player);
        } else

        if (event.getSlot() == SLOT_DISPLAY) {
            if (event.getClick().isLeftClick()) {
                GiftPack giftPack = GiftPack.getGiftPack(clickedInventory.getItem(SLOT_DISPLAY));
                assert giftPack != null;
                player.getInventory().addItem(giftPack.getOpenItemStack());
            } else
            if (event.getClick().isRightClick()) {
                GiftPack giftPack = GiftPack.getGiftPack(clickedInventory.getItem(SLOT_DISPLAY));
                assert giftPack != null;
                player.getInventory().addItem(giftPack.getDisplayItemStack());
            }
        }
    }

    public void open(@NotNull Player player, @NotNull GiftPack giftPack) {
        // 变量初始化
        Validate.notNull(player);
        Inventory inventory = Bukkit.createInventory(new GUIObject.EditorMainGUI(),54,lang.title());

        // 复制默认背包
        int i = 0;
        for (ItemStack itemStack : defaultInventory) {
            if (itemStack!=null) inventory.setItem(i,itemStack);
            i++;
        }

        // 设置 displayItemStack 物品 到新背包
        ItemStack displayItemStack = giftPack.getOpenItemStack();
        List<String> displayItemStackLore = displayItemStack.getLore();
        if (displayItemStackLore == null) displayItemStackLore = new ArrayList<>();
        displayItemStackLore.addAll(lang.Display().lore());
        displayItemStack.setLore(displayItemStackLore);
        inventory.setItem(SLOT_DISPLAY, displayItemStack);

        // 填充剩下的格子
        for (i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, FILLER);
            }
        }

        player.openInventory(inventory);
    }
}
