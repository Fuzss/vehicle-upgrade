package fuzs.vehicleupgrade.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ClientConfig implements ConfigCore {
    static final String INVENTORY_CATEGORY = "inventory";

    @Config(description = "Allow rendering held items while rowing. The items this applies to are defined by a corresponding tag.")
    public boolean holdItemsWhileRowing = true;
    @Config(category = INVENTORY_CATEGORY, description = "The inventory to open first when riding a vehicle.")
    public VehicleInventory defaultVehicleInventory = VehicleInventory.VEHICLE;
    @Config(
            category = INVENTORY_CATEGORY,
            description = "Press control + the inventory key, or press the inventory key twice to open the player inventory instead of the ridden vehicle inventory."
    )
    public InventorySwitch switchVehicleInventory = InventorySwitch.CONTROL;
    @Config(
            category = INVENTORY_CATEGORY,
            description = "Add a button to the player and current vehicle inventory for switching in-between."
    )
    public boolean vehicleInventoryButton = true;
    @Config(
            category = INVENTORY_CATEGORY,
            description = "Show health and armor attributes for a mob in the inventory screen."
    )
    public boolean mobAttributesInInventory = false;
    @Config(
            description = {
                    "Add non-default entity attributes to the targeted entity section on the debug screen.",
                    "Useful for inspecting horse-like animals."
            }
    )
    public boolean debugEntityAttributes = false;
}
