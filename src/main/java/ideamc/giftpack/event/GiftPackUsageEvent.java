package ideamc.giftpack.event;

import ideamc.giftpack.api.GiftPack;
import ideamc.giftpack.configs.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static ideamc.giftpack.GiftPackMain.getData;

/**
 * @author xiantiao
 * @date 2024/5/4
 * GiftPack
 */
public class GiftPackUsageEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onGiftPackUsage(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType().isAir()) return;

        int giftPackUid = GiftPack.isGiftPack(item);

        if (giftPackUid == 0) {return;}

        GiftPack giftPack = getData().getGiftPack(giftPackUid);

        if (giftPack == null) {
            player.sendMessage("这个礼包不存在！");
            return;
        }

        player.sendMessage("你打开了礼包 "+giftPackUid);

    }
}
