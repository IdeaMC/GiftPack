package ideamc.giftpack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariDataSource;
import ideamc.giftpack.configs.Config;
import ideamc.giftpack.configs.ConfigManager;
import ideamc.giftpack.configs.Lang;
import ideamc.giftpack.dataer.Data;
import ideamc.giftpack.dataer.OptionalTypeAdapter;
import ideamc.giftpack.dataer.sqlite.SQLiter;
import ideamc.giftpack.error.DataError;
import ideamc.giftpack.error.SaveDataError;
import ideamc.giftpack.utils.GiftPack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.*;

/**
 * @author xiantiao
 */
public final class GiftPackMain extends JavaPlugin {

    private static GiftPackMain instance; // 插件实例
    private static ConfigManager<Lang> langConfigManager;
    private static ConfigManager<Config> configConfigManager;
    private static Data data;
    public final static String sign = "GiftPack ";

    @Override
    public void onEnable() {
        instance = this;

        langConfigManager = ConfigManager.create(getDataFolder().toPath(),"lang.yml", Lang.class);
        configConfigManager = ConfigManager.create(getDataFolder().toPath(),"config.yml", Config.class);
        langConfigManager.reloadConfig();
        configConfigManager.reloadConfig();

        if ("SQLiter".equals(configConfigManager.getConfigData().storage().method())) {
            data = new SQLiter();
            data.initialization();
        } else {
            getLogger().severe("unKnow storage method");
            getPluginLoader().disablePlugin(this);
        }

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ideamc.giftpack.gui.Admin(),this);
        pluginManager.registerEvents(new ideamc.giftpack.gui.GiftPackList(),this);

        getCommand("giftpack").setExecutor(new GiftPackCommand());

        //test();
        test3();
    }


    public static void test3() {
        // 注册 ItemStack 类以便 Bukkit API 可以识别它
        ConfigurationSerialization.registerClass(ItemStack.class);

        // 创建一个 ItemStack 数组
        ItemStack[] originalItems = new ItemStack[]{
                new ItemStack(Material.DIAMOND_SWORD, 1),
                new ItemStack(Material.GOLDEN_APPLE, 3),
                new ItemStack(Material.IRON_AXE, 2)
        };

        // 使用 Gson 将 ItemStack[] 转换为 JSON 字符串
        Gson gson = new Gson();
        String json = gson.toJson(serializeItemStackArray(originalItems));

        // 输出 JSON 字符串
        System.out.println("Serialized ItemStack Array: " + json);

        // 从 JSON 字符串中恢复 ItemStack[]
        ItemStack[] deserializedItems = deserializeItemStackArray(gson.fromJson(json, ArrayList.class).toArray());

        // 输出恢复后的 ItemStack[]
        System.out.println("Deserialized ItemStack Array:");
        for (ItemStack item : deserializedItems) {
            System.out.println(item);
        }
    }

    // 将 ItemStack[] 转换为包含 Map<String, Object> 的列表
    public static List<Map<String, Object>> serializeItemStackArray(ItemStack[] items) {
        List<Map<String, Object>> serializedItems = new ArrayList<>();
        for (ItemStack item : items) {
            serializedItems.add(item.serialize());
        }
        return serializedItems;
    }

    // 将包含 Map<String, Object> 的列表转换为 ItemStack[]
    public static ItemStack[] deserializeItemStackArray(Object[] serializedItems) {
        ItemStack[] deserializedItems = new ItemStack[serializedItems.length];
        for (int i = 0; i < serializedItems.length; i++) {
            deserializedItems[i] = ItemStack.deserialize((Map<String, Object>) serializedItems[i]);
        }
        return deserializedItems;
    }




    private void test2() throws InterruptedException {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        Inventory inventory =Bukkit.createInventory(null,54,"test");
        ItemStack i = new ItemStack(Material.GOLDEN_AXE);
        i.setLore(Collections.singletonList("你好"));
        inventory.addItem(i);

        String[] itemStackStrList = new String[54];

        int i2 = 0;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null) itemStackStrList[i2] = itemStack.toString();
        }

        String json = gson.toJson(itemStackStrList);

        getLogger().info("json "+json);




        String[] object = gson.fromJson(json,String[].class);
        Inventory inventory1 = Bukkit.createInventory(null,54,"test");
        ConfigurationSerialization.registerClass(ItemStack.class);
        for (String s : object) {
            if (s!=null) inventory1.addItem();
        }

        Thread.sleep(2000);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.openInventory(inventory1);
        }
    }


    void test() {
        Data data = new SQLiter();

        ItemStack itemStack = new ItemStack(Material.GOLDEN_AXE);
        itemStack.setLore(Collections.singletonList("测试"));

        GiftPack giftPack = new GiftPack("测试礼包",itemStack, UUID.randomUUID());
        giftPack.getInventory().addItem(itemStack);


        GsonBuilder gsonBuilder = new GsonBuilder();
        // 将自定义的 TypeAdapter 注册到 GsonBuilder 中
        gsonBuilder.registerTypeAdapter(Optional.class, new OptionalTypeAdapter());
        // 创建 Gson 对象
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(giftPack.getInventory());
        getLogger().info(json);

        reloadConfig();
        getConfig().get("");

        int uid;
        try {
            uid = data.saveGiftPack(giftPack);
        } catch (DataError | SaveDataError e) {
            throw new RuntimeException(e);
        }

        try {
            GiftPack giftPack2 = data.getGiftPack(uid);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.openInventory(giftPack2.getInventory());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("uid："+ uid);
    }



    @Override
    public void onDisable() {
        ((HikariDataSource)((SQLiter) data).getDataSource()).close();
        instance = null;
    }
    public static GiftPackMain getInstance() {return instance;}
    public static Lang getLangConfigManager(){return langConfigManager.getConfigData();}
    public static Config getConfigConfigManager(){return configConfigManager.getConfigData();}
    public static Data getData() {return data;}
}
