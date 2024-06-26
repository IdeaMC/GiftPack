package ideamc.giftpack.configs;

import ideamc.giftpack.utils.MaterialSerialiser;
import org.bukkit.Material;
import space.arim.dazzleconf.annote.*;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

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
    Gui gui();
    @SubSection
    interface Gui {

        @ConfComments("\n  # 管理主页面")
        Admin admin();
        @SubSection
        interface Admin {
            @ConfDefault.DefaultString("GiftPack - admin")
            @ConfComments("# admin主界面标题名字")
            @AnnotationBasedSorter.Order(100)
            String title();

            @AnnotationBasedSorter.Order(110)
            PackManage PackManage();

            @SubSection
            interface PackManage {
                @AnnotationBasedSorter.Order(100)
                @ConfDefault.DefaultString("全部礼包")
                String name();
                @AnnotationBasedSorter.Order(110)
                @ConfDefault.DefaultString("GOLDEN_AXE")
                Material material();
                @AnnotationBasedSorter.Order(120)
                @ConfDefault.DefaultStrings({"当前一共有 %giftpack_all_size% 个礼包", " ", "点击我管理礼包"})
                List<String> lore();
            }


            @AnnotationBasedSorter.Order(120)
            CreatePack CreatePack();

            @SubSection
            interface CreatePack {
                @AnnotationBasedSorter.Order(100)
                @ConfDefault.DefaultString("创建礼包")
                String name();
                @AnnotationBasedSorter.Order(110)
                @ConfDefault.DefaultString("REDSTONE_TORCH")
                Material material();
                @AnnotationBasedSorter.Order(120)
                @ConfDefault.DefaultStrings({"当前一共有 %giftpack_all_size% 个礼包", " ", "点击我创建一个新的礼包"})
                List<String> lore();
            }


            @AnnotationBasedSorter.Order(130)
            MyPacks MyPacks();

            @SubSection
            interface MyPacks {
                @AnnotationBasedSorter.Order(100)
                @ConfDefault.DefaultString("我的礼包")
                String name();
                @AnnotationBasedSorter.Order(110)
                @ConfDefault.DefaultString("NETHER_STAR")
                Material material();
                @AnnotationBasedSorter.Order(120)
                @ConfDefault.DefaultStrings({"我的礼包"})
                List<String> lore();
            }
        }


        @ConfComments("\n  # 礼包管理主页面")
        GiftPackList giftPackList();
        @SubSection
        interface GiftPackList {
            @ConfDefault.DefaultString("GiftPack - giftPackList")
            @ConfComments("# giftPackList主界面标题名字")
            @AnnotationBasedSorter.Order(100)
            String title();

            @AnnotationBasedSorter.Order(110)
            LastPage LastPage();
            @SubSection
            interface LastPage {
                @AnnotationBasedSorter.Order(100)
                @ConfDefault.DefaultString("上一页")
                String name();

                @AnnotationBasedSorter.Order(110)
                @ConfDefault.DefaultString("FIREWORK_ROCKET")
                Material material();

                @AnnotationBasedSorter.Order(120)
                @ConfDefault.DefaultStrings({"你当前在 %giftpack_gui_gpl_page% 页"})
                List<String> lore();
            }

            @AnnotationBasedSorter.Order(120)
            NextPage NextPage();
            @SubSection
            interface NextPage {
                @AnnotationBasedSorter.Order(100)
                @ConfDefault.DefaultString("下一页")
                String name();

                @AnnotationBasedSorter.Order(110)
                @ConfDefault.DefaultString("FIREWORK_ROCKET")
                Material material();

                @AnnotationBasedSorter.Order(120)
                @ConfDefault.DefaultStrings({"你当前在 %giftpack_gui_gpl_page% 页"})
                List<String> lore();
            }

            @AnnotationBasedSorter.Order(130)
            CLOSE Close();
            @SubSection
            interface CLOSE {
                @AnnotationBasedSorter.Order(100)
                @ConfDefault.DefaultString("关闭")
                String name();

                @AnnotationBasedSorter.Order(110)
                @ConfDefault.DefaultString("FEATHER")
                Material material();

                @AnnotationBasedSorter.Order(120)
                @ConfDefault.DefaultStrings({"返回到管理主界面"})
                List<String> lore();
            }
        }






        @ConfComments("\n  # 管理主页面")
        EditorMain editorMain();
        @SubSection
        interface EditorMain {
            @ConfDefault.DefaultString("GiftPack - editor")
            @ConfComments("# editor主界面标题名字")
            @AnnotationBasedSorter.Order(100)
            String title();

            @AnnotationBasedSorter.Order(110)
            SetDisplay SetDisplay();

            @SubSection
            interface SetDisplay {
                @AnnotationBasedSorter.Order(100)
                @ConfDefault.DefaultString("设置礼包名字")
                String name();
                @AnnotationBasedSorter.Order(110)
                @ConfDefault.DefaultString("NAME_TAG")
                Material material();
                @AnnotationBasedSorter.Order(120)
                @ConfDefault.DefaultStrings({"会覆盖物品礼包名字"})
                List<String> lore();
            }


            @AnnotationBasedSorter.Order(120)
            Rewards Rewards();

            @SubSection
            interface Rewards {
                @AnnotationBasedSorter.Order(100)
                @ConfDefault.DefaultString("编辑奖励")
                String name();
                @AnnotationBasedSorter.Order(110)
                @ConfDefault.DefaultString("CHEST")
                Material material();
                @AnnotationBasedSorter.Order(120)
                @ConfDefault.DefaultStrings({"编辑当前礼包的奖励"})
                List<String> lore();
            }

            @AnnotationBasedSorter.Order(140)
            Display Display();
            @SubSection
            interface Display {
                @AnnotationBasedSorter.Order(120)
                @ConfKey("addLore")
                @ConfDefault.DefaultStrings({"左键获取可以使用的礼包","右键获取Display物品"})
                List<String> lore();
            }

            @AnnotationBasedSorter.Order(140)
            Close Close();
            @SubSection
            interface Close {
                @AnnotationBasedSorter.Order(100)
                @ConfDefault.DefaultString("关闭")
                String name();

                @AnnotationBasedSorter.Order(110)
                @ConfDefault.DefaultString("FEATHER")
                Material material();

                @AnnotationBasedSorter.Order(120)
                @ConfDefault.DefaultStrings({"返回到管理主界面"})
                List<String> lore();
            }
        }
    }
}
