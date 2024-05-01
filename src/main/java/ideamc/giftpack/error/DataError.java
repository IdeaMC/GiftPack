package ideamc.giftpack.error;

/**
 * @author xiantiao
 * @date 2024/5/1
 * GiftPack
 */
public class DataError extends Exception {
    Exception error;
    public DataError(Exception error) {
        super(error);
        this.error = error;
    }
    public Exception getError() {
        return error;
    }
}