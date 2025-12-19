package fuzs.vehicleupgrade.fabric.client;

import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.client.VehicleUpgradeClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class VehicleUpgradeFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(VehicleUpgrade.MOD_ID, VehicleUpgradeClient::new);
    }
}
