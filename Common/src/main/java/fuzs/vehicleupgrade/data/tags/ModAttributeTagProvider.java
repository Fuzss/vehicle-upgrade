package fuzs.vehicleupgrade.data.tags;

import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ModAttributeTagProvider extends AbstractTagProvider<Attribute> {

    public ModAttributeTagProvider(DataProviderContext context) {
        super(Registries.ATTRIBUTE, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModRegistry.DEBUG_ATTRIBUTES_ATTRIBUTE_TAG)
                .add(Attributes.ARMOR.value(),
                        Attributes.ARMOR_TOUGHNESS.value(),
                        Attributes.ATTACK_DAMAGE.value(),
                        Attributes.ATTACK_KNOCKBACK.value(),
                        Attributes.FLYING_SPEED.value(),
                        Attributes.JUMP_STRENGTH.value(),
                        Attributes.KNOCKBACK_RESISTANCE.value(),
                        Attributes.MAX_HEALTH.value(),
                        Attributes.MOVEMENT_SPEED.value(),
                        Attributes.SCALE.value(),
                        Attributes.STEP_HEIGHT.value());
    }
}
