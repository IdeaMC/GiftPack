import ideamc.giftpack.dataer.Data;
import ideamc.giftpack.dataer.sqlite.SQLiter;
import ideamc.giftpack.error.DataError;
import ideamc.giftpack.error.SaveDataError;
import ideamc.giftpack.utils.GiftPack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.UUID;

class Scratch {
    public static void main(String[] args) {
        Data data = new SQLiter("C:\\code\\java\\IdeaMC\\Idea\\GiftPack\\src\\main\\resources\\data-test.db");

        data.initialization();


        ItemStack itemStack = new ItemStack(Material.GOLDEN_AXE);
        itemStack.setLore(Collections.singletonList("测试"));

        GiftPack giftPack = new GiftPack("测试礼包",itemStack,UUID.randomUUID());
        giftPack.getInventory().addItem(itemStack);

        int uid;
        try {
            uid = data.saveGiftPack(giftPack);
        } catch (DataError | SaveDataError e) {
            throw new RuntimeException(e);
        }

        System.out.println("uid："+ uid);
    }
}