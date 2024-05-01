package ideamc.giftpack.error;

/**
 * @author xiantiao
 * @date 2024/4/30
 * GiftPack
 */
public class SaveDataError extends Exception {
    SaveDataErrorType saveDataErrorType;
    public SaveDataError(SaveDataErrorType saveDataErrorType) {
        super("An error occurred while saving: "+saveDataErrorType);
        this.saveDataErrorType = saveDataErrorType;
    }

    public SaveDataErrorType type() {
        return saveDataErrorType;
    }

    public enum SaveDataErrorType {
        nameNULL,itemStackNULL,creatorNULL,inventoryNULL,CanNotGetUid
    }
}
