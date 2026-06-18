package fuzs.vehicleupgrade.common.data.tags;

import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.common.api.data.v2.tags.AbstractTagProvider;
import fuzs.vehicleupgrade.common.init.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.ItemIds;
import net.minecraft.world.item.Item;

public class ModItemTagsProvider extends AbstractTagProvider<Item> {

    public ModItemTagsProvider(DataProviderContext context) {
        super(Registries.ITEM, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModRegistry.HOLDABLE_WHILE_ROWING_ITEM_TAG).add(ItemIds.FILLED_MAP);
    }
}
