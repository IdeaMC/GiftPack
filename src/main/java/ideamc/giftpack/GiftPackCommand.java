package ideamc.giftpack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author xiantiao
 * @date 2024/4/27
 * GiftPack
 */
public class GiftPackCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player) || args.length == 0) { sender.sendMessage("GiftPack by IdeaMC"); return true;}

        if (args.length == 1) {

            if ("admin".equalsIgnoreCase(args[0])) {
                new ideamc.giftpack.gui.Admin().open(player);
            }

            return true;
        } else

        if (args.length == 2) {

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
