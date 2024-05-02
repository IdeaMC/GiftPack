package ideamc.giftpack.dataer;

import ideamc.giftpack.error.DataError;
import ideamc.giftpack.error.SaveDataError;
import ideamc.giftpack.utils.GiftPack;

import java.sql.SQLException;
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
     * 通过 GiftPack UID获取 GiftPack
     * @param i GiftPack UID
     * @return 可能null
     */
    GiftPack getGiftPack(int i) throws SQLException;

    /**
     * 通过 GiftPack UID获取 GiftPackItemstack
     * @param i GiftPack UID
     * @return 可能null
     */
    GiftPack getGiftPackItemstack(int i) throws SQLException;

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
     */
    int saveGiftPack(GiftPack giftPack, int uid) throws SaveDataError;

    void initialization();

    void close();
}
