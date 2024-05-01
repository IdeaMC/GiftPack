import ideamc.giftpack.dataer.Data;
import ideamc.giftpack.dataer.sqlite.SQLiter;
import ideamc.giftpack.error.DataError;
import ideamc.giftpack.error.SaveDataError;
import ideamc.giftpack.utils.GiftPack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Collections;
import java.util.UUID;

class Scratch {
    public static void main(String[] args) throws SQLException {
        SQLiter data = new SQLiter("C:\\code\\java\\IdeaMC\\Idea\\GiftPack\\src\\main\\resources\\data.db");

        int uid = data.getUID(123);

        System.out.println("uid："+ uid);
    }
}