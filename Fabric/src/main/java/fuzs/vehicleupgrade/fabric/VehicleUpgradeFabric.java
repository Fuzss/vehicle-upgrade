package fuzs.vehicleupgrade.fabric;

import fuzs.vehicleupgrade.common.VehicleUpgrade;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class VehicleUpgradeFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(VehicleUpgrade.MOD_ID, VehicleUpgrade::new);
    }
}
