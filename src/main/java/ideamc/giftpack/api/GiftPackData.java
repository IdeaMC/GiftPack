package ideamc.giftpack.api;

import ideamc.giftpack.error.DataError;
import ideamc.giftpack.utils.DefaultGiftPack;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author xiantiao
 * @date 2024/4/30
 * GiftPack
 *
 * <p>
 * if you need throw DataError
 * you can:
 * try {
 *   // code...
 * } catch (<T> e) {
 *     throw new DataError(e);
 * }
 */
public interface GiftPackData {

    /**
     * 通过 UID 获取 GiftPack
     * @param uid GiftPack UID
     * @return 可能null
     */
    GiftPack getGiftPack(int uid);

    /**
     * 通过 UID 获取 GiftPackDisplayItemStack
     * @param uid GiftPack UID
     * @return 可能null
     */
    ItemStack getDisplayItemstackOfUid(int uid);

    /**
     * 获取指定数量的 displayItemStack
     * @param startUid 从uid几开始, 传入0则代表从从数据库中的第一条数据开始
     * @param quantity 一共需要的数量
     * @return 数组长度代表获取到的数量
     */
    Gift[] getGiftOfUid(int startUid, int quantity);

    /**
     * 获取 GiftPack 数量
     * @return 全部
     */
    int size();

    /**
     * 获取 creator 创建的 GiftPack
     *
     * @return 全部
     */
    int size(UUID uuid) throws DataError;

    /**
     * 保存GiftPack
     * @param giftPack 礼包
     * @param uid 为0时分配新的uid
     * @return 当传入uid不为0时，返回传入的的uid | 分配到的新uid
     */
    int saveGiftPack(GiftPack giftPack, int uid);

    void initialization();

    void close();
}
