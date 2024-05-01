package ideamc.giftpack.dataer;

import ideamc.giftpack.error.DataError;
import ideamc.giftpack.error.SaveDataError;
import ideamc.giftpack.utils.GiftPack;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Base64;
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
public interface Data {

    /**
     * 通过 GiftPack UID获取
     * @param i GiftPack UID
     * @return 可能null
     */
    GiftPack getGiftPack(int i);

    /**
     * 获取 GiftPack 数量
     * @return 全部
     */
    int size() throws SQLException, DataError;

    /**
     * 获取 creator 创建的 GiftPack
     *
     * @return 全部
     */
    int size(UUID uuid) throws DataError;

    /**
     * 保存新的GiftPack到数据库
     * @param giftPack
     * @return 该GiftPack分配的UID，返回0
     */
    int saveGiftPack(GiftPack giftPack) throws DataError, SaveDataError;

    /**
     * 覆盖某个uid的GiftPack
     * @param giftPack 礼包
     * @param uid 可以是不存在的uid
     */
    void saveGiftPack(GiftPack giftPack, int uid) throws SaveDataError;

    void initialization();

    void close();

    static String base64(String input) {
        if (input == null) return null;
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }
}
