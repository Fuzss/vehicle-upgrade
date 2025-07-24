package fuzs.vehicleupgrade.neoforge;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.data.tags.ModAttributeTagProvider;
import fuzs.vehicleupgrade.data.tags.ModBlockTagProvider;
import fuzs.vehicleupgrade.data.tags.ModEntityTypeTagProvider;
import fuzs.vehicleupgrade.data.tags.ModItemTagProvider;
import net.neoforged.fml.common.Mod;

@Mod(VehicleUpgrade.MOD_ID)
public class VehicleUpgradeNeoForge {

    public VehicleUpgradeNeoForge() {
        ModConstructor.construct(VehicleUpgrade.MOD_ID, VehicleUpgrade::new);
        DataProviderHelper.registerDataProviders(VehicleUpgrade.MOD_ID,
                ModBlockTagProvider::new,
                ModEntityTypeTagProvider::new,
                ModItemTagProvider::new,
                ModAttributeTagProvider::new);
    }
}
