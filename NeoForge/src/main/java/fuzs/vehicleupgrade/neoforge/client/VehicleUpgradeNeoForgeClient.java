package fuzs.vehicleupgrade.neoforge.client;

import fuzs.vehicleupgrade.common.VehicleUpgrade;
import fuzs.vehicleupgrade.common.client.VehicleUpgradeClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = VehicleUpgrade.MOD_ID, dist = Dist.CLIENT)
public class VehicleUpgradeNeoForgeClient {

    public VehicleUpgradeNeoForgeClient() {
        ClientModConstructor.construct(VehicleUpgrade.MOD_ID, VehicleUpgradeClient::new);
    }
}
