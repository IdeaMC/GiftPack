package deserialization;

import com.google.gson.Gson;

/**
 * @author xiantiao
 * @date 2024/5/1
 * GiftPack
 */
public class Main {
    public static void main(String[] args) {
        Class c = new Class("xiantiao");
        c.message("你好");

        String json = new Gson().toJson(c);
        System.out.println(json);

        Class c2 = new Gson().fromJson(json, Class.class);
        c2.message("123");
    }

}
