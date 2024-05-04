package ideamc.giftpack.gui.list;

import ideamc.giftpack.GiftPackMain;
import ideamc.giftpack.api.Gift;
import ideamc.giftpack.configs.Lang;
import ideamc.giftpack.api.GiftPackData;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xiantiao
 * @date 2024/4/30
 * GiftPack
 */

// TODO 礼包列表
public class GiftPackList implements Listener {
    private static final GiftPackData data;

    private static final Inventory defaultInventory;
    private static final Lang.Gui.GiftPackList lang = GiftPackMain.getLangConfigManager().gui().giftPackList();

    private static final ItemStack LAST_PAGE = new ItemStack(lang.LastPage().material());
    private static final ItemStack NEXT_PAGE = new ItemStack(lang.NextPage().material());
    private static final ItemStack CLOSE = new ItemStack(lang.Close().material());

    private static final ItemStack FILLER = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);

    private static final int SLOT_LAST_PAGE = 47;
    private static final int SLOT_NEXT_PAGE = 51;
    private static final int SLOT_CLOSE = 53;

    static {
        setDisplayName(lang.LastPage().name(), LAST_PAGE);
        setDisplayName(lang.NextPage().name(), NEXT_PAGE);
        setDisplayName(lang.Close().name(), CLOSE);
        setDisplayName(" ", FILLER);

        setLore(CLOSE, lang.Close().lore());

        data = GiftPackMain.getData();

        defaultInventory = Bukkit.createInventory(new GUIObject.GiftPackListGUI(), 54);

        defaultInventory.setItem(SLOT_LAST_PAGE, LAST_PAGE);
        defaultInventory.setItem(SLOT_NEXT_PAGE, NEXT_PAGE);
        defaultInventory.setItem(SLOT_CLOSE, CLOSE);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;

        int slot = event.getSlot();

        if (!((clickedInventory.getHolder()!=null) && clickedInventory.getHolder() instanceof GUIObject.GiftPackListGUI)) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        if (slot == SLOT_CLOSE) {
            Bukkit.getScheduler().runTask(GiftPackMain.getInstance(), () -> {
                    clickedInventory.getViewers().get(0).closeInventory();
                    Admin.open(player);
            });
        }
    }

    // TODO 自定义页面
    public void open(Player player, int page) {
        Inventory inventory = Bukkit.createInventory(new GUIObject.GiftPackListGUI(),54, lang.title());

        // 复制初始化的背包
        int i = 45;
        while (i<54) {
            inventory.setItem(i,defaultInventory.getItem(i));
            i++;
        }

        // 加载礼包列表
        i = 0;
        for (Gift gift : data.getGiftOfUid(0, 45)) {
            if (gift.getDisplayItemStack() != null && gift.getUid() > 0) {
                ItemStack displayItemStack = gift.getDisplayItemStack();

                List<String> lore = displayItemStack.getItemMeta().getLore();
                if (lore == null) {
                    lore = new ArrayList<>();
                }

                lore.add(" ");
                lore.add("uid: "+gift.getUid());

                displayItemStack.setLore(lore);

                inventory.setItem(i,displayItemStack);
            }
            i++;
        }

        // 填充剩下的格子
        for (i = 0; i < defaultInventory.getSize(); i++) {
            if (defaultInventory.getItem(i) == null) {
                defaultInventory.setItem(i, FILLER);
            }
        }

        setLore(Objects.requireNonNull(defaultInventory.getItem(SLOT_LAST_PAGE)), papi(lang.LastPage().lore(),page));
        setLore(Objects.requireNonNull(defaultInventory.getItem(SLOT_NEXT_PAGE)), papi(lang.NextPage().lore(),page));

        player.openInventory(inventory);
    }
    private List<String> papi(List<String> list, int page) {
        return list.stream()
                .map(s -> s.replaceAll("%giftpack_gui_gpl_page%", String.valueOf(page)))
                .collect(Collectors.toList());
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
