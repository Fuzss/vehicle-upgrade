package fuzs.vehicleupgrade.neoforge;

import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.vehicleupgrade.common.VehicleUpgrade;
import fuzs.vehicleupgrade.common.data.tags.ModAttributeTagsProvider;
import fuzs.vehicleupgrade.common.data.tags.ModBlockTagsProvider;
import fuzs.vehicleupgrade.common.data.tags.ModEntityTypeTagsProvider;
import fuzs.vehicleupgrade.common.data.tags.ModItemTagsProvider;
import net.neoforged.fml.common.Mod;

@Mod(VehicleUpgrade.MOD_ID)
public class VehicleUpgradeNeoForge {

    public VehicleUpgradeNeoForge() {
        ModConstructor.construct(VehicleUpgrade.MOD_ID, VehicleUpgrade::new);
        DataProviderHelper.registerDataProviders(VehicleUpgrade.MOD_ID,
                ModBlockTagsProvider::new,
                ModEntityTypeTagsProvider::new,
                ModItemTagsProvider::new,
                ModAttributeTagsProvider::new);
    }
}
