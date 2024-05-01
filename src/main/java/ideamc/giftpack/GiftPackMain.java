package ideamc.giftpack;

import com.zaxxer.hikari.HikariDataSource;
import ideamc.giftpack.configs.Config;
import ideamc.giftpack.configs.ConfigManager;
import ideamc.giftpack.configs.Lang;
import ideamc.giftpack.dataer.Data;
import ideamc.giftpack.dataer.sqlite.SQLiter;
import ideamc.giftpack.error.DataError;
import ideamc.giftpack.error.SaveDataError;
import ideamc.giftpack.utils.GiftPack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Collections;
import java.util.UUID;

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

        try {
            GiftPack giftPack = data.getGiftPack(6);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.openInventory(giftPack.getInventory());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //test();
    }


    void test() {
        Data data = new SQLiter();

        ItemStack itemStack = new ItemStack(Material.GOLDEN_AXE);
        itemStack.setLore(Collections.singletonList("测试"));

        GiftPack giftPack = new GiftPack("测试礼包",itemStack, UUID.randomUUID());
        giftPack.getInventory().addItem(itemStack);

        int uid;
        try {
            uid = data.saveGiftPack(giftPack);
        } catch (DataError | SaveDataError e) {
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
