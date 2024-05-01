import java.io.Serializable;

/**
 * @author xiantiao
 * @date 2024/5/1
 * GiftPack
 */
class Person implements Serializable {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

