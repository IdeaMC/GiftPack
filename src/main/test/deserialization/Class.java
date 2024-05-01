package deserialization;

/**
 * @author xiantiao
 * @date 2024/5/1
 * GiftPack
 */
public class Class {
    private final String name;
    public Class(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void message(String s) {
        System.out.println(name+": "+s);
    }
}
