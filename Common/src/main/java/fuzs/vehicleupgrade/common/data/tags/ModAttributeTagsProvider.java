package fuzs.vehicleupgrade.common.data.tags;

import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.common.api.data.v2.tags.AbstractTagAppender;
import fuzs.puzzleslib.common.api.data.v2.tags.AbstractTagProvider;
import fuzs.vehicleupgrade.common.init.ModRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Optional;
import java.util.stream.Stream;

public class ModAttributeTagsProvider extends AbstractTagProvider<Attribute> {

    public ModAttributeTagsProvider(DataProviderContext context) {
        super(Registries.ATTRIBUTE, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        AbstractTagAppender<Attribute> tag = this.tag(ModRegistry.DEBUG_ATTRIBUTES_ATTRIBUTE_TAG);
        Stream.of(Attributes.ARMOR,
                        Attributes.ARMOR_TOUGHNESS,
                        Attributes.ATTACK_DAMAGE,
                        Attributes.ATTACK_KNOCKBACK,
                        Attributes.FLYING_SPEED,
                        Attributes.JUMP_STRENGTH,
                        Attributes.KNOCKBACK_RESISTANCE,
                        Attributes.MAX_HEALTH,
                        Attributes.MOVEMENT_SPEED,
                        Attributes.SCALE,
                        Attributes.STEP_HEIGHT)
                .map(Holder::unwrapKey)
                .<ResourceKey<Attribute>>mapMulti(Optional::ifPresent)
                .forEach(tag::add);
    }
}
