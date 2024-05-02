package ideamc.giftpack.utils;

import org.bukkit.Material;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

/**
 * @author xiantiao
 * @date 2024/5/2
 * GiftPack
 */
public class MaterialSerialiser implements ValueSerialiser<Material> {

    @Override
    public Class<Material> getTargetClass() {
        return Material.class;
    }

    @Override
    public Material deserialise(FlexibleType flexibleType) throws BadValueException {
        return Material.valueOf(flexibleType.getString());

    }

    @Override
    public String serialise(Material material, Decomposer decomposer) {
        return material.name();
    }
}
