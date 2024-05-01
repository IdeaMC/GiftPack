package ideamc.giftpack.configs;

import space.arim.dazzleconf.annote.*;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

/**
 * @author xiantiao
 * @date 2024/4/27
 * GiftPack
 */

@SuppressWarnings("unused")
@ConfHeader("# 语言文件 GiftPack Lang zh_cn ver 1.0.0 by xiantiao \n\n")
public interface Lang {

    @ConfComments("# 这里是关于GUI的语言设置")
    GuiTitle gui();
    @SubSection
    interface GuiTitle {

        @ConfComments("\n# 管理主页面")
        Admin admin();

        @SubSection
        interface Admin {
            @ConfDefault.DefaultStrings("GiftPack - admin")
            @ConfComments("\n# admin主界面标题名字")
            String title();
        }
    }
}
