package fuzs.vehicleupgrade.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ClientConfig implements ConfigCore {
    @Config(description = "Add a button to the player inventory for switching to the inventory of the current vehicle (if available).")
    public VehicleButton vehicleButton = VehicleButton.BUTTON;
    @Config(description = "Allow rendering held items while rowing. The items this applies to are defined by a corresponding tag.")
    public boolean holdItemsWhileRowing = true;
    @Config(
            description = {
                    "Add non-default entity attributes to the targeted entity section on the debug screen.",
                    "Useful for inspecting horse-like animals."
            }
    )
    public boolean debugEntityAttributes = false;

}
