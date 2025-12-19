package fuzs.vehicleupgrade.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ClientConfig implements ConfigCore {
    static final String INVENTORY_CATEGORY = "inventory";

    @Config(category = ServerConfig.BOAT_CATEGORY, description = {
            "Allow rendering held items while rowing.", "The items this applies to are defined by a corresponding tag."
    })
    public boolean holdItemsWhileRowing = true;
    @Config(category = INVENTORY_CATEGORY, description = "The inventory to open first when riding a vehicle.")
    public VehicleInventory defaultVehicleInventory = VehicleInventory.VEHICLE;
    @Config(category = INVENTORY_CATEGORY,
            description = "Press control + the inventory key, or press the inventory key twice to open the player inventory instead of the vehicle inventory.")
    public InventorySwitch switchVehicleInventory = InventorySwitch.CONTROL;
    @Config(category = INVENTORY_CATEGORY,
            description = "Add a button to the player and vehicle inventory for switching in-between.")
    public boolean vehicleInventoryButton = true;
    @Config(category = INVENTORY_CATEGORY,
            description = "Show health and armor attributes for a mob in the corresponding inventory screen.")
    public boolean mobAttributesInInventory = false;
    @Config(description = "When looking down the current mount will begin to fade so what lies below can easily be seen. Does not apply to non-living vehicles.")
    public boolean translucentMount = true;
}
