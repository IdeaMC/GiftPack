package ideamc.giftpack.configs;

import ideamc.giftpack.utils.MaterialSerialiser;
import org.bukkit.Material;
import space.arim.dazzleconf.annote.*;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import javax.annotation.processing.SupportedAnnotationTypes;
import java.util.List;

/**
 * @author xiantiao
 * @date 2024/4/27
 * GiftPack
 */

@SuppressWarnings("ALL")
@ConfHeader("# 语言文件 GiftPack Lang zh_cn ver 1.0.0 by xiantiao \n\n")
@ConfSerialisers(MaterialSerialiser.class)
public interface Lang {

    @ConfComments("# 这里是关于GUI的语言设置")
    GuiTitle gui();
    @SubSection
    interface GuiTitle {

        @ConfComments("\n  # 管理主页面")
        Admin admin();

        @SubSection
        interface Admin {
            @ConfDefault.DefaultStrings("GiftPack - admin")
            @ConfComments("# admin主界面标题名字")
            @AnnotationBasedSorter.Order(100)
            String title();

            @AnnotationBasedSorter.Order(110)
            PackManage PackManage();

            @SubSection
            interface PackManage {
                @AnnotationBasedSorter.Order(100)
                @ConfDefault.DefaultStrings("全部礼包")
                String name();
                @AnnotationBasedSorter.Order(110)
                @ConfDefault.DefaultString("GOLDEN_AXE")
                Material material();
                @AnnotationBasedSorter.Order(120)
                @ConfDefault.DefaultStrings({"当前一共有 %giftpack_all_size% 个礼包", " ", "点击我管理礼包"})
                List<String> lore();
                @AnnotationBasedSorter.Order(130)
                @ConfDefault.DefaultInteger(11)
                int slot();
            }


            @AnnotationBasedSorter.Order(110)
            CreatePack CreatePack();

            @SubSection
            interface CreatePack {
                @AnnotationBasedSorter.Order(100)
                @ConfDefault.DefaultStrings("创建礼包")
                String name();
                @AnnotationBasedSorter.Order(110)
                @ConfDefault.DefaultString("REDSTONE_TORCH")
                Material material();
                @AnnotationBasedSorter.Order(120)
                @ConfDefault.DefaultStrings({"当前一共有 %giftpack_all_size% 个礼包", " ", "点击我创建一个新的礼包"})
                List<String> lore();
                @AnnotationBasedSorter.Order(130)
                @ConfDefault.DefaultInteger(13)
                int slot();
            }


            @AnnotationBasedSorter.Order(110)
            MyPacks MyPacks();

            @SubSection
            interface MyPacks {
                @AnnotationBasedSorter.Order(100)
                @ConfDefault.DefaultStrings("我的礼包")
                String name();
                @AnnotationBasedSorter.Order(110)
                @ConfDefault.DefaultString("NETHER_STAR")
                Material material();
                @AnnotationBasedSorter.Order(120)
                @ConfDefault.DefaultStrings({"我的礼包"})
                List<String> lore();
                @AnnotationBasedSorter.Order(130)
                @ConfDefault.DefaultInteger(15)
                int slot();
            }
        }
    }
}
