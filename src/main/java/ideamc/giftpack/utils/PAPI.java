package ideamc.giftpack.utils;

import ideamc.giftpack.GiftPackMain;
import ideamc.giftpack.api.GiftPackData;

/**
 * @author xiantiao
 * @date 2024/5/2
 * GiftPack
 */
public class PAPI {
    private static final GiftPackData giftData = GiftPackMain.getData();
    public static String to(String s) {

        s = s.replaceAll("%giftpack_all_size%", String.valueOf(giftData.size()));

        return s;
    }
}
