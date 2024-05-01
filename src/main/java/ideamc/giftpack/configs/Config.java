package ideamc.giftpack.configs;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfHeader;
import space.arim.dazzleconf.annote.SubSection;

/**
 * @author xiantiao
 * @date 2024/4/30
 * GiftPack
 */

@SuppressWarnings("unused")
@ConfHeader("# 配置文件 GiftPack Config zh_cn ver 1.0.0 by xiantiao \n\n")
public interface Config {
    @ConfComments("# 这里是关于GUI的语言设置")
    Data storage();

    @SubSection
    interface Data {
        @ConfComments("\n# 数据储存方式 \n  # 目前支持 \n#\n  # -- SQLiter")
        @ConfDefault.DefaultString("SQLiter")
        String method();
    }
}
