package ideamc.giftpack;

import ideamc.giftpack.api.GiftPack;
import ideamc.giftpack.gui.list.Admin;
import ideamc.giftpack.gui.list.editor.EditorMain;
import ideamc.giftpack.utils.DefaultGiftPack;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static ideamc.giftpack.GiftPackMain.getData;
import static ideamc.giftpack.GiftPackMain.test;

/**
 * @author xiantiao
 * @date 2024/4/27
 * GiftPack
 */
public class GiftPackCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player) || args.length == 0) { sender.sendMessage("GiftPack by IdeaMC"); return true;}

        if (args.length == 1) {

            if ("admin".equalsIgnoreCase(args[0])) {
                Admin.open(player);
            }

            return true;
        } else

        if (args.length == 2) {

            if ("test".equalsIgnoreCase(args[0])) {
                test(Integer.parseInt(args[1]));
            }
            if ("open".equalsIgnoreCase(args[0])) {
                GiftPack giftPack = getData().getGiftPack(Integer.parseInt(args[1]));
                if (giftPack == null) {
                    player.sendMessage("该礼包不存在");
                    return true;
                }
                new EditorMain().open(player, giftPack);
            }

            return true;
        } else

        if (args.length == 3) {

            return true;
        } else

        if (args.length == 4) {

            return true;
        }

        return true;
    }
}
