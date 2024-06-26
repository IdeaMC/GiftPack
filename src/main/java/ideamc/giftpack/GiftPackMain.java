package ideamc.giftpack;

import ideamc.giftpack.api.GiftPack;
import ideamc.giftpack.configs.Config;
import ideamc.giftpack.configs.ConfigManager;
import ideamc.giftpack.configs.Lang;
import ideamc.giftpack.api.GiftPackData;
import ideamc.giftpack.dataer.SQLiter;
import ideamc.giftpack.utils.DefaultGiftPack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.UUID;

/**
 * @author xiantiao
 */
public final class GiftPackMain extends JavaPlugin {

    private static GiftPackMain instance; // 插件实例
    private static ConfigManager<Lang> langConfigManager;
    private static ConfigManager<Config> configConfigManager;
    private static GiftPackData giftPackData;

    @Override
    public void onEnable() {
        instance = this;

        langConfigManager = ConfigManager.create(getDataFolder().toPath(),"lang.yml", Lang.class);
        configConfigManager = ConfigManager.create(getDataFolder().toPath(),"config.yml", Config.class);
        langConfigManager.reloadConfig();
        configConfigManager.reloadConfig();

        if ("SQLiter".equals(configConfigManager.getConfigData().storage().method())) {
            giftPackData = new SQLiter();
            giftPackData.initialization();
        } else {
            getLogger().severe("unKnow storage method");
            getPluginLoader().disablePlugin(this);
        }

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ideamc.giftpack.gui.list.Admin(),this);
        pluginManager.registerEvents(new ideamc.giftpack.gui.list.GiftPackList(),this);
        pluginManager.registerEvents(new ideamc.giftpack.gui.list.editor.EditorMain(),this);
        pluginManager.registerEvents(new ideamc.giftpack.gui.list.editor.RewardEditing(),this);

        pluginManager.registerEvents(new ideamc.giftpack.event.GiftPackUsageEvent(),this);
        //ideamc.giftpack.gui.list.Admin.initialize();

        getCommand("giftpack").setExecutor(new GiftPackCommand());
    }

    public static void test(int uid) {
        GiftPackData giftPackData = new SQLiter();

        ItemStack itemStack = new ItemStack(Material.GOLDEN_AXE);
        itemStack.setLore(Collections.singletonList("测试"));

        GiftPack giftPack = new DefaultGiftPack(itemStack, UUID.randomUUID());
        giftPack.getItemRewards().setItem(4,itemStack);

        uid = giftPackData.saveGiftPack(giftPack,uid);

        try {
            GiftPack giftPack2 = giftPackData.getGiftPack(uid);

            if (giftPack2 == null) return;

            Thread.sleep(1000);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.openInventory(giftPack2.getItemRewards());
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        GiftPackMain.getInstance().getLogger().info("uid："+ uid);
    }



    @Override
    public void onDisable() {

        giftPackData.close();
        instance = null;
    }
    public static GiftPackMain getInstance() {return instance;}
    public static Lang getLangConfigManager(){return langConfigManager.getConfigData();}
    public static Config getConfigConfigManager(){return configConfigManager.getConfigData();}
    public static GiftPackData getData() {return giftPackData;}

}
